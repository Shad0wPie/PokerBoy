package se.cygni.texasholdem.player;

import se.cygni.texasholdem.client.CurrentPlayState;
import se.cygni.texasholdem.game.Action;
import se.cygni.texasholdem.game.Hand;

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
        return null;
    }

    private Action showDown() {

    }

}
