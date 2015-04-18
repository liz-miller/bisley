package miller;

import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Pile;

public class MoveToAceFoundationMove extends ks.common.model.Move{
	/** The tableau buildable pile. */
	protected BuildablePile tableau;
	
	/** The Card being dragged. */
	protected Card draggingCard;
	
	/** The Ace Foundation Pile. */
	protected Pile aceFoundation;
	
	
	public MoveToAceFoundationMove(BuildablePile tableau, Card draggingCard, Pile aceFoundation) {
		super();
		
		this.tableau = tableau;
		this.draggingCard = draggingCard;
		this.aceFoundation = aceFoundation;
	}
	
	
	public boolean doMove (Solitaire theGame) {
		// Verify we can do the move.
		if (valid (theGame) == false)
			return false;

		// EXECUTE:
		// Deal with both situations
		if (draggingCard == null)
			aceFoundation.add (tableau.get());
		else
			aceFoundation.add (draggingCard);

		// advance score by adding the rank of the draggingCard to the current total.
		theGame.updateScore (draggingCard.getRank());
		return true;
	}
	
	public boolean undo(ks.common.games.Solitaire game) {
		// VALIDATE:
		if (aceFoundation.empty()) return false;

		// EXECUTE:
		// remove card and move to aceFoundation
		tableau.add (aceFoundation.get());

		// reverse score advance
		game.updateScore (-draggingCard.getRank());
		return true;
	}

	/**
	 * Action for bisley: Tableau card dragged to Ace Foundation Pile.
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
		 * Ace foundation is not empty.
		 * The card's rank is 1 greater than the Ace foundation's rank.
		 * The card and the foundation have the same suit.
		 */
		
		if (!aceFoundation.empty() && (c.getRank() == aceFoundation.rank() + 1) && (c.getSuit() == aceFoundation.suit()))
			
			// Hearts: build up to 4
			if(c.getSuit()==Card.HEARTS && aceFoundation.rank() <=4){
				validation = true;
			}
			// Diamonds: build up to 8
			if(c.getSuit()==Card.DIAMONDS && aceFoundation.rank()<=8){
				validation = true;
			}
			// Spades: build up to Q
			if(c.getSuit()==Card.SPADES && aceFoundation.rank()<=12){
				validation = true;
			}
			// The suit is not clubs.
			if(aceFoundation.suit()==Card.CLUBS){
				validation = false;
			}
			
					
		/*
		 * The foundation is never empty.
		 */
		if (aceFoundation.empty())
			validation = false;

		return validation;
	}
	
}
