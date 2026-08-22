package dev.liamtolkkinen.extendeditems.internal;

import dev.liamtolkkinen.extendeditems.ExtendedItemDefinition;
import dev.liamtolkkinen.extendeditems.ExtendedItemId;
import dev.liamtolkkinen.extendeditems.ExtendedItemIds;
import dev.liamtolkkinen.extendeditems.ExtendedItemService;
import dev.liamtolkkinen.extendeditems.ExtendedItemValidationResult;
import dev.liamtolkkinen.extendeditems.ExtendedItemValidationStatus;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * Default implementation backed by the immutable built-in item registry.
 */
public final class DefaultExtendedItemService implements ExtendedItemService {

    private static final int CURRENT_FORMAT_VERSION = 1;

    private final ExtendedItemRegistry registry;

    public DefaultExtendedItemService(ExtendedItemRegistry registry) {
        this.registry =
            Objects.requireNonNull(
                registry,
                "registry");
    }

    public static DefaultExtendedItemService createDefault() {
        return new DefaultExtendedItemService(
            new ExtendedItemRegistry(
                List.of(
                    /*
                     * Sanctuary anchors
                     */

                    definition(
                        ExtendedItemIds.SANCTUARY_BEACON,
                        Material.BEACON,
                        "Sanctuary Beacon",
                        false),

                    definition(
                        ExtendedItemIds.SANCTUARY_CONDUIT,
                        Material.CONDUIT,
                        "Sanctuary Conduit",
                        false),

                    /*
                     * Sanctuary progression artifacts
                     */

                    definition(
                        ExtendedItemIds.SANCTUARY_CORE,
                        Material.NETHER_STAR,
                        "Sanctuary Core"),

                    definition(
                        ExtendedItemIds.TERRITORY_KEYSTONE,
                        Material.LODESTONE,
                        "Territory Keystone"),

                    definition(
                        ExtendedItemIds.WATCHERS_EYE,
                        Material.ENDER_EYE,
                        "Watcher's Eye"),

                    definition(
                        ExtendedItemIds.WARD_STONE,
                        Material.OBSIDIAN,
                        "Ward Stone"),

                    definition(
                        ExtendedItemIds.BLAST_WARD,
                        Material.CRYING_OBSIDIAN,
                        "Blast Ward"),

                    definition(
                        ExtendedItemIds.PURIFICATION_RELIC,
                        Material.GHAST_TEAR,
                        "Purification Relic"),

                    definition(
                        ExtendedItemIds.SEAL_OF_KEEPING,
                        Material.ENDER_CHEST,
                        "Seal of Keeping"),

                    definition(
                        ExtendedItemIds.GUARDIAN_TOKEN,
                        Material.HEART_OF_THE_SEA,
                        "Guardian Token"),

                    definition(
                        ExtendedItemIds.SENTINEL_SEAL,
                        Material.ECHO_SHARD,
                        "Sentinel Seal"),

                    definition(
                        ExtendedItemIds.CONSECRATED_SHARD,
                        Material.AMETHYST_SHARD,
                        "Consecrated Shard"),

                    definition(
                        ExtendedItemIds.CONSECRATED_KEYSTONE,
                        Material.RESPAWN_ANCHOR,
                        "Consecrated Keystone"),

                    definition(
                        ExtendedItemIds.DIVINE_RELIC,
                        Material.TOTEM_OF_UNDYING,
                        "Divine Relic"),

                    /*
                     * Land sentries
                     */

                    definition(
                        ExtendedItemIds.SENTRY_IRON_GOLEM,
                        Material.SMOOTH_STONE_SLAB,
                        "Iron Golem Sentry Post"),

                    definition(
                        ExtendedItemIds.SENTRY_PILLAGER,
                        Material.DARK_OAK_SLAB,
                        "Pillager Sentry Post"),

                    definition(
                        ExtendedItemIds.SENTRY_SKELETON,
                        Material.QUARTZ_SLAB,
                        "Skeleton Sentry Post"),

                    definition(
                        ExtendedItemIds.SENTRY_PIGLIN_BRUTE,
                        Material.BLACKSTONE_SLAB,
                        "Piglin Brute Sentry Post"),

                    definition(
                        ExtendedItemIds.SENTRY_ENDERMAN,
                        Material.PURPUR_SLAB,
                        "Enderman Sentry Post"),

                    definition(
                        ExtendedItemIds.SENTRY_EVOKER,
                        Material.STONE_BRICK_SLAB,
                        "Evoker Sentry Post"),

                    definition(
                        ExtendedItemIds.SENTRY_BABY_ZOMBIE,
                        Material.MOSSY_COBBLESTONE_SLAB,
                        "Baby Zombie Sentry Post"),

                    definition(
                        ExtendedItemIds.SENTRY_BLAZE,
                        Material.NETHER_BRICK_SLAB,
                        "Blaze Sentry Post"),

                    definition(
                        ExtendedItemIds.SENTRY_WARDEN,
                        Material.SCULK_SENSOR,
                        "Warden Sentry Post"),

                    definition(
                        ExtendedItemIds.SENTRY_CREAKING,
                        Material.PALE_OAK_SLAB,
                        "Creaking Sentry Post"),

                    definition(
                        ExtendedItemIds.SENTRY_WITHER,
                        Material.POLISHED_BLACKSTONE_SLAB,
                        "Wither Sentry Post"),

                    /*
                     * Aquatic sentries
                     */

                    definition(
                        ExtendedItemIds.SENTRY_DROWNED,
                        Material.PRISMARINE_SLAB,
                        "Drowned Sentry Post"),

                    definition(
                        ExtendedItemIds.SENTRY_GUARDIAN,
                        Material.DARK_PRISMARINE_SLAB,
                        "Guardian Sentry Post"),

                    definition(
                        ExtendedItemIds.SENTRY_ELDER_GUARDIAN,
                        Material.PRISMARINE_BRICK_SLAB,
                        "Elder Guardian Sentry Post"),

                    /*
                     * Land companions
                     */

                    definition(
                        ExtendedItemIds.COMPANION_IRON_GOLEM,
                        Material.IRON_GOLEM_SPAWN_EGG,
                        "Iron Golem Companion"),

                    definition(
                        ExtendedItemIds.COMPANION_PILLAGER,
                        Material.PILLAGER_SPAWN_EGG,
                        "Pillager Companion"),

                    definition(
                        ExtendedItemIds.COMPANION_SKELETON,
                        Material.SKELETON_SPAWN_EGG,
                        "Skeleton Companion"),

                    definition(
                        ExtendedItemIds.COMPANION_PIGLIN_BRUTE,
                        Material.PIGLIN_BRUTE_SPAWN_EGG,
                        "Piglin Brute Companion"),

                    definition(
                        ExtendedItemIds.COMPANION_ENDERMAN,
                        Material.ENDERMAN_SPAWN_EGG,
                        "Enderman Companion"),

                    definition(
                        ExtendedItemIds.COMPANION_EVOKER,
                        Material.EVOKER_SPAWN_EGG,
                        "Evoker Companion"),

                    /*
                     * The intended guard is a Baby Zombie.
                     * Sanctuary controls the spawned entity variant.
                     */

                    definition(
                        ExtendedItemIds.COMPANION_BABY_ZOMBIE,
                        Material.ZOMBIE_SPAWN_EGG,
                        "Baby Zombie Companion"),

                    definition(
                        ExtendedItemIds.COMPANION_BLAZE,
                        Material.BLAZE_SPAWN_EGG,
                        "Blaze Companion"),

                    definition(
                        ExtendedItemIds.COMPANION_WARDEN,
                        Material.WARDEN_SPAWN_EGG,
                        "Warden Companion"),

                    definition(
                        ExtendedItemIds.COMPANION_CREAKING,
                        Material.CREAKING_SPAWN_EGG,
                        "Creaking Companion"),

                    definition(
                        ExtendedItemIds.COMPANION_WITHER,
                        Material.WITHER_SPAWN_EGG,
                        "Wither Companion"),

                    /*
                     * Aquatic companions
                     */

                    definition(
                        ExtendedItemIds.COMPANION_DROWNED,
                        Material.DROWNED_SPAWN_EGG,
                        "Drowned Companion"),

                    definition(
                        ExtendedItemIds.COMPANION_GUARDIAN,
                        Material.GUARDIAN_SPAWN_EGG,
                        "Guardian Companion"),

                    definition(
                        ExtendedItemIds.COMPANION_ELDER_GUARDIAN,
                        Material.ELDER_GUARDIAN_SPAWN_EGG,
                        "Elder Guardian Companion"),

                    /*
                     * Additional companion identities
                     */

                    definition(
                        ExtendedItemIds.COMPANION_AXOLOTL,
                        Material.AXOLOTL_SPAWN_EGG,
                        "Axolotl Companion"),

                    definition(
                        ExtendedItemIds.COMPANION_DOLPHIN,
                        Material.DOLPHIN_SPAWN_EGG,
                        "Dolphin Companion"))));
    }

    @Override
    public ItemStack create(ExtendedItemId id) {
        ExtendedItemDefinition definition =
            registry.require(id);

        ItemStack item =
            new ItemStack(definition.material());

        ItemMetaFactory.apply(
            item,
            definition);

        return item;
    }

    @Override
    public boolean is(
        ItemStack item,
        ExtendedItemId expected)
    {
        Objects.requireNonNull(
            expected,
            "expected");

        return getId(item)
            .filter(expected::equals)
            .isPresent();
    }

    @Override
    public Optional<ExtendedItemId> getId(
        ItemStack item)
    {
        if (item == null ||
            item.getType().isAir())
        {
            return Optional.empty();
        }

        ItemMeta meta =
            item.getItemMeta();

        if (meta == null) {
            return Optional.empty();
        }

        PersistentDataContainer pdc =
            meta.getPersistentDataContainer();

        if (!pdc.has(
            ExtendedItemKeys.ID,
            PersistentDataType.STRING))
        {
            return Optional.empty();
        }

        String persistentId =
            pdc.get(
                ExtendedItemKeys.ID,
                PersistentDataType.STRING);

        return registry
            .findByPersistentId(persistentId)
            .map(ExtendedItemDefinition::id);
    }

    @Override
    public ExtendedItemValidationResult validate(
        ItemStack item)
    {
        if (item == null) {
            return result(
                ExtendedItemValidationStatus.INVALID_FORMAT,
                null,
                null,
                "ItemStack is null.");
        }

        if (item.getType().isAir()) {
            return result(
                ExtendedItemValidationStatus.MISSING_ID,
                null,
                null,
                "Air does not contain an ExtendedItems ID.");
        }

        ItemMeta meta =
            item.getItemMeta();

        if (meta == null) {
            return result(
                ExtendedItemValidationStatus.MISSING_ID,
                null,
                null,
                "Item has no ItemMeta and therefore no ExtendedItems ID.");
        }

        PersistentDataContainer pdc =
            meta.getPersistentDataContainer();

        if (!pdc.has(ExtendedItemKeys.ID)) {
            return result(
                ExtendedItemValidationStatus.MISSING_ID,
                null,
                null,
                "Missing extendeditems:id metadata.");
        }

        if (!pdc.has(
            ExtendedItemKeys.ID,
            PersistentDataType.STRING))
        {
            return result(
                ExtendedItemValidationStatus.INVALID_FORMAT,
                null,
                null,
                "extendeditems:id exists but is not a STRING.");
        }

        String persistentId =
            pdc.get(
                ExtendedItemKeys.ID,
                PersistentDataType.STRING);

        if (persistentId == null ||
            persistentId.isBlank())
        {
            return result(
                ExtendedItemValidationStatus.INVALID_FORMAT,
                null,
                null,
                "extendeditems:id is not a non-blank STRING.");
        }

        Optional<ExtendedItemDefinition> definitionOptional =
            registry.findByPersistentId(persistentId);

        if (definitionOptional.isEmpty()) {
            return result(
                ExtendedItemValidationStatus.UNKNOWN_ITEM,
                null,
                readVersionIfInteger(pdc),
                "Unknown ExtendedItems persistent ID '"
                    + persistentId
                    + "'.");
        }

        ExtendedItemDefinition definition =
            definitionOptional.get();

        if (!pdc.has(ExtendedItemKeys.VERSION)) {
            return result(
                ExtendedItemValidationStatus.MISSING_VERSION,
                definition.id(),
                null,
                "Missing extendeditems:version metadata.");
        }

        if (!pdc.has(
            ExtendedItemKeys.VERSION,
            PersistentDataType.INTEGER))
        {
            return result(
                ExtendedItemValidationStatus.INVALID_FORMAT,
                definition.id(),
                null,
                "extendeditems:version exists but is not an INTEGER.");
        }

        Integer version =
            pdc.get(
                ExtendedItemKeys.VERSION,
                PersistentDataType.INTEGER);

        if (version == null ||
            version <= 0)
        {
            return result(
                ExtendedItemValidationStatus.INVALID_FORMAT,
                definition.id(),
                version,
                "extendeditems:version is not a positive INTEGER.");
        }

        if (version != definition.version()) {
            return result(
                ExtendedItemValidationStatus.UNSUPPORTED_VERSION,
                definition.id(),
                version,
                "Unsupported format version "
                    + version
                    + " for "
                    + definition.id()
                    + "; supported version is "
                    + definition.version()
                    + ".");
        }

        if (item.getType() !=
            definition.material())
        {
            return result(
                ExtendedItemValidationStatus.INVALID_MATERIAL,
                definition.id(),
                version,
                "Expected material "
                    + definition.material()
                    + " but found "
                    + item.getType()
                    + ".");
        }

        return result(
            ExtendedItemValidationStatus.VALID,
            definition.id(),
            version,
            "Item is a valid "
                + definition.id()
                + ".");
    }

    ExtendedItemRegistry registry() {
        return registry;
    }

    private static ExtendedItemDefinition definition(
        ExtendedItemId id,
        Material material,
        String displayName)
    {
        return definition(
            id,
            material,
            displayName,
            true);
    }

    private static ExtendedItemDefinition definition(
        ExtendedItemId id,
        Material material,
        String displayName,
        boolean glint)
    {
        return new ExtendedItemDefinition(
            id,
            CURRENT_FORMAT_VERSION,
            material,
            Component.text(displayName),
            List.of(),
            glint,
            Set.of());
    }

    private static Integer readVersionIfInteger(
        PersistentDataContainer pdc)
    {
        if (!pdc.has(
            ExtendedItemKeys.VERSION,
            PersistentDataType.INTEGER))
        {
            return null;
        }

        return pdc.get(
            ExtendedItemKeys.VERSION,
            PersistentDataType.INTEGER);
    }

    private static ExtendedItemValidationResult result(
        ExtendedItemValidationStatus status,
        ExtendedItemId itemId,
        Integer foundVersion,
        String detail)
    {
        return ExtendedItemValidationResult.of(
            status,
            itemId,
            foundVersion,
            detail);
    }
}
