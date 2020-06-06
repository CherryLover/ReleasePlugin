package me.monster.lab.apkplugin;


import com.android.build.gradle.AppExtension;
import com.android.build.gradle.internal.dsl.BuildType;
import com.android.build.gradle.internal.dsl.DefaultConfig;

import org.gradle.BuildListener;
import org.gradle.BuildResult;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.ProjectConfigurationException;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.tasks.TaskState;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MoveApkPlugin implements Plugin<Project> {

    private boolean containRelease;

    private MoveApk mMoveApk;

    private String versionName;
    private boolean minifyEnabled;

    @Override
    public void apply(final Project project) {
        if (!hasAndroidPlugin(project)) {
            throw new ProjectConfigurationException("the android plugin must be applied", new Throwable());
        }

        mMoveApk = project.getExtensions().create("moveapk", MoveApk.class);

        project.getGradle().addListener(new TaskExecutionListener() {
            @Override
            public void beforeExecute(@NotNull Task task) {
            }

            @Override
            public void afterExecute(@NotNull Task task, @NotNull TaskState taskState) {
                if ("assembleRelease".equals(task.getName())) {
                    containRelease = true;
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
                getBuildGradleInfo(project);
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
        File apkFile = getApkFile(files);
        if (apkFile == null) {
            System.err.println(apkPath + " do not contain any file what it's name end with apk");
            return;
        }
        if (StringUtil.isEmpty(mMoveApk.distPath)) {
            System.err.println("You must set distPath in Your app/build.gradle to be continue... eg: /Users/jiang/apk");
            return;
        }
        if (StringUtil.isEmpty(mMoveApk.distPrefix)) {
            System.err.println("You must set prefix Your app/build.gradle to be continue... eg: brazil-dr-");
            return;
        }
        String distPath = mMoveApk.distPath + File.separator;
        String distFolder = (StringUtil.isEmpty(mMoveApk.distFolder) ? StringUtil.getTimeDay() : mMoveApk.distFolder) + File.separator;
        FileUtil.copyFile(apkFile, new File(getApkName(distPath, distFolder)));
        if (minifyEnabled) {
            FileUtil.copyDir(projectPath + "/build/outputs/mapping/release/", distPath + distFolder + "mapping" + versionName + File.separator);
        }
        System.out.println("copy to : " + distPath + distFolder);
    }

    public File getApkFile(List<File> files) {
        List<File> apkFiles = new ArrayList<>();
        for (File file : files) {
            if (FileUtil.getFileExtension(file).equals("apk")) {
                apkFiles.add(file);
            }
        }
        if (apkFiles.isEmpty()) {
            return null;
        }
        File lastModified = apkFiles.get(0);
        long newOne = lastModified.lastModified();
        for (File apkFile : apkFiles) {
            if (apkFile.lastModified() > newOne) {
                newOne = apkFile.lastModified();
                lastModified = apkFile;
            }
        }
        return lastModified;
    }

    private String getApkName(String path, String folder) {
        String prefix = mMoveApk.getDistPrefix().endsWith("-") ? mMoveApk.getDistPrefix().substring(0, mMoveApk.getDistPrefix().length() - 1) : mMoveApk.getDistPrefix();
        String apkName = "release-" + prefix + "-" + versionName + (minifyEnabled ? "-proguard-" : "-") + StringUtil.getTimeMinute() + ".apk";
        System.out.println("distFileName is " + apkName);
        return path + folder + apkName;
    }

    private static boolean hasAndroidPlugin(Project project) {
        return project.getPlugins().hasPlugin("com.android.application");
    }

    /**
     * 获取项目构建基础信息
     * versionCode
     * versionName
     * minifyEnabled
     *
     * @param project Project
     */
    private void getBuildGradleInfo(Project project) {
        AppExtension appExtension = project.getExtensions().findByType(AppExtension.class);
        if (appExtension != null) {
            DefaultConfig defaultConfig = appExtension.getDefaultConfig();
            if (defaultConfig != null) {
                versionName = defaultConfig.getVersionName();
            }
            NamedDomainObjectContainer<BuildType> buildTypes = appExtension.getBuildTypes();
            if (buildTypes != null) {
                if (!buildTypes.isEmpty()) {
                    for (BuildType buildType : buildTypes) {
                        if ("release".equals(buildType.getName())) {
                            minifyEnabled = buildType.isMinifyEnabled();
                        }
                    }
                }
            }
        }
    }

}
