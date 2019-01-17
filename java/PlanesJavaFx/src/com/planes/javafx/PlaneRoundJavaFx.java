package com.planes.javafx;

//JNI class interfacing to the c++ game engine
public class PlaneRoundJavaFx {
	static {
	      System.loadLibrary("libCommon"); // Load native library hello.dll (Windows) or libhello.so (Unixes)
	                                   //  at runtime
	                                   // This library contains a native method called sayHello()
	   }
	 
	// Declare an instance native method sayHello() which receives no parameter and returns void
	public native void createPlanesRound(); 
	
	public static void main(String[] args) {
		new PlaneRoundJavaFx().createPlanesRound();
	}
}
