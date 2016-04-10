package se.cygni.texasholdem.player;

import se.cygni.texasholdem.game.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 10/04/16.
 */
public class HoleCards {

    public List<Card> cards;
    public HandStrength strength;


    public HoleCards(List<Card> cards) {
        this.cards = new ArrayList<Card>(cards);
        this.strength = calculateStrength(this);
    }

    private static HandStrength calculateStrength(HoleCards holeCards) {
        return null;
    }
}

}
