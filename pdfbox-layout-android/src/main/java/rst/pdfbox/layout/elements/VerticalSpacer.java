package rst.pdfbox.layout.elements;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

import rst.pdfbox.layout.text.DrawListener;
import rst.pdfbox.layout.text.Position;

/**
 * A drawable element that occupies some vertical space without any graphical
 * representation.
 */
public class VerticalSpacer implements Drawable, Element, Dividable {

    private float height;

    /**
     * Creates a vertical space with the given height.
     *
     * @param height the height of the space.
     */
    public VerticalSpacer(float height) {
        this.height = height;
    }

    @Override
    public float getWidth() throws IOException {
        return 0;
    }

    @Override
    public float getHeight() throws IOException {
        return height;
    }

    @Override
    public Position getAbsolutePosition() {
        return null;
    }

    @Override
    public void draw(PDDocument pdDocument, PDPageContentStream contentStream,
                     Position upperLeft, DrawListener drawListener) throws IOException {
        if (drawListener != null) {
            drawListener.drawn(this, upperLeft, getWidth(), getHeight());
        }
    }

    @Override
    public Divided divide(float remainingHeight, final float pageHeight)
            throws IOException {
        return new Divided(new VerticalSpacer(remainingHeight),
                new VerticalSpacer(getHeight() - remainingHeight));
    }

    @Override
    public Drawable removeLeadingEmptyVerticalSpace() {
        return this;
    }

}
