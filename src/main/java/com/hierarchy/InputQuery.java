package com.hierarchy;

/**
 * The Class InputQuery.
 */
public class InputQuery {

    /** The query type. */
    int queryType;

    /** The discount. */
    float discount;

    /** The node id. */
    int nodeId;

    /** The parent id. */
    int parentId;

    /** The price. */
    float price;

    /** The node name. */
    String nodeName;

    /**
     * Instantiates a new input query.
     */
    public InputQuery() {
    }

    /**
     * Gets the query type.
     *
     * @return the query type
     */
    public int getQueryType() {
	return queryType;
    }

    /**
     * Sets the query type.
     *
     * @param queryType the new query type
     */
    public void setQueryType(int queryType) {
	this.queryType = queryType;
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
     * Gets the node id.
     *
     * @return the node id
     */
    public int getNodeId() {
	return nodeId;
    }

    /**
     * Sets the node id.
     *
     * @param nodeId the new node id
     */
    public void setNodeId(int nodeId) {
	this.nodeId = nodeId;
    }

    /**
     * Gets the parent id.
     *
     * @return the parent id
     */
    public int getParentId() {
	return parentId;
    }

    /**
     * Sets the parent id.
     *
     * @param parentId the new parent id
     */
    public void setParentId(int parentId) {
	this.parentId = parentId;
    }

    /**
     * Gets the price.
     *
     * @return the price
     */
    public float getPrice() {
	return price;
    }

    /**
     * Sets the price.
     *
     * @param price the new price
     */
    public void setPrice(float price) {
	this.price = price;
    }

    /**
     * Gets the node name.
     *
     * @return the node name
     */
    public String getNodeName() {
	return nodeName;
    }

    /**
     * Sets the node name.
     *
     * @param nodeName the new node name
     */
    public void setNodeName(String nodeName) {
	this.nodeName = nodeName;
    }

}
