plugins {
    `java-library`
}

group = "dev.liamtolkkinen"
version = providers.gradleProperty("releaseVersion")
    .orElse("0.1.0-SNAPSHOT")
    .get()

base {
    archivesName = "extendeditems"
}

repositories {
    mavenCentral()

    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    // Paper is supplied by the consuming Paper plugin/server at runtime.
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")

    // Tests directly use Paper/Bukkit/Adventure types.
    testImplementation("io.papermc.paper:paper-api:26.1.2.build.+")

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.1.0")

    testImplementation(
        "org.mockbukkit.mockbukkit:mockbukkit-v26.1.2:4.114.0"
    )
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = 25
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version
        )
    }
}

tasks.wrapper {
    gradleVersion = "9.7.1"
    distributionType = Wrapper.DistributionType.BIN
}
