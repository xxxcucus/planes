#include "guesspoint.h"


//constructor for a guess point
//this one initializes the GuessPoint only partially
//the result of the guess being not known
GuessPoint::GuessPoint(int row, int col):
    m_row(row),
    m_col(col)
{
}

//constructor which initializes the data members of the guess point
GuessPoint::GuessPoint(int row, int col, Type tp):
    m_row(row),
    m_col(col),
    m_type(tp)
{
}

//equals operator
bool GuessPoint::operator==(const GuessPoint &gp1) const
{
    if(m_row==gp1.m_row && m_col==gp1.m_col /*&& m_type==gp1.m_type*/)
        return true;
    else
        return false;
}


