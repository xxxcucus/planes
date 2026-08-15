#ifndef __SEND_PLANE_POSITIONS_COMM_OBJ_
#define __SEND_PLANE_POSITIONS_COMM_OBJ_

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include <QMessageBox>

#include "basiscommobj.h"
#include "viewmodels/planespositionsviewmodel.h"
class MultiplayerRound;

//class MULTIPLAYER_EXPORT SendPlanePositionsCommObj : public BasisCommObj
class SendPlanePositionsCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    SendPlanePositionsCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData, MultiplayerRound* mrd):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData), m_MultiRound(mrd) {
        m_NoUserMessageBox = new QMessageBox(m_ParentWidget);
        m_NoUserMessageBox->setText("No user logged in");
        m_NoUserMessageBox->setStandardButtons(QMessageBox::NoButton);
    }
    
    virtual ~SendPlanePositionsCommObj();

    bool makeRequest();
    bool validateReply(const QJsonObject& retJson) override;
   
protected:
    SendPlanePositionsCommObj(): m_MultiRound(nullptr) {}

public slots:
    void finishedRequest() override;       
    
signals:
    void roundCancelled();
    void opponentPlanePositionsReceived();
    void waitForOpponentPlanePositions();
    
private:
    PlanesPositionsViewModel prepareViewModel();
    void processResponse(const QJsonObject& retJson);

private:
    QString m_GameName;
    MultiplayerRound* m_MultiRound = nullptr;

    QMessageBox* m_NoUserMessageBox = nullptr;

    friend class SendPlanePositionsCommObjTest;
};

















#endif
