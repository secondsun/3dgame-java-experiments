package dev.secondsun.geometry;

import dev.secondsun.util.Maths;

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

}
