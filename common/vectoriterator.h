#ifndef VECTORITERATOR_H
#define VECTORITERATOR_H

#include <vector>

namespace PlanesCommonTools
{
//defines an iterator over a QList

template <class T>
class VectorIterator
{
protected:
    std::vector<T> m_internalList;
    int m_idx;

public:
    //constructor
    VectorIterator();

    //sets the position of the iterator before the first  element
    void reset();
    //during an iteration checks to see if there is a next element
    bool hasNext() const;
    //during an iteration returns the next element
    const T& next();
    //returns number of elements
    int itemNo() const;
};

template <class T>
VectorIterator<T>::VectorIterator()
{
    //generates the list of points
    m_internalList.clear();
    //puts the index one before the first element in the list
    reset();
}

template <class T>
void VectorIterator<T>::reset()
{
    m_idx = -1;
}

//during a point iteration checks to see if there is a next point
template <class T>
bool VectorIterator<T>::hasNext() const
{
    return (m_idx < static_cast<int>(m_internalList.size()) - 1);
}

//during an iteration returns the next point
template <class T>
const T& VectorIterator<T>::next()
{
    return  m_internalList[++m_idx];
}

//returns number of points on the plane
template <class T>
int VectorIterator<T>::itemNo() const
{
    return m_internalList.size();
}

}

#endif // VECTORITERATOR_H
