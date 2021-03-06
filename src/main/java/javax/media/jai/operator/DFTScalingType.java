/*
 * $RCSfile: DFTScalingType.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.1 $
 * $Date: 2005/02/11 04:57:34 $
 * $State: Exp $
 */
package javax.media.jai.operator;

import javax.media.jai.EnumeratedParameter;
import javax.media.jai.EnumeratedParameter;

/**
 * Class used to represent the acceptable values of the "scalingType"
 * parameter of the "DFT" and "IDFT" operations.  Acceptable values for the
 * "scalingType" parameter are defined in the <code>DFTDescriptor</code>
 * and <code>IDFTDescriptor</code> by the constants <code>SCALING_NONE</code>,
 * <code>SCALING_UNITARY</code>, and <code>SCALING_DIMENSIONS</code>.
 *
 * @since JAI 1.1
 */
public final class DFTScalingType extends EnumeratedParameter {
    DFTScalingType(String name, int value) {
        super(name, value);
    }
}
