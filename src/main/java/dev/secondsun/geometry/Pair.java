package dev.secondsun.geometry;


public class Pair<T, T1> {
    public T first;
    public T1 second;
    public Pair(T first, T1 second){
        this.first = first;
        this.second = second;
    }



    public static <T, T1> Pair<T, T1> of(T first, T1 second) {
        return new Pair<>(first, second);
    }
}
