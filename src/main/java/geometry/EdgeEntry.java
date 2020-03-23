package geometry;

public class EdgeEntry {

  public int startX;
  public int endX;

  public float textureVectorX;
  public float textureVectorY;
  public float textureVectorLength;
  public int textureId;

  public EdgeEntry(int startX, int endX, float textureVectorX, float textureVectorY,
      float textureVectorLength, int textureId) {
    if (endX-startX > 15) {
     // throw new RuntimeException("Coords too long " + startX + "->" + endX);
    }
    this.startX = startX;
    this.endX = endX;
    this.textureVectorX = textureVectorX;
    this.textureVectorY = textureVectorY;
    this.textureVectorLength = textureVectorLength;
    this.textureId = textureId;
  }

  @Override
  public String toString() {
    return "EdgeEntry{" +
        "startX=" + startX +
        ", endX=" + endX +
        ", textureVectorX=" + textureVectorX +
        ", textureVectorY=" + textureVectorY +
        ", textureVectorLength=" + textureVectorLength +
        ", textureId=" + textureId +
        '}';
  }
}
