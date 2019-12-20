package me.monster.lab.apkplugin;


import org.gradle.BuildListener;
import org.gradle.BuildResult;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.tasks.TaskState;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class MoveApkPlugin implements Plugin<Project> {

    private boolean containRelease;
    private MoveApk mMoveApk;

    @Override
    public void apply(final Project project) {
        mMoveApk = project.getExtensions().create("moveapk", MoveApk.class);
        project.getGradle().addListener(new TaskExecutionListener() {
            @Override
            public void beforeExecute(@NotNull Task task) {
            }

            @Override
            public void afterExecute(@NotNull Task task, @NotNull TaskState taskState) {
                System.out.println("after " + task.getName() + " done ---");
                String buildPath = project.getBuildDir().getPath();
                String projectPath = project.getProjectDir().getPath();
                if ("assembleDebug".equals(task.getName())) {
                    String debugApkPath = buildPath + "/outputs/apk/debug/";
                    moveApk(debugApkPath, projectPath);
                }
                if ("assembleRelease".equals(task.getName())) {
                    containRelease = true;
                    System.out.println(mMoveApk.toString());
                }
            }
        });

        project.getGradle().addBuildListener(new BuildListener() {
            @Override
            public void buildStarted(Gradle gradle) {

            }

            @Override
            public void settingsEvaluated(Settings settings) {

            }

            @Override
            public void projectsLoaded(Gradle gradle) {

            }

            @Override
            public void projectsEvaluated(Gradle gradle) {

            }

            @Override
            public void buildFinished(BuildResult buildResult) {
                if (containRelease) {
                    System.out.println("buildFinished !!! Time to for copy release apk");
                    String projectPath = project.getProjectDir().getPath();

                    try {
                        moveRelease(projectPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void moveRelease(String projectPath) {
        String apkPath = projectPath + File.separator + "release/";
        List<File> files = FileUtil.listFilesInDir(apkPath);
        if (files == null || files.isEmpty()) {
            System.err.println(apkPath + " do not contain any files");
            return;
        }
        File apkFile = null;
        for (File file : files) {
            if (FileUtil.getFileExtension(file).equals("apk")) {
                apkFile = file;
                break;
            }
        }
        if (apkFile == null) {
            System.err.println(apkPath + " do not contain any file what it's name end with apk");
            return;
        }
        String distPath = (StringUtil.isEmpty(mMoveApk.distPath) ? "/Users/jiang/apk" : mMoveApk.distPath) + File.separator;
        String distFolder = (StringUtil.isEmpty(mMoveApk.distFolder) ? StringUtil.getTime() : mMoveApk.distFolder) + File.separator;
        FileUtil.copyFile(apkFile, new File(distPath + distFolder + apkFile.getName()));
        if (mMoveApk.minifyEnabled) {
            System.out.println("copy From : " + projectPath + "/build/outputs/mapping/release/");
            System.out.println("copy To   : " + distPath + distFolder + "mapping/");
            FileUtil.copyDir(projectPath + "/build/outputs/mapping/release/", distPath + distFolder + "mapping/");
        }
        System.out.println("copy release apk file success");

    }

    private void moveApk(String inputPath, String outputPath) {
        List<File> files = FileUtil.listFilesInDir(inputPath);
        if (files.isEmpty()) {
            System.err.println(inputPath + " do not contain any files");
            return;
        }
        File apkFile = null;
        for (File file : files) {
            if (FileUtil.getFileExtension(file).equals("apk")) {
                apkFile = file;
                break;
            }
        }
        if (apkFile == null) {
            System.err.println(inputPath + " do not contain any file what it's name end with apk");
            return;
        }
        FileUtil.copyFile(apkFile, new File(outputPath + File.separator + apkFile.getName()));
        System.out.println("copy file success");
    }
}
