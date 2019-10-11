package com.hierarchy;

/**
 * The Class InputRelationship.
 */
public class InputRelationship {

    /** The node 1 id. */
    int node1Id;

    /** The node 2 id. */
    int node2Id;

    /**
     * Instantiates a new input relationship.
     *
     * @param node1Id the node 1 id
     * @param node2Id the node 2 id
     */
    public InputRelationship(int node1Id, int node2Id) {
	super();
	this.node1Id = node1Id;
	this.node2Id = node2Id;
    }

    /**
     * Gets the node 1 id.
     *
     * @return the node 1 id
     */
    public int getNode1Id() {
	return node1Id;
    }

    /**
     * Gets the node 2 id.
     *
     * @return the node 2 id
     */
    public int getNode2Id() {
	return node2Id;
    }

}
