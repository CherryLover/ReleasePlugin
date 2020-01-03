package me.monster.lab.apkplugin;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: Created jiangjiwei in 2020-01-03 13:59
 */
public class MoveApkPluginTest {

    @Test
    public void test_last_apk() {
        String folder = "/Users/jiang/Code/Android/CodeWork/ObreatingDocterAndroidBrazil/app/release/";
        File file = new File(folder);
        if (file.isDirectory()) {
            String[] list = file.list();
            List<File> fileList = new ArrayList<>(list.length);
            for (String s : list) {
                fileList.add(new File(folder + s));
            }
            MoveApkPlugin moveApkPlugin = new MoveApkPlugin();
            File apkFile = moveApkPlugin.getApkFile(fileList);
            if (apkFile != null) {
                System.out.println("file Name : " + apkFile.getName() + " last: " + StringUtil.format(new Date(apkFile.lastModified())));
            }
        }
    }
}
