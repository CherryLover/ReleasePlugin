package me.monster.lab.apkplugin;

/**
 * @description
 * @author: Created jiangjiwei in 2019-12-20 13:33
 */
public class MoveApk {
    public String distFolder;
    public String distPath;

    public String distPrefix;

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

    public String getDistPrefix() {
        return distPrefix == null ? "" : distPrefix;
    }

    public void setDistPrefix(String distPrefix) {
        this.distPrefix = distPrefix;
    }

    @Override
    public String toString() {
        return "MoveApk{" +
                ", distFolder='" + distFolder + '\'' +
                ", distPath='" + distPath + '\'' +
                '}';
    }
}
