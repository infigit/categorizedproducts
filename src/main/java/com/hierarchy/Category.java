package com.hierarchy;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * The class representing Category.
 * <p>
 * A category has list of direct child category, direct child products;
 * respective ids of child categories and products to support crud operation on
 * treeset (as java.util.treeset is showing erratic behavior while
 * addition/removal)
 * </p>
 * It has reference of parent category id. For root category, the parent
 * category id will be 0.
 * <p>
 * A category also holds reference of maximum discounted product under its
 * hierarchy
 * </p>
 */
public class Category {

    /** The id. */
    private int id;

    /** The name. */
    private String name;

    /** The parent category id. */
    private int parentCategoryId;

    /** The direct child product ids. */
    private Set<Integer> directChildProductIds;

    /** The direct child category ids. */
    private Set<Integer> directChildCategoryIds;

    /** The direct child products. */
    private TreeSet<ChildProduct> directChildProducts;

    /** The direct child categories. */
    private TreeSet<ChildCategory> directChildCategories;

    /** The max discounted product id. */
    private int maxDiscountedProductId;

    /** The max discounted product discount. */
    private float maxDiscountedProductDiscount;

    /**
     * Instantiates a new category.
     *
     * @param id the id
     * @param name the name
     */
    public Category(int id, String name) {
	this.id = id;
	this.name = name;
	this.directChildProducts = new TreeSet<ChildProduct>(new MaxDiscountChildProductComparator());
	this.directChildCategories = new TreeSet<ChildCategory>(new MaxDiscountChildCategoryComparator());
	this.directChildProductIds = new HashSet<Integer>();
	this.directChildCategoryIds = new HashSet<Integer>();
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
     * Gets the parent category id.
     *
     * @return the parent category id
     */
    public int getParentCategoryId() {
	return parentCategoryId;
    }

    /**
     * Sets the parent category id.
     *
     * @param parentCategoryId the new parent category id
     */
    public void setParentCategoryId(int parentCategoryId) {
	this.parentCategoryId = parentCategoryId;
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
     * Gets the direct child product ids.
     *
     * @return the direct child product ids
     */
    public Set<Integer> getDirectChildProductIds() {
	return directChildProductIds;
    }

    /**
     * Sets the direct child product ids.
     *
     * @param directChildProductIds the new direct child product ids
     */
    public void setDirectChildProductIds(Set<Integer> directChildProductIds) {
	this.directChildProductIds = directChildProductIds;
    }

    /**
     * Gets the direct child category ids.
     *
     * @return the direct child category ids
     */
    public Set<Integer> getDirectChildCategoryIds() {
	return directChildCategoryIds;
    }

    /**
     * Sets the direct child category ids.
     *
     * @param directChildCategoryIds the new direct child category ids
     */
    public void setDirectChildCategoryIds(Set<Integer> directChildCategoryIds) {
	this.directChildCategoryIds = directChildCategoryIds;
    }

    /**
     * Gets the direct child products.
     *
     * @return the direct child products
     */
    public TreeSet<ChildProduct> getDirectChildProducts() {
	return directChildProducts;
    }

    /**
     * Sets the direct child products.
     *
     * @param directChildProducts the new direct child products
     */
    public void setDirectChildProducts(TreeSet<ChildProduct> directChildProducts) {
	this.directChildProducts = directChildProducts;
    }

    /**
     * Gets the direct child categories.
     *
     * @return the direct child categories
     */
    public TreeSet<ChildCategory> getDirectChildCategories() {
	return directChildCategories;
    }

    /**
     * Sets the direct child categories.
     *
     * @param directChildCategories the new direct child categories
     */
    public void setDirectChildCategories(TreeSet<ChildCategory> directChildCategories) {
	this.directChildCategories = directChildCategories;
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
	Category other = (Category) obj;
	if (id != other.id)
	    return false;
	return true;
    }
}
