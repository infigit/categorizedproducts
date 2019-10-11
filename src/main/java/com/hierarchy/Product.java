package com.hierarchy;

/**
 * The Class representing Product.
 * <p>
 * Along with important attribute it has reference to it's parent category
 * </p>
 */
public class Product {

    /** The id. */
    int id;

    /** The name. */
    String name;

    /** The original price. */
    float originalPrice;

    /** The selling price. */
    float sellingPrice;

    /** The discount. */
    float discount;

    /** The category id. */
    int categoryId;

    /**
     * Instantiates a new product.
     *
     * @param id the id
     * @param name the name
     * @param originalPrice the original price
     */
    public Product(int id, String name, float originalPrice) {
	this.id = id;
	this.name = name;
	this.originalPrice = originalPrice;
	this.sellingPrice = originalPrice;
	this.discount = 0;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
	return id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Gets the original price.
     *
     * @return the original price
     */
    public float getOriginalPrice() {
	return originalPrice;
    }

    /**
     * Sets the original price.
     *
     * @param originalPrice the new original price
     */
    public void setOriginalPrice(float originalPrice) {
	this.originalPrice = originalPrice;
    }

    /**
     * Gets the selling price.
     *
     * @return the selling price
     */
    public float getSellingPrice() {
	return sellingPrice;
    }

    /**
     * Sets the selling price.
     *
     * @param sellingPrice the new selling price
     */
    public void setSellingPrice(float sellingPrice) {
	this.sellingPrice = sellingPrice;
    }

    /**
     * Gets the category id.
     *
     * @return the category id
     */
    public int getCategoryId() {
	return categoryId;
    }

    /**
     * Gets the discount.
     *
     * @return the discount
     */
    public float getDiscount() {
	return discount;
    }

    /**
     * Sets the discount.
     *
     * @param discount the new discount
     */
    public void setDiscount(float discount) {
	this.discount = discount;
    }

    /**
     * Sets the category id.
     *
     * @param categoryId the new category id
     */
    public void setCategoryId(int categoryId) {
	this.categoryId = categoryId;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + id;
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
	Product other = (Product) obj;
	if (id != other.id)
	    return false;
	return true;
    }

}