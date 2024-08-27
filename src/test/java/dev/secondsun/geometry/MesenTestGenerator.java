package dev.secondsun.geometry;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.ceil;
import static java.lang.Math.round;

/**
 * This test class generates test data for tests to be run on the X-GSU project with the Mesen2 emulator
 */
public class MesenTestGenerator {
    @Test
    public void createLookAtMatrixTests() {

        //The camera struct in X-GSU is 18 bytes
        //Each struct
        //   .struct camera
        //      eye .tag vector3
        //      lookAt .tag vector3
        //      up .tag vector3
        //   .endstruct

        List<Camera> cameraInput = new ArrayList<>();
        List<float[][]> lookAtOutputs = new ArrayList<>();


        var camX = -64;
        var camY = 64;
        var camZ = -64;


        for (int x = 0; x < 360; x+=30) {
            camX= (int) (-256*Math.sin(Math.toRadians(x)));
            //camY= (int) (256*Math.cos(Math.toRadians(theta)));
            camZ= (int) (-256*Math.cos(Math.toRadians(x)));
            var position = new Vertex(camX,camY,camZ);
            var lookAt = new Vertex(0,0,0);
            var forward = position.subtract(lookAt).normalize();
            var yAxis = new Vertex(0f,1f,0f);
            var right = yAxis.cross(forward).normalize();
            var up = forward.cross(right).normalize();

            var camera = new Camera(position, lookAt, up);
            cameraInput.add(camera);
            lookAtOutputs.add(camera.lookAt());
        }

        //Convert objects to fixed 8.8
        List<Integer> convertedInputs = convert(cameraInput);
        List<Integer> convertLookAt = convertLookAt(lookAtOutputs);

        System.out.printf("input = {%s}\n", convertedInputs.stream().map(Integer::toHexString).map(it->"0x"+it).collect(Collectors.joining(",")));

        System.out.printf("expected = {%s}\n", convertLookAt.stream().map(Integer::toHexString).map(it->"0x"+it).collect(Collectors.joining(",")));




    }

    private List<Integer> convertLookAt(List<float[][]> lookAtOutputs) {
        return lookAtOutputs.stream().flatMap( output ->
                    Stream.of(round(output[0][0]*256) & 0xFFFF,
                            round(output[0][1]*256) & 0xFFFF,
                            round(output[0][2]*256) & 0xFFFF,
                            0,
                            round(output[1][0]*256) & 0xFFFF,
                            round(output[1][1]*256) & 0xFFFF,
                            round(output[1][2]*256) & 0xFFFF,
                            0,
                            round(output[2][0]*256) & 0xFFFF,
                            round(output[2][1]*256) & 0xFFFF,
                            round(output[2][2]*256) & 0xFFFF,
                            0,
                            0,0,0,0x100
                              )
        ).toList();
    }

    private List<Integer> convert(List<Camera> cameraInput) {
                return cameraInput.stream().flatMap(camera -> {
                        return Stream.of(round(camera.getFrom().x * 256) & 0xFFFF,
                        round(camera.getFrom().y * 256)& 0xFFFF,
                        round(camera.getFrom().z * 256)& 0xFFFF,
                        round(camera.getTo().x * 256)& 0xFFFF,
                        round(camera.getTo().y * 256)& 0xFFFF,
                        round(camera.getTo().z * 256)& 0xFFFF,
                        round(camera.getUp().x * 256)& 0xFFFF,
                        round(camera.getUp().y * 256)& 0xFFFF,
                        round(camera.getUp().z * 256)& 0xFFFF);
            }).toList();
    }
}
