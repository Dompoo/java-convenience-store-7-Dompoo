package store.testUtil.testDouble;

import store.io.writer.Writer;

public class WriterFake implements Writer {

    private String output;

    @Override
    public void writeLine(String value) {
        output = value;
    }

    public String getOutput() {
        return output;
    }
}
