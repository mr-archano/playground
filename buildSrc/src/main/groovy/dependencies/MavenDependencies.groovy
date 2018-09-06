package dependencies

final class MavenDependencies {

    final AndroidDependencies android = new AndroidDependencies()
    final String gradleBuildPropertiesPlugin = 'com.novoda:gradle-build-properties-plugin:0.4.1'
    final String junit = 'junit:junit:4.12'
    final KotlinDependencies kotlin = new KotlinDependencies()
    final String okhttp = 'com.squareup.okhttp3:okhttp:3.11.0'
    final String rxjava = 'io.reactivex.rxjava2:rxjava:2.2.0'
    final String truth = 'com.google.truth:truth:0.42'
}
