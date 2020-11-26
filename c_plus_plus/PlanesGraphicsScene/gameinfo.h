#ifndef __GAME_INFO__
#define __GAME_INFO__

class GameInfo {

private:
    bool m_IsSinglePlayer = false;
    
public:
    GameInfo() {}
    
    void setSinglePlayer(bool singlePlayer) {
        m_IsSinglePlayer = singlePlayer;
    }
    bool getSinglePlayer() {
        return m_IsSinglePlayer;
    }
    
};

#endif
