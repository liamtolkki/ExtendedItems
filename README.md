# ExtendedItems

ExtendedItems is a shared Java library for identifying, creating, and validating custom server-side items used by multiple Paper plugins.

It is not a Paper plugin and must not be installed as `plugins/ExtendedItems.jar`.

The durable cross-plugin contract is the metadata stored on the `ItemStack`:

```text
extendeditems:id
extendeditems:version
```

Gameplay plugins own their own state and rules. ExtendedItems only owns shared item identity, format validation, and common presentation metadata.

## Current status

ExtendedItems is currently an alpha library.

The first released item catalog contains the guard-related item identities required by Sanctuary:

```text
14 sentry IDs
14 matching companion IDs
2 additional companion IDs
30 total guard-related IDs
```

These persistent IDs are now part of the ExtendedItems compatibility contract.

The alpha release does not imply that Sanctuary must make every item obtainable or implement gameplay for every companion. ExtendedItems only establishes what each item is.

For example:

```text
ExtendedItems:
"This is a Warden Companion item."

Sanctuary:
"Is Warden Companion gameplay enabled?
Can this player use it?
What stats does it have?
How is it spawned?
Does it permanently die?"
```

## Requirements

- JDK 25
- Gradle Wrapper 9.7.1
- IntelliJ IDEA recommended
- Paper API 26.1.2

## Guard item rules

### Sentries

Sentry items use:

```text
themed post block + glint
```

Most sentry posts use slabs.

The Warden is the intentional exception and uses:

```text
SCULK_SENSOR
```

This preserves a low-profile post shape while making the Warden visibly distinct as a high-tier sentry.

### Companions

Companion items use:

```text
matching mob spawn egg + glint
```

This makes the intended mob immediately recognizable while distinguishing the item from an ordinary spawn egg.

Every mob with a sentry ID also has a matching companion ID.

Whether a companion is actually obtainable or enabled is a Sanctuary gameplay decision.

### Lore

The initial alpha guard definitions do not add shared lore.

ExtendedItems currently owns:

- Persistent ID
- Format version
- Material
- Display name
- Glint

Lore can be added later when wording is intentionally defined as part of the shared presentation contract.

## Guard item catalog

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
| `sentry_wither` | Wither Sentry Post | `POLISHED_BLACKSTONE_SLAB` |

### Aquatic sentries

| Persistent ID | Display Name | Material |
| --- | --- | --- |
| `sentry_drowned` | Drowned Sentry Post | `PRISMARINE_SLAB` |
| `sentry_guardian` | Guardian Sentry Post | `DARK_PRISMARINE_SLAB` |
| `sentry_elder_guardian` | Elder Guardian Sentry Post | `PRISMARINE_BRICK_SLAB` |

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
| `companion_wither` | Wither Companion | `WITHER_SPAWN_EGG` |
| `companion_drowned` | Drowned Companion | `DROWNED_SPAWN_EGG` |
| `companion_guardian` | Guardian Companion | `GUARDIAN_SPAWN_EGG` |
| `companion_elder_guardian` | Elder Guardian Companion | `ELDER_GUARDIAN_SPAWN_EGG` |

`companion_baby_zombie` intentionally uses `ZOMBIE_SPAWN_EGG`. Sanctuary controls the resulting entity variant.

The Warden, Creaking, and Wither companion identities are reserved even if Sanctuary never enables their companion gameplay.

### Additional companion identities

| Persistent ID | Display Name | Material |
| --- | --- | --- |
| `companion_axolotl` | Axolotl Companion | `AXOLOTL_SPAWN_EGG` |
| `companion_dolphin` | Dolphin Companion | `DOLPHIN_SPAWN_EGG` |

These identities are reserved for possible future support-companion gameplay.

## Format version

Every current guard item uses:

```text
extendeditems:version = 1
```

This is the ExtendedItems item-format version.

It is not:

- The library version
- The Sanctuary tier
- A guard level
- A gameplay balance version
- An alpha/beta/release marker

The alpha library may therefore publish format-version-1 items.

## Project structure

```text
ExtendedItems/
├── .github/
│   └── workflows/
│       └── build.yml
├── gradle/
│   └── wrapper/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── dev/liamtolkkinen/extendeditems/
│   │           ├── ExtendedItems.java
│   │           ├── ExtendedItemDefinition.java
│   │           ├── ExtendedItemId.java
│   │           ├── ExtendedItemIds.java
│   │           ├── ExtendedItemService.java
│   │           ├── ExtendedItemValidationResult.java
│   │           ├── ExtendedItemValidationStatus.java
│   │           └── internal/
│   │               ├── DefaultExtendedItemService.java
│   │               ├── ExtendedItemKeys.java
│   │               ├── ExtendedItemRegistry.java
│   │               └── ItemMetaFactory.java
│   └── test/
│       └── java/
│           └── dev/liamtolkkinen/extendeditems/
│               ├── ExtendedItemCatalogTests.java
│               ├── ExtendedItemCreationTests.java
│               ├── ExtendedItemIdentificationTests.java
│               ├── ExtendedItemRegistryTests.java
│               ├── ExtendedItemValidationTests.java
│               ├── MetadataCoexistenceTests.java
│               ├── MockBukkitTestBase.java
│               └── TestItems.java
├── bootstrap-gradle-wrapper.ps1
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
└── README.md
```

## IntelliJ IDEA setup

1. Install JDK 25.
2. Open the repository folder in IntelliJ IDEA.
3. Import it as a Gradle project when prompted.
4. Set the Project SDK to JDK 25.
5. Set the Gradle JVM to JDK 25.
6. Let IntelliJ sync dependencies.
7. Run the Gradle `test` or `build` task.

The project uses the Gradle Kotlin DSL.

## Gradle wrapper

The repository uses the committed Gradle Wrapper and pins Gradle 9.7.1.

Normal development should use:

```powershell
.\gradlew.bat build
```

`bootstrap-gradle-wrapper.ps1` is retained only for reconstructing the wrapper files if they are missing from a fresh repository setup.

## Public API

The static `ExtendedItems` facade is the default public entry point.

### Create a registered item

```java
ItemStack item = ExtendedItems.create(
    ExtendedItemIds.SENTRY_IRON_GOLEM);
```

or:

```java
ItemStack item = ExtendedItems.create(
    ExtendedItemIds.COMPANION_WARDEN);
```

### Identify an item

```java
boolean matches = ExtendedItems.is(
    item,
    ExtendedItemIds.SENTRY_IRON_GOLEM);
```

```java
Optional<ExtendedItemId> id = ExtendedItems.getId(item);
```

Identification only resolves IDs registered by this version of ExtendedItems.

Arbitrary PDC strings do not become recognized items.

Identification and validation are intentionally separate. A malformed item can still contain a recognized ID.

### Validate an item

```java
ExtendedItemValidationResult result =
    ExtendedItems.validate(item);

if (!result.isValid()) {
    logger.warning(result.detail());
}
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

## Persistent metadata contract

ExtendedItems writes:

```text
extendeditems:id      STRING
extendeditems:version INTEGER
```

The namespace and key meanings are compatibility-sensitive.

Do not identify ExtendedItems items from only:

- Material
- Display name
- Lore
- Glint

PDC metadata is authoritative for identity.

## Stateful item metadata

ExtendedItems does not own gameplay instance state.

A future Sanctuary-owned item may contain both ExtendedItems metadata and Sanctuary metadata:

```text
extendeditems:id = sentry_iron_golem
extendeditems:version = 1

sanctuary:some_instance_id = <UUID>
sanctuary:owner_uuid = <UUID>
```

ExtendedItems interprets only the `extendeditems` fields.

## Adding future shared items

When a new real item is needed:

1. Add a public constant to `ExtendedItemIds` using a stable persistent string.
2. Add exactly one `ExtendedItemDefinition` to the default registry in `DefaultExtendedItemService`.
3. Set a positive format version.
4. Define its Material and presentation metadata.
5. Add it to the catalog tests.
6. Add any behavior-specific creation or validation tests needed.
7. Verify consumer-plugin metadata still coexists with ExtendedItems metadata.
8. Build and test locally.
9. Let GitHub CI build and test the committed change.

Once a persistent ID is released into real inventories, do not rename it casually.

## Tests

Tests use JUnit and MockBukkit.

The test suite contains two kinds of item definitions:

### Production catalog tests

These verify the actual released ExtendedItems identities.

The catalog tests verify:

- Exactly 30 guard-related definitions exist
- Every ID creates successfully
- Every created item validates
- Persistent ID matches the contract
- Format version is `1`
- Material matches the agreed vanilla counterpart
- Display name matches the agreed name
- Glint is enabled
- All persistent IDs are unique

### Framework tests

Test-only definitions remain under `src/test`.

They are used to verify malformed metadata, unsupported versions, duplicate registration, metadata coexistence, and other framework behavior without intentionally corrupting a production definition.

Run:

```powershell
.\gradlew.bat test
```

For a full clean build:

```powershell
.\gradlew.bat clean build
```

## Build output

Normal development builds use:

```text
0.1.0-SNAPSHOT
```

The main library JAR is written as:

```text
build/libs/extendeditems-0.1.0-SNAPSHOT.jar
```

ExtendedItems is a library JAR. Do not copy it directly into the Paper server's `plugins` folder.

## GitHub CI

`.github/workflows/build.yml` runs for pushes, pull requests, and tags beginning with `v`.

For a normal push or pull request it:

1. Sets up Java 25.
2. Uses the committed Gradle Wrapper.
3. Runs a clean build and all tests.
4. Uploads the snapshot JAR as a workflow artifact.

This is validation only. It does not create a GitHub Release.

## Alpha release

The first recommended release tag for this catalog is:

```text
v0.1.0-alpha.1
```

Create it only after:

```powershell
.\gradlew.bat clean build
```

passes locally and the normal GitHub pipeline passes on the committed catalog.

Then:

```powershell
git tag v0.1.0-alpha.1
git push origin v0.1.0-alpha.1
```

The release workflow will build:

```text
extendeditems-0.1.0-alpha.1.jar
```

and create:

```text
GitHub prerelease
ExtendedItems 0.1.0-alpha.1
```

The alpha tag marks library/API maturity.

It does not change the persisted item format version, which remains `1`.

## GitHub Releases

A version tag publishes an authoritative release JAR.

Stable example:

```powershell
git tag v0.1.0
git push origin v0.1.0
```

The CI workflow then:

```text
v0.1.0 tag
    ↓
build with version 0.1.0
    ↓
run tests
    ↓
create extendeditems-0.1.0.jar
    ↓
create GitHub Release v0.1.0
    ↓
attach extendeditems-0.1.0.jar
```

Tags containing a prerelease suffix such as:

```text
v0.1.0-alpha.1
v0.1.0-beta.1
v0.1.0-rc.1
```

are published as GitHub prereleases.

God, Sanctuary, and future consumers should pin an exact ExtendedItems release version rather than pulling an unspecified latest build.

## Consuming from God or Sanctuary

The intended distribution model is GitHub Releases, not GitHub Packages.

A consuming repository will declare the ExtendedItems version it needs, download the corresponding release JAR automatically, compile against it, and shade or relocate it into its own plugin JAR.

Conceptually:

```text
God or Sanctuary build
    ↓
read required ExtendedItems version
    ↓
download that version from the ExtendedItems GitHub Release
    ↓
compile against ExtendedItems
    ↓
shade/relocate ExtendedItems into the plugin JAR
```

Persistent PDC metadata, not Java object identity, is the cross-plugin contract.

God and Sanctuary can therefore carry separate shaded copies as long as both understand the same released item format.

The exact download task belongs in the consuming repository and should be added when the first consumer actually needs ExtendedItems.

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

- Favor
- Economy transactions
- Quests
- Sanctuary progression
- Whether a guard is obtainable
- Whether a companion is enabled
- Guard stats
- Guard spawning behavior
- Guard death behavior
- Player ownership
- Crafting rules
- Inventory consumption
- Permission checks
- Persistent gameplay state

These boundaries are part of the library contract, not just implementation details.
