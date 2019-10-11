package com.hierarchy;

/**
 * The Class InputNode.
 */
public class InputNode {

    /** The id. */
    int id;

    /** The name. */
    String name;

    /** The price. */
    float price;

    /**
     * Instantiates a new input node.
     *
     * @param id the id
     * @param name the name
     * @param price the price
     */
    public InputNode(int id, String name, float price) {
	super();
	this.id = id;
	this.name = name;
	this.price = price;
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
     * Gets the price.
     *
     * @return the price
     */
    public float getPrice() {
	return price;
    }

}
