package dev.liamtolkkinen.extendeditems.internal;

import dev.liamtolkkinen.extendeditems.ExtendedItemDefinition;
import dev.liamtolkkinen.extendeditems.ExtendedItemId;
import dev.liamtolkkinen.extendeditems.ExtendedItemService;
import dev.liamtolkkinen.extendeditems.ExtendedItemValidationResult;
import dev.liamtolkkinen.extendeditems.ExtendedItemValidationStatus;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * Default implementation backed by the immutable built-in item registry.
 */
public final class DefaultExtendedItemService implements ExtendedItemService {
    private final ExtendedItemRegistry registry;

    public DefaultExtendedItemService(ExtendedItemRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry");
    }

    public static DefaultExtendedItemService createDefault() {
        ExtendedItemDefinition consecratedKeystone = new ExtendedItemDefinition(
            ExtendedItemId.CONSECRATED_KEYSTONE,
            1,
            Material.ECHO_SHARD,
            Component.text("Consecrated Keystone"),
            List.of(
                Component.text("A divine artifact used to"),
                Component.text("strengthen a Sanctuary.")),
            false,
            Set.of());

        return new DefaultExtendedItemService(
            new ExtendedItemRegistry(List.of(consecratedKeystone)));
    }

    @Override
    public ItemStack create(ExtendedItemId id) {
        ExtendedItemDefinition definition = registry.require(id);
        ItemStack item = new ItemStack(definition.material());
        ItemMetaFactory.apply(item, definition);
        return item;
    }

    @Override
    public boolean is(ItemStack item, ExtendedItemId expected) {
        Objects.requireNonNull(expected, "expected");
        return getId(item).filter(expected::equals).isPresent();
    }

    @Override
    public Optional<ExtendedItemId> getId(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return Optional.empty();
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return Optional.empty();
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(ExtendedItemKeys.ID, PersistentDataType.STRING)) {
            return Optional.empty();
        }

        String persistentId = pdc.get(ExtendedItemKeys.ID, PersistentDataType.STRING);
        return ExtendedItemId.fromPersistentId(persistentId);
    }

    @Override
    public ExtendedItemValidationResult validate(ItemStack item) {
        if (item == null) {
            return result(
                ExtendedItemValidationStatus.INVALID_FORMAT,
                null,
                null,
                "ItemStack is null.");
        }

        if (item.getType().isAir()) {
            return result(
                ExtendedItemValidationStatus.MISSING_ID,
                null,
                null,
                "Air does not contain an ExtendedItems ID.");
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return result(
                ExtendedItemValidationStatus.MISSING_ID,
                null,
                null,
                "Item has no ItemMeta and therefore no ExtendedItems ID.");
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        if (!pdc.has(ExtendedItemKeys.ID)) {
            return result(
                ExtendedItemValidationStatus.MISSING_ID,
                null,
                null,
                "Missing extendeditems:id metadata.");
        }

        if (!pdc.has(ExtendedItemKeys.ID, PersistentDataType.STRING)) {
            return result(
                ExtendedItemValidationStatus.INVALID_FORMAT,
                null,
                null,
                "extendeditems:id exists but is not a STRING.");
        }

        String persistentId = pdc.get(ExtendedItemKeys.ID, PersistentDataType.STRING);
        if (persistentId == null || persistentId.isBlank()) {
            return result(
                ExtendedItemValidationStatus.INVALID_FORMAT,
                null,
                null,
                "extendeditems:id is not a non-blank STRING.");
        }

        Optional<ExtendedItemDefinition> definitionOptional =
            registry.findByPersistentId(persistentId);
        if (definitionOptional.isEmpty()) {
            return result(
                ExtendedItemValidationStatus.UNKNOWN_ITEM,
                null,
                readVersionIfInteger(pdc),
                "Unknown ExtendedItems persistent ID '" + persistentId + "'.");
        }

        ExtendedItemDefinition definition = definitionOptional.get();

        if (!pdc.has(ExtendedItemKeys.VERSION)) {
            return result(
                ExtendedItemValidationStatus.MISSING_VERSION,
                definition.id(),
                null,
                "Missing extendeditems:version metadata.");
        }

        if (!pdc.has(ExtendedItemKeys.VERSION, PersistentDataType.INTEGER)) {
            return result(
                ExtendedItemValidationStatus.INVALID_FORMAT,
                definition.id(),
                null,
                "extendeditems:version exists but is not an INTEGER.");
        }

        Integer version = pdc.get(ExtendedItemKeys.VERSION, PersistentDataType.INTEGER);
        if (version == null || version <= 0) {
            return result(
                ExtendedItemValidationStatus.INVALID_FORMAT,
                definition.id(),
                version,
                "extendeditems:version is not a positive INTEGER.");
        }

        if (version != definition.version()) {
            return result(
                ExtendedItemValidationStatus.UNSUPPORTED_VERSION,
                definition.id(),
                version,
                "Unsupported format version " + version + " for " + definition.id()
                    + "; supported version is " + definition.version() + ".");
        }

        if (item.getType() != definition.material()) {
            return result(
                ExtendedItemValidationStatus.INVALID_MATERIAL,
                definition.id(),
                version,
                "Expected material " + definition.material() + " but found " + item.getType() + ".");
        }

        return result(
            ExtendedItemValidationStatus.VALID,
            definition.id(),
            version,
            "Item is a valid " + definition.id() + ".");
    }

    ExtendedItemRegistry registry() {
        return registry;
    }

    private static Integer readVersionIfInteger(PersistentDataContainer pdc) {
        if (!pdc.has(ExtendedItemKeys.VERSION, PersistentDataType.INTEGER)) {
            return null;
        }

        return pdc.get(ExtendedItemKeys.VERSION, PersistentDataType.INTEGER);
    }

    private static ExtendedItemValidationResult result(
        ExtendedItemValidationStatus status,
        ExtendedItemId itemId,
        Integer foundVersion,
        String detail
    ) {
        return ExtendedItemValidationResult.of(status, itemId, foundVersion, detail);
    }
}
