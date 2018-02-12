cmake_minimum_required (VERSION 2.6)
project (Planes)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTOUIC ON)
set(CMAKE_INCLUDE_CURRENT_DIR ON)
set(CMAKE_AUTORCC ON)

if (UNIX)
find_package(Qt5 COMPONENTS Core Widgets Quick)
endif (UNIX)

if (UNIX)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
endif (UNIX)

if (WIN32)
find_package(Qt5Widgets)
endif (WIN32)

#message("Source dir: " ${CMAKE_CURRENT_SOURCE_DIR})
#message(${Qt5Widgets_LIBRARIES})
#message(${Qt5Widgets_INCLUDE_DIRS})
#message("Qt dir: " ${QTDIR})

add_subdirectory(PlanesWidget)
add_subdirectory(PlanesGraphicsScene)
add_subdirectory(PlanesQML)
add_subdirectory(common)

