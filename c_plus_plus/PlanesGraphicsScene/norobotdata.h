#ifndef __NOROBOTDATA__
#define __NOROBOTDATA__

#include <QString>
#include <QJsonObject>
#include <vector>

struct NoRobotData {
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
