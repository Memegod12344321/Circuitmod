# Cursed.exe — AI Horror Mod Creator

**Cursed.exe** is the Forge 1.20.1 horror-mod creator prototype inside this repository. It is isolated in its own folder, so the existing Circuitmod, Stalker mod, compiler, website, and workflows are preserved.

## Included
- A separate Forge 1.20.1 / Java 17 mod project.
- A prototype horror mob, **The Stalker**, summonable with `/summon aihorrorcreator:stalker`.
- A server-side nighttime encounter event.
- A dark terminal-style Cursed.exe prompt page.
- A Python API that turns a horror-mob prompt into a validated JSON specification using an OpenAI API key. It never executes generated code.
- GitHub Actions workflow that builds the mod and uploads the JAR.

## Current limits
The AI currently produces a validated specification; it does **not** yet convert the specification into a new compiled mob automatically. The Stalker reuses the vanilla zombie model and AI. Custom models, original sound assets, advanced behaviors, and the full prompt-to-compiled-mod pipeline are future work.

## Build on GitHub (works from Android)
1. Open the repository's **Actions** tab.
2. Select **Build Cursed.exe (Forge 1.20.1)**.
3. Choose **Run workflow** on the `ai-horror-forge1201` branch.
4. Wait for the workflow to finish successfully.
5. Open the run's artifacts and download `cursed-exe-forge1201`.
6. Extract the artifact ZIP and place the JAR in your Forge 1.20.1 instance's `mods` folder.

The workflow uses Java 17 and Gradle 8.8.

## Run the prompt API
Python 3.11+ is required:

```sh
cd ai-horror-creator/creator
python -m pip install -r requirements.txt
export OPENAI_API_KEY="your-key"
uvicorn app:app --host 127.0.0.1 --port 8000
```

Open `http://127.0.0.1:8000` for the Cursed.exe interface. The Generate button sends the prompt to `POST /generate`.

**Never commit your API key to GitHub.** The API returns a safe JSON specification, not a compiled JAR.
