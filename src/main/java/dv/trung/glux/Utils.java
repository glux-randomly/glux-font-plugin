package dv.trung.glux;

import org.gradle.api.GradleException;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class Utils {

    private final static Pattern VARIANT_PATTERN = Pattern.compile("(?:([^\\p{javaUpperCase}]+)((?:\\p{javaUpperCase}[^\\p{javaUpperCase}]*)*)\\/)?([^\\/]*)");
    private final static Pattern FLAVOR_PATTERN = Pattern.compile("(\\p{javaUpperCase}[^\\p{javaUpperCase}]*)");

    public static void deleteFolder(final File folder) {
        if (!folder.exists()) {
            return;
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    if (!file.delete()) {
                        throw new GradleException("Failed to delete: " + file);
                    }
                }
            }
        }
        if (!folder.delete()) {
            throw new GradleException("Failed to delete: " + folder);
        }
    }

    private static List<String> splitVariantNames(String variant) {
        if (variant == null) {
            return new ArrayList<>();
        }
        List<String> flavors = new ArrayList<>();
        Matcher flavorMatcher = FLAVOR_PATTERN.matcher(variant);
        while (flavorMatcher.find()) {
            String match = flavorMatcher.group(1);
            if (match != null) {
                flavors.add(match.toLowerCase());
            }
        }
        return flavors;
    }

    private static long countSlashes(String input) {
        return input.codePoints().filter(x -> x == '/').count();
    }

    public static List<String> getJsonLocations(String variantDirname) {
        Matcher variantMatcher = VARIANT_PATTERN.matcher(variantDirname);
        List<String> fileLocations = new ArrayList<>();
        if (!variantMatcher.matches()) {
            return fileLocations;
        }
        List<String> flavorNames = new ArrayList<>();
        if (variantMatcher.group(1) != null) {
            flavorNames.add(variantMatcher.group(1).toLowerCase());
        }
        flavorNames.addAll(splitVariantNames(variantMatcher.group(2)));
        String buildType = variantMatcher.group(3);
        String flavorName = variantMatcher.group(1) + variantMatcher.group(2);
        fileLocations.add("src/" + flavorName + "/" + buildType);
        fileLocations.add("src/" + buildType + "/" + flavorName);
        fileLocations.add("src/" + flavorName);
        fileLocations.add("src/" + buildType);
        fileLocations.add("src/" + flavorName + capitalize(buildType));
        fileLocations.add("src/" + buildType);
        String fileLocation = "src";
        for(String flavor : flavorNames) {
            fileLocation += "/" + flavor;
            fileLocations.add(fileLocation);
            fileLocations.add(fileLocation + "/" + buildType);
            fileLocations.add(fileLocation + capitalize(buildType));
        }
        fileLocations = fileLocations.stream().distinct().sorted(Comparator.comparing(Utils::countSlashes)).collect(toList());
        return fileLocations;
    }

    private static String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static String getValuesContent(Map<String, String> values) {
        StringBuilder sb = new StringBuilder(256);

        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<resources>\n");

        for (Map.Entry<String, String> entry : values.entrySet()) {
            String name = entry.getKey();
            sb.append("    <string name=\"").append(name).append("\" translatable=\"false\"");
            sb.append(">").append(entry.getValue()).append("</string>\n");
        }

        sb.append("</resources>\n");

        return sb.toString();
    }

}
