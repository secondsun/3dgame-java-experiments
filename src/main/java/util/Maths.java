package util;

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
}
