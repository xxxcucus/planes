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
	leftpane/leftpane.cpp
	leftpane/gamestatsframe.cpp
	leftpane/scoreframe.cpp
    rightpane/rightpane.cpp
    rightpane/account/accountwidget.cpp
    rightpane/account/clickablelabel.cpp
    rightpane/account/loginregisterform.cpp
    rightpane/account/mainaccountwidget.cpp
    rightpane/account/norobotwidget.cpp
    rightpane/account/userprofileframe.cpp
    rightpane/boards/computerboard.cpp
	rightpane/boards/customgraphicsscene.cpp
	rightpane/boards/customgraphicsview.cpp
	rightpane/boards/genericboard.cpp
	rightpane/boards/gridsquare.cpp
	rightpane/boards/playareagridsquare.cpp
	rightpane/boards/playerboard.cpp
	rightpane/game/gamewidget.cpp
	rightpane/game/gameendwidget.cpp
	rightpane/game/gamestatuswidget.cpp
	rightpane/game/creategamewidget.cpp
	rightpane/options/gamedifficultyoptions.cpp
	rightpane/options/optionswindow.cpp
	multiplayerround.cpp
	communicationtools.cpp
	main.cpp
    animatedtextitem.cpp
    customhorizlayout.cpp
	planesgsview.cpp
	planesgswindow.cpp
)

set(GS_HEADR
	leftpane/leftpane.h
	leftpane/gamestatsframe.h
	leftpane/scoreframe.h
    rightpane/rightpane.h
    rightpane/account/accountwidget.h
    rightpane/account/clickablelabel.h
    rightpane/account/loginregisterform.h
    rightpane/account/mainaccountwidget.h
    rightpane/account/norobotwidget.h
    rightpane/account/userprofileframe.h
    rightpane/boards/computerboard.h
	rightpane/boards/customgraphicsscene.h
	rightpane/boards/customgraphicsview.h
	rightpane/boards/genericboard.h
	rightpane/boards/gridsquare.h
	rightpane/boards/playareagridsquare.h
	rightpane/boards/playerboard.h
	rightpane/game/gamewidget.h
	rightpane/game/gameendwidget.h
	rightpane/game/gamestatuswidget.h
	rightpane/game/creategamewidget.h
	rightpane/options/gamedifficultyoptions.h
	rightpane/options/optionswindow.h
	viewmodels/gameviewmodel.h
	viewmodels/loginviewmodel.h
	viewmodels/norobotviewmodel.h
	viewmodels/planespositionsviewmodel.h
	viewmodels/getopponentplanespositionsviewmodel.h
	global/globaluserdata.h
	global/globalgamedata.h
	global/globaldata.h
	multiplayerround.h
	customhorizlayout.h
	animatedtextitem.h
	planesgsview.h
	planesgswindow.h	
	communicationtools.h
	gameinfo.h
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
