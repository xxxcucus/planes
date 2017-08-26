#include "planegridqml.h"
#include <QDebug>

QColor PlaneGridQML::getPlanePointColor(int idx) const
{
   int colorStep = (m_MaxPlaneBodyColor - m_MinPlaneBodyColor) / m_PlaneGrid.getPlaneNo();
   int annotation = m_PlaneGrid.getPlanePointAnnotation(idx);
   std::vector<int> planesIdx = m_PlaneGrid.decodeAnnotation(annotation);
   if (planesIdx.size() > 1)    //point belongs to more planes mark it with red
       return QColor(255, 0, 0);
   if (planesIdx.size() == 1) {
       int grayCol = m_MinPlaneBodyColor + planesIdx[0] * colorStep;
       return QColor(grayCol, grayCol, grayCol);
   }
   qDebug() << "Error: point belongs to no plane";
   return QColor(255, 255, 255);
}
