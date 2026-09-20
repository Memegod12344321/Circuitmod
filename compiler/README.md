# CircuitMod Compiler

Initial MVP for generating a Java mod entry point from JSON.

## Input

```json
{
  "mod_id": "examplemod",
  "mod_name": "Example Mod",
  "package": "com.generated.examplemod"
}
```

## Run

```bash
java -jar circuitmod-compiler.jar spec.json generated
```

The generated Java source is written under `generated/src/main/java`.

> This is the first compiler foundation. Forge MDK integration, registries, entities, resources, and final generated-mod packaging are the next implementation stage.
