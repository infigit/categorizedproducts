package com.hierarchy;

/**
 * The class representing ChildCategory.
 * <p>
 * This representation contains category id and maximum discounted product under
 * this child category. Actual category information is represented by Category
 * class. This helps in minimizing the memory foot print while storing child
 * category information.
 * </p>
 */
public class ChildCategory {

    /** The category id. */
    int categoryId;

    /** The max discounted product id. */
    int maxDiscountedProductId;

    /** The max discounted product discount. */
    float maxDiscountedProductDiscount;

    /**
     * Gets the category id.
     *
     * @return the category id
     */
    public int getCategoryId() {
	return categoryId;
    }

    /**
     * Sets the category id.
     *
     * @param categoryId the new category id
     */
    public void setCategoryId(int categoryId) {
	this.categoryId = categoryId;
    }

    /**
     * Gets the max discounted product id.
     *
     * @return the max discounted product id
     */
    public int getMaxDiscountedProductId() {
	return maxDiscountedProductId;
    }

    /**
     * Sets the max discounted product id.
     *
     * @param maxDiscountedProductId the new max discounted product id
     */
    public void setMaxDiscountedProductId(int maxDiscountedProductId) {
	this.maxDiscountedProductId = maxDiscountedProductId;
    }

    /**
     * Gets the max discounted product discount.
     *
     * @return the max discounted product discount
     */
    public float getMaxDiscountedProductDiscount() {
	return maxDiscountedProductDiscount;
    }

    /**
     * Sets the max discounted product discount.
     *
     * @param maxDiscountedProductDiscount the new max discounted product
     *            discount
     */
    public void setMaxDiscountedProductDiscount(float maxDiscountedProductDiscount) {
	this.maxDiscountedProductDiscount = maxDiscountedProductDiscount;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + categoryId;
	return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ChildCategory other = (ChildCategory) obj;
	if (categoryId != other.categoryId)
	    return false;
	return true;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ChildCategory [categoryId=" + categoryId + "]";
    }

}
