package dev.itsharshxd.zentrix.api.gui;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/** Fluent runtime layout definition for an addon GUI ID. */
public final class GuiLayoutDefinition {
    private final String id;
    private final int size;
    private final Map<String, Integer> elements = new LinkedHashMap<>();
    private final Map<String, String> contentLists = new LinkedHashMap<>();
    private boolean fillerEnabled = true;
    private Material fillerMaterial = Material.GRAY_STAINED_GLASS_PANE;
    private String fillerSlots = "all";

    private GuiLayoutDefinition(String id, int size) {
        this.id = id;
        this.size = size;
    }

    @NotNull public static GuiLayoutDefinition create(@NotNull String id, int size) {
        return new GuiLayoutDefinition(id, size);
    }
    @NotNull public GuiLayoutDefinition element(@NotNull String elementId, int slot) {
        elements.put(elementId, slot); return this;
    }
    @NotNull public GuiLayoutDefinition automaticElement(@NotNull String elementId) {
        elements.put(elementId, GuiLayout.AUTO); return this;
    }
    @NotNull public GuiLayoutDefinition content(@NotNull String listId, @NotNull String slots) {
        contentLists.put(listId, slots); return this;
    }
    @NotNull public GuiLayoutDefinition automaticContent(@NotNull String listId) {
        contentLists.put(listId, "auto"); return this;
    }
    @NotNull public GuiLayoutDefinition filler(@NotNull String slots) {
        fillerEnabled = true; fillerSlots = slots; return this;
    }
    @NotNull public GuiLayoutDefinition filler(@NotNull Material material, @NotNull String slots) {
        fillerEnabled = true; fillerMaterial = material; fillerSlots = slots; return this;
    }
    @NotNull public GuiLayoutDefinition noFiller() { fillerEnabled = false; return this; }
    @NotNull public String id() { return id; }
    public int size() { return size; }
    @NotNull public Map<String, Integer> elements() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(elements));
    }
    @NotNull public Map<String, String> contentLists() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(contentLists));
    }
    public boolean fillerEnabled() { return fillerEnabled; }
    @NotNull public Material fillerMaterial() { return fillerMaterial; }
    @NotNull public String fillerSlots() { return fillerSlots; }
}
