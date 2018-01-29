cmake_minimum_required (VERSION 2.6)
project (PlanesQML)

cmake_policy(SET CMP0020 NEW)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../common
	)

set(QML_SRCS 	
	planegridqml.cpp
	main.cpp
	planegameqml.cpp)

qt5_add_resources(RESOURCES qml.qrc)
	
add_executable(PlanesQML ${QML_SRCS} ${RESOURCES})

target_link_libraries(PlanesQML
	libCommon)

qt5_use_modules(PlanesQML Core Quick)

