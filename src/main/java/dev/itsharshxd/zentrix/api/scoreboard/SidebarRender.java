package dev.itsharshxd.zentrix.api.scoreboard;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One sidebar about to be shown to one player, which a {@link SidebarContributor} may change.
 *
 * <p>Title and lines are rendered text in Zentrix's colour format ({@code &} codes and
 * {@code &#RRGGBB} hex colours), with Zentrix's own placeholders and PlaceholderAPI already
 * resolved. A sidebar shows at most {@value #MAX_LINES} lines; extra lines are dropped. Text a
 * contributor adds is shown as written, so it must respect identity masking itself - use the
 * player's display name rather than their real one.
 *
 * @since 1.8.0
 */
public final class SidebarRender {

    /** The most lines a sidebar can show. */
    public static final int MAX_LINES = 15;

    private final Player player;
    private final ZentrixGame game;
    private String title;
    private final List<String> lines;

    public SidebarRender(@NotNull Player player, @Nullable ZentrixGame game, @NotNull String title,
                         @NotNull List<String> lines) {
        this.player = Objects.requireNonNull(player, "player");
        this.game = game;
        this.title = Objects.requireNonNull(title, "title");
        this.lines = new ArrayList<>(lines);
    }

    /** The player who will see the sidebar. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * The match the player is in or spectates; empty for the lobby sidebar and anywhere outside a
     * match.
     */
    @NotNull
    public Optional<ZentrixGame> getGame() {
        return Optional.ofNullable(game);
    }

    /** The title. */
    @NotNull
    public String getTitle() {
        return title;
    }

    /** Replaces the title. */
    public void setTitle(@NotNull String title) {
        this.title = Objects.requireNonNull(title, "title");
    }

    /** The lines, top to bottom; listeners may change the list. Null entries are not allowed. */
    @NotNull
    public List<String> getLines() {
        return lines;
    }
}
