package dev.itsharshxd.zentrix.api.scenario.hook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The gameplay decision points Zentrix consults active scenarios about.
 *
 * <p>Every request below is scoped to one game: it is only raised for players, blocks and entities
 * that belong to the match the scenario was activated for, so a scenario running in one arena can
 * never see or change anything happening in another.
 *
 * <p>Requests whose fields are documented as mutable may be edited in place and then
 * {@linkplain HookOutcome#pass() passed}, which lets several scenarios refine the same request
 * before Zentrix applies it. Returning {@link HookOutcome#replace(Object)} or
 * {@link HookOutcome#cancel()} instead ends the consultation immediately.
 *
 * @since 1.6.0
 */
public final class GameplayHooks {

    private GameplayHooks() {
    }

    /**
     * What a broken block drops. Replacing supplies the complete drop list; cancelling drops
     * nothing at all.
     */
    public static final GameplayHook<BlockDropRequest, List<ItemStack>> BLOCK_DROPS =
            hook("zentrix:block-drops", BlockDropRequest.class);

    /**
     * What a killed entity drops. Replacing supplies the complete drop list; cancelling drops
     * nothing at all.
     */
    public static final GameplayHook<EntityDropRequest, List<ItemStack>> ENTITY_DROPS =
            hook("zentrix:entity-drops", EntityDropRequest.class);

    /**
     * How much damage a player takes. Replacing sets the final amount; cancelling prevents the
     * damage.
     */
    public static final GameplayHook<DamageRequest, Double> PLAYER_DAMAGE =
            GameplayHook.of("zentrix:player-damage", DamageRequest.class, Double.class);

    /**
     * What happens when a player in the match dies. Replacing with {@code true} tells Zentrix the
     * scenario handled the death itself and its own elimination handling must be skipped;
     * cancelling suppresses the drops only.
     */
    public static final GameplayHook<DeathRequest, Boolean> PLAYER_DEATH =
            GameplayHook.of("zentrix:player-death", DeathRequest.class, Boolean.class);

    /**
     * The result of a crafting operation. Replacing substitutes the crafted item; cancelling blocks
     * the recipe.
     */
    public static final GameplayHook<CraftRequest, ItemStack> CRAFT_RESULT =
            GameplayHook.of("zentrix:craft-result", CraftRequest.class, ItemStack.class);

    /**
     * Whether the match is over. Replacing with {@code true} ends the game, with {@code false}
     * keeps it running; cancelling also keeps it running. This is the hook an alternative win
     * condition installs.
     */
    public static final GameplayHook<WinConditionRequest, Boolean> WIN_CONDITION =
            GameplayHook.of("zentrix:win-condition", WinConditionRequest.class, Boolean.class);

    /**
     * Where the deathmatch happens. Replacing supplies a
     * {@link dev.itsharshxd.zentrix.api.deathmatch.DeathmatchDestination} and Zentrix runs its whole
     * ordinary deathmatch there instead of copying its template; passing or cancelling leaves the
     * template arena in place, so a scenario whose preferred world turns out to be unavailable only
     * has to pass and the match carries on untouched.
     *
     * <p>Raised once per deathmatch, after the start is committed to and before any world is copied.
     *
     * @since 1.6.0
     */
    public static final GameplayHook<DeathmatchDestinationRequest,
            dev.itsharshxd.zentrix.api.deathmatch.DeathmatchDestination> DEATHMATCH_DESTINATION =
            GameplayHook.of("zentrix:deathmatch-destination", DeathmatchDestinationRequest.class,
                    dev.itsharshxd.zentrix.api.deathmatch.DeathmatchDestination.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <R> GameplayHook<R, List<ItemStack>> hook(String id, Class<R> requestType) {
        Class<List<ItemStack>> valueType = (Class<List<ItemStack>>) (Class) List.class;
        return GameplayHook.of(id, requestType, valueType);
    }

    // ==========================================
    // Requests
    // ==========================================

    /** A block broken by a player inside the match. Drops and experience are mutable. */
    public static final class BlockDropRequest {

        private final Player player;
        private final Block block;
        private final ItemStack tool;
        private final List<ItemStack> drops;
        private int experience;

        public BlockDropRequest(
                @NotNull Player player,
                @NotNull Block block,
                @Nullable ItemStack tool,
                @NotNull Collection<ItemStack> drops,
                int experience) {
            this.player = player;
            this.block = block;
            this.tool = tool;
            this.drops = new ArrayList<>(drops);
            this.experience = experience;
        }

        @NotNull
        public Player player() {
            return player;
        }

        @NotNull
        public Block block() {
            return block;
        }

        /** The item used to break the block, if any. */
        @NotNull
        public Optional<ItemStack> tool() {
            return Optional.ofNullable(tool);
        }

        /** The drops, mutable in place. */
        @NotNull
        public List<ItemStack> drops() {
            return drops;
        }

        public int experience() {
            return experience;
        }

        public void experience(int experience) {
            this.experience = Math.max(0, experience);
        }
    }

    /** An entity killed inside the match. Drops and experience are mutable. */
    public static final class EntityDropRequest {

        private final Entity entity;
        private final Player killer;
        private final List<ItemStack> drops;
        private int experience;

        public EntityDropRequest(
                @NotNull Entity entity,
                @Nullable Player killer,
                @NotNull Collection<ItemStack> drops,
                int experience) {
            this.entity = entity;
            this.killer = killer;
            this.drops = new ArrayList<>(drops);
            this.experience = experience;
        }

        @NotNull
        public Entity entity() {
            return entity;
        }

        /** The player credited with the kill, if any. */
        @NotNull
        public Optional<Player> killer() {
            return Optional.ofNullable(killer);
        }

        /** The drops, mutable in place. */
        @NotNull
        public List<ItemStack> drops() {
            return drops;
        }

        public int experience() {
            return experience;
        }

        public void experience(int experience) {
            this.experience = Math.max(0, experience);
        }
    }

    /** Damage about to be applied to a player in the match. The amount is mutable. */
    public static final class DamageRequest {

        private final Player victim;
        private final Entity damager;
        private final String cause;
        private double damage;

        public DamageRequest(
                @NotNull Player victim, @Nullable Entity damager, @NotNull String cause, double damage) {
            this.victim = victim;
            this.damager = damager;
            this.cause = cause;
            this.damage = damage;
        }

        @NotNull
        public Player victim() {
            return victim;
        }

        /** The entity dealing the damage, if any. */
        @NotNull
        public Optional<Entity> damager() {
            return Optional.ofNullable(damager);
        }

        /** The Bukkit damage cause name. */
        @NotNull
        public String cause() {
            return cause;
        }

        public double damage() {
            return damage;
        }

        public void damage(double damage) {
            this.damage = Math.max(0.0D, damage);
        }
    }

    /** A player of the match dying. Drops and the kept-inventory flag are mutable. */
    public static final class DeathRequest {

        private final Player victim;
        private final Player killer;
        private final List<ItemStack> drops;
        private final Location location;
        private boolean keepInventory;

        public DeathRequest(
                @NotNull Player victim,
                @Nullable Player killer,
                @NotNull Collection<ItemStack> drops,
                @NotNull Location location,
                boolean keepInventory) {
            this.victim = victim;
            this.killer = killer;
            this.drops = new ArrayList<>(drops);
            this.location = location;
            this.keepInventory = keepInventory;
        }

        @NotNull
        public Player victim() {
            return victim;
        }

        @NotNull
        public Optional<Player> killer() {
            return Optional.ofNullable(killer);
        }

        /** The drops, mutable in place. */
        @NotNull
        public List<ItemStack> drops() {
            return drops;
        }

        @NotNull
        public Location location() {
            return location.clone();
        }

        public boolean keepInventory() {
            return keepInventory;
        }

        public void keepInventory(boolean keepInventory) {
            this.keepInventory = keepInventory;
        }
    }

    /** A crafting result about to be shown to a player of the match. */
    public static final class CraftRequest {

        private final Player player;
        private final ItemStack result;

        public CraftRequest(@NotNull Player player, @Nullable ItemStack result) {
            this.player = player;
            this.result = result;
        }

        @NotNull
        public Player player() {
            return player;
        }

        /** The result Zentrix would produce, if any. */
        @NotNull
        public Optional<ItemStack> result() {
            return Optional.ofNullable(result);
        }
    }

    /**
     * The deathmatch Zentrix is about to start, described as it would run it on its own.
     *
     * <p>Everything here is the configured deathmatch as it stands, which is what a scenario needs
     * in order to reproduce it somewhere else: remap the bounds and the spawns into its own world,
     * hand back a {@link dev.itsharshxd.zentrix.api.deathmatch.DeathmatchDestination}, and the rest
     * of the deathmatch runs unchanged.
     *
     * @since 1.6.0
     */
    public static final class DeathmatchDestinationRequest {

        private final String templateWorldName;
        private final String spawnType;
        private final Location borderCorner1;
        private final Location borderCorner2;
        private final List<Location> configuredSpawns;
        private final int participantCount;

        public DeathmatchDestinationRequest(
                @NotNull String templateWorldName,
                @NotNull String spawnType,
                @Nullable Location borderCorner1,
                @Nullable Location borderCorner2,
                @NotNull Collection<Location> configuredSpawns,
                int participantCount) {
            this.templateWorldName = templateWorldName;
            this.spawnType = spawnType;
            this.borderCorner1 = borderCorner1 == null ? null : borderCorner1.clone();
            this.borderCorner2 = borderCorner2 == null ? null : borderCorner2.clone();
            this.configuredSpawns = configuredSpawns.stream()
                    .filter(java.util.Objects::nonNull)
                    .map(Location::clone)
                    .toList();
            this.participantCount = participantCount;
        }

        /** The template world Zentrix would copy. */
        @NotNull
        public String templateWorldName() {
            return templateWorldName;
        }

        /** How the configuration places players: {@code RANDOM} or {@code CUSTOM}. */
        @NotNull
        public String spawnType() {
            return spawnType;
        }

        /**
         * One configured corner of the deathmatch border, in template-world coordinates.
         *
         * <p>Empty when the deathmatch is configured without bounds, in which case a destination
         * that wants a border has to decide on one itself.
         */
        @NotNull
        public Optional<Location> borderCorner1() {
            return Optional.ofNullable(borderCorner1 == null ? null : borderCorner1.clone());
        }

        /** The opposite configured corner, in template-world coordinates. */
        @NotNull
        public Optional<Location> borderCorner2() {
            return Optional.ofNullable(borderCorner2 == null ? null : borderCorner2.clone());
        }

        /** The configured custom spawns, in template-world coordinates; empty for random spawns. */
        @NotNull
        public List<Location> configuredSpawns() {
            return configuredSpawns;
        }

        /** How many players are about to be moved into the deathmatch. */
        public int participantCount() {
            return participantCount;
        }
    }

    /** Zentrix's periodic check of whether the match is over. */
    public static final class WinConditionRequest {

        private final int aliveTeamCount;
        private final int alivePlayerCount;
        private final List<String> aliveTeamIds;
        private final boolean defaultDecision;

        public WinConditionRequest(
                int aliveTeamCount,
                int alivePlayerCount,
                @NotNull Collection<String> aliveTeamIds,
                boolean defaultDecision) {
            this.aliveTeamCount = aliveTeamCount;
            this.alivePlayerCount = alivePlayerCount;
            this.aliveTeamIds = List.copyOf(aliveTeamIds);
            this.defaultDecision = defaultDecision;
        }

        public int aliveTeamCount() {
            return aliveTeamCount;
        }

        public int alivePlayerCount() {
            return alivePlayerCount;
        }

        @NotNull
        public List<String> aliveTeamIds() {
            return aliveTeamIds;
        }

        /** What Zentrix would decide on its own. */
        public boolean defaultDecision() {
            return defaultDecision;
        }
    }
}
