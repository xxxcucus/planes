#ifndef __PLANE_INTERSECTING_POINT_ITERATOR__
#define __PLANE_INTERSECTING_POINT_ITERATOR__

#include <array>
#include "plane.h"
#include "coordinate2d.h"
#include "vectoriterator.h"

template <unsigned int M, unsigned int N, unsigned int P, typename T, T... ints>
constexpr int computeGeneratorCount(std::integer_sequence<T, ints...> int_seq)
{
    int count = 0;

    ([&count]() {
        int i = ints / N / P;
        int j = (ints - i * N * P) / P;
        int k = (ints - i * N * P - j * P);
        Plane pl = Plane(i, j, (Plane::Orientation)k);
        if (pl.containsPointConstExpr(PlanesCommonTools::Coordinate2D(0, 0)))
            count++;
        }(), ...);

    return count;
}

//lists the relatives positions of all planes that pass through the point (0,0)
class PlaneIntersectingPointIterator: public PlanesCommonTools::VectorIterator<Plane>
{
    PlanesCommonTools::Coordinate2D m_point;

public:
    //constructor taking a QPoint
    PlaneIntersectingPointIterator(const PlanesCommonTools::Coordinate2D& qp = PlanesCommonTools::Coordinate2D(0,0));

private:
    //generates list of plane indexes that pass through (0,0)
    void generateList();

    
    //constexpr std::array<Plane, generatorCount> generateConstList();

   
    static constexpr int generatorCount = computeGeneratorCount<11, 11, 4, std::size_t>(std::make_index_sequence<11 * 11 * 4>{});
};

#endif // __PLANE_INTERSECTING_POINT_ITERATOR__
