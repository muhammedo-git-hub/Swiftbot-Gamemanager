package NoughtsAndCrosses;

package data;
import java.io.IOException;
import java.util.Optional;

import data.DataTypes.MotionException;
import data.DataTypes.RoundOutcome;
import data.DataTypes.UserTurnResult;

abstract public class Subprocesses {

    // Subprocess 1 - user registration 
    initAndRegister();


    //Subprocess 2 - generate & roll dice
    generateAndRollDice();
      

    // Subprocess 3 - user's turn 
    userTurn();
     

    // Subprocess 4 - swiftbot's turn 
    swiftBotTurn();
    

    // Subprocess 5 - check win/draw conditions 
    Optional<RoundOutcome> checkWinDrawAndAct();
  

    // Subprocess 6 - update round & scoreboard 
    appendRoundLogAndUpdateScoreboard(RoundOutcome outcome);
      

    // Subprocess 7 - play again or quit 
    playAgainOrQuit();
      
}