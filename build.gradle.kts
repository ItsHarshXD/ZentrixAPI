plugins {
    java
    `maven-publish`
}

group = "com.github.ItsHarshXD"
version = "1.0.0"

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
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.9-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    compileOnly("org.jetbrains:annotations:24.1.0")
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