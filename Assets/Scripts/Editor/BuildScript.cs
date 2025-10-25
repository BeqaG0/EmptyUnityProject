using UnityEditor;

public static class BuildScript
{
    public static void PerformBuild()
    {
        string path = "Build/MyGame.exe";
        string[] scenes = {
            "Assets/Scenes/Main.unity"
        };

        BuildPipeline.BuildPlayer(scenes, path, BuildTarget.StandaloneWindows64, BuildOptions.None);
    }
}
