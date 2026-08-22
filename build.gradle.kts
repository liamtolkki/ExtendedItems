plugins {
    `java-library`
    `maven-publish`
}

group = "dev.liamtolkkinen"
version = "0.1.0-SNAPSHOT"

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

    withSourcesJar()
    withJavadocJar()
}

dependencies {
    // ExtendedItems compiles against Paper, but consuming plugins provide
    // Paper at runtime.
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")

    // Tests directly use Bukkit/Paper/Adventure types.
    testImplementation("io.papermc.paper:paper-api:26.1.2.build.+")

    // JUnit
    testImplementation("org.junit.jupiter:junit-jupiter:6.1.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.1.0")

    // Paper test environment
    testImplementation(
        "org.mockbukkit.mockbukkit:mockbukkit-v26.1.2:4.114.0"
    )
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = 25
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "extendeditems"
        }
    }
}

tasks.wrapper {
    gradleVersion = "9.7.1"
    distributionType = Wrapper.DistributionType.BIN
}