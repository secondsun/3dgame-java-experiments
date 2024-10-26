package dev.secondsun.geometry;

import org.ejml.simple.SimpleMatrix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransformationsTest {

  @Test
    public void testInvertViewTransform() {
      int camX=148, camY = 128, camZ = 148;
      var position = new Vertex(camX,camY,camZ);
      var lookAt = new Vertex(20,0,20);
      var forward = position.subtract(lookAt).normalize();
      var yAxis = new Vertex(0f,1f,0f);
      var right = yAxis.cross(forward).normalize();
      var up = forward.cross(right).normalize();

      var camera = new Camera(position, lookAt, up);
      var lookAtMatrix = camera.lookAt();

      Vertex original = new Vertex(30f,48f,50f);
      Vertex transformed = new Vertex(30f,48f,50f).transform(lookAtMatrix);

    var lookatUndo = new SimpleMatrix(lookAtMatrix).invert().toArray2();

      var lookAtUndoMatrix = new float[][] {
              {(float)lookatUndo[0][0],(float)lookatUndo[0][1],(float)lookatUndo[0][2],(float)lookatUndo[0][3]},
              {(float)lookatUndo[1][0],(float)lookatUndo[1][1],(float)lookatUndo[1][2],(float)lookatUndo[1][3]},
              {(float)lookatUndo[2][0],(float)lookatUndo[2][1],(float)lookatUndo[2][2],(float)lookatUndo[2][3]},
              {(float)lookatUndo[3][0],(float)lookatUndo[3][1],(float)lookatUndo[3][2],(float)lookatUndo[3][3]},
      };


      transformed = transformed.transform(lookAtUndoMatrix);

      assertEquals(original, transformed);


  }

}
