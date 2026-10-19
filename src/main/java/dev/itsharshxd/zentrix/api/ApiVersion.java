package dev.itsharshxd.zentrix.api;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A ZentrixAPI contract version, {@code MAJOR.MINOR.PATCH}.
 *
 * <p>{@link #CURRENT} is the version of the API classes on the classpath. The running plugin
 * reports the same value through {@link ZentrixAPI#getAPIVersion()}, because Zentrix ships these
 * classes itself.
 *
 * <p>Compatibility follows semantic versioning: an addon built for {@code 1.6.0} runs on any
 * {@code 1.x} release at or above {@code 1.6.0}, and on no {@code 2.x} release. Missing minor or
 * patch numbers read as {@code 0}, so {@code "1.6"} is {@code 1.6.0}.
 *
 * @param major incompatible contract changes
 * @param minor additions that keep older addons working
 * @param patch fixes that change no contract
 * @since 1.8.0
 */
public record ApiVersion(int major, int minor, int patch) implements Comparable<ApiVersion> {

    /** The version of the API classes this addon was compiled and is running against. */
    public static final ApiVersion CURRENT = new ApiVersion(1, 9, 0);

    private static final Pattern FORMAT = Pattern.compile("(\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?");

    public ApiVersion {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("Version numbers cannot be negative");
        }
    }

    /**
     * Reads a version string.
     *
     * @param version {@code MAJOR}, {@code MAJOR.MINOR} or {@code MAJOR.MINOR.PATCH}; surrounding
     *                whitespace and a leading {@code v} are ignored
     * @return the version, or empty when the text is not a version
     */
    @NotNull
    public static Optional<ApiVersion> parse(@Nullable String version) {
        if (version == null) {
            return Optional.empty();
        }
        String text = version.trim();
        if (text.startsWith("v") || text.startsWith("V")) {
            text = text.substring(1);
        }
        Matcher matcher = FORMAT.matcher(text);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        try {
            return Optional.of(new ApiVersion(
                    Integer.parseInt(matcher.group(1)),
                    matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2)),
                    matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3))));
        } catch (NumberFormatException tooLarge) {
            return Optional.empty();
        }
    }

    /**
     * Whether an addon requiring {@code required} can run on {@code current}.
     *
     * @param required the version an addon needs
     * @param current  the version installed
     * @return how the two relate
     */
    @NotNull
    public static Compatibility check(@Nullable String required, @Nullable String current) {
        Optional<ApiVersion> needed = parse(required);
        Optional<ApiVersion> installed = parse(current);
        if (needed.isEmpty() || installed.isEmpty()) {
            return Compatibility.MALFORMED;
        }
        return installed.get().supports(needed.get());
    }

    /**
     * How this installed version relates to a version an addon requires.
     *
     * @param required the version an addon needs
     * @return {@link Compatibility#COMPATIBLE} when an addon requiring it can run here
     */
    @NotNull
    public Compatibility supports(@NotNull ApiVersion required) {
        if (major != required.major) {
            return Compatibility.INCOMPATIBLE_MAJOR;
        }
        return compareTo(required) >= 0 ? Compatibility.COMPATIBLE : Compatibility.OUTDATED;
    }

    /** Whether this version is the same as or newer than {@code other}. */
    public boolean isAtLeast(@NotNull ApiVersion other) {
        return compareTo(other) >= 0;
    }

    @Override
    public int compareTo(@NotNull ApiVersion other) {
        if (major != other.major) {
            return Integer.compare(major, other.major);
        }
        if (minor != other.minor) {
            return Integer.compare(minor, other.minor);
        }
        return Integer.compare(patch, other.patch);
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }

    /** How a required version relates to the installed one. */
    public enum Compatibility {
        /** The installed API offers everything the required version promises. */
        COMPATIBLE,
        /** Same major version, but older than required: update Zentrix. */
        OUTDATED,
        /** A different major version: the contracts are not compatible in either direction. */
        INCOMPATIBLE_MAJOR,
        /** One of the two strings is not a version. */
        MALFORMED;

        /** Whether the addon may run. */
        public boolean isCompatible() {
            return this == COMPATIBLE;
        }
    }
}
