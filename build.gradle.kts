plugins {
    kotlin("jvm") version "1.9.22"  // Use the latest stable Kotlin version
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

kotlin {
    jvmToolchain(21) // ระบุ JDK ที่ใช้คอมไพล์
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    
    // Your existing JavaFX dependencies
    implementation("org.controlsfx:controlsfx:11.1.2")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0")
    implementation("net.synedra:validatorfx:0.4.0")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.bootstrapfx:bootstrapfx-core:0.4.0")
    implementation("eu.hansolo:tilesfx:17.1.17")
    implementation("com.github.almasb:fxgl:17.3")
    implementation("eu.hansolo.fx:countries:17.0.23")
    implementation("eu.hansolo.fx:heatmap:17.0.12")
    implementation("eu.hansolo:toolboxfx:17.0.29")
    implementation("eu.hansolo:toolbox:17.0.33")
    implementation("org.openjfx:javafx-swing:17.0.2")

    // JUnit 5
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0") // or latest

}

javafx {
    version = "17.0.9"
    modules = listOf(
        "javafx.controls",
        "javafx.fxml",
        "javafx.web"
    )
}

application {
    mainClass.set("com.wellet.demofxwellet.HelloApplication")
}

tasks.test {
    useJUnitPlatform()
}
