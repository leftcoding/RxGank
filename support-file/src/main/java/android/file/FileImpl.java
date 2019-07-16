package android.file;

import java.util.ArrayList;
import java.util.List;

class FileImpl {
    private FileEntity fileEntity;
    private String rootParent;
    private String cacheDir;

    void setFileEntity(FileEntity fileEntity) {
        this.fileEntity = fileEntity;
    }

    void setRootParent(String rootParent) {
        this.rootParent = rootParent;
    }

    void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    void create() {
        if (fileEntity.isAddDefaultDir) {
            FileUtils.makeFiles(rootParent, getDefaultDirs());
        }
        FileUtils.makeFiles(rootParent, convert(fileEntity.externalDirs));
        FileUtils.makeFiles(cacheDir, convert(fileEntity.internalDirs));
    }

    private String[] getDefaultDirs() {
        List<String> defaultDirs = new ArrayList<>();
        defaultDirs.add(FileInfo.IMAGE);
        defaultDirs.add(FileInfo.CRASH);
        defaultDirs.add(FileInfo.IMAGE_CACHE);
        defaultDirs.add(FileInfo.NETWORK_CACHE);
        defaultDirs.add(FileInfo.APK);
        defaultDirs.add(FileInfo.GALLERY);
        return convert(defaultDirs);
    }

    private String[] convert(List<String> list) {
        if (list == null) return new String[0];
        return list.toArray(new String[0]);
    }
}
