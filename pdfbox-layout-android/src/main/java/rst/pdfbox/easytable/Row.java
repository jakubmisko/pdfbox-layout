package rst.pdfbox.easytable;

import android.support.annotation.ColorInt;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Row {

    private Table table;
    private final List<Cell> cells;
    private Integer borderColor;

    private Row(final List<Cell> cells) {
        super();
        this.cells = cells;
        for (final Cell cell : cells) {
            cell.setRow(this);
        }
    }

    public Table getTable() {
        return table;
    }

    void setTable(final Table table) {
        this.table = table;
    }

    public List<Cell> getCells() {
        return this.cells;
    }

    float getHeightWithoutFontHeight() {
        if (cells.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Collections.sort(cells, (cell1, cell2) -> Float.compare(cell1.getHeightWithoutFontSize(), cell2.getHeightWithoutFontSize()));
        return cells.get(cells.size() - 1).getHeightWithoutFontSize();

    }

    public Integer getBorderColor() {
        if (borderColor != null) {
            return borderColor;
        } else {
            return getTable().getBorderColor();
        }
    }

    private void setBorderColor(@ColorInt Integer borderColor) {
        this.borderColor = borderColor;
    }

    public static class RowBuilder {
        private final List<Cell> cells = new LinkedList<>();
        private Integer backgroundColor;
        private Integer borderColor;

        public RowBuilder add(final Cell cell) {
            cells.add(cell);
            return this;
        }

        public RowBuilder setBackgroundColor(@ColorInt Integer backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public RowBuilder setBorderColor(@ColorInt Integer borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Row build() {
            for (Cell c : cells) {
                if (!c.hasBackgroundColor() && backgroundColor != null) {
                    c.setBackgroundColor(backgroundColor);
                }
            }
            Row row = new Row(cells);
            if (borderColor != null) {
                row.setBorderColor(borderColor);
            }
            return row;
        }
    }

}