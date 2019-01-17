cmake_minimum_required (VERSION 3.10)
project (libCommon)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

if (With_Java)
include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	$ENV{JAVA_HOME}/include/
	$ENV{JAVA_HOME}/include/win32/
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
	computerlogic.h
	revertcomputerlogic.h
	guesspoint.h
	planeiterators.h
	coordinate2d.h
	vectoriterator.h
	planeorientationdata.h
	headdata.h)

if (With_Java)
	list(APPEND COMMON_HEADR com_planes_javafx_PlaneRoundJavaFx.h)
endif (With_Java)

set(COMMON_SRCS 	
	plane.cpp
	planegrid.cpp
	planeround.cpp
    computerlogic.cpp
	revertcomputerlogic.cpp
	guesspoint.cpp
	planeiterators.cpp
	gamestatistics.cpp
	coordinate2d.cpp
	planeorientationdata.cpp
	headdata.cpp)

if (With_Java)
	list(APPEND COMMON_SRCS com_planes_javafx_PlaneRoundJavaFx.cpp)
endif (With_Java)

add_library(libCommon SHARED ${COMMON_SRCS} ${COMMON_HEADR})
install(TARGETS libCommon DESTINATION bin)
