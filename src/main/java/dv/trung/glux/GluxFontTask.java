package dv.trung.glux;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.resources.TextResource;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class GluxFontTask extends DefaultTask {

    private Project project;
    private File intermediateDir;
    private File fontsDir;
    private String variantDir;
    private String packageNameXOR1;
    private TextResource packageNameXOR2;

    @OutputDirectory
    public File getIntermediateDir() {
        return intermediateDir;
    }

    @Input
    public String getVariantDir() {
        return variantDir;
    }

    @Input
    public File getFontsDir() {
        return fontsDir;
    }

    /**
     * Either packageNameXOR1 or packageNameXOR2 must be present, but both must be marked as @Optional or Gradle
     * will throw an exception if one is missing.
     */
    @Input
    @Optional
    public String getPackageNameXOR1() {
        return packageNameXOR1;
    }

    @Input
    @Optional
    public TextResource getPackageNameXOR2() {
        return packageNameXOR2;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setFontsDir(File fontsDir) {
        this.fontsDir = fontsDir;
    }

    public void setIntermediateDir(File intermediateDir) {
        this.intermediateDir = intermediateDir;
    }

    public void setVariantDir(String variantDir) {
        this.variantDir = variantDir;
    }

    public void setPackageNameXOR1(String packageNameXOR1) {
        this.packageNameXOR1 = packageNameXOR1;
    }

    public void setPackageNameXOR2(TextResource packageNameXOR2) {
        this.packageNameXOR2 = packageNameXOR2;
    }

    @TaskAction
    public void action() throws IOException {
        if (fontsDir == null || fontsDir.isFile()) {
            project.getLogger().error("Please provide your custom font into fonts directory in the assets folder");
            return;
        }
        File[] fonts = fontsDir.listFiles();
        if (fonts == null || fonts.length == 0) {
            project.getLogger().error("Please provide your custom font into fonts directory in the assets folder");
            return;
        }
        Map<String, String> resValues = new TreeMap<>();
        for (File font : fonts) {
            String fontName = font.getName();
            String fontPath = String.format("fonts/%s", fontName);
            resValues.put("path_" + fontName.replace("-", "_"), fontPath);
        }

        // write the values file.
        File values = new File(intermediateDir, "values");
        if (!values.exists() && !values.mkdirs()) {
            throw new GradleException("Failed to create folder: " + values);
        }

        Files.asCharSink(new File(values, "values.xml"), Charsets.UTF_8).write(Utils.getValuesContent(resValues));
    }
}
