package store.testUtil.testDouble;

import java.util.ArrayList;
import java.util.List;
import store.io.reader.Reader;

public class ReaderFake implements Reader {

    private final List<String> inputStrings = new ArrayList<>();
    private final List<String> inputs = new ArrayList<>();
    private int inputCounter = 0;

    public void setInputStrings(String... input) {
        this.inputStrings.addAll(List.of(input));
    }

    public void setInputs(String... input) {
        this.inputs.addAll(List.of(input));
    }

    @Override
    public List<String> readLineAsStrings(String spliter) {
        return inputStrings;
    }

    @Override
    public String readLineAsString() {
        return getNextInput();
    }

    private String getNextInput() {
        return inputs.get(inputCounter++);
    }
}
