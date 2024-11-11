package store.config.infra.filePathLoader;

import java.util.Objects;
import store.infra.filePathLoader.DefaultFilePathLoader;
import store.infra.filePathLoader.FilePathLoader;

public class FilePathLoaderConfig {

    private static FilePathLoader filePathLoader;

    public static FilePathLoader getFilePathLoader() {
        if (Objects.isNull(filePathLoader)) {
            filePathLoader = new DefaultFilePathLoader();
        }
        return filePathLoader;
    }
}
