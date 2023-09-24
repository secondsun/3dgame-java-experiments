package dev.secondsun.util;

import dev.secondsun.geometry.Quad;
import dev.secondsun.geometry.Vertex;
import dev.secondsun.geometry.Vertex2D;

public final class Maths {
    public static int min(int... y) {
        int toReturn = Integer.MAX_VALUE;
        for (int min : y) {
            if (min < toReturn) {
                toReturn = min;
            }
        }
        return toReturn;
    }

    public static int max(int... y) {
        int toReturn = Integer.MIN_VALUE;
        for (int max : y) {
            if (max > toReturn) {
                toReturn = max;
            }
        }
        return toReturn;
    }

    public static float min(float... y) {
        float toReturn = Float.MAX_VALUE;
        for (float min : y) {
            if (min < toReturn) {
                toReturn = min;
            }
        }
        return toReturn;
    }

    public static float max(float... y) {
        float toReturn = -999999999f;
        for (float max : y) {
            if (max > toReturn) {
                toReturn = max;
            }
        }
        return toReturn;
    }

    public static int clamp(int min, int max, int val) {
        return Math.min(max, Math.max(min, val));
    }

    public static Vertex subtract(Vertex from, Vertex to) {
        return new Vertex(from.x - to.x, from.y - to.y, from.z - to.z);
    }

    public static Vertex add(Vertex from, Vertex to) {
        return new Vertex(from.x + to.x, from.y + to.y, from.z + to.z);
    }

    public static Vertex normalize(Vertex subtract) {
        float length = (float) Math.sqrt(subtract.x * subtract.x + subtract.y * subtract.y + subtract.z * subtract.z);
        return new Vertex(subtract.x / length, subtract.y / length, subtract.z / length);
    }

    public static Vertex2D subtract(Vertex2D from, Vertex2D to) {
        return new Vertex2D(from.x - to.x, from.y - to.y);
    }

    public static Vertex2D add(Vertex2D from, Vertex2D to) {
        return new Vertex2D(from.x + to.x, from.y + to.y);
    }

    public static Vertex2D scale(Vertex2D from, float to) {
        return new Vertex2D(from.x * to, from.y * to);
    }

    public static Vertex scale(Vertex from, float to) {
        return new Vertex(from.x * to, from.y * to, from.z * to);
    }

    /**
     * The vertecies in quad need to be in order such that the first vertx is 0,0 in the texture
     * the clockwise around
     * <p>
     * see http://iquilezles.org/www/articles/ibilinear/ibilinear.htm
     *
     * @param point point to get the uv of
     * @param quad  where the point is
     * @return uv from 0 to 1
     */
    public static Vertex2D reverseBilinear(Vertex2D point, Quad quad) {
        
        var e = Maths.subtract(quad.B(), quad.A());
        var f = Maths.subtract(quad.D(), quad.A());
        var g = Maths.subtract(Maths.add(quad.C(), Maths.subtract(quad.A(), quad.B())), quad.D());
        var h = Maths.subtract(point, quad.A());

        float k1 = Maths.cross(e, f) + Maths.cross(h, g);
        float k0 = Maths.cross(h, e);


        float v = Math.abs(-k0 / k1);
        float u = Math.abs((h.x * k1 + f.x * k0) / (e.x * k1 - g.x * k0));

        var tu = u;
        var tv = v;


        return new Vertex2D(tu, tv);


    }

    private static float cross(Vertex2D a, Vertex2D b) {
        return a.x * b.y - a.y * b.x;
    }

    public static float dot(Vertex vertex, Vertex axis) {
        return vertex.x * axis.x + vertex.y * axis.y + vertex.z * axis.z;
    }



}
