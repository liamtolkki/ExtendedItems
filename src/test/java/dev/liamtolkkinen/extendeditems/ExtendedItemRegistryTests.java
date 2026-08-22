package dev.liamtolkkinen.extendeditems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.liamtolkkinen.extendeditems.internal.ExtendedItemRegistry;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

class ExtendedItemRegistryTests extends MockBukkitTestBase {
    @Test
    void persistentIdsAreUnique() {
        Set<String> persistentIds = new HashSet<>();

        Arrays.stream(ExtendedItemId.values()).forEach(id ->
            assertTrue(
                persistentIds.add(id.persistentId()),
                () -> "Duplicate persistent ID: " + id.persistentId()));
    }

    @Test
    void duplicateEnumRegistrationIsRejected() {
        ExtendedItemDefinition first = definition(1);
        ExtendedItemDefinition second = definition(2);

        assertThrows(
            IllegalArgumentException.class,
            () -> new ExtendedItemRegistry(List.of(first, second)));
    }

    @Test
    void definitionRequiresPositiveVersion() {
        assertThrows(
            IllegalArgumentException.class,
            () -> definition(0));
    }

    @Test
    void defaultServiceRegistersEveryEnumValue() {
        for (ExtendedItemId id : ExtendedItemId.values()) {
            ItemStack item = ExtendedItems.create(id);
            assertEquals(id, ExtendedItems.getId(item).orElseThrow());
        }
    }

    private static ExtendedItemDefinition definition(int version) {
        return new ExtendedItemDefinition(
            ExtendedItemId.CONSECRATED_KEYSTONE,
            version,
            Material.ECHO_SHARD,
            Component.text("Test"),
            List.of(),
            false,
            Set.of());
    }
}
