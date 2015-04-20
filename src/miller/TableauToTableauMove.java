package miller;

import ks.common.games.Solitaire;
import ks.common.model.Column;
import ks.common.model.Card;
import ks.common.model.Move;

/*
 * Moves a card from a starting tableau buildable pile to a 
 * destination tableau buildable pile if the move is valid.
 * 
 * 4/19/15
 */

public class TableauToTableauMove extends Move {
	Column start;
	Card cardMoved;
	Column end;
	
	
	public TableauToTableauMove (Column start, Card cardMoved, Column end){
		this.start = start;
		this.cardMoved = cardMoved;
		this.end = end;
		
	}

	@Override 
	public boolean doMove (Solitaire game) {

		// VALIDATE:
        if (valid(game) == false) { return false; }

		// EXECUTE:add top card of the starting tableau to the top of the ending tableau
        cardMoved = start.get();
        end.add(cardMoved);
        
        return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		// EXECUTE:add top card of the ending tableau to the top of the starting tableau
		cardMoved = end.get();
		start.add(cardMoved);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		Card startTop = start.get();
		Card endTop = end.get();
		boolean validation = false;
		
		/*
		 * Valid if:
		 * Starting tableau is not empty.
		 * Starting tableau card is 1 rank higher than ending tableau card.
		 * Starting tableau card is 1 rank less than ending tableau card.
		 * Starting tableau card and ending tableau card have the same suit.
		 */
		if(!start.empty() && (startTop.getSuit()==endTop.getSuit()) && ((startTop.getRank()+1==endTop.getRank()) || (startTop.getRank()-1==endTop.getRank()))){
			validation = true;
			}
			return validation;
		}
		
	}
