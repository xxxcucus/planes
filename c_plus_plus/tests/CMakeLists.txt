cmake_minimum_required (VERSION 3.10)
project (commobjtest)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

set(CMAKE_AUTOMOC ON)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}/../multiround/communicationobjects
	${CMAKE_CURRENT_SOURCE_DIR}/../multiround/global
	${CMAKE_CURRENT_SOURCE_DIR}/../multiround/
	${CMAKE_CURRENT_SOURCE_DIR}/../bcrypt
	${Qt6Widgets_INCLUDE_DIRS}
	${Qt6Network_INCLUDE_DIRS}
    ${Qt6Core_INCLUDE_DIRS}
	${Qt6Network_INCLUDE_DIRS})

set(TEST_HEADR 
    cancelroundcommobjtest.h
	connecttogamecommobjtest.h
	creategamecommobjtest.h
	logincommobjtest.h)

set(TEST_SRCS 	
	cancelroundcommobjtest.cpp
	connecttogamecommobjtest.cpp
	creategamecommobjtest.cpp
	logincommobjtest.cpp
	main.cpp)

enable_testing(true)
add_executable(commobjtest  ${TEST_HEADR} ${TEST_SRCS})
add_test(NAME commobjtest COMMAND commobjtest)

target_link_libraries(commobjtest 
		libMultiRound
		libbcrypt
        Qt::Test
		Qt6::Widgets
		Qt6::Network
		Qt6::Core)

if (UNIX)
install(TARGETS commobjtest DESTINATION tests)
else (UNIX)
install(TARGETS commobjtest DESTINATION bin)
endif (UNIX)