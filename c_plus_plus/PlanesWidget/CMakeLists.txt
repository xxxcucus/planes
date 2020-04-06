cmake_minimum_required (VERSION 3.10)
project (PlanesWidget)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)


include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../common
	${Qt5Widgets_INCLUDE_DIRS})

#link_directories(
#	${CMAKE_CURRENT_BINARY_DIR}/../common/Release/
#)

set (WIDGET_HEADR 	
    baserenderarea.h
	buttons.h
 	choicedebugwidget.h
    debugrenderarea.h
	editplanescontrolwidget.h
	gamerenderarea.h
	gamestatswidget.h
	planeswview.h
	planeswwindow.h
	revertcomputerlogic.h)

set(WIDGET_SRCS 	
    baserenderarea.cpp
	buttons.cpp
 	choicedebugwidget.cpp
    debugrenderarea.cpp
	editplanescontrolwidget.cpp
	gamerenderarea.cpp
	gamestatswidget.cpp
	main.cpp
	planeswview.cpp
	planeswwindow.cpp
	revertcomputerlogic.cpp)

    qt5_wrap_ui(UI_HEADERS
        EditControl.ui
	GameForm.ui)

add_definitions(${Qt5Widgets_DEFINITIONS})
add_executable(PlanesWidget ${WIDGET_SRCS} ${UI_HEADERS} ${WIDGET_HEADR})
target_link_libraries(PlanesWidget 
	libCommon
	${Qt5Widgets_LIBRARIES})
qt5_use_modules(PlanesWidget Widgets)

install(TARGETS PlanesWidget DESTINATION bin)

