#ifndef __WIN_BCRYPT__H
#define __WIN_BCRYPT__H

#include <iostream>

#include "crypt_blowfish.h"
#include "bcrypt.h"

class BCrypt {
public:
	static std::string generateHash(const std::string & password, int workload = 12) {
		char salt[BCRYPT_HASHSIZE];
		char hash[BCRYPT_HASHSIZE];
		int ret;
		printf("generateHash %d\n", BCRYPT_HASHSIZE);
		ret = bcrypt_gensalt(workload, salt);
		printf("bcrypt_gensalt %d\n", ret);
		if (ret != 0)throw std::runtime_error{ "bcrypt: can not generate salt" };
		ret = bcrypt_hashpw(password.c_str(), salt, hash);
		printf("bcrypt_hashpw %d\n", ret);
		if (ret != 0)throw std::runtime_error{ "bcrypt: can not generate hash" };
		return std::string{ hash };
	}

	static bool validatePassword(const std::string & password, const std::string & hash) {
		return (bcrypt_checkpw(password.c_str(), hash.c_str()) == 0);
	}
};

#endif