# Simple isometric 3D engine in Java

This is an implementation of a basic 3D engine for learning and experimentation.

# Usage

To include this library in your project use the following maven dependency : 

```
<dependency>
    <groupId>dev.secondsun</groupId>
    <artifactId>java-isoengine</artifactId>
    <version>0.3</version>
</dependency>
```

If you are using jpms modules :

```
    requires dev.secondsun.javaisoengine;
```

# Deployment

To deploy this artifact to Maven Central 
`mvn deploy -Prelease` with your credentials set for ossrh / Sonatype. 