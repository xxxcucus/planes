#include "revertcomputerlogic.h"

//equals operator: copies the choices array and the guesses list from the ComputerLogic object
void RevertComputerLogic::operator=(const ComputerLogic& cl)
{
    if (m_row != cl.getRowNo() && m_col != cl.getRowNo())
        return;

    const std::vector<int> choices = cl.getChoicesArray();

    for(int i = 0;i < maxChoiceNo; i++)
        m_choices[i] = choices[i];

    m_guessesList.clear();
    m_guessesList = cl.getListGuesses();

    m_extendedGuessesList.clear();
    m_extendedGuessesList = cl.getExtendedListGuesses();

    m_playList.clear();
    m_playList = cl.getListGuesses();

    m_pos = static_cast<int>(m_playList.size()) - 1;
}

//constructor
RevertComputerLogic::RevertComputerLogic(int row, int col, int planeno):
    ComputerLogic(row, col, planeno), m_pos(0) {
    m_playList.clear();
}


//reverts the computer strategy by n steps
void RevertComputerLogic::revert(int n)
{
    if (n <= 0)
        return;

    //computes to which step of the computer strategy the strategy must be reverted
    int nsteps = m_pos - n;

    if (nsteps < -1)
        nsteps = -1;

    //resets the computer logic object
    reset();

    //play the strategy forwards
    for(int i = 0; i <= nsteps; i++)
    {
        addData(m_playList.at(i));
    }
    m_pos = nsteps;
}

//plays the computer strategy forward
void RevertComputerLogic::next()
{
    if (m_pos >= static_cast<int>(m_playList.size()) - 1)
        return;

    addData(m_playList.at(m_pos + 1));
    m_pos++;
}
