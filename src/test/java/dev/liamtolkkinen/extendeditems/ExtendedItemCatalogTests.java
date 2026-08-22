package dev.liamtolkkinen.extendeditems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.liamtolkkinen.extendeditems.internal.ExtendedItemKeys;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.Test;

class ExtendedItemCatalogTests
        extends MockBukkitTestBase
{
    private static final int FORMAT_VERSION = 1;

    private static final List<ExpectedItem> EXPECTED_ITEMS =
            List.of(
                    /*
                     * Land sentries
                     */

                    expected(
                            ExtendedItemIds.SENTRY_IRON_GOLEM,
                            "sentry_iron_golem",
                            "Iron Golem Sentry Post",
                            Material.SMOOTH_STONE_SLAB),

                    expected(
                            ExtendedItemIds.SENTRY_PILLAGER,
                            "sentry_pillager",
                            "Pillager Sentry Post",
                            Material.DARK_OAK_SLAB),

                    expected(
                            ExtendedItemIds.SENTRY_SKELETON,
                            "sentry_skeleton",
                            "Skeleton Sentry Post",
                            Material.QUARTZ_SLAB),

                    expected(
                            ExtendedItemIds.SENTRY_PIGLIN_BRUTE,
                            "sentry_piglin_brute",
                            "Piglin Brute Sentry Post",
                            Material.BLACKSTONE_SLAB),

                    expected(
                            ExtendedItemIds.SENTRY_ENDERMAN,
                            "sentry_enderman",
                            "Enderman Sentry Post",
                            Material.PURPUR_SLAB),

                    expected(
                            ExtendedItemIds.SENTRY_EVOKER,
                            "sentry_evoker",
                            "Evoker Sentry Post",
                            Material.STONE_BRICK_SLAB),

                    expected(
                            ExtendedItemIds.SENTRY_BABY_ZOMBIE,
                            "sentry_baby_zombie",
                            "Baby Zombie Sentry Post",
                            Material.MOSSY_COBBLESTONE_SLAB),

                    expected(
                            ExtendedItemIds.SENTRY_BLAZE,
                            "sentry_blaze",
                            "Blaze Sentry Post",
                            Material.NETHER_BRICK_SLAB),

                    expected(
                            ExtendedItemIds.SENTRY_WARDEN,
                            "sentry_warden",
                            "Warden Sentry Post",
                            Material.SCULK_SENSOR),

                    expected(
                            ExtendedItemIds.SENTRY_CREAKING,
                            "sentry_creaking",
                            "Creaking Sentry Post",
                            Material.PALE_OAK_SLAB),

                    expected(
                            ExtendedItemIds.SENTRY_WITHER,
                            "sentry_wither",
                            "Wither Sentry Post",
                            Material.POLISHED_BLACKSTONE_SLAB),

                    /*
                     * Aquatic sentries
                     */

                    expected(
                            ExtendedItemIds.SENTRY_DROWNED,
                            "sentry_drowned",
                            "Drowned Sentry Post",
                            Material.PRISMARINE_SLAB),

                    expected(
                            ExtendedItemIds.SENTRY_GUARDIAN,
                            "sentry_guardian",
                            "Guardian Sentry Post",
                            Material.DARK_PRISMARINE_SLAB),

                    expected(
                            ExtendedItemIds.SENTRY_ELDER_GUARDIAN,
                            "sentry_elder_guardian",
                            "Elder Guardian Sentry Post",
                            Material.PRISMARINE_BRICK_SLAB),

                    /*
                     * Land companions
                     */

                    expected(
                            ExtendedItemIds.COMPANION_IRON_GOLEM,
                            "companion_iron_golem",
                            "Iron Golem Companion",
                            Material.IRON_GOLEM_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_PILLAGER,
                            "companion_pillager",
                            "Pillager Companion",
                            Material.PILLAGER_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_SKELETON,
                            "companion_skeleton",
                            "Skeleton Companion",
                            Material.SKELETON_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_PIGLIN_BRUTE,
                            "companion_piglin_brute",
                            "Piglin Brute Companion",
                            Material.PIGLIN_BRUTE_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_ENDERMAN,
                            "companion_enderman",
                            "Enderman Companion",
                            Material.ENDERMAN_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_EVOKER,
                            "companion_evoker",
                            "Evoker Companion",
                            Material.EVOKER_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_BABY_ZOMBIE,
                            "companion_baby_zombie",
                            "Baby Zombie Companion",
                            Material.ZOMBIE_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_BLAZE,
                            "companion_blaze",
                            "Blaze Companion",
                            Material.BLAZE_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_WARDEN,
                            "companion_warden",
                            "Warden Companion",
                            Material.WARDEN_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_CREAKING,
                            "companion_creaking",
                            "Creaking Companion",
                            Material.CREAKING_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_WITHER,
                            "companion_wither",
                            "Wither Companion",
                            Material.WITHER_SPAWN_EGG),

                    /*
                     * Aquatic companions
                     */

                    expected(
                            ExtendedItemIds.COMPANION_DROWNED,
                            "companion_drowned",
                            "Drowned Companion",
                            Material.DROWNED_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_GUARDIAN,
                            "companion_guardian",
                            "Guardian Companion",
                            Material.GUARDIAN_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_ELDER_GUARDIAN,
                            "companion_elder_guardian",
                            "Elder Guardian Companion",
                            Material.ELDER_GUARDIAN_SPAWN_EGG),

                    /*
                     * Additional companions
                     */

                    expected(
                            ExtendedItemIds.COMPANION_AXOLOTL,
                            "companion_axolotl",
                            "Axolotl Companion",
                            Material.AXOLOTL_SPAWN_EGG),

                    expected(
                            ExtendedItemIds.COMPANION_DOLPHIN,
                            "companion_dolphin",
                            "Dolphin Companion",
                            Material.DOLPHIN_SPAWN_EGG));

    @Test
    void catalogContainsThirtyPublishedIds() {
        long publicItemIdFields =
                java.util.Arrays
                        .stream(
                                ExtendedItemIds.class
                                        .getDeclaredFields())
                        .filter(
                                ExtendedItemCatalogTests
                                        ::isPublishedItemIdField)
                        .count();

        assertEquals(
                30,
                publicItemIdFields);

        assertEquals(
                30,
                EXPECTED_ITEMS.size());
    }

    @Test
    void allPersistentIdsAreUnique() {
        Set<String> persistentIds =
                new HashSet<>();

        for (ExpectedItem expected :
                EXPECTED_ITEMS)
        {
            assertTrue(
                    persistentIds.add(
                            expected.persistentId()),
                    () ->
                            "Duplicate persistent ID: "
                                    + expected.persistentId());
        }

        assertEquals(
                EXPECTED_ITEMS.size(),
                persistentIds.size());
    }

    @Test
    void everyCatalogItemMatchesExpectedContract() {
        for (ExpectedItem expected :
                EXPECTED_ITEMS)
        {
            ItemStack item =
                    ExtendedItems.create(
                            expected.id());

            ItemMeta meta =
                    item.getItemMeta();

            assertEquals(
                    expected.material(),
                    item.getType(),
                    expected.persistentId());

            assertEquals(
                    Component.text(
                            expected.displayName()),
                    meta.displayName(),
                    expected.persistentId());

            assertEquals(
                    expected.persistentId(),
                    meta.getPersistentDataContainer()
                            .get(
                                    ExtendedItemKeys.ID,
                                    PersistentDataType.STRING),
                    expected.persistentId());

            assertEquals(
                    FORMAT_VERSION,
                    meta.getPersistentDataContainer()
                            .get(
                                    ExtendedItemKeys.VERSION,
                                    PersistentDataType.INTEGER),
                    expected.persistentId());

            assertTrue(
                    meta.hasEnchantmentGlintOverride(),
                    expected.persistentId());

            assertTrue(
                    meta.getEnchantmentGlintOverride(),
                    expected.persistentId());

            assertTrue(
                    ExtendedItems.is(
                            item,
                            expected.id()),
                    expected.persistentId());

            assertTrue(
                    ExtendedItems
                            .validate(item)
                            .isValid(),
                    expected.persistentId());
        }
    }

    private static boolean isPublishedItemIdField(
            Field field)
    {
        int modifiers =
                field.getModifiers();

        return Modifier.isPublic(modifiers)
                && Modifier.isStatic(modifiers)
                && Modifier.isFinal(modifiers)
                && field.getType()
                .equals(ExtendedItemId.class);
    }

    private static ExpectedItem expected(
            ExtendedItemId id,
            String persistentId,
            String displayName,
            Material material)
    {
        return new ExpectedItem(
                id,
                persistentId,
                displayName,
                material);
    }

    private record ExpectedItem(
            ExtendedItemId id,
            String persistentId,
            String displayName,
            Material material)
    {
    }
}
