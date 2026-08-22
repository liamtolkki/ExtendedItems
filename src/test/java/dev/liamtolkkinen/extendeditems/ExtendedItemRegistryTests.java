package dev.liamtolkkinen.extendeditems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.liamtolkkinen.extendeditems.internal.ExtendedItemRegistry;
import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

class ExtendedItemRegistryTests
    extends MockBukkitTestBase
{
    @Test
    void itemIdRequiresPersistentId() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ExtendedItemId(null));

        assertThrows(
            IllegalArgumentException.class,
            () -> new ExtendedItemId(""));

        assertThrows(
            IllegalArgumentException.class,
            () -> new ExtendedItemId("   "));
    }

    @Test
    void itemIdsUsePersistentValueEquality() {
        ExtendedItemId first =
            new ExtendedItemId("same_test_id");

        ExtendedItemId second =
            new ExtendedItemId("same_test_id");

        assertEquals(
            first,
            second);

        assertEquals(
            first.hashCode(),
            second.hashCode());
    }

    @Test
    void duplicateRegistrationIsRejected() {
        ExtendedItemDefinition first =
            definition(
                TestItems.STANDARD_ID,
                1);

        ExtendedItemDefinition second =
            definition(
                new ExtendedItemId(
                    TestItems.STANDARD_ID
                        .persistentId()),
                2);

        assertThrows(
            IllegalArgumentException.class,
            () -> new ExtendedItemRegistry(
                List.of(
                    first,
                    second)));
    }

    @Test
    void definitionRequiresPositiveVersion() {
        assertThrows(
            IllegalArgumentException.class,
            () -> definition(
                TestItems.STANDARD_ID,
                0));
    }

    @Test
    void emptyRegistryIsValid() {
        ExtendedItemRegistry registry =
            new ExtendedItemRegistry(
                List.of());

        assertTrue(
            registry
                .definitions()
                .isEmpty());
    }

    @Test
    void productionFacadeRecognizesReleasedItem() {
        var item =
            ExtendedItems.create(
                ExtendedItemIds.SENTRY_IRON_GOLEM);

        assertTrue(
            ExtendedItems.is(
                item,
                ExtendedItemIds.SENTRY_IRON_GOLEM));

        assertTrue(
            ExtendedItems
                .validate(item)
                .isValid());
    }

    @Test
    void productionFacadeDoesNotRecognizeTestOnlyItem() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ExtendedItems.create(
                TestItems.UNREGISTERED_ID));

        assertTrue(
            ExtendedItems
                .getId(
                    TestItems.createStandard())
                .isEmpty());

        assertEquals(
            ExtendedItemValidationStatus.UNKNOWN_ITEM,
            ExtendedItems
                .validate(
                    TestItems.createStandard())
                .status());
    }

    private static ExtendedItemDefinition definition(
        ExtendedItemId id,
        int version)
    {
        return new ExtendedItemDefinition(
            id,
            version,
            Material.ECHO_SHARD,
            Component.text("Test"),
            List.of(),
            false,
            Set.of());
    }
}
