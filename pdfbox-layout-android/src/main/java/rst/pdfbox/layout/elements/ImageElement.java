package rst.pdfbox.layout.elements;

import android.graphics.Bitmap;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDColorSpace;
import com.tom_roush.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import rst.pdfbox.layout.text.DrawListener;
import rst.pdfbox.layout.text.Position;
import rst.pdfbox.layout.text.WidthRespecting;
import rst.pdfbox.layout.util.CompatibilityHelper;

public class ImageElement implements Element, Drawable, Dividable,
	WidthRespecting {

    /**
     * Set this to {@link #setWidth(float)} resp. {@link #setHeight(float)}
     * (usually both) in order to respect the {@link WidthRespecting width}.
     */
    public final static float SCALE_TO_RESPECT_WIDTH = -1f;

    private Bitmap image;
    private float width;
    private float height;
    private float maxWidth = -1;
    private Position absolutePosition;

    public ImageElement(final Bitmap image) {
	this.image = image;
	this.width = image.getWidth();
	this.height = image.getHeight();
    }

    @Override
    public float getWidth() throws IOException {
	if (width == SCALE_TO_RESPECT_WIDTH) {
	    if (getMaxWidth() > 0 && image.getWidth() > getMaxWidth()) {
		return getMaxWidth();
	    }
	    return image.getWidth();
	}
	return width;
    }

    /**
     * Sets the width. Default is the image width. Set to
     * {@link #SCALE_TO_RESPECT_WIDTH} in order to let the image
     * {@link WidthRespecting respect any given width}.
     * 
     * @param width
     *            the width to use.
     */
    public void setWidth(float width) {
	this.width = width;
    }

    @Override
    public float getHeight() throws IOException {
	if (height == SCALE_TO_RESPECT_WIDTH) {
	    if (getMaxWidth() > 0 && image.getWidth() > getMaxWidth()) {
		return getMaxWidth() / (float) image.getWidth()
			* (float) image.getHeight();
	    }
	    return image.getHeight();
	}
	return height;
    }

    /**
     * Sets the height. Default is the image height. Set to
     * {@link #SCALE_TO_RESPECT_WIDTH} in order to let the image
     * {@link WidthRespecting respect any given width}. Usually this makes only
     * sense if you also set the width to {@link #SCALE_TO_RESPECT_WIDTH}.
     * 
     * @param height
     *            the height to use.
     */
    public void setHeight(float height) {
	this.height = height;
    }

    @Override
    public Divided divide(float remainingHeight, float nextPageHeight)
	    throws IOException {
	if (getHeight() <= nextPageHeight) {
	    return new Divided(new VerticalSpacer(remainingHeight), this);
	}
	return new Cutter(this).divide(remainingHeight, nextPageHeight);
    }

    @Override
    public float getMaxWidth() {
	return maxWidth;
    }

    @Override
    public void setMaxWidth(float maxWidth) {
	this.maxWidth = maxWidth;
    }

    @Override
    public Position getAbsolutePosition() {
	return absolutePosition;
    }

    /**
     * Sets the absolute position to render at.
     * 
     * @param absolutePosition
     *            the absolute position.
     */
    public void setAbsolutePosition(Position absolutePosition) {
	this.absolutePosition = absolutePosition;
    }

    @Override
    public void draw(PDDocument pdDocument, PDPageContentStream contentStream,
                     Position upperLeft, DrawListener drawListener) throws IOException {
//	CompatibilityHelper.drawImage(image, pdDocument, contentStream,
//		upperLeft, getWidth(), getHeight());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        PDImageXObject imagePd = new PDImageXObject(pdDocument, new ByteArrayInputStream(outputStream.toByteArray()), null, (int) getWidth(), (int) getHeight(), 128, PDDeviceRGB.INSTANCE);
	contentStream.drawImage(imagePd, upperLeft.getX(), upperLeft.getY());
	if (drawListener != null) {
	    drawListener.drawn(this, upperLeft, getWidth(), getHeight());
	}
    }

    @Override
    public Drawable removeLeadingEmptyVerticalSpace() {
	return this;
    }
}
