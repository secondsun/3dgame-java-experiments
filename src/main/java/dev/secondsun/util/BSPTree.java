package dev.secondsun.util;

import dev.secondsun.geometry.Camera;
import dev.secondsun.geometry.Model;
import dev.secondsun.geometry.Triangle;
import dev.secondsun.geometry.Vertex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BSPTree {

    public BSPTree() {
    }

    public void add(BoundedCube object) {
        if (root == null) {
            root = new Node(object);
        } else {
            root.add(object);
        }
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

        public  Node(BoundedCube object) {
            this.add(object);
        }

        public Node() {
        }

        public void add(BoundedCube object) {
            if (partition == null && bounds == null) {
                bounds = object;
            } else if (partition == null){ //turn into two nodes and create a partition
                var behindObject = this.bounds;//As a rule we add objects from front to back
                Plane partition = findPartition (behindObject, object);
                this.bounds = null;
                this.partition = partition;
                behind = new Node();
                behind.bounds = behindObject;
                front = new Node();
                front.bounds = object;
            } else {//by definition we add to the front.
                front.add(object);
            }

        }

        private Plane findPartition(BoundedCube behindObject, BoundedCube frontObject) {
            var nearTestPlane = new Plane(new Vertex(0,0,behindObject.near), new Vertex(0,0,1));
            var farTestPlane = new Plane(new Vertex(0,0,behindObject.far), new Vertex(0,0,-1));
            var leftTestPlane = new Plane(new Vertex(behindObject.left,0,0), new Vertex(-1,0,0));
            var rightTestPlane = new Plane(new Vertex(behindObject.right,0,0), new Vertex(1,0,0));
            var topTestPlane = new Plane(new Vertex(0,behindObject.top,0), new Vertex(0,1,0));
            var bottomTestPlane = new Plane(new Vertex(0,behindObject.bottom,0), new Vertex(0,-1,0));

            if (frontObject.isInFront(nearTestPlane)) {
                return nearTestPlane;
            }
            if (frontObject.isInFront(farTestPlane)) {
                return farTestPlane;
            }
            if (frontObject.isInFront(topTestPlane)) {
                return topTestPlane;
            }
            if (frontObject.isInFront(bottomTestPlane)) {
                return bottomTestPlane;
            }
            if (frontObject.isInFront(leftTestPlane)) {
                return leftTestPlane;
            }
            if (frontObject.isInFront(rightTestPlane)) {
                return rightTestPlane;
            }
            throw new IllegalStateException("can't place object" + behindObject.toString() + "\n" + frontObject.toString());
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


}
