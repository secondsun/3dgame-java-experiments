package util;

import geometry.Vertex;

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
        return Math.min(max, Math.max(min,val));
    }

    public static Vertex subtract(Vertex from, Vertex to) {
        return new Vertex(from.x-to.x,from.y-to.y,from.z-to.z);
    }

    public static Vertex normalize(Vertex subtract) {
        float length = (float) Math.sqrt(subtract.x*subtract.x+subtract.y*subtract.y+subtract.z*subtract.z);
        return new Vertex(subtract.x/length,subtract.y/length,subtract.z/length);
    }
}
