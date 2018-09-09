package plugins

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class AndroidJacocoReportPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.withId('com.android.application') {
            addJacocoReportTasks(project)
        }
        project.plugins.withId('com.android.library') {
            addJacocoReportTasks(project)
        }
    }

    private static void addJacocoReportTasks(Project project) {
        project.plugins.withId('jacoco') {

            def excludeList = ['**/R.class', '**/BuildConfig.class']

            project.extensions.configure(JacocoPluginExtension) { jacocoExtension ->
                jacocoExtension.ext.exclude = { patterns ->
                    excludeList.addAll(patterns)
                }
            }

            def allJacocoReportsTask = project.tasks.create('jacocoUnitTestReport', DefaultTask) {
                group 'verification'
                description 'Generate Jacoco reports for all unit test variants'
            }

            project.android {
                buildTypes.all { buildType ->
                    buildType.testCoverageEnabled true
                }

                testOptions {
                    unitTests {
                        includeAndroidResources = true
                    }
                }

                unitTestVariants.all { unitTestVariant ->
                    def variant = unitTestVariant.testedVariant

                    project.tasks.withType(Test)
                            .findByName("test${unitTestVariant.name.capitalize()}")
                            ?.with { unitTestTask ->

                        JacocoReport jacocoReportTask = project.tasks.create("jacoco${unitTestVariant.name.capitalize()}Report", JacocoReport) { JacocoReport jacocoReportTask ->
                            jacocoReportTask.group = "verification"
                            jacocoReportTask.description = "Generate Jacoco reports for ${unitTestVariant.name} variant"

                            def sourceDirectories = project.files(variant.sourceSets.collect {
                                it.java.srcDirs
                            }.flatten())
                            jacocoReportTask.sourceDirectories = sourceDirectories
                            project.logger.debug ">>> ${jacocoReportTask.path}.sourceDirectories = $jacocoReportTask.sourceDirectories.files"

                            def classDirectories = project.fileTree(dir: variant.javaCompile.destinationDir, excludes: excludeList)
                            jacocoReportTask.classDirectories = classDirectories
                            project.logger.debug ">>> ${jacocoReportTask.path}.classDirectories = $jacocoReportTask.classDirectories.files"

                            jacocoReportTask.executionData new File("${project.buildDir}/jacoco/test${unitTestVariant.name.capitalize()}.exec")
                            jacocoReportTask.reports {
                                xml.enabled = true
                                html.enabled = true
                            }
                            jacocoReportTask.dependsOn unitTestTask
                            allJacocoReportsTask.dependsOn jacocoReportTask
                        }

                        project.plugins.withId('kotlin-android') {
                            def additionalSourceDirectories = project.files(variant.sourceSets.collect {
                                it.kotlin.srcDirs
                            }.flatten())
                            jacocoReportTask.additionalSourceDirs(additionalSourceDirectories)
                            project.logger.debug ">>> ${jacocoReportTask.path}.additionalSourceDirectories = $jacocoReportTask.additionalSourceDirs.files"

                            def additionalClassDirectories = project.fileTree(dir: new File(project.buildDir, "tmp/kotlin-classes/${variant.name}"), excludes: excludeList)
                            jacocoReportTask.additionalClassDirs(additionalClassDirectories)
                            project.logger.debug ">>> ${jacocoReportTask.path}.additionalClassDirectories = $jacocoReportTask.additionalClassDirs.files"
                        }
                    }
                }
            }
        }
    }
}
