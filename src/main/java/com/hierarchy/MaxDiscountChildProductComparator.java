package com.hierarchy;

import java.util.Comparator;

/**
 * The Class MaxDiscountChildProductComparator.
 * <p>
 * This comparator is used to decide ordering on the basis of discount. Using
 * this comparator sort the collection like treeset with maximum discounted
 * product as last.
 * </p>
 */
public class MaxDiscountChildProductComparator implements Comparator<ChildProduct> {

    /*
     * (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(ChildProduct c1, ChildProduct c2) {
	if (c1.getProductId() == c2.getProductId()) {
	    return 0;
	}
	if (c1.getAbsoluteDiscount() > c2.getAbsoluteDiscount()) {
	    return 1;
	} else if (c1.getAbsoluteDiscount() < c2.getAbsoluteDiscount()) {
	    return -1;
	}

	return c2.getProductId() - c1.getProductId();
    }

}
