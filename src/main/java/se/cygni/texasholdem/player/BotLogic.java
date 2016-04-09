package se.cygni.texasholdem.player;

import se.cygni.texasholdem.client.CurrentPlayState;
import se.cygni.texasholdem.game.Action;
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
