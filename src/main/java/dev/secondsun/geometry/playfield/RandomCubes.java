package dev.secondsun.geometry.playfield;

import dev.secondsun.geometry.Model;
import dev.secondsun.geometry.Triangle;
import dev.secondsun.geometry.Vertex;
import dev.secondsun.util.CubeBuilder;
import dev.secondsun.util.OctTree;
import dev.secondsun.util.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomCubes implements Model {

    float absRange = 48f;
    float cubesize = 4f;

    final ArrayList<Triangle> triangles = new ArrayList<>();
        final Random random;
        public final OctTree tree = new OctTree(new Range(-absRange,absRange),new Range(-absRange,absRange),new Range(-absRange,absRange));

    public RandomCubes(int count, int i) {
        random = new Random(i);
        IntStream.range(0,count).forEach( in -> triangles.addAll(newCube()));
        triangles.forEach(tri ->tree.insert(tri.v1, tri));
        triangles.forEach(tri ->tree.insert(tri.v2, tri));
        triangles.forEach(tri ->tree.insert(tri.v3, tri));
    }

    private List<Triangle> newCube() {
        CubeBuilder builder = new CubeBuilder(new Vertex(random.nextFloat(absRange*2f)-absRange,random.nextFloat(absRange*2f)-absRange,random.nextFloat(absRange*2f)-absRange), cubesize,cubesize,cubesize);
        builder.setColor(random.nextInt(255)<<16|random.nextInt(255)<<8|random.nextInt(255));
        return builder.cube().getTriangles();
    }

    @Override
    public List<Triangle> getTriangles() {
        return triangles;
    }
}
