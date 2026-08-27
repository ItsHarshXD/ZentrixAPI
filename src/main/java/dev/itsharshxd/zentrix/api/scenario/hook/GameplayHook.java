package dev.itsharshxd.zentrix.api.scenario.hook;

import java.util.Locale;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * A typed decision point in Zentrix's default gameplay that a scenario may take over.
 *
 * <p>Each hook pairs a request type with a result type. A scenario registers a
 * {@link GameplayHookHandler} for the hook through its game context; Zentrix consults the
 * registered handlers, highest scenario priority first, whenever it reaches that decision point.
 * The first handler that does not {@linkplain HookOutcome#pass() pass} decides the outcome, so a
 * combination of scenarios always resolves the same way.
 *
 * <p>The built-in hooks live in {@link GameplayHooks}. A scenario may also define its own hook and
 * dispatch it itself, which is how mechanics Zentrix never anticipated — custom roles, bosses,
 * objectives, gravity or crafting systems — stay composable between scenarios.
 *
 * @param <R> the request type handed to handlers
 * @param <V> the value type a handler may substitute
 * @since 1.6.0
 */
public final class GameplayHook<R, V> {

    private final String id;
    private final Class<R> requestType;
    private final Class<V> valueType;

    private GameplayHook(String id, Class<R> requestType, Class<V> valueType) {
        this.id = id;
        this.requestType = requestType;
        this.valueType = valueType;
    }

    /**
     * Creates a hook key.
     *
     * <p>Addon hooks should namespace their id, for example {@code myaddon:boss-spawn}, so two
     * addons cannot collide.
     *
     * @param id          the stable hook identifier
     * @param requestType the request type handed to handlers
     * @param valueType   the value type a handler may substitute
     */
    @NotNull
    public static <R, V> GameplayHook<R, V> of(
            @NotNull String id, @NotNull Class<R> requestType, @NotNull Class<V> valueType) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("A gameplay hook needs an id");
        }
        return new GameplayHook<>(
                id.trim().toLowerCase(Locale.ROOT),
                Objects.requireNonNull(requestType, "requestType"),
                Objects.requireNonNull(valueType, "valueType"));
    }

    @NotNull
    public String id() {
        return id;
    }

    @NotNull
    public Class<R> requestType() {
        return requestType;
    }

    @NotNull
    public Class<V> valueType() {
        return valueType;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GameplayHook<?, ?> hook && id.equals(hook.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "GameplayHook[" + id + "]";
    }
}
