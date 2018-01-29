cmake_minimum_required (VERSION 2.6)
project (Planes)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTOUIC ON)
set(CMAKE_INCLUDE_CURRENT_DIR ON)
set(CMAKE_AUTORCC ON)

#find_package(Qt5 Core Widgets Quick)

find_package(Qt5Widgets)

#message("Source dir: " ${CMAKE_CURRENT_SOURCE_DIR})
#message(${Qt5Widgets_LIBRARIES})
#message(${Qt5Widgets_INCLUDE_DIRS})
#message("Qt dir: " ${QTDIR})

add_subdirectory(PlanesWidget)
add_subdirectory(PlanesGraphicsScene)
add_subdirectory(PlanesQML)
add_subdirectory(common)

