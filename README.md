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

This repository is pre-1.0.

The ExtendedItems framework is implemented, but there are currently no released gameplay item IDs.

That is intentional. Item identities will be added only when an actual quest, recipe, Sanctuary progression requirement, or other cross-plugin gameplay feature defines a real item that must persist across plugin boundaries.

The production catalog is therefore currently empty:

```text
ExtendedItemIds
    no released IDs yet
```

Automated tests use test-only item IDs and definitions. Those test IDs exist only under `src/test` and are not included in the production JAR.

## Requirements

- JDK 25
- Gradle Wrapper 9.7.1
- IntelliJ IDEA recommended
- Paper API 26.1.2

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

Once an item has been released into `ExtendedItemIds`, consuming code will use the registered ID:

```java
ItemStack item = ExtendedItems.create(
    ExtendedItemIds.SOME_RELEASED_ITEM);
```

There are no production IDs yet, so this is intentionally not usable until the first real item contract is added.

### Identify an item

```java
boolean matches = ExtendedItems.is(
    item,
    ExtendedItemIds.SOME_RELEASED_ITEM);
```

```java
Optional<ExtendedItemId> id = ExtendedItems.getId(item);
```

Identification only resolves IDs registered by this version of ExtendedItems. Arbitrary PDC strings do not become recognized items.

Identification and validation are intentionally separate. A malformed item can still contain a recognized ID.

### Validate an item

```java
ExtendedItemValidationResult result = ExtendedItems.validate(item);

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

A future Sanctuary-owned anchor may contain both ExtendedItems metadata and Sanctuary metadata:

```text
extendeditems:id = <released anchor ID>
extendeditems:version = 1

sanctuary:anchor_id = <UUID>
sanctuary:owner_uuid = <UUID>
sanctuary:tier = 2
```

ExtendedItems interprets only the `extendeditems` fields.

## Adding the first real shared item

Do not add an item until its gameplay purpose is real enough to lock the persistent identity.

When that happens:

1. Add a public constant to `ExtendedItemIds` using a stable persistent string.
2. Add exactly one `ExtendedItemDefinition` to the default registry in `DefaultExtendedItemService`.
3. Set a positive format version.
4. Define its Material and presentation metadata.
5. Add creation tests.
6. Add identification tests.
7. Add validation tests.
8. Verify consumer-plugin metadata still coexists with ExtendedItems metadata.
9. Build and test locally.
10. Let GitHub CI build and test the committed change.

Once a persistent ID is released into real inventories, do not rename it casually.

## Tests

Tests use JUnit and MockBukkit.

The test suite currently uses test-only definitions to verify the framework without publishing placeholder gameplay IDs.

Coverage includes:

- Item creation
- Material and display metadata
- Persistent ID and version metadata
- Glint handling
- Vanilla-item rejection
- Unknown IDs
- Identification vs validation separation
- Missing metadata
- Unsupported versions
- Invalid material
- Invalid PDC data types
- Registry duplicate rejection
- Empty production registry support
- Consumer-plugin metadata coexistence

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

## GitHub Releases

A version tag publishes an authoritative release JAR.

Example:

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

Do not create a release tag merely because the framework builds. A tag should be created when there is a version of ExtendedItems that another repository should be able to depend on reproducibly.

God, Sanctuary, and future consumers can later be configured to download a specific release JAR automatically during their builds. They should pin a specific version rather than pulling an unspecified latest build.

## Consuming from God or Sanctuary

The intended distribution model is GitHub Releases, not GitHub Packages.

A consuming repository will eventually declare the ExtendedItems version it needs, download the corresponding release JAR automatically, compile against it, and shade/relocate it into its own plugin JAR.

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

Persistent PDC metadata, not Java object identity, is the cross-plugin contract. God and Sanctuary can therefore carry separate shaded copies as long as both understand the same released item format.

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
- Player ownership
- Crafting rules
- Inventory consumption
- Permission checks
- Beacon or Conduit state
- Persistent gameplay state

These boundaries are part of the library contract, not just implementation details.
