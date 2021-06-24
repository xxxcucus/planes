#ifndef PLAYERBOARD_H
#define PLAYERBOARD_H

#include "genericboard.h"
#include "planegrid.h"


///the board where the player lays his/her planes and where the computer tries to guess their position
class PlayerBoard : public GenericBoard
{
    Q_OBJECT
public:
    PlayerBoard(PlaneGrid& grid, int squareWidth = 30) : GenericBoard(grid, squareWidth) {}
    virtual ~PlayerBoard() {}

    /**
     * functions to edit the player's game board
     */

    /**
     * The selected plane changes
     */
    void selectPlaneClicked(bool );
    /**
     * @brief Operations with the selected plane.
     */
    void rotatePlaneClicked(bool );
    void upPlaneClicked(bool );
    void downPlaneClicked(bool );
    void leftPlaneClicked(bool );
    void rightPlaneClicked(bool );

    /**
     * @brief : Actions to be taken when the round ends.
     */
    void endRound(bool isPlayerWinner, bool isDraw, bool isSinglePlayer) override;

    inline void generateBoardItems() override {
        GenericBoard::generateBoardItems();
        m_SelectedPlane = 0;
    }
};

#endif // PLAYERBOARD_H
