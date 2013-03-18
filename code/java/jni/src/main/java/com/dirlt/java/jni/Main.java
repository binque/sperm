package com.dirlt.java.jni;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 3/18/13
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static native void hello();

    public static void main(String[] args) {
        System.loadLibrary("hello");
        hello();
    }
}
