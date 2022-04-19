package dev.secondsun.util;

import dev.secondsun.geometry.Vertex;

public class Plane {

    public final Vertex location, normal;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((normal == null) ? 0 : normal.hashCode());
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
        Plane other = (Plane) obj;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (normal == null) {
            if (other.normal != null)
                return false;
        } else if (!normal.equals(other.normal))
            return false;
        return true;
    }

    public Plane(Vertex  location, Vertex normal) {
        this.location = location;
        this.normal = normal;
    }
}
