package geometry;

public class EdgeTableTuple {

    public int countEdgeBucket = 0; //no. of edgebuckets
    public EdgeBucket[] buckets = new EdgeBucket[10000];



    public EdgeTableTuple() {
        for (int i = 0; i< 10000; i++) {
            buckets[i] = new EdgeBucket(0,0,0);
        }
    }
}
