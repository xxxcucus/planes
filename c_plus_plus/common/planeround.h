#ifndef __PLANE_ROUND_JAVAFX_
#define __PLANE_ROUND_JAVAFX_

#include "planegrid.h"
#include "computerlogic.h"
#include "gamestatistics.h"
#include "guesspoint.h"
#include "planeroundoptions.h"
#include "playerguessreaction.h"
#include "abstractplaneround.h"

class PlaneRound : public AbstractPlaneRound {
public:
	enum class GameStages { GameNotStarted = 0, BoardEditing = 1, Game = 2};

	PlaneRound(int rowNo, int colNo, int planeNo);
	~PlaneRound();
    
    //inits a new round
	virtual void initRound() override;

	/**
	@param[in] gp - the player's guess together with its evaluation
	@param[out] pgr - response to the player's guess: the computer's guess, if the game ended, winner, game statistics
	Plays a step in the game, as triggered by the player's guess gp.
	*/
	void playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr) override;

	/**
		@param[in] row, col - coordinates of player's guess
		@param[out] guessRes - the evaluation of the player's guess
		@param[out] pgr - response to the player's guess: the computer's guess, if the game ended, winner, game statistics
		Plays a step in the game, as triggered by the the player's guess coordinates.
	*/
	void playerGuessIncomplete(int row, int col, GuessPoint::Type& guessRes, PlayerGuessReaction& pgr) override;


	ComputerLogic* computerLogic() { return m_computerLogic; }


	/**
	Sets the computer skill. When this is during a game reject the change.
	**/
	bool setComputerSkill(int computerSkill);

	/**
	Sets the computer skill. When this is during a game reject the change.
	**/
	bool setShowPlaneAfterKill(bool showPlane);

	int getComputerSkill() {
		return m_RoundOptions.m_ComputerSkillLevel;
	}
	bool getShowPlaneAfterKill() {
		return m_RoundOptions.m_ShowPlaneAfterKill;
	}
	
	void setRoundCancelled() {
        m_State = AbstractPlaneRound::GameStages::GameNotStarted;
        m_playerGuessList.clear();
        m_computerGuessList.clear();
    }

private:
	//based on the available information makes the next move for the computer
	GuessPoint guessComputerMove();
	void updateGameStatsAndGuessListPlayer(const GuessPoint& gp) override;
	void updateGameStatsAndReactionComputer(PlayerGuessReaction& pgr);
    
    void reset() override;

private:
	//the computer's strategy
	ComputerLogic* m_computerLogic;
    PlaneRoundOptions m_RoundOptions;
};


#endif
