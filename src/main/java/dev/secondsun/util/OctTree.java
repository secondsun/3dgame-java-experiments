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
    //Front/Back in Order xyPlane, xzPlane, yzPLane
    private OctNode FFF, FFB, FBF, FBB, BFF,BFB, BBF, BBB;


    public OctTree(Range xyPlaneRange, Range xzPlaneRange, Range yzPlaneRange) {
        FFF =  FFB =  FBF= FBB = BFF=BFB= BBF= BBB = OctNode.Empty;
        this.xyPlaneRange = xyPlaneRange;
        this.xzPlaneRange = xzPlaneRange;
        this.yzPlaneRange = yzPlaneRange;
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

        return toReturn.stream().collect(Collectors.toUnmodifiableList());
    }

    boolean isInFrustum(float[][] frustum )
    {
        int p;
        var z = xyPlaneRange.mid();
        var y = xzPlaneRange.mid();
        var x = yzPlaneRange.mid();
        var size = xyPlaneRange.length();
        for( p = 0; p < 6; p++ )
        {
            if( frustum[p][0] * (x - size) + frustum[p][1] * (y - size) + frustum[p][2]    * (z - size) + frustum[p][3] > 0 )
                continue;
            if( frustum[p][0] * (x + size) + frustum[p][1] * (y - size) + frustum[p][2]    * (z - size) + frustum[p][3] > 0 )
                continue;
            if( frustum[p][0] * (x - size) + frustum[p][1] * (y + size) + frustum[p][2]    * (z - size) + frustum[p][3] > 0 )
                continue;
            if( frustum[p][0] * (x + size) + frustum[p][1] * (y + size) + frustum[p][2]    * (z - size) + frustum[p][3] > 0 )
                continue;
            if( frustum[p][0] * (x - size) + frustum[p][1] * (y - size) + frustum[p][2]    * (z + size) + frustum[p][3] > 0 )
                continue;
            if( frustum[p][0] * (x + size) + frustum[p][1] * (y - size) + frustum[p][2]    * (z + size) + frustum[p][3] > 0 )
                continue;
            if( frustum[p][0] * (x - size) + frustum[p][1] * (y + size) + frustum[p][2]    * (z + size) + frustum[p][3] > 0 )
                continue;
            if( frustum[p][0] * (x + size) + frustum[p][1] * (y + size) + frustum[p][2]    * (z + size) + frustum[p][3] > 0 )
                continue;
            return false;
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
                        FFF = handleInsert(point, polygon, FFF, new OctTree(xyPlaneRange.firstHalf(), xzPlaneRange.secondHalf(), yzPlaneRange.secondHalf()));
                    }
                } else {
                    if (FFB == OctNode.Empty) {
                        FFB = new OctNode.PolygonNode(point, polygon);
                    } else {
                        FFB = handleInsert(point, polygon, FFB, new OctTree(xyPlaneRange.firstHalf(), xzPlaneRange.secondHalf(), yzPlaneRange.firstHalf()));
                    }
                }
            } else {
                if (point.x > yzPlaneRange.mid()) {
                    if (FBF == OctNode.Empty) {
                        FBF = new OctNode.PolygonNode(point, polygon);
                    } else {
                        FBF = handleInsert(point, polygon, FBF, new OctTree(xyPlaneRange.firstHalf(), xzPlaneRange.firstHalf(), yzPlaneRange.secondHalf()));
                    }
                } else {
                    if (FBB == OctNode.Empty) {
                        FBB = new OctNode.PolygonNode(point, polygon);
                    } else {
                        FBB = handleInsert(point, polygon, FBB, new OctTree(xyPlaneRange.firstHalf(), xzPlaneRange.firstHalf(), yzPlaneRange.firstHalf()));
                    }
                }
            }
        } else {
            if (point.y > xzPlaneRange.mid()) {
                if (point.x > yzPlaneRange.mid()) {
                    if (BFF == OctNode.Empty) {
                        BFF = new OctNode.PolygonNode(point, polygon);
                    } else {
                        BFF = handleInsert(point, polygon, BFF, new OctTree(xyPlaneRange.secondHalf(), xzPlaneRange.secondHalf(), yzPlaneRange.secondHalf()));
                    }
                } else {
                    if (BFB == OctNode.Empty) {
                        BFB = new OctNode.PolygonNode(point, polygon);
                    } else {
                        BFB = handleInsert(point, polygon, BFB, new OctTree(xyPlaneRange.secondHalf(), xzPlaneRange.secondHalf(), yzPlaneRange.firstHalf()));
                    }
                }
            } else {
                if (point.x > yzPlaneRange.mid()) {
                    if (BBF == OctNode.Empty) {
                        BBF = new OctNode.PolygonNode(point, polygon);
                    } else {
                        BBF = handleInsert(point, polygon, BBF, new OctTree(xyPlaneRange.secondHalf(), xzPlaneRange.firstHalf(), yzPlaneRange.secondHalf()));
                    }
                } else {
                    if (BBB == OctNode.Empty) {
                        BBB = new OctNode.PolygonNode(point, polygon);
                    } else {
                        BBB =handleInsert(point, polygon, BBB, new OctTree(xyPlaneRange.secondHalf(), xzPlaneRange.firstHalf(), yzPlaneRange.firstHalf()));
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
