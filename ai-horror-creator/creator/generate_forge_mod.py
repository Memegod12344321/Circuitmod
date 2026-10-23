import json
import os
import re
import sys
from pathlib import Path

from openai import OpenAI

ROOT = Path(__file__).resolve().parents[1]
JAVA_ROOT = ROOT / "src/main/java/com/memegod/aihorrorcreator"
PROMPT = os.environ.get("HORROR_PROMPT", "").strip()
if len(PROMPT) < 12:
    raise SystemExit("HORROR_PROMPT must describe the mod in at least 12 characters.")
if not os.environ.get("OPENAI_API_KEY"):
    raise SystemExit("Missing GitHub repository secret OPENAI_API_KEY. Add it under Settings > Secrets and variables > Actions.")

schema = {
    "type": "object",
    "additionalProperties": False,
    "properties": {
        "mod_summary": {"type": "string"},
        "files": {
            "type": "array",
            "minItems": 3,
            "maxItems": 16,
            "items": {
                "type": "object",
                "additionalProperties": False,
                "properties": {
                    "path": {"type": "string"},
                    "content": {"type": "string"}
                },
                "required": ["path", "content"]
            }
        }
    },
    "required": ["mod_summary", "files"]
}

system = r"""
You are a senior Minecraft Forge 1.20.1 (47.4.28), Java 17 mod developer.
Generate a REAL compilable Forge mod implementation for the user's horror-mod request.
Output JSON matching the supplied schema. All file paths must be relative Java source paths
under src/main/java/com/memegod/aihorrorcreator/ and end in .java.
Use package com.memegod.aihorrorcreator or its subpackages. The fixed mod ID is aihorrorcreator.
The existing build.gradle, mods.toml, settings.gradle and gradle.properties are managed by the project
and must not be generated or modified. You MUST include the entrypoint
src/main/java/com/memegod/aihorrorcreator/AIHorrorCreator.java with @Mod("aihorrorcreator").
Implement the actual requested features in Java, not merely a design document or JSON spec.
Include entity registration, entity attributes and a client renderer if the mod requires custom entities.
Use only Minecraft 1.20.1 and Forge 47 APIs; no Fabric, NeoForge, newer-version APIs, external libraries,
internet downloads, reflection tricks, or pseudocode. Prefer safe, well-known Forge event APIs.
Keep code concise and split it into sensible files. Every generated Java file must compile together.
For horror mobs, implement custom attributes, goals/behavior and spawn/event logic requested by the user.
If a requested feature needs custom textures, sounds or models that cannot be generated as source code,
use a vanilla renderer/model and clearly document that limitation in mod_summary. Do not claim custom
assets exist unless they are included. Do not add code that reads secrets, opens network connections,
runs shell commands, writes outside the project, or damages user data. Do not use TODOs in place of
core requested behavior. Keep mob spawning bounded and server-side; avoid overwhelming entity spawning.
Include any needed imports. The class AIHorrorCreator must compile and register its mod event handlers.
""".strip()

client = OpenAI()
response = client.chat.completions.create(
    model=os.environ.get("OPENAI_MODEL", "gpt-4.1"),
    messages=[
        {"role": "system", "content": system},
        {"role": "user", "content": "Build this Minecraft Forge 1.20.1 horror mod:\n\n" + PROMPT}
    ],
    response_format={
        "type": "json_schema",
        "json_schema": {"name": "forge_horror_mod", "strict": True, "schema": schema}
    },
    temperature=0.2,
)
raw = response.choices[0].message.content
if not raw:
    raise SystemExit("AI returned an empty result.")
data = json.loads(raw)
files = data.get("files", [])
if not 3 <= len(files) <= 16:
    raise SystemExit("AI output must contain between 3 and 16 Java source files.")

written = set()
required_main = "src/main/java/com/memegod/aihorrorcreator/AIHorrorCreator.java"
for item in files:
    rel = item.get("path", "")
    content = item.get("content", "")
    if not rel.startswith("src/main/java/com/memegod/aihorrorcreator/") or not rel.endswith(".java"):
        raise SystemExit(f"Rejected unsafe or unsupported generated path: {rel!r}")
    if ".." in Path(rel).parts or rel.startswith("/") or len(content) > 50000:
        raise SystemExit(f"Rejected unsafe or oversized generated file: {rel!r}")
    if rel in written:
        raise SystemExit(f"Duplicate generated path: {rel}")
    if not content.strip() or "package " not in content:
        raise SystemExit(f"Invalid Java source: {rel}")
    written.add(rel)

if required_main not in written:
    raise SystemExit(f"AI output must include {required_main}")

# Remove only Java sources in this dedicated generated package; keep build files and assets.
for path in JAVA_ROOT.rglob("*.java"):
    path.unlink()
for item in files:
    dest = ROOT / item["path"]
    dest.parent.mkdir(parents=True, exist_ok=True)
    dest.write_text(item["content"], encoding="utf-8")

summary = str(data.get("mod_summary", "AI-generated Forge horror mod."))
(ROOT / "AI_GENERATED_MOD_SUMMARY.md").write_text(
    "# AI-generated horror mod\n\n"
    "Prompt: " + PROMPT.replace("\n", " ") + "\n\n"
    "## AI summary\n\n" + summary + "\n\n"
    "## Build details\n\nMinecraft Forge 1.20.1, Forge 47.4.28, Java 17. "
    "Generated Java sources are compiled by the workflow; compilation success does not guarantee "
    "every mechanic behaves exactly as intended in-game.\n",
    encoding="utf-8"
)
print(f"Generated {len(files)} Java source files.")
print(summary)
