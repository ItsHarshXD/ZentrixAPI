import java.io.PrintWriter
import java.io.StringWriter
import java.util.jar.JarFile
import java.util.spi.ToolProvider

plugins {
    java
    `maven-publish`
}

group = "com.github.ItsHarshXD"
version = "1.13.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven("https://jitpack.io")
}

val baselineApi by configurations.creating

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.9-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    compileOnly("org.jetbrains:annotations:24.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")
    testImplementation("io.papermc.paper:paper-api:1.21.9-R0.1-SNAPSHOT")
    baselineApi("com.github.ItsHarshXD:ZentrixAPI:1.9.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("verifyBinaryCompatibility") {
    group = "verification"
    description = "Checks that every public baseline API declaration still exists in this build."
    dependsOn(tasks.jar)
    doLast {
        val oldJar = baselineApi.singleFile
        val newJar = tasks.jar.get().archiveFile.get().asFile
        val javap = ToolProvider.findFirst("javap").orElseThrow()
        val support = configurations.compileClasspath.get().asPath

        fun declarations(jar: File, className: String): Set<String> {
            val output = StringWriter()
            val exit = javap.run(PrintWriter(output), PrintWriter(output),
                "-public", "-classpath", jar.absolutePath + File.pathSeparator + support, className)
            check(exit == 0) { "javap failed for $className: $output" }
            return output.toString().lineSequence().map(String::trim)
                .filter { it.endsWith(";") && (it.startsWith("public ") || it.startsWith("protected ")) }
                .map { it.replace(" abstract ", " ").replace(" default ", " ") }
                .toSet()
        }

        val failures = mutableListOf<String>()
        JarFile(oldJar).use { baseline ->
            baseline.entries().asSequence()
                .filter { !it.isDirectory && it.name.endsWith(".class") && it.name != "module-info.class" }
                .map { it.name.removeSuffix(".class").replace('/', '.') }
                .forEach { className ->
                    val current = declarations(newJar, className)
                    declarations(oldJar, className).filterNot(current::contains)
                        .forEach { failures += "$className: $it" }
                }
        }
        check(failures.isEmpty()) { "Binary-incompatible API declarations:\n" + failures.joinToString("\n") }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        addStringOption("Xdoclint:none", "-quiet")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "com.github.ItsHarshXD"
            artifactId = "ZentrixAPI"
            
            from(components["java"])

            pom {
                name.set("ZentrixAPI")
                description.set("Developer API for creating Zentrix addons")
                url.set("https://github.com/ItsHarshXD/ZentrixAPI")

                licenses {
                    license {
                        name.set("All Rights Reserved")
                    }
                }

                developers {
                    developer {
                        id.set("itsharshxd")
                        name.set("ItsHarshXD")
                    }
                }

                scm {
                    connection.set("scm:git:github.com/ItsHarshXD/ZentrixAPI.git")
                    developerConnection.set("scm:git:ssh://github.com/ItsHarshXD/ZentrixAPI.git")
                    url.set("https://github.com/ItsHarshXD/ZentrixAPI")
                }
            }
        }
    }
}
