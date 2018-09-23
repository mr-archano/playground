package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinConfigurationPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.withId('kotlin-android') {
            project.android.sourceSets.all { sourceSet ->
                sourceSet.java.srcDirs += sourceSet.kotlin.srcDirs
            }
        }
        project.plugins.withId('kotlin') {
            project.sourceSets.all { sourceSet ->
                sourceSet.java.srcDirs += sourceSet.kotlin.srcDirs
            }
        }
    }
}
