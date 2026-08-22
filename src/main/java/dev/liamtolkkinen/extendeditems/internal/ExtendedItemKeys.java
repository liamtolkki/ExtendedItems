package dev.liamtolkkinen.extendeditems.internal;

import dev.liamtolkkinen.extendeditems.ExtendedItems;
import org.bukkit.NamespacedKey;

public final class ExtendedItemKeys {
    public static final NamespacedKey ID = new NamespacedKey(ExtendedItems.NAMESPACE, "id");
    public static final NamespacedKey VERSION = new NamespacedKey(ExtendedItems.NAMESPACE, "version");

    private ExtendedItemKeys() {
    }
}
