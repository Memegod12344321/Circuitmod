# Upload a mod source project

Upload a ZIP file into this folder using GitHub's Add file → Upload files. The ZIP must contain a Gradle source project (`build.gradle` or `build.gradle.kts`, plus `src/`). Uploading a ZIP starts the GitHub Actions build. Open the Actions tab to see errors and download the compiled JAR artifact.

This workflow currently runs a Gradle build with Java 17. The selected loader/version on the website is informational; the project ZIP's own build files determine the actual loader and Minecraft version. Make sure the ZIP is compatible with the intended Java version.