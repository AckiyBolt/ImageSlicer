package ua.bolt.tbot.slicer.model;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by kbelentsov on 22.12.2015.
 */
public class CutImageResult {

    private int rowCount;
    private int columnCount;
    private List<File> files;

    public static CutImageResult build(int rowCount, int columnCount, List<File> files) {
        return new CutImageResult(rowCount, columnCount, Collections.unmodifiableList(files));
    }

    private CutImageResult(int rowCount, int columnCount, List<File> files) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.files = files;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public List<File> getFiles() {
        return files;
    }
}
