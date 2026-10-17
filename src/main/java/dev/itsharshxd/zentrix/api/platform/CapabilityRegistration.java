package dev.itsharshxd.zentrix.api.platform;

import dev.itsharshxd.zentrix.api.addon.Registration;
import org.jetbrains.annotations.NotNull;

/**
 * A capability an addon reports through {@link PlatformService#registerCapability}.
 *
 * <p>The registration ends when it is unregistered or when its owner is disabled, whichever comes
 * first. Unregistering twice is harmless.
 *
 * @since 1.8.0
 */
public interface CapabilityRegistration extends Registration {

    /** The registered capability id. */
    @NotNull
    String id();
}
