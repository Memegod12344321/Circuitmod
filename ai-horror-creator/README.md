# AI Horror Mod Creator — Forge 1.20.1

This project lives in its own folder so it does not overwrite the existing Circuitmod, Stalker mod, compiler, website, or workflows.

## What is included
- A separate Forge 1.20.1 / Java 17 mod project.
- A first in-game prototype entity, **The Stalker**, which uses the vanilla zombie model/AI and can be summoned with `/summon aihorrorcreator:stalker`.
- A server-side nighttime encounter event with conservative spawn checks.
- A small Python API that turns a horror-mob prompt into a validated JSON specification using an OpenAI API key. It never executes generated code.
- GitHub Actions build workflow that compiles the Forge mod and uploads the JAR as an artifact.

## Important current limits
The JSON generator is the first safe specification stage: its generated health/speed/behavior fields are not yet wired into dynamic entity registration. The prototype Stalker is a real mod entity, but it currently reuses the vanilla zombie model and AI. Custom models, sounds, advanced AI, configurable event tables, and importing generated specifications into the compiled mod are next steps.

## Build on GitHub (recommended for Android)
1. Open the repository's **Actions** tab.
2. Select **Build Forge AI Horror Creator**.
3. Press **Run workflow** on the `ai-horror-forge1201` branch.
4. When it finishes successfully, open the run and download the `ai-horror-creator-forge1201` artifact.
5. Extract the artifact ZIP and put the JAR into a Forge 1.20.1 instance's `mods` folder.

The workflow uses Java 17 and Gradle 8.8. Minecraft Forge 1.20.1 uses Java 17. See the [Forge getting-started guide](https://docs.minecraftforge.net/en/1.19.x/gettingstarted/).

## Local build
Install JDK 17 and Gradle 8.8, then run from this directory:

```sh
gradle build
```

The JAR will be in `build/libs/`.

## Run the prompt-to-spec API
Python 3.11+ is required:

```sh
cd creator
python -m pip install -r requirements.txt
export OPENAI_API_KEY="your-key"
uvicorn app:app --host 127.0.0.1 --port 8000
```

Then POST JSON to `http://127.0.0.1:8000/generate`:

```json
{"prompt":"A tall shadow creature that stalks players at night"}
```

The API returns a validated spec, not a compiled mod. Never commit your API key to GitHub.
