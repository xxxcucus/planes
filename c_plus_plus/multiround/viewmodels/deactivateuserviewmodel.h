#ifndef __DEACTIVATE_USER_VIEW_MODEL__
#define __DEACTIVATE_USER_VIEW_MODEL__

#include <QJsonObject>
#include <QString>
#include <QDebug>
#include "basisrequestviewmodel.h"


struct DeactivateUserViewModel: public BasisRequestViewModel {

    public:
    QJsonObject toJson() {
        return BasisRequestViewModel::toJson();
    }

};

#endif

