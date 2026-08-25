package dev.itsharshxd.zentrix.api.identity;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The face a player wears while they are masked: a name and, optionally, a skin.
 *
 * <p>An identity is presentation only. It never replaces the player's real profile, UUID, name,
 * statistics or anything Zentrix stores about them — it is what other clients are told, and nothing
 * more. That is what makes a mask safe to drop at any moment and impossible to persist by accident.
 *
 * <p>The skin is supplied the way Mojang serves it: a base64 {@code textures} property value and the
 * signature that came with it. Leaving both out yields a nameless-skin mask, where the client falls
 * back to the default Steve/Alex model derived from the alias.
 *
 * @param alias         the name every other client sees; 1-16 characters of {@code [A-Za-z0-9_]}
 * @param skinValue     the base64 {@code textures} property value, or null for the default skin
 * @param skinSignature the signature Mojang issued for {@code skinValue}, or null when unsigned
 * @since 1.9.0
 */
public record MaskedIdentity(
        @NotNull String alias, @Nullable String skinValue, @Nullable String skinSignature) {

    /** The longest name a vanilla client accepts in a player-info entry. */
    public static final int MAXIMUM_ALIAS_LENGTH = 16;

    public MaskedIdentity {
        if (alias == null || alias.isBlank()) {
            throw new IllegalArgumentException("A masked identity needs an alias");
        }
        alias = alias.trim();
        if (alias.length() > MAXIMUM_ALIAS_LENGTH) {
            throw new IllegalArgumentException(
                    "Alias '" + alias + "' is longer than " + MAXIMUM_ALIAS_LENGTH + " characters");
        }
        if (!alias.matches("[A-Za-z0-9_]+")) {
            throw new IllegalArgumentException("Alias '" + alias
                    + "' must only contain letters, digits or underscores");
        }
        if (skinValue != null && skinValue.isBlank()) {
            skinValue = null;
        }
        if (skinSignature != null && skinSignature.isBlank()) {
            skinSignature = null;
        }
        // A signature without the value it signs is meaningless and would be rejected by the client.
        if (skinValue == null) {
            skinSignature = null;
        }
    }

    /** A mask that only changes the name, leaving the client to derive a default skin. */
    @NotNull
    public static MaskedIdentity ofAlias(@NotNull String alias) {
        return new MaskedIdentity(alias, null, null);
    }

    /** A mask with an unsigned skin, which clients accept in offline and proxied setups. */
    @NotNull
    public static MaskedIdentity of(@NotNull String alias, @Nullable String skinValue) {
        return new MaskedIdentity(alias, skinValue, null);
    }

    /** Whether this identity carries a skin of its own. */
    public boolean hasSkin() {
        return skinValue != null;
    }

    @NotNull
    public Optional<String> skin() {
        return Optional.ofNullable(skinValue);
    }

    @NotNull
    public Optional<String> signature() {
        return Optional.ofNullable(skinSignature);
    }

    /** The alias in the form aliases are compared by, which is case-insensitively. */
    @NotNull
    public String normalizedAlias() {
        return alias.toLowerCase(Locale.ROOT);
    }

    /** The same identity under a different name, keeping the skin. */
    @NotNull
    public MaskedIdentity withAlias(@NotNull String newAlias) {
        return Objects.equals(alias, newAlias)
                ? this
                : new MaskedIdentity(newAlias, skinValue, skinSignature);
    }

    /** The same alias wearing a different skin. */
    @NotNull
    public MaskedIdentity withSkin(@Nullable String value, @Nullable String signature) {
        return new MaskedIdentity(alias, value, signature);
    }
}
