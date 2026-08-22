package dev.liamtolkkinen.extendeditems;

import dev.liamtolkkinen.extendeditems.internal.DefaultExtendedItemService;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;

/**
 * Static entry point for the shared ExtendedItems contract.
 *
 * <p>ExtendedItems is a Java library, not a Paper plugin. Consuming plugins should include it as a
 * build dependency and shade/relocate it into their own plugin JAR.</p>
 */
public final class ExtendedItems {
    public static final String NAMESPACE = "extendeditems";

    private static final ExtendedItemService SERVICE = DefaultExtendedItemService.createDefault();

    private ExtendedItems() {
    }

    public static ExtendedItemService service() {
        return SERVICE;
    }

    public static ItemStack create(ExtendedItemId id) {
        return SERVICE.create(id);
    }

    public static boolean is(ItemStack item, ExtendedItemId expected) {
        return SERVICE.is(item, expected);
    }

    public static Optional<ExtendedItemId> getId(ItemStack item) {
        return SERVICE.getId(item);
    }

    public static ExtendedItemValidationResult validate(ItemStack item) {
        return SERVICE.validate(item);
    }
}
