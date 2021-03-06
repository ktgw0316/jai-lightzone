/*
 * $RCSfile: MlibAffineTableOpImage.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.3 $
 * $Date: 2005/12/15 18:35:46 $
 * $State: Exp $
 */
package com.sun.media.jai.mlib;
import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.geom.AffineTransform;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationTable;
import java.util.Map;
import javax.media.jai.BorderExtender;
import com.sun.medialib.mlib.*;


// import OpImageTester;

/**
 * An OpImage class to perform an Affine using interpolation coefficients
 * represented in a Table, on a source image.
 *
 */
public class MlibAffineTableOpImage extends MlibAffineOpImage {

    /**
     * Creates a MlibAffineTableOpImage given a ParameterBlock containing the
     * image source and the AffineTransform.  The image dimensions are derived
     * from the source image.  The tile grid layout, SampleModel, and
     * ColorModel may optionally be specified by an ImageLayout
     * object.
     *
     * @param source a RenderedImage.
     * @param layout an ImageLayout optionally containing the tile grid layout,
     *        SampleModel, and ColorModel, or null.
     * @param tr the AffineTransform.
     * @param interp the Interpolation to be used (Table-based)
     */
    public MlibAffineTableOpImage(RenderedImage source,
				    BorderExtender extender,
				    Map config,
                                    ImageLayout layout,
                                    AffineTransform tr,
                                    Interpolation interp,
                                    double[] backgroundValues) {
	super(source,
              layout,
              config,
	      extender,
              tr,
              interp,
              backgroundValues);
    }

    /**
     * Performs nearest-neighbour affine transformation on a specified
     * rectangle.  The sources are cobbled.
     *
     * @param sources an array of source Rasters, guaranteed to provide all
     *                necessary source data for computing the output.
     * @param dest a WritableRaster tile containing the area to be computed.
     * @param destRect the rectangle within dest to be processed.
     */
    protected void computeRect(Raster[] sources,
                               WritableRaster dest,
                               Rectangle destRect) {

        // Cast the Interpolation object to InterpolationTable object
        InterpolationTable jtable = (InterpolationTable)interp;

        // The Medialib InterpolationTable class equivalent
        mediaLibImageInterpTable mlibInterpTable;

        Raster source = sources[0];
        Rectangle srcRect = source.getBounds();

        int formatTag = MediaLibAccessor.findCompatibleTag(sources,dest);

        MediaLibAccessor srcAccessor =
            new MediaLibAccessor(source,srcRect,formatTag);
        MediaLibAccessor dstAccessor =
            new MediaLibAccessor(dest,destRect,formatTag);

        //
        // The AffineTransform needs to be readjusted as per the
        // location of the current source & destination rectangles
        //

        // Clone the global transform so as not to write to an instance
        // variable as this method may be called from different threads.
        double[] medialib_tr = (double[])this.medialib_tr.clone();

        medialib_tr[2] = m_transform[0] * srcRect.x +
            m_transform[1] * srcRect.y +
            m_transform[2] -
            destRect.x;
        medialib_tr[5] = m_transform[3] * srcRect.x +
            m_transform[4] * srcRect.y +
            m_transform[5] -
            destRect.y;

	mediaLibImage srcML[], dstML[];

        switch (dstAccessor.getDataType()) {
        case DataBuffer.TYPE_BYTE:
        case DataBuffer.TYPE_USHORT:
        case DataBuffer.TYPE_SHORT:
        case DataBuffer.TYPE_INT:
            mlibInterpTable =
                new mediaLibImageInterpTable(Constants.MLIB_INT,
                                             jtable.getWidth(),
                                             jtable.getHeight(),
                                             jtable.getLeftPadding(),
                                             jtable.getTopPadding(),
                                             jtable.getSubsampleBitsH(),
                                             jtable.getSubsampleBitsV(),
                                             jtable.getPrecisionBits(),
                                             jtable.getHorizontalTableData(),
                                             jtable.getVerticalTableData());

	    srcML = srcAccessor.getMediaLibImages();
	    dstML = dstAccessor.getMediaLibImages();

            if (setBackground)
                Image.AffineTable2(dstML[0],
                                   srcML[0],
                                   medialib_tr,
                                   mlibInterpTable,
                                   Constants.MLIB_EDGE_DST_NO_WRITE,
                                   intBackgroundValues);
            else
                Image.AffineTable(dstML[0],
                                  srcML[0],
                                  medialib_tr,
                                  mlibInterpTable,
                                  Constants.MLIB_EDGE_DST_NO_WRITE);
            MlibUtils.clampImage(dstML[0], getColorModel());
            break;

        case DataBuffer.TYPE_FLOAT:
             mlibInterpTable =
                new mediaLibImageInterpTable(Constants.MLIB_FLOAT,
                                             jtable.getWidth(),
                                             jtable.getHeight(),
                                             jtable.getLeftPadding(),
                                             jtable.getTopPadding(),
                                             jtable.getSubsampleBitsH(),
                                             jtable.getSubsampleBitsV(),
                                             jtable.getPrecisionBits(),
                                             jtable.getHorizontalTableDataFloat(),
                                             jtable.getVerticalTableDataFloat());

            srcML = srcAccessor.getMediaLibImages();
	    dstML = dstAccessor.getMediaLibImages();

            if (setBackground)
                Image.AffineTable2_Fp(dstML[0],
                                      srcML[0],
                                      medialib_tr,
                                      mlibInterpTable,
                                      Constants.MLIB_EDGE_DST_NO_WRITE,
                                      backgroundValues);
            else
                Image.AffineTable_Fp(dstML[0],
                                     srcML[0],
                                     medialib_tr,
                                     mlibInterpTable,
                                     Constants.MLIB_EDGE_DST_NO_WRITE);
	    break;

        case DataBuffer.TYPE_DOUBLE:
             mlibInterpTable =
                new mediaLibImageInterpTable(Constants.MLIB_DOUBLE,
                                             jtable.getWidth(),
                                             jtable.getHeight(),
                                             jtable.getLeftPadding(),
                                             jtable.getTopPadding(),
                                             jtable.getSubsampleBitsH(),
                                             jtable.getSubsampleBitsV(),
                                             jtable.getPrecisionBits(),
                                             jtable.getHorizontalTableDataDouble(),
                                             jtable.getVerticalTableDataDouble());

	    srcML = srcAccessor.getMediaLibImages();
	    dstML = dstAccessor.getMediaLibImages();

            if (setBackground)
                Image.AffineTable2_Fp(dstML[0],
                                      srcML[0],
                                      medialib_tr,
                                      mlibInterpTable,
                                      Constants.MLIB_EDGE_DST_NO_WRITE,
                                      backgroundValues);
            else
                Image.AffineTable_Fp(dstML[0],
                                     srcML[0],
                                     medialib_tr,
                                     mlibInterpTable,
                                     Constants.MLIB_EDGE_DST_NO_WRITE);
	    break;

        default:
            String className = this.getClass().getName();
            throw new RuntimeException(JaiI18N.getString("Generic2"));
        }

        if (dstAccessor.isDataCopy()) {
            dstAccessor.copyDataToRaster();
        }
    }
}
