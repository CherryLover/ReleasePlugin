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
                if ("assembleRelease".equals(task.getName())) {
                    containRelease = true;
                    System.out.println(mMoveApk.toString());
                }
            }
        });

        project.getGradle().addBuildListener(new BuildListener() {
            @Override
            public void buildStarted(@NotNull Gradle gradle) {

            }

            @Override
            public void settingsEvaluated(@NotNull Settings settings) {

            }

            @Override
            public void projectsLoaded(@NotNull Gradle gradle) {

            }

            @Override
            public void projectsEvaluated(@NotNull Gradle gradle) {

            }

            @Override
            public void buildFinished(@NotNull BuildResult buildResult) {
                if (containRelease) {
                    System.out.println("\n\nbuildFinished !!! Time to for copy release apk");
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
        if (StringUtil.isEmpty(mMoveApk.distPath)) {
            System.err.println("You must set distPath in Your app/build.gradle to be continue... eg: /Users/jiang/apk");
            return;
        }
        String distPath = mMoveApk.distPath + File.separator;
        String distFolder = (StringUtil.isEmpty(mMoveApk.distFolder) ? StringUtil.getTime() : mMoveApk.distFolder) + File.separator;
        FileUtil.copyFile(apkFile, new File(distPath + distFolder + apkFile.getName()));
        if (mMoveApk.minifyEnabled) {
            FileUtil.copyDir(projectPath + "/build/outputs/mapping/release/", distPath + distFolder + "mapping/");
        }
        System.out.println("copy to : " + distPath + distFolder);

    }

}
