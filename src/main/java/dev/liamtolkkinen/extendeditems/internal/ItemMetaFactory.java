package dev.liamtolkkinen.extendeditems.internal;

import dev.liamtolkkinen.extendeditems.ExtendedItemDefinition;
import java.util.Objects;
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
            }

            if (!definition.itemFlags().isEmpty()) {
                meta.addItemFlags(definition.itemFlags().toArray(org.bukkit.inventory.ItemFlag[]::new));
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
