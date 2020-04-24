package util;

import geometry.Camera;
import geometry.Model;
import geometry.Triangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BSPTree {
    public void add(BoundedCube object) {
        root.add(object);
    }

    /**
     * Returns a list of tiles that are ordered front to back in draw order.
     * @param tiles
     * @param camera
     * @return
     */
    public List<Triangle> order(List<Triangle> tiles, Camera camera) {
        var toReturn = new ArrayList<Triangle>(tiles.size());

        List<BoundedCube> modelsInOrder = this.getModelsInOrderFrom(getRoot(),camera);

        toReturn.addAll(modelsInOrder.stream().flatMap((boundedCube -> {return boundedCube.model.getTriangles().stream();})).collect(Collectors.toList()));

        return toReturn;
    }

    private List<BoundedCube> getModelsInOrderFrom(Node node, Camera camera) {
        ArrayList<BoundedCube> cubes = new ArrayList<>();

        if (node.bounds != null) {
            cubes.add(node.bounds);
        } else {
            if (camera.getFrom().isBehind(node.partition)) {
                cubes.addAll(getModelsInOrderFrom(node.behind, camera));
                cubes.addAll(getModelsInOrderFrom(node.front, camera));
            } else {
                cubes.addAll(getModelsInOrderFrom(node.front, camera));
                cubes.addAll(getModelsInOrderFrom(node.behind, camera));
            }
        }

        return cubes;
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
