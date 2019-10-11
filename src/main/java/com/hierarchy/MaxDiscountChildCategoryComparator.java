package com.hierarchy;

import java.util.Comparator;

/**
 * The Class MaxDiscountChildCategoryComparator.
 * <p>
 * This comparator is used to decide ordering on the basis of discount. Using
 * this comparator sort the collection like treeset with maximum discounted
 * product as last.
 * </p>
 */
public class MaxDiscountChildCategoryComparator implements Comparator<ChildCategory> {

    /*
     * (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(ChildCategory c1, ChildCategory c2) {
	if (c1.getCategoryId() == c2.getCategoryId()) {
	    return 0;
	}
	if (c1.getMaxDiscountedProductDiscount() > c2.getMaxDiscountedProductDiscount()) {
	    return 1;
	} else if (c1.getMaxDiscountedProductDiscount() < c2.getMaxDiscountedProductDiscount()) {
	    return -1;
	}
	if (c1.getMaxDiscountedProductId() != 0 && c2.getMaxDiscountedProductId() != 0) {
	    return c2.getMaxDiscountedProductId() - c1.getMaxDiscountedProductId();
	} else if (c1.getMaxDiscountedProductId() == 0 && c2.getMaxDiscountedProductId() == 0) {
	    return c1.getCategoryId() - c2.getCategoryId();
	} else if (c1.getMaxDiscountedProductId() != 0) {
	    return 1;
	} else {
	    return -1;
	}
    }

}
