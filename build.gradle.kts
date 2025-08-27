plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
	kotlin("kapt") version "1.9.25"
}

group = "sh4dow18"
version = "0.9.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.postgresql:postgresql:42.7.7")
	implementation("org.mapstruct:mapstruct:1.6.3")
	implementation("org.springframework.boot:spring-boot-starter-webflux:3.5.5")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-gson:0.11.5")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.11") {
		exclude(group = "org.springframework", module = "spring-webmvc")
	}
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	kapt("org.mapstruct:mapstruct-processor:1.6.3")
}

configurations.all {
	resolutionStrategy.eachDependency {
		// Force secure version for CVE-2025-41242
		if (requested.group == "org.springframework" && requested.name == "spring-webmvc") {
			useVersion("6.2.10")
			because("Mitigar CVE-2025-41242 en dependencias transitivas")
		}
		// Force secure version for CVE-2025-48924
		if (requested.group == "org.apache.commons" && requested.name == "commons-lang3") {
			useVersion("3.18.0")
			because("Mitigar CVE-2025-48924 en dependencias transitivas")
		}
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
