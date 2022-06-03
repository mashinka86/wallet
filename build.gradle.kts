import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

java.sourceCompatibility = JavaVersion.VERSION_17

allprojects{
	apply(plugin = "kotlin")
	group ="ru.maria.wallet"
	repositories {
		mavenCentral()
		maven { url = uri("https://repo.spring.io/snapshot") }
		maven { url = uri("https://repo.spring.io/milestone") }
	}
	dependencies {
		implementation("org.springframework.boot:spring-boot-autoconfigure")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
		testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
		testImplementation("org.mockito:mockito-core")
		testImplementation("org.mockito:mockito-junit-jupiter")
		testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")

	}
}
repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
	maven { url = uri("https://repo.spring.io/milestone") }
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
