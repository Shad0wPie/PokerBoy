package se.cygni.texasholdem.player;

import se.cygni.texasholdem.client.CurrentPlayState;
import se.cygni.texasholdem.communication.message.event.*;
import se.cygni.texasholdem.game.*;
import se.cygni.texasholdem.game.definitions.PlayState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BotLogic {

    CurrentPlayState playState;
    HashMap<ActionType, Action> actions;
    String playerName;
    PlayState currentPlayState;
    private static List<List<Card>> superStrongHands;

    private int raisedCounter;

    public BotLogic(String player) {
        playerName = player;
        currentPlayState = PlayState.PRE_FLOP;
    }

    public Action getMove(CurrentPlayState newPlayState, HashMap<ActionType, Action> newActions){

        this.playState = newPlayState;
        this.actions = newActions;
        Action action = actions.get(ActionType.FOLD);

        PlayState newPlayState = playState.getCurrentPlayState();
        if (currentPlayState.equals(newPlayState)){
            changeState(newPlayState);
        }

        switch (currentPlayState){
            case PRE_FLOP:
                action = preFlop();
                break;
            case FLOP:
                action = flop();
                break;
            case TURN:
                action = turn();
                break;
            case RIVER:
                action = river();
                break;
            case SHOWDOWN:
                action = showDown();
                break;
        }
        resetRound();
        return action;
    }

    private void changeState(PlayState newPlayState) {
        resetRound();
        currentPlayState = newPlayState;
    }

    private void resetRound() {
        raisedCounter = 0;
    }

    private Action river() {
        return defaultAction();
    }

    private Action turn() {
        return defaultAction();
        Hand
    }

    private Action preFlop() {
        List<Card> myCards = playState.getMyCards();
        Action action = actions.get(ActionType.FOLD);
        switch (getHandRank(myCards)){
            case SUPERSTRONG:
                //I alla positioner: Höj om det är ohöjt eller max 1 höjning sedan innan, all-in om höjt 2ggr eller mer sedan innan
                break;
            case STRONG:
                //I alla positioner: höj om först in eller max 1 limpare. I sen position: höj om max 2 limpare. Syna om max 1 höjning
                break;
            case MEDIUM:
                //I alla positioner: höj om först in. I sen position: höj om max 1 limpare.
                if(hurMangaHarGattMed==0 || (hurMangaHarGattMed==1 && getPosition()<=3 && hurMangaHojningarInnan==0))
                {
                    //höj
                }
                else
                {
                    //fold
                }
                break;
            case WEAK:
                if(minPosition<=3 && hurMangaHarGattMed==0)
                {
                    //höj
                }
                else
                {
                    //fold
                }
                //Om sen position && du är först in, höj. Annars fold
                break;
            case WORTHLESS:
                //Om minposition=BB och ingen höjning krävs, check. Annars fold
                if(minPosition == 0 && hurMangaHojningarInnan==0)
                {
                    //checka
                }
                else
                {
                    //folda
                }
                break;
            //Funktioner som behövs: minPosition (returnera siffra 0-9, 0=BB, 1=SB, 2=Dealer...)
            //hurMangaHarGattMed (returnera 0 om först in, 1 om 1 spelare gått med osv)
            //hurMangaHojningarInnan

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
    private int getMyPosistion(){
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

    private int getNumberOfRaises(){
        return raisedCounter;
    }

    public void onPlayerRaised(PlayerRaisedEvent event){
        raisedCounter++;
    }

    public void onPlayerCalled(PlayerCalledEvent event) {
        //nothing for now
    }

    public void onPlayerFolded(PlayerFoldedEvent event) {
        //nothing for now
    }

    public void onPlayerWentAllIn(PlayerWentAllInEvent event) {
        //nothing for now
    }

    public void onPlayIsStarted(PlayIsStartedEvent event) {
        //nothing for now
    }

    public void onTableChangedState(TableChangedStateEvent event) {
        //nothing for now
    }
}
