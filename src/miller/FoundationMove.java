package miller;

import ks.common.games.Solitaire;

import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * FoundationMove: Moves cards from a BuildablePile to a Pile.
 * @param Bisley
 * @param BuildablePile
 * @param Card
 * @param Pile
 * @param boolean
 * @author Liz Miller
 */

public class FoundationMove extends Move {
	Bisley bisley;
	BuildablePile tableau;
	Card cardMoved;
	Pile foundation;
	boolean isAce;
	
	public FoundationMove(Bisley bisley, BuildablePile tableau, Card cardMoved, Pile foundation, boolean isAce){
		this.bisley = bisley;
		this.tableau = tableau;
		this.cardMoved = cardMoved;
		this.foundation = foundation;
		this.isAce = isAce;
	}

	@Override
	public boolean doMove(Solitaire game) {
		// Verify we can do the move.
		if (valid (game) == false){ return false; }

		foundation.add (cardMoved);

		// advance score by adding the rank of the cardMoved to the current total.
		game.updateScore (+1);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		// VALIDATE:
		if (foundation.empty()) { return false; }

		// EXECUTE:
		// remove card and move to aceFoundation
		
		tableau.add (foundation.get());

		// reverse score advance
		game.updateScore (-1);
		return true;
	}

	/**
	 * Action for bisley: Tableau card dragged to Ace Foundation Pile.
	 * <p>
	 * @param d ks.common.games.Solitaire
	 */
	@Override
	public boolean valid(Solitaire game) {
		// VALIDATION:
		boolean validation = false;
		boolean checkKings = false; 

		if (cardMoved != null) {
			/*
			 *  not buildablePile.empty()
			 *  tableau has a rank one less than the foundation
			 *  tableau and foundation have the same suit
			 */
			if(foundation.empty()){
				validation = true;
			}else if (!foundation.empty() && (cardMoved.getRank() == foundation.rank() + 1 || cardMoved.getRank() == foundation.rank()-1) && (cardMoved.getSuit() == foundation.suit())){
				validation = true;
			}
			/*
			 * foundation.empty()
			 * not buildablePile.empty()
			 * tableau.rank() == KING
			 */
//			if (cardMoved.getRank() == Card.KING){
//				validation = true;  
//		    }else {
			/* 
			 * Verify the following:
			 * Ace foundation is not empty.
			 * The card's rank is 1 greater than the Ace foundation's rank.
			 * The card and the foundation have the same suit.
			 */	
			if (isAce && (cardMoved.getRank() == foundation.rank() + 1) && (cardMoved.getSuit() == foundation.suit() && cardMoved.getRank()!=Card.KING)){
	
				// Hearts: build up to 4
				if(cardMoved.getSuit()==Card.HEARTS && foundation.rank() <=4){
					validation = true;
				}
				// Diamonds: build up to 8
				if(cardMoved.getSuit()==Card.DIAMONDS && foundation.rank()<=8){
					validation = true;
				}
				// Spades: build up to Q
				if(cardMoved.getSuit()==Card.SPADES && foundation.rank()<=12){
					validation = true;
				}
				// The suit is not clubs.
				if(foundation.suit()==Card.CLUBS){
					validation = false;
				}
			} else if (!isAce){ //going to king foundation
				/* 
				 * Verify the following: 
				 * 
				 * The card's rank is 1 less than the King foundation's rank.
				 * The card and the foundation have the same suit.
				 * The suit is not spades.
				 */
				
				if(foundation.empty() && cardMoved.getRank()!=Card.KING){
					validation = false; 
				}
				
				
				if (!foundation.empty() && (cardMoved.getRank() == foundation.rank() - 1) && (cardMoved.getSuit() == foundation.suit() && cardMoved.getRank()!=Card.KING)){
	
					// Hearts: build down to 5
					if(cardMoved.getSuit()==Card.HEARTS && (cardMoved.getRank() >=5 || cardMoved.getRank()<=13)){
						validation = true;
					}
					// Diamonds: build down to 9
					if(cardMoved.getSuit()==Card.DIAMONDS && (cardMoved.getRank()>=9 || cardMoved.getRank()<=13)){
						validation = true;
					}
					// Clubs: build down to 2
					if(cardMoved.getSuit()==Card.CLUBS && (cardMoved.getRank()>=2 || cardMoved.getRank()<=13)){
						validation = true;
					}
					// The suit is not spades.
					if(foundation.suit()==Card.SPADES || cardMoved.getRank()==Card.ACE){
						validation = false;
					}
				}
		}
	 }
	
		return validation;
}
}

