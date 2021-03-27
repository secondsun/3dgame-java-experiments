package importer;

import geometry.Triangle;
import geometry.Vertex;
import geometry.Vertex2D;
import util.Resources;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class ImportMain {

    private static final int MESH_INDEX = 0x40;
    private static final int PALETTE_INDEX = 0x44;

    private static int pointer;

    public static ArrayList<Triangle> calc(String meshFile, String textureFile) throws IOException {

        var mapStream = ImportMain.class.getClassLoader().getResourceAsStream(meshFile);
        var textureStream = ImportMain.class.getClassLoader().getResourceAsStream(textureFile);
        var meshBytes = mapStream.readAllBytes();

        pointer = PALETTE_INDEX;
        pointer = readInt(meshBytes);
        int[] palette = readPalette(meshBytes);
        var textureBytes = textureStream.readAllBytes();
        registerTextures(textureBytes, palette);




        pointer = MESH_INDEX;
        pointer = readInt(meshBytes);
        System.out.println((pointer & 0x0FF) + " " + pointer);
        var texturedTriangleCount = readUnsignedShort(meshBytes);
        var texturedQuadCount = readUnsignedShort(meshBytes );
        var untexturedTriangleCount = readUnsignedShort(meshBytes );
        var untexturedQuadCount = readUnsignedShort(meshBytes );

        var triangles = new ArrayList<Triangle>();

        for (int i = 0; i < texturedTriangleCount; i++) {
            var tri = new Triangle(
                    new Vertex(readSignedShort(meshBytes), -readSignedShort(meshBytes), readSignedShort(meshBytes)),
                    new Vertex(readSignedShort(meshBytes), -readSignedShort(meshBytes), readSignedShort(meshBytes)),
                    new Vertex(readSignedShort(meshBytes), -readSignedShort(meshBytes), readSignedShort(meshBytes)),
                    Color.RED.getRGB()
            );
            triangles.add(tri);
        }

        for (int i = 0; i < texturedQuadCount; i++) {
            var tri = new Triangle(
                    new Vertex(readSignedShort(meshBytes), -readSignedShort(meshBytes), readSignedShort(meshBytes)),
                    new Vertex(readSignedShort(meshBytes), -readSignedShort(meshBytes), readSignedShort(meshBytes)),
                    new Vertex(readSignedShort(meshBytes), -readSignedShort(meshBytes), readSignedShort(meshBytes)),
                    Color.RED.getRGB()
            );
            triangles.add(tri);

            tri = new Triangle(
                    new Vertex(readSignedShort(meshBytes), -readSignedShort(meshBytes), readSignedShort(meshBytes)),
                    tri.v3,
                    tri.v2,
                    Color.RED.getRGB()
            );
            triangles.add(tri);

        }

        //skip past skirt
        for (int i = 0; i < untexturedTriangleCount; i++) {
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);        }

        for (int i = 0; i < untexturedQuadCount; i++) {
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
        }
        //skip past normals
        for (int i = 0; i < texturedTriangleCount; i++) {
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);

        }

        for (int i = 0; i < texturedQuadCount; i++) {
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);
            readSignedShort(meshBytes);

        }

        //set texture coordinates
        for (int i = 0; i < texturedTriangleCount; i++) {
            int au = (meshBytes[pointer++]) & 0x0FF;
            int av = meshBytes[pointer++]& 0x0FF;
            int palettenumber = meshBytes[pointer++]& 0x0FF;

            pointer++;//padding
            int bu = meshBytes[pointer++]& 0x0FF;
            int bv = meshBytes[pointer++]& 0x0FF;
            int page = 256*(meshBytes[pointer++]& 0x03);
            pointer++;//padding

            int cu = meshBytes[pointer++]& 0x0FF;
            int cv = meshBytes[pointer++]& 0x0FF;

            var image = Resources.getImage(palettenumber);

            var color  = image.getRGB((au + bu + cu)/3,((page + cv)  +(page + bv)  +(page + av) )/3);

            triangles.get(i).textureId = color;
        }

        for (int i = 0; i < texturedQuadCount; i++) {
            int au = (meshBytes[pointer++]) & 0x0FF;
            int av = meshBytes[pointer++]& 0x0FF;
            int palettenumber = meshBytes[pointer++]& 0x0FF;

            pointer++;//padding
            int bu = meshBytes[pointer++]& 0x0FF;
            int bv = meshBytes[pointer++]& 0x0FF;
            int page = 256*(meshBytes[pointer++]& 0x03);
            pointer++;//padding

            int cu = meshBytes[pointer++]& 0x0FF;
            int cv = meshBytes[pointer++]& 0x0FF;

            int du = meshBytes[pointer++]& 0x0FF;
            int dv = meshBytes[pointer++]& 0x0FF;

            var image = Resources.getImage(palettenumber);

            var color  = image.getRGB((du + au + bu + cu)/4,((page + dv)  +(page + cv)  +(page + bv)  +(page + av) )/4);
            triangles.get(texturedTriangleCount + ( i * 2 )).textureId = color;
            triangles.get(texturedTriangleCount + ( i * 2 ) + 1).textureId = color;
        }

        //buffer?
        pointer+= (4*untexturedQuadCount + 4*untexturedTriangleCount);


        //Polygon tile locations
        pointer+= (2*texturedQuadCount + 2*texturedTriangleCount);

        return triangles;

    }

    private static int[] readPalette(byte[] meshBytes) {
        int[] palette = new int[256];

        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                int color = readUnsignedShort(meshBytes);
                palette[ y*16 + x ] = color;
            }
        }

        return palette;

    }

    private static void registerTextures(byte[] textureBytes, int[] palette) {
        for (int i = 0; i < 16; i++) {
            Color[] colors = new Color[16];
            for (int j = 0; j < 16; j++) {
                int b,g,r,a, color;
                color = palette[i*16 + j];
                r = color & 0x1F;
                g = (color >> 5) & 0x1F;
                b = (color >> 10) & 0x1F;
                a = (color >> 15) & 0x01;

                if (r == 0 && g == 0 && b == 0 && a == 0) {
                    colors[j] = new Color(0,0,0,128);
                } else {
                    colors[j] = new Color((r*255)/31,(g*255)/31,(b*255)/31);
                }
            }
            BufferedImage image = new BufferedImage(256,1024, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0 ; y <1024;y++){
                for (int x = 0; x < 128; x++) {
                    int duopixel = textureBytes[(y*128) + x];
                    int leftpixel = duopixel & 0x0F;
                    int rightpixel = (duopixel>>4) & 0x0F;

                    image.setRGB(2*x,y,colors[leftpixel].getRGB());
                    image.setRGB(2*x+1,y,colors[rightpixel].getRGB());

                }
            }
            Resources.setImage(image, i);


        }
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
