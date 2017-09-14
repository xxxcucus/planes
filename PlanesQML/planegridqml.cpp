#include "planegridqml.h"
#include <QDebug>

QColor PlaneGridQML::getPlanePointColor(int idx) const
{
   ///@todo: mark the head of the planes with green
   int colorStep = (m_MaxPlaneBodyColor - m_MinPlaneBodyColor) / m_PlaneGrid.getPlaneNo();
   int annotation = m_PlaneGrid.getPlanePointAnnotation(idx);
   std::vector<int> planesIdx = m_PlaneGrid.decodeAnnotation(annotation);
   if (planesIdx.size() > 1)    //point belongs to more planes mark it with red
       return QColor(255, 0, 0);
   if (planesIdx.size() == 1) {
       if (m_SelectedPlane != planesIdx[0]) {
           int grayCol = m_MinPlaneBodyColor + planesIdx[0] * colorStep;
           return QColor(grayCol, grayCol, grayCol);
       } else {
           return QColor(0, 0, 255);
       }
   }
   qDebug() << "Error: point belongs to no plane";
   return QColor(255, 255, 255);
}
