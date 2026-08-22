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

This repository is pre-1.0 and starts with the first cross-plugin artifact required by the implementation plan:

```text
CONSECRATED_KEYSTONE
persistent ID: sanctuary_consecrated_keystone
format version: 1
```

The remaining planned item IDs are intentionally not registered yet. Their names and purposes were marked provisional in the implementation plan and should be locked before they become persistent contracts.

The Consecrated Keystone currently uses `Material.ECHO_SHARD`. This is a pre-1.0 implementation choice based on the Echo Shard example in the implementation plan and can still be changed before the first stable release.

No glint is enabled for the Keystone yet. Glint support exists in the item definition model, but the implementation plan does not specify that this item should use it.

## Requirements

- JDK 25
- Gradle 9.7.1 or newer compatible Gradle 9.x
- IntelliJ IDEA recommended
- Paper API 26.1.2

Paper 26.1+ requires Java 25.

## Project structure

```text
ExtendedItems/
├── .github/
│   └── workflows/
│       └── build.yml
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── src/
│   ├── main/
│   │   └── java/
│   │       └── dev/liamtolkkinen/extendeditems/
│   │           ├── ExtendedItems.java
│   │           ├── ExtendedItemDefinition.java
│   │           ├── ExtendedItemId.java
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
4. Set the Gradle JVM to JDK 25.
5. Let IntelliJ sync dependencies.
6. Run the Gradle `test` or `build` task.

The project uses the Gradle Kotlin DSL.

## Gradle wrapper

`gradle-wrapper.properties` is included and pins Gradle 9.7.1.

The source package in this repository also includes `bootstrap-gradle-wrapper.ps1`. Run it once from PowerShell if the generated wrapper JAR and launch scripts are not present yet:

```powershell
.\bootstrap-gradle-wrapper.ps1
```

The bootstrap script downloads Gradle 9.7.1 to a temporary directory, runs the official `wrapper` task, and removes the temporary distribution. It generates:

```text
gradle/wrapper/gradle-wrapper.jar
gradlew
gradlew.bat
```

Commit those generated wrapper files. After that, normal development should use:

```powershell
.\gradlew.bat build
```

If Gradle 9.7.1 is already installed globally, you can generate the same files directly with:

```powershell
gradle wrapper --gradle-version 9.7.1 --distribution-type bin
```

## Public API

The simplest API is the static `ExtendedItems` facade.

### Create an item

```java
ItemStack item = ExtendedItems.create(
    ExtendedItemId.CONSECRATED_KEYSTONE);
```

### Identify an item

```java
boolean isKeystone = ExtendedItems.is(
    item,
    ExtendedItemId.CONSECRATED_KEYSTONE);
```

```java
Optional<ExtendedItemId> id = ExtendedItems.getId(item);
```

Identification and validation are intentionally separate. A malformed item can still contain a recognized ID.

### Validate an item

```java
ExtendedItemValidationResult result = ExtendedItems.validate(item);

if (!result.isValid()) {
    logger.warning(result.detail());
}
```

Current validation statuses are:

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

For a Consecrated Keystone:

```text
extendeditems:id = sanctuary_consecrated_keystone
extendeditems:version = 1
```

Do not identify ExtendedItems items from only:

- Material
- Display name
- Lore
- Glint

PDC metadata is authoritative for identity.

## Stateful item metadata

ExtendedItems does not own gameplay instance state.

A future Sanctuary-owned anchor can contain both ExtendedItems metadata and Sanctuary metadata:

```text
extendeditems:id = sanctuary_beacon
extendeditems:version = 1

sanctuary:anchor_id = <UUID>
sanctuary:owner_uuid = <UUID>
sanctuary:tier = 2
```

ExtendedItems should only interpret the `extendeditems` fields.

## Adding a new shared item

Before adding a new item, lock its persistent ID. Do not ship placeholder IDs into real inventories.

Then:

1. Add the enum value and explicit persistent string to `ExtendedItemId`.
2. Add exactly one `ExtendedItemDefinition` to the default registry in `DefaultExtendedItemService`.
3. Set a positive format version.
4. Define its Material and presentation metadata.
5. Add creation tests.
6. Add identification tests.
7. Add validation tests.
8. Verify the registry compatibility tests still pass.

Once a persistent ID is released, do not rename it casually even if the Java enum name changes later.

## Tests

Tests use JUnit and MockBukkit.

The test suite covers:

- Registered item creation
- Material and display metadata
- Persistent ID and version metadata
- Vanilla-item rejection
- Unknown IDs
- Identification vs validation separation
- Missing metadata
- Unsupported versions
- Invalid material
- Invalid PDC data types
- Persistent ID uniqueness
- Registry completeness
- Consumer-plugin metadata coexistence

Run:

```powershell
.\gradlew.bat test
```

or, before the wrapper scripts are generated:

```powershell
gradle test
```

## Build output

Run:

```powershell
.\gradlew.bat build
```

The main library JAR is written under:

```text
build/libs/
```

ExtendedItems is a library JAR. Do not copy it directly into the Paper server's `plugins` folder.

## Consuming from God or Sanctuary

During local development, a consuming plugin may use a Maven-local publication:

```powershell
.\gradlew.bat publishToMavenLocal
```

Then the consuming plugin can resolve:

```text
dev.liamtolkkinen:extendeditems:0.1.0-SNAPSHOT
```

The consuming plugin should shade and relocate ExtendedItems into its final plugin JAR.

Persistent PDC metadata, not Java object identity, is the cross-plugin contract. This means God and Sanctuary can safely carry separate shaded copies as long as both understand the same released item format.

A GitHub Packages publication can be added once the final repository owner and package distribution policy are locked.

## CI

`.github/workflows/build.yml` runs on pushes and pull requests.

It:

1. Sets up Java 25.
2. Sets up Gradle 9.7.1.
3. Runs `gradle build`.
4. Uploads the built library JAR as a workflow artifact.

The workflow intentionally does not publish a Maven package yet.

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
