cmake_minimum_required (VERSION 3.10)
project (libSingleRound)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

if (With_Java)
set(CMAKE_WINDOWS_EXPORT_ALL_SYMBOLS 1)
endif(With_Java)

if (With_Java)
include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	$ENV{JAVA_HOME}/include/
	$ENV{JAVA_HOME}/include/win32/
	$ENV{JAVA_HOME}/include/linux/
	)
else (With_Java)
include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	)
endif (With_Java)

set(COMMON_HEADR 
	plane.h
	planegrid.h
	planeround.h
	abstractplaneround.h
	computerlogic.h
	guesspoint.h
	planeiterators.h
	planepointiterator.h
	planeintersectingpointiterator.h
	coordinate2d.h
	vectoriterator.h
	planeorientationdata.h
	planeroundoptions.h
	headdata.h
	playerguessreaction.h)

set(COMMON_SRCS 	
	plane.cpp
	planegrid.cpp
	planeround.cpp
	abstractplaneround.cpp
    computerlogic.cpp
	guesspoint.cpp
	planeiterators.cpp
	planepointiterator.cpp
	planeintersectingpointiterator.cpp
	gamestatistics.cpp
	coordinate2d.cpp
	planeorientationdata.cpp
	planeroundoptions.cpp
	headdata.cpp)


add_library(libSingleRound SHARED ${COMMON_SRCS} ${COMMON_HEADR})


if (UNIX)
install(TARGETS libSingleRound DESTINATION lib)
else (UNIX)
install(TARGETS libSingleRound DESTINATION bin)
endif (UNIX)
