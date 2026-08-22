package dev.liamtolkkinen.extendeditems;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.liamtolkkinen.extendeditems.internal.ExtendedItemKeys;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.Test;

class ExtendedItemIdentificationTests extends MockBukkitTestBase {
    @Test
    void validItemResolvesToCorrectId() {
        ItemStack item = ExtendedItems.create(ExtendedItemId.CONSECRATED_KEYSTONE);

        assertTrue(ExtendedItems.is(item, ExtendedItemId.CONSECRATED_KEYSTONE));
        assertTrue(ExtendedItems.getId(item).filter(ExtendedItemId.CONSECRATED_KEYSTONE::equals).isPresent());
    }

    @Test
    void vanillaItemDoesNotResolveAsCustomItem() {
        ItemStack vanillaItem = new ItemStack(Material.ECHO_SHARD);

        assertFalse(ExtendedItems.is(vanillaItem, ExtendedItemId.CONSECRATED_KEYSTONE));
        assertTrue(ExtendedItems.getId(vanillaItem).isEmpty());
    }

    @Test
    void unknownPersistentIdDoesNotResolve() {
        ItemStack item = new ItemStack(Material.ECHO_SHARD);
        item.editMeta(meta -> meta.getPersistentDataContainer().set(
            ExtendedItemKeys.ID,
            PersistentDataType.STRING,
            "future_unknown_item"));

        assertTrue(ExtendedItems.getId(item).isEmpty());
        assertFalse(ExtendedItems.is(item, ExtendedItemId.CONSECRATED_KEYSTONE));
    }

    @Test
    void malformedIdTypeDoesNotResolveDuringIdentification() {
        ItemStack item = new ItemStack(Material.ECHO_SHARD);
        item.editMeta(meta -> meta.getPersistentDataContainer().set(
            ExtendedItemKeys.ID,
            PersistentDataType.INTEGER,
            123));

        assertTrue(ExtendedItems.getId(item).isEmpty());
        assertFalse(ExtendedItems.is(item, ExtendedItemId.CONSECRATED_KEYSTONE));
    }

    @Test
    void identificationRemainsSeparateFromValidation() {
        ItemStack item = ExtendedItems.create(ExtendedItemId.CONSECRATED_KEYSTONE);
        item.editMeta(meta -> meta.getPersistentDataContainer().set(
            ExtendedItemKeys.VERSION,
            PersistentDataType.INTEGER,
            999));

        assertTrue(ExtendedItems.is(item, ExtendedItemId.CONSECRATED_KEYSTONE));
        assertFalse(ExtendedItems.validate(item).isValid());
    }
}
