#ifndef __PLANE_INTERSECTING_POINT_ITERATOR__
#define __PLANE_INTERSECTING_POINT_ITERATOR__

#include <array>
#include <utility>
#include "plane.h"
#include "coordinate2d.h"
#include "vectoriterator.h"


template <unsigned int M, unsigned int N, unsigned int P, typename T, T... ints>
    static constexpr int computeGeneratorCount(std::integer_sequence<T, ints...> int_seq)
    {
        int count = 0;

        ([&count]() {
            size_t i = ints / N / P;
            size_t j = (ints - i * N * P) / P;
            size_t k = (ints - i * N * P - j * P);
            Plane pl = Plane((int)i - M / 2, (int)j - M / 2, (Plane::Orientation)k);
            if (pl.containsPointConstExpr(PlanesCommonTools::Coordinate2D(0, 0)))
                count++;
            }(), ...);

        return count;
    }



template <unsigned int M, unsigned int N, unsigned int P, unsigned int Q, typename T, T... ints>
    constexpr std::array<Plane, Q>  generatePlaneList(std::integer_sequence<T, ints...> int_seq)
    {
        std::array<Plane, Q> retVal = std::array<Plane, Q>{};
        int count = 0;

        ([&retVal, &count]() {
            size_t i = ints / N / P;
            size_t j = (ints - i * N * P) / P;
            size_t k = (ints - i * N * P - j * P);
            Plane pl = Plane((int)i - M / 2, (int)j - M / 2, (Plane::Orientation)k);
            if (pl.containsPointConstExpr(PlanesCommonTools::Coordinate2D(0, 0))) {
                retVal[count] = pl;
                count++;
            }
            }(), ...);

        return retVal;
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
    void generateListOptimized();

    static constexpr int generatorCount = computeGeneratorCount<11, 11, 4, size_t>(std::make_index_sequence<11 * 11 * 4>());
    static constexpr std::array<Plane, generatorCount> generatorList = generatePlaneList<11, 11, 4, generatorCount, size_t>(std::make_index_sequence<11 * 11 * 4>());
};

#endif // __PLANE_INTERSECTING_POINT_ITERATOR__


