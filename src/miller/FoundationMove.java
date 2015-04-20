package miller;

import ks.common.games.Solitaire;
import ks.common.model.Column;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;

public class FoundationMove extends Move {
	Column tableau;
	Card cardMoved;
	Pile foundation;
	boolean isAce;
	
	public FoundationMove(Column tableau, Card cardMoved, Pile foundation, boolean isAce){
		this.tableau = tableau;
		this.cardMoved = cardMoved;
		this.foundation = foundation;
		this.isAce = isAce;
	}

	@Override
	public boolean doMove(Solitaire game) {
		// Verify we can do the move.
		if (valid (game) == false)
			return false;

		// EXECUTE:
		// Deal with both situations
		if (cardMoved== null)
			return false;
		else
			foundation.add (cardMoved);

		// advance score by adding the rank of the draggingCard to the current total.
		game.updateScore (+1);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		// VALIDATE:
		if (foundation.empty()) return false;

		// EXECUTE:
		// remove card and move to aceFoundation
		Card undoMove = foundation.get();
		tableau.add (undoMove);

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

		// If draggingCard is null, then action has not taken place.
		Card c;
		if (cardMoved == null) {
			if (tableau.empty()) return false;   // NOTHING TO EXTRACT!
			c = tableau.peek();
		} else {
			c = cardMoved;
		}

		/* 
		 * Verify the following:
		 * Ace foundation is not empty.
		 * The card's rank is 1 greater than the Ace foundation's rank.
		 * The card and the foundation have the same suit.
		 */	
		if (isAce && !foundation.empty() && (c.getRank() == foundation.rank() + 1) && (c.getSuit() == foundation.suit()))

			// Hearts: build up to 4
			if(c.getSuit()==Card.HEARTS && foundation.rank() <=4){
				validation = true;
			}
		// Diamonds: build up to 8
		if(c.getSuit()==Card.DIAMONDS && foundation.rank()<=8){
			validation = true;
		}
		// Spades: build up to Q
		if(c.getSuit()==Card.SPADES && foundation.rank()<=12){
			validation = true;
		}
		// The suit is not clubs.
		if(foundation.suit()==Card.CLUBS){
			validation = false;
		}

		/*
		 * The foundation is only empty if it belongs to kings
		 */
		if (foundation.empty()){
			// If draggingCard is null, then action has not taken place.
			if (cardMoved == null) {
				if (tableau.empty()) return false;   // NOTHING TO EXTRACT!
				c = tableau.peek();
			} else {
				c = cardMoved;
			}

			/* 
			 * Verify the following:
			 * King foundation is not empty.
			 * The card's rank is 1 less than the King foundation's rank.
			 * The card and the foundation have the same suit.
			 * The suit is not spades.
			 */

			if (!foundation.empty() && (c.getRank() == foundation.rank() - 1) && (c.getSuit() == foundation.suit()))

				// Hearts: build down to 5
				if(c.getSuit()==Card.HEARTS && foundation.rank() >=5){
					validation = true;
				}
			// Diamonds: build down to 9
			if(c.getSuit()==Card.DIAMONDS && foundation.rank()>=9){
				validation = true;
			}
			// Clubs: build down to 2
			if(c.getSuit()==Card.CLUBS && foundation.rank()>=2){
				validation = true;
			}
			// The suit is not spades.
			if(foundation.suit()==Card.SPADES){
				validation = false;
			}
		}

		return validation;
	}

}
