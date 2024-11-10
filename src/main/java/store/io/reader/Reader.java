package store.io.reader;

import java.util.List;

public interface Reader {

    List<String> readLineAsStrings(String spliter);

    String readLineAsString();
}
