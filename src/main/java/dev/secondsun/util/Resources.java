package dev.secondsun.util;

import dev.secondsun.geometry.Texture;
import dev.secondsun.geometry.Vertex2D;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;


public class Resources implements Externalizable {
    private  final  Map<Integer, BufferedImage> images = new HashMap<>();
    private  final  Map<Integer, Texture> textures = new HashMap<>();

    public Map<Integer, BufferedImage> images() {
        return new HashMap<>(images);
    }

    public Map<Integer, Texture> textures() {
        return new HashMap<>(textures);
    }

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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(images.size());
        images.entrySet().forEach( entry -> {
                try {
                    out.writeInt(entry.getKey());
                    var image = entry.getValue();
                    var bos = new ByteArrayOutputStream();
                    ImageIO.write(image, "PNG", bos);
                    out.writeInt(bos.size());
                    out.write(bos.toByteArray());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        );
        out.writeInt(textures.size());
        textures.entrySet().forEach(entry -> {
            try {
                out.writeInt(entry.getKey());
            
               out.writeObject(entry.getValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        });
    }   

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        var imagesCount = in.readInt();
        IntStream.range(0, imagesCount).forEach(ignore -> {
            try {
                var id = in.readInt();
                var imageLength = in.readInt();
                var imageBytes = new byte[imageLength];
                in.readFully(imageBytes);
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
                images.put(id, image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        var textureCount = in.readInt();
        IntStream.range(0,textureCount).forEach(ignore -> {
            try {
            var id = in.readInt();
            var texture = (Texture) in.readObject();
            textures.put(id, texture);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        });


    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((images == null) ? 0 : images.hashCode());
        result = prime * result + ((textures == null) ? 0 : textures.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Resources other = (Resources) obj;
        if (images == null) {
            if (other.images != null)
                return false;
        } else if (!images.equals(other.images))
            return false;
        if (textures == null) {
            if (other.textures != null)
                return false;
        } else if (!textures.equals(other.textures))
            return false;
        return true;
    }

}
