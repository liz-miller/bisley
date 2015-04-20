package miller;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;

public class TestTableauToTableauMove extends TestCase {
	
	public void testSimple(){
		Bisley bisleyGame = new Bisley();
		GameWindow gw = Main.generateWindow(bisleyGame, Deck.OrderBySuit);
		
		// obtain the top card of the 1st tableau pile
		BuildablePile startTableau = bisleyGame.tableau[1];
		BuildablePile endTableau = bisleyGame.tableau[2];
		
		//TableauToTableauMove ttm = new TableauToTableauMove(startTableau,endTableau);
		
		
	}
	
	
	
}
