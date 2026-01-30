#ifndef COORDINATE2D_H
#define COORDINATE2D_H

namespace PlanesCommonTools {

    class Coordinate2D
    {
    private:
        int m_x = 0;
        int m_y = 0;

    public:
        constexpr Coordinate2D() {}
        constexpr Coordinate2D(int x, int y) : m_x(x), m_y(y) { }
        constexpr int x() const {
            return m_x;
        }
        constexpr int y() const {
            return m_y;
        }

    friend constexpr  Coordinate2D operator+(const Coordinate2D& c1, const Coordinate2D& c2) {
        return Coordinate2D(c1.m_x + c2.m_x, c1.m_y + c2.m_y);
    }

    friend constexpr bool operator==(const Coordinate2D& c1, const Coordinate2D& c2) {
        return (c1.m_x == c2.m_x) && (c1.m_y == c2.m_y);
    }

    };

} //namespace
#endif // COORDINATE2D_H
