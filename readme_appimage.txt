1. Created appdir folder as instructed in Gitbub. 
https://github.com/probonopd/linuxdeployqt

2. Copy from planes  installation directory  with cp -a (to copy all links) the libs to lib folder
3. Copy from install with cp the entire contents of bin folder
4. Set LD_LIBRARY_PATH to Appdir/usr/lib
4. Run command

~/Downloads/linuxdeployqt-continuous-x86_64.AppImage Appdir/usr/share/applications/planes.desktop -qmake=/home/cristi/Qt/5.14.1/gcc_64/bin/qmake -appimage -qmldir=Appdir/usr/bin

