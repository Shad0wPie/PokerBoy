package se.cygni.texasholdem.player;

import se.cygni.texasholdem.client.CurrentPlayState;
import se.cygni.texasholdem.communication.message.event.*;
import se.cygni.texasholdem.game.*;
import se.cygni.texasholdem.game.definitions.PlayState;
import se.cygni.texasholdem.game.definitions.PokerHand;
import se.cygni.texasholdem.game.util.PokerHandUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BotLogic {

    // permanent variables
    private final String playerName;

    // per game variables
    private HoleCards holeCards;

    // per turn variables
    private PlayState currentPlayState; // Turn / River / Flop / ...
    private CurrentPlayState roundInfo; // information
    private HashMap<ActionType, Action> actions; // possible action this turn
    private int raisedCounter;

    public BotLogic(String player) {
        playerName = player;
        currentPlayState = PlayState.PRE_FLOP;
    }

    public Action getMove(CurrentPlayState newPlayState, HashMap<ActionType, Action> newActions){

        this.roundInfo = newPlayState;
        this.actions = newActions;
        Action action = actions.get(ActionType.FOLD);

        PlayState newState = roundInfo.getCurrentPlayState();
        if (currentPlayState.equals(newPlayState)){
            changeState(newState);
        }

        if(holeCards == null){
            holeCards = new HoleCards(roundInfo.getMyCards());
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
        return flop();
    }

    private Action turn() {
        return flop();
    }

    private Action preFlop() {
        switch (holeCards.getStrength()){
            case SUPERDUPERMEGADEATH:
                //I alla positioner: Höj om det är ohöjt eller max 1 höjning sedan innan, all-in om höjt 2ggr eller mer sedan innan
                if (actions.get(ActionType.RAISE) != null) {
                    return actions.get(ActionType.RAISE);
                }
                else {
                    return actions.get(ActionType.ALL_IN);
                }
                //break;
            case NOTQUITEASMEGADEATH:
                if (actions.get(ActionType.RAISE) != null) {
                    return actions.get(ActionType.RAISE);
                }
                else {
                    return actions.get(ActionType.ALL_IN);
                }
                //I alla positioner: höj om först in eller max 1 limpare. I sen position: höj om max 2 limpare. Syna om max 1 höjning
                //break;
            case HALFWAYSUPERDUPER:
                if (actions.get(ActionType.CHECK) != null) {
                    return actions.get(ActionType.CHECK);
                }
                else if(actions.get(ActionType.CALL) != null){
                    return actions.get(ActionType.CALL);
                }else{
                    return actions.get(ActionType.FOLD);
                }
                //break;
            case PRETTYBADBUTNOTWORTHLESS:
                if (actions.get(ActionType.CHECK) != null) {
                    return actions.get(ActionType.CHECK);
                }
                else if(actions.get(ActionType.CALL) != null){
                    return actions.get(ActionType.CALL);
                }else{
                    return actions.get(ActionType.FOLD);
                }
                //Om sen position && du är först in, höj. Annars fold
                //break;
            case WORTHLESS:
                if (actions.get(ActionType.CHECK) != null) {
                    return actions.get(ActionType.CHECK);
                }else{
                    return actions.get(ActionType.FOLD);
                }
                //break;
            //Funktioner som behövs: minPosition (returnera siffra 0-9, 0=BB, 1=SB, 2=Dealer...)
            //hurMangaHarGattMed (returnera 0 om först in, 1 om 1 spelare gått med osv)
            //hurMangaHojningarInnan
        }
        return actions.get(ActionType.FOLD);
    }

   /* private HandStrength getHandRank(List<Card> cards) {
        //0=superstarka
        //1=starka, spelbara från alla positioner
        //2=medelstarka, spelbara från sena positioner
        //3=skit
    }*/

    private Action flop() {

        float win = chanceOfWinning();

        if(win>0.95){
            if (actions.get(ActionType.RAISE) != null) {
                return actions.get(ActionType.RAISE);
            }else{
                return actions.get(ActionType.ALL_IN);
            }
        } else if(win>0.9){
            if (actions.get(ActionType.CHECK) != null) {
                return actions.get(ActionType.CHECK);
            }else if (actions.get(ActionType.CALL) != null){
                return actions.get(ActionType.CALL);
            }else{
                return actions.get(ActionType.FOLD);
            }
        } else if(win > 0.8 && roundInfo.getMyInvestmentInPot()>roundInfo.getMyCurrentChipAmount()*0.2){
            if (actions.get(ActionType.CHECK) != null) {
                return actions.get(ActionType.CHECK);
            }else if (actions.get(ActionType.CALL) != null){
                return actions.get(ActionType.CALL);
            }else{
                return actions.get(ActionType.FOLD);
            }
        } else {
            if (actions.get(ActionType.CHECK) != null) {
                return actions.get(ActionType.CHECK);
            } else{
                return actions.get(ActionType.FOLD);
            }
        }
    }

    private float chanceOfWinning() {
        List<Card> myCards = roundInfo.getMyCardsAndCommunityCards();

        List<Card> deck = Deck.getOrderedListOfCards();

        PokerHandUtil pokerHandUtil = new PokerHandUtil(roundInfo.getCommunityCards(), roundInfo.getMyCards());
        Hand myBestHand = pokerHandUtil.getBestHand();
        PokerHand myBestPokerHand = myBestHand.getPokerHand();


        int betterHands = 0;
        int totalHands = 0;

        boolean remove = false;
        for (int i = 0; i < deck.size(); i++) {
            if (remove){
                deck.remove(i-1);
                remove = false;
            }
            for (Card myC : myCards){
                if(deck.get(i).equals(myC)){
                    remove = true;
                }
            }
        }

        Card oldCard = deck.get(0);
        for (Card c1 : deck){
                deck.remove(oldCard);
            for (Card c2 : deck){
                if(c1.equals(c2)){
                    continue;
                }
                ArrayList<Card> tempCards = new ArrayList<Card>();
                tempCards.add(c1);
                tempCards.add(c2);
                PokerHandUtil tempPokerHandUtil = new PokerHandUtil(roundInfo.getCommunityCards(), tempCards);
                Hand bestHand = tempPokerHandUtil.getBestHand();
                PokerHand bestPokerHand = bestHand.getPokerHand();
                if(myBestPokerHand.getOrderValue() < bestPokerHand.getOrderValue()){
                    betterHands++;
                }
                totalHands++;
            }
            oldCard = c1;
        }
        return 1 - betterHands/((float)totalHands);
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
        List<GamePlayer> players = roundInfo.getPlayers();

        int bbInd = players.indexOf(roundInfo.getBigBlindPlayer());

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
        holeCards = null;
    }

    public void onTableChangedState(TableChangedStateEvent event) {
        //nothing for now
    }
}
