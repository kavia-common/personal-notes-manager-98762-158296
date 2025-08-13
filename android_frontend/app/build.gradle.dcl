androidApplication {
    namespace = "org.example.app"

    dependencies {
        // AndroidX core and appcompat
        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.appcompat:appcompat:1.6.1")

        // Material Components for modern UI
        implementation("com.google.android.material:material:1.11.0")

        // UI widgets
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.cardview:cardview:1.0.0")
        implementation("androidx.activity:activity-ktx:1.8.2")

        // Unit testing (Declarative DSL)
        testing {
            dependencies {
                implementation("junit:junit:4.13.2")
                runtimeOnly("org.junit.vintage:junit-vintage-engine:5.10.2")
            }
        }

        // Keep existing sample dependencies (not used by app logic but harmless)
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation(project(":utilities"))
    }
}
