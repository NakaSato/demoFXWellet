plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // JavaFX dependencies (handled by plugin)
    
    // Only core UI libraries that are known to work well
    implementation("org.controlsfx:controlsfx:11.1.2")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")

    // SQLite database
    implementation("org.xerial:sqlite-jdbc:3.43.2.2")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics", "javafx.web", "javafx.swing")
}

application {
    mainClass.set("com.wellet.fxwellet.MainApplication")
}
