package miller;

import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Column;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.view.BuildablePileView;

/*
 * Moves a card from a starting tableau buildable pile to a 
 * destination tableau buildable pile if the move is valid.
 * 
 * 4/19/15
 */

public class TableauToTableauMove extends Move {
	BuildablePile start;
	Card cardMoved;
	BuildablePile end;
	
	
	public TableauToTableauMove (BuildablePile fromTableau, Card cardMoved, BuildablePile src){
		this.start = fromTableau;
		this.cardMoved = cardMoved;
		this.end = src;		
	}

	@Override 
	public boolean doMove (Solitaire game) {

		// VALIDATE:
		if (valid (game) == false){ return false; }

		// EXECUTE:add top card of the starting tableau to the top of the ending tableau
        end.add(cardMoved);
        
        return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		// EXECUTE:add top card of the ending tableau to the top of the starting tableau
		if (end.empty()) { return false; }
		
		start.add(end.get());
		return true;

	}

	@Override
	public boolean valid(Solitaire game) {
		Card endTop = end.get();
		boolean validation = false;
		
		/*
		 * Valid if:
		 * Starting tableau is not empty.
		 * Starting tableau card is 1 rank higher than ending tableau card.
		 * Starting tableau card is 1 rank less than ending tableau card.
		 * Starting tableau card and ending tableau card have the same suit.
		 */
		
		if(end.empty()){
			validation = false;
		}else if((cardMoved.getSuit()==endTop.getSuit()) && ((cardMoved.getRank()+1==endTop.getRank()) ||
				(cardMoved.getRank()-1==endTop.getRank()))){
			validation = true;
	    }
		return validation;
	
		}
		
	}
