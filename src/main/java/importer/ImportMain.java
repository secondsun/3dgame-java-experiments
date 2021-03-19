package importer;

import java.io.IOException;

public class ImportMain {

    public static void main(String... args) throws IOException {
        var mapStream = ImportMain.class.getClassLoader().getResourceAsStream("MAP056.48");
        var bytes = mapStream.readAllBytes();

        var meshPointer = readInt(bytes, 0x40);
        System.out.println(meshPointer &0x0FF);
    }

    private static int readInt(byte[] bytes, int i) {
        return  bytes[i] | (bytes[i+1] << 8) |(bytes[i+2] << 16) |(bytes[i+3] << 24);
    }

}
