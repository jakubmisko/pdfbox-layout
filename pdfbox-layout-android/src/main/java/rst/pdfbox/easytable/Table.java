package rst.pdfbox.easytable;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;

import java.util.LinkedList;
import java.util.List;

public class Table {

    private List<Row> rows = new LinkedList<>();
    private List<Column> columns = new LinkedList<>();
    private PDFont font = PDType1Font.HELVETICA;
    private int fontSize = 12;
    private int numberOfColumns = 0;
    private float width = 0;
    private float borderWidth = 0.2f;
    private Integer borderColor = Color.BLACK;

    private Table(final List<Row> rows, final List<Column> columns) {
        this.rows = rows;
        this.columns = columns;
    }

    private void setFont(final PDFont font) {
        this.font = font;
    }

    private void setFontSize(final int fontSize) {
        this.fontSize = fontSize;
    }

    private void setNumberOfColumns(final int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    private void setWidth(final float width) {
        this.width = width;
    }

    private void setBorderWidth(final float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public float getWidth() {
        return width;
    }

    public PDFont getFont() {
        return font;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public int getFontSize() {
        return fontSize;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public float getFontHeight() {
        return font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
    }

    public float getHeight() {
        float height = 0;
        for (final Row row : rows) {
            height += (row.getHeightWithoutFontHeight() + this.getFontHeight());
        }
        return height;
    }

    public Integer getBorderColor() {
        return borderColor;
    }

    private void setBorderColor(@ColorInt Integer borderColor) {
        this.borderColor = borderColor;
    }


    public static class TableBuilder {
        private final List<Row> rows = new LinkedList<>();
        private final List<Column> columns = new LinkedList<>();
        private int numberOfColumns = 0;
        private float width = 0;
        private Table table = new Table(rows, columns);

        public TableBuilder addRow(Row row) {
            if (row.getCells().size() != numberOfColumns && row.getOnCustomDraw() == null) {
                throw new IllegalArgumentException(
                        "Number withText row cells does not match with number withText table columns");
            }
            row.setTable(table);
            rows.add(row);
            return this;
        }

        public TableBuilder addColumnOfWidth(int width) {
            this.addColumn(new Column(width));
            return this;
        }

        public TableBuilder addColumn(final Column column) {
            numberOfColumns++;
            columns.add(column);
            width += column.getWidth();
            return this;
        }

        public TableBuilder setFont(final PDFont font) {
            this.table.setFont(font);
            return this;
        }

        public TableBuilder setFontSize(final int fontSize) {
            this.table.setFontSize(fontSize);
            return this;
        }

        public TableBuilder setBorderWidth(final float borderWidth) {
            this.table.setBorderWidth(borderWidth);
            return this;
        }

        public TableBuilder setBorderColor(@ColorInt Integer color) {
            this.table.setBorderColor(color);
            return this;
        }

        public Table build() {
            table.setWidth(width);
            table.setNumberOfColumns(numberOfColumns);
            return table;
        }

    }

}
