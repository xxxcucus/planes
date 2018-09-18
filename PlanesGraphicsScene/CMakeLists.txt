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

install(TARGETS PlanesGraphicsScene DESTINATION bin)