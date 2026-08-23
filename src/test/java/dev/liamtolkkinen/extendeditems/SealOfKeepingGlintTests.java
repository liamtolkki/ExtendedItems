package dev.liamtolkkinen.extendeditems;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.junit.jupiter.api.Test;

class SealOfKeepingGlintTests extends MockBukkitTestBase {

    @Test
    void sealOfKeepingHasVisibleGlintFallback() {
        var item = ExtendedItems.create(ExtendedItemIds.SEAL_OF_KEEPING);
        var meta = item.getItemMeta();

        assertTrue(meta.hasEnchantmentGlintOverride());
        assertTrue(meta.getEnchantmentGlintOverride());
        assertTrue(meta.hasEnchant(Enchantment.UNBREAKING));
        assertTrue(meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS));
    }
}
