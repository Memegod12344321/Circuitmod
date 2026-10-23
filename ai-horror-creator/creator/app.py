import os
import re
from pathlib import Path
from typing import Literal

from fastapi import FastAPI, HTTPException
from fastapi.responses import FileResponse
from openai import OpenAI
from pydantic import BaseModel, Field

app = FastAPI(title="Cursed.exe", version="0.2.0")
WEB_DIR = Path(__file__).resolve().parent
PROJECT_DIR = WEB_DIR.parent


class GenerateRequest(BaseModel):
    prompt: str = Field(min_length=8, max_length=1200)


class HorrorSpec(BaseModel):
    name: str = Field(min_length=3, max_length=32)
    entity_id: str = Field(pattern=r"^[a-z][a-z0-9_]{1,30}$")
    description: str = Field(min_length=10, max_length=500)
    health: int = Field(ge=1, le=200)
    movement_speed: float = Field(ge=0.05, le=0.8)
    damage: float = Field(ge=1, le=40)
    behavior: Literal["stalk", "ambush", "patrol", "flee"]
    spawn_time: Literal["night", "caves", "any"]
    event: Literal["whisper", "distant_footsteps", "lights_out", "none"]


def get_spec(prompt: str) -> HorrorSpec:
    if not os.getenv("OPENAI_API_KEY"):
        raise HTTPException(status_code=503, detail="Set OPENAI_API_KEY in the server environment first.")
    try:
        response = OpenAI().chat.completions.create(
            model=os.getenv("OPENAI_MODEL", "gpt-4.1-mini"),
            messages=[
                {"role": "system", "content": (
                    "Design a Minecraft Forge 1.20.1 horror entity specification. "
                    "Return only values matching the supplied JSON schema. Keep names "
                    "Minecraft-appropriate. Do not output code, commands, or file paths."
                )},
                {"role": "user", "content": prompt},
            ],
            response_format={"type": "json_schema", "json_schema": {
                "name": "horror_entity_spec", "strict": True,
                "schema": HorrorSpec.model_json_schema()
            }},
        )
        raw = response.choices[0].message.content
        if not raw:
            raise ValueError("The model returned an empty response.")
        spec = HorrorSpec.model_validate_json(raw)
        spec.entity_id = re.sub(r"[^a-z0-9_]+", "_", spec.entity_id.lower()).strip("_")[:31] or "custom_stalker"
        return spec
    except HTTPException:
        raise
    except Exception as exc:
        raise HTTPException(status_code=502, detail=f"Generation or validation failed: {type(exc).__name__}")


@app.get("/")
def home():
    return FileResponse(WEB_DIR / "index.html")


@app.get("/health")
def health():
    return {"ok": True, "service": "Cursed.exe", "api_key_configured": bool(os.getenv("OPENAI_API_KEY"))}


@app.post("/generate", response_model=HorrorSpec)
def generate(request: GenerateRequest):
    return get_spec(request.prompt)


