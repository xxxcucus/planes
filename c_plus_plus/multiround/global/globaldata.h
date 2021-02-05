#ifndef __GLOBAL_DATA__
#define __GLOBAL_DATA__

#include "globaluserdata.h"
#include "globalgamedata.h"

struct GlobalData {
  
    GlobalGameData m_GameData;
    GlobalUserData m_UserData;
    
    
public:
    void reset() {
        m_GameData.reset();
        m_UserData.reset();
    }
};



#endif
