#include "planesgsview.h"


#include <QHBoxLayout>

PlanesGSView::PlanesGSView(PlaneGrid *pGrid, PlaneGrid* cGrid, ComputerLogic* cLogic, PlaneRound *rd, QWidget *parent)
    : QWidget(parent), playerGrid(pGrid), computerGrid(cGrid), computerLogic(cLogic), round(rd)
{
    QHBoxLayout* hLayout = new QHBoxLayout();
    m_LeftPane = new LeftPane(this);
    m_LeftPane->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Expanding);

    m_RightPane = new RightPane(this);

    hLayout->addWidget(m_LeftPane);
    hLayout->addWidget(m_RightPane);
    setLayout(hLayout);
}



