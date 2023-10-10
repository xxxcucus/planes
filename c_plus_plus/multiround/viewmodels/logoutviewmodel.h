#ifndef __LOGOUT_VIEW_MODEL__
#define __LOGOUT_VIEW_MODEL__

#include <QJsonObject>
#include <QString>
#include <QDebug>
#include "basisrequestviewmodel.h"


struct LogoutViewModel: public BasisRequestViewModel {

    public:
    QJsonObject toJson() {
        return BasisRequestViewModel::toJson();
    }

};

#endif
