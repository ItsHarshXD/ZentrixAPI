package dev.itsharshxd.zentrix.api.phase;

import static org.junit.jupiter.api.Assertions.*;

import dev.itsharshxd.zentrix.api.nether.NetherBorderSettings;
import dev.itsharshxd.zentrix.api.nether.NetherToggleRequest;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class PhaseBuilderTest {
    @Test
    void emitsCanonicalActionKeys() {
        PhaseBuilder builder = new PhaseBuilder().name("api_test").onStart(actions -> actions
            .announce("hello")
            .togglePvP(true)
            .giveItem("minecraft:stone")
            .command("say test")
            .toggleNether(new NetherToggleRequest(true, Optional.of(false), Optional.of(
                new NetherBorderSettings(true, 0.0, 30, 2.0, 5.0, 10, 15)))));

        assertEquals("hello", builder.getOnStartActions().get(0).getParameters().get("value"));
        assertEquals(true, builder.getOnStartActions().get(1).getParameters().get("enable"));
        assertEquals("minecraft:stone", builder.getOnStartActions().get(2).getParameters().get("itemId"));
        assertEquals("say test", builder.getOnStartActions().get(3).getParameters().get("value"));
        Map<?, ?> border = (Map<?, ?>) builder.getOnStartActions().get(4).getParameters().get("border");
        assertEquals(0.0, border.get("shrinkTo"));
        assertEquals(30, border.get("duration"));
        assertThrows(UnsupportedOperationException.class, border::clear);
    }

    @Test
    void resolvesLegacyAliasesWithoutMutatingParameters() {
        PhaseBuilder.PhaseAction action = new PhaseBuilder.PhaseAction(
            PhaseBuilder.PhaseActionType.SOUND, Map.of("sound", "ENTITY_PLAYER_LEVELUP"));
        assertEquals("ENTITY_PLAYER_LEVELUP", action.getParameter("name", "sound").orElseThrow());
        assertFalse(action.getParameters().containsKey("name"));
    }

    @Test
    void borderDamageBufferIsCopied() {
        PhaseBuilder original = new PhaseBuilder().name("border_test")
            .border(border -> border.damageBuffer(7.5));
        assertEquals(7.5, original.copy().getBorderConfig().getDamageBuffer());
    }
}
