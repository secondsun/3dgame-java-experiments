package util;

import geometry.Triangle;

import java.util.List;

public class BSPTree {
    public static class Node {
        public Node front;
        public Node back;
        public Plane partition;
        public List<BoundedCube> objects;
    }
    public static class Leaf extends Node {
        public float floorHeight;
        public float ceilHeight;
        public BoundedCube bounds;
        public boolean isBack;
    }
    private Node root;

    public BSPTree(Node root) {
        this.root = root;
    }
    /**
     Gets the root node of this tree.
     */
    public Node getRoot() {
        return root;
    }

}
