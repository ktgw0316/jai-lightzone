/*
 * $RCSfile: AndCRIF.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.1 $
 * $Date: 2005/02/11 04:56:14 $
 * $State: Exp $
 */
package com.lightcrafts.media.jai.opimage;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import com.lightcrafts.mediax.jai.CRIFImpl;
import com.lightcrafts.mediax.jai.ImageLayout;

/**
 * A <code>CRIF</code> supporting the "And" operation in the
 * rendered and renderable image layers.
 *
 * @since EA2
 * @see com.lightcrafts.mediax.jai.operator.AndDescriptor
 * @see AndOpImage
 *
 */
public class AndCRIF extends CRIFImpl {

     /** Constructor. */
    public AndCRIF() {
        super("and");
    }

    /**
     * Creates a new instance of <code>AndOpImage</code> in the
     * rendered layer. This method satisifies the implementation of RIF.
     *
     * @param paramBlock   The two source images to be "anded" together.
     * @param renderHints  Optionally contains destination image layout.     
     */
    public RenderedImage create(ParameterBlock paramBlock,
                                RenderingHints renderHints) {
        // Get ImageLayout from renderHints if any.
        ImageLayout layout = RIFUtil.getImageLayoutHint(renderHints);
        

        return new AndOpImage(paramBlock.getRenderedSource(0),
                              paramBlock.getRenderedSource(1),
                              renderHints,
                              layout);
    }
}
