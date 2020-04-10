/*
 * $RCSfile: JPEGTileDecoder.java,v $
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

import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.image.SampleModel;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.media.jai.RasterFactory;
import javax.media.jai.tilecodec.TileCodecDescriptor;
import javax.media.jai.tilecodec.TileCodecParameterList;
import javax.media.jai.tilecodec.TileDecoderImpl;
import javax.media.jai.util.ImagingListener;
import com.sun.media.jai.util.ImageUtil;

/**
 * A concrete implementation of the <code>TileDecoderImpl</code> class
 * for the jpeg tile codec.
 */
public class JPEGTileDecoder extends TileDecoderImpl {
	/* The associated TileCodecDescriptor */
	private TileCodecDescriptor tcd = null;

	/**
	 * Constructs a <code>JPEGTileDecoder</code>.
	 * <code>JPEGTileDecoder</code> may throw a
	 * <code>IllegalArgumentException</code> if <code>param</code>'s
	 * <code>getParameterListDescriptor()</code> method does not return
	 * the same descriptor as that from the associated
	 * <code>TileCodecDescriptor</code>'s
	 * <code>getParameterListDescriptor</code> method for the "tileDecoder"
	 * registry mode.
	 *
	 * <p> If param is null, then the default parameter list for decoding
	 * as defined by the associated <code>TileCodecDescriptor</code>'s
	 * <code>getDefaultParameters()</code> method will be used for decoding.
	 *
	 * @param input The <code>InputStream</code> to decode data from.
	 * @param param  The object containing the tile decoding parameters.
	 * @throws IllegalArgumentException if input is null.
	 * @throws IllegalArgumentException if param is not appropriate.
	 */
	public JPEGTileDecoder(InputStream input, TileCodecParameterList param) {
		super("jpeg", input, param);
		tcd = TileCodecUtils.getTileCodecDescriptor("tileDecoder", "jpeg");
	}

	/**
	 * Returns a <code>Raster</code> that contains the decoded contents
	 * of the <code>InputStream</code> associated with this
	 * <code>TileDecoder</code>.
	 *
	 * <p>This method can perform the decoding correctly only when
	 * <code>includesLocationInfoInfo()</code> returns true.
	 *
	 * @throws IOException if an I/O error occurs while reading from the
	 * associated InputStream.
	 * @throws IllegalArgumentException if the associated
	 * TileCodecDescriptor's includesLocationInfoInfo() returns false.
	 */
	@Override
	public Raster decode() throws IOException{
		if (!tcd.includesLocationInfo())
			throw new IllegalArgumentException(
					JaiI18N.getString("JPEGTileDecoder0") );
		return decode(null);
	}

	@Override
	public Raster decode(Point location) throws IOException{
		SampleModel sm = null;
		byte[] data = null;

		ObjectInputStream ois = new ObjectInputStream(inputStream);

		try {
			// read the quality and qualitySet from the stream
			paramList.setParameter("quality", ois.readFloat());
			paramList.setParameter("qualitySet", ois.readBoolean());
			sm = TileCodecUtils.deserializeSampleModel(ois.readObject());
			location = (Point)ois.readObject();
			data = (byte[]) ois.readObject();
		}
		catch (ClassNotFoundException e) {
			ImagingListener listener =
					ImageUtil.getImagingListener((RenderingHints)null);
			listener.errorOccurred(JaiI18N.getString("ClassNotFound"),
					e, this, false);
//            e.printStackTrace();
			return null;
		}
		finally {
			ois.close();
		}

		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		WritableRaster ras = ImageIO.read(bais).getRaster();

		// set the original sample model to the decoded raster
		if (sm != null) {
			int minX = ras.getMinX();
			int minY = ras.getMinY();
			int h = ras.getHeight();
			int w = ras.getWidth();
			double[] buf = ras.getPixels(minX, minY, w, h, (double[])null);
			ras = RasterFactory.createWritableRaster(sm, new Point(minX, minY));
			ras.setPixels(minX, minY, w, h, buf);
		}
		return ras;
	}
}

