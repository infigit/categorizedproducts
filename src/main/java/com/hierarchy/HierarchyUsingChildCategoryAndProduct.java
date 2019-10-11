/*
 *
 */
package com.hierarchy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class HierarchyUsingChildCategoryAndProduct.
 * <p>
 * If the node being added has price as -1 it is treated as category. Category
 * data structure itself does not contains any price information.
 * </p>
 * <p>
 * A valid discount is applicable on empty category. Check
 * discountApplicableOnCategory for more information.
 * </p>
 * <p>
 * Child categories and child products are represented as java.uti.TreeSet as
 * Treeset guarantee log(n) time for add, contains and remove. Here however the
 * equality and comparator is not working during the remove and add operation.
 * As work around these children are populated new each time, making the
 * complexity as nlog(n). This is also mentioned as TODO at relevant sections.
 * Also the in-place update and sorting as described here:
 * https://stackoverflow.com/questions/2579679/maintaining-treeset-sort-as-object-changes-value
 * can not be used due to recursive nature of operations.
 * </p>
 * <p>
 * Any crud or update (valid discount) operation on hierarchy keeps the whole
 * tree consistent with the max discounted product. This also has performance
 * impact on most of the operations. With java.util.tree set it should run under
 * hlog(n) time where h represents the height of tree upwards from that node,
 * however due to treemap irratic behavior it runs in max hnlog(n) time.
 * </p>
 * <p>
 * Hierarchy constructed here makes assumption that a product or a category will
 * have a single parent. The code is tested for the same. In case of violation
 * in this regard behavior is unexpected.
 * </p>
 */
public class HierarchyUsingChildCategoryAndProduct {

	private static final String DISCOUNT_CANNOT_BE_APPLIED = "Discount cannot be applied.";

	private static final String DISCOUNT_APPLIED = "Discount applied.";

	private static final String PRODUCT_ADDED = "Product added.";

	private static final String CATEGORY_ADDED = "Category added.";

	private static final String CATEGORY_DELETED = "Category deleted.";

	private static final String PRODUCT_DELETED = "Product deleted.";

	private static final String INVALID_ID = "Invalid ID.";

	/** The categories. Global reference to hold all categories in this tree. */
	Map<Integer, Category> categories;

	/** The products. Global reference to hold all products in this tree. */
	Map<Integer, Product> products;

	/** The LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HierarchyUsingChildCategoryAndProduct.class);

	/**
	 * Instantiates a new hierarchy using child category and product.
	 */
	public HierarchyUsingChildCategoryAndProduct() {
		this.categories = new HashMap<>();
		this.products = new HashMap<>();
	}

	/**
	 * Adds the node info. if price is negative then it is treated as category.
	 *
	 * @param id    the id
	 * @param name  the name
	 * @param price the price
	 * @return the string
	 */
	public String addNodeInfo(final int id, final String name, final float price) {
		if (price == -1) {
			return addCategoryInfo(id, name);
		}
		return addProductInfo(id, name, price);
	}

	/**
	 * Adds the node by searching in existing node information. In case both node1id
	 * and node2id are category id, this assume that first is parent and second is
	 * child.
	 *
	 * @param node1Id the node 1 id
	 * @param node2Id the node 2 id
	 * @return the string
	 */
	public String addNode(final int node1Id, final int node2Id) {
		if (this.products.containsKey(node1Id) && this.products.containsKey(node2Id)) {
			return "Invalid nodes. Both are products";
		}
		// If both are categories
		if (this.categories.containsKey(node1Id) && this.categories.containsKey(node2Id)) {
			return addExistingCategory(node1Id, node2Id);
		}
		int categoryId = 0;
		int productId = 0;
		if (this.categories.containsKey(node1Id)) {
			categoryId = node1Id;
			productId = node2Id;
		} else {
			categoryId = node2Id;
			productId = node1Id;
		}

		return addExistingProduct(categoryId, productId);
	}

	/**
	 * Adds the new node. Same as addNode, however this method first create the node
	 * information, store the global reference and then call to add existing
	 * category or add existing product.
	 *
	 * @param parentCategoryId the parent category id
	 * @param nodeId           the node id
	 * @param price            the price
	 * @param nodeName         the node name
	 * @return the string
	 */
	public String addNewNode(final int parentCategoryId, final int nodeId, final float price, final String nodeName) {
		if (!this.categories.containsKey(parentCategoryId)) {
			return "Invalid parent category id.";
		}
		// This new node is category so create category info and then call add
		// existing category
		if (price == -1) {
			if (this.categories.containsKey(nodeId)) {
				return "Category not added.";
			}
			addCategoryInfo(nodeId, nodeName);
			return addExistingCategory(parentCategoryId, nodeId);
		} else {
			// This new node is product so create product info and then call add
			// existing product
			if (this.products.containsKey(nodeId)) {
				return "Product not added.";
			}
			addProductInfo(nodeId, nodeName, price);
			return addExistingProduct(parentCategoryId, nodeId);
		}
	}

	/**
	 * Update max discounted product.
	 * <p>
	 * Step 1: Update the discounted production information for current category
	 * </p>
	 * <p>
	 * Step 2: Iterate over the list of immediate child products and sub categories.
	 * Choose the maximum discounted product from them. In case there are two
	 * products with same discount, select the product with minimum id.
	 * </p>
	 * <p>
	 * Step 3: Repopulate the child category for current category
	 * </p>
	 * <p>
	 * Step 4: Check if it is required to update parent category maximum discounted
	 * product. If yes update the parent category max discounted product information
	 * and trigger recursive update for parent hierarchy.
	 * </p>
	 *
	 * @param category the category
	 */
	private void updateMaxDiscountedProduct(final Category category) {

		// Step 1
		// First update the value of discount of product with maximum discount
		// under this category in case the product current value is updated
		// TODO: Following section can be removed if can get the
		// java.util.treeset insertion and deletion with ordering to work
		// properly
		if (category.getMaxDiscountedProductId() != 0) {
			// If the product has been deleted then set the discounted product
			// id and discount to 0
			if (this.products.get(category.getMaxDiscountedProductId()) == null) {
				category.setMaxDiscountedProductId(0);
				category.setMaxDiscountedProductDiscount(0);
			} else {
				category.setMaxDiscountedProductDiscount(
						this.products.get(category.getMaxDiscountedProductId()).getDiscount());
			}
		}

		// Step 2
		// Get the max discounted direct product of this category
		ChildProduct tempChildProduct = null;
		if (category.getDirectChildProducts().size() > 0) {
			tempChildProduct = category.getDirectChildProducts().last();
		}

		// Get the child category having maximum discounted product
		ChildCategory tempChildCategory = null;
		if (category.getDirectChildCategories().size() > 0) {
			tempChildCategory = category.getDirectChildCategories().last();
		}

		int maxDiscountedProductId = 0;
		float maxDiscountedProductDiscount = 0;

		if (tempChildProduct != null && tempChildCategory != null) {
			// If the child product has more discount than child category select
			// product
			if (tempChildProduct.getAbsoluteDiscount() > tempChildCategory.getMaxDiscountedProductDiscount()) {
				maxDiscountedProductId = tempChildProduct.getProductId();
				maxDiscountedProductDiscount = tempChildProduct.getAbsoluteDiscount();
				// If the child category has more discounted product than child
				// product then select product from that child category
			} else if (tempChildProduct.getAbsoluteDiscount() < tempChildCategory.getMaxDiscountedProductDiscount()) {
				maxDiscountedProductId = tempChildCategory.getMaxDiscountedProductId();
				maxDiscountedProductDiscount = tempChildCategory.getMaxDiscountedProductDiscount();
			} else {

				// In case discount is same in both case, select the product
				// having smaller id
				maxDiscountedProductId = tempChildProduct.getProductId() < tempChildCategory.getMaxDiscountedProductId()
						? tempChildProduct.getProductId()
						: tempChildCategory.getMaxDiscountedProductId();
				maxDiscountedProductDiscount = tempChildProduct.getAbsoluteDiscount();
			}
		} else if (tempChildProduct != null) {
			maxDiscountedProductId = tempChildProduct.getProductId();
			maxDiscountedProductDiscount = tempChildProduct.getAbsoluteDiscount();
		} else if (tempChildCategory != null) {
			maxDiscountedProductId = tempChildCategory.getMaxDiscountedProductId();
			maxDiscountedProductDiscount = tempChildCategory.getMaxDiscountedProductDiscount();
		}

		// Compare the above selected product with maximum discounted product at
		// this category and update the category discounted product accordingly
		if (category.getMaxDiscountedProductId() == 0) {
			category.setMaxDiscountedProductId(maxDiscountedProductId);
			category.setMaxDiscountedProductDiscount(maxDiscountedProductDiscount);
		} else {
			if (category.getMaxDiscountedProductDiscount() < maxDiscountedProductDiscount) {
				category.setMaxDiscountedProductId(maxDiscountedProductId);
				category.setMaxDiscountedProductDiscount(maxDiscountedProductDiscount);
			} else if (category.getMaxDiscountedProductDiscount() == maxDiscountedProductDiscount) {
				if (maxDiscountedProductId != 0 && category.getMaxDiscountedProductId() > maxDiscountedProductId) {
					category.setMaxDiscountedProductId(maxDiscountedProductId);
				}
			}
		}

		// Step 3
		// Now update the discount information in parent category tree
		// Go to next immediate parent and update the discounted parent id
		// in the parent category list
		int parentCategoryId = category.getParentCategoryId();
		if (parentCategoryId != 0) {
			Category parentCategory = this.categories.get(parentCategoryId);
			repopulateChildCategories(parentCategory);
		}

		// Step 4
		// Check if the changes in current category maximum discounted product
		// need to trigger update in parent category
		Category parentCategory = this.categories.get(category.getParentCategoryId());
		if (parentCategory != null) {
			if (parentCategory.getMaxDiscountedProductDiscount() <= category.getMaxDiscountedProductDiscount()) {
				// Trigger update for maximum discounted product for parent
				updateMaxDiscountedProduct(parentCategory);
			}
		}
	}

	/**
	 * Removes the node.
	 *
	 * @param nodeId the node id
	 * @return the string
	 */
	public String removeNode(final int nodeId) {
		if (this.products.containsKey(nodeId)) {
			return removeProduct(nodeId);
		} else if (this.categories.containsKey(nodeId)) {
			return removeCategory(nodeId);
		}

		return INVALID_ID;
	}

	/**
	 * Apply discount on node. Return Invalid ID. in case the node id is not valid.
	 *
	 * @param nodeId       the node id
	 * @param discountType the discount type
	 * @param discount     the discount
	 * @return the string
	 */
	public String applyDiscountOnNode(final int nodeId, final int discountType, final float discount) {

		if (this.categories.containsKey(nodeId)) {
			return applyDiscountOnCategory(nodeId, discountType, discount);
		} else if (this.products.containsKey(nodeId)) {
			return applyDiscountOnProduct(nodeId, discountType, discount);
		}

		return INVALID_ID;
	}

	/**
	 * Apply discount on category.
	 *
	 * @param categoryId   the category id
	 * @param discountType the discount type
	 * @param discount     the discount
	 * @return the string
	 */
	public String applyDiscountOnCategory(final int categoryId, final int discountType, final float discount) {
		return applyDiscountOnCategory(categoryId, discountType, discount, true);
	}

	/**
	 * Discount applicable on category. This method recurse down the whole tree to
	 * check every product under this category and subcategories if discount can be
	 * applied or not.
	 * <p>
	 * Though category itself does not have any price information and applying any
	 * discount on an empty category ideally should not have any affect, -ve flat or
	 * % discount is not allowed, % discount greater than 100 is not allowed as to
	 * be coherent with strategy for a product when applying discount on a
	 * hierarchy.
	 * </p>
	 * 
	 * @param categoryId   the category id
	 * @param discountType the discount type 1: for % discount and 2: for flat
	 *                     discount
	 * @param discount     the discount
	 * @return true, if successful
	 */
	private boolean discountApplicableOnCategory(final int categoryId, final int discountType, final float discount) {
		Category category = this.categories.get(categoryId);
		if (category == null) {
			return false;
		}

		// Flat discount
		if (discountType == 2) {
			if (discount < 0) {
				return false;
			}
		} else if (discountType == 1) {
			// % discount
			if (discount < 0) {
				return false;
			} else if (discount > 100) {
				return false;
			}
		}

		Set<Integer> childProductIds = category.getDirectChildProductIds();

		Set<Integer> childCategoryIds = category.getDirectChildCategoryIds();
		// Check if the discount can be applied to immediate product children
		for (Integer childProductId : childProductIds) {
			boolean applicable = discountApplicableOnProduct(childProductId, discountType, discount);
			if (!applicable) {
				return false;
			}
		}

		// Check if the discount can be applied to sub categories
		for (Integer childCategoryId : childCategoryIds) {
			LOGGER.debug("checking applicability of discout of type [{}] with value [{}] on category [{}]",
					discountType, discount, childCategoryId);
			boolean applicable = discountApplicableOnCategory(childCategoryId, discountType, discount);
			if (!applicable) {
				return false;
			}
		}

		LOGGER.debug("Discount of type [{}] with value [{}] is applicable on category [{}]", discountType, discount,
				categoryId);
		return true;

	}

	/**
	 * Removes the product. Removing a product also triggers updation of max
	 * discounted product for its parent category and category hierarchy till root.
	 *
	 * @param productId the product id
	 * @return the string 'Invalid ID.' or 'Product deleted.'
	 */
	private String removeProduct(final int productId) {
		Product product = this.products.get(productId);
		if (product == null) {
			return INVALID_ID;
		}

		// Get the category containing this product
		Category category = this.categories.get(product.getCategoryId());

		// Remove this product from category children
		ChildProduct childProduct = new ChildProduct();
		childProduct.setProductId(productId);
		childProduct.setAbsoluteDiscount(product.getDiscount());
		LOGGER.debug("number of direct child products under category [{}] before product [{}] removal [{}]",
				category.getId(), productId, category.getDirectChildProducts().size());
		category.getDirectChildProductIds().remove(childProduct.getProductId());

		repopulateChildProducts(category);

		LOGGER.debug("number of direct child products under category [{}] after product [{}] removal [{}]",
				category.getId(), productId, category.getDirectChildProducts().size());
		// Remove product from global products
		this.products.remove(productId);

		// If this product is maximum discounted product under this category,
		// clear it from category
		if (category.getMaxDiscountedProductId() == productId) {
			category.setMaxDiscountedProductId(0);
			category.setMaxDiscountedProductDiscount(0);

			// Go to next immediate parent and update the discounted parent id
			// in the parent category list
			int parentCategoryId = category.getParentCategoryId();
			if (parentCategoryId != 0) {
				Category parentCategory = this.categories.get(parentCategoryId);
				repopulateChildCategories(parentCategory);
			}
		}

		// Traverse all the parent categories where this product featured as max
		// discount product and remove it
		int parentCategoryId = category.getParentCategoryId();
		while (parentCategoryId != 0) {
			Category parentCategory = this.categories.get(parentCategoryId);
			if (parentCategory.getMaxDiscountedProductId() == productId) {
				parentCategory.setMaxDiscountedProductId(0);
				parentCategory.setMaxDiscountedProductDiscount(0);
			}
			parentCategoryId = parentCategory.getParentCategoryId();
		}
		// Update the max discounted product for that category
		updateMaxDiscountedProduct(category);

		return PRODUCT_DELETED;
	}

	/**
	 * Removes the category. Removing a category also triggers updation of max
	 * discounted product in category hierarchy till root.
	 * 
	 * @param categoryId the category id
	 * @return the string 'Invalid ID.' or 'Category deleted.'
	 */
	private String removeCategory(final int categoryId) {
		Category category = this.categories.get(categoryId);
		if (category == null) {
			return INVALID_ID;
		}

		// Get the parent category of this category
		Category parentCategory = this.categories.get(this.categories.get(categoryId).getParentCategoryId());

		if (parentCategory != null) {
			parentCategory.getDirectChildCategoryIds().remove(categoryId);

			// Recreate childCategory from list of categoryIds
			// This is to prevent erratic behavior of java.util treeset
			repopulateChildCategories(parentCategory);
		}

		// Get the maximum discounted product for this category
		int maxDiscountedProductId = category.getMaxDiscountedProductId();
		// Traverse all the parent categories where this product featured as max
		// discount product and remove it
		int parentCategoryId = category.getParentCategoryId();
		while (parentCategoryId != 0) {
			Category tempParent = this.categories.get(parentCategoryId);
			if (maxDiscountedProductId != 0 && tempParent.getMaxDiscountedProductId() == maxDiscountedProductId) {
				tempParent.setMaxDiscountedProductId(0);
				tempParent.setMaxDiscountedProductDiscount(0);
			}
			parentCategoryId = tempParent.getParentCategoryId();
		}

		// Remove category. Note: Removing this category removes all the
		// subcategory and sub products under this category.
		// categories.remove(categoryId);
		removeCategoryTreeReference(categoryId);

		// Update the max discounted product for that category
		if (parentCategory != null) {
			updateMaxDiscountedProduct(parentCategory);
		}

		return CATEGORY_DELETED;
	}

	/**
	 * Adds the category info.
	 *
	 * @param id   the id
	 * @param name the name
	 * @return the string
	 */
	private String addCategoryInfo(final int id, final String name) {
		if (this.categories.containsKey(id)) {
			return "Category already exist";
		}
		if (this.products.containsKey(id)) {
			return "Invalid category id. A product with same id already exist";
		}
		Category category = new Category(id, name);
		this.categories.put(id, category);
		LOGGER.debug("category info added id [{}], name [{}]", id, name);
		return "New Category information created";
	}

	/**
	 * Adds the product info.
	 *
	 * @param id    the id
	 * @param name  the name
	 * @param price the price
	 * @return the string
	 */
	private String addProductInfo(final int id, final String name, final float price) {
		if (this.products.containsKey(id)) {
			return "Product already exist";
		}
		if (this.categories.containsKey(id)) {
			return "Invalid product id. A category with same id already exist";
		}
		Product product = new Product(id, name, price);
		this.products.put(id, product);
		LOGGER.debug("product info added id [{}], name [{}]", id, name);
		return "New Product information created";
	}

	/**
	 * Adds the existing category. Adds category node represented by categoryId
	 * under parent category represented by parentId.
	 *
	 * @param parentId   the parent id
	 * @param categoryId the category id
	 * @return the string
	 */
	private String addExistingCategory(final int parentId, final int categoryId) {

		// Update the parent id for category
		Category parentCategory = this.categories.get(parentId);
		Category category = this.categories.get(categoryId);
		category.setParentCategoryId(parentId);

		// Add this category as child of parent category
		ChildCategory childCategory = new ChildCategory();
		childCategory.setCategoryId(category.getId());
		childCategory.setMaxDiscountedProductId(category.getMaxDiscountedProductId());
		childCategory.setMaxDiscountedProductDiscount(category.getMaxDiscountedProductDiscount());
		parentCategory.getDirectChildCategories().add(childCategory);
		parentCategory.getDirectChildCategoryIds().add(categoryId);
		LOGGER.debug("added category [{}] to parent category [{}]", categoryId, parentId);

		// Update maximum discounted product in the hierarchy of parent
		// categories of this category due to addition of this category
		updateMaxDiscountedProduct(parentCategory);
		return CATEGORY_ADDED;
	}

	/**
	 * Adds the existing product.
	 *
	 * @param categoryId the category id
	 * @param productId  the product id
	 * @return the string
	 */
	private String addExistingProduct(final int categoryId, final int productId) {

		// Update the parent id for product
		Category category = this.categories.get(categoryId);
		Product product = this.products.get(productId);
		product.setCategoryId(categoryId);

		// Add this product as child of parent category
		ChildProduct childProduct = new ChildProduct();
		childProduct.setProductId(product.getId());
		childProduct.setAbsoluteDiscount(product.getOriginalPrice() - product.getSellingPrice());
		category.getDirectChildProducts().add(childProduct);
		category.getDirectChildProductIds().add(productId);

		LOGGER.debug("added product [{}] to parent category [{}]", productId, categoryId);

		// Update maximum discounted product in the hierarchy of parent
		// categories of this product due to addition of this product
		updateMaxDiscountedProduct(category);

		return PRODUCT_ADDED;
	}

	/**
	 * Repopulate child categories. Helper method to repopulate the child category
	 * treeset which maintain order as defined by comparator
	 * MaxDiscountChildCategoryComparator This method is only required as contains,
	 * insertion and deletion for child treeset is not working properly. Adding
	 * elements to new treeset works,however this increases the complexity to
	 * nlog(n) instead of log(n)
	 *
	 * @param category the category
	 */
	private void repopulateChildCategories(final Category category) {
		TreeSet<ChildCategory> newChildCategories = new TreeSet<>(
				new MaxDiscountChildCategoryComparator());
		for (int directChildCategoryId : category.getDirectChildCategoryIds()) {
			ChildCategory childCategory = new ChildCategory();
			Category tempCategory = this.categories.get(directChildCategoryId);
			childCategory.setCategoryId(tempCategory.getId());
			childCategory.setMaxDiscountedProductId(tempCategory.getMaxDiscountedProductId());
			childCategory.setMaxDiscountedProductDiscount(tempCategory.getMaxDiscountedProductDiscount());
			newChildCategories.add(childCategory);
		}
		category.setDirectChildCategories(newChildCategories);
	}

	/**
	 * Apply discount on category.
	 *
	 * @param categoryId                    the category id
	 * @param discountType                  the discount type 1: % discount, 2: flat
	 *                                      discount
	 * @param discount                      the discount
	 * @param checkForDiscountApplicability the check for discount applicability
	 * @return the string
	 */
	private String applyDiscountOnCategory(final int categoryId, final int discountType, final float discount,
			final boolean checkForDiscountApplicability) {
		Category category = this.categories.get(categoryId);
		if (category == null) {
			return INVALID_ID;
		}

		// Check if discount can be applied or not on this category
		if (checkForDiscountApplicability) {
			if (!discountApplicableOnCategory(categoryId, discountType, discount)) {
				return DISCOUNT_CANNOT_BE_APPLIED;
			}
		}

		Set<Integer> childProductIds = category.getDirectChildProductIds();

		Set<Integer> childCategoryIds = category.getDirectChildCategoryIds();

		TreeSet<ChildProduct> newChildProducts = new TreeSet<>(new MaxDiscountChildProductComparator());
		// Now discount, first on immediate child products
		for (Integer childProductId : childProductIds) {
			// applyDiscountOnProduct(childProduct.getProductId(), discountType,
			// discount);
			Product product = this.products.get(childProductId);
			applyDiscountOnlyOnProduct(product.getId(), discountType, discount);

			ChildProduct newChildProduct = new ChildProduct();
			newChildProduct.setProductId(product.getId());
			newChildProduct.setAbsoluteDiscount(product.getDiscount());
			newChildProducts.add(newChildProduct);
		}

		category.setDirectChildProducts(newChildProducts);
		updateMaxDiscountedProduct(category);

		// Apply discount on category
		for (Integer childCategoryId : childCategoryIds) {
			applyDiscountOnCategory(childCategoryId, discountType, discount, false);
		}

		return DISCOUNT_APPLIED;
	}

	/**
	 * Gets the maximum discounted product. If a valid product is not found returns
	 * null
	 *
	 * @param nodeId the node id
	 * @return the maximum discounted product
	 */
	public Product getMaximumDiscountedProduct(final int nodeId) {
		LOGGER.debug("Getting maximum discounted product for [{}]", nodeId);
		if (this.products.containsKey(nodeId)) {
			return this.products.get(nodeId);
		} else if (this.categories.containsKey(nodeId)) {
			int maxDiscountedProductId = this.categories.get(nodeId).getMaxDiscountedProductId();
			return this.products.get(maxDiscountedProductId);
		}

		return null;
	}

	/**
	 * Repopulate child products. Helper method to repopulate the child product
	 * treeset which maintain order as defined by comparator
	 * MaxDiscountChildProductComparator This method is only required as contains,
	 * insertion and deletion for child treeset is not working properly. Adding
	 * elements to new treeset works,however this increases the complexity to
	 * nlog(n) instead of log(n)
	 *
	 * @param category the category
	 */
	private void repopulateChildProducts(final Category category) {
		TreeSet<ChildProduct> newChildProducts = new TreeSet<>(new MaxDiscountChildProductComparator());
		for (Integer childProductId : category.getDirectChildProductIds()) {
			Product tempProduct = this.products.get(childProductId);
			ChildProduct newChildProduct = new ChildProduct();
			newChildProduct.setProductId(tempProduct.getId());
			newChildProduct.setAbsoluteDiscount(tempProduct.getDiscount());
			newChildProducts.add(newChildProduct);
		}
		category.setDirectChildProducts(newChildProducts);
	}

	/**
	 * Removes the category tree reference from global reference by removing this
	 * category, its sub categories and products under it. This takes O(n) time
	 * where n denoted number of nodes in tree represented by this category.
	 *
	 * @param categoryId the category id
	 */
	private void removeCategoryTreeReference(final int categoryId) {
		Category category = this.categories.get(categoryId);
		// Remove reference of this category
		this.categories.remove(categoryId);
		if (category != null) {
			Set<Integer> directChildCategoryIds = category.getDirectChildCategoryIds();
			Set<Integer> directChildProductsIds = category.getDirectChildProductIds();
			if (directChildProductsIds != null && directChildProductsIds.size() > 0) {
				for (Integer childProductId : directChildProductsIds) {
					this.products.remove(childProductId);
				}
			}
			if (directChildCategoryIds != null && directChildCategoryIds.size() > 0) {
				for (Integer childCategoryId : directChildCategoryIds) {
					removeCategoryTreeReference(childCategoryId);
				}
			}

		}
	}

	/**
	 * Discount applicable on product.
	 *
	 * @param productId    the product id
	 * @param discountType the discount type 1: % discount, 2: flat discount
	 * @param discount     the discount
	 * @return true, if successful
	 */
	private boolean discountApplicableOnProduct(final int productId, final int discountType, final float discount) {
		Product product = this.products.get(productId);
		if (product == null) {
			return false;
		}
		// Flat discount
		if (discountType == 2) {
			if (discount < 0) {
				return false;
			} else if (product.getSellingPrice() < discount) {
				return false;
			}

		} else if (discountType == 1) {
			// % discount
			if (discount < 0) {
				return false;
			} else if (discount > 100) {
				return false;
			}

		}
		LOGGER.debug("Discount applicable on product [{}]", productId);
		return true;
	}

	/**
	 * Apply discount on product.
	 *
	 * @param productId    the product id
	 * @param discountType the discount type 1: % discount, 2: flat discount
	 * @param discount     the discount
	 * @return the string
	 */
	private String applyDiscountOnProduct(final int productId, final int discountType, final float discount) {
		String response = applyDiscountOnlyOnProduct(productId, discountType, discount);
		if (!"product discount applied.".equals(response)) {
			return response;
		}

		Product product = this.products.get(productId);

		LOGGER.debug(
				"discount of type [{}] with value [{}] applied on product [{}]. Original price [{}], selling price [{}]",
				discountType, discount, productId, product.getOriginalPrice(), product.getSellingPrice());
		// Update the maxDiscounted product for the category of this product
		// In order to trigger sorting on child tree set first remove the
		// product and then add the product
		// TODO: Write better code/strategy to update tree set. Currnet way
		// is
		// not efficient it takes 2*log(n) time instead of log(n)
		ChildProduct childProduct = new ChildProduct();
		childProduct.setProductId(productId);
		childProduct.setAbsoluteDiscount(product.getDiscount());
		Category category = this.categories.get(product.getCategoryId());
		LOGGER.debug(
				"Removing and adding this child product [{}] to category [{}] product tree set so that discount applied can take affect on actual order",
				productId, category.getId());
		category.getDirectChildProducts().remove(childProduct);
		category.getDirectChildProducts().add(childProduct);

		// Update max discounted product on parent and higher hierarchy
		// category
		updateMaxDiscountedProduct(category);

		return DISCOUNT_APPLIED;
	}

	/**
	 * Apply discount only on this product. This method does not trigger max
	 * discounted product update on parent hierarchy. See applyDiscountOnProduct for
	 * the same.
	 *
	 * @param productId    the product id
	 * @param discountType the discount type 1: % discount, 2: flat discount
	 * @param discount     the discount
	 * @return the string
	 */
	private String applyDiscountOnlyOnProduct(final int productId, final int discountType, final float discount) {
		Product product = this.products.get(productId);
		if (product == null) {
			return INVALID_ID;
		}
		// Flat discount
		if (discountType == 2) {
			if (discountApplicableOnProduct(productId, discountType, discount)) {
				product.setSellingPrice(product.getSellingPrice() - discount);
				product.setDiscount(product.getOriginalPrice() - product.getSellingPrice());
			} else {
				return DISCOUNT_CANNOT_BE_APPLIED;
			}

		} else if (discountType == 1) {
			// % discount
			if (discountApplicableOnProduct(productId, discountType, discount)) {
				product.setSellingPrice(product.getSellingPrice() * ((100 - discount) / 100));
				product.setDiscount(product.getOriginalPrice() - product.getSellingPrice());
			} else {
				return DISCOUNT_CANNOT_BE_APPLIED;
			}

		}
		return "product discount applied.";
	}
}