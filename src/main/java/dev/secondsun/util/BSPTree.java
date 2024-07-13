package dev.secondsun.util;

import dev.secondsun.geometry.Camera;import dev.secondsun.geometry.Triangle;
import dev.secondsun.geometry.Vertex;

import java.util.ArrayList;
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
     * @param camera
     * @return
     */
    public List<Triangle> order(Camera camera) {
        var toReturn = new ArrayList<Triangle>();

        List<BoundedCube> modelsInOrder = this.getModelsInOrderFrom(getRoot(),camera);

        toReturn.addAll(modelsInOrder.stream().flatMap((boundedCube -> {return boundedCube.model.getTriangles().stream();})).collect(Collectors.toList()));

        return toReturn;
    }

    public List<BoundedCube> getModelsInOrderFrom(Node node, Camera camera) {
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
                Plane foundPartition = findPartition (behindObject, object);
                this.bounds = null;
                this.partition = foundPartition;
                behind = new Node();
                behind.bounds = behindObject;
                front = new Node();
                front.bounds = object;
            } else {//by definition we add to the front.
                front.add(object);
            }

        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((behind == null) ? 0 : behind.hashCode());
            result = prime * result + ((bounds == null) ? 0 : bounds.hashCode());
            result = prime * result + ((front == null) ? 0 : front.hashCode());
            result = prime * result + ((partition == null) ? 0 : partition.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node other = (Node) obj;
            if (behind == null) {
                if (other.behind != null)
                    return false;
            } else if (!behind.equals(other.behind))
                return false;
            if (bounds == null) {
                if (other.bounds != null)
                    return false;
            } else if (!bounds.equals(other.bounds))
                return false;
            if (front == null) {
                if (other.front != null)
                    return false;
            } else if (!front.equals(other.front))
                return false;
            if (partition == null) {
                if (other.partition != null)
                    return false;
            } else if (!partition.equals(other.partition))
                return false;
            return true;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((root == null) ? 0 : root.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BSPTree other = (BSPTree) obj;
        if (root == null) {
            if (other.root != null)
                return false;
        } else if (!root.equals(other.root))
            return false;
        return true;
    }


    


}
