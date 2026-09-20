package com.circuitmod;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java -jar circuitmod-compiler.jar <spec.json> <output-dir>");
            System.exit(1);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode spec = mapper.readTree(Path.of(args[0]).toFile());
        Path output = Path.of(args[1]);
        Files.createDirectories(output);

        String modId = required(spec, "mod_id");
        String modName = spec.path("mod_name").asText(modId);
        String packageName = spec.path("package").asText("com.generated." + modId);
        String className = toClassName(modId) + "Mod";

        Path source = output.resolve("src/main/java").resolve(packageName.replace('.', '/')).resolve(className + ".java");
        Files.createDirectories(source.getParent());
        Files.writeString(source, "package " + packageName + ";\n\n"
                + "import net.minecraftforge.fml.common.Mod;\n\n"
                + "@Mod(\"" + modId + "\")\n"
                + "public final class " + className + " {\n"
                + "    public " + className + "() {\n"
                + "        System.out.println(\"" + escape(modName) + " loaded!\");\n"
                + "    }\n"
                + "}\n");

        Files.writeString(output.resolve("generated-info.txt"), "Generated mod: " + modName + " (" + modId + ")\n");
        System.out.println("Generated source at " + source);
    }

    private static String required(JsonNode node, String key) throws IOException {
        String value = node.path(key).asText("").trim();
        if (value.isEmpty() || !value.matches("[a-z0-9_]+")) {
            throw new IOException(key + " must contain lowercase letters, numbers, or underscores");
        }
        return value;
    }

    private static String toClassName(String value) {
        StringBuilder result = new StringBuilder();
        for (String part : value.split("_")) {
            if (!part.isEmpty()) result.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return result.toString();
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
