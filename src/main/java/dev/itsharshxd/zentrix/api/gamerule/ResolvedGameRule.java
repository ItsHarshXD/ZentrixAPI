package dev.itsharshxd.zentrix.api.gamerule;

import java.util.Objects;

public record ResolvedGameRule(
    GameRuleDefinition definition,
    Object value,
    GameRuleSource source
) {
    public ResolvedGameRule {
        Objects.requireNonNull(definition, "definition");
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(source, "source");
        if (definition.valueType() == GameRuleValueType.BOOLEAN && !(value instanceof Boolean)
            || definition.valueType() == GameRuleValueType.INTEGER && !(value instanceof Integer)) {
            throw new IllegalArgumentException("Value does not match game-rule definition");
        }
    }
}
