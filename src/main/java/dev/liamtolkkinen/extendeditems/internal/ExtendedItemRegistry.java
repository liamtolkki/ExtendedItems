package dev.liamtolkkinen.extendeditems.internal;

import dev.liamtolkkinen.extendeditems.ExtendedItemDefinition;
import dev.liamtolkkinen.extendeditems.ExtendedItemId;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable registry used internally by the default service implementation.
 */
public final class ExtendedItemRegistry {
    private final Map<ExtendedItemId, ExtendedItemDefinition> byId;
    private final Map<String, ExtendedItemDefinition> byPersistentId;

    public ExtendedItemRegistry(Collection<ExtendedItemDefinition> definitions) {
        Objects.requireNonNull(definitions, "definitions");

        Map<ExtendedItemId, ExtendedItemDefinition> definitionsById = new HashMap<>();
        Map<String, ExtendedItemDefinition> definitionsByPersistentId = new HashMap<>();

        for (ExtendedItemDefinition definition : definitions) {
            Objects.requireNonNull(definition, "definitions must not contain null values");

            ExtendedItemDefinition duplicateId = definitionsById.put(definition.id(), definition);
            if (duplicateId != null) {
                throw new IllegalArgumentException(
                    "Duplicate ExtendedItemId registration: " + definition.id());
            }

            ExtendedItemDefinition duplicatePersistentId =
                definitionsByPersistentId.put(definition.persistentId(), definition);
            if (duplicatePersistentId != null) {
                throw new IllegalArgumentException(
                    "Duplicate persistent ID registration: " + definition.persistentId());
            }
        }

        this.byId = Collections.unmodifiableMap(definitionsById);
        this.byPersistentId = Collections.unmodifiableMap(definitionsByPersistentId);
    }

    public ExtendedItemDefinition require(ExtendedItemId id) {
        Objects.requireNonNull(id, "id");

        ExtendedItemDefinition definition = byId.get(id);
        if (definition == null) {
            throw new IllegalArgumentException(
                "No ExtendedItems definition registered for " + id.persistentId());
        }

        return definition;
    }

    public Optional<ExtendedItemDefinition> find(ExtendedItemId id) {
        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(byId.get(id));
    }

    public Optional<ExtendedItemDefinition> findByPersistentId(String persistentId) {
        if (persistentId == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(byPersistentId.get(persistentId));
    }

    public Collection<ExtendedItemDefinition> definitions() {
        return byId.values();
    }
}
