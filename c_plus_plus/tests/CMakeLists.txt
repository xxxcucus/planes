cmake_minimum_required (VERSION 3.10)
project (mytest)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

set(CMAKE_AUTOMOC ON)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}/../multiround/communicationobjects
	${CMAKE_CURRENT_SOURCE_DIR}/../multiround/global
	${CMAKE_CURRENT_SOURCE_DIR}/../multiround/
	${Qt6Widgets_INCLUDE_DIRS}
	${Qt6Network_INCLUDE_DIRS}
    ${Qt6Core_INCLUDE_DIRS}
	${Qt6Network_INCLUDE_DIRS})

set(TEST_HEADR 
    cancelroundcommobjtest.h)

set(TEST_SRCS 	
	cancelroundcommobjtest.cpp)


enable_testing(true)
add_executable(mytest  ${TEST_HEADR} ${TEST_SRCS})
add_test(NAME mytest COMMAND mytest)

target_link_libraries(mytest libMultiRound
        Qt::Test Qt6::Widgets Qt6::Network Qt6::Core)

if (UNIX)
install(TARGETS mytest DESTINATION tests)
else (UNIX)
install(TARGETS mytest DESTINATION bin)
endif (UNIX)