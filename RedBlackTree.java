
import java.util.Scanner;

public class RedBlackTree {

    private static final int BLACK = 1;
    private static final int RED = 2;
    private static final int DOUBLE_BLACK = 3;

    private Tree root;
    private Tree aboveTree;
    private Tree nodeToDelete;
    public RedBlackTree() {
        System.out.println("Advanced Algorithms: Red Black Tree\n");
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("1: Insert\n2: Delete\n3: View Tree (Inorder)\n4: Quit\n");
            int i = sc.nextInt();
            switch (i) {
                case 1:{
                    System.out.println("Enter a value to insert");
                    int num = sc.nextInt();
                    insert(num);
                    break;
                }
                case 2:{
                    System.out.println("Enter a value to delete");
                    int num = sc.nextInt();
                    deleteValue(num);
                    break;
                }
                case 3:{
                    printTreeInOrder(root);
                    break;
                }
                case 4: {
                    return;
                }
            }
        }
    }

    private void printTreeInOrder(Tree tree) {
        if(tree == null) return;
        printTreeInOrder(tree.left);
        System.out.println(tree.val +"\t("+ ((tree.color == BLACK)?"Black":"Red")+")");
        printTreeInOrder(tree.right);
    }

    private void insert(int i) {
        if(root == null) {
            root = new Tree();
            root.color = BLACK;
            root.val = i;
            return;
        }
        Tree temp = root;
        Tree back = null;
        while (temp!=null) {
            back = temp;
            if(temp.val>i) temp = temp.left;
            else if(temp.val<i) temp = temp.right;
            else {
                System.out.println("value "+i+" already exists in tree");
                return;
            }
        }
        Tree insertionTree = new Tree();
        insertionTree.val = i;
        insertionTree.color = RED;
        if(i>back.val) back.right = insertionTree;
        else back.left = insertionTree;
        balanceForInsertion(null,root);
    }

    private void balanceForInsertion(Tree up,Tree tree) {
        if(tree.color == RED) {

            //Red Parent, Black Uncle Red Right Child
            if (tree == up.right && (up.left == null || up.left.color == BLACK) && (tree.right != null && tree.right.color == RED)) {
                performLeftRotation(up,tree);
                up.color = RED;
                tree.color = BLACK;
                System.out.println(tree.val+"\t"+"Red Parent, Black Uncle Red Right Child");
                balanceForInsertion(null, root);
                return;
            }

            //Mirror Case
            else  if (tree == up.left && (up.right == null || up.right.color == BLACK) && (tree.left != null && tree.left.color == RED)) {
                performRightRotation(up, tree);
                up.color = RED;
                tree.color = BLACK;
                System.out.println(tree.val+"\t"+"Red Parent, Black Uncle Red Left Child");
                balanceForInsertion(null, root);
                return;
            }

            //Red Parent, Red Uncle, Red Left Child
            else if(tree == up.left && (up.right!=null && up.right.color == RED) && (tree.left!=null && tree.left.color == RED)) {
                tree.color = BLACK;
                up.right.color = BLACK;
                if(up!=root) up.color = RED;
                System.out.println(tree.val+"\t"+"Red Parent, Red Uncle Red Left Child");
                balanceForInsertion(null, root);
                return;
            }

            //Mirror Case
            else if(tree == up.right && (up.left!=null && up.left.color == RED) && (tree.right!=null && tree.right.color == RED)) {
                tree.color = BLACK;
                up.left.color = BLACK;
                if(up!=root) up.color = RED;
                System.out.println(tree.val+"\t"+"Red Parent, Red Uncle Red Right Child");
                balanceForInsertion(null, root);
                return;
            }

            //tree (red) -> left child of parent (up) and tree.right is red
            else if(tree == up.left && (tree.right!=null && tree.right.color == RED)) {
                if(up==root) root = tree;
                performLeftRotation(up, tree);
                System.out.println(tree.val+"\t"+"tree (red) -> left child of parent (up) and tree.right is red");
                balanceForInsertion(null, root);
                return;
            }

            //Mirror Case
            else if(tree == up.right && (tree.left!=null && tree.left.color == RED)) {
                if(up==root) root = tree;
                performRightRotation(up, tree);
                System.out.println(tree.val+"\t"+"tree (red) -> right child of parent (up) and tree.left is red");
                balanceForInsertion(null, root);
                return;
            }
        }
        if(tree.left!=null) balanceForInsertion(tree,tree.left);
        if(tree.right!=null) balanceForInsertion(tree,tree.right);
    }

    private void performLeftRotation(Tree up, Tree tree) {
        if(up==root) root = tree;
        else {
            findLocation(up,root);
            if(aboveTree.left == up) aboveTree.left = tree;
            else aboveTree.right = tree;
        }
        Tree temp = tree.left;
        tree.left = up;
        up.right = temp;
    }

    private void performRightRotation(Tree up, Tree tree) {
        if(up==root) root = tree;
        else {
            findLocation(up,root);
            if(aboveTree.left == up) aboveTree.left = tree;
            else aboveTree.right = tree;
        }
        Tree temp = tree.right;
        tree.right = up;
        up.left = temp;
    }

    private void findLocation(Tree target, Tree tree) {
        if(tree == null) return;
        if((tree.left!=null && tree.left == target) || (tree.right!=null && tree.right == target)) {
            aboveTree = tree;
            return;
        }
        if(tree.val > target.val) findLocation(target, tree.left);
        else findLocation(target, tree.right);
    }

    private void deleteValue(int val) {
        nodeToDelete = null;
        fetchNodeToDelete(root, val);
        if(nodeToDelete == null) {
            System.out.println("Value "+val+" not found in tree");
            return;
        }
        if(nodeToDelete.color == BLACK && (nodeToDelete.left == null || nodeToDelete.left.color == BLACK) && (nodeToDelete.right == null || nodeToDelete.right.color == BLACK)) nodeToDelete.color = DOUBLE_BLACK;
        else {
            if (nodeToDelete.left == null && nodeToDelete.right == null) removeFromTree(nodeToDelete);
        }
        balanceForDeletion(null, root);
    }

    private void removeFromTree(Tree node) {
        if(node == root) {
            root = null;
            return;
        }
        findLocation(node, root);
        if(node == aboveTree.right) aboveTree.right = null;
        else aboveTree.left = null;
    }

    private void balanceForDeletion(Tree up, Tree tree) {

        if(tree == null) return;

        if(tree.color == DOUBLE_BLACK || tree == nodeToDelete) {

            //It is feasible to keep the deletion node at the bottom of the tree
            if(tree == nodeToDelete) {
                if(tree.color == RED && tree.left == null && tree.right == null) {
                    removeFromTree(tree);
                    return;
                }

                else if((tree.left != null && tree.left.color == RED)) {
                    Tree temp = tree.left;
                    Tree back = null;
                    while(temp!=null) {
                        back = temp;
                        temp = temp.right;
                    }
                    nodeToDelete = back;
                    tree.val = back.val;
                    if(tree.color == DOUBLE_BLACK) tree.color = BLACK;
                    if(nodeToDelete.color == BLACK && (nodeToDelete.left == null || nodeToDelete.left.color == BLACK) && (nodeToDelete.right == null || nodeToDelete.right.color == BLACK)) nodeToDelete.color = DOUBLE_BLACK;
                    balanceForDeletion(null, root);
                    return;
                }

                else if((tree.right != null && tree.right.color == RED)) {
                    Tree temp = tree.right;
                    Tree back = null;
                    while(temp!=null) {
                        back = temp;
                        temp = temp.left;
                    }
                    nodeToDelete = back;
                    tree.val = back.val;
                    if(tree.color == DOUBLE_BLACK) tree.color = BLACK;
                    if(nodeToDelete.color == BLACK && (nodeToDelete.left == null || nodeToDelete.left.color == BLACK) && (nodeToDelete.right == null || nodeToDelete.right.color == BLACK)) nodeToDelete.color = DOUBLE_BLACK;
                    balanceForDeletion(null, root);
                    return;
                }

                else if(tree.left!=null || tree.right!=null) {
                    Tree temp = findSuccessor(tree);
                    tree.val = temp.val;
                    nodeToDelete = temp;
                    if(tree.color == DOUBLE_BLACK) tree.color = BLACK;
                    if(nodeToDelete.color == BLACK && (nodeToDelete.left == null || nodeToDelete.left.color == BLACK) && (nodeToDelete.right == null || nodeToDelete.right.color == BLACK)) nodeToDelete.color = DOUBLE_BLACK;
                    balanceForDeletion(null, root);
                    return;
                }

                else if(tree == root) {
                    removeFromTree(tree);
                    return;
                }

            }

            //Case 1: Root is Double Black
            /**(Termination Case)*/
            if(tree == root) {
                    tree.color = BLACK;
                    System.out.println("Case 1");
                    return;
            }

            //Case 2: Black Parent, Red Right Sibling, Children of sibling: black, black
            else if(up.color == BLACK && up.left == tree &&
                    (up.right!=null && up.right.color == RED
                            && ((up.right.left == null || up.right.left.color == BLACK)
                            && ((up.right.right == null || up.right.right.color == BLACK))))) {
                System.out.println("Case 2");
                up.right.color = BLACK;
                up.color = RED;
                performLeftRotation(up,up.right);
                balanceForDeletion(null, root);
                return;
            }

            //Mirror Case
            else if(up.color == BLACK && up.right == tree &&
                    (up.left!=null && up.left.color == RED
                            && ((up.left.left == null || up.left.left.color == BLACK)
                            && ((up.left.right == null || up.left.right.color == BLACK))))) {
                System.out.println("Case 2 Mirror");
                up.left.color = BLACK;
                up.color = RED;
                performRightRotation(up,up.left);
                balanceForDeletion(null, root);
                return;
            }

            //Case 3: Black Parent, Black Right Sibling, Children of sibling: black, black
            else if(up.color == BLACK && up.left == tree &&
                    (up.right!=null
                            && ((up.right.left == null || up.right.left.color == BLACK)
                            && ((up.right.right == null || up.right.right.color == BLACK))))) {
                System.out.println("Case 3");
                up.right.color = RED;
                up.color = DOUBLE_BLACK;
                if(tree == nodeToDelete) removeFromTree(tree);
                else tree.color = BLACK;
                balanceForDeletion(null, root);
                return;
            }

            //Mirror Case
            else if(up.color == BLACK && up.right == tree &&
                    (up.left!=null
                            && ((up.left.left == null || up.left.left.color == BLACK)
                            && ((up.left.right == null || up.left.right.color == BLACK))))) {
                System.out.println("Case 3 Mirror");
                up.left.color = RED;
                up.color = DOUBLE_BLACK;
                if(tree == nodeToDelete) removeFromTree(tree);
                else tree.color = BLACK;
                balanceForDeletion(null, root);
                return;
            }

            //Case 4: Red Parent, Black Sibling, Black Children of Sibling
            /**(Termination Case)*/
            else if(up.color == RED && up.left == tree &&
                    (up.right!=null
                            && ((up.right.left == null || up.right.left.color == BLACK)
                            && ((up.right.right == null || up.right.right.color == BLACK))))) {
                System.out.println("Case 4");
                up.color = BLACK;
                up.right.color = RED;
                tree.color = BLACK;
                if(tree == nodeToDelete) removeFromTree(tree);
                return;
            }

            //Mirror Case
            else if(up.color == RED && up.right == tree &&
                    (up.left!=null
                            && ((up.left.left == null || up.left.left.color == BLACK)
                            && ((up.left.right == null || up.left.right.color == BLACK))))) {
                System.out.println("Case 4 Mirror");
                up.color = BLACK;
                up.left.color = RED;
                tree.color = BLACK;
                if(tree == nodeToDelete) removeFromTree(tree);
                return;
            }

            //Case 5: Black parent, Black Right Sibling, Red Left Child of Sibling
            else if(up.color == BLACK && up.left == tree &&
                    (up.right!=null
                            && ((up.right.left != null && up.right.left.color == RED)
                            && ((up.right.right == null || up.right.right.color == BLACK))))) {
                System.out.println("Case 5");
                up.right.color = RED;
                up.right.left.color = BLACK;
                performRightRotation(up.right,up.right.left);
                balanceForDeletion(null, root);
                return;
            }

            //Mirror Case
            else if(up.color == BLACK && up.right == tree &&
                    (up.left!=null
                            && ((up.left.left == null || up.left.left.color == BLACK)
                            && ((up.left.right != null && up.left.right.color == RED))))) {
                System.out.println("Case 5 Mirror");
                up.right.color = RED;
                up.right.left.color = BLACK;
                performRightRotation(up.right,up.right.left);
                balanceForDeletion(null, root);
                return;
            }

            //Case 6: Black Right Sibling and Red Right Child of Sibling
            /**(Termination Case)*/
            else if(up.left == tree && up.right!=null && (up.right.right!=null && up.right.right.color == RED)) {
                System.out.println("Case 6");
                up.right.right.color = BLACK;
                tree.color = BLACK;
                performLeftRotation(up,up.right);
                if(tree == nodeToDelete) removeFromTree(tree);
                return;
            }

            //Mirror Case
            else if(up.right == tree && up.left!=null && (up.left.left!=null && up.left.left.color == RED)) {
                System.out.println("Case 6 Mirror");
                tree.color = BLACK;
                up.left.left.color = BLACK;
                performRightRotation(up,up.left);
                if(tree == nodeToDelete) removeFromTree(tree);
                return;
            }
        }
        balanceForDeletion(tree, tree.left);
        balanceForDeletion(tree, tree.right);
    }

    private Tree findSuccessor(Tree tree) {
        Tree back = tree;
        Tree temp = tree;
        if(tree.right!=null) {
            temp = tree.right;
            while (temp!=null) {
                back = temp;
                temp = temp.left;
            }
        }
        else if(tree.left!=null) {
            temp = tree.left;
            while (temp!=null) {
                back = temp;
                temp = temp.right;
            }
        }
        return back;
    }

    private void fetchNodeToDelete(Tree tree, int val) {
        if(tree == null) return;
        if(val == 5) System.out.println(tree.val);
        if(tree.val == val) {
            nodeToDelete = tree;
            return;
        }
        if(tree.val > val) fetchNodeToDelete(tree.left, val);
        else fetchNodeToDelete(tree.right, val);
    }

    public Tree getRoot() { return root; }

    class Tree {
        Tree left;
        Tree right;
        int val;
        int color;
    }
}