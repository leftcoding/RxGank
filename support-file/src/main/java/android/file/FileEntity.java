package android.file;

import java.util.ArrayList;
import java.util.List;

public class FileEntity {
    public List<String> internalDirs;
    public List<String> externalDirs;
    public boolean isAddDefaultDir;

    private FileEntity() {

    }

    private FileEntity(Build build) {
        this.internalDirs = build.internalDirs;
        this.externalDirs = build.externalDirs;
        this.isAddDefaultDir = build.isAddDefaultDir;
    }

    public static class Build {
        private List<String> internalDirs = new ArrayList<>();
        private List<String> externalDirs = new ArrayList<>();
        private boolean isAddDefaultDir = true;

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

        public Build setAddDefaultDir(boolean addDefaultDir) {
            isAddDefaultDir = addDefaultDir;
            return this;
        }

        public FileEntity build() {
            return new FileEntity(this);
        }
    }
}
