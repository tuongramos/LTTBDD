import java.util.jar.Attributes
import java.util.jar.JarOutputStream
import java.util.jar.Manifest

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.uicomponents"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.uicomponents"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    debugImplementation("androidx.compose.ui:ui-tooling")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.12.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test:runner:1.6.2")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// Windows' Java launcher can misread Unicode paths from Gradle's @argfile.
// A manifest classpath uses percent-encoded file URLs and avoids that conversion.
tasks.withType<Test>().configureEach {
    if (System.getProperty("os.name").startsWith("Windows") && rootDir.path.any { it.code > 127 }) {
        doFirst {
            val manifest = Manifest().apply {
                mainAttributes[Attributes.Name.MANIFEST_VERSION] = "1.0"
                mainAttributes[Attributes.Name.CLASS_PATH] =
                    classpath.files.joinToString(" ") { it.toURI().toASCIIString() }
            }
            val directory = gradle.gradleUserHomeDir.resolve("unicode-test-classpaths").apply { mkdirs() }
            val pathJar = directory.resolve("ui-components-${rootDir.path.hashCode()}-$name.jar")
            JarOutputStream(pathJar.outputStream(), manifest).use { }
            classpath = files(pathJar)
        }
    }
}
