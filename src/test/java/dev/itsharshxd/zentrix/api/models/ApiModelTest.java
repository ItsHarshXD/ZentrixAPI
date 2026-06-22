package dev.itsharshxd.zentrix.api.models;

import static org.junit.jupiter.api.Assertions.*;

import dev.itsharshxd.zentrix.api.gamerule.*;
import dev.itsharshxd.zentrix.api.nether.NetherBorderSettings;
import dev.itsharshxd.zentrix.api.nether.NetherStatus;
import dev.itsharshxd.zentrix.api.world.WorldBorderSnapshot;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ApiModelTest {
    @Test
    void netherRadiusZeroMeansPreserveAndIsValid() {
        NetherBorderSettings settings = new NetherBorderSettings(null, 0.0, null, null, null, null, null);
        assertEquals(0.0, settings.getTargetRadius().orElseThrow());
        assertThrows(IllegalArgumentException.class,
            () -> new NetherBorderSettings(null, -1.0, null, null, null, null, null));
    }

    @Test
    void gameRuleModelsEnforceValueTypesAndImmutability() {
        GameRuleDefinition booleanRule = new GameRuleDefinition("doDaylightCycle", GameRuleValueType.BOOLEAN);
        ResolvedGameRule resolved = new ResolvedGameRule(booleanRule, false, GameRuleSource.ARENA_OVERRIDE);
        assertFalse((Boolean) resolved.value());
        assertThrows(IllegalArgumentException.class,
            () -> new ResolvedGameRule(booleanRule, 1, GameRuleSource.DEFAULT));

        GameRuleMutationResult mutation = new GameRuleMutationResult(
            GameRuleMutationResult.Status.APPLIED, 1, Optional.of(resolved));
        assertEquals(1, mutation.affectedCount());
    }

    @Test
    void worldBorderSnapshotRejectsNegativeValues() {
        assertDoesNotThrow(() -> new WorldBorderSnapshot(100, 1, 5, 10, 15));
        assertThrows(IllegalArgumentException.class,
            () -> new WorldBorderSnapshot(-1, 1, 5, 10, 15));
        assertThrows(IllegalArgumentException.class,
            () -> new WorldBorderSnapshot(Double.NaN, 1, 5, 10, 15));
    }

    @Test
    void requiredStatusFieldsRejectNull() {
        assertThrows(NullPointerException.class,
            () -> new NetherStatus(false, null, false, Optional.empty(), false,
                Optional.empty(), Optional.empty()));
        assertThrows(NullPointerException.class,
            () -> new GameRuleMutationResult(null, 0, Optional.empty()));
    }
}
