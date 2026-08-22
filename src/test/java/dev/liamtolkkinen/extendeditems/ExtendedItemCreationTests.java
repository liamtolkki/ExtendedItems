package dev.liamtolkkinen.extendeditems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.liamtolkkinen.extendeditems.internal.ExtendedItemKeys;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.Test;

class ExtendedItemCreationTests extends MockBukkitTestBase {
    @Test
    void registeredItemHasExpectedMaterialAndMetadata() {
        ItemStack item = TestItems.createStandard();
        ItemMeta meta = item.getItemMeta();

        assertEquals(
            Material.ECHO_SHARD,
            item.getType());

        assertEquals(
            "test_standard_item",
            meta.getPersistentDataContainer().get(
                ExtendedItemKeys.ID,
                PersistentDataType.STRING));

        assertEquals(
            1,
            meta.getPersistentDataContainer().get(
                ExtendedItemKeys.VERSION,
                PersistentDataType.INTEGER));
    }

    @Test
    void registeredItemHasExpectedDisplayMetadata() {
        ItemStack item = TestItems.createStandard();
        ItemMeta meta = item.getItemMeta();

        assertEquals(
            Component.text("Test Artifact"),
            meta.displayName());

        assertEquals(
            List.of(
                Component.text("Test lore line one."),
                Component.text("Test lore line two.")),
            meta.lore());

        assertFalse(
            meta.hasEnchantmentGlintOverride());
    }

    @Test
    void definitionCanEnableGlint() {
        ItemStack item = TestItems.createGlowing();
        ItemMeta meta = item.getItemMeta();

        assertTrue(
            meta.hasEnchantmentGlintOverride());

        assertTrue(
            meta.getEnchantmentGlintOverride());
    }
}
