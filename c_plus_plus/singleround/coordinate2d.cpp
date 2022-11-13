#include "coordinate2d.h"

namespace PlanesCommonTools {

    Coordinate2D operator+(const Coordinate2D& c1, const Coordinate2D& c2) {
        return Coordinate2D(c1.m_x + c2.m_x, c1.m_y + c2.m_y);
    }

    bool operator==(const Coordinate2D& c1, const Coordinate2D& c2) {
        return (c1.m_x == c2.m_x) && (c1.m_y == c2.m_y);
    }
}
