package dev.secondsun.geometry.playfield;

import dev.secondsun.geometry.Model;
import dev.secondsun.geometry.Triangle;
import dev.secondsun.geometry.Vertex;
import dev.secondsun.util.CubeBuilder;
import dev.secondsun.util.OctTree;
import dev.secondsun.util.PyramidBuilder;
import dev.secondsun.util.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomPyramids implements Model {

    public static float absRange = 18f;
    public static float cubesize = 8f;

    List<Triangle> triangles = new ArrayList<>();
    Random random;
    public OctTree tree = new OctTree(absRange, Vertex.ZERO);

    public RandomPyramids(int count, int seed) {
        random = new Random(seed);
        IntStream.range(0,count).forEach( in -> triangles.addAll(newPyramid()));
        triangles.forEach(tri ->tree.insert(tri.v1, tri));
        triangles.forEach(tri ->tree.insert(tri.v2, tri));
        triangles.forEach(tri ->tree.insert(tri.v3, tri));
    }

    private List<Triangle> newPyramid() {
        PyramidBuilder builder = new PyramidBuilder(new Vertex(random.nextFloat(absRange*2f)-absRange,random.nextFloat(absRange*2f)-absRange,random.nextFloat(absRange*2f)-absRange), cubesize,cubesize,cubesize);
        builder.setColor(random.nextInt(255)<<16|random.nextInt(255)<<8|random.nextInt(255));
        return builder.build().getTriangles();
    }

    @Override
    public List<Triangle> getTriangles() {
        return triangles;
    }


}
