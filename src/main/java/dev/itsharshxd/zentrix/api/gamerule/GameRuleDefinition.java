package dev.itsharshxd.zentrix.api.gamerule;

import java.util.Objects;

public record GameRuleDefinition(String name, GameRuleValueType valueType) {
    public GameRuleDefinition {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(valueType, "valueType");
        if (name.isBlank()) throw new IllegalArgumentException("name cannot be blank");
    }
}
