cmake_minimum_required (VERSION 2.6)
project (Planes)

set(CMAKE_AUTOMOC ON)
set(CMAKE_INCLUDE_CURRENT_DIR ON)

FIND_PACKAGE(Qt5Widgets)

INCLUDE_DIRECTORIES(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/common
	${CMAKE_CURRENT_SOURCE_DIR}/PlanesWidget
	${CMAKE_CURRENT_SOURCE_DIR}/PlanesGraphicsScene
	${Qt5Widgets_INCLUDE_DIRS})

MESSAGE("Source dir: " ${CMAKE_CURRENT_SOURCE_DIR})

qt5_wrap_ui(UI_HEADERS
            EditControl.ui
	    GameControl.ui)

add_definitions(${Qt5Widgets_DEFINITIONS})

add_executable(Planes 
        PlanesWidget/baserenderarea.cpp
	PlanesWidget/buttons.cpp
 	PlanesWidget/choicedebugwidget.cpp
        common/computerlogic.cpp
        PlanesWidget/debugrenderarea.cpp
	PlanesWidget/editplanescontrolwidget.cpp
	PlanesWidget/gamerenderarea.cpp
	PlanesWidget/gamestatswidget.cpp
	PlanesWidget/main.cpp
	common/plane.cpp
	common/planegame.cpp
	common/planegrid.cpp
	common/planesmodel.cpp
	PlanesWidget/planeswview.cpp
	PlanesWidget/planeswwindow.cpp)

target_link_libraries(Planes ${Qt5Widgets_LIBRARIES})

qt5_use_modules(Planes Widgets)
