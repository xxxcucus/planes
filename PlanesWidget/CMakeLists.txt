cmake_minimum_required (VERSION 2.6)
project (PlanesWidget)

cmake_policy(SET CMP0020 NEW)
find_package(Qt5Widgets)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../
	${CMAKE_CURRENT_SOURCE_DIR}/../common
	${Qt5Widgets_INCLUDE_DIRS})

set(WIDGET_SRCS 	../common/plane.cpp
	../common/planegrid.cpp
	../common/planesmodel.cpp
	../common/planeround.cpp
    ../common/computerlogic.cpp
	../common/guesspoint.cpp
	../common/planeiterators.cpp
	../common/gamestatistics.cpp
    baserenderarea.cpp
	buttons.cpp
 	choicedebugwidget.cpp
    debugrenderarea.cpp
	editplanescontrolwidget.cpp
	gamerenderarea.cpp
	gamestatswidget.cpp
	main.cpp
	planeswview.cpp
	planeswwindow.cpp)
	

qt5_wrap_ui(UI_HEADERS
        PlanesWidget/EditControl.ui
	    PlanesWidget/GameControl.ui)

add_definitions(${Qt5Widgets_DEFINITIONS})
add_executable(PlanesWidget ${WIDGET_SRCS})
target_link_libraries(PlanesWidget ${Qt5Widgets_LIBRARIES})
qt5_use_modules(PlanesWidget Widgets)

