package plugins

import com.novoda.buildproperties.Entry

import java.lang.reflect.Type

class BuildConfigExtension {

    final String packageName
    final String className
    private final Map<String, FieldSpec> entries = [:]

    BuildConfigExtension(String packageName, String className) {
        this.packageName = packageName
        this.className = className
    }

    void buildConfigBoolean(String name, Entry entry) {
        entries[name] = new FieldSpec(Boolean, { String.valueOf(entry.boolean) })
    }

    void buildConfigInt(String name, Entry entry) {
        entries[name] = new FieldSpec(Integer, { String.valueOf(entry.int) })
    }

    void buildConfigLong(String name, Entry entry) {
        entries[name] = new FieldSpec(Long, { "${entry.long}L" })

    }

    void buildConfigDouble(String name, Entry entry) {
        entries[name] = new FieldSpec(Double, { String.valueOf(entry.double) })

    }

    void buildConfigString(String name, Entry entry) {
        entries[name] = new FieldSpec(String, { "\"$entry.string\"" })
    }

    Iterator<Map.Entry<String, FieldSpec>> fieldSpecs() {
        return entries.entrySet().iterator()
    }

    static class FieldSpec {
        final Type type
        private final Closure<String> value

        FieldSpec(Type type, Closure<String> value) {
            this.type = type
            this.value = value
        }

        def getValue() {
            return value.call()
        }
    }

}
