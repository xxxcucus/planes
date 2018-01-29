cmake_minimum_required (VERSION 2.6)
project (PlanesGraphicsScene)

cmake_policy(SET CMP0020 NEW)
include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../common
	${Qt5Widgets_INCLUDE_DIRS})

set(GS_SRCS 	
	animatedtextitem.cpp
	computerboard.cpp
	customgraphicsscene.cpp
	customgraphicsview.cpp
	customhorizlayout.cpp
	gamestatsframe.cpp
	genericboard.cpp
	gridsquare.cpp
	leftpane.cpp
	planesgsview.cpp
	planesgswindow.cpp
	playareagridsquare.cpp
	playerboard.cpp
	rightpane.cpp
	scoreframe.cpp
	main.cpp
)

add_executable(PlanesGraphicsScene 
	${GS_SRCS}
	resources.qrc)
target_link_libraries(PlanesGraphicsScene
	libCommon
	${Qt5Widgets_LIBRARIES})
qt5_use_modules(PlanesGraphicsScene Widgets)

#install (FILES "c:/qt/tools/mingw530_32/bin/LIBGCC_S_DW2-1.DLL"
#	"c:/qt/tools/mingw530_32/bin/LIBSTDC++-6.DLL"
#	"c:/qt/tools/mingw530_32/bin/LIBWINPTHREAD-1.DLL"
#	"c:/qt/5.7/mingw53_32/bin/QT5CORE.DLL"
#	"c:/qt/5.7/mingw53_32/bin/QT5GUI.DLL"
#	"c:/qt/5.7/mingw53_32/bin/QT5WIDGETS.DLL" 
#	"c:/qt/5.7/mingw53_32/bin/libEGL.DLL"
#	"c:/qt/5.7/mingw53_32/bin/libGLESv2.DLL"
#	 DESTINATION bin)

#install (FILES "c:/qt/5.7/mingw53_32/plugins/platforms/qwindows.dll"
#	DESTINATION bin/platforms)

#install(TARGETS PlanesGraphicsScene RUNTIME DESTINATION bin)
#set(CPACK_SOURCE_GENERATOR "ZIP")
#set(CPACK_GENERATOR "NSIS")

#set(CPACK_PACKAGE_DESCRIPTION "Board Game")
#set(CPACK_PACKAGE_VENDOR "Cristian Cucu")
#set(CPACK_PACKAGE_VERSION "0.1")
#set(CPACK_PACKAGE_SECTION "Games")
#include(CPack)