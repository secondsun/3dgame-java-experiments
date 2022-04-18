package dev.secondsun.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;

import dev.secondsun.geometry.playfield.DormRoom;
import dev.secondsun.util.Resources;

public class ResourcesIOTest {

    @Test
    public void testIO() throws Exception {
        var resources = new Resources();
        new DormRoom(resources);//Initialize resources

        var bos = new ByteArrayOutputStream();
        var oos = new ObjectOutputStream(bos);
        oos.writeObject(resources);


        var newResources = (Resources)new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray())).readObject();
        var newImages = newResources.images();
        var newTextures = newResources.textures();
        var oldImages = resources.images();
        var oldTextures = resources.textures();

        assertEquals(oldImages.size(), newImages.size());
        assertEquals(oldTextures.size(), newTextures.size());
    

        assertEquals(oldImages.keySet(), newImages.keySet());
        
        List<BufferedImage> oldImageValues = List.copyOf(oldImages.values());
        List<BufferedImage> newImageValues = List.copyOf(newImages.values());
        
        for (int i = 0; i < oldImageValues.size(); i++) {
            var newImage = newImageValues.get(i);
            var oldImage = oldImageValues.get(i);
            assertNotEquals(newImage, oldImage);
            int width = newImage.getWidth();
            int height = newImage.getHeight();
            assertEquals(oldImage.getWidth(), width);
            assertEquals(oldImage.getHeight(), height);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    assertEquals(oldImage.getRGB(x, y), newImage.getRGB(x, y));
                }   
            }
        }

        assertEquals(oldTextures.keySet(), newTextures.keySet());
        
        assertTrue(newTextures.values().containsAll(oldTextures.values()));

    }
    
}
