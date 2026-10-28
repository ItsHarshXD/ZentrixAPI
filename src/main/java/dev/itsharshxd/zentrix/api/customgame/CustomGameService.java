package dev.itsharshxd.zentrix.api.customgame;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Player-hosted custom games: listing, joining and moderating them.
 *
 * <p>Hosts draft and publish games through the custom games menu, when their perk tier allows
 * {@link dev.itsharshxd.zentrix.api.perk.Perk#CUSTOM_GAMES_CREATE} (since 1.10.0); this service exposes the
 * published games and the same join, cancel and terminate operations the menus use, so an addon
 * can build its own listings or moderation tools. Each change of state fires
 * {@link dev.itsharshxd.zentrix.api.events.customgame.CustomGameStateChangeEvent}; a successful
 * publication fires {@link dev.itsharshxd.zentrix.api.events.customgame.CustomGamePublishEvent}.
 *
 * <p>Every method must be called on the main thread. Returned games are snapshots.
 *
 * @since 1.8.0
 */
public interface CustomGameService {

    /** Whether custom games are enabled on this server. */
    boolean isEnabled();

    /** Every published game that has not started yet, soonest first. */
    @NotNull
    List<CustomGameInfo> getUpcomingGames();

    /** Every game that is not over yet - upcoming or running - in no particular order. */
    @NotNull
    List<CustomGameInfo> getActiveGames();

    /** The game with this ID, while it is not over. */
    @NotNull
    Optional<CustomGameInfo> getGame(@NotNull UUID customGameId);

    /** The games a player hosts that are not over yet. */
    @NotNull
    List<CustomGameInfo> getGamesHostedBy(@NotNull UUID hostId);

    /** The custom game a runtime lobby or match belongs to, if it is one. */
    @NotNull
    Optional<CustomGameInfo> getGame(@NotNull ZentrixGame runtime);

    /**
     * Puts a player - or, for a party leader, their whole party - into one specific custom game,
     * exactly as choosing it in the custom games menu would.
     * <p>
     * A password-protected game requires its password unless the player is its host; it is checked
     * here, so pass what the player typed. Main thread only.
     * </p>
     *
     * @param password the password the player gave, or null when they gave none
     * @return the outcome; {@link JoinResult#JOINING} means the join was accepted and is underway
     */
    @NotNull
    JoinResult join(@NotNull Player player, @NotNull UUID customGameId, @Nullable String password);

    /**
     * Calls off a game that has not started, as an administrator would: its waiting players are
     * told and returned to the lobby, and its arena is released.
     *
     * @return whether the game was called off
     */
    boolean cancel(@NotNull UUID customGameId);

    /**
     * Ends a running custom game's match, as an administrator would. No winner is declared and no
     * reward is paid.
     *
     * @return whether the match was ended
     */
    boolean terminate(@NotNull UUID customGameId);

    /** The outcome of a join request. */
    enum JoinResult {
        /** Accepted; the player, or their party, is on the way in. */
        JOINING,
        /** Custom games are disabled. */
        DISABLED,
        /** The game no longer exists or no longer accepts players. */
        UNAVAILABLE,
        /** The player is already in a game. */
        ALREADY_IN_GAME,
        /** The game's deathmatch is configured incorrectly. */
        DEATHMATCH_MISCONFIGURED,
        /** Only the party leader can take the party into a game. */
        NOT_PARTY_LEADER,
        /** The party does not fit. */
        PARTY_TOO_LARGE,
        /** The game is full. */
        FULL,
        /** The game's arena is missing. */
        SOURCE_MISSING,
        /** A join for this player is already underway. */
        ALREADY_JOINING,
        /** The game is password-protected and no password was given. */
        PASSWORD_REQUIRED,
        /** The password given was wrong. */
        WRONG_PASSWORD
    }
}
