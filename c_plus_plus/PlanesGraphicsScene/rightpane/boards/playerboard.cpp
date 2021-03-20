#include "playerboard.h"

void PlayerBoard::selectPlaneClicked(bool)
{
    m_SelectedPlane = (m_SelectedPlane + 1) % m_Grid.getPlaneNo();
    //qDebug() << "select plane " << m_SelectedPlane;
    hidePlanes();
    displayPlanes();
}

void PlayerBoard::rotatePlaneClicked(bool)
{
    //qDebug() << "rotate plane";
    m_Grid.rotatePlane(m_SelectedPlane);
    updateEditorBoard();
}

void PlayerBoard::upPlaneClicked(bool )
{
    //qDebug() << "up plane";
    m_Grid.movePlaneUpwards(m_SelectedPlane);
    updateEditorBoard();
}

void PlayerBoard::downPlaneClicked(bool )
{
    //qDebug() << "down plane";
    m_Grid.movePlaneDownwards(m_SelectedPlane);
    updateEditorBoard();
}

void PlayerBoard::leftPlaneClicked(bool )
{
    //qDebug() << "left plane";
    m_Grid.movePlaneLeft(m_SelectedPlane);
    updateEditorBoard();
}

void PlayerBoard::rightPlaneClicked(bool )
{
    //qDebug() << "right plane";
    m_Grid.movePlaneRight(m_SelectedPlane);
    updateEditorBoard();
}

void PlayerBoard::endRound(bool isPlayerWinner, bool isDraw, bool isSinglePlayer) {
    GenericBoard::endRound(isPlayerWinner, isDraw, isSinglePlayer);
}
