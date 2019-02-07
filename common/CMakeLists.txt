cmake_minimum_required (VERSION 3.10)
project (libCommon)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

#clang means android
if (CMAKE_ANDROID_API)
set(CMAKE_WINDOWS_EXPORT_ALL_SYMBOLS 1)
endif(CMAKE_ANDROID_API)

if (With_Java OR CMAKE_ANDROID_API)
include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	$ENV{JAVA_HOME}/include/
	$ENV{JAVA_HOME}/include/win32/
	)
else (With_Java OR CMAKE_ANDROID_API)
include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	)
endif (With_Java OR CMAKE_ANDROID_API)

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

if (With_Java OR CMAKE_ANDROID_API)
	list(APPEND COMMON_HEADR com_planes_javafx_PlaneRoundJavaFx.h)
endif (With_Java OR CMAKE_ANDROID_API)

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

if (With_Java OR CMAKE_ANDROID_API)
	list(APPEND COMMON_SRCS com_planes_javafx_PlaneRoundJavaFx.cpp)
endif (With_Java OR CMAKE_ANDROID_API)


if (CMAKE_ANDROID_API)
	add_library(libCommon SHARED ${COMMON_SRCS} ${COMMON_HEADR})
	install(TARGETS libCommon DESTINATION bin)
else (CMAKE_ANDROID_API)
	add_library(libCommon SHARED ${COMMON_SRCS} ${COMMON_HEADR})
	install(TARGETS libCommon DESTINATION bin)
endif (CMAKE_ANDROID_API)