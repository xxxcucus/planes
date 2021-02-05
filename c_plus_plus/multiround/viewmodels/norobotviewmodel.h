#ifndef __NOROBOT_VIEW_MODEL__
#define __NOROBOT_VIEW_MODEL__

#include <QString>
#include <QJsonObject>


struct NoRobotViewModel {
    QString m_requestId;
    QString m_answer;
    
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("id", m_requestId);
        retVal.insert("answer", m_answer);        
        return retVal;
    }
};

#endif
