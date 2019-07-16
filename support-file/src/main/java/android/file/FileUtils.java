package android.file;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;

public class FileUtils {
    /**
     * 存放应用缓存路径
     *
     * @return 没有外置sd卡，存放手机rom内存中,
     * getExternalCacheDir 地址：/storage/emulated/0/Android/data/{应用包名}/cache
     * getCacheDir 地址：/data/user/0/{应用包名}/cache
     */
    static File getDiskCacheDir(Context context) {
        if (isSDCardMounted()) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                return file.getAbsoluteFile();
            }
        }
        return context.getCacheDir().getAbsoluteFile();
    }

    /**
     * 判断是否有外置sd卡，且已经挂载
     */
    private static boolean isSDCardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable();
    }

    /**
     * 获取文件大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 创建文件
     *
     * @param parent 文件相对父目录
     * @param child  子文件
     */
    static File makeFile(File parent, String child) {
        if (parent != null && parent.isDirectory()) {
            File file = new File(parent, child);
            return makeFile(file);
        }
        return null;
    }

    /**
     * 创建文件
     *
     * @param parent 文件相对父目录
     * @param child  子文件
     */
    static File makeFile(String parent, String child) {
        if (!TextUtils.isEmpty(parent) && !TextUtils.isEmpty(child)) {
            File file = new File(parent, child);
            return makeFile(file);
        }
        return null;
    }

    /**
     * 创建文件
     *
     * @param pathname 文件完整路径
     */
    public static void makeFile(String pathname) {
        if (!TextUtils.isEmpty(pathname)) {
            File _file = new File(pathname);
            makeFile(_file);
        }
    }

    /**
     * 创建文件
     *
     * @param parent  文件相对父目录
     * @param childes 子文件
     */
    static void makeFiles(String parent, String... childes) {
        if (TextUtils.isEmpty(parent) || childes == null || childes.length == 0) return;
        for (String child : childes) {
            if (TextUtils.isEmpty(child)) continue;
            File file = new File(parent, child);
            makeFile(file);
        }
    }

    /**
     * 创建文件
     *
     * @param file 文件绝对路径
     */
    static File makeFile(File file) {
        if (file != null && !file.exists()) {
            return file.mkdirs() ? file : null;
        }
        return file;
    }

    /**
     * 删除文件
     *
     * @param file 文件绝对路径
     */
    static boolean deleteFile(File file) {
        if (file != null) {
            return file.delete();
        }
        return false;
    }
}
