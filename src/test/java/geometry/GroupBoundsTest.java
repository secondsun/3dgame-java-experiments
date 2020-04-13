package geometry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.BoundedCube;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupBoundsTest {

    @Test
    @DisplayName("Create a basic cube around a Model")
    public void createsBoundedCube() {
        BoundedCube boundedGroup = new BoundedCube(new MonastaryPlayfield());


        assertEquals(0,boundedGroup.left);
        assertEquals(256,boundedGroup.right);

        assertEquals(0,boundedGroup.bottom);
        assertEquals(256,boundedGroup.top);


        assertEquals(0,boundedGroup.far);
        assertEquals(96,boundedGroup.near);

    }

}
