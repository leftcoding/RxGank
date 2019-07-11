package android.file;

public interface IRxFile {
    /**
     * 设置root文件夹名称
     */
    IRxFile setRootName(String rootName);

    /**
     * 设置配置信息
     */
    IFile config();
}
