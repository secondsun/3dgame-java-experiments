package dev.secondsun.game;

import dev.secondsun.geometry.Camera;
import dev.secondsun.geometry.Triangle;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Renderer {
    BufferedImage draw(List<Triangle> tiles);

}
