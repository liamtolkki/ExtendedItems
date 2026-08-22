package dev.liamtolkkinen.extendeditems;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.liamtolkkinen.extendeditems.internal.ExtendedItemKeys;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.Test;

class ExtendedItemIdentificationTests
    extends MockBukkitTestBase
{
    @Test
    void validItemResolvesToCorrectId() {
        ItemStack item = TestItems.createStandard();

        assertTrue(
            TestItems.SERVICE.is(
                item,
                TestItems.STANDARD_ID));

        assertTrue(
            TestItems.SERVICE
                .getId(item)
                .filter(TestItems.STANDARD_ID::equals)
                .isPresent());

        assertFalse(
            TestItems.SERVICE.is(
                item,
                TestItems.GLOWING_ID));
    }

    @Test
    void vanillaItemDoesNotResolveAsCustomItem() {
        ItemStack vanillaItem =
            new ItemStack(Material.ECHO_SHARD);

        assertFalse(
            TestItems.SERVICE.is(
                vanillaItem,
                TestItems.STANDARD_ID));

        assertTrue(
            TestItems.SERVICE
                .getId(vanillaItem)
                .isEmpty());
    }

    @Test
    void unknownPersistentIdDoesNotResolve() {
        ItemStack item =
            new ItemStack(Material.ECHO_SHARD);

        item.editMeta(meta ->
            meta.getPersistentDataContainer().set(
                ExtendedItemKeys.ID,
                PersistentDataType.STRING,
                "future_unknown_item"));

        assertTrue(
            TestItems.SERVICE
                .getId(item)
                .isEmpty());

        assertFalse(
            TestItems.SERVICE.is(
                item,
                TestItems.STANDARD_ID));
    }

    @Test
    void malformedIdTypeDoesNotResolveDuringIdentification() {
        ItemStack item =
            new ItemStack(Material.ECHO_SHARD);

        item.editMeta(meta ->
            meta.getPersistentDataContainer().set(
                ExtendedItemKeys.ID,
                PersistentDataType.INTEGER,
                123));

        assertTrue(
            TestItems.SERVICE
                .getId(item)
                .isEmpty());

        assertFalse(
            TestItems.SERVICE.is(
                item,
                TestItems.STANDARD_ID));
    }

    @Test
    void identificationRemainsSeparateFromValidation() {
        ItemStack item =
            TestItems.createStandard();

        item.editMeta(meta ->
            meta.getPersistentDataContainer().set(
                ExtendedItemKeys.VERSION,
                PersistentDataType.INTEGER,
                999));

        assertTrue(
            TestItems.SERVICE.is(
                item,
                TestItems.STANDARD_ID));

        assertFalse(
            TestItems.SERVICE
                .validate(item)
                .isValid());
    }
}
