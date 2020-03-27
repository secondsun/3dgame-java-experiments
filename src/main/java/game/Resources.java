package game;

import geometry.Texture;
import geometry.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Resources {
    private static final  Map<Integer, BufferedImage> images = new HashMap<>();
    private static final  Map<Integer, Texture> textures = new HashMap<>();

    public static  int setImage(BufferedImage in) {
        var hash = in.hashCode();
        images.put(hash,in);
        return hash;
    }

    public static BufferedImage getImage(int in) {
        return images.get(in);
    }

    public static  int setTexture(int imageId, Vertex textureOrigin, float x, float y) {
        var text = new Texture(imageId, textureOrigin, x, y);
        var hash = text.hashCode();
        textures.put(hash,text);
        return hash;
    }

    public static Texture getTexture(int in) {
        return textures.get(in);
    }

}
