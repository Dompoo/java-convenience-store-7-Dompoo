package store.common;

import java.util.Objects;
import store.common.exception.StoreExceptions;

public class ParamsValidator {

    public static void validateParamsNotNull(final Object... params) {
        for (Object obj : params) {
            if (Objects.isNull(obj)) {
                throw StoreExceptions.ILLEGAL_ARGUMENT.get();
            }
        }
    }
}
