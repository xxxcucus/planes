###################################################################################
#
#  Copyright (c) 2014, webvariants GmbH, http://www.webvariants.de
#
#  This file is released under the terms of the MIT license. You can find the
#  complete text in the attached LICENSE file or online at:
#
#  http://www.opensource.org/licenses/mit-license.php
#
#  @author: Tino Rusch (tino.rusch@webvariants.de)
#
###############################################################################

set( CMAKE_ASM_FLAGS "${CXXFLAGS} -x assembler-with-cpp")

#set( CMAKE_CXX_FLAGS  "${CMAKE_CXX_FLAGS} -Wall --std=c++11 -O3" )
set( CMAKE_C_FLAGS  "${CMAKE_C_FLAGS} -Wall -O3" )

if (NOT WIN32)
	include_directories( ${CMAKE_CURRENT_SOURCE_DIR}/include)
endif()


set(BCRYPT_SRCS
	bcrypt.c
	crypt_blowfish.c
	crypt_gensalt.c
	wrapper.c
)

set(BCRYPT_HEADRS
    bcrypt.h
    BCrypt.hpp
    crypt.h
    crypt_blowfish.h
    crypt_gensalt.h
    ow-crypt.h
    winbcrypt.h
)

add_library(libbcrypt SHARED ${BCRYPT_SRCS})

if (UNIX)
install(TARGETS libbcrypt DESTINATION lib)
else (UNIX)
install(TARGETS libbcrypt DESTINATION bin)
endif (UNIX)
