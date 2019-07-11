package android.file;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;


public class RxFile implements IRxFile {
    private static volatile RxFile rxFile;
    private FileImpl fileImpl;

    private static String rootName;

    private RxFile(Context context) {
        final Context finalContext = context.getApplicationContext() == null ? context : context.getApplicationContext();
        String cacheDir = getCacheDir(finalContext).getAbsolutePath();

        fileImpl = new FileImpl();
        fileImpl.setCacheDir(cacheDir);
    }

    @Override
    public IRxFile setRootName(String rootName) {
        if (TextUtils.isEmpty(rootName)) {
            throw new RuntimeException("rootName can't be null");
        }
        RxFile.rootName = rootName;
        return this;
    }

    @Override
    public IFile config() {
        return fileImpl;
    }

    public static File imageFile() {
        return FileUtils.makeFile(getRootDir(), FileInfo.IMAGE);
    }

    public static File imageCacheFile() {
        return FileUtils.makeFile(getRootDir(), FileInfo.IMAGE_CACHE);
    }

    public static File crashFile() {
        return FileUtils.makeFile(getRootDir(), FileInfo.CRASH);
    }

    public static File networkFile() {
        return FileUtils.makeFile(getRootDir(), FileInfo.NETWORK_CACHE);
    }

    public static File apkFile() {
        return FileUtils.makeFile(getRootDir(), FileInfo.APK);
    }

    public static File galleryFile() {
        return FileUtils.makeFile(getRootDir(), FileInfo.GALLERY);
    }

    public static File makeFile(String child) {
        return FileUtils.makeFile(getRootDir(), child);
    }

    public static File makeFile(File file) {
        return FileUtils.makeFile(file);
    }

    public static File makeFile(String parent, String child) {
        return FileUtils.makeFile(parent, child);
    }

    /**
     * 在 root 跟文件夹下，创建相对文件
     *
     * @param parent 相对路径父类文件夹
     * @param child  子类文件夹
     * @return 返回 File
     */
    public static File makeFileChild(String parent, String child) {
        return FileUtils.makeFile(getRootDir() + File.separator + parent, child);
    }

    public static File makeFile(File file, String child) {
        return FileUtils.makeFile(file, child);
    }

    /**
     * 在手机内部包名文件夹下，创建文件夹
     *
     * @param context 上下文
     * @param child   子文件夹名称
     */
    public static File makeInternalFile(Context context, String child) {
        return FileUtils.makeFile(getCacheDir(context), child);
    }

    public static boolean deleteFile(File parent) {
        return FileUtils.deleteFile(parent);
    }

    public static boolean deleteFile(String child) {
        final File file = makeFile(getRootDir(), child);
        return FileUtils.deleteFile(file);
    }

    public static boolean deleteFile(String parent, String child) {
        final File file = makeFile(parent, child);
        return FileUtils.deleteFile(file);
    }

    public static boolean deleteFile(File parent, String child) {
        final File file = makeFile(parent, child);
        return FileUtils.deleteFile(file);
    }

    /**
     * 删除 root 跟文件夹下
     *
     * @param parent 相对路径父类文件夹
     * @param child  子文件
     * @return true 删除成功，false 删除失败
     */
    public static boolean deleteFileChild(String parent, String child) {
        final File file = makeFileChild(parent, child);
        return FileUtils.deleteFile(file);
    }

    /**
     * 获取目录下的文件集合
     *
     * @param file 绝对目录名称
     */
    public static File[] getFiles(File file) {
        if (!file.exists()) {
            return null;
        }
        return file.listFiles();
    }

    /**
     * 获取目录下的文件集合
     *
     * @param child root 文件夹下，相对目录文件夹下文件集合
     */
    public static File[] getChildFiles(String child) {
        File file = makeFile(child);
        if (!file.exists()) {
            return null;
        }
        return file.listFiles();
    }

    /**
     * 获取目录下的文件集合
     *
     * @param child root 文件夹下，相对目录文件夹下文件集合
     */
    public static File[] getInternalChildFiles(Context context, String child) {
        File file = makeInternalFile(context, child);
        if (!file.exists()) {
            return null;
        }
        return file.listFiles();
    }

    /**
     * 获取目录下的文件集合
     *
     * @param child root 文件夹下，相对目录文件夹下文件集合
     */
    public static File[] getInternalChildFiles(File child) {
        if (!child.exists()) {
            return null;
        }
        return child.listFiles();
    }

    public static String getRootDir() {
        final String rootDir = TextUtils.isEmpty(rootName) ? FileInfo.ROOT : rootName;
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + rootDir;
    }

    public static File getCacheDir(Context context) {
        return FileUtils.getDiskCacheDir(context);
    }

    public static RxFile with(Context context) {
        if (rxFile == null) {
            synchronized (RxFile.class) {
                if (rxFile == null) {
                    rxFile = new RxFile(context);
                }
            }
        }
        return rxFile;
    }
}
