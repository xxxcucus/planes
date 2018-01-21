cmake_minimum_required (VERSION 2.6)
project (Planes)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTOUIC ON)
set(CMAKE_INCLUDE_CURRENT_DIR ON)
set(CMAKE_AUTORCC ON)
cmake_policy(SET CMP0020 NEW)


find_package(Qt5Widgets)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/common
	${CMAKE_CURRENT_SOURCE_DIR}/PlanesWidget
	${CMAKE_CURRENT_SOURCE_DIR}/PlanesGraphicsScene
	${Qt5Widgets_INCLUDE_DIRS})

message("Source dir: " ${CMAKE_CURRENT_SOURCE_DIR})
message(${Qt5Widgets_LIBRARIES})
message(${Qt5Widgets_INCLUDE_DIRS})
message("Qt dir: " ${QTDIR})

qt5_wrap_ui(UI_HEADERS
            PlanesWidget/EditControl.ui
	    PlanesWidget/GameControl.ui)

add_definitions(${Qt5Widgets_DEFINITIONS})

add_executable(PlanesWidget 
	common/plane.cpp
	common/planegrid.cpp
	common/planesmodel.cpp
	common/planeround.cpp
    common/computerlogic.cpp
	common/guesspoint.cpp
	common/planeiterators.cpp
	common/gamestatistics.cpp
    PlanesWidget/baserenderarea.cpp
	PlanesWidget/buttons.cpp
 	PlanesWidget/choicedebugwidget.cpp
    PlanesWidget/debugrenderarea.cpp
	PlanesWidget/editplanescontrolwidget.cpp
	PlanesWidget/gamerenderarea.cpp
	PlanesWidget/gamestatswidget.cpp
	PlanesWidget/main.cpp
	PlanesWidget/planeswview.cpp
	PlanesWidget/planeswwindow.cpp)

target_link_libraries(PlanesWidget ${Qt5Widgets_LIBRARIES})

qt5_use_modules(PlanesWidget Widgets)

add_executable(PlanesGraphicsScene 
	common/plane.cpp
	common/planegrid.cpp
	common/planesmodel.cpp
	common/planeround.cpp
    common/computerlogic.cpp
	common/guesspoint.cpp
	common/planeiterators.cpp
	common/gamestatistics.cpp
	PlanesGraphicsScene/animatedtextitem.cpp
	PlanesGraphicsScene/computerboard.cpp
	PlanesGraphicsScene/customgraphicsscene.cpp
	PlanesGraphicsScene/customgraphicsview.cpp
	PlanesGraphicsScene/customhorizlayout.cpp
	PlanesGraphicsScene/gamestatsframe.cpp
	PlanesGraphicsScene/genericboard.cpp
	PlanesGraphicsScene/gridsquare.cpp
	PlanesGraphicsScene/leftpane.cpp
	PlanesGraphicsScene/planesgsview.cpp
	PlanesGraphicsScene/planesgswindow.cpp
	PlanesGraphicsScene/playareagridsquare.cpp
	PlanesGraphicsScene/playerboard.cpp
	PlanesGraphicsScene/rightpane.cpp
	PlanesGraphicsScene/scoreframe.cpp
	PlanesGraphicsScene/main.cpp
	PlanesGraphicsScene/resources.qrc)

target_link_libraries(PlanesGraphicsScene ${Qt5Widgets_LIBRARIES})

qt5_use_modules(PlanesGraphicsScene Widgets)

install (FILES "c:/qt/tools/mingw530_32/bin/LIBGCC_S_DW2-1.DLL"
	"c:/qt/tools/mingw530_32/bin/LIBSTDC++-6.DLL"
	"c:/qt/tools/mingw530_32/bin/LIBWINPTHREAD-1.DLL"
	"c:/qt/5.7/mingw53_32/bin/QT5CORE.DLL"
	"c:/qt/5.7/mingw53_32/bin/QT5GUI.DLL"
	"c:/qt/5.7/mingw53_32/bin/QT5WIDGETS.DLL" 
	"c:/qt/5.7/mingw53_32/bin/libEGL.DLL"
	"c:/qt/5.7/mingw53_32/bin/libGLESv2.DLL"
	 DESTINATION bin)

install (FILES "c:/qt/5.7/mingw53_32/plugins/platforms/qwindows.dll"
	DESTINATION bin/platforms)

install(TARGETS PlanesGraphicsScene RUNTIME DESTINATION bin)
set(CPACK_SOURCE_GENERATOR "ZIP")
set(CPACK_GENERATOR "NSIS")

set(CPACK_PACKAGE_DESCRIPTION "Board Game")
set(CPACK_PACKAGE_VENDOR "Cristian Cucu")
set(CPACK_PACKAGE_VERSION "0.1")
set(CPACK_PACKAGE_SECTION "Games")
include(CPack)