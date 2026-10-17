package dev.itsharshxd.zentrix.api.platform;

/**
 * Whether a capability can be used right now, and if not, why.
 *
 * @since 1.8.0
 */
public enum CapabilityState {
    /** Usable now. */
    AVAILABLE,
    /** Starting up; ask again later. */
    INITIALIZING,
    /** Supported by this installation, but switched off by its configuration. */
    DISABLED,
    /** Not part of the installed edition. No addon can unlock it. */
    RESTRICTED,
    /** Needs a plugin that is missing, disabled or too old. */
    DEPENDENCY_MISSING,
    /** Tried to start and failed. */
    FAILED,
    /** Not known to this installation, for example an id from a newer release. */
    UNKNOWN;

    /** Whether the capability can be used now. */
    public boolean isAvailable() {
        return this == AVAILABLE;
    }

    /**
     * Whether the capability may become available without changing the installation's plugins,
     * edition or version: true only while it is still starting.
     */
    public boolean isTemporary() {
        return this == INITIALIZING;
    }
}
