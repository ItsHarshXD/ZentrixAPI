package dev.itsharshxd.zentrix.api.gui;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/** Immutable, fully resolved inventory layout. */
public final class GuiLayout {
    public static final int HIDDEN = -1;
    public static final int AUTO = -2;

    private final String id;
    private final int size;
    private final boolean explicitSize;
    private final Map<String, Integer> elements;
    private final Map<String, int[]> contentLists;
    private final boolean fillerEnabled;
    private final Material fillerMaterial;
    private final int[] fillerSlots;

    public GuiLayout(
            @NotNull String id,
            int size,
            boolean explicitSize,
            @NotNull Map<String, Integer> elements,
            @NotNull Map<String, int[]> contentLists,
            boolean fillerEnabled,
            @NotNull Material fillerMaterial,
            @NotNull int[] fillerSlots) {
        this.id = id;
        this.size = size;
        this.explicitSize = explicitSize;
        this.elements = Collections.unmodifiableMap(new LinkedHashMap<>(elements));
        Map<String, int[]> copied = new LinkedHashMap<>();
        contentLists.forEach((key, value) -> copied.put(key, value.clone()));
        this.contentLists = Collections.unmodifiableMap(copied);
        this.fillerEnabled = fillerEnabled;
        this.fillerMaterial = fillerMaterial;
        this.fillerSlots = fillerSlots.clone();
    }

    @NotNull public String id() { return id; }
    public int size() { return size; }
    public int rows() { return size / 9; }
    public boolean hasExplicitSize() { return explicitSize; }
    @NotNull public Map<String, Integer> elements() { return elements; }
    public int slot(@NotNull String elementId) { return elements.getOrDefault(elementId, HIDDEN); }
    public int slot(@NotNull String elementId, int automaticSlot) {
        int slot = elements.getOrDefault(elementId, automaticSlot);
        return slot == AUTO ? automaticSlot : slot;
    }
    public boolean has(@NotNull String elementId) { return slot(elementId) >= 0; }
    @NotNull public int[] contentSlots() { return contentSlots("entries"); }
    @NotNull public int[] contentSlots(@NotNull String listId) {
        int[] slots = contentLists.get(listId);
        return slots == null ? new int[0] : slots.clone();
    }
    public int pageSize(@NotNull String listId) { return contentSlots(listId).length; }
    public int pageCount(int total) { return pageCount(total, "entries"); }
    public int pageCount(int total, @NotNull String listId) {
        int perPage = pageSize(listId);
        return perPage <= 0 ? 1 : Math.max(1, (Math.max(0, total) + perPage - 1) / perPage);
    }
    public int centerOf(@NotNull String listId) {
        int[] slots = contentSlots(listId);
        if (slots.length == 0) return HIDDEN;
        double row = Arrays.stream(slots).map(slot -> slot / 9).average().orElse(0);
        double column = Arrays.stream(slots).map(slot -> slot % 9).average().orElse(0);
        int closest = slots[0];
        double distance = Double.MAX_VALUE;
        for (int slot : slots) {
            double rowDelta = slot / 9 - row;
            double columnDelta = slot % 9 - column;
            double candidate = rowDelta * rowDelta + columnDelta * columnDelta;
            if (candidate < distance) { distance = candidate; closest = slot; }
        }
        return closest;
    }
    public boolean hasFiller() { return fillerEnabled && fillerSlots.length > 0; }
    @NotNull public Material fillerMaterial() { return fillerMaterial; }
    @NotNull public int[] fillerSlots() { return fillerSlots.clone(); }
}
