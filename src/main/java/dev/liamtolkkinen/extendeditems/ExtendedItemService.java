package dev.liamtolkkinen.extendeditems;

import java.util.Optional;
import org.bukkit.inventory.ItemStack;

/**
 * Public service contract for creating, identifying, and validating shared custom items.
 */
public interface ExtendedItemService {
    ItemStack create(ExtendedItemId id);

    boolean is(ItemStack item, ExtendedItemId expected);

    Optional<ExtendedItemId> getId(ItemStack item);

    ExtendedItemValidationResult validate(ItemStack item);
}
