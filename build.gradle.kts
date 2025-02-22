plugins {
    kotlin("jvm") version "2.1.10"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.4")
    testImplementation("io.mockk:mockk:1.13.16")
    testImplementation("org.assertj:assertj-core:3.27.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
