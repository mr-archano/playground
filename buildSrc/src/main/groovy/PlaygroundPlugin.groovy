import org.gradle.api.Plugin
import org.gradle.api.Project
import plugins.AndroidConfigurationPlugin
import plugins.AndroidJacocoReportPlugin
import plugins.KotlinConfigurationPlugin

class PlaygroundPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        new AndroidConfigurationPlugin().apply(project)
        new AndroidJacocoReportPlugin().apply(project)
        new KotlinConfigurationPlugin().apply(project)
    }
}
