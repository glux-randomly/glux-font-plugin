package dv.trung.glux

import org.gradle.api.Plugin
import org.gradle.api.Project

class GluxFontPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.android.applicationVariants.all { variant ->
            handleVariant(project, variant)
        }
    }

    private static void handleVariant(Project project, def variant) {
        File fontsFolder = project.file(project.file(project.buildDir).getParent() + "/src/main/assets/fonts")
        File outputDir = project.file("$project.buildDir/generated/res/glux/$variant.dirName")
        GluxFontTask task = project.tasks.create("process${variant.name.capitalize()}GluxFont", GluxFontTask)

        task.setProject(project)
        task.setFontsDir(fontsFolder)
        task.setIntermediateDir(outputDir)
        task.setVariantDir(variant.dirName)

        // This is necessary for backwards compatibility with versions of gradle that do not support
        // this new API.
        if (variant.respondsTo("applicationIdTextResource")) {
            task.setPackageNameXOR2(variant.applicationIdTextResource)
            task.dependsOn(variant.applicationIdTextResource)
        } else {
            task.setPackageNameXOR1(variant.applicationId)
        }

        // This is necessary for backwards compatibility with versions of gradle that do not support
        // this new API.
        if (variant.respondsTo("registerGeneratedResFolders")) {
            task.ext.generatedResFolders = project.files(outputDir).builtBy(task)
            variant.registerGeneratedResFolders(task.generatedResFolders)
            if (variant.respondsTo("getMergeResourcesProvider")) {
                variant.mergeResourcesProvider.configure { dependsOn(task) }
            } else {
                //noinspection GrDeprecatedAPIUsage
                variant.mergeResources.dependsOn(task)
            }
        } else {
            //noinspection GrDeprecatedAPIUsage
            variant.registerResGeneratingTask(task, outputDir)
        }
    }
}
