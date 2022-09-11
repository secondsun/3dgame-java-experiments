package dev.secondsun.game;

import dev.secondsun.geometry.Triangle;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Renderer {
    /**
     *
     * @param tiles the ordered list of polygons to draw
     * @return integrer array of RGB data
     */
    int[] draw(List<Triangle> tiles);

}
