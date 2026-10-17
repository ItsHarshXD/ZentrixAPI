package dev.itsharshxd.zentrix.api.platform;

import java.util.List;

/**
 * Ids of the capabilities Zentrix reports itself through {@link PlatformService#getCapability}.
 *
 * <p>Each one separates "this feature exists in the API" from "this server can do it now": a
 * method that exists may still be restricted by the edition, missing a dependency or switched off.
 *
 * @since 1.8.0
 */
public final class ZentrixCapabilities {

    /** Opening a player's glider: dragon-bus ejection and {@code SkyDropService} deployment. */
    public static final String GLIDER_DEPLOYMENT = "zentrix:glider-deployment";

    /** Built-in glider cosmetics: textured gliders, previews and their selection menus. */
    public static final String GLIDER_VISUALS = "zentrix:glider-visuals";

    /** The one-time import of players' gliders from the retired Matrix Gliders plugin. */
    public static final String MATRIX_GLIDERS_IMPORT = "zentrix:matrix-gliders-import";

    /** ItemsAdder and Nexo items in kits, loot, recipes and menus. */
    public static final String EXTERNAL_ITEMS = "zentrix:external-items";

    /** Cornucopia schematic placement, which needs FastAsyncWorldEdit. */
    public static final String CORNUCOPIA = "zentrix:cornucopia";

    /** Copying WorldGuard regions into runtime worlds. */
    public static final String WORLDGUARD_REGIONS = "zentrix:worldguard-regions";

    /** Coordinating team nametags and tab-list entries with TAB. */
    public static final String TAB_INTEGRATION = "zentrix:tab";

    /** Keeping spectators out of players' Simple Voice Chat audio. */
    public static final String VOICE_CHAT_ISOLATION = "zentrix:voice-chat-isolation";

    /** Packet-level identity masking through {@code IdentityService}. */
    public static final String IDENTITY_MASKING = "zentrix:identity-masking";

    /** Zentrix's {@code %zentrix_...%} placeholders, addon placeholders included. */
    public static final String PLACEHOLDERS = "zentrix:placeholders";

    /** Profile, statistic and balance storage. */
    public static final String STORAGE = "zentrix:storage";

    /** Customizing menu layouts in {@code guis.yml}. */
    public static final String GUI_CUSTOMIZATION = "zentrix:gui-customization";

    /** Every id above, in the order Zentrix reports them. */
    public static final List<String> BUILT_IN = List.of(
            GLIDER_DEPLOYMENT, GLIDER_VISUALS, MATRIX_GLIDERS_IMPORT, EXTERNAL_ITEMS, CORNUCOPIA,
            WORLDGUARD_REGIONS, TAB_INTEGRATION, VOICE_CHAT_ISOLATION, IDENTITY_MASKING,
            PLACEHOLDERS, STORAGE, GUI_CUSTOMIZATION);

    private ZentrixCapabilities() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}
