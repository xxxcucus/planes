#ifndef COORDINATE2D_H
#define COORDINATE2D_H

namespace PlanesCommonTools {

    class Coordinate2D
    {
    private:
        int m_x = 0;
        int m_y = 0;

    public:
        Coordinate2D() {}
        Coordinate2D(int x, int y) : m_x(x), m_y(y) { }
        int x() const {
            return m_x;
        }
        int y() const {
            return m_y;
        }

    friend Coordinate2D operator+(const Coordinate2D& c1, const Coordinate2D& c2);
    friend bool operator==(const Coordinate2D& c1, const Coordinate2D& c2);
    };

} //namespace
#endif // COORDINATE2D_H
