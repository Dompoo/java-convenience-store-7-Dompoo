package store.config.infra.filePathLoader;

import java.util.Objects;
import java.util.ServiceLoader;
import store.common.exception.StoreExceptions;
import store.infra.filePathLoader.FilePathLoader;

public class FilePathLoaderConfig {

    private static FilePathLoader filePathLoader;

    public static FilePathLoader getFilePathLoader() {
        if (Objects.isNull(filePathLoader)) {
            ServiceLoader<FilePathLoader> loader = ServiceLoader.load(FilePathLoader.class);
            filePathLoader = loader.findFirst().orElseThrow(StoreExceptions.APPLICATION_LOAD_FAIL::get);
        }
        return filePathLoader;
    }
}
