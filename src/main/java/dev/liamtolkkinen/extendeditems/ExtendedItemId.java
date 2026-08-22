package dev.liamtolkkinen.extendeditems;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Stable logical identifiers for ExtendedItems-managed items.
 *
 * <p>The persisted ID is explicit and must not be derived from the Java enum name. Once a
 * persisted ID is released into real inventories it becomes a compatibility contract.</p>
 */
public enum ExtendedItemId {
    CONSECRATED_KEYSTONE("sanctuary_consecrated_keystone");

    private static final Map<String, ExtendedItemId> BY_PERSISTENT_ID = buildPersistentIdLookup();

    private final String persistentId;

    ExtendedItemId(String persistentId) {
        if (persistentId == null || persistentId.isBlank()) {
            throw new IllegalArgumentException("persistentId must not be blank");
        }

        this.persistentId = persistentId;
    }

    public String persistentId() {
        return persistentId;
    }

    public static Optional<ExtendedItemId> fromPersistentId(String persistentId) {
        if (persistentId == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(BY_PERSISTENT_ID.get(persistentId));
    }

    private static Map<String, ExtendedItemId> buildPersistentIdLookup() {
        Map<String, ExtendedItemId> valuesByPersistentId = new HashMap<>();

        Arrays.stream(values()).forEach(id -> {
            ExtendedItemId previous = valuesByPersistentId.put(id.persistentId, id);
            if (previous != null) {
                throw new IllegalStateException(
                    "Duplicate ExtendedItems persistent ID '" + id.persistentId + "' for "
                        + previous.name() + " and " + id.name());
            }
        });

        return Collections.unmodifiableMap(valuesByPersistentId);
    }
}
