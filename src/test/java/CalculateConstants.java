import game.Board;
import geometry.Constants;
import geometry.Vertex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.la4j.LinearAlgebra;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateConstants {

    private static final int SCALE = 100;//Scaling factor, we only want to use ints

    @Test
    @DisplayName("Summers, you are calculating the reverse transformation matricies ")
    public void calc2() {
        int count = 0;
        for (int rotY = 0; rotY < 360; rotY += (360 / 50)) {
            for (int rotX = 30; rotX <= 45; rotX += (15 / 4)) {


                double[][] rotXMatrixd = new double[][]{
                        {1, 0, 0},
                        {0, Math.round(Math.cos(Math.toRadians(-rotX)) *  128), Math.round(Math.sin(Math.toRadians(-rotX)) * -128)},
                        {0, Math.round(Math.cos(Math.toRadians(-rotX)) *  128), Math.round(Math.sin(Math.toRadians(-rotX)) * -128)},
                };

                double[][] rotYMatrixd = new double[][]{
                        {Math.round(Math.cos(Math.toRadians(-rotY)) *  128), 0, Math.round(Math.sin(Math.toRadians(-rotY)) *  128)},
                        {0, 1, 0},
                        {Math.round(-Math.sin(Math.toRadians(-rotY)) *  128), 0, Math.round(Math.cos(Math.toRadians(-rotY)) *  128)},
                };

                Matrix rotXMatrix = new Basic2DMatrix(rotXMatrixd);
                Matrix rotYMatrix = new Basic2DMatrix(rotYMatrixd);
                Matrix rotMatrix = rotXMatrix.multiply(rotYMatrix);

                //System.out.println(rotY + "," + rotX + " -> {" + rotMatrix + "}");

                count++;
            }


            int rotX = 45;


            double[][] rotXMatrixd = new double[][]{
                    {1, 0, 0},
                    {0, Math.round(Math.cos(Math.toRadians(-rotX)) *  128), Math.round(Math.sin(Math.toRadians(-rotX)) * -128)},
                    {0, Math.round(Math.cos(Math.toRadians(-rotX)) *  128), Math.round(Math.sin(Math.toRadians(-rotX)) * -128)},
            };

            double[][] rotYMatrixd = new double[][]{
                    {Math.round(Math.cos(Math.toRadians(rotY)) *  128), 0, Math.round(Math.sin(Math.toRadians(rotY)) *  128)},
                    {0, 1, 0},
                    {Math.round(-Math.sin(Math.toRadians(rotY)) *  128), 0, Math.round(Math.cos(Math.toRadians(rotY)) *  128)},
            };

            Matrix rotXMatrix = new Basic2DMatrix(rotXMatrixd);
            Matrix rotYMatrix = new Basic2DMatrix(rotYMatrixd);
            Matrix rotMatrix = rotXMatrix.multiply(rotYMatrix);


            System.out.println(rotY + "," + rotX + " -> {" + rotMatrix + "}");

            count++;

        }
        System.out.println("Using " + count * 2 + " bytes");


    }


    @Test
    @DisplayName("Summers, you are calculating the transformation matricies ")
    public void calc() {
        int count = 0;
        for (int rotY = 0; rotY < 360; rotY += (360 / 50)) {
            for (int rotX = 30; rotX <= 45; rotX += (15 / 4)) {


                double[][] rotXMatrixd = new double[][]{
                        {1, 0, 0},
                        {0, Math.round(Math.cos(Math.toRadians(rotX)) *  128), Math.round(Math.sin(Math.toRadians(rotX)) * -128)},
                        {0, Math.round(Math.cos(Math.toRadians(rotX)) *  128), Math.round(Math.sin(Math.toRadians(rotX)) * -128)},
                };

                double[][] rotYMatrixd = new double[][]{
                        {Math.round(Math.cos(Math.toRadians(rotY)) *  128), 0, Math.round(Math.sin(Math.toRadians(rotY)) *  128)},
                        {0, 1, 0},
                        {-Math.round(Math.sin(Math.toRadians(rotY)) *  128), 0, Math.round(Math.cos(Math.toRadians(rotY)) *  128)},
                };

                Matrix rotXMatrix = new Basic2DMatrix(rotXMatrixd);
                Matrix rotYMatrix = new Basic2DMatrix(rotYMatrixd);
                Matrix rotMatrix = rotYMatrix.multiply(rotXMatrix);


                System.out.println(rotY + "," + rotX + " -> {" + rotMatrix + "}");
                count++;
            }


            int rotX = 45;


            double[][] rotXMatrixd = new double[][]{
                    {1, 0, 0},
                    {0, Math.round(Math.cos(Math.toRadians(rotX)) *  128), Math.round(Math.sin(Math.toRadians(rotX)) * -128)},
                    {0, Math.round(Math.cos(Math.toRadians(rotX)) *  128), Math.round(Math.sin(Math.toRadians(rotX)) * -128)},
            };

            double[][] rotYMatrixd = new double[][]{
                    {Math.round(Math.cos(Math.toRadians(rotY)) *  128), 0, Math.round(Math.sin(Math.toRadians(rotY)) *  128)},
                    {0, 1, 0},
                    {Math.round(-Math.sin(Math.toRadians(rotY)) *  128), 0, Math.round(Math.cos(Math.toRadians(rotY)) *  128)},
            };

            Matrix rotXMatrix = new Basic2DMatrix(rotXMatrixd);
            Matrix rotYMatrix = new Basic2DMatrix(rotYMatrixd);
            Matrix rotMatrix = rotYMatrix.multiply(rotXMatrix);


            System.out.println(rotY + "," + rotX + " -> {" + rotMatrix + "}");

            count++;

        }
        System.out.println("Using " + count * 2 + " bytes");


    }

    @Test
    @DisplayName("Check if I can reverse board positions")
    public void reverse() {
        //For each pixel we want to see if we should check if it is on the board.
        //To do this we multiply the pixel by the reverse matrix and if it is on the screen, we draw it.
        Board board = new Board(192,156, new int[]{0xfc49ab,0xff7300,0xe7ff00,0x5fe8ff,0x64ff00});
        int rotY = 0;
        int rotX = 30;
        board = board.rotateY(rotY).rotateX(rotX);

        int miss = board.draw(0,0,0);
        int hit = board.draw(96,128,0);
        assertEquals(0, miss);
        assertEquals(0xfc49ab, hit);

    }

}
