Demonstration of single threaded operation for CRUD, price update and price query on categoriezed products (in memory).
Solution is optimized for speed.

Structure is presented as tree of categories with products at bottom.

## Nodes:
1. Category: A category can have any number of categories / products underneath. A category does not have any price information.
2. Product: A product can exist only at leaf level.

## Operation:
1. Add a category under a parent category.
2. Add a product under a parent category.
3. Delete category. Deleting a category will delete all the categories and products in the subtree of category being deleted.
4. Delete product.
5. Apply absolute discount on a category. If successful it should reduce the price of all products in category subtree by absolute discount provided.
   For an unsuccessful operation check rules section below.
6. Apply % discount. It will behave as absolute discount, however it will change the price of product by given % if applied successfully.
7. Given a category id, get the maximum discounted product (product having maximum difference between purchase price and selling price) under
   category subtree.

## Rules:
1. A valid discount on any level will be applied to all it's children (recursively till the leaf level).
2. If applying discount at any category makes selling price of any of product in it's subtree negative, 
   then the operation of applying discount can not be carried out at category level. 
   In this case none of the product price in the subtree of the category will be affected.
3. If applying a discount on a product price makes the selling price to go negative, then the operation should not be carried.
   Price of product should remain un affected.
4. A product can belong to only one category.
5. Price should be shown to nearest integer value.
   
## Assumption:
1. There is only one root category.
2. A -ve price during add operation denotes adding a category.

## Input:

<number of operations for defining category / product>
--individual operations in format:
id price name
E.g.
6 1000 A
--operations to form relationship between two categories / between a category / product in format:
id1 id2
where id1 is parent of id2
E.g.
4 6
<number of operations tree>
--individual operations in format:
operation_type operation
E.g.
1 1 10
% discount: <QueryType> <CategoryId/ProductId> <Discount Percent>

### Number of possible operations on tree (query type is fixed as shown in example):

### % discount: <QueryType> <CategoryId/ProductId> <Discount Percent>
E.g. 1 1 10

### Flat discount: <QueryType> <CategoryId/ProductId> <Flat Amount>
E.g. 2 9 100

### Get maximum discounted product: <QueryType> <CategoryId/ProductId>
E.g. 3 9

### Add category / product: <QueryType> <ParentCategotryId> <NewNodeId> <Price> <NewNodeName>
E.g. 4 4 23 -1 Android Phones

### Delete Category / product: <QueryType> <CategoryId/ProductId>
E.g. 5 6


## Sample input file:

4

6 1000 A

9 700 B

4 -1 Mobiles

1 -1 All Products

4 6

4 9

1 4

3

1 1 10

1 4 30

3 4

## Sample output of above file:

Discount applied.

Discount applied.

9 B 700.0 378.0

Few input samples attached with the program.
