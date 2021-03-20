package importer;

import geometry.Triangle;
import geometry.Vertex;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ImportMain {

    private static byte[] bytes;
    private static int pointer;

    public static ArrayList<Triangle> calc(String arg) throws IOException {
        var mapStream = ImportMain.class.getClassLoader().getResourceAsStream(arg);
        var bytes = mapStream.readAllBytes();
        pointer = 0x40;
        pointer = readInt(bytes);
        System.out.println((pointer & 0x0FF) + " " + pointer);
        var texturedTriangleCount = readUnsignedShort(bytes);
        var texturedQuadCount = readUnsignedShort(bytes );
        var untexturedTriangleCount = readUnsignedShort(bytes );
        var untexturedQuadCount = readUnsignedShort(bytes );

        var triangles = new ArrayList<Triangle>();

        for (int i = 0; i < texturedTriangleCount; i++) {
            var tri = new Triangle(
                    new Vertex(readSignedShort(bytes), -readSignedShort(bytes), readSignedShort(bytes)),
                    new Vertex(readSignedShort(bytes), -readSignedShort(bytes), readSignedShort(bytes)),
                    new Vertex(readSignedShort(bytes), -readSignedShort(bytes), readSignedShort(bytes)),
                    Color.RED.getRGB()
            );
            triangles.add(tri);
        }

        for (int i = 0; i < texturedQuadCount; i++) {
            var tri = new Triangle(
                    new Vertex(readSignedShort(bytes), -readSignedShort(bytes), readSignedShort(bytes)),
                    new Vertex(readSignedShort(bytes), -readSignedShort(bytes), readSignedShort(bytes)),
                    new Vertex(readSignedShort(bytes), -readSignedShort(bytes), readSignedShort(bytes)),
                    Color.RED.getRGB()
            );
            triangles.add(tri);

            tri = new Triangle(
                    new Vertex(readSignedShort(bytes), -readSignedShort(bytes), readSignedShort(bytes)),
                    tri.v3,
                    tri.v2,
                    Color.RED.getRGB()
            );
            triangles.add(tri);

        }

        return triangles;

    }

    private static int readInt(byte[] bytes) {
        return (bytes[pointer++] & 0xFF) | ((bytes[pointer++] & 0x0FF) << 8) | ((bytes[pointer++] & 0x0FF) << 16) | ((bytes[pointer++] & 0x0FF) << 24);
    }


    private static int readSignedShort(byte[] bytes) {

        return (short)((bytes[pointer++] & 0xFF) | ((bytes[pointer++] & 0x0FF) << 8)) ;
    }

    private static int readUnsignedShort(byte[] bytes) {

        return (bytes[pointer++] & 0xFF) | ((bytes[pointer++] & 0x0FF) << 8) ;
    }

}
