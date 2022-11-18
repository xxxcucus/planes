cmake_minimum_required (VERSION 3.10)
project (PlanesQML)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../singleround
	)

#link_directories(
#	${CMAKE_CURRENT_BINARY_DIR}/../common/Release/
#)

set (QML_HEADR
	planegridqml.h
	planegameqml.h)

set(QML_SRCS 	
	planegridqml.cpp
	main.cpp
	planegameqml.cpp)

qt_add_resources(RESOURCES qml.qrc)
	
add_executable(PlanesQML ${QML_SRCS} ${RESOURCES} ${QML_HEADR})

target_link_libraries(PlanesQML
	libSingleRound
	Qt6::Core
	Qt6::Quick)


install(TARGETS PlanesQML DESTINATION bin)
