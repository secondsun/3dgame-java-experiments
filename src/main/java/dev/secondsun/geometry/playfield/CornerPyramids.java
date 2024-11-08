package dev.secondsun.geometry.playfield;

import dev.secondsun.geometry.Model;
import dev.secondsun.geometry.Triangle;
import dev.secondsun.geometry.Vertex;
import dev.secondsun.util.OctTree;
import dev.secondsun.util.PyramidBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class CornerPyramids implements Model {

    public static float absRange = 8f;
    public static float cubesize = 8f;

    List<Triangle> triangles = new ArrayList<>();
    Random random;
    public OctTree tree = new OctTree(absRange*3, Vertex.ZERO);

    public CornerPyramids(int seed) {
        random = new Random(seed);
        IntStream.range(0,8).forEach( in -> triangles.addAll(newPyramid(in)));
        triangles.forEach(tri ->tree.insert(tri.v1, tri));
        triangles.forEach(tri ->tree.insert(tri.v2, tri));
        triangles.forEach(tri ->tree.insert(tri.v3, tri));
    }

    public CornerPyramids(int count, int seed) {
        random = new Random(seed);
        IntStream.range(0,count).forEach( in -> triangles.addAll(newPyramid(in)));
        triangles.forEach(tri ->tree.insert(tri.v1, tri));
        triangles.forEach(tri ->tree.insert(tri.v2, tri));
        triangles.forEach(tri ->tree.insert(tri.v3, tri));
    }


    private List<Triangle> newPyramid(int position) {
        float scale = (position>>3)+1;
        float x = (((position & 1)==0) ? 1:-1) * cubesize * scale;
        position=position>>1;
        float y = (((position & 1)==0) ? 1:-1) * cubesize* scale;
        position=position>>1;
        float z = (((position & 1)==0) ? 1:-1) * cubesize* scale;

        PyramidBuilder builder = new PyramidBuilder(new Vertex(x,y,z), cubesize,cubesize,cubesize);
        builder.setColor(random.nextInt(255)<<16|random.nextInt(255)<<8|random.nextInt(255));
        return builder.build().getTriangles();
    }

    @Override
    public List<Triangle> getTriangles() {
        return triangles;
    }


}
