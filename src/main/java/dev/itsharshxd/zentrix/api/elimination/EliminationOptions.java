package dev.itsharshxd.zentrix.api.elimination;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What an elimination should and should not do besides removing the player from the match.
 *
 * <p>Zentrix's ordinary death pipeline pays out kills, currency, first blood and statistics because
 * a player earned them. An elimination that comes from a rule rather than from a fight normally
 * should not, which is exactly what these switches are for: {@link #scenario()} starts from
 * "nobody earned anything" and lets you turn back on only what your rule really awards.
 *
 * <p>The parts that keep the match consistent — removing the player from their team, converting them
 * to a spectator, marking a team eliminated, firing the addon events, checking the win condition —
 * are not optional and always happen.
 *
 * <h2>Example</h2>
 * <pre>{@code
 * // An objective win: the losing teams are out, but nobody gets kill credit for it.
 * EliminationOptions options = EliminationOptions.scenario()
 *         .announce(true)
 *         .messageKey("scenarios.dragon-rush.eliminated")
 *         .build();
 * }</pre>
 *
 * @since 1.6.0
 */
public final class EliminationOptions {

    private final EliminationCause cause;
    private final UUID attacker;
    private final boolean awardKillCredit;
    private final boolean awardCurrency;
    private final boolean awardFirstBlood;
    private final boolean recordStatistics;
    private final boolean spawnCorpse;
    private final boolean recordForRevival;
    private final boolean dropInventory;
    private final boolean announce;
    private final String messageKey;
    private final boolean checkWinCondition;

    private EliminationOptions(Builder builder) {
        this.cause = builder.cause;
        this.attacker = builder.attacker;
        this.awardKillCredit = builder.awardKillCredit;
        this.awardCurrency = builder.awardCurrency;
        this.awardFirstBlood = builder.awardFirstBlood;
        this.recordStatistics = builder.recordStatistics;
        this.spawnCorpse = builder.spawnCorpse;
        this.recordForRevival = builder.recordForRevival;
        this.dropInventory = builder.dropInventory;
        this.announce = builder.announce;
        this.messageKey = builder.messageKey;
        this.checkWinCondition = builder.checkWinCondition;
    }

    /**
     * The preset for a rule-driven elimination: no kill credit, no kill reward, no first blood, no
     * corpse and no revival record, but statistics, announcement and the win-condition check intact.
     */
    @NotNull
    public static Builder scenario() {
        return new Builder()
                .cause(EliminationCause.SCENARIO)
                .awardKillCredit(false)
                .awardCurrency(false)
                .awardFirstBlood(false)
                .recordStatistics(true)
                .spawnCorpse(false)
                .recordForRevival(false)
                .dropInventory(false)
                .announce(true)
                .checkWinCondition(true);
    }

    /** The preset for an administrative removal: nothing is awarded and nothing is announced. */
    @NotNull
    public static Builder administrative() {
        return scenario()
                .cause(EliminationCause.ADMINISTRATIVE)
                .recordStatistics(false)
                .announce(false);
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public EliminationCause cause() {
        return cause;
    }

    /** The player credited with causing this, when the caller wants one recorded. */
    @NotNull
    public java.util.Optional<UUID> attacker() {
        return java.util.Optional.ofNullable(attacker);
    }

    /** Whether the attacker's kill counter goes up. */
    public boolean awardKillCredit() {
        return awardKillCredit;
    }

    /** Whether kill and death currency rewards are paid out. */
    public boolean awardCurrency() {
        return awardCurrency;
    }

    /** Whether this elimination may claim the match's first-blood reward. */
    public boolean awardFirstBlood() {
        return awardFirstBlood;
    }

    /** Whether the death is written to the eliminated player's persistent profile. */
    public boolean recordStatistics() {
        return recordStatistics;
    }

    /** Whether a lootable corpse is left behind. */
    public boolean spawnCorpse() {
        return spawnCorpse;
    }

    /** Whether teammates may revive the eliminated player afterwards. */
    public boolean recordForRevival() {
        return recordForRevival;
    }

    /** Whether the eliminated player's inventory is dropped where they stood. */
    public boolean dropInventory() {
        return dropInventory;
    }

    /** Whether the match is told somebody was eliminated. */
    public boolean announce() {
        return announce;
    }

    /**
     * The locale key of the announcement, which receives a {@code {victim}} placeholder.
     *
     * <p>Empty falls back to Zentrix's ordinary elimination message.
     */
    @NotNull
    public java.util.Optional<String> messageKey() {
        return java.util.Optional.ofNullable(messageKey);
    }

    /**
     * Whether the win condition is checked once the whole request is done.
     *
     * <p>This is the only win-condition check an elimination makes. Removing a player does not run
     * one of its own, so a batch that eliminates the second-to-last team ends the match here, once,
     * after every player and team in the request has been through the pipeline — which is what
     * keeps the elimination announcements ahead of the winner announcement.
     *
     * <p>Turning it off means no synchronous check at all. The match will still end, because the
     * playing state re-checks the win condition every second, but it ends on that tick rather than
     * inside the call, and the eliminated player is no longer identified to it — the final defeat
     * goes unrecorded. Switch it off only when the caller runs its own check straight afterwards,
     * for instance because it is still mutating the match. Defaults to {@code true}.
     */
    public boolean checkWinCondition() {
        return checkWinCondition;
    }

    /** Fluent builder for {@link EliminationOptions}. */
    public static final class Builder {

        private EliminationCause cause = EliminationCause.PLUGIN;
        private UUID attacker;
        private boolean awardKillCredit;
        private boolean awardCurrency;
        private boolean awardFirstBlood;
        private boolean recordStatistics = true;
        private boolean spawnCorpse;
        private boolean recordForRevival;
        private boolean dropInventory;
        private boolean announce = true;
        private String messageKey;
        private boolean checkWinCondition = true;

        @NotNull
        public Builder cause(@NotNull EliminationCause cause) {
            this.cause = cause;
            return this;
        }

        @NotNull
        public Builder attacker(@Nullable UUID attacker) {
            this.attacker = attacker;
            return this;
        }

        @NotNull
        public Builder awardKillCredit(boolean value) {
            this.awardKillCredit = value;
            return this;
        }

        @NotNull
        public Builder awardCurrency(boolean value) {
            this.awardCurrency = value;
            return this;
        }

        @NotNull
        public Builder awardFirstBlood(boolean value) {
            this.awardFirstBlood = value;
            return this;
        }

        @NotNull
        public Builder recordStatistics(boolean value) {
            this.recordStatistics = value;
            return this;
        }

        @NotNull
        public Builder spawnCorpse(boolean value) {
            this.spawnCorpse = value;
            return this;
        }

        @NotNull
        public Builder recordForRevival(boolean value) {
            this.recordForRevival = value;
            return this;
        }

        @NotNull
        public Builder dropInventory(boolean value) {
            this.dropInventory = value;
            return this;
        }

        @NotNull
        public Builder announce(boolean value) {
            this.announce = value;
            return this;
        }

        @NotNull
        public Builder messageKey(@Nullable String messageKey) {
            this.messageKey = messageKey == null || messageKey.isBlank() ? null : messageKey;
            return this;
        }

        /**
         * Whether the request checks the win condition when it is done. Defaults to on, and
         * switching it off leaves the match to end on the playing state's next one-second tick.
         *
         * @see EliminationOptions#checkWinCondition()
         */
        @NotNull
        public Builder checkWinCondition(boolean value) {
            this.checkWinCondition = value;
            return this;
        }

        @NotNull
        public EliminationOptions build() {
            return new EliminationOptions(this);
        }
    }
}
