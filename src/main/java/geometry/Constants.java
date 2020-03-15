package geometry;

import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final int[] rotX128Array = {0,30, 33, 36, 39, 42, 45};
    public static final int[] rotY128Array = {0, 7, 14, 21, 28, 35, 42, 49, 56, 63, 70, 77, 84, 91, 98, 105, 112, 119, 126, 133, 140, 147, 154, 161, 168, 175, 182, 189, 196, 203, 210, 217, 224, 231, 238, 245, 252, 259, 266, 273, 280, 287, 294, 301, 308, 315, 322, 329, 336, 343, 350, 357,};

    public static Map<Point, Matrix> getPrecalcs() {
        var map = new HashMap<Point, Matrix>();

        for (int rotx : rotX128Array) {
            for (int roty : rotY128Array) {
                map.put(new Point(rotx, roty), calc(rotx, roty));
            }
        }

        return map;
    }

    private static Matrix calc(int rotX, int rotY) {
        double[][] rotXMatrixd = new double[][]{
                {1, 0, 0},
                {0, Math.round(Math.cos(Math.toRadians(rotX)) * 128), Math.round(Math.sin(Math.toRadians(rotX)) * 128)},
                {0, -Math.round(Math.sin(Math.toRadians(rotX)) * 128), Math.round(Math.cos(Math.toRadians(rotX)) * 128)},
        };

        double[][] rotYMatrixd = new double[][]{
                {Math.round(Math.cos(Math.toRadians(rotY)) * 128), 0, -Math.round(Math.sin(Math.toRadians(rotY)) * 128)},
                {0, 1, 0},
                {-Math.round(Math.sin(Math.toRadians(rotY)) * 128), 0, Math.round(Math.cos(Math.toRadians(rotY)) * 128)},
        };

        Matrix rotXMatrix = new Basic2DMatrix(rotXMatrixd);
        Matrix rotYMatrix = new Basic2DMatrix(rotYMatrixd);
        Matrix rotMatrix = rotYMatrix.multiply(rotXMatrix);
        return rotMatrix;
    }

    private static Matrix calcReverse(int rotX, int rotY) {
        double[][] rotXMatrixd = new double[][]{
                {1, 0, 0},
                {0, Math.round(Math.cos(Math.toRadians(-rotX)) * 128), Math.round(Math.sin(Math.toRadians(-rotX)) * 128)},
                {0, -Math.round(Math.sin(Math.toRadians(-rotX)) * 128), Math.round(Math.cos(Math.toRadians(-rotX)) * -128)},
        };

        double[][] rotYMatrixd = new double[][]{
                {Math.round(Math.cos(Math.toRadians(-rotY)) * 128), 0, Math.round(Math.sin(Math.toRadians(-rotY)) * -128)},
                {0, 1, 0},
                {-Math.round(Math.sin(Math.toRadians(-rotY)) * 128), 0, Math.round(Math.cos(Math.toRadians(-rotY)) * 128)},
        };

        Matrix rotXMatrix = new Basic2DMatrix(rotXMatrixd);
        Matrix rotYMatrix = new Basic2DMatrix(rotYMatrixd);
        Matrix rotMatrix = rotXMatrix.multiply(rotYMatrix);
        return rotMatrix;
    }

    public static Map<Point, Matrix> getReversePrecalcs() {
        var map = new HashMap<Point, Matrix>();

        for (
                int rotx : rotX128Array) {
            for (int roty : rotY128Array) {
                map.put(new Point(rotx, roty), calcReverse(rotx, roty));
            }
        }

        return map;
    }


}
