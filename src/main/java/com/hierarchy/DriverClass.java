package com.hierarchy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DriverClass.
 * <p>
 * This class reads the input from file as 'FileReader(new
 * File("./src/main/resources/input2.txt"))'. Replace this file with your input
 * file to run program. All the classes represented by 'Input*' are just
 * supporting classes to read input file. They are not used otherwise.
 * </p>
 * <p>
 * Output is printed on console as well as in log file:
 * ./src/main/resources/logs/output.log
 * </p>
 * <p>
 * Change the root level of logger in ./src/main/resources/log4.properties to
 * see debug output.
 * </p>
 */
public class DriverClass {

    /** The LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverClass.class);

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws NumberFormatException the number format exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws NumberFormatException, IOException {

	BufferedReader br = null;
	try {
	    // Pass your input file here. Sample files are include with this
	    // project
	    FileReader fileReader = new FileReader(new File("./src/main/resources/input.txt"));
	    br = new BufferedReader(fileReader);

	    HierarchyUsingChildCategoryAndProduct hierarchy = new HierarchyUsingChildCategoryAndProduct();

	    Integer noOfNodes = Integer.parseInt(br.readLine()); // Reading

	    // Prepare list of input nodes by reading next N lines
	    List<InputNode> inputNodes = new LinkedList<InputNode>();
	    for (int i = 0; i < noOfNodes; i++) {
		String nodeInfo = br.readLine();
		String[] nodeAttributes = nodeInfo.split(" ");
		int nodeId = Integer.parseInt(nodeAttributes[0].trim());
		float nodePrice = Float.parseFloat(nodeAttributes[1].trim());
		String nodeName = nodeAttributes[2].trim();

		InputNode inputNode = new InputNode(nodeId, nodeName, nodePrice);
		inputNodes.add(inputNode);
		hierarchy.addNodeInfo(nodeId, nodeName, nodePrice);
	    }

	    // Prepare list of relationship between nodes by reading next N-1
	    // lines
	    List<InputRelationship> inputRelationships = new LinkedList<InputRelationship>();
	    for (int i = 0; i < noOfNodes - 1; i++) {
		String nodeInfo = br.readLine();
		String[] relationAttribute = nodeInfo.split(" ");
		int node1Id = Integer.parseInt(relationAttribute[0].trim());
		int node2Id = Integer.parseInt(relationAttribute[1].trim());

		InputRelationship inputRelationship = new InputRelationship(node1Id, node2Id);
		inputRelationships.add(inputRelationship);
		hierarchy.addNode(node1Id, node2Id);
	    }

	    // Reading the number of queries
	    Integer noOfQueries = Integer.parseInt(br.readLine());

	    // Prepare list of input queries by reading
	    List<InputQuery> inputQueries = new LinkedList<InputQuery>();
	    for (int i = 0; i < noOfQueries; i++) {
		String query = br.readLine();
		String[] queryAttributes = query.split(" ");

		int queryType = Integer.parseInt(queryAttributes[0].trim());
		switch (queryType) {
		// % discount: <QueryType> <CategoryId/ProductId> <Discount
		// Percent>
		case 1: {
		    InputQuery inputQuery = new InputQuery();
		    inputQuery.setQueryType(queryType);
		    int nodeId = Integer.parseInt(queryAttributes[1].trim());
		    float discount = Float.parseFloat(queryAttributes[2].trim());
		    inputQuery.setNodeId(nodeId);
		    inputQuery.setDiscount(discount);
		    inputQueries.add(inputQuery);
		    LOGGER.debug(">>>>>>> % discount query on node [{}] with value [{}]", nodeId, discount);
		    LOGGER.info(hierarchy.applyDiscountOnNode(nodeId, 1, discount));

		    break;
		}
		// Flat discount: <QueryType> <CategoryId/ProductId> <Flat
		// Amount>
		case 2: {
		    InputQuery inputQuery = new InputQuery();
		    inputQuery.setQueryType(queryType);
		    int nodeId = Integer.parseInt(queryAttributes[1].trim());
		    float discount = Float.parseFloat(queryAttributes[2].trim());
		    inputQuery.setNodeId(nodeId);
		    inputQuery.setDiscount(discount);
		    inputQueries.add(inputQuery);
		    LOGGER.info(hierarchy.applyDiscountOnNode(nodeId, 2, discount));
		    break;
		}
		// Read discount: <QueryType> <CategoryId/ProductId>
		case 3: {
		    InputQuery inputQuery = new InputQuery();
		    inputQuery.setQueryType(queryType);
		    int nodeId = Integer.parseInt(queryAttributes[1].trim());
		    inputQuery.setNodeId(nodeId);
		    inputQueries.add(inputQuery);
		    Product product = hierarchy.getMaximumDiscountedProduct(nodeId);
		    if (product != null) {
			LOGGER.info("{} {} {} {}", product.getId(), product.getName(), product.getOriginalPrice(),
				product.getSellingPrice());
		    } else {
			LOGGER.info("Invalid ID.");
		    }
		    break;
		}
		// Add category / product: <QueryType> <ParentCategotryId>
		// <NewNodeId> <Price> <NewNodeName>
		case 4: {
		    InputQuery inputQuery = new InputQuery();
		    inputQuery.setQueryType(queryType);
		    int parentId = Integer.parseInt(queryAttributes[1].trim());
		    int nodeId = Integer.parseInt(queryAttributes[2].trim());
		    float price = Float.parseFloat(queryAttributes[3].trim());
		    String nodeName = queryAttributes[4];
		    inputQuery.setParentId(parentId);
		    inputQuery.setNodeId(nodeId);
		    inputQuery.setPrice(price);
		    inputQuery.setNodeName(nodeName);
		    inputQueries.add(inputQuery);
		    LOGGER.info(hierarchy.addNewNode(parentId, nodeId, price, nodeName));
		    break;
		}
		// Delete Category / product: <QueryType> <CategoryId/ProductId>
		case 5: {
		    InputQuery inputQuery = new InputQuery();
		    inputQuery.setQueryType(queryType);
		    int nodeId = Integer.parseInt(queryAttributes[1].trim());
		    inputQuery.setNodeId(nodeId);
		    inputQueries.add(inputQuery);
		    LOGGER.info(hierarchy.removeNode(nodeId));
		    break;
		}
		default: {
		    LOGGER.error("Invalid query type");
		    break;
		}
		}
	    }
	} finally {
	    br.close();
	}
    }
}
