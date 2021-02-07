cmake_minimum_required (VERSION 3.10)
project (libMultiRound)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)


include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../common
	${CMAKE_CURRENT_SOURCE_DIR}/../bcrypt
	${Qt5Widgets_INCLUDE_DIRS}
	${Qt5Network_INCLUDE_DIRS})

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
    communicationobjects/basiscommobj.h
    communicationobjects/creategamecommobj.h
	
    communicationtools.h)

set(MULTIROUND_SRCS 	
    multiplayerround.cpp
    communicationtools.cpp
    communicationobjects/basiscommobj.cpp
    communicationobjects/creategamecommobj.cpp
    )


add_library(libMultiRound SHARED 
        ${MULTIROUND_SRCS}
        ${MULTIROUND_HEADR}
)

target_link_libraries(libMultiRound
	libCommon
	${Qt5Widgets_LIBRARIES}
	${Qt5Network_LIBRARIES})


if (UNIX)
install(TARGETS libMultiRound DESTINATION lib)
else (UNIX)
install(TARGETS libMultiRound DESTINATION bin)
endif (UNIX)
