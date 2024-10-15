plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.diffplug.spotless") version "7.0.0.BETA2"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("eu.michael-simons.neo4j:neo4j-migrations-spring-boot-starter:2.13.1")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.testcontainers:neo4j:1.20.2")
    testImplementation("org.testcontainers:junit-jupiter:1.20.2")
    testImplementation("io.cucumber:cucumber-java:7.20.1")
    testImplementation("io.cucumber:cucumber-junit:7.20.1")
    testImplementation("io.cucumber:cucumber-spring:7.20.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configurations {
    create("cucumberRuntime") {
        extendsFrom(configurations["testImplementation"])
    }
}

tasks.register<JavaExec>("cucumberCli") {
    dependsOn("assemble", "testClasses")

    mainClass.set("io.cucumber.core.cli.Main")

    classpath = configurations["cucumberRuntime"] +
        sourceSets["main"].output +
        sourceSets["test"].output

    args =
        listOf(
            "--plugin",
            "pretty",
            "--plugin",
            "html:target/cucumber-report.html",
            "--glue",
            "org.example.xpneo4j.acceptance",
            "src/test/resources/features",
        )
}

spotless {
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint() // for formatting Kotlin Gradle files
    }
    java {
        target("**/*.java")
        googleJavaFormat()
    }
}
