package geometry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.BSPTree;
import util.BoundedCube;
import util.Plane;

import static geometry.MonasteryPlayfield.TILE_LENGTH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupBoundsTest {

    @Test
    @DisplayName("Create a basic cube around a Model")
    public void createsBoundedCube() {
        BoundedCube boundedGroup = new BoundedCube(new MonasteryPlayfield());


        assertEquals(0,boundedGroup.left);
        assertEquals(256,boundedGroup.right);

        assertEquals(0,boundedGroup.bottom);
        assertEquals(256,boundedGroup.top);


        assertEquals(-1,boundedGroup.far);
        assertEquals(96,boundedGroup.near);

    }

    @Test
    @DisplayName("The Monastery should have the parts in cubes")
    public void createsBoundedMonastery() {
        var playfield = new MonasteryPlayfield();
        var  ground = new BoundedCube(playfield.field());
        var  building = new BoundedCube(playfield.castle());
        var  roof = new BoundedCube(playfield.roof());
        var  path = new BoundedCube(playfield.path());

        assertEquals(-1,ground.far);//there's a correction texture, see the monastray class
        assertEquals(0,ground.near);

        assertEquals(0,building.far);
        assertEquals(48,building.near);
        assertEquals(96,building.left);
        assertEquals(192,building.right);
        assertEquals(224,building.top);
        assertEquals(144,building.bottom);


        assertEquals(48,roof.far);
        assertEquals(96,roof.near);

        assertNotNull(path);

    }

    @Test
    @DisplayName("Test BSP tree")
    public void bspTreeTests() {

        var playfield = new MonasteryPlayfield();
        var  roof = new BoundedCube(playfield.roof());
        var  path = new BoundedCube(playfield.path());

        Plane plane = new Plane(new Vertex(0,0,4), new Vertex(0,0,1));//this is the plane that splits the castle and roof facing up

        BSPTree tree = new BSPTree(new BSPTree.Node());
        tree.add(plane);
        tree.add(roof);
        tree.add(path);


        assertEquals(tree.getRoot().partition, plane);
        assertEquals(tree.getRoot().behind.bounds, path);
        assertEquals(tree.getRoot().front.bounds, roof);
    }
    @Test
    @DisplayName("Test BSP tree again")
    public void bspTreeTests2() {

        var playfield = new MonasteryPlayfield();
        var  ground = new BoundedCube(playfield.field());
        var  building = new BoundedCube(playfield.castle());
        var  roof = new BoundedCube(playfield.roof());
        var  path = new BoundedCube(playfield.path());

        Plane roofPlane = new Plane(new Vertex(0,9*TILE_LENGTH,3*TILE_LENGTH), new Vertex(0,0,1));//this is the plane that splits the castle and roof facing up
        Plane groundPlane = new Plane(new Vertex(0,0,0), new Vertex(0,0,1));//this is the plane that splits the castle and roof facing up
        Plane castlePlane = new Plane(new Vertex(0,9*TILE_LENGTH,3*TILE_LENGTH), new Vertex(0,-1,0));//this is the plane that splits the castle and roof facing up
        BSPTree tree = new BSPTree(new BSPTree.Node());
        tree.add(groundPlane);
        tree.add(roofPlane);
        tree.add(castlePlane);
        tree.add(roof);
        tree.add(path);
        tree.add(ground);
        tree.add(building);


        assertEquals(tree.getRoot().partition, groundPlane);
        assertEquals(tree.getRoot().behind.bounds, ground);
        assertEquals(tree.getRoot().front.bounds, null);
        assertEquals(tree.getRoot().front.partition, roofPlane);
        assertEquals(tree.getRoot().front.front.bounds, roof);
        assertEquals(tree.getRoot().front.behind.partition, castlePlane);
        assertEquals(tree.getRoot().front.behind.bounds, null);
        assertEquals(tree.getRoot().front.behind.behind.partition, null);
        assertEquals(tree.getRoot().front.behind.behind.bounds, building);
        assertEquals(tree.getRoot().front.behind.front.bounds, path);
    }

}

