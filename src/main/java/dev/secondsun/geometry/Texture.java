package dev.secondsun.geometry;

import java.io.Serializable;

public record Texture(int imageId, Vertex2D origin, int u, int v) implements Serializable{
}
