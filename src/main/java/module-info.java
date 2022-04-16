open module dev.secondsun.javaisoengine {

  requires transitive org.la4j;
  requires transitive java.desktop;
  requires java.base;
  exports dev.secondsun.game;
  exports dev.secondsun.geometry;
  exports dev.secondsun.geometry.playfield;
  exports dev.secondsun.util;
}
