package dev.itsharshxd.zentrix.api.matchmaking;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Immutable atomic matchmaking request. */
public final class MatchmakingRequest {

    private final List<Player> onlineTargets;
    private final String gameTypeName;
    private final String sourceArenaName;
    private final UUID partyLeaderId;

    public MatchmakingRequest(
            @NotNull Collection<? extends Player> onlineTargets,
            @Nullable String gameTypeName,
            @Nullable String sourceArenaName,
            @Nullable UUID partyLeaderId) {
        Objects.requireNonNull(onlineTargets, "onlineTargets");
        this.onlineTargets = onlineTargets.stream()
                .filter(Objects::nonNull)
                .map(Player.class::cast)
                .distinct()
                .toList();
        this.gameTypeName = normalize(gameTypeName);
        this.sourceArenaName = normalize(sourceArenaName);
        this.partyLeaderId = partyLeaderId;
    }

    @NotNull public List<Player> getOnlineTargets() { return onlineTargets; }
    @NotNull public Optional<String> getGameTypeName() { return Optional.ofNullable(gameTypeName); }
    @NotNull public Optional<String> getSourceArenaName() { return Optional.ofNullable(sourceArenaName); }
    @NotNull public Optional<UUID> getPartyLeaderId() { return Optional.ofNullable(partyLeaderId); }

    @NotNull
    public static Builder builder(@NotNull Collection<? extends Player> onlineTargets) {
        return new Builder(onlineTargets);
    }

    private static String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public static final class Builder {
        private final Collection<? extends Player> onlineTargets;
        private String gameTypeName;
        private String sourceArenaName;
        private UUID partyLeaderId;

        private Builder(Collection<? extends Player> onlineTargets) {
            this.onlineTargets = Objects.requireNonNull(onlineTargets, "onlineTargets");
        }

        public Builder gameTypeName(@Nullable String value) { this.gameTypeName = value; return this; }
        public Builder sourceArenaName(@Nullable String value) { this.sourceArenaName = value; return this; }
        public Builder partyLeaderId(@Nullable UUID value) { this.partyLeaderId = value; return this; }

        public MatchmakingRequest build() {
            return new MatchmakingRequest(onlineTargets, gameTypeName, sourceArenaName, partyLeaderId);
        }
    }
}
