package android.file;

import java.util.ArrayList;
import java.util.List;

public class FileImpl implements IFile {
    private FileEntity fileEntity = new FileEntity.Build().build();
    private String cacheDir;

    public FileImpl() {

    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    @Override
    public IFile setFileEntity(FileEntity fileEntity) {
        if (fileEntity == null) {
            throw new RuntimeException("fileEntity can't be null");
        }
        this.fileEntity = fileEntity;
        return this;
    }

    @Override
    public void create() {
        final String rootParent = RxFile.getRootDir();
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
