package geometry;

import java.util.Objects;

public class EdgeEntry {

  public int startX;
  public int endX;
  public float z;
  public float textureVectorX;
  public float textureVectorY;
  public float textureVectorLength;
  public int textureId;

  public EdgeEntry(int startX, int endX,float z, float textureVectorX, float textureVectorY,
      float textureVectorLength, int textureId) {
    if (endX - startX < 0) {
      System.err.println("negative length startX:"+startX + " endX:" + endX);
      this.startX = -1;
      this.endX = -1;
    } else {
      this.startX = startX;
      this.endX = endX;
    }
    this.z = z;
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
            ", z=" + z +
            ", textureVectorX=" + textureVectorX +
            ", textureVectorY=" + textureVectorY +
            ", textureVectorLength=" + textureVectorLength +
            ", textureId=" + textureId +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EdgeEntry edgeEntry = (EdgeEntry) o;
    return startX == edgeEntry.startX &&
            endX == edgeEntry.endX &&
            Float.compare(edgeEntry.z, z) == 0 &&
            Float.compare(edgeEntry.textureVectorX, textureVectorX) == 0 &&
            Float.compare(edgeEntry.textureVectorY, textureVectorY) == 0 &&
            Float.compare(edgeEntry.textureVectorLength, textureVectorLength) == 0 &&
            textureId == edgeEntry.textureId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(startX, endX, z, textureVectorX, textureVectorY, textureVectorLength, textureId);
  }
}
