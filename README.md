# ExtendedItems

ExtendedItems is a shared Java library for identifying, creating, and validating custom server-side items used by multiple Paper plugins.

It is not a Paper plugin and must not be installed directly in a server's `plugins` folder.

The durable cross-plugin contract is stored on each item as:

```text
extendeditems:id
extendeditems:version
```

Gameplay plugins own gameplay state and behavior. ExtendedItems owns only shared identity, format validation, and common presentation metadata.

## Requirements

- JDK 25
- Gradle Wrapper 9.7.1
- Paper API 26.1.2

## Current catalog

The current catalog contains 43 published item identities:

```text
2 Sanctuary anchor IDs
1 Sanctuary interaction item ID
8 Sanctuary progression artifact IDs
15 sentry IDs
15 matching companion IDs
2 additional companion IDs

43 total published IDs
```

Every current catalog item uses:

```text
extendeditems:version = 1
```

The format version is independent from the library release version.

## Sanctuary anchors

| Persistent ID | Display Name | Material | Glint |
| --- | --- | --- | --- |
| `sanctuary_beacon` | Sanctuary Beacon | `BEACON` | No |
| `sanctuary_conduit` | Sanctuary Conduit | `CONDUIT` | No |

ExtendedItems owns only the stable anchor identity. Sanctuary owns instance state such as anchor UUID, owner, tier, placement, and Sanctuary membership.

## Sanctuary interaction items

| Persistent ID | Display Name | Material |
| --- | --- | --- |
| `divine_altar` | Divine Altar | `LECTERN` |

## Sanctuary progression artifacts

| Persistent ID | Display Name | Material |
| --- | --- | --- |
| `sanctuary_core` | Sanctuary Core | `END_CRYSTAL` |
| `territory_keystone` | Territory Keystone | `LODESTONE` |
| `watchers_eye` | Watcher's Eye | `ENDER_EYE` |
| `attunement_relic` | Attunement Relic | `ECHO_SHARD` |
| `consecrated_shard_fragment` | Consecrated Shard Fragment | `SMALL_AMETHYST_BUD` |
| `consecrated_shard` | Consecrated Shard | `AMETHYST_SHARD` |
| `consecrated_keystone` | Consecrated Keystone | `RESPAWN_ANCHOR` |
| `divine_relic` | Divine Relic | `TOTEM_OF_UNDYING` |

All current Sanctuary progression artifacts use glint.

Their gameplay purpose is owned by Sanctuary. Current intended meanings are:

```text
sanctuary_core
Anchor tier progression artifact.

territory_keystone
Permanent Sanctuary-wide extension unlock.

watchers_eye
Anchor awareness artifact used by Sanctuary's sentry and violation-detection rules.

attunement_relic
Universal anchor effect attunement artifact.

consecrated_shard_fragment
Lowest-level Sanctuary crafting material.

consecrated_shard
General Sanctuary crafting material.

consecrated_keystone
High-end anchor progression artifact.

divine_relic
Endgame divine artifact. Sanctuary owns how it is earned, renewed, and consumed.
```

The older `ward_stone`, `blast_ward`, `purification_relic`, `seal_of_keeping`, `guardian_token`, and `sentinel_seal` identities are no longer part of the current catalog. The former Sentinel Seal presentation direction was repurposed as the Echo Shard-based Attunement Relic.

## Sentries

Sentry items use a themed post block with glint.

### Land sentries

| Persistent ID | Display Name | Material |
| --- | --- | --- |
| `sentry_iron_golem` | Iron Golem Sentry Post | `SMOOTH_STONE_SLAB` |
| `sentry_pillager` | Pillager Sentry Post | `DARK_OAK_SLAB` |
| `sentry_skeleton` | Skeleton Sentry Post | `QUARTZ_SLAB` |
| `sentry_piglin_brute` | Piglin Brute Sentry Post | `BLACKSTONE_SLAB` |
| `sentry_enderman` | Enderman Sentry Post | `PURPUR_SLAB` |
| `sentry_evoker` | Evoker Sentry Post | `STONE_BRICK_SLAB` |
| `sentry_baby_zombie` | Baby Zombie Sentry Post | `MOSSY_COBBLESTONE_SLAB` |
| `sentry_blaze` | Blaze Sentry Post | `NETHER_BRICK_SLAB` |
| `sentry_warden` | Warden Sentry Post | `SCULK_SENSOR` |
| `sentry_creaking` | Creaking Sentry Post | `PALE_OAK_SLAB` |
| `sentry_creeper` | Creeper Sentry Post | `WAXED_WEATHERED_CUT_COPPER_SLAB` |
| `sentry_wither` | Wither Sentry Post | `POLISHED_BLACKSTONE_SLAB` |

### Aquatic sentries

| Persistent ID | Display Name | Material |
| --- | --- | --- |
| `sentry_drowned` | Drowned Sentry Post | `PRISMARINE_SLAB` |
| `sentry_guardian` | Guardian Sentry Post | `DARK_PRISMARINE_SLAB` |
| `sentry_elder_guardian` | Elder Guardian Sentry Post | `PRISMARINE_BRICK_SLAB` |

## Companions

Companion items use the matching spawn egg with glint. ExtendedItems only identifies the item; Sanctuary owns spawning, health, ownership, follow/stay state, combat behavior, and any additional instance metadata.

### Matching companions

| Persistent ID | Display Name | Material |
| --- | --- | --- |
| `companion_iron_golem` | Iron Golem Companion | `IRON_GOLEM_SPAWN_EGG` |
| `companion_pillager` | Pillager Companion | `PILLAGER_SPAWN_EGG` |
| `companion_skeleton` | Skeleton Companion | `SKELETON_SPAWN_EGG` |
| `companion_piglin_brute` | Piglin Brute Companion | `PIGLIN_BRUTE_SPAWN_EGG` |
| `companion_enderman` | Enderman Companion | `ENDERMAN_SPAWN_EGG` |
| `companion_evoker` | Evoker Companion | `EVOKER_SPAWN_EGG` |
| `companion_baby_zombie` | Baby Zombie Companion | `ZOMBIE_SPAWN_EGG` |
| `companion_blaze` | Blaze Companion | `BLAZE_SPAWN_EGG` |
| `companion_warden` | Warden Companion | `WARDEN_SPAWN_EGG` |
| `companion_creaking` | Creaking Companion | `CREAKING_SPAWN_EGG` |
| `companion_creeper` | Creeper Companion | `CREEPER_SPAWN_EGG` |
| `companion_wither` | Wither Companion | `WITHER_SPAWN_EGG` |
| `companion_drowned` | Drowned Companion | `DROWNED_SPAWN_EGG` |
| `companion_guardian` | Guardian Companion | `GUARDIAN_SPAWN_EGG` |
| `companion_elder_guardian` | Elder Guardian Companion | `ELDER_GUARDIAN_SPAWN_EGG` |

`companion_baby_zombie` intentionally uses `ZOMBIE_SPAWN_EGG`. Sanctuary controls the resulting entity variant.

### Additional companion identities

| Persistent ID | Display Name | Material |
| --- | --- | --- |
| `companion_axolotl` | Axolotl Companion | `AXOLOTL_SPAWN_EGG` |
| `companion_dolphin` | Dolphin Companion | `DOLPHIN_SPAWN_EGG` |

## Public API

Create a registered item:

```java
ItemStack item = ExtendedItems.create(
    ExtendedItemIds.ATTUNEMENT_RELIC);
```

Identify an item:

```java
boolean matches = ExtendedItems.is(
    item,
    ExtendedItemIds.ATTUNEMENT_RELIC);

Optional<ExtendedItemId> id = ExtendedItems.getId(item);
```

Validate an item:

```java
ExtendedItemValidationResult result =
    ExtendedItems.validate(item);
```

Validation statuses are:

```text
VALID
UNKNOWN_ITEM
MISSING_ID
MISSING_VERSION
UNSUPPORTED_VERSION
INVALID_MATERIAL
INVALID_FORMAT
```

Do not identify ExtendedItems items from only material, display name, lore, or glint. PDC metadata is authoritative.

## Stateful metadata

ExtendedItems does not own gameplay instance state. A Sanctuary-owned item may contain both namespaces:

```text
extendeditems:id = sanctuary_beacon
extendeditems:version = 1

sanctuary:anchor_id = <UUID>
sanctuary:owner_uuid = <UUID>
sanctuary:tier = 1
```

The same applies to companion health, sentry state, and other Sanctuary-specific data. ExtendedItems interprets only the `extendeditems` fields.

## Building

Normal development uses the committed Gradle Wrapper:

```powershell
.\gradlew.bat clean build --no-daemon
```

Snapshot builds produce:

```text
build/libs/extendeditems-0.1.0-SNAPSHOT.jar
```

ExtendedItems is a library JAR. Consumers should compile against a pinned release and shade or relocate it into the consuming plugin as appropriate.

## Tests

Tests use JUnit and MockBukkit. The production catalog tests verify:

- Exactly 43 published definitions exist
- Every published ID creates successfully
- Every created item validates
- Persistent IDs are unique
- Format version matches the contract
- Material matches the catalog
- Display name matches the catalog
- Glint matches the catalog

Run:

```powershell
.\gradlew.bat test
```

or:

```powershell
.\gradlew.bat clean build --no-daemon
```

## Adding shared items

When a new shared item is needed:

1. Add a public constant to `ExtendedItemIds` using a stable persistent ID.
2. Add one `ExtendedItemDefinition` to the default registry.
3. Define its material and presentation metadata.
4. Add it to the catalog tests.
5. Update this README.
6. Verify consumer-owned metadata still coexists with ExtendedItems metadata.
7. Build and test before release.

Persistent IDs are compatibility-sensitive once they are used in real inventories or persistent data.

## GitHub releases

`.github/workflows/build.yml` builds and tests pushes, pull requests, and version tags.

Tags beginning with `v` produce an authoritative release JAR. Prerelease tags such as:

```text
v0.1.0-alpha.8
```

are published as GitHub prereleases with the generated changelog and an attached versioned JAR.

Consumers should pin an exact ExtendedItems release rather than depending on an unspecified latest build.

## Ownership boundaries

ExtendedItems owns:

- Stable shared item IDs
- `extendeditems` PDC keys
- Format versions
- Item creation
- Item identification
- Format validation
- Common presentation metadata

ExtendedItems does not own:

- Sanctuary progression rules
- Crafting recipes
- Favor or economy logic
- Sentry AI
- Companion AI
- Companion persisted health
- Guard spawning or death behavior
- Anchor ownership or tier
- Player ownership
- Inventory consumption
- Permissions
- Persistent gameplay state
