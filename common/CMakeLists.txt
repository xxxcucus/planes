cmake_minimum_required (VERSION 3.10)
project (libCommon)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	$ENV{JAVA_HOME}/include/
	$ENV{JAVA_HOME}/include/win32/
	)

set(COMMON_HEADR 
	plane.h
	planegrid.h
	planesmodel.h
	planeround.h
	com_planes_javafx_PlaneRoundJavaFx.h
	computerlogic.h
	revertcomputerlogic.h
	guesspoint.h
	planeiterators.h
	coordinate2d.h
	vectoriterator.h
	planeorientationdata.h
	headdata.h)

set(COMMON_SRCS 	
	plane.cpp
	planegrid.cpp
	planesmodel.cpp
	planeround.cpp
	com_planes_javafx_PlaneRoundJavaFx.cpp
    computerlogic.cpp
	revertcomputerlogic.cpp
	guesspoint.cpp
	planeiterators.cpp
	gamestatistics.cpp
	coordinate2d.cpp
	planeorientationdata.cpp
	headdata.cpp)

add_library(libCommon SHARED ${COMMON_SRCS} ${COMMON_HEADR})

install(TARGETS libCommon DESTINATION bin)

#target_link_libraries(PlanesWidget ${Qt5Widgets_LIBRARIES})

#qt5_use_modules(libCommon Core)