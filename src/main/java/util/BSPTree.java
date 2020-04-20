package util;

import geometry.Triangle;

import java.util.List;

public class BSPTree {
    public void add(BoundedCube object) {
        root.add(object);
    }

    public static class Node {
        public Node behind;
        public Node front;
        public Plane partition;
        public BoundedCube bounds;

        public void add(Plane object) {
            if (partition == null && bounds == null) {
                partition = object;
            } else {
                if (object.location.isBehind(partition)) {
                    if (behind == null) {
                        behind = new Node();
                        behind.partition = object;
                    } else {
                        behind.add(object);
                    }
                } else if (object.location.isInFront(partition)) {
                    if (front == null) {
                        front = new Node();
                        front.partition = object;
                    } else {
                        front.add(object);
                    }
                } else {
                    throw new IllegalStateException("Should be behind or in front");
                }
            }

        }

        public void add(BoundedCube object) {
            if (partition == null && bounds == null) {
                bounds = object;
            } else {
                if (object.isBehind(partition)) {
                    if (behind == null) {
                        behind = new Node();
                        behind.bounds = object;
                    } else {
                        behind.add(object);
                    }
                } else if (object.isInFront(partition)) {
                    if (front == null) {
                        front = new Node();
                        front.bounds = object;
                    } else {
                        front.add(object);
                    }
                } else {
                    throw new IllegalStateException("Should be behind or in front");
                }
            }

        }
    }


    private Node root;

    public BSPTree(Node root) {
        this.root = root;
    }

    /**
     * Gets the root node of this tree.
     */
    public Node getRoot() {
        return root;
    }

    public void add(Plane plane) {
        root.add(plane);

    }

}
