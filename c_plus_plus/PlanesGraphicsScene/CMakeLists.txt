cmake_minimum_required (VERSION 3.10)
project (PlanesGraphicsScene)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../singleround
	${CMAKE_CURRENT_SOURCE_DIR}/../multiround/
	${CMAKE_CURRENT_SOURCE_DIR}/../bcrypt
	${Qt6Widgets_INCLUDE_DIRS}
	${Qt6Core_INCLUDE_DIRS})

#link_directories(
#	${CMAKE_CURRENT_BINARY_DIR}/../common/Release/
#)

set(GS_SRCS 	
	leftpane/leftpane.cpp
	leftpane/round/gamestatsframe.cpp
	leftpane/round/playroundwidget.cpp
	leftpane/boardediting/boardeditingwidget.cpp
	leftpane/startnewround/startnewroundwidget.cpp
	leftpane/startnewround/scoreframe.cpp
    rightpane/rightpane.cpp
    leftpane/account/clickablelabel.cpp
    leftpane/account/loginregisterform.cpp
    leftpane/account/mainaccountwidget.cpp
    leftpane/account/norobotwidget.cpp
	leftpane/account/norobotdialog.cpp
    leftpane/account/userprofileframe.cpp
    rightpane/boards/computerboard.cpp
	rightpane/boards/customgraphicsscene.cpp
	rightpane/boards/customgraphicsview.cpp
	rightpane/boards/genericboard.cpp
	rightpane/boards/gridsquare.cpp
	rightpane/boards/playareagridsquare.cpp
	rightpane/boards/playerboard.cpp
	leftpane/game/gamewidget.cpp
	leftpane/game/gameendwidget.cpp
	leftpane/game/gamestatuswidget.cpp
	leftpane/game/creategamewidget.cpp
	rightpane/options/gamedifficultyoptions.cpp
	rightpane/options/serversettingsoptions.cpp
	rightpane/options/optionswindow.cpp
	main.cpp
    animatedtextitem.cpp
    customhorizlayout.cpp
	planesgsview.cpp
	planesgswindow.cpp
	statusbarwidget.cpp
)

set(GS_HEADR
	leftpane/leftpane.h
	leftpane/round/gamestatsframe.h
	leftpane/round/playroundwidget.h
	leftpane/boardediting/boardeditingwidget.h
	leftpane/startnewround/startnewroundwidget.h
	leftpane/startnewround/scoreframe.h
    rightpane/rightpane.h
    leftpane/account/clickablelabel.h
    leftpane/account/loginregisterform.h
    leftpane/account/mainaccountwidget.h
    leftpane/account/norobotwidget.h
    leftpane/account/userprofileframe.h
	leftpane/account/norobotdialog.h
    rightpane/boards/computerboard.h
	rightpane/boards/customgraphicsscene.h
	rightpane/boards/customgraphicsview.h
	rightpane/boards/genericboard.h
	rightpane/boards/gridsquare.h
	rightpane/boards/playareagridsquare.h
	rightpane/boards/playerboard.h
	leftpane/game/gamewidget.h
	leftpane/game/gameendwidget.h
	leftpane/game/gamestatuswidget.h
	leftpane/game/creategamewidget.h
	rightpane/options/gamedifficultyoptions.h
	rightpane/options/serversettingsoptions.h
	rightpane/options/optionswindow.h
	customhorizlayout.h
	animatedtextitem.h
	planesgsview.h
	planesgswindow.h
	statusbarwidget.h
)

add_executable(PlanesGraphicsScene 
	${GS_SRCS}
	${GS_HEADR}
	resources.qrc)
target_link_libraries(PlanesGraphicsScene
	libSingleRound
	libMultiRound
	libbcrypt
	Qt6::Widgets
	Qt6::Core
	Qt6::Network)


install(TARGETS PlanesGraphicsScene DESTINATION bin)
