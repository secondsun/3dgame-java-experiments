package dev.secondsun.util;

import dev.secondsun.geometry.Camera;
import dev.secondsun.geometry.Triangle;
import dev.secondsun.geometry.Vertex;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OctTree {
    private final Range xyPlaneRange, xzPlaneRange, yzPlaneRange;
    private final Vertex center;
    private final float range;
    //Front/Back in Order xyPlane, xzPlane, yzPLane
    private OctNode FFF, FFB, FBF, FBB, BFF,BFB, BBF, BBB;


    public OctTree(float range, Vertex center) {
        FFF =  FFB =  FBF= FBB = BFF=BFB= BBF= BBB = OctNode.Empty;
        this.center = center;
        this.range = range;
        this.xyPlaneRange = new Range(-range+ center.z, range+ center.z);
        this.xzPlaneRange = new Range(-range+ center.y, range+ center.y);
        this.yzPlaneRange = new Range(-range+ center.x, range+ center.x);
    }

    public List<Triangle> traverse(Camera camera, float[][] frustum) {
        Vertex camPos = camera.getFrom();
        List<OctNode> nodesToProcess;
        var toReturn = new LinkedHashSet<Triangle>();

        if (!isInFrustum(frustum)) {
            return new ArrayList<>();
        }

        if (camPos.z <= xyPlaneRange.mid()) {
            if (camPos.y > xzPlaneRange.mid()) {
                if (camPos.x > yzPlaneRange.mid()) {
                    nodesToProcess = List.of(FFF,FFB,FBF,FBB,BFF,BFB,BBF,BBB);
                } else {
                    nodesToProcess = List.of(FFB,BFB,FBB,BBB,FFF,BFF,FBF,BBF);
                }
            } else {
                if (camPos.x > yzPlaneRange.mid()) {
                    nodesToProcess = List.of(FBF,FBB,BBF,BBB,FFB,BFF,FFF,BFB);
                } else {
                    nodesToProcess = List.of(FBB,BBB,FBF,BBF,BFB,FFF,FFB,BFF);
                }
            }
        } else {
            if (camPos.y > xzPlaneRange.mid()) {
                if (camPos.x > yzPlaneRange.mid()) {
                    nodesToProcess = List.of(BFF, FFF, BBF, FBF, BFB, FFB, BBB, FBB);
                } else {
                    nodesToProcess = List.of(BFB, BFF, BBB, BBF, FFB, FFF, FBB, FBF);
                }
            } else {
                if (camPos.x > yzPlaneRange.mid()) {
                        nodesToProcess = List.of(BBF, FBF, BBB, FBB, BFF, FFF, BFB, FFB);
                    } else {
                        nodesToProcess = List.of(BBB, BBF, FBB, FBF, BFF, FFB, BFB, FFF);
                    }
                }
            }

        nodesToProcess.stream().filter(Objects::nonNull).forEach(node -> {
            if (node instanceof OctNode.PolygonNode) {
                toReturn.addAll(((OctNode.PolygonNode) node).getSortedPolys(camPos));
            } else if (node instanceof OctNode.IntermediateNode) {
                toReturn.addAll(((OctNode.IntermediateNode) node).tree().traverse(camera, frustum));
            }
        });

        return toReturn.stream().toList();
    }

    boolean isInFrustum(float[][] frustum )
    {
//        int p;
//        var z = center.z;
//        var y = center.y;
//        var x = center.x;
//        var size = this.range;
//        for( p = 0; p < 6; p++ )
//        {
//            if( frustum[p][0] * (x - size) + frustum[p][1] * (y - size) + frustum[p][2]    * (z - size) + frustum[p][3] > 0 )
//                continue;
//            if( frustum[p][0] * (x + size) + frustum[p][1] * (y - size) + frustum[p][2]    * (z - size) + frustum[p][3] > 0 )
//                continue;
//            if( frustum[p][0] * (x - size) + frustum[p][1] * (y + size) + frustum[p][2]    * (z - size) + frustum[p][3] > 0 )
//                continue;
//            if( frustum[p][0] * (x + size) + frustum[p][1] * (y + size) + frustum[p][2]    * (z - size) + frustum[p][3] > 0 )
//                continue;
//            if( frustum[p][0] * (x - size) + frustum[p][1] * (y - size) + frustum[p][2]    * (z + size) + frustum[p][3] > 0 )
//                continue;
//            if( frustum[p][0] * (x + size) + frustum[p][1] * (y - size) + frustum[p][2]    * (z + size) + frustum[p][3] > 0 )
//                continue;
//            if( frustum[p][0] * (x - size) + frustum[p][1] * (y + size) + frustum[p][2]    * (z + size) + frustum[p][3] > 0 )
//                continue;
//            if( frustum[p][0] * (x + size) + frustum[p][1] * (y + size) + frustum[p][2]    * (z + size) + frustum[p][3] > 0 )
//                continue;
//            return false;
//        }
//        return true;

        for ( var i = 0; i < 6; i ++ ) {

			var plane = new Vertex(frustum[ i ][0],frustum[ i ][1],frustum[ i ][2]);
            // corner at max distance
            var _vector = new Vertex(plane.normalize().x > 0 ? center.x+range : center.x-range,
                                     plane.normalize().y > 0 ? center.y+range : center.y-range,
                                     plane.normalize().z > 0 ? center.z+range : center.z-range);



            if ( Maths.dot(plane.normalize(),( _vector )) + frustum[i][3] < 0 ) {

                return false;

            }

        }

        return true;
    }

    public void insert(Vertex point, Triangle polygon) {
        //TODO add a node
        //see https://www.cg.tuwien.ac.at/research/vr/lodestar/tech/octree/
        if (point.z <= xyPlaneRange.mid()) {
            if (point.y > xzPlaneRange.mid()) {
                if (point.x > yzPlaneRange.mid()) {
                    if (FFF == OctNode.Empty) {
                        FFF = new OctNode.PolygonNode(point, polygon);
                    } else {
                        FFF = handleInsert(point, polygon, FFF, new OctTree(range/2f, new Vertex(center.x + range/2, center.y + range/2f, center.z - range/2f)));
                    }
                } else {
                    if (FFB == OctNode.Empty) {
                        FFB = new OctNode.PolygonNode(point, polygon);
                    } else {
                        FFB = handleInsert(point, polygon, FFB, new OctTree(range/2f, new Vertex(center.x - range/2, center.y + range/2f, center.z - range/2f)));
                    }
                }
            } else {
                if (point.x > yzPlaneRange.mid()) {
                    if (FBF == OctNode.Empty) {
                        FBF = new OctNode.PolygonNode(point, polygon);
                    } else {
                        FBF = handleInsert(point, polygon, FBF, new OctTree(range/2f, new Vertex(center.x + range/2f, center.y - range/2f, center.z - range/2f)));
                    }
                } else {
                    if (FBB == OctNode.Empty) {
                        FBB = new OctNode.PolygonNode(point, polygon);
                    } else {
                        FBB = handleInsert(point, polygon, FBB, new OctTree(range/2f, new Vertex(center.x - range/2f, center.y - range/2f, center.z - range/2f)));
                    }
                }
            }
        } else {
            if (point.y > xzPlaneRange.mid()) {
                if (point.x > yzPlaneRange.mid()) {
                    if (BFF == OctNode.Empty) {
                        BFF = new OctNode.PolygonNode(point, polygon);
                    } else {
                        BFF = handleInsert(point, polygon, BFF, new OctTree(range/2f, new Vertex(center.x + range/2f, center.y + range/2f, center.z + range/2f)));
                    }
                } else {
                    if (BFB == OctNode.Empty) {
                        BFB = new OctNode.PolygonNode(point, polygon);
                    } else {
                        BFB = handleInsert(point, polygon, BFB, new OctTree(range/2f, new Vertex(center.x - range/2f, center.y + range/2f, center.z + range/2f)));
                    }
                }
            } else {
                if (point.x > yzPlaneRange.mid()) {
                    if (BBF == OctNode.Empty) {
                        BBF = new OctNode.PolygonNode(point, polygon);
                    } else {
                        BBF = handleInsert(point, polygon, BBF, new OctTree(range/2f, new Vertex(center.x + range/2f, center.y - range/2f, center.z + range/2f)));;
                    }
                } else {
                    if (BBB == OctNode.Empty) {
                        BBB = new OctNode.PolygonNode(point, polygon);
                    } else {
                        BBB =handleInsert(point, polygon, BBB, new OctTree(range/2f, new Vertex(center.x - range/2f, center.y - range/2f, center.z + range/2f)));
                    }
                }
            }
        }
    }

    private OctNode handleInsert(Vertex point, Triangle polygon, OctNode node, OctTree newIntermediate) {
         if (node instanceof OctNode.IntermediateNode) {
            ((OctNode.IntermediateNode) node).tree().insert(point,polygon);
        } else if (node instanceof OctNode.PolygonNode) {
             if (((OctNode.PolygonNode) node).point().equals(point)) {
                 ((OctNode.PolygonNode) node).add(polygon);
             }
            else {
                OctNode.PolygonNode oldNode = (OctNode.PolygonNode) node;
                node = new OctNode.IntermediateNode(newIntermediate);
                newIntermediate.insert(point, polygon);
                oldNode.getPolygons().forEach(  poly ->{
                     newIntermediate.insert(oldNode.point(), poly);
                 });
            }
        }
         return node;
    }

}
