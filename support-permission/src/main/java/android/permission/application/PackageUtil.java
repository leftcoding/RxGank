package android.permission.application;

import android.app.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

class PackageUtil {
    static List<String> getClassName(Application application) {
        List<String> classNameList = new ArrayList<>();
        try {
            final String packageName = application.getPackageName();
            DexFile df = new DexFile(application.getPackageCodePath());//通过DexFile查找当前的APK中可执行文件
            Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) {//遍历
                String className = enumeration.nextElement();
                if (className.contains(packageName)) {//在当前所有可执行的类里面查找包含有该包名的所有类
                    if (!className.matches(".*\\$.*")) {
                        classNameList.add(className);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNameList;
    }
}
