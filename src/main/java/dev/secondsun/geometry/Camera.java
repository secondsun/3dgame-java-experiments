package dev.secondsun.geometry;

import dev.secondsun.util.Maths;
import org.ejml.simple.SimpleMatrix;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Camera {
    private Vertex from, to, up;

    public Camera(Vertex from, Vertex to, Vertex up) {
        this.from = from;
        this.to = to;
        this.up = up;
    }

    public Vertex getFrom() {
        return from;
    }

    public void setFrom(Vertex from) {
        this.from = from;
    }

    public Vertex getUp() {
        return up;
    }

    public void setUp(Vertex up) {
        this.up = up;
    }

    public Vertex getTo() {
        return to;
    }

    public void setTo(Vertex to) {
        this.to = to;
    }

    public float[][] lookAt() {
        float[][] camToWorld = new float[4][4];

        Vertex forward = Maths.normalize(Maths.subtract(from, to));
        Vertex left = Maths.normalize(up.cross(forward));
        Vertex up = forward.cross(left);

        camToWorld[0][0] = left.x;
        camToWorld[0][1] = left.y;
        camToWorld[0][2] = left.z;
        camToWorld[1][0] = up.x;
        camToWorld[1][1] = up.y;
        camToWorld[1][2] = up.z;
        camToWorld[2][0] = forward.x;
        camToWorld[2][1] = forward.y;
        camToWorld[2][2] = forward.z;

        var eye = from;

        camToWorld[0][3] = -left.x * eye.x - left.y * eye.y - left.z * eye.z;
        camToWorld[1][3] = -up.x * eye.x - up.y * eye.y - up.z * eye.z;
        camToWorld[2][3] = -forward.x * eye.x - forward.y * eye.y - forward.z * eye.z;
        camToWorld[3][3] = 1;

        return camToWorld;
    }

    public float[][] frustum(double fov) {
        var frustumCorners = new ArrayList<Vertex>();
        float [][] toReturn = new float[7][4];
        var farv = 256f;
        var lookAt = lookAt();

        frustumCorners.add(new Vertex((float) Math.tan(fov/2),(float) Math.tan(-fov/2),1f));//p1.z
        frustumCorners.add(new Vertex((float) Math.tan(-fov/2),(float) Math.tan(-fov/2),1f));//p2.z
        frustumCorners.add(new Vertex((float) Math.tan(fov/2)* farv,(float) Math.tan(-fov/2) * farv,farv));//p3.z
        frustumCorners.add(new Vertex((float) Math.tan(-fov/2)* farv,(float) Math.tan(-fov/2) * farv,farv));//p4.z
        frustumCorners.add(new Vertex((float) Math.tan(fov/2),(float) Math.tan(fov/2),1f));//p5.z
        frustumCorners.add(new Vertex((float) Math.tan(-fov/2),(float) Math.tan(fov/2),1f));//p6.z
        frustumCorners.add(new Vertex((float) Math.tan(fov/2)* farv,(float) Math.tan(fov/2) * farv,farv));//p7.z
        frustumCorners.add(new Vertex((float) Math.tan(-fov/2)* farv,(float) Math.tan(fov/2) * farv,farv));//p8.z

        var lookatUndo = new SimpleMatrix(lookAt).invert().toArray2();

        var lookAtUndoMatrix = new float[][] {
                {(float)lookatUndo[0][0],(float)lookatUndo[0][1],(float)lookatUndo[0][2],(float)lookatUndo[0][3]},
                {(float)lookatUndo[1][0],(float)lookatUndo[1][1],(float)lookatUndo[1][2],(float)lookatUndo[1][3]},
                {(float)lookatUndo[2][0],(float)lookatUndo[2][1],(float)lookatUndo[2][2],(float)lookatUndo[2][3]},
                {(float)lookatUndo[3][0],(float)lookatUndo[3][1],(float)lookatUndo[3][2],(float)lookatUndo[3][3]},
        };

        frustumCorners.forEach(vertext -> vertext.transform(lookAtUndoMatrix));

        Triangle top = new Triangle(frustumCorners.get(0),frustumCorners.get(1),frustumCorners.get(3));
        Triangle right = new Triangle(frustumCorners.get(3),frustumCorners.get(1),frustumCorners.get(5));
        Triangle bottom = new Triangle(frustumCorners.get(7),frustumCorners.get(5),frustumCorners.get(4));
        Triangle left = new Triangle(frustumCorners.get(2),frustumCorners.get(6),frustumCorners.get(4));
        Triangle near = new Triangle(frustumCorners.get(0),frustumCorners.get(4),frustumCorners.get(5));
        Triangle far = new Triangle(frustumCorners.get(2),frustumCorners.get(3),frustumCorners.get(7));

        var temp = right.normal().normalize();
        toReturn[0] = new float[] {temp.x,temp.y,-temp.z, 0f};
        temp = left.normal().normalize();
        toReturn[1] = new float[] {temp.x,temp.y,-temp.z, 0f};
        temp = bottom.normal().normalize();
        toReturn[2] = new float[] {temp.x,temp.y,-temp.z, 0f};
        temp = top.normal().normalize();
        toReturn[3] = new float[] {temp.x,temp.y,-temp.z, 0f};
        temp = far.normal().normalize();
        toReturn[4] = new float[] {temp.x,temp.y,-temp.z, 128f};
        temp = near.normal().normalize();
        toReturn[5] = new float[] {temp.x,temp.y,-temp.z, 1f};


        return toReturn;
    }
}
