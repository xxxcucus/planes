#ifndef __BASIS_REQUEST_VIEW_MODEL__
#define __BASIS_REQUEST_VIEW_MODEL__

struct BasisRequestViewModel {
    QString m_Username;
    long int m_UserId;

    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("userId", QString::number(m_UserId));
        retVal.insert("userName", m_Username);
        return retVal;
    }
};

#endif
