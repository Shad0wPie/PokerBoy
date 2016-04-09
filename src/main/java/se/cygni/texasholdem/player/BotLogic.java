package se.cygni.texasholdem.player;

import se.cygni.texasholdem.client.CurrentPlayState;
import se.cygni.texasholdem.game.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BotLogic {

    CurrentPlayState playState;
    HashMap<ActionType, Action> actions;
    String playerName;

    public BotLogic(String player) {
        playerName = player;
    }

    public Action getMove(CurrentPlayState newPlayState, HashMap<ActionType,Action> newActions){

        this.playState = newPlayState;
        this.actions = newActions;

        switch (playState.getCurrentPlayState()){
            case PRE_FLOP:
                return preFlop();
            case FLOP:
                return  flop();
            case TURN:
                return turn();
            case RIVER:
                return river();
            case SHOWDOWN:
                return showDown();
        }
        return null;
    }

    private Action river() {
        return defaultAction();
    }

    private Action turn() {
        return defaultAction();
    }

    private Action preFlop() {
        List<Card> myCards = playState.getMyCards();
        Action action = actions.get(ActionType.FOLD);
        switch (getHandRank(myCards)){
            case SUPERSTRONG:
                //I alla positioner: Höj om det är ohöjt sedan innan, all-in om höjt sedan innan
                break;
            case STRONG:
                //I alla positioner: höj om först in eller max 1 limpare. I sen position: höj om max 2 limpare. Syna om max 1 höjning
                break;
            case MEDIUM:
                //I alla positioner: höj om först in. I sen position: höj om max 1 limpare.
                break;
            case WEAK:
                //Om sen position && du är först in, höj. Annars fold
                break;
            case WORTHLESS:
                //Om minposition=BB och ingen höjning krävs, check. Annars fold
                break;
        }
        return action;
    }

    private HandStrength getHandRank(List<Card> cards) {
        //0=superstarka
        //1=starka, spelbara från alla positioner
        //2=medelstarka, spelbara från sena positioner
        //3=skit
    }

    private Action flop() {
        return defaultAction();
    }

    private Action showDown() {

        return defaultAction();
    }

    private Action defaultAction(){
        if (actions.get(ActionType.CHECK) != null)
            return actions.get(ActionType.CHECK);
        else
            return actions.get(ActionType.FOLD);
    }

    /**
     * Calculates the players position relative to BigBlind (BB = 0, SB = 1, ...)
     * @return position number
     */
    private int GetPosistion(){

        List<GamePlayer> players = playState.getPlayers();

        int bbInd = players.indexOf(playState.getBigBlindPlayer());

        int myInd = 0;
        for (int i = 0; i < players.size(); i++) {
            GamePlayer player = players.get(i);
            if (player.getName().equals(playerName))
                myInd = i;
        }
        if (myInd < bbInd) {
            return bbInd-myInd;
        }else{
            return players.size()-(myInd-bbInd);
        }
    }

}
