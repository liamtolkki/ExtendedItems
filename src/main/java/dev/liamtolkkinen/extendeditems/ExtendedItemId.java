package dev.liamtolkkinen.extendeditems;

import java.util.Objects;

/**
 * Stable logical identifier for an ExtendedItems-managed item.
 *
 * <p>The persisted ID is explicit and must never be derived from a Java field name. Instances are
 * intentionally created only inside this package so consuming plugins use IDs published by
 * ExtendedItems rather than inventing their own.</p>
 */
public final class ExtendedItemId {
    private final String persistentId;

    ExtendedItemId(String persistentId) {
        if (persistentId == null || persistentId.isBlank()) {
            throw new IllegalArgumentException("persistentId must not be blank");
        }

        this.persistentId = persistentId;
    }

    /**
     * Gets the stable string stored in {@code extendeditems:id}.
     *
     * @return persistent item ID
     */
    public String persistentId() {
        return persistentId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof ExtendedItemId that)) {
            return false;
        }

        return persistentId.equals(that.persistentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persistentId);
    }

    @Override
    public String toString() {
        return persistentId;
    }
}
