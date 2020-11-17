cmake_minimum_required (VERSION 3.10)
project (PlanesGraphicsScene)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../common
	${CMAKE_CURRENT_SOURCE_DIR}/../bcrypt
	${Qt5Widgets_INCLUDE_DIRS})

#link_directories(
#	${CMAKE_CURRENT_BINARY_DIR}/../common/Release/
#)

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
	accountwidget.cpp
	loginregisterform.cpp
	mainaccountwidget.cpp
	norobotwidget.cpp
	userprofileframe.cpp
	clickablelabel.cpp
	optionswindow.cpp
	gamedifficultyoptions.cpp
	communicationtools.cpp
	main.cpp
)

set(GS_HEADR 	
	animatedtextitem.h
	computerboard.h
	customgraphicsscene.h
	customgraphicsview.h
	customhorizlayout.h
	gamestatsframe.h
	genericboard.h
	gridsquare.h
	leftpane.h
	planesgsview.h
	planesgswindow.h
	playareagridsquare.h
	playerboard.h
	rightpane.h
	scoreframe.h
	accountwidget.h
	loginregisterform.h
	mainaccountwidget.h
	norobotwidget.h
	userprofileframe.h
	clickablelabel.h
	optionswindow.h
	userdata.h
	logindata.h
	norobotdata.h
	communicationtools.h
	gamedifficultyoptions.h
)

add_executable(PlanesGraphicsScene 
	${GS_SRCS}
	${GS_HEADR}
	resources.qrc)
target_link_libraries(PlanesGraphicsScene
	libCommon
	libbcrypt
	${Qt5Widgets_LIBRARIES}
	${Qt5Network_LIBRARIES})
qt5_use_modules(PlanesGraphicsScene Widgets Network)

install(TARGETS PlanesGraphicsScene DESTINATION bin)
