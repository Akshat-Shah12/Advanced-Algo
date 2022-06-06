

import java.util.ArrayList;
import java.util.Scanner;

public class BinomialHeap {

    private HeapNode root = null;

    static class Tree{
        int val;
        Tree[] children = null;
    }

    static class HeapNode{
        Tree tree;
        HeapNode next;
    }

    public BinomialHeap() {
        Scanner sc = new Scanner(System.in);
        int val = 0;
        int choice = 0;
        do {
            System.out.println("""
                    1: Insert into Heap
                    2: Merge with another Heap
                    3: Show Heap Elements
                    4: Change a node value
                    5: Find Minimum value in Heap
                    6: Extract minimum value in heap
                    7: Find a value in heap
                    8: Delete a value from Heap
                    9: Exit
                    """);
            switch ((choice = sc.nextInt())) {
                case 1: {
                    System.out.print("Elements to insert (-1 to terminate) = ");
                    while(true) {
                        val = sc.nextInt();
                        if(val == -1) break;
                        root = insertVal(root, val);
                        root = balanceHeap(root);
                    }
                    System.out.println("Inserted successfully\n");
                    break;
                }
                case 2: {
                    System.out.println("Enter elements of heap to be merged (-1 to terminate insertion)");
                    HeapNode node = null;
                    while(true) {
                        val = sc.nextInt();
                        if(val == -1) break;
                        node = insertVal(node, val);
                        node = balanceHeap(node);
                    }
                    root = insertNode(root , node);
                    root = balanceHeap(root);
                    System.out.println("Merged heaps successfully\n");
                    break;
                }
                case 3: {
                    System.out.println("Heap Elements are :-");
                    printHeap(root);
                    break;
                }
                case 4: {
                    System.out.print("Old key = ");
                    int oldKey = sc.nextInt();
                    System.out.print("New key = ");
                    int newKey = sc.nextInt();
                    changeKey(root, oldKey, newKey);
                    break;
                }
                case 5: {
                    HeapNode node = getMinimumNode(root);
                    System.out.println(node.tree.val);
                    break;
                }
                case 6:{
                    root = deleteVal(root, getMinimumValue(root, Integer.MAX_VALUE));
                    break;
                }
                case 7:{
                    System.out.print("value = ");
                    val = sc.nextInt();
                    if(findValueInHeap(root, val)) System.out.println("Found");
                    else System.out.println("Not Found");
                    break;
                }
                case 8:{
                    System.out.print("value = ");
                    val = sc.nextInt();
                    root = deleteVal(root, val);
                    break;
                }
                case 9: break;
            }
        }while(choice!=9);
    }

    private boolean isHeapBalanced(HeapNode node) {
        if(node.next == null) return true;
        if(getHeight(node.tree) == getHeight(node.next.tree)) return false;
        return isHeapBalanced(node.next);
    }

    private boolean isHeapSorted(HeapNode node) {
        if(node.next == null) return true;
        if(getHeight(node.next.tree)<getHeight(node.tree)) return false;
        return isHeapSorted(node.next);
    }

    private HeapNode checkAndSwap(HeapNode root, HeapNode node) {
        if(node==null || node.next == null) return root;
        if(getHeight(node.tree)>getHeight(node.next.tree)) {
            root = swap(root, node, node.next);
        }
        return checkAndSwap(root, node.next);
    }

    private HeapNode swap(HeapNode root, HeapNode node1, HeapNode node2) {
        HeapNode left1 = findLeftConnectedNode(root,node1);
        if(node1 == root) {
            root = node2;
        }
        else {
            left1.next = node2;
        }
        HeapNode temp = node2.next;
        node2.next = node1;
        node1.next = temp;
        return root;
    }

    private HeapNode sortHeap(HeapNode root) {
        root = checkAndSwap(root,root);
        if(!isHeapSorted(root)) return sortHeap(root);
        return root;
    }

    private HeapNode balanceHeap(HeapNode root) {
        return checkAndMerge(root,root);
    }

    private void printHeap(HeapNode node) {
        if(node == null) return;
        printTree(node.tree);
        System.out.println();
        printHeap(node.next);
    }

    private void printTree(Tree tree) {
        System.out.print(tree.val+ " ");
        if(tree.children!=null) {
            for(Tree t: tree.children) printTree(t);
        }
    }

    private HeapNode checkAndMerge(HeapNode root, HeapNode node) {
        if(node == null || node.next == null) return root;
        if(isHeapBalanced(root)) return root;
        if(getHeight(node.tree)==getHeight(node.next.tree) && (node.next.next == null || getHeight(node.next.next.tree)!=getHeight(node.tree))) {
            root = mergeSameHeightRoots(root, node, node.next);
            return checkAndMerge(root,root);
        }
        return checkAndMerge(root, node.next);
    }

    private HeapNode mergeSameHeightRoots(HeapNode root, HeapNode node1, HeapNode node2) {
        if(node1.tree.val>node2.tree.val) {
            Tree tree = node2.tree;
            if(tree.children == null) {
                tree.children = new Tree[]{node1.tree};
            }
            else {
                Tree[] trees = new Tree[tree.children.length+1];
                System.arraycopy(node2.tree.children,0,trees,0,node2.tree.children.length);
                trees[node2.tree.children.length] = node1.tree;
                node2.tree.children = trees;
            }
            root = deleteNode(root, node1);
        }
        else {
            Tree tree = node1.tree;
            if(tree.children == null) {
                tree.children = new Tree[]{node2.tree};
            }
            else {
                Tree[] trees = new Tree[tree.children.length+1];
                System.arraycopy(node1.tree.children,0,trees,0,node1.tree.children.length);
                trees[node1.tree.children.length] = node2.tree;
                node1.tree.children = trees;
            }
            root = deleteNode(root, node2);
        }
        return root;
    }

    private HeapNode deleteNode(HeapNode root, HeapNode node) {
        HeapNode left = findLeftConnectedNode(root, node);
        if(left != null) {
            left.next = node.next;
        }
        else {
            root = node.next;
        }
        return root;
    }

    private HeapNode findLeftConnectedNode(HeapNode root, HeapNode node) {
        if(node == root) return null;
        if(root.next == node) return root;
        return findLeftConnectedNode(root.next, node);
    }

    private HeapNode insertNode(HeapNode root, HeapNode node) {
        HeapNode temp = root;
        HeapNode back = null;
        while (temp!=null) {
            back = temp;
            temp = temp.next;
        }
        if(root == null) {
            root = node;
            return root;
        }
        back.next = node;
        root = sortHeap(root);
        return root;
    }

    private HeapNode insertVal(HeapNode root, int val) {
        HeapNode node = new HeapNode();
        Tree tree = new Tree();
        tree.val = val;
        node.tree = tree;
        return insertNode(root, node);
    }

    private void findDepth(Tree tree, int currentDepth, ArrayList<Integer> arrayList) {
        if(tree.children == null || tree.children.length == 0) {
            if(!arrayList.contains(currentDepth)) arrayList.add(currentDepth);
            return;
        }
        for(Tree t: tree.children) findDepth(t,currentDepth+1,arrayList);
    }

    private HeapNode changeKey(HeapNode root, int oldKey, int newKey) {
        HeapNode node = findTreeInHeap(root, oldKey);
        if(node == null) {
            System.out.println("Value "+oldKey+" not found in heap");
            return root;
        }
        Tree tree = node.tree;
        ArrayList<Tree> arrayList = new ArrayList<>();
        findChildTree(tree, oldKey, arrayList);
        Tree temp = arrayList.get(0);
        temp.val = newKey;
        while (!isTreeBalanced(tree)) balanceTree(tree);
        return root;
    }

    private void balanceTree(Tree tree) {
        if(tree.children == null) return;
        int temp = Integer.MAX_VALUE;
        for(Tree t: tree.children) {
            temp = Integer.min(temp, t.val);
        }
        if(tree.val > temp) {
            for(Tree t: tree.children) {
                if(t.val == temp) {
                    t.val = tree.val;
                    tree.val = temp;
                    break;
                }
            }
        }
        for(Tree t: tree.children) balanceTree(t);
    }

    private boolean isTreeBalanced(Tree tree) {
        boolean b = true;
        if(tree.children == null) return b;
        for(Tree t: tree.children) b = b && (tree.val<=t.val);
        for(Tree t: tree.children) b = b && isTreeBalanced(t);
        return b;
    }

    private void findChildTree(Tree tree, int key, ArrayList<Tree> arrayList) {
        if(tree.val == key) arrayList.add(tree);
        if(tree.children == null) return;
        for(Tree t: tree.children) findChildTree(t, key, arrayList);
    }

    private HeapNode findTreeInHeap(HeapNode node, int key) {
        if(node == null) return null;
        ArrayList<Integer> arrayList = new ArrayList<>();
        traverseTree(node.tree, arrayList);
        if(arrayList.contains(key)) return node;
        return findTreeInHeap(node.next, key);
    }

    private void traverseTree(Tree tree, ArrayList<Integer> arrayList) {
        if(!arrayList.contains(tree.val)) arrayList.add(tree.val);
        if(tree.children == null) return;
        for(Tree t: tree.children) traverseTree(t, arrayList);
    }

    private HeapNode getMinimumNode(HeapNode root) {
        return findTreeWithKey(root, getMinimumValue(root, Integer.MAX_VALUE));
    }

    private int getMinimumValue(HeapNode node, int val) {
        if(node == null) return val;
        return getMinimumValue(node.next, Integer.min(val, node.tree.val));
    }

    private HeapNode findTreeWithKey(HeapNode root, int key) {
        if(root == null || root.tree.val == key) return root;
        return findTreeWithKey(root.next, key);
    }

    private int getHeight(Tree tree) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        findDepth(tree,0,arrayList);
        return arrayList.get(arrayList.size()-1);
    }

    private boolean findValueInHeap(HeapNode root, int val) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        traverseHeap(root, arrayList);
        return arrayList.contains(val);
    }

    private void traverseHeap(HeapNode root, ArrayList<Integer> arrayList) {
        if(root == null) return;
        traverseTree(root.tree, arrayList);
        traverseHeap(root.next, arrayList);
    }

    private HeapNode deleteVal(HeapNode root, int val) {
        if(!findValueInHeap(root, val)) {
            System.out.println("Not Found");
            return root;
        }
        root = changeKey(root, val, Integer.MIN_VALUE);
        HeapNode node = getMinimumNode(root);
        root = deleteNode(root, node);
        if(node.tree.children!=null) {
            for(Tree t: node.tree.children) {
                HeapNode nodeToAdd = new HeapNode();
                nodeToAdd.tree = t;
                root = insertNode(root, nodeToAdd);
                root = balanceHeap(root);
            }
        }
        return root;
    }
}