#ifndef _PLANEROUNDOPTIONS__
#define _PLANEROUNDOPTIONS__


class PlaneRoundOptions
{
public:
	PlaneRoundOptions();

private:
	void reset();

public:
	int m_ComputerSkillLevel = 2; // 0 - EASY, 1 - MEDIUM, 2- ADVANCED
	bool m_ShowPlaneAfterKill = false;
};



#endif

