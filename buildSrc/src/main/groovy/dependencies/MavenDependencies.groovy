package dependencies

final class MavenDependencies {

    final AndroidDependencies android = new AndroidDependencies()
    final String gradleBuildPropertiesPlugin = 'com.novoda:gradle-build-properties-plugin:0.4.1'
    final String junit = 'junit:junit:4.12'
    final KotlinDependencies kotlin = new KotlinDependencies()
}
