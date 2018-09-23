package dependencies;

final class RetrofitDependencies {

    private static final String VERSION = '2.4.0'

    final String core = module('retrofit')
    final String rxjava = module('adapter-rxjava2')
    final String moshi = module('converter-moshi')

    private static String module(String name, String version = VERSION) {
        "com.squareup.retrofit2:$name:$version"
    }
}
