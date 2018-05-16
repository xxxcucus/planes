#ifndef GUESSPOINT_H
#define GUESSPOINT_H

//describes a guess together with its result Miss,Hit or Dead
class GuessPoint
{
public:
    enum Type {Miss = 0, Hit = 1, Dead = 2};

    //the coordinates of the guess
    int m_row, m_col;
    //the result of the guess
    Type m_type;

public:
    //various constructors
    GuessPoint(int row, int col);
    GuessPoint(int row, int col, Type tp);

    //sets the result of the guess
    void setType(Type tp) { m_type = tp; }
    //tests to see whether a point lies in a list of guesses
    //static bool isGuess(QPoint qp, const QList <GuessPoint>& guessList);

    //compares two guess points
    bool operator ==(const GuessPoint& pl1) const;

    bool isDead() const { return m_type == Dead; }
    bool isHit() const { return m_type == Hit; }
    bool isMiss() const { return m_type == Miss; }
};


#endif // GUESSPOINT_H
