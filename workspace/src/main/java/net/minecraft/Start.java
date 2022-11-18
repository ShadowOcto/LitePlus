package net.minecraft;

import net.minecraft.client.main.Main;

import java.io.File;

public class Start {
    public static void main(String[] args) {
        String userHome = System.getProperty("user.home", ".");
        File workingDirectory;
        switch (getPlatform()) {
            case LINUX:
                workingDirectory = new File(userHome, ".minecraft/");
                break;
            case WINDOWS:
                String applicationData = System.getenv("APPDATA");
                String folder = applicationData != null ? applicationData : userHome;
                workingDirectory = new File(folder, ".minecraft/");
                break;
            case MACOS:
                workingDirectory = new File(userHome, "Library/Application Support/minecraft");
                break;
            default:
                workingDirectory = new File(userHome, "minecraft/");
        }

        Main.main(new String[]{"--version", "Client",
                "--accessToken", "0",
                "--assetIndex", "1.8",
                "--userProperties", "{}",
                "--gameDir", new File(workingDirectory, ".").getAbsolutePath(),
                "--assetsDir", new File(workingDirectory, "assets/").getAbsolutePath()});
    }

    private static OS getPlatform() {
        String s = System.getProperty("os.name").toLowerCase();
        return s.contains("win") ? OS.WINDOWS : (s.contains("mac") ? OS.MACOS : (s.contains("solaris") ? OS.SOLARIS : (s.contains("sunos") ?
                OS.SOLARIS : (s.contains("linux") ? OS.LINUX : (s.contains("unix") ? OS.LINUX : OS.UNKNOWN)))));
    }

    public enum OS {
        LINUX,
        MACOS,
        SOLARIS,
        UNKNOWN,
        WINDOWS;
    }
}