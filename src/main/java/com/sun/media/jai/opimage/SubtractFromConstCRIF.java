/*
 * $RCSfile: SubtractFromConstCRIF.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.1 $
 * $Date: 2005/02/11 04:56:45 $
 * $State: Exp $
 */
package com.sun.media.jai.opimage;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.CRIFImpl;
import javax.media.jai.ImageLayout;
import javax.media.jai.operator.SubtractFromConstDescriptor;

/**
 * A <code>CRIF</code> supporting the "SubtractFromConst" operation in
 * the rendered and renderable image layers.
 *
 * @see SubtractFromConstDescriptor
 * @see SubtractFromConstOpImage
 *
 *
 * @since EA2
 */
public class SubtractFromConstCRIF extends CRIFImpl {

    /** Constructor. */
    public SubtractFromConstCRIF() {
        super("subtractfromconst");
    }

    /**
     * Creates a new instance of <code>SubtractFromConstOpImage</code>
     * in the rendered layer.
     *
     * @param args   The source image and the constants.
     * @param hints  Optionally contains destination image layout.
     */
    public RenderedImage create(ParameterBlock args,
                                RenderingHints renderHints) {
        // Get ImageLayout from renderHints if any.
        ImageLayout layout = RIFUtil.getImageLayoutHint(renderHints);
        

        return new SubtractFromConstOpImage(args.getRenderedSource(0),
                                            renderHints,
                                            layout,
                                         (double[])args.getObjectParameter(0));
    }
}
