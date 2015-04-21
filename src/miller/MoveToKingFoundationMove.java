package miller;

import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Pile;

public class MoveToKingFoundationMove extends ks.common.model.Move{
	/** The tableau buildable pile. */
	protected BuildablePile tableau;
	
	/** The Card being dragged. */
	protected Card draggingCard;
	
	/** The King Foundation Pile. */
	protected Pile kingFoundation;
	
	
	public MoveToKingFoundationMove(BuildablePile tableau, Card draggingCard, Pile kingFoundation) {
		super();
		
		this.tableau = tableau;
		this.draggingCard = draggingCard;
		this.kingFoundation = kingFoundation;
	}
	
	
	public boolean doMove (Solitaire theGame) {
		// Verify we can do the move.
		if (valid (theGame) == false)
			return false;

		// EXECUTE:
		// Deal with both situations
		if (draggingCard == null)
			kingFoundation.add (tableau.get());
		else
			kingFoundation.add (draggingCard);

		// advance score
		theGame.updateScore (draggingCard.getRank());
		return true;
	}
	
	public boolean undo(ks.common.games.Solitaire game) {
		// VALIDATE:
		if (kingFoundation.empty()) return false;

		// EXECUTE:
		// remove card and move to kingFoundation
		tableau.add (kingFoundation.get());

		// reverse score advance
		game.updateScore (-draggingCard.getRank());
		return true;
	}

	/**
	 * Action for bisley: Tableau card dragged to King Foundation Pile.
	 * <p>
	 * @param d ks.common.games.Solitaire
	 */
	public boolean valid(ks.common.games.Solitaire game) {
		// VALIDATION:
		boolean validation = false;

		// If draggingCard is null, then action has not taken place.
		Card c;
		if (draggingCard == null) {
			if (tableau.empty()) return false;   // NOTHING TO EXTRACT!
			c = tableau.peek();
		} else {
			c = draggingCard;
		}
		
		/* 
		 * Verify the following:
		 * King foundation is not empty.
		 * The card's rank is 1 less than the King foundation's rank.
		 * The card and the foundation have the same suit.
		 * The suit is not spades.
		 */
		
		if ((c.getRank()==Card.KING)){
			if((c.getRank() == kingFoundation.rank() - 1) && (c.getSuit() == kingFoundation.suit())){
				// Hearts: build down to 5
				if(c.getSuit()==Card.HEARTS && kingFoundation.rank() >=5){
					validation = true;
				}
				// Diamonds: build down to 9
				if(c.getSuit()==Card.DIAMONDS && kingFoundation.rank()>=9){
					validation = true;
				}
				// Clubs: build down to 2
				if(c.getSuit()==Card.CLUBS && kingFoundation.rank()>=2){
					validation = true;
				}
				// The suit is not spades.
				if(kingFoundation.empty() && c.getSuit()==Card.SPADES && c.getRank()==Card.KING){
					validation = true;
				}

			}
		}

		/*
		 * Verify that the first card in the foundation is a king.
		 */
//		if (kingFoundation.empty() && (c.getRank() == Card.KING))
//			validation = true;

		return validation;
	}
	
}
