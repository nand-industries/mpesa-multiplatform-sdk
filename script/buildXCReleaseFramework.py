import subprocess
import os
import datetime
import shutil

# --- Configuration ---
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
GRADLE_ROOT_DIR = os.path.abspath(os.path.join(SCRIPT_DIR, '..'))  # parent of /script
TARGET_ARTIFACT_DIR = os.path.join(GRADLE_ROOT_DIR, 'sdk', 'build', 'XCFrameworks', 'release')
FINAL_DESTINATION = os.path.join(GRADLE_ROOT_DIR, 'xcReleaseFramework')
ARTIFACT_NAME = 'MpesaMultiplatformSdk.xcframework'
FULL_ARTIFACT_PATH = os.path.join(TARGET_ARTIFACT_DIR, ARTIFACT_NAME)
FINAL_ARTIFACT_PATH = os.path.join(FINAL_DESTINATION, ARTIFACT_NAME)


def generate_and_move_xc_framework():
    delete_framework_if_exists()
    execute_gradle_command_to_generate_framework()

    if os.path.exists(FULL_ARTIFACT_PATH):
        move_artifact_replacing_if_exists()
    else:
        print("Build failed — artifact not found.")


def delete_framework_if_exists():
    if os.path.exists(FULL_ARTIFACT_PATH):
        print(f"Artifact found at: {FULL_ARTIFACT_PATH}, deleting...")
        delete_framework()
    else:
        print(f"Artifact not found at: {FULL_ARTIFACT_PATH}")


def delete_framework():
    try:
        if os.path.isdir(FULL_ARTIFACT_PATH):
            shutil.rmtree(FULL_ARTIFACT_PATH)
        else:
            os.remove(FULL_ARTIFACT_PATH)
        print("Artifact deleted.")
    except Exception as e:
        print(f"Error while deleting artifact: {e}")


def execute_gradle_command_to_generate_framework():
    # Run Gradle from the project root
    cmd = ["./gradlew", ":sdk:assembleMpesaMultiplatformSdkReleaseXCFramework"]

    print("Running Gradle command:", " ".join(cmd))
    result = subprocess.run(
        cmd,
        cwd=GRADLE_ROOT_DIR,
        check=False
    )

    if result.returncode != 0:
        print(f"Gradle build failed with exit code {result.returncode}")
    else:
        print("Gradle build finished successfully.")


def move_artifact_replacing_if_exists():
    os.makedirs(FINAL_DESTINATION, exist_ok=True)

    # if something is already there, delete it
    if os.path.exists(FINAL_ARTIFACT_PATH):
        print(f"Destination already has {FINAL_ARTIFACT_PATH}, replacing it...")
        if os.path.isdir(FINAL_ARTIFACT_PATH):
            shutil.rmtree(FINAL_ARTIFACT_PATH)
        else:
            os.remove(FINAL_ARTIFACT_PATH)

    print(f"Moving {FULL_ARTIFACT_PATH} -> {FINAL_ARTIFACT_PATH}")
    shutil.move(FULL_ARTIFACT_PATH, FINAL_ARTIFACT_PATH)
    print("Move completed.")


if __name__ == "__main__":
    generate_and_move_xc_framework()
