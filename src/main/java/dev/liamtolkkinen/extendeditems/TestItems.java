package dev.liamtolkkinen.extendeditems;

import dev.liamtolkkinen.extendeditems.internal.DefaultExtendedItemService;
import dev.liamtolkkinen.extendeditems.internal.ExtendedItemRegistry;
import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

final class TestItems {
    static final ExtendedItemId STANDARD_ID =
        new ExtendedItemId("test_standard_item");

    static final ExtendedItemId GLOWING_ID =
        new ExtendedItemId("test_glowing_item");

    static final ExtendedItemId UNREGISTERED_ID =
        new ExtendedItemId("test_unregistered_item");

    static final ExtendedItemDefinition STANDARD_DEFINITION =
        new ExtendedItemDefinition(
            STANDARD_ID,
            1,
            Material.ECHO_SHARD,
            Component.text("Test Artifact"),
            List.of(
                Component.text("Test lore line one."),
                Component.text("Test lore line two.")),
            false,
            Set.of());

    static final ExtendedItemDefinition GLOWING_DEFINITION =
        new ExtendedItemDefinition(
            GLOWING_ID,
            1,
            Material.AMETHYST_SHARD,
            Component.text("Glowing Test Artifact"),
            List.of(),
            true,
            Set.of());

    static final ExtendedItemService SERVICE =
        new DefaultExtendedItemService(
            new ExtendedItemRegistry(
                List.of(
                    STANDARD_DEFINITION,
                    GLOWING_DEFINITION)));

    private TestItems() {
    }

    static ItemStack createStandard() {
        return SERVICE.create(STANDARD_ID);
    }

    static ItemStack createGlowing() {
        return SERVICE.create(GLOWING_ID);
    }
}
