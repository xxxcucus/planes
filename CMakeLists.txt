cmake_minimum_required (VERSION 2.6)
project (Planes)

set(CMAKE_AUTOMOC ON)
set(CMAKE_INCLUDE_CURRENT_DIR ON)

FIND_PACKAGE(Qt5Widgets)

INCLUDE_DIRECTORIES(
	${CMAKE_CURRENT_SOURCE_DIR}
	${Qt5Widgets_INCLUDE_DIRS})

qt5_wrap_ui(UI_HEADERS
            EditControl.ui
	    GameControl.ui)

add_definitions(${Qt5Widgets_DEFINITIONS})

add_executable(Planes 
        baserenderarea.cpp
	buttons.cpp
 	choicedebugwidget.cpp
        computerlogic.cpp
        debugrenderarea.cpp
	editplanescontrolwidget.cpp
	gamerenderarea.cpp
	gamestatswidget.cpp
	main.cpp
	plane.cpp
	planegame.cpp
	planegrid.cpp
	planesmodel.cpp
	planesview.cpp
	planeswindow.cpp)

target_link_libraries(Planes ${Qt5Widgets_LIBRARIES})

qt5_use_modules(Planes Widgets)
