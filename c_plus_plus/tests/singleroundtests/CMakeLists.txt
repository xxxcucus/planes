cmake_minimum_required (VERSION 3.10)
project (singleroundtest)

cmake_policy(SET CMP0020 NEW)
cmake_policy(SET CMP0043 NEW)

set(CMAKE_AUTOMOC ON)
set(CMAKE_INCLUDE_CURRENT_DIR ON)

include_directories(
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../../singleround
    ${Qt6Core_INCLUDE_DIRS}
)

set(TEST_HEADR 
	planetest.h
	coordinate2dtest.h
	vectoriteratortest.h
	planepointiteratortest.h
	planeintersectingpointiteratortest.h
	orientationtest.h
	guesspointtest.h
	gamestagestest.h
	planeorientationdatatest.h
	headdatatest.h
	planegridtest.h
	computerlogictest.h
	planeroundtest.h
)

set(TEST_SRCS 
	planetest.cpp
	vectoriteratortest.cpp
	planepointiteratortest.cpp
	planeintersectingpointiteratortest.cpp
	coordinate2dtest.cpp
	orientationtest.cpp
	guesspointtest.cpp
	gamestagestest.cpp
	planeorientationdatatest.cpp
	headdatatest.cpp
	planegridtest.cpp
	computerlogictest.cpp
	planeroundtest.cpp
	main.cpp)

enable_testing(true)
add_executable(singleroundtest  ${TEST_HEADR} ${TEST_SRCS})
add_test(NAME singleroundtest COMMAND singleroundtest)

target_link_libraries(singleroundtest 
		libSingleRound
        Qt6::Test
		Qt6::Core)

if (UNIX)
install(TARGETS singleroundtest DESTINATION tests)
else (UNIX)
install(TARGETS singleroundtest DESTINATION bin)
endif (UNIX)