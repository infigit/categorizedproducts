package com.hierarchy;

/**
 * The class ChildProduct.
 * <p>
 * This representation contains product id and absolute discount. This helps in
 * minimizing the memory foot print while storing child product information.
 * </p>
 */
public class ChildProduct {

    /** The product id. */
    int productId;

    /** The absolute discount. */
    float absoluteDiscount;

    /**
     * Gets the product id.
     *
     * @return the product id
     */
    public int getProductId() {
	return productId;
    }

    /**
     * Sets the product id.
     *
     * @param productId the new product id
     */
    public void setProductId(int productId) {
	this.productId = productId;
    }

    /**
     * Gets the absolute discount.
     *
     * @return the absolute discount
     */
    public float getAbsoluteDiscount() {
	return absoluteDiscount;
    }

    /**
     * Sets the absolute discount.
     *
     * @param absoluteDiscount the new absolute discount
     */
    public void setAbsoluteDiscount(float absoluteDiscount) {
	this.absoluteDiscount = absoluteDiscount;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + productId;
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
	ChildProduct other = (ChildProduct) obj;
	if (productId != other.productId)
	    return false;
	return true;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ChildProduct [productId=" + productId + "]";
    }

}
