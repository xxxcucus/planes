#ifndef __GLOBAL_GAME_DATA__
#define __GLOBAL_GAME_DATA__


struct GlobalGameData {

long int m_GameId;
long int m_RoundId;
long int m_UserId;
long int m_OtherUserId;

public:
    void reset() {
        m_GameId = 0;
        m_RoundId = 0;
        m_UserId = 0;
        m_OtherUserId = 0;
    }    
};


#endif
