package plugins


import com.novoda.buildproperties.Entry
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static com.google.common.truth.Truth.assertThat

class KotlinConfigGeneratorTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder()

    private final KotlinConfigGenerator generator = new KotlinConfigGenerator()

    @Test
    void name1() {
        BuildConfigExtension extension = new BuildConfigExtension('io.archano', 'FooConfig')
        extension.buildConfigBoolean('flag', newEntry { true })
        extension.buildConfigInt('two', newEntry { 2 })
        extension.buildConfigLong('fourBillions', newEntry { 4000000000L })
        extension.buildConfigDouble('pi', newEntry { Math.PI })
        extension.buildConfigString('helloWorld', newEntry { 'hello world' })

        generator.generate(extension, temporaryFolder.root)

        File buildConfig = new File(temporaryFolder.root, 'io/archano/FooConfig.kt')
        assertThat(buildConfig.text).contains('val flag: Boolean = true')
        assertThat(buildConfig.text).contains('val two: Integer = 2')
        assertThat(buildConfig.text).contains('val fourBillions: Long = 4000000000L')
        assertThat(buildConfig.text).contains("val pi: Double = $Math.PI")
        assertThat(buildConfig.text).contains('val helloWorld: String = "hello world"')
    }

    private static Entry newEntry(Closure closure) {
        return new Entry(UUID.randomUUID().toString(), closure)
    }

}
