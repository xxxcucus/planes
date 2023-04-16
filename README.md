# planes
Variant of battleships game implemented with Qt, C++ for desktop and with Java and Kotlin for Android.

3 versions of the game of Planes are implemented with Qt: PlanesWidget, first naive version,
PlanesGraphicsScene an implementation using QGraphicsScene,
PlanesQML an implementation using QML. These use a game engine programmed in C++.

The Android application is currently developed in Kotlin.
The older version of the Android application in Java still exists in the repository for reference.

Starting with version 0.3.16 for the desktop version and version 0.4.0 for the Android app, a multiplayer module is included.

In the releases section you will find a detailed documentation of the C++ part of the project. (TheGameOfPlanes.pdf)

![alt text](https://github.com/xxxcucus/planes/blob/master/Screenshots/PlanesQML1.jpg)

# installation instructions

1. Go to the Releases page : https://github.com/xxxcucus/planes/releases

For Windows:
1. Unzip Planes.x.y.z_windows.zip file to a folder on your computer
2. In the bin folder of the unzipped folder run one of the binaries PlanesQML.exe, PlanesGraphicsScene.exe, PlanesWidget.exe

For Linux

The easiest way is to use the Planes.x.y.z-x86_64.AppImage or Planes_Multiplayer.x.y.z-x86_64.AppImage directly on your Linux machine.
Alternatively you could:

1. Download Planes.x.y.z_linux.tar.gz to a folder on your computer.
2. Extract the installation folder from the archive with tar -xvf Planes.x.y.z_linux.tar.gz
3. Execute one of the scripts PlanesWidget.sh, PlanesGraphicsScene.sh, PlanesQML.sh


For Android 
1. Download Planes.x.y.y_android.apk to the Download folder on your device.
2. With the file explorer navigate to the saved .apk and open it.

or get the app directly from Google Play Store: https://play.google.com/store/apps/details?id=com.planes.android

# compilation instructions

For the C++ projects PlanesWidget, PlanesGraphicsScene and PlanesQML, you will find the compilation and installation instructions in the Appendix of the pdf documentation - see the Releases page (available after Release 0.3.1)

# tests
The C++ projects include unittests. These are grouped under 2 CMake projects: singleroundtests and commobjtests. To run the tests under Linux, install the software and then run the script runTests.sh. To run the tests under Windows run the corresponding executables found in the bin folder of the installation. The Android project include the equivalent of the singleroundtests as well as instrumented tests for fragments. You can run them from inside the Android Studio.

# tutorials

1. Multi-Player Planes: https://youtu.be/mlSvZREBTwA
2. Single-Player Planes: https://youtu.be/N2Cg8eflCxM
3. Positioning of Planes: https://youtu.be/qgL0RdwqBRY
4. Guessing Planes Positions: https://youtu.be/CAxSPp2h_Vo

[![@xxxcucus's Holopin board](https://holopin.io/api/user/board?user=xxxcucus)](https://holopin.io/@xxxcucus)
