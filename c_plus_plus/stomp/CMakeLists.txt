cmake_minimum_required (VERSION 3.10)
project (libStomp)
add_definitions(-DMAKE_STOMP_LIB)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}/
	${Qt6Core_INCLUDE_DIRS}
	${Qt6Network_INCLUDE_DIRS}
	${Qt6WebSockets_INCLUDE_DIRS})


set(STOMP_HEADR 
	stompframe.h
	stompclient.h
	stompframecreator.h
	stompframeparser.h
	)

set(STOMP_SRCS 	
	stompframe.cpp
	stompclient.cpp
	stompframecreator.cpp
	stompframeparser.cpp
	)


add_library(libStomp SHARED ${STOMP_HEADR} ${STOMP_SRCS})

target_link_libraries(libStomp
 	Qt6::Widgets
	Qt6::Network
	Qt6::WebSockets)

if (UNIX)
install(TARGETS libStomp DESTINATION lib)
else (UNIX)
install(TARGETS libStomp DESTINATION bin)
endif (UNIX)
