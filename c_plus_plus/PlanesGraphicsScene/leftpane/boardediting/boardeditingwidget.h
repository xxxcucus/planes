#ifndef __BOARD_EDITING_WIDGET__
#define __BOARD_EDITING_WIDGET__

#include <QWidget>
#include <QPushButton>
#include "gameinfo.h"


class BoardEditingWidget: public QWidget {
    Q_OBJECT
    
public:
    BoardEditingWidget(GameInfo* gameInfo, QWidget *parent = 0);
    void waitForOpponentPlanesPositions();
    void activateEditingBoard();
    void activateDoneButton(bool planesOverlap);
    void activateGameTab();
    
signals:
   void selectPlaneClicked(bool);
   void rotatePlaneClicked(bool);
   void doneClicked(bool);
   void upPlaneClicked(bool);
   void downPlaneClicked(bool);
   void leftPlaneClicked(bool);
   void rightPlaneClicked(bool);
   void acquireOpponentPositionsClicked(bool);
   void cancelRoundClicked(bool);
    
private:
    QPushButton* m_selectPlaneButton;
    QPushButton* m_rotatePlaneButton;
    QPushButton* m_leftPlaneButton;
    QPushButton* m_rightPlaneButton;
    QPushButton* m_upPlaneButton;
    QPushButton* m_downPlaneButton;
    QPushButton* m_doneButton;
    QPushButton* m_acquireOpponentPositionsButton;
   
    QPushButton* m_CancelRoundButton;

    GameInfo* m_GameInfo;
};















#endif
