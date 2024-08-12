package dev.secondsun.util;

public record Range(float start, float end) {

    public float mid() {
        return (end + start)/2f;
    }

    public Range firstHalf() {
        return new Range(start(),mid());
    }

    public Range secondHalf() {
        return new Range(mid(),end());
    }
}
