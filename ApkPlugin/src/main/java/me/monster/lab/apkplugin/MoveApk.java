package me.monster.lab.apkplugin;

/**
 * @description
 * @author: Created jiangjiwei in 2019-12-20 13:33
 */
public class MoveApk {
    public boolean minifyEnabled;

    public String distFolder;
    public String distPath;

    public boolean isMinifyEnabled() {
        return minifyEnabled;
    }

    public void setMinifyEnabled(boolean minifyEnabled) {
        this.minifyEnabled = minifyEnabled;
    }

    public String getDistFolder() {
        return distFolder == null ? "" : distFolder;
    }

    public void setDistFolder(String distFolder) {
        this.distFolder = distFolder;
    }

    public String getDistPath() {
        return distPath == null ? "" : distPath;
    }

    public void setDistPath(String distPath) {
        this.distPath = distPath;
    }

    @Override
    public String toString() {
        return "MoveApk{" +
                "minifyEnabled=" + minifyEnabled +
                ", distFolder='" + distFolder + '\'' +
                ", distPath='" + distPath + '\'' +
                '}';
    }
}
