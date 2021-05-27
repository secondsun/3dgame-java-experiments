package dev.secondsun.util;

import dev.secondsun.geometry.Vertex;

public class Plane {

    public final Vertex location, normal;

    public Plane(Vertex  location, Vertex normal) {
        this.location = location;
        this.normal = normal;
    }
}
