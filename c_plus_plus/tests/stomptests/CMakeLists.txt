cmake_minimum_required (VERSION 3.16)
project (stomptest)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

set(CMAKE_AUTOMOC ON)
set(CMAKE_INCLUDE_CURRENT_DIR ON)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../../stomp
	${Qt6Core_INCLUDE_DIRS}
)

set(TEST_HEADR 
	stompframetest.h
	stompframeparsertest.h
)

set(TEST_SRCS 
	stompframetest.cpp
	stompframeparsertest.cpp
	main.cpp)

enable_testing(true)
add_executable(stomptest  ${TEST_HEADR} ${TEST_SRCS})
add_test(NAME stomptest COMMAND stomplibtest)

target_link_libraries(stomptest 
	libStomp
	Qt6::Test
	Qt6::Core)

if (UNIX)
install(TARGETS stomptest DESTINATION tests)
else (UNIX)
install(TARGETS stomptest DESTINATION bin)
endif (UNIX)
