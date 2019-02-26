# planes
Variant of battleships game implemented with Qt, C++ and with JavaFx and Android(under development).

3 versions of the game of Planes are implemented with Qt: PlanesWidget, first naive version,
PlanesGraphicsScene an implementation using QGraphicsScene,
PlanesQML an implementation using QML. These use a game engine programmed in C++.

2 other version are based on Java, but using the same game C++ engine via Java Native Interface.
One GUI uses JavaFx and the other (currently under development) Android.

![alt text](https://github.com/xxxcucus/planes/blob/master/Book/BoardWithPlanes.png)

# installation instructions

1. Go to the Releases page : https://github.com/xxxcucus/planes/releases

For Windows:
1. Unzip Planes.x.y.z_windows.zip file to a folder on your computer
2. In the bin folder of the unzipped folder run one of the binaries PlanesQML.exe, PlanesGraphicsScene.exe, PlanesWidget.exe

For Linux
1. Download Planes.x.y.z_linux.tar.gz to a folder on your computer.
2. Extract the installation folder from the archive with tar -xvf Planes.x.y.z_linux.tar.gz
3. Execute one of the scripts PlanesWidget.sh, PlanesGraphicsScene.sh, PlanesQML.sh

For Android (still under development)
1. Download Planes.x.y.y_android.apk to the Download folder on your device.
2. With the file explorer navigate to the saved .apk and open it.
3. When the operating system asks allow the application from unknown source to be installed.
