package se.cygni.texasholdem.player;

import se.cygni.texasholdem.client.CurrentPlayState;
import se.cygni.texasholdem.game.Action;

/**
 * Created by adam on 09/04/16.
 */
public class BotLogic {

    CurrentPlayState playState;

    public BotLogic() {

    }

    public Action getMove(CurrentPlayState playState){

        this.playState = playState;

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
        return null;
    }

    private Action turn() {
        return null;
    }

    private Action preFlop() {
        return null;
    }

    private Action flop() {
        return null;
    }

    private Action showDown() {

    }

}
