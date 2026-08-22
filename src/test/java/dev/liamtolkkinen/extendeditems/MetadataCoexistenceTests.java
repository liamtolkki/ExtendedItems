package dev.liamtolkkinen.extendeditems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.Test;

class MetadataCoexistenceTests
    extends MockBukkitTestBase
{
    private static final NamespacedKey SANCTUARY_ANCHOR_ID =
        new NamespacedKey(
            "sanctuary",
            "anchor_id");

    private static final NamespacedKey SANCTUARY_TIER =
        new NamespacedKey(
            "sanctuary",
            "tier");

    @Test
    void consumingPluginMetadataCoexistsWithExtendedItemsMetadata() {
        ItemStack item =
            TestItems.createStandard();

        String anchorId =
            UUID.randomUUID().toString();

        item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(
                SANCTUARY_ANCHOR_ID,
                PersistentDataType.STRING,
                anchorId);

            meta.getPersistentDataContainer().set(
                SANCTUARY_TIER,
                PersistentDataType.INTEGER,
                2);
        });

        assertTrue(
            TestItems.SERVICE
                .validate(item)
                .isValid());

        assertEquals(
            anchorId,
            item.getItemMeta()
                .getPersistentDataContainer()
                .get(
                    SANCTUARY_ANCHOR_ID,
                    PersistentDataType.STRING));

        assertEquals(
            2,
            item.getItemMeta()
                .getPersistentDataContainer()
                .get(
                    SANCTUARY_TIER,
                    PersistentDataType.INTEGER));
    }
}
