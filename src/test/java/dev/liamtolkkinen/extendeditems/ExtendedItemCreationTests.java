package dev.liamtolkkinen.extendeditems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    void consecratedKeystoneHasExpectedMaterialAndMetadata() {
        ItemStack item = ExtendedItems.create(ExtendedItemId.CONSECRATED_KEYSTONE);
        ItemMeta meta = item.getItemMeta();

        assertEquals(Material.ECHO_SHARD, item.getType());
        assertEquals("sanctuary_consecrated_keystone", meta.getPersistentDataContainer().get(
            ExtendedItemKeys.ID,
            PersistentDataType.STRING));
        assertEquals(1, meta.getPersistentDataContainer().get(
            ExtendedItemKeys.VERSION,
            PersistentDataType.INTEGER));
    }

    @Test
    void consecratedKeystoneHasExpectedDisplayMetadata() {
        ItemStack item = ExtendedItems.create(ExtendedItemId.CONSECRATED_KEYSTONE);
        ItemMeta meta = item.getItemMeta();

        assertEquals(Component.text("Consecrated Keystone"), meta.displayName());
        assertEquals(
            List.of(
                Component.text("A divine artifact used to"),
                Component.text("strengthen a Sanctuary.")),
            meta.lore());
        assertFalse(meta.hasEnchantmentGlintOverride());
    }
}
