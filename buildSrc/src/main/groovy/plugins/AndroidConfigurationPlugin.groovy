package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidConfigurationPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.withId('com.android.application') {
            addAndroidUtils(project.android, project.rootProject.version)
        }
        project.plugins.withId('com.android.library') {
            addAndroidUtils(project.android, project.rootProject.version)
        }
    }

    private static void addAndroidUtils(android, version) {
        android.ext.applySdk = {
            delegate = android

            compileSdkVersion 27

            defaultConfig {
                minSdkVersion 21
                targetSdkVersion 27
                versionCode 1
                versionName version
            }
        }
    }
}
