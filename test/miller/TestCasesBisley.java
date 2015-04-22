package miller;

import heineman.klondike.FlipCardMove;
import heineman.klondike.MoveColumnMove;
import heineman.klondike.MoveWasteToPileMove;

import java.awt.event.MouseEvent;

import miller.Bisley;
import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Stack;
import ks.common.view.ColumnView;
import ks.launcher.Main;

public class TestCasesBisley extends TestCase {
	Bisley bisley;
	GameWindow gw;
	
	@Override
	protected void setUp(){
		bisley = new Bisley();
		gw = Main.generateWindow(bisley, Deck.OrderBySuit);
		
	}
	
	@Override
	protected void tearDown(){
		gw.setVisible(false);
		gw.dispose();
	}
	
	/*
	 * 
	 */
	public void testFoundationMove(){
		setUp();

		// Move tableau to Ace Foundation (Case is true)
		Card topCard = bisley.tableau[11].peek(); //look at the 2H on tableau 11
		//try to move the 2H to AH foundation
		FoundationMove fm = new FoundationMove(bisley.tableau[11], topCard, bisley.aces[3], true); //try to move the card to the foundation
		assertTrue(fm.valid(bisley));
		
		fm.doMove(bisley); //actually do the foundation move
		assertEquals(1, bisley.getScoreValue()); //verify that the score has been updated to 1
		assertEquals(topCard, bisley.aces[3].peek());//verify that the topCard has been moved to the ace foundation (if true)
		
		fm.undo(bisley); //undo the foundation move
		assertEquals(topCard, bisley.tableau[11].peek());//verify that the topCard has been moved back to the tableau foundation (if true)
		assertEquals(0, bisley.getScoreValue());	
		
		// Move tableau to Ace Foundation (Case is false)
		topCard = bisley.tableau[12].peek(); //look at the 6C on tableau 12
		//try to move the 6C to AH foundation
		fm = new FoundationMove(bisley.tableau[12], topCard, bisley.aces[3], true); //try to move the card to the foundation
		assertFalse(fm.valid(bisley));
		
		fm.doMove(bisley); //actually do the foundation move
		assertEquals(0, bisley.getScoreValue()); //verify that the score has been updated to 0
		assertEquals(topCard, bisley.tableau[12].peek());//verify that the topCard has been moved back to its origin tableau	
		
		
		// Move tableau to King Foundation (Case is true)
		topCard = bisley.tableau[2].peek(); //look at the KH on tableau 2
		//try to move the KH to KH foundation
		fm = new FoundationMove(bisley.tableau[2], topCard, bisley.kings[3], false); //try to move the card to the foundation
		assertTrue(fm.valid(bisley));

		fm.doMove(bisley); //actually do the foundation move
		assertEquals(1, bisley.getScoreValue()); //verify that the score has been updated to 1
		assertEquals(topCard, bisley.kings[3].peek());//verify that the topCard has been moved to the ace foundation (if true)
			
		fm.undo(bisley); //undo the foundation move
		assertEquals(topCard, bisley.tableau[2].peek());//verify that the topCard has been moved back to its origin tableau	
		assertEquals(0, bisley.getScoreValue());	 
		
	}
	
	public void testAutoMove(){
		setUp();
		// Move tableau to King Foundation (Case is true)
		Card topCard = bisley.tableau[2].peek(); //look at the KH on tableau 2
		//try to move the KH to KH foundation
		FoundationMove fm = new FoundationMove(bisley.tableau[2], topCard, bisley.kings[3], false); //try to move the card to the foundation
		assertTrue(fm.valid(bisley));
		fm.doMove(bisley);
		assertEquals(1, bisley.getScoreValue());

				
	}
	
	public void testTableauToTableauMove(){
		setUp();
		
		Card topCard = bisley.tableau[1].peek();
		TableauToTableauMove ttm = new TableauToTableauMove(bisley.tableau[1], topCard, bisley.tableau[9]);
		assertTrue(ttm.valid(bisley));
		
		ttm.doMove(bisley);
		//assertEquals(topCard,bisley.tableau[9].peek());
		ttm.undo(bisley);
		//assertEquals(topCard, bisley.tableau[1].peek());
		
		ttm = new TableauToTableauMove(bisley.tableau[1], topCard, bisley.tableau[2]);
		assertFalse(ttm.valid(bisley));
		
		bisley.tableau[1].removeAll(); //remove all cards from tableau 1
		
		topCard = bisley.tableau[2].peek(); //obtain top card of tableau 2
		
		ttm = new TableauToTableauMove(bisley.tableau[2], topCard, bisley.tableau[1]);
		assertFalse(ttm.valid(bisley)); //validate that no card can be moved on an empty tableau pile
		
		ttm.doMove(bisley);
		assertEquals(topCard, bisley.tableau[2].peek());
				
	}
	
	public void testBuildablePileController(){

	}
	
	public void testMoveColumnMove(){
		
	}
	
	
}
