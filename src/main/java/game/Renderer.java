package game;

import geometry.Camera;
import geometry.Triangle;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Renderer {
    BufferedImage draw(List<Triangle> tiles);

    void setCamera(Camera camera);
}
