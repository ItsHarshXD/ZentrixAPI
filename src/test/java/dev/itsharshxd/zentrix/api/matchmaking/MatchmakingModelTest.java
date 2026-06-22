package dev.itsharshxd.zentrix.api.matchmaking;

import static org.junit.jupiter.api.Assertions.*;

import dev.itsharshxd.zentrix.api.arena.ArenaSourceStatus;
import dev.itsharshxd.zentrix.api.ZentrixAPI;
import java.util.List;
import org.junit.jupiter.api.Test;

class MatchmakingModelTest {
    @Test void oldImplementationsReceiveStructuredUnsupportedServices() {
        var service = dev.itsharshxd.zentrix.api.arena.ArenaSourceService.unsupported();
        assertEquals(ArenaSourceStatus.UNSUPPORTED,
                service.registerSource("abc").join().status());
        assertFalse(service.isSourceBusy("abc").join());
    }

    @Test void resultCopiesCollections() {
        MatchmakingResult result = new MatchmakingResult(MatchmakingStatus.SOURCE_REQUIRED, List.of(), List.of(), null,
                null, SourceDisposition.NONE, "needed");
        assertFalse(result.isSuccess()); assertTrue(result.getRuntimeGame().isEmpty());
    }
}
