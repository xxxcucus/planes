cmake_minimum_required (VERSION 3.10)
project (PlanesGraphicsScene)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../singleround
	${CMAKE_CURRENT_SOURCE_DIR}/../multiround/
	${CMAKE_CURRENT_SOURCE_DIR}/../bcrypt
	${CMAKE_CURRENT_SOURCE_DIR}/../stomp
	${Qt6Widgets_INCLUDE_DIRS}
	${Qt6Core_INCLUDE_DIRS}
	#${Qt6WebSockets_INCLUDE_DIRS}
	${Qt6Sql_INCLUDE_DIRS})

set(GS_SRCS 	
	leftpane/leftpane.cpp
	leftpane/round/gamestatsframe.cpp
	leftpane/round/playroundwidget.cpp
	leftpane/boardediting/boardeditingwidget.cpp
	leftpane/startnewround/startnewroundwidget.cpp
	leftpane/startnewround/scoreframe.cpp
    leftpane/account/clickablelabel.cpp
    leftpane/account/loginregisterform.cpp
    leftpane/account/mainaccountwidget.cpp
    leftpane/account/norobotwidget.cpp
	leftpane/account/norobotdialog.cpp
    leftpane/account/userprofileframe.cpp
    leftpane/account/userprofileform.cpp
	leftpane/game/gamewidget.cpp
	leftpane/game/gameendwidget.cpp
	leftpane/game/gamestatuswidget.cpp
	leftpane/game/creategamewidget.cpp
    rightpane/rightpane.cpp
    rightpane/boards/computerboard.cpp
	rightpane/boards/customgraphicsscene.cpp
	rightpane/boards/customgraphicsview.cpp
	rightpane/boards/genericboard.cpp
	rightpane/boards/gridsquare.cpp
	rightpane/boards/playareagridsquare.cpp
	rightpane/boards/playerboard.cpp
	rightpane/options/gamedifficultyoptions.cpp
	rightpane/options/serversettingsoptions.cpp
	rightpane/options/optionswindow.cpp
	rightpane/chat/playerslistwidget.cpp
	rightpane/chat/chatwidget.cpp
	rightpane/chat/userwithstatuswidget.cpp
	main.cpp
    animatedtextitem.cpp
    customhorizlayout.cpp
	planesgsview.cpp
	planesgswindow.cpp
	statusbarwidget.cpp
	databaseservice.cpp
)

set(GS_HEADR
	leftpane/leftpane.h
	leftpane/round/gamestatsframe.h
	leftpane/round/playroundwidget.h
	leftpane/boardediting/boardeditingwidget.h
	leftpane/startnewround/startnewroundwidget.h
	leftpane/startnewround/scoreframe.h
    leftpane/account/clickablelabel.h
    leftpane/account/loginregisterform.h
    leftpane/account/userprofileform.h
    leftpane/account/mainaccountwidget.h
    leftpane/account/norobotwidget.h
    leftpane/account/userprofileframe.h
	leftpane/account/norobotdialog.h
	leftpane/game/gamewidget.h
	leftpane/game/gameendwidget.h
	leftpane/game/gamestatuswidget.h
	leftpane/game/creategamewidget.h
	rightpane/rightpane.h
	rightpane/boards/computerboard.h
	rightpane/boards/customgraphicsscene.h
	rightpane/boards/customgraphicsview.h
	rightpane/boards/genericboard.h
	rightpane/boards/gridsquare.h
	rightpane/boards/playareagridsquare.h
	rightpane/boards/playerboard.h
	rightpane/options/gamedifficultyoptions.h
	rightpane/options/serversettingsoptions.h
	rightpane/options/optionswindow.h
	rightpane/chat/playerslistwidget.h
	rightpane/chat/chatwidget.h
	rightpane/chat/userwithstatuswidget.h
	customhorizlayout.h
	animatedtextitem.h
	planesgsview.h
	planesgswindow.h
	statusbarwidget.h
	databaseservice.h
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
	Qt6::Network
	Qt6::Sql)


install(TARGETS PlanesGraphicsScene DESTINATION bin)
