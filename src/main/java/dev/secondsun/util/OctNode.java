package dev.secondsun.util;

import dev.secondsun.geometry.Triangle;
import dev.secondsun.geometry.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public sealed interface OctNode {
    final OctNode Empty = new OctNode.Empty();

    record Empty() implements  OctNode{}

    final class PolygonNode implements OctNode {


        private final Vertex point;
        private final List<Triangle> polygons = new ArrayList<>();

        public PolygonNode(Vertex point, Triangle polygon)  {
            this.point = point;
            this.polygons.add(polygon);
        }

        public void add(Triangle polygon) {
            polygons.add(polygon);
        }

        public List<Triangle> getPolygons() {
            return List.copyOf(polygons);
        }

        public List<Triangle> getSortedPolys(Vertex cameraPosition) {
            return polygons.stream().sorted(new Comparator<Triangle>() {
                @Override
                public int compare(Triangle o1, Triangle o2) {
                    var first = Maths.distance(o1.center(),cameraPosition);
                    var second = Maths.distance(o1.center(),cameraPosition);
                    if (first-second == 0) {
                        return 0;
                    } else if (first-second < 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }).toList();
        }

        public Vertex point() {
            return point;
        }
    }
    record IntermediateNode(OctTree tree) implements OctNode {

    }
}
