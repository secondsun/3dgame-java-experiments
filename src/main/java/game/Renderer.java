package game;

import geometry.Triangle;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Renderer {
    BufferedImage draw(List<Triangle> tiles);
}
