package miller;

import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Move;
import ks.common.model.Pile;


/**
 * Move card from top of tableau Pile to the top of a foundationPile.
 */
public class FoundationMove extends Move {
	BuildablePile tableau;
	Card cardBeingDragged;
	Pile foundation;

	public FoundationMove(BuildablePile from, Card cardBeingDragged, Pile to) {
		this.tableau = from;
		this.cardBeingDragged = cardBeingDragged;
		this.foundation = to;
	}
	
	@Override
	public boolean doMove(Solitaire game) {
		if (!valid(game)) { return false; }
		
		foundation.add(cardBeingDragged);
		game.updateScore(+1);
		return true;
	}

	@Override
	public boolean undo(Solitaire game) {
		tableau.add(foundation.get());
		game.updateScore(-1);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		// TODO Auto-generated method stub
		return false;
	}

}
