package geometry;

public class EdgeBucket {
    public int ymax;//max y-coordinate of edge
    public float xofymin; //x-coordinate of lowest edge point updated only in aet
    public float slopeinverse;
    public int color;

    public EdgeBucket(int ymax, float xofymin, float slopeinverse) {
        this.ymax = ymax;
        this.xofymin = xofymin;
        this.slopeinverse = slopeinverse;
    }
}
