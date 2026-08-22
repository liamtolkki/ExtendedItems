package dev.liamtolkkinen.extendeditems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.liamtolkkinen.extendeditems.internal.ExtendedItemKeys;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.Test;

class ExtendedItemValidationTests extends MockBukkitTestBase {
    @Test
    void validItemPassesValidation() {
        ExtendedItemValidationResult result = ExtendedItems.validate(
            ExtendedItems.create(ExtendedItemId.CONSECRATED_KEYSTONE));

        assertTrue(result.isValid());
        assertEquals(ExtendedItemValidationStatus.VALID, result.status());
        assertEquals(ExtendedItemId.CONSECRATED_KEYSTONE, result.itemId().orElseThrow());
        assertEquals(1, result.foundVersion().orElseThrow());
    }

    @Test
    void missingIdIsRejected() {
        ExtendedItemValidationResult result = ExtendedItems.validate(
            new ItemStack(Material.ECHO_SHARD));

        assertEquals(ExtendedItemValidationStatus.MISSING_ID, result.status());
    }

    @Test
    void unknownIdIsRejected() {
        ItemStack item = new ItemStack(Material.ECHO_SHARD);
        item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(
                ExtendedItemKeys.ID,
                PersistentDataType.STRING,
                "future_unknown_item");
            meta.getPersistentDataContainer().set(
                ExtendedItemKeys.VERSION,
                PersistentDataType.INTEGER,
                1);
        });

        ExtendedItemValidationResult result = ExtendedItems.validate(item);

        assertEquals(ExtendedItemValidationStatus.UNKNOWN_ITEM, result.status());
    }

    @Test
    void missingVersionIsRejected() {
        ItemStack item = ExtendedItems.create(ExtendedItemId.CONSECRATED_KEYSTONE);
        item.editMeta(meta -> meta.getPersistentDataContainer().remove(ExtendedItemKeys.VERSION));

        ExtendedItemValidationResult result = ExtendedItems.validate(item);

        assertEquals(ExtendedItemValidationStatus.MISSING_VERSION, result.status());
    }

    @Test
    void unsupportedVersionIsRejected() {
        ItemStack item = ExtendedItems.create(ExtendedItemId.CONSECRATED_KEYSTONE);
        item.editMeta(meta -> meta.getPersistentDataContainer().set(
            ExtendedItemKeys.VERSION,
            PersistentDataType.INTEGER,
            999));

        ExtendedItemValidationResult result = ExtendedItems.validate(item);

        assertEquals(ExtendedItemValidationStatus.UNSUPPORTED_VERSION, result.status());
        assertEquals(999, result.foundVersion().orElseThrow());
    }

    @Test
    void invalidMaterialIsRejected() {
        ItemStack item = ExtendedItems.create(ExtendedItemId.CONSECRATED_KEYSTONE);
        item.setType(Material.DIRT);

        ExtendedItemValidationResult result = ExtendedItems.validate(item);

        assertEquals(ExtendedItemValidationStatus.INVALID_MATERIAL, result.status());
    }

    @Test
    void malformedIdTypeIsRejectedAsInvalidFormat() {
        ItemStack item = new ItemStack(Material.ECHO_SHARD);
        item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(
                ExtendedItemKeys.ID,
                PersistentDataType.INTEGER,
                123);
            meta.getPersistentDataContainer().set(
                ExtendedItemKeys.VERSION,
                PersistentDataType.INTEGER,
                1);
        });

        ExtendedItemValidationResult result = ExtendedItems.validate(item);

        assertFalse(result.isValid());
        assertEquals(ExtendedItemValidationStatus.INVALID_FORMAT, result.status());
    }

    @Test
    void malformedVersionTypeIsRejectedAsInvalidFormat() {
        ItemStack item = ExtendedItems.create(ExtendedItemId.CONSECRATED_KEYSTONE);
        item.editMeta(meta -> meta.getPersistentDataContainer().set(
            ExtendedItemKeys.VERSION,
            PersistentDataType.STRING,
            "one"));

        ExtendedItemValidationResult result = ExtendedItems.validate(item);

        assertEquals(ExtendedItemValidationStatus.INVALID_FORMAT, result.status());
    }
}
