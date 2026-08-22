package dev.liamtolkkinen.extendeditems;

import java.util.Objects;
import java.util.Optional;

/**
 * Structured validation result for an ExtendedItems item.
 *
 * @param status validation status
 * @param itemId recognized item ID when one could be resolved
 * @param foundVersion version read from the item when available
 * @param detail diagnostic detail intended for logs and debugging
 */
public record ExtendedItemValidationResult(
    ExtendedItemValidationStatus status,
    Optional<ExtendedItemId> itemId,
    Optional<Integer> foundVersion,
    String detail
) {
    public ExtendedItemValidationResult {
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(itemId, "itemId");
        Objects.requireNonNull(foundVersion, "foundVersion");
        Objects.requireNonNull(detail, "detail");
    }

    public boolean isValid() {
        return status == ExtendedItemValidationStatus.VALID;
    }

    public static ExtendedItemValidationResult of(
        ExtendedItemValidationStatus status,
        ExtendedItemId itemId,
        Integer foundVersion,
        String detail
    ) {
        return new ExtendedItemValidationResult(
            status,
            Optional.ofNullable(itemId),
            Optional.ofNullable(foundVersion),
            detail);
    }
}
