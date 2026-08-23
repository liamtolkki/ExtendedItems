package dev.liamtolkkinen.extendeditems.internal;

import dev.liamtolkkinen.extendeditems.ExtendedItemDefinition;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/**
 * Applies only ExtendedItems-owned metadata to a newly created ItemStack.
 */
public final class ItemMetaFactory {
    private ItemMetaFactory() {
    }

    public static void apply(ItemStack item, ExtendedItemDefinition definition) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(definition, "definition");

        item.editMeta(meta -> {
            meta.displayName(definition.displayName());
            meta.lore(definition.lore());

            if (definition.glint()) {
                meta.setEnchantmentGlintOverride(true);

                /*
                 * Ender Chest item rendering can make the component-only glint
                 * override effectively invisible on some clients/render paths.
                 * A hidden harmless enchantment supplies a second glint source
                 * without changing the visible tooltip or item identity.
                 */
                if (definition.material() == Material.ENDER_CHEST) {
                    meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
            }

            if (!definition.itemFlags().isEmpty()) {
                meta.addItemFlags(definition.itemFlags().toArray(ItemFlag[]::new));
            }

            meta.getPersistentDataContainer().set(
                ExtendedItemKeys.ID,
                PersistentDataType.STRING,
                definition.persistentId());
            meta.getPersistentDataContainer().set(
                ExtendedItemKeys.VERSION,
                PersistentDataType.INTEGER,
                definition.version());
        });
    }
}
