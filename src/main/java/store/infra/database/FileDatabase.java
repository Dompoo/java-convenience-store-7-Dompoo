package store.infra.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import store.common.exception.StoreExceptions;
import store.infra.entity.DatabaseEntity;

public abstract class FileDatabase<T extends DatabaseEntity> implements Database<T> {

    private static final String COLUMN_SEPARATOR = ",";
    private static final String NEW_LINE = "\n";

    private final String headerLine;

    public FileDatabase() {
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath()))) {
            this.headerLine = reader.readLine();
        } catch (IOException e) {
            throw StoreExceptions.FILE_NOT_READABLE.get();
        }
    }

    @Override
    public List<T> readAll() {
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath()))) {
            return buildObjects(reader);
        } catch (IOException e) {
            throw StoreExceptions.FILE_NOT_READABLE.get();
        }
    }

    @Override
    public void updateAll(final List<T> objects) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFilePath(), false))) {
            writer.write(buildUpdateScript(objects));
        } catch (IOException e) {
            throw StoreExceptions.FILE_NOT_WRITEABLE.get();
        }
    }

    private String buildUpdateScript(final List<T> objects) {
        StringJoiner stringJoiner = new StringJoiner(NEW_LINE);
        stringJoiner.add(headerLine);
        for (T object : objects) {
            String line = object.toLine(headerLine.split(COLUMN_SEPARATOR));
            stringJoiner.add(line);
        }
        stringJoiner.add("");
        return stringJoiner.toString();
    }


    private List<T> buildObjects(final BufferedReader reader) throws IOException {
        List<T> objects = new ArrayList<>();
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null && !line.isBlank()) {
            objects.add(convertLineToObject(mapData(line)));
        }
        return objects;
    }

    private Map<String, String> mapData(final String line) {
        String[] datas = line.split(COLUMN_SEPARATOR);
        String[] columns = headerLine.split(COLUMN_SEPARATOR);
        Map<String, String> dataMap = new HashMap<>();
        for (int i = 0; i < columns.length; i++) {
            dataMap.put(columns[i], datas[i]);
        }
        return dataMap;
    }

    protected abstract String getFilePath();

    protected abstract T convertLineToObject(Map<String, String> dataMap);
}
