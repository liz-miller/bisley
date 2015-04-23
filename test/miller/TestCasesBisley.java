package miller;

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
import ks.tests.KSTestCase;

public class TestCasesBisley extends KSTestCase {
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
	public void testFoundationMoveToAceTrue(){
		setUp();

		// Move tableau to Ace Foundation (Case is true)
		Card topCard = bisley.tableau[11].peek(); //look at the 2H on tableau 11
		//try to move the 2H to AH foundation
		FoundationMove fm = new FoundationMove(bisley,bisley.tableau[11], topCard, bisley.aces[3], true); //try to move the card to the foundation
		assertTrue(fm.valid(bisley));
		
		fm.doMove(bisley); //actually do the foundation move
		assertEquals(1, bisley.getScoreValue()); //verify that the score has been updated to 1
		assertEquals(topCard, bisley.aces[3].peek());//verify that the topCard has been moved to the ace foundation (if true)
		
		fm.undo(bisley); //undo the foundation move
		assertEquals(topCard, bisley.tableau[11].peek());//verify that the topCard has been moved back to the tableau foundation (if true)
		assertEquals(0, bisley.getScoreValue());	

	}
	
	public void testFoundationMoveToAceFalse(){
		
		setUp();
		// Move tableau to Ace Foundation (Case is false)
		Card topCard = bisley.tableau[12].peek(); //look at the 6C on tableau 12
		//try to move the 6C to AH foundation
		FoundationMove fm = new FoundationMove(bisley,bisley.tableau[12], topCard, bisley.aces[3], true); //try to move the card to the foundation
		assertFalse(fm.valid(bisley));
		
		fm.doMove(bisley); //actually do the foundation move
		assertEquals(0, bisley.getScoreValue()); //verify that the score has been updated to 0
		assertEquals(topCard, bisley.tableau[12].peek());//verify that the topCard has been moved back to its origin tableau	

		
	}
	
	public void testFoundationMoveToKingTrue(){
		setUp();
		// Move tableau to King Foundation (Case is true)
		Card topCard = bisley.tableau[2].peek(); //look at the KH on tableau 2
		//try to move the KH to KH foundation
		FoundationMove fm = new FoundationMove(bisley,bisley.tableau[2], topCard, bisley.kings[3], false); //try to move the card to the foundation
		assertTrue(fm.valid(bisley));

		fm.doMove(bisley); //actually do the foundation move
		assertEquals(1, bisley.getScoreValue()); //verify that the score has been updated to 1
		assertEquals(topCard, bisley.kings[3].peek());//verify that the topCard has been moved to the ace foundation (if true)
			
		fm.undo(bisley); //undo the foundation move
		assertEquals(topCard, bisley.tableau[2].peek());//verify that the topCard has been moved back to its origin tableau	
		assertEquals(0, bisley.getScoreValue());	
		
	}
	
	public void testFoundationMoveToKingFalse(){
//		setUp();
//		
//		Card topCard = bisley.tableau[2].peek();
//		FoundationMove fm = new FoundationMove(bisley,bisley.tableau[2],topCard, bisley.kings[1],false);
//		assertFalse(fm.valid(bisley));
//		
//		fm.doMove(bisley);
//		assertEquals(topCard, bisley.tableau[2].peek());
				
	}
		
	public void testAutoMove(){
		setUp();
		// Move tableau to King Foundation (Case is true)
		Card topCard = bisley.tableau[2].peek(); //look at the KH on tableau 2
		//try to move the KH to KH foundation
		FoundationMove fm = new FoundationMove(bisley,bisley.tableau[2], topCard, bisley.kings[3], false); //try to move the card to the foundation
		assertTrue(fm.valid(bisley));
		fm.doMove(bisley);
		assertEquals(1, bisley.getScoreValue());				
	}
	
	public void testTableauToTableauMove(){
		bisley = new Bisley();
		gw = Main.generateWindow(bisley, Deck.OrderBySuit);
		
		Card topCard = bisley.tableau[1].peek();
		TableauToTableauMove ttm = new TableauToTableauMove(bisley.tableau[1], topCard, bisley.tableau[9]);
		assertTrue(ttm.valid(bisley));
		
		ttm.doMove(bisley);
		assertEquals(2,bisley.tableau[9].count());
		ttm.undo(bisley);
			
		ttm = new TableauToTableauMove(bisley.tableau[1], topCard, bisley.tableau[2]);
		assertFalse(ttm.valid(bisley));
		
		bisley.tableau[1].removeAll(); //remove all cards from tableau 1
		
		topCard = bisley.tableau[2].peek(); //obtain top card of tableau 2
		
		ttm = new TableauToTableauMove(bisley.tableau[2], topCard, bisley.tableau[1]);
		assertFalse(ttm.valid(bisley)); //validate that no card can be moved on an empty tableau pile
		
		ttm.doMove(bisley);
		assertEquals(topCard, bisley.tableau[2].peek());
				
	}
	
	public void testMoveColumnMove(){
		setUp();
		// first create a mouse event
		MouseEvent pr = createPressed (bisley, bisley.tableauView[1], 0, 0);
		for (int i = 1; i < 13; i++) {
			bisley.tableauView[i].getMouseManager().handleMouseEvent(pr);
		}

		// Now clear room for the 2 to go down to empty buildablePile 2
		pr = createPressed (bisley, bisley.tableauView[2], 0, bisley.tableauView[2].getSmallOverlap());
		ColumnView cv = bisley.tableauView[2].getColumnView(pr);
		MoveColumnMove mcm = new MoveColumnMove (bisley.tableau[2], bisley.tableau[7], (Column) cv.getModelElement(), 1); 
		mcm.doMove(bisley);

		assertEquals (4, bisley.tableau[1].count());

		// move column 7 back to 1
		pr = createPressed (bisley, bisley.tableauView[7], 0, 6*bisley.tableauView[7].getSmallOverlap());
		cv = bisley.tableauView[7].getColumnView(pr);
		mcm = new MoveColumnMove (bisley.tableau[7], bisley.tableau[1], (Column) cv.getModelElement(), 1); 
		mcm.doMove(bisley);

		assertEquals (4, bisley.tableau[1].count());

		bisley.getContainer().repaint();	
		
	}
	
	public void testTableauToTableauController(){
		setUp();
		
		//false event. card will not be moved (press only)
		// first create a mouse event on 7th tableau 
		MouseEvent pr = createPressed (bisley, bisley.tableauView[7], 0, 0);
		bisley.tableauView[7].getMouseManager().handleMouseEvent(pr);

		// drop on the first column
		MouseEvent rel = createReleased (bisley, bisley.tableauView[1], 0, 0);
		bisley.tableauView[1].getMouseManager().handleMouseEvent(rel);
		
		// verify that a press doesn't modify a tableau pile
		assertEquals (4, bisley.tableau[7].count());
		assertEquals(4, bisley.tableau[1].count());
		
		//false event. card will not be moved (drag, but with false move validation)
		
		
		
	}

	
}
