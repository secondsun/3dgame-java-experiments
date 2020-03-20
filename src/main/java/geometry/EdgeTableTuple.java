package geometry;

public class EdgeTableTuple {

    public int countEdgeBucket = 0; //no. of edgebuckets
    public EdgeBucket[] buckets = new EdgeBucket[10000];
    public final int polyId;


    public EdgeTableTuple(int polyId) {
        this.polyId = polyId;
        for (int i = 0; i< 10000; i++) {
            buckets[i] = new EdgeBucket(0,0,0, this.polyId);
        }
    }
}
