package android.file;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class FileEntity {
    List<String> internalDirs;
    List<String> externalDirs;
    boolean isAddDefaultDir;
    String externalRootDir;

    private FileEntity() {

    }

    private FileEntity(Build build) {
        this.internalDirs = build.internalDirs;
        this.externalDirs = build.externalDirs;
        this.isAddDefaultDir = build.isAddDefaultDir;
        this.externalRootDir = build.externalRootDir;
    }

    public static class Build {
        private List<String> internalDirs = new ArrayList<>();
        private List<String> externalDirs = new ArrayList<>();
        private boolean isAddDefaultDir = true;
        private String externalRootDir = "RxFile";

        public Build() {

        }

        public Build addInternalDir(String internalDir) {
            internalDirs.add(internalDir);
            return this;
        }

        public Build addExternalDir(String externalDir) {
            externalDirs.add(externalDir);
            return this;
        }

        public Build isAddDefaultDir(boolean addDefaultDir) {
            isAddDefaultDir = addDefaultDir;
            return this;
        }

        public Build setExternalRootDir(String name) {
            if (TextUtils.isEmpty(name)) {
                throw new RuntimeException("externalRootDir can't be null");
            }
            this.externalRootDir = name;
            return this;
        }

        public FileEntity build() {
            return new FileEntity(this);
        }
    }
}
