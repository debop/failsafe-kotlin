
plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.30")
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.0")

    compile("net.jodah:failsafe:2.0.1")
    compile("io.github.microutils:kotlin-logging:1.6.23")

    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.3")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.2")

    testImplementation("org.amshove.kluent:kluent:1.47")
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("FAILED")
    }

    maxParallelForks = Runtime.getRuntime().availableProcessors()
    setForkEvery(1L)
}
