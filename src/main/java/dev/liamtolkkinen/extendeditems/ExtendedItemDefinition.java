package dev.liamtolkkinen.extendeditems;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

/**
 * Immutable definition of the ExtendedItems-owned portion of a custom item.
 *
 * <p>Gameplay-specific instance state belongs to the consuming plugin and is intentionally not
 * represented here.</p>
 */
public record ExtendedItemDefinition(
    ExtendedItemId id,
    int version,
    Material material,
    Component displayName,
    List<Component> lore,
    boolean glint,
    Set<ItemFlag> itemFlags
) {
    public ExtendedItemDefinition {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(material, "material");
        Objects.requireNonNull(displayName, "displayName");
        Objects.requireNonNull(lore, "lore");
        Objects.requireNonNull(itemFlags, "itemFlags");

        if (version <= 0) {
            throw new IllegalArgumentException("version must be greater than zero");
        }

        lore = List.copyOf(lore);
        itemFlags = Set.copyOf(itemFlags);
    }

    public String persistentId() {
        return id.persistentId();
    }
}
