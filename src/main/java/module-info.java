open module dev.secondsun.javaisoengine {

  requires transitive org.la4j;
  requires transitive java.desktop;
  requires java.base;
    requires com.google.gson;
    requires ejml.simple;
    exports dev.secondsun.game;
  exports dev.secondsun.geometry;
  exports dev.secondsun.geometry.playfield;
  exports dev.secondsun.util;
}
