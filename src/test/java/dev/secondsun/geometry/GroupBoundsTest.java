package dev.secondsun.geometry;

import dev.secondsun.geometry.playfield.MonasteryPlayfield;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dev.secondsun.util.BSPTree;
import dev.secondsun.util.BoundedCube;
import dev.secondsun.util.Resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupBoundsTest {

    @Test
    @DisplayName("Create a basic cube around a Model")
    public void createsBoundedCube() {
        var resources = new Resources();

        BoundedCube boundedGroup = new BoundedCube(new MonasteryPlayfield(resources));


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
        var resources = new Resources();
        var playfield = new MonasteryPlayfield(resources);
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
        var resources = new Resources();
        var playfield = new MonasteryPlayfield(resources);
        var  roof = new BoundedCube(playfield.roof());
        var  path = new BoundedCube(playfield.path());

        //Plane plane = new Plane(new Vertex(0,0,4), new Vertex(0,0,1));//this is the plane that splits the castle and roof facing up

        BSPTree tree = new BSPTree(new BSPTree.Node());

        tree.add(path);
        tree.add(roof);


        //assertEquals(tree.getRoot().partition, plane);
        assertEquals(tree.getRoot().behind.bounds, path);
        assertEquals(tree.getRoot().front.bounds, roof);
    }
    @Test
    @DisplayName("Test BSP tree again")
    public void bspTreeTests2() {
        var resources = new Resources();
        var playfield = new MonasteryPlayfield(resources);
        var  ground = new BoundedCube(playfield.field());
        var  building = new BoundedCube(playfield.castle());
        var  roof = new BoundedCube(playfield.roof());
        var  path = new BoundedCube(playfield.path());

//        Plane roofPlane = new Plane(new Vertex(0,9*TILE_LENGTH,3*TILE_LENGTH), new Vertex(0,0,1));//this is the plane that splits the castle and roof facing up
//        Plane groundPlane = new Plane(new Vertex(0,0,0), new Vertex(0,0,1));//this is the plane that splits the castle and roof facing up
//        Plane castlePlane = new Plane(new Vertex(0,9*TILE_LENGTH,3*TILE_LENGTH), new Vertex(0,-1,0));//this is the plane that splits the castle and roof facing up
        BSPTree tree = new BSPTree(new BSPTree.Node());
//        tree.add(groundPlane);
//        tree.add(roofPlane);
//        tree.add(castlePlane);

        tree.add(ground);
        tree.add(path);
        tree.add(building);
        tree.add(roof);

        assertEquals(tree.getRoot().behind.bounds, ground);
        assertEquals(null, tree.getRoot().front.bounds);
        assertEquals(null, tree.getRoot().front.front.bounds);
        assertEquals(tree.getRoot().front.front.front.bounds, roof);
        assertEquals(tree.getRoot().front.behind.bounds, path);
        assertEquals(null, tree.getRoot().front.front.behind.partition);
        assertEquals(tree.getRoot().front.front.behind.bounds, building);

    }

}

