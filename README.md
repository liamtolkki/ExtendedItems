# ExtendedItems Implementation Plan

## Purpose

ExtendedItems will be a shared Java library for identifying, creating, and validating custom server-side items used across multiple Paper plugins.

It is not a Paper plugin and will not be installed on the Minecraft server as a standalone runtime dependency.

Its primary purpose is to provide a stable cross-plugin contract for special items that may be created by one plugin and consumed or recognized by another.

Initial consumers will include:

- God
- Sanctuary
- Future progression plugins

The first major use case is allowing God to grant special artifacts through quests, Favor, or other progression events while Sanctuary recognizes and consumes those artifacts without depending directly on God.

## Core Design Goal

ExtendedItems defines item identity.

It does not own gameplay behavior.

For example:

```text
God
  |
  | grants
  v
Consecrated Keystone
  |
  | identified by
  v
ExtendedItems
  |
  | consumed by
  v
Sanctuary
```

Sanctuary should only need to know:

```text
This item is a valid Consecrated Keystone.
```

Sanctuary should not need to know:

- Why God granted it
- Whether it came from a quest
- Whether it was purchased with Favor
- What relationship value was required
- What conversation caused it to be awarded

Likewise, God should not need to know how Sanctuary uses the artifact after granting it.

## Dependency Structure

The intended dependency structure is:

```text
ExtendedItems
     |
     +-------- God
     |
     +-------- Sanctuary
     |
     +-------- Future plugins
```

ExtendedItems should remain independent of God, Sanctuary, or any other gameplay plugin.

It may depend on the Paper API for `ItemStack`, `ItemMeta`, `PersistentDataContainer`, `NamespacedKey`, and related types.

## Relationship to Other Shared Libraries

The initial shared library structure is expected to be:

```text
ExtendedUI
    Shared UI infrastructure

ExtendedItems
    Shared custom item identity and construction

God
    AI, quests, Favor, divine rewards

Sanctuary
    Beacons, conduits, territory, protections,
    trust, guards, and Sanctuary advancements
```

ExtendedItems should not depend on ExtendedUI.

ExtendedUI should not depend on ExtendedItems unless a future reusable UI component has a real need for item identity.

The libraries should remain independently usable.

## Responsibilities

ExtendedItems should own:

- Stable custom item IDs
- Shared PDC keys
- Item creation
- Item identification
- Item validation
- Item format versioning
- Common display metadata
- Common lore
- Enchantment glint presentation where appropriate
- Shared item flags where appropriate
- Optional migration support for older item formats
- Test utilities for validating custom items

ExtendedItems should not own:

- Favor balances
- Economy transactions
- Quests
- Sanctuary progression
- Beacon state
- Conduit state
- Player ownership rules
- Guard behavior
- Crafting progression logic
- Advancement progress
- Permission checks
- Persistent gameplay state

Those responsibilities remain with the consuming plugin.

## Item Identity

A custom item must never be identified solely by its vanilla `Material`, display name, lore, or enchantments.

For example, this is invalid:

```java
if (item.getType() == Material.ECHO_SHARD)
{
    // Treat as Consecrated Keystone
}
```

An ordinary Echo Shard must remain an ordinary Echo Shard.

Custom identity will be stored using `PersistentDataContainer` metadata.

Conceptually:

```text
extendeditems:id = sanctuary_consecrated_keystone
extendeditems:version = 1
```

Recommended data types:

```text
extendeditems:id
    STRING

extendeditems:version
    INTEGER
```

## Namespace

All ExtendedItems-owned metadata should use one stable namespace:

```text
extendeditems
```

Examples:

```text
extendeditems:id
extendeditems:version
```

Consuming plugins may add their own metadata under their own namespaces.

For example, a Sanctuary Beacon may contain both:

```text
extendeditems:id = sanctuary_beacon
extendeditems:version = 1
```

and:

```text
sanctuary:anchor_id = <UUID>
sanctuary:owner_uuid = <UUID>
sanctuary:tier = 2
```

ExtendedItems identifies what the item is.

Sanctuary owns what that particular instance means.

## Item IDs

Item IDs must be stable.

Once an item ID is released and used in persistent player inventories, containers, or saved worlds, it should not be casually renamed.

The Java API may expose strongly typed identifiers.

For example:

```java
public enum ExtendedItemId
{
    BLESSED_FOUNDATION,
    BLESSED_WARD,
    CONSECRATED_KEYSTONE,
    DIVINE_CORE,
    SANCTUARY_BEACON,
    SANCTUARY_CONDUIT
}
```

The persisted ID should be explicitly defined rather than derived from enum naming if long-term compatibility is important.

For example:

```java
CONSECRATED_KEYSTONE("sanctuary_consecrated_keystone")
```

This prevents a future Java refactor from silently changing the stored item identity.

## Item Definitions

Each custom item should have one centralized definition.

A definition may contain:

```text
ID
Vanilla Material
Display name
Lore
Glint state
Default item flags
Format version
```

Conceptually:

```java
public record ExtendedItemDefinition(
    ExtendedItemId id,
    String persistentId,
    int version,
    Material material,
    Component displayName,
    List<Component> lore,
    boolean glint)
{
}
```

The final implementation may use a class rather than a record if behavior is required.

## Item Creation

All standard creation of shared items should go through ExtendedItems.

Conceptually:

```java
ItemStack item =
    ExtendedItems.create(
        ExtendedItemId.CONSECRATED_KEYSTONE);
```

This method should:

1. Resolve the registered item definition.
2. Create the vanilla `ItemStack`.
3. Apply the display name.
4. Apply lore.
5. Apply glint if configured.
6. Apply common item flags.
7. Write the persistent item ID.
8. Write the item format version.
9. Return the finished `ItemStack`.

Consuming plugins should not duplicate the metadata format manually.

## Item Identification

ExtendedItems should provide direct identification helpers.

Conceptually:

```java
boolean ExtendedItems.is(
    ItemStack item,
    ExtendedItemId expected);
```

It should also support resolving an unknown item:

```java
Optional<ExtendedItemId> ExtendedItems.getId(
    ItemStack item);
```

Possible API:

```java
public interface ExtendedItemService
{
    ItemStack create(ExtendedItemId id);

    boolean is(
        ItemStack item,
        ExtendedItemId expected);

    Optional<ExtendedItemId> getId(
        ItemStack item);

    ExtendedItemValidationResult validate(
        ItemStack item);
}
```

The exact API will be finalized during implementation.

## Validation

Identification and validation should be separate concepts.

An item may contain a recognized ID but still be malformed.

Examples:

```text
extendeditems:id = sanctuary_consecrated_keystone
extendeditems:version = 999
```

or:

```text
ID says Consecrated Keystone
Material is unexpectedly DIRT
```

ExtendedItems should be able to report this.

Conceptually:

```text
VALID
UNKNOWN_ITEM
MISSING_ID
UNSUPPORTED_VERSION
INVALID_MATERIAL
INVALID_FORMAT
```

A consuming plugin can then choose whether to reject, migrate, or otherwise handle the item.

## Versioning

Versioning should exist from the first release.

Every shared custom item should include:

```text
extendeditems:version = 1
```

This version represents the ExtendedItems format of that item.

It does not represent:

- Sanctuary tier
- Beacon tier
- Plugin version
- Quest version
- Item quantity

Those values belong elsewhere.

For example:

```text
extendeditems:id = sanctuary_beacon
extendeditems:version = 1

sanctuary:tier = 3
```

These are separate concepts.

## Migration

The first version does not need a complex migration framework.

However, the API should leave room for migration later.

Possible future behavior:

```java
ItemStack migrated =
    ExtendedItems.migrate(item);
```

For v1, it is acceptable for validation to reject unsupported versions.

The important requirement is that version metadata exists before custom items begin appearing in real inventories.

## Generic Artifacts vs Stateful Items

ExtendedItems must distinguish between generic artifacts and stateful item instances.

### Generic Artifact

Example:

```text
Consecrated Keystone
```

This item is fungible.

One Consecrated Keystone is equivalent to another.

It may only need:

```text
extendeditems:id
extendeditems:version
```

It does not need a unique UUID.

### Stateful Item

Example:

```text
Sanctuary Beacon
```

This item may represent an existing persistent Sanctuary.

ExtendedItems should identify it as:

```text
extendeditems:id = sanctuary_beacon
```

Sanctuary then adds:

```text
sanctuary:anchor_id
sanctuary:owner_uuid
sanctuary:tier
```

ExtendedItems should not attempt to interpret those Sanctuary fields.

## Instance IDs

ExtendedItems should not automatically assign a unique instance ID to every custom item.

Generic artifacts should remain stackable when appropriate.

A unique instance ID should only exist when the consuming gameplay system requires one.

For v1, stateful plugins should own their own instance identifiers.

Example:

```text
sanctuary:anchor_id
```

rather than:

```text
extendeditems:instance_id
```

unless a future cross-plugin requirement justifies moving that concept into ExtendedItems.

## Stacking

Generic items should be allowed to stack when that fits gameplay.

Examples:

```text
Blessed Ward
Consecrated Keystone
Divine Fragment
```

may be stackable.

Stateful items such as a Sanctuary Beacon with a unique anchor ID should not rely on stacking.

The consuming plugin should prevent or avoid invalid stacking behavior for stateful items.

ExtendedItems should not assume that every custom item is unstackable.

## Display Metadata

ExtendedItems should centralize standard presentation for shared items.

For example:

```text
Consecrated Keystone

A divine artifact used to
strengthen a Sanctuary.
```

The library may apply:

- Custom display name
- Lore
- Glint
- Hidden item flags if needed
- Other stable presentation metadata

The display name and lore are presentation only.

They are not security or identity boundaries.

The PDC ID remains authoritative.

## Glint

Certain items may use the enchantment glint for visual distinction.

The glint should be configured by the item definition.

The implementation should prefer the modern Paper-supported item metadata mechanism rather than adding meaningless enchantments solely to force a glint when possible.

## Cross-Plugin Contract

ExtendedItems exists specifically so one plugin can safely create an item that another plugin recognizes.

Example:

```text
God
|
| ExtendedItems.create(CONSECRATED_KEYSTONE)
v
Player Inventory
|
| Later
v
Sanctuary
|
| ExtendedItems.is(item, CONSECRATED_KEYSTONE)
v
Valid Sanctuary Upgrade Ingredient
```

Neither plugin needs to reference the other's implementation.

This is the primary architectural reason for the library.

## God Integration

God will use ExtendedItems for divine rewards.

Possible sources include:

- Quest completion
- Favor purchase
- Relationship-based reward
- Major progression event
- Operator-granted reward
- Future God-controlled systems

God owns the decision to award the item.

ExtendedItems only creates the item.

## Sanctuary Integration

Sanctuary will use ExtendedItems for:

- Sanctuary Beacon identity
- Sanctuary Conduit identity
- Beacon upgrade artifacts
- Conduit upgrade artifacts
- Protection unlock artifacts
- Other Sanctuary progression items

Sanctuary will own:

- Crafting requirements
- Upgrade requirements
- Consumption logic
- Sanctuary tier
- Owner
- Anchor ID
- Protection progression
- Persistent Sanctuary state

ExtendedItems should not know any of those rules.

## Initial Sanctuary Artifacts

The exact progression tree is not yet finalized, so these IDs are provisional examples:

```text
BLESSED_FOUNDATION
BLESSED_WARD
CONSECRATED_KEYSTONE
DIVINE_CORE
SANCTUARY_BEACON
SANCTUARY_CONDUIT
```

The final names and purposes should be locked before item IDs are considered stable.

Do not release placeholder IDs into real player inventories.

## Crafting Integration

ExtendedItems should not register crafting recipes itself.

Sanctuary owns Sanctuary recipes.

Example:

```text
Vanilla Beacon
+ required mineral materials
+ Blessed Foundation
        |
        v
Sanctuary Beacon I
```

Sanctuary asks ExtendedItems to create or identify the special ingredients and output identity.

Likewise:

```text
Sanctuary Beacon I
+ rare Minecraft materials
+ Consecrated Keystone
        |
        v
Sanctuary Beacon II
```

Sanctuary performs the crafting validation and preserves its own anchor metadata.

ExtendedItems only provides shared item identity.

## Item Consumption

ExtendedItems should not remove items from player inventories.

Inventory mutation belongs to the consuming plugin.

This keeps ExtendedItems free of gameplay transaction behavior.

## Security Boundary

Custom item metadata must be treated as structured input, not proof that the item is legitimate in every gameplay context.

ExtendedItems can confirm:

```text
This ItemStack has a supported ExtendedItems identity and format.
```

It cannot confirm:

```text
This player is allowed to use it.
This item belongs to this player.
This Sanctuary upgrade is valid.
This quest was legitimately completed.
```

Those checks belong to the consuming plugin.

For stateful items, the owning plugin should validate embedded instance data against authoritative persistent state.

## Unknown Items

ExtendedItems should fail safely when it encounters unknown IDs.

An older ExtendedItems version should not guess what an unknown future item means.

The consuming plugin can then reject the operation without modifying the item.

## API Stability

Because custom items may remain in inventories for months or years, the public API and persisted item IDs should be treated as compatibility-sensitive.

Internal implementation classes may change freely.

The following should remain stable once released:

- Persistent namespace
- Persistent item IDs
- Version semantics
- Basic validation behavior

Breaking changes should require an explicit migration strategy.

## Proposed Package Structure

A likely initial project structure is:

```text
ExtendedItems/
├── README.md
├── build.gradle.kts
├── settings.gradle.kts
└── src/
    ├── main/
    │   └── java/
    │       └── dev/liamtolkkinen/extendeditems/
    │           ├── ExtendedItems.java
    │           ├── ExtendedItemId.java
    │           ├── ExtendedItemDefinition.java
    │           ├── ExtendedItemService.java
    │           ├── ExtendedItemValidationResult.java
    │           ├── ExtendedItemRegistry.java
    │           └── internal/
    │               ├── ExtendedItemKeys.java
    │               └── ItemMetaFactory.java
    │
    └── test/
        └── java/
```

The exact package naming may be adjusted before implementation.

## Registry

Item definitions should be registered centrally.

The registry should provide a one-to-one relationship between:

```text
ExtendedItemId
persistent string ID
definition
```

Duplicate persistent IDs must be rejected.

Unknown definitions should fail clearly during development.

The registry should preferably be immutable after initialization unless a real plugin-extension requirement appears later.

## Public API vs Internal API

The library should keep a small public API.

Public:

```text
ExtendedItemId
ExtendedItemService
ExtendedItemValidationResult
possibly ExtendedItemDefinition
```

Internal:

```text
PDC key management
metadata construction
registry implementation
format parsing
migration implementation
```

Consuming plugins should not depend on internal classes.

## Packaging

ExtendedItems will be a normal Java library.

It should not be installed as:

```text
plugins/ExtendedItems.jar
```

The recommended approach is for each consuming plugin to include it as a build dependency and shade/relocate it into the resulting plugin JAR.

Example:

```text
God.jar
  +-- God
  +-- relocated ExtendedItems
```

```text
Sanctuary.jar
  +-- Sanctuary
  +-- relocated ExtendedItems
```

This avoids a runtime dependency on a separately installed library plugin.

For the initial item contract, persistent PDC metadata is the cross-plugin boundary, so a standalone runtime plugin is unnecessary.

## Important Shading Consideration

Because both God and Sanctuary may contain their own shaded copy of ExtendedItems, cross-plugin communication must not depend on Java object identity from the library.

The durable contract is:

```text
ItemStack
+
PersistentDataContainer metadata
```

Each plugin's local ExtendedItems copy can independently recognize the same stable persisted ID.

This makes shading safe.

## Testing

ExtendedItems should have automated tests from the beginning.

At minimum, tests should verify:

### Creation

- Each registered item can be created.
- Created items use the expected Material.
- Created items contain the expected persistent ID.
- Created items contain the expected version.
- Display metadata matches the definition.

### Identification

- A valid item resolves to the correct `ExtendedItemId`.
- A normal vanilla item does not resolve as a custom item.
- An item with another custom ID does not match the wrong ID.

### Validation

- Missing ID is rejected.
- Unknown ID is rejected.
- Missing version is rejected.
- Unsupported version is rejected.
- Invalid Material is rejected when Material is part of the definition contract.
- Valid items pass validation.

### Compatibility

- Persistent IDs are unique.
- Every enum value has a registered definition.
- Every definition has a supported version greater than zero.
- No duplicate persistent IDs exist.

### Stateful Metadata

ExtendedItems tests should verify that consuming-plugin metadata can coexist with ExtendedItems metadata without being removed or overwritten.

For example:

```text
extendeditems:id
extendeditems:version
sanctuary:anchor_id
sanctuary:tier
```

must all survive normal metadata updates.

## Initial Implementation Order

### Phase 1: Create Project

Create the ExtendedItems Java library repository.

Add:

- Gradle project
- Paper API compile dependency
- Test framework
- Package structure
- Initial README

### Phase 2: Define Stable Metadata Keys

Implement:

```text
extendeditems:id
extendeditems:version
```

Centralize all `NamespacedKey` creation.

### Phase 3: Define Item ID Model

Implement:

- `ExtendedItemId`
- Stable persistent string IDs
- Item definition model
- Registry

Do not finalize placeholder item IDs until their names are agreed upon.

### Phase 4: Implement Creation

Implement:

```java
create(ExtendedItemId)
```

with:

- Material
- Display name
- Lore
- Glint
- PDC ID
- PDC version

### Phase 5: Implement Identification

Implement:

```java
is(...)
getId(...)
```

### Phase 6: Implement Validation

Implement structured validation results for malformed, unknown, or unsupported items.

### Phase 7: Add Tests

Complete automated tests for:

- Registry
- Creation
- Identification
- Validation
- Metadata coexistence

### Phase 8: Integrate God

Use ExtendedItems to create the first cross-plugin divine artifact.

Initial God integration should prove that a custom artifact can be granted reliably.

### Phase 9: Integrate Sanctuary

Use ExtendedItems to identify:

- Sanctuary Beacon
- Sanctuary Conduit
- First shared Sanctuary upgrade artifact

Sanctuary should retain ownership of all stateful anchor metadata.

### Phase 10: Validate Cross-Plugin Compatibility

Test the full flow:

```text
God creates artifact
        |
        v
Player receives artifact
        |
        v
Server restart
        |
        v
Artifact remains valid
        |
        v
Sanctuary recognizes artifact
        |
        v
Sanctuary consumes it in progression
```

This is the first major milestone for ExtendedItems.

## Design Rules

1. ExtendedItems is a Java library, not a Paper plugin.
2. ExtendedItems defines item identity and presentation, not gameplay behavior.
3. PDC metadata is authoritative for custom item identity.
4. Material, display name, lore, and glint are never sufficient identity checks.
5. Persisted item IDs must remain stable once released.
6. Every released item includes a format version.
7. Generic artifacts do not receive unnecessary unique instance IDs.
8. Stateful plugin-specific metadata remains owned by the consuming plugin.
9. ExtendedItems does not perform economy transactions.
10. ExtendedItems does not consume player inventory.
11. ExtendedItems does not decide whether a player may use an item.
12. Cross-plugin compatibility must work through persistent metadata, not shared Java object instances.
13. Unknown or unsupported item formats fail safely.
14. Automated tests are required for every registered item definition.
15. New abstractions should be added only when real cross-plugin requirements justify them.

## First Milestone

The first useful milestone is:

```text
God
  |
  | creates
  v
Consecrated Keystone
  |
  | survives inventory storage and restart
  v
Player
  |
  | presents item to
  v
Sanctuary
  |
  | validates through ExtendedItems
  v
Sanctuary upgrade succeeds
```

Once this works, ExtendedItems has proven its primary purpose.

## Long-Term Direction

ExtendedItems may eventually provide common item identities for:

```text
God
- Quest rewards
- Favor goods
- Divine artifacts

Sanctuary
- Beacons
- Conduits
- Protection artifacts
- Upgrade components
- Special guard deployment items

Future Equipment System
- Divine smithing materials
- Upgrade catalysts
- Special templates

Future Vault System
- Vault keys
- Access artifacts

Other Plugins
- Shared progression items
- Cross-plugin unlock tokens
```

The library should remain intentionally small.

Its job is not to become a general Minecraft item framework.

Its job is to provide one stable, tested, versioned way for independently developed plugins to agree on what a special item is.
