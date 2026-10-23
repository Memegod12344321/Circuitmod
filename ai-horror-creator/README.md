# Cursed.exe — AI Horror Mod Creator

Cursed.exe is a prompt-driven Minecraft Forge 1.20.1 horror mod generator, maintained in its own folder inside the existing Circuitmod repository.

## What it is meant to do

1. Describe a horror mod in plain English.
2. Send the prompt to an AI model that generates actual Java source files for Forge 1.20.1.
3. Compile those sources using GitHub Actions, Java 17, and ForgeGradle.
4. Download the built mod JAR and generated source as workflow artifacts.

The generator constrains output to Java files under the dedicated mod package, validates file paths and output sizes, and never executes AI-produced shell commands. Generated source is still AI-written code: compilation and in-game testing matter.

## Setup once (from your phone)

1. Open the repository's **Settings → Secrets and variables → Actions**.
2. Add a repository secret named `OPENAI_API_KEY` with your own API key.
3. Open the **Actions** tab.
4. Select **Build Cursed.exe AI Horror Mod (Forge 1.20.1)**.
5. Tap **Run workflow** and enter a detailed prompt in **Describe the actual horror mod to generate**.
6. Run it and wait for both AI generation and the Forge build to succeed.
7. Open the successful run. Download `cursed-exe-forge1201` for the JAR and `cursed-exe-generated-source` for the Java sources and summary.
8. Put the JAR in the `mods` folder of a Minecraft Forge 1.20.1 installation.

Never commit your API key to a source file or workflow.

## Example prompt

```text
Create a tall, unsettling creature for Forge 1.20.1. It should stalk the nearest player at a distance, retreat when directly looked at, and occasionally approach at night. Give it 70 health, make it faster than a zombie, and limit natural encounters to at most one nearby creature per player. Use a vanilla renderer if custom assets are unavailable. Make all behavior server-side and compatible with multiplayer.
```

## Local prompt API

The FastAPI interface can generate a validated design specification. The actual Java source generation and compilation currently run in GitHub Actions, where the API secret is configured. The source-generation script is `creator/generate_forge_mod.py`.

## Honest limitations

- The generated JAR must pass the build and should be tested in-game before use.
- AI output may still contain API mistakes; a failed compile is not a successful mod.
- Custom textures, models, and sounds are not automatically created by this Java-source generator.
- Some complex requested behavior may be approximated or need another generation/fix cycle.
- The base template uses Minecraft Forge 1.20.1 and Java 17.

Existing files outside `ai-horror-creator/` are not overwritten by this generator.
