plugins {
    java
    id("org.springframework.boot") version "2.7.16"
    id("io.spring.dependency-management") version "1.1.3"

    id("com.diffplug.spotless") version "6.22.0"
}

group = "edu.ncsu.csc"
version = "0.0.1-SNAPSHOT"
description = "iTrust2"

val javaVersion = JavaVersion.VERSION_17

java {
    sourceCompatibility = javaVersion
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springdoc", "springdoc-openapi-webmvc-core", "1.7.0")
    implementation("org.springdoc", "springdoc-openapi-security", "1.7.0")
    implementation("org.springdoc", "springdoc-openapi-ui", "1.7.0")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.hibernate:hibernate-validator:8.0.1.Final")
    runtimeOnly("mysql:mysql-connector-java:8.0.33")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("org.seleniumhq.selenium:selenium-java:4.15.0")
    implementation("com.paulhammant:ngwebdriver:1.2")
    implementation("io.github.bonigarcia:webdrivermanager:5.6.2")

    testImplementation("io.cucumber:cucumber-java:7.14.0")
    testImplementation("io.cucumber:cucumber-junit:7.14.0")
    testImplementation("io.cucumber:cucumber-spring:7.14.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test:5.3.6.RELEASE")
    testImplementation("junit:junit:4.13.2")
}

spotless {

    format("misc") {
        target("*.gradle", "*.md", ".gitignore")

        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    java {
        googleJavaFormat("1.18.1")
            .aosp()
            .reflowLongStrings()
            .formatJavadoc(true)
            .reorderImports(true)

        formatAnnotations()
        importOrder(group as String, "java|javax|jakarta", "", "\\#$group", "\\#")
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
