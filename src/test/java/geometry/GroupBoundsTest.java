package geometry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.BSPTree;
import util.BoundedCube;
import util.Plane;

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


        assertEquals(0,boundedGroup.far);
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

        assertEquals(0,ground.far);
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
        Plane plan = new Plane(new Vertex(0,0,4), new Vertex(0,0,1));
        BSPTree tree = new BSPTree();
    }

}
