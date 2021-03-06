/*
 * $RCSfile: SubtractCRIF.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.1 $
 * $Date: 2005/02/11 04:56:44 $
 * $State: Exp $
 */
package com.sun.media.jai.opimage;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.CRIFImpl;
import javax.media.jai.ImageLayout;
import javax.media.jai.operator.SubtractDescriptor;

/**
 * A <code>CRIF</code> supporting the "Subtract" operation in the rendered
 * and renderable image layers.
 *
 * @see SubtractDescriptor
 * @see SubtractOpImage
 *
 */
public class SubtractCRIF extends CRIFImpl {

    /** Constructor. */
    public SubtractCRIF() {
        super("subtract");
    }

    /**
     * Creates a new instance of <code>SubtractOpImage</code> in the rendered
     * layer. This method satisfies the implementation of RIF.
     *
     * @param paramBlock   The two source images to be subtracted.
     * @param renderHints  Optionally contains destination image layout.
     */
    public RenderedImage create(ParameterBlock paramBlock,
                                RenderingHints renderHints) {
        // Get ImageLayout from renderHints if any.
        ImageLayout layout = RIFUtil.getImageLayoutHint(renderHints);
        

        return new SubtractOpImage(paramBlock.getRenderedSource(0),
                                   paramBlock.getRenderedSource(1),
                                   renderHints,
                                   layout);
    }
}
