package dependencies

final class KotlinDependencies {

    private static final String VERSION = '1.3.61'

    final String plugin = module('gradle-plugin')
    final String stdlib = module('stdlib')

    private static String module(String name, String version = VERSION) {
        return "org.jetbrains.kotlin:kotlin-$name:$version"
    }
}
