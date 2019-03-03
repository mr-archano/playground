package plugins

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

class KotlinConfigGenerator {

    void generate(BuildConfigExtension extension, File dir) {
        fileSpec(extension).writeTo(dir)
    }

    private FileSpec fileSpec(BuildConfigExtension extension) {
        FileSpec.builder(extension.packageName, extension.className)
                .addType(typeSpec(extension))
                .build()
    }

    private TypeSpec typeSpec(BuildConfigExtension extension) {
        extension.fieldSpecs()
                .collect { entry -> propertySpec(entry) }
                .inject(TypeSpec.companionObjectBuilder(extension.className)) { builder, propertySpec -> builder.addProperty(propertySpec) }
                .build()
    }

    private static PropertySpec propertySpec(Map.Entry<String, BuildConfigExtension.FieldSpec> entry) {
        BuildConfigExtension.FieldSpec fieldSpec = entry.value
        return PropertySpec.builder(entry.key, fieldSpec.type)
                .initializer(fieldSpec.value)
                .build()
    }

}
