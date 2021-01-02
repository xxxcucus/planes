#ifndef __GLOBAL_GAME_DATA__
#define __GLOBAL_GAME_DATA__


struct GlobalGameData {

long int m_GameId;
long int m_RoundId;

public:
    void reset() {
        m_GameId = 0;
        m_RoundId = 0;
    }    
};


#endif
