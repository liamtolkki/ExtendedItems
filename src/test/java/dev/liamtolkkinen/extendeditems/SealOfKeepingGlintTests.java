package dev.liamtolkkinen.extendeditems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;

class SealOfKeepingGlintTests extends MockBukkitTestBase {

    @Test
    void sealOfKeepingUsesShulkerShellWithGlintOverride() {
        var item = ExtendedItems.create(ExtendedItemIds.SEAL_OF_KEEPING);
        var meta = item.getItemMeta();

        assertEquals(Material.SHULKER_SHELL, item.getType());
        assertTrue(meta.hasEnchantmentGlintOverride());
        assertTrue(meta.getEnchantmentGlintOverride());
    }
}
