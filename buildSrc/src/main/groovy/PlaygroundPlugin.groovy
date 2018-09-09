import org.gradle.api.Plugin
import org.gradle.api.Project
import plugins.AndroidConfigurationPlugin

class PlaygroundPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        new AndroidConfigurationPlugin().apply(project)
    }
}
