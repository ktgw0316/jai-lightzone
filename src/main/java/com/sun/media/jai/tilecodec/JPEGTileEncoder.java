/*
 * $RCSfile: JPEGTileEncoder.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.1 $
 * $Date: 2005/02/11 04:56:57 $
 * $State: Exp $
 */
package com.sun.media.jai.tilecodec;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.tilecodec.TileCodecDescriptor;
import javax.media.jai.tilecodec.TileCodecParameterList;
import javax.media.jai.tilecodec.TileEncoderImpl;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * A concrete implementation of the <code>TileEncoderImpl</code> class
 * for the jpeg tile codec.
 */
public class JPEGTileEncoder extends TileEncoderImpl {
    /* The associated TileCodecDescriptor */
    private TileCodecDescriptor tcd = null ;

    /**
     * Constructs an <code>JPEGTileEncoder</code>. Concrete implementations
     * of <code>TileEncoder</code> may throw an
     * <code>IllegalArgumentException</code> if the
     * <code>param</code>'s <code>getParameterListDescriptor()</code> method
     * does not return the same descriptor as that from the associated
     * <code>TileCodecDescriptor</code>'s 
     * <code>getParameterListDescriptor</code> method for the "tileEncoder" 
     * registry mode. 
     *
     * <p> If param is null, then the default parameter list for encoding
     * as defined by the associated <code>TileCodecDescriptor</code>'s 
     * <code>getDefaultParameters()</code> method will be used for encoding.
     *
     * @param output The <code>OutputStream</code> to write encoded data to.
     * @param param  The object containing the tile encoding parameters.
     * @throws IllegalArgumentException if param is not the appropriate 
     * Class type.
     * @throws IllegalArgumentException is output is null.
     */
    public JPEGTileEncoder(OutputStream output, TileCodecParameterList param) {
        super("jpeg", output, param) ;
        tcd = TileCodecUtils.getTileCodecDescriptor("tileEncoder", "jpeg");
    }

    /**
     * Encodes a <code>Raster</code> and writes the output
     * to the <code>OutputStream</code> associated with this 
     * <code>TileEncoder</code>.
     *
     * @param ras the <code>Raster</code> to encode.
     * @throws IOException if an I/O error occurs while writing to the 
     * OutputStream.
     * @throws IllegalArgumentException if ras is null.
     */
    @Override
    public void encode(Raster ras) throws IOException {
        if(ras == null)
            throw new IllegalArgumentException(
                    JaiI18N.getString("TileEncoder1")) ;

        ImageWriter writer = null;
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
        while (iter.hasNext()) {
            writer = iter.next();
        }
        assert writer != null;
        ImageWriteParam param = writer.getDefaultWriteParam();
        if(paramList.getBooleanParameter("qualitySet")) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            float quality = paramList.getFloatParameter("quality") ;
            param.setCompressionQuality(quality);
        }
        ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
        writer.setOutput(ios);
        IIOImage iioImage = new IIOImage(ras, null, null);
        writer.write(null, iioImage, param);
        ios.close();
        writer.dispose();
    }
}
