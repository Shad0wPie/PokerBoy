package se.cygni.texasholdem.player;

import se.cygni.texasholdem.game.Card;
import se.cygni.texasholdem.game.definitions.Rank;

import java.util.ArrayList;
import java.util.List;

public class HoleCards {

    private List<Card> cards;
    private HandStrength strength;

    public List<Card> getCards() {
        return cards;
    }

    public HandStrength getStrength() {
        return strength;
    }

    public HoleCards(List<Card> cards) {
        this.cards = new ArrayList<Card>(cards);
        this.strength = calculateStrength(this);
    }

    private static HandStrength calculateStrength(HoleCards holeCards) {

        Card firstCard = holeCards.getCards().get(0);
        Card secondCard = holeCards.getCards().get(1);

        //Check strongest
        if (checkHand(holeCards, Rank.ACE,Rank.ACE) || checkHand(holeCards, Rank.KING,Rank.KING) || checkHand(holeCards, Rank.QUEEN,Rank.QUEEN)){
            return HandStrength.SUPERDUPERMEGADEATH;
        }

        //Check second strongest
        if(firstCard.getRank() == secondCard.getRank() && firstCard.getRank().compareTo(Rank.EIGHT) > 0 ){
            return HandStrength.NOTQUITEASMEGADEATH;
        }
        if (checkHand(holeCards, Rank.ACE,Rank.KING,true) || checkHand(holeCards, Rank.ACE,Rank.QUEEN, true)){
            return HandStrength.NOTQUITEASMEGADEATH;
        }

        //third
        if (checkHand(holeCards, Rank.ACE,Rank.KING, false) || checkHand(holeCards, Rank.ACE,Rank.QUEEN, false) ||
                checkHand(holeCards, Rank.KING,Rank.QUEEN, true) || checkHand(holeCards, Rank.SEVEN,Rank.SEVEN, false) ){
            return HandStrength.HALFWAYSUPERDUPER;
        }

        //fourth
        if (checkHand(holeCards, Rank.ACE,Rank.JACK) || checkHand(holeCards, Rank.QUEEN,Rank.JACK,true) || checkHand(holeCards, Rank.KING,Rank.JACK,true) ||
                checkHand(holeCards, Rank.KING,Rank.QUEEN, false) || checkHand(holeCards, Rank.KING,Rank.TEN, true) || checkHand(holeCards, Rank.ACE,Rank.TEN, true) ||
                checkHand(holeCards, Rank.ACE,Rank.TEN, true)){
            return HandStrength.PRETTYBADBUTNOTWORTHLESS;
        }
        //ELSE
        return HandStrength.WORTHLESS;

    }

    private static boolean checkHand(HoleCards hc, Rank r1, Rank r2) {
        return checkHand(hc,r1,r2,false);
    }

    private static boolean checkHand(HoleCards hc, Rank r1, Rank r2, boolean isSuited){
        List<Card> cards = hc.getCards();
        if (isSuited && cards.get(0).getSuit() != cards.get(1).getSuit()) {
            //
            return false;
        } else {
            // check if the cards match the specified rank
            return cards.get(0).getRank() == r1 && cards.get(1).getRank() == r2 || cards.get(1).getRank() == r1 && cards.get(0).getRank() == r2;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HoleCards holeCards = (HoleCards) o;

        return !(cards != null ? !cards.equals(holeCards.cards) : holeCards.cards != null);
    }

    @Override
    public int hashCode() {
        return cards != null ? cards.hashCode() : 0;
    }
}