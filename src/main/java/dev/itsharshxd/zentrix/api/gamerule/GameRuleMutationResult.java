package dev.itsharshxd.zentrix.api.gamerule;

import java.util.Optional;
import java.util.Objects;

public record GameRuleMutationResult(
    Status status,
    int affectedCount,
    Optional<ResolvedGameRule> effectiveValue
) {
    public GameRuleMutationResult {
        Objects.requireNonNull(status, "status");
        if (affectedCount < 0) throw new IllegalArgumentException("affectedCount cannot be negative");
        effectiveValue = effectiveValue == null ? Optional.empty() : effectiveValue;
    }

    public enum Status {
        APPLIED,
        NO_CHANGE,
        INVALID_RULE,
        INVALID_VALUE,
        INVALID_SCOPE,
        NOT_FOUND,
        READ_ONLY,
        FAILED
    }
}
