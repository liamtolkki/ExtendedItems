package dev.liamtolkkinen.extendeditems;

/**
 * High-level result of validating an ItemStack against the ExtendedItems contract.
 */
public enum ExtendedItemValidationStatus {
    VALID,
    UNKNOWN_ITEM,
    MISSING_ID,
    MISSING_VERSION,
    UNSUPPORTED_VERSION,
    INVALID_MATERIAL,
    INVALID_FORMAT
}
