cmake_minimum_required (VERSION 3.10)
project (libMultiRound)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

set(CMAKE_AUTOMOC ON)
set(CMAKE_INCLUDE_CURRENT_DIR ON)
add_definitions(-DMAKE_MULTIPLAYERROUND_LIB)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../common
	${CMAKE_CURRENT_SOURCE_DIR}/../bcrypt
	${Qt6Widgets_INCLUDE_DIRS}
	${Qt6Network_INCLUDE_DIRS}
    ${Qt6Core_INCLUDE_DIRS})

set(MULTIROUND_HEADR 
    multiplayerround.h
    global/globaldata.h 
    global/globalgamedata.h 
    global/globaluserdata.h
    viewmodels/gameviewmodel.h
	viewmodels/loginviewmodel.h
	viewmodels/norobotviewmodel.h
	viewmodels/planespositionsviewmodel.h
	viewmodels/getopponentplanespositionsviewmodel.h
	viewmodels/newmoveviewmodel.h
	viewmodels/cancelroundviewmodel.h
	viewmodels/startnewroundviewmodel.h
	viewmodels/sendwinnerviewmodel.h
	viewmodels/unsentmovesviewmodel.h
    communicationobjects/basiscommobj.h
    communicationobjects/creategamecommobj.h
    communicationobjects/refreshgamestatuscommobj.h
    communicationobjects/connecttogamecommobj.h
    communicationobjects/logincommobj.h
    communicationobjects/registercommobj.h
    communicationobjects/norobotcommobj.h
    communicationobjects/sendplanepositionscommobj.h
    communicationobjects/acquireopponentpositionscommobj.h
    communicationobjects/sendmovecommobj.h
    communicationobjects/requestopponentmovescommobj.h
    communicationobjects/cancelroundcommobj.h
    communicationobjects/startnewroundcommobj.h
    communicationobjects/sendwinnercommobj.h
    communicationobjects/getserverversioncommobj.h
    communicationtools.h
    gameinfo.h)

set(MULTIROUND_SRCS 	
    multiplayerround.cpp
    communicationtools.cpp
    communicationobjects/basiscommobj.cpp
    communicationobjects/creategamecommobj.cpp
    communicationobjects/refreshgamestatuscommobj.cpp
    communicationobjects/connecttogamecommobj.cpp
    communicationobjects/logincommobj.cpp
    communicationobjects/registercommobj.cpp
    communicationobjects/norobotcommobj.cpp
    communicationobjects/sendplanepositionscommobj.cpp
    communicationobjects/acquireopponentpositionscommobj.cpp
    communicationobjects/sendmovecommobj.cpp
    communicationobjects/requestopponentmovescommobj.cpp
    communicationobjects/cancelroundcommobj.cpp
    communicationobjects/startnewroundcommobj.cpp
    communicationobjects/sendwinnercommobj.cpp
    communicationobjects/getserverversioncommobj.cpp
    )


add_library(libMultiRound SHARED
    ${MULTIROUND_SRCS}
    ${MULTIROUND_HEADR}
)

target_link_libraries(libMultiRound
	libCommon
    libbcrypt
	Qt6::Widgets
	Qt6::Network
    Qt6::Core)

set_target_properties(libMultiRound PROPERTIES
    AUTOMOC ON
)

if (UNIX)
install(TARGETS libMultiRound DESTINATION lib)
else (UNIX)
install(TARGETS libMultiRound DESTINATION bin)
endif (UNIX)
