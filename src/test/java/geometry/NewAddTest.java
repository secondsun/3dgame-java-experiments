package dev.secondsun.geometry;

import dev.secondsun.geometry.playfield.DormRoom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import dev.secondsun.util.BSPTree;
import dev.secondsun.util.BoundedCube;

public class NewAddTest {



    @Test
    public void testNewBspTreeAdd() {
        var tree = new BSPTree();

        var dorm = new DormRoom();
        var floor = dorm.floorModel();

        tree.add(new BoundedCube(floor));

        Assertions.assertEquals(floor, tree.getRoot().bounds.model);
    }

    @Test
    public void testNewBspTreeAddMultiple() {
        var tree = new BSPTree();

        var dorm = new DormRoom();
        var floor = dorm.floorModel();
        var bed = dorm.bedModel();
        var bookshelf = dorm.bookshelfModel();

        tree.add(new BoundedCube(floor));
        tree.add(new BoundedCube(bookshelf));
        tree.add(new BoundedCube(bed));


        Assertions.assertNotNull(tree.getRoot().partition);
        Assertions.assertNull(tree.getRoot().bounds);
        Assertions.assertEquals(bed, tree.getRoot().front.front.bounds.model);
        Assertions.assertEquals(floor, tree.getRoot().behind.bounds.model);

    }

}
