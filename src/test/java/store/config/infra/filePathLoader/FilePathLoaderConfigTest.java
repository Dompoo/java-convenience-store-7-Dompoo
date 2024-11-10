package store.config.infra.filePathLoader;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import store.infra.filePathLoader.FilePathLoader;
import store.testUtil.TestFilePathLoader;

class FilePathLoaderConfigTest {

    @Test
    void 테스트_환경에서의_FILE_PATH_LOADER를_불러온다() {
        //given

        //when
        FilePathLoader filePathLoader = FilePathLoaderConfig.getFilePathLoader();

        //then
        assertThat(filePathLoader)
                .isExactlyInstanceOf(TestFilePathLoader.class);
    }

    @Test
    void 상품_파일_위치를_불러온다() {
        //given
        FilePathLoader filePathLoader = FilePathLoaderConfig.getFilePathLoader();

        //when
        String result = filePathLoader.getProductFilePath();

        //then
        assertThat(result).isEqualTo("src/test/resources/products_test.md");
    }

    @Test
    void 프로모션_파일_위치를_불러온다() {
        //given
        FilePathLoader filePathLoader = FilePathLoaderConfig.getFilePathLoader();

        //when
        String result = filePathLoader.getPromotionFilePath();

        //then
        assertThat(result).isEqualTo("src/test/resources/promotions_test.md");
    }
}
