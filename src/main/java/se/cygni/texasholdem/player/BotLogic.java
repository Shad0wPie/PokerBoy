package se.cygni.texasholdem.player;

import se.cygni.texasholdem.client.CurrentPlayState;
import se.cygni.texasholdem.game.Action;
import se.cygni.texasholdem.game.Hand;
import se.cygni.texasholdem.game.ActionType;

import java.util.HashMap;

public class BotLogic {

    CurrentPlayState playState;
    HashMap<ActionType, Action> actions;

    public BotLogic() {

    }

    public Action getMove(CurrentPlayState newPlayState, HashMap<ActionType,Action> newActions){

        this.playState = newPlayState;
        this.actions = newActions;

        switch (playState.getCurrentPlayState()){
            case PRE_FLOP:
                return preFlop();
                break;
            case FLOP:
                return  flop();
                break;
            case TURN:
                return turn();
                break;
            case RIVER:
                return river();
                break;
            case SHOWDOWN:
                return showDown();
                break;
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
        Hand minHand = playState.getMyCards();
        switch (getHandRank(minHand)){
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

    }

    private HandStrength getHandRank(Hand minHand) {
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

}
