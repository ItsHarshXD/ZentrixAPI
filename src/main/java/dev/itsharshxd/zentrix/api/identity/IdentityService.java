package dev.itsharshxd.zentrix.api.identity;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Packet-level identity masking: what other clients are told a player is called and looks like.
 *
 * <p>Everything this service does is presentation. The masked player keeps their real UUID, their
 * real profile, their real name on the server, their statistics and every stored record; only the
 * packets leaving the server carry the {@linkplain MaskedIdentity alias and skin} instead. Nothing
 * is ever written to a profile, a head, a corpse, an item or a database, which is what makes a mask
 * removable at any instant and impossible to leak into the next match.
 *
 * <p>Zentrix routes its own player-facing text through this service, so a mask reaches nametags, the
 * tab list, chat, death and elimination messages, scoreboards, GUIs, teammate trackers, corpses,
 * player heads and command suggestions without the caller doing anything else. Console output and
 * debug logging deliberately keep the real name.
 *
 * <h2>Example</h2>
 * <pre>{@code
 * IdentityService identities = ZentrixAPI.get().getIdentityService();
 *
 * // Put a player behind an alias for the length of a match.
 * IdentityHandle handle = identities.mask(
 *         this, game, player, MaskedIdentity.ofAlias("Player_7"));
 *
 * // Render somebody's name the way the match should see it.
 * String shown = identities.getDisplayName(target.getUniqueId());
 *
 * // And take it off again; Zentrix would do this on its own at the end of the match.
 * handle.release();
 * }</pre>
 *
 * <p>Every method is safe to call from the main thread. {@link #getDisplayName(UUID)} and the
 * {@code mask*} text helpers are also safe from other threads, because they only read the alias
 * table.
 *
 * @since 1.6.0
 */
public interface IdentityService {

    /**
     * Whether the packet layer needed for masking is available on this server.
     *
     * <p>False means every masking call is a no-op and names are shown unchanged, which is what a
     * caller should check before promising players anonymity.
     */
    boolean isSupported();

    // ==========================================
    // Applying and removing
    // ==========================================

    /**
     * Puts a player behind an alias and, when the identity carries one, a skin.
     *
     * <p>Applying a mask to an already masked player replaces the previous one and releases its
     * handle. The mask survives respawning, revival, dimension transfers and reconnecting: Zentrix
     * re-sends it whenever the client would otherwise see the real profile again.
     *
     * @param owner    the plugin asking for the mask; its shutdown removes the mask again
     * @param game     the match the mask belongs to; it is removed when that match ends
     * @param player   the player to mask
     * @param identity the face to wear
     * @return the handle, or an already-released handle when masking is unsupported
     */
    @NotNull
    IdentityHandle mask(
            @NotNull Plugin owner,
            @NotNull ZentrixGame game,
            @NotNull Player player,
            @NotNull MaskedIdentity identity);

    /**
     * Removes a player's mask and restores their real profile and skin to everybody.
     *
     * <p>Idempotent, and safe for a player who is offline: the restoration is queued and enforced
     * when they next connect.
     *
     * @return true when a mask was removed
     */
    boolean unmask(@NotNull UUID playerId);

    /**
     * Removes every mask belonging to one match.
     *
     * @return how many masks were removed
     */
    int unmaskAll(@NotNull ZentrixGame game);

    /**
     * Removes every mask a plugin applied, across every match.
     *
     * @return how many masks were removed
     */
    int unmaskAll(@NotNull Plugin owner);

    /**
     * Re-sends a player's mask to everybody who can see them.
     *
     * <p>Zentrix already does this after respawning, revival, world changes and reconnects. Call it
     * yourself only after something outside Zentrix re-sent the real profile.
     */
    void refresh(@NotNull Player player);

    // ==========================================
    // Queries
    // ==========================================

    boolean isMasked(@Nullable UUID playerId);

    /** The face a player is currently wearing, empty when they are not masked. */
    @NotNull
    Optional<MaskedIdentity> getIdentity(@Nullable UUID playerId);

    /** The player hiding behind an alias, empty when nobody is. Compared case-insensitively. */
    @NotNull
    Optional<UUID> resolveAlias(@Nullable String alias);

    /** Every masked player of one match. */
    @NotNull
    Collection<UUID> getMasked(@NotNull ZentrixGame game);

    /** Every masked player on the server. */
    @NotNull
    Collection<UUID> getMasked();

    // ==========================================
    // Rendering
    // ==========================================

    /**
     * The name a player should be shown under: their alias while masked, their real name otherwise.
     *
     * <p>This is the one call every piece of player-facing text needs. It never returns null, and
     * falls back to the last known real name for a player who is offline.
     */
    @NotNull
    String getDisplayName(@Nullable UUID playerId);

    /** The name a player should be shown under. */
    @NotNull
    String getDisplayName(@Nullable Player player);

    /**
     * Replaces every masked player's real name in a piece of text with their alias.
     *
     * <p>For text that was already assembled from names — a death message, a scoreboard line, a
     * formatted chat line — where the individual names are no longer separable. Whole words only, so
     * a name that happens to be a substring of another word is left alone.
     */
    @NotNull
    String maskText(@Nullable String text);

    /** The component form of {@link #maskText(String)}, applied to every literal part. */
    @NotNull
    Component maskText(@Nullable Component text);

    /**
     * Rewrites a player head so it shows the masked owner's alias and skin instead of theirs.
     *
     * <p>The item is copied, never edited in place, and an item that is not a head or whose owner is
     * not masked is returned unchanged. Use this wherever a head is about to be shown to players;
     * the stored item keeps the real owner, so nothing false is ever persisted.
     */
    @NotNull
    ItemStack maskHead(@Nullable ItemStack head);
}
