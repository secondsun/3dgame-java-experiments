package dev.secondsun.util;

import dev.secondsun.geometry.Texture;
import dev.secondsun.geometry.Vertex2D;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Resources {
    private  final  Map<Integer, BufferedImage> images = new HashMap<>();
    private  final  Map<Integer, Texture> textures = new HashMap<>();

    public   int setImage(BufferedImage in) {
        var hash = in.hashCode();
        images.put(hash,in);
        return hash;
    }

    public  BufferedImage getImage(int in) {
        return images.get(in);
    }

    public   int setTexture(int imageId, Vertex2D textureOrigin, int u, int v) {
        var text = new Texture(imageId, textureOrigin, u, v);
        var hash = text.hashCode();
        textures.put(hash,text);
        return hash;
    }

    public  Texture getTexture(int in) {
        return textures.get(in);
    }

}
