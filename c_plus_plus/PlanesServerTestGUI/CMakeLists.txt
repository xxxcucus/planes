cmake_minimum_required (VERSION 3.10)
project (PlanesServerTestGUI)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../common
	${Qt5Widgets_INCLUDE_DIRS})


set(PSTG_SRCS 	
	main.cpp
	mainwindow.cpp
)

set(PSTG_HEADR 	
)

add_executable(PlanesServerTestGUI 
	${PSTG_SRCS}
	${PSTG_HEADR})
target_link_libraries(PlanesServerTestGUI
	${Qt5Widgets_LIBRARIES})
qt5_use_modules(PlanesServerTestGUI Widgets)

install(TARGETS PlanesServerTestGUI DESTINATION bin)
