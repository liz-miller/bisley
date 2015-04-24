package miller;

import java.awt.event.MouseEvent;

import vfinal.NarcoticFinal;
import miller.Bisley;
import ks.client.gamefactory.GameWindow;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;
import ks.launcher.Main;
import ks.tests.KSTestCase;
import ks.tests.model.ModelFactory;

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
	
	/**
	 * Tests the initialization of the Bisley solitaire game.
	 */
	public void testInitialize(){
		//verify that aces start in the assigned ace foundation
		Card aces1 = bisley.aces[1].peek();
		Card aces2 = bisley.aces[2].peek();
		Card aces3 = bisley.aces[3].peek();
		Card aces4 = bisley.aces[4].peek();
		
		Card aceClubs = new Card (1,1);
		Card aceDia = new Card (1,2);
		Card aceHeart = new Card (1,3);
		Card aceSpades = new Card(1,4);
		
		assertEquals(aceClubs, aces1);
		assertEquals(aceDia, aces2);
		assertEquals(aceHeart, aces3);
		assertEquals(aceSpades, aces4);
		
		//verify that tableau piles 1-9 start with 4 cards
		for(int tableauNum=1; tableauNum<=9; tableauNum++){
			assertEquals(4, bisley.tableau[tableauNum].count());
		}
		//verify that tableau piles 10-13 start with 3 cards
		for(int tableauNum=10; tableauNum<=13; tableauNum++){
			assertEquals(3, bisley.tableau[tableauNum].count());
		}
		
		//verify that the King foundations are empty
		for(int i=1; i<=4; i++){
			assertTrue(bisley.kings[i].empty());
		}
		
	} 
	
	/**
	 * 
	 */
	public void testFoundationMoveToAceTrue(){
		// Move tableau to Ace Foundation (Case is true)
		Card topCard = bisley.tableau[11].peek(); //look at the 2H on tableau 11
		//try to move the 2H to AH foundation
		FoundationMove fm = new FoundationMove(bisley,bisley.tableau[11], topCard, bisley.aces[3], true); //try to move the card to the foundation
		assertTrue(fm.valid(bisley));
		
		fm.doMove(bisley); //actually do the foundation move
		assertEquals(1, bisley.getScoreValue()); //verify that the score has been updated to 1
		assertEquals(topCard, bisley.aces[3].peek());//verify that the topCard has been moved to the ace foundation (if true)
		bisley.getContainer().repaint();
		
		fm.undo(bisley); //undo the foundation move
		assertEquals(topCard, bisley.tableau[11].peek());//verify that the topCard has been moved back to the tableau foundation (if true)
		assertEquals(0, bisley.getScoreValue());
		bisley.getContainer().repaint();
		
		
		//Move 2D to diamond ace foundation
		ModelFactory.init(bisley.tableau[13], "5S 4H 2D");
		// verify that the top card is now a 2C
		assertEquals ("2D", bisley.tableau[13].peek().toString());
		fm = new FoundationMove(bisley,bisley.tableau[13],bisley.tableau[13].peek(),bisley.aces[2],true);
		assertTrue(fm.valid(bisley));
		
		fm.doMove(bisley);
		assertEquals(bisley.tableau[13].peek(),bisley.aces[2].peek());
		bisley.getContainer().repaint();
		
		//Move 2S to spade ace foundation
		ModelFactory.init(bisley.tableau[13], "5S 4H 2S");
		// verify that the top card is now a 2C
		assertEquals ("2S", bisley.tableau[13].peek().toString());
		fm = new FoundationMove(bisley,bisley.tableau[13],bisley.tableau[13].peek(),bisley.aces[4],true);
		assertTrue(fm.valid(bisley));
		
		fm.doMove(bisley);
		assertEquals(bisley.tableau[13].peek(),bisley.aces[4].peek());
		bisley.getContainer().repaint();
		
		//Move 2C to club foundation
		ModelFactory.init(bisley.aces[1], "AC 2C");
				// verify that the top card is now a 2C
		assertEquals ("2C", bisley.aces[1].peek().toString());
		
		// validate that the 3C from tableau 8 can be moved to the top of ace foundation 1
		fm = new FoundationMove(bisley, bisley.tableau[8], bisley.tableau[8].peek(), bisley.aces[1],true);
			
		// make move.
		fm.doMove(bisley);
		assertEquals (4, bisley.tableau[8].count());
		assertEquals ("2C",bisley.aces[1].peek().toString());

	}
	
	/**
	 * 
	 */
	public void testFoundationMoveToAceFalse(){
		//Case: Move tableau to Ace Foundation (Case is false)
		Card topCard = bisley.tableau[12].peek(); //look at the 6C on tableau 12
		//try to move the 6C to AH foundation
		FoundationMove fm = new FoundationMove(bisley,bisley.tableau[12], topCard, bisley.aces[3], true); //try to move the card to the foundation
		assertFalse(fm.valid(bisley));
		
		fm.doMove(bisley); //actually do the foundation move
		assertEquals(0, bisley.getScoreValue()); //verify that the score has been updated to 0
		assertEquals(topCard, bisley.tableau[12].peek());//verify that the topCard has been moved back to its origin tableau	
		bisley.getContainer().repaint();

		//Case: Move 2D to heart ace foundation
		ModelFactory.init(bisley.tableau[13], "5S 4H 2D");
		// verify that the top card is now a 2C
		assertEquals ("2D", bisley.tableau[13].peek().toString());
		fm = new FoundationMove(bisley,bisley.tableau[13],bisley.tableau[13].peek(),bisley.aces[3],true);
		assertFalse(fm.valid(bisley));
		
		fm.doMove(bisley);
		assertEquals("AD",bisley.aces[2].peek().toString());
		bisley.getContainer().repaint();
			
	}
	
	
	/**
	 * 
	 */
	public void testFoundationMoveToKingTrue(){
		//Case: Move tableau to Empty King Foundation (Case is true)
		Card topCard = bisley.tableau[2].peek(); //look at the KH on tableau 2
		//try to move the KH to KH foundation
		FoundationMove fm = new FoundationMove(bisley,bisley.tableau[2], topCard, bisley.kings[3], false); //try to move the card to the foundation
		assertTrue(fm.valid(bisley));
		bisley.getContainer().repaint();

		fm.doMove(bisley); //actually do the foundation move
		assertEquals(1, bisley.getScoreValue()); //verify that the score has been updated to 1
		assertEquals(topCard, bisley.kings[3].peek());//verify that the topCard has been moved to the ace foundation (if true)
		bisley.getContainer().repaint();
			
		fm.undo(bisley); //undo the foundation move
		assertEquals(topCard, bisley.tableau[2].peek());//verify that the topCard has been moved back to its origin tableau	
		assertEquals(0, bisley.getScoreValue());
		bisley.getContainer().repaint();
		
		//Case: Move card to king foundation with king already there
		ModelFactory.init(bisley.kings[1], "KC QC");
		// verify that the top card is now a 2C
		assertEquals ("QC", bisley.kings[1].peek().toString());
		fm = new FoundationMove(bisley,bisley.tableau[3],bisley.tableau[3].peek(),bisley.kings[1],true);
		assertTrue(fm.valid(bisley));
		
		fm.doMove(bisley);
		assertEquals("JC",bisley.kings[1].peek().toString());
		bisley.getContainer().repaint();
		
	}
	
	/**
	 * 
	 */
	public void testFoundationMoveToKingFalse(){
		Card topCard = bisley.tableau[2].peek();
		FoundationMove fm = new FoundationMove(bisley,bisley.tableau[2], topCard, bisley.kings[1],false);
			
		fm.doMove(bisley);
		assertEquals(topCard, bisley.tableau[2].peek());
		bisley.getContainer().repaint();
		
		topCard = bisley.tableau[1].peek();
		FoundationMove fmm = new FoundationMove(bisley, bisley.tableau[1], topCard, bisley.kings[1],false);
		//assertTrue(fmm.valid(bisley));
		//bisley.getContainer().repaint();
		
		fmm.doMove(bisley);
		assertEquals(topCard, bisley.tableau[1].peek());
		bisley.getContainer().repaint();
		
		//Case: move a non-king to an empty king foundation pile
		bisley.kings[1].removeAll();
		fm = new FoundationMove(bisley, bisley.tableau[13], bisley.tableau[13].peek(), bisley.kings[1],false);
		assertFalse(fm.valid(bisley));
		
		
		//Case: Move card to king foundation with king already there
		ModelFactory.init(bisley.kings[1], "KC QC JC 10C 9C");
		ModelFactory.init(bisley.tableau[4], "QS 4D 8H 10C");
		assertEquals ("9C", bisley.kings[1].peek().toString());
		assertEquals ("10C", bisley.tableau[4].peek().toString());
		
		fm = new FoundationMove(bisley,bisley.tableau[4],bisley.tableau[4].peek(),bisley.kings[1],true);
		assertFalse(fm.valid(bisley));

		fm.doMove(bisley);
		assertEquals("9C",bisley.kings[1].peek().toString());
		assertEquals("10C", bisley.tableau[4].peek().toString());
		bisley.getContainer().repaint();
		
		//Case: Move card to king foundation with king already there (no spades)
		ModelFactory.init(bisley.kings[4], "KS");
		ModelFactory.init(bisley.tableau[1], "QS 4D 8H QS");
		assertEquals ("KS", bisley.kings[4].peek().toString());
		assertEquals ("QS", bisley.tableau[1].peek().toString());

		fm = new FoundationMove(bisley,bisley.tableau[1],bisley.tableau[1].peek(),bisley.kings[4],false);
		assertFalse(fm.valid(bisley));

		fm.doMove(bisley);
		assertEquals("KS",bisley.kings[4].peek().toString());
		assertEquals("QS", bisley.tableau[1].peek().toString());
		bisley.getContainer().repaint();
		
		//Case: Move card to king foundation with King already there (not less than 5 on King of Hearts)
		ModelFactory.init(bisley.kings[3], "KH QH JH 10H 9H 8H 7H 6H 5H");
		ModelFactory.init(bisley.tableau[1], "QS 4D 8H 4H");
		assertEquals ("5H", bisley.kings[3].peek().toString());
		assertEquals ("4H", bisley.tableau[1].peek().toString());

		fm = new FoundationMove(bisley,bisley.tableau[1],bisley.tableau[1].peek(),bisley.kings[3],false);
		assertFalse(fm.valid(bisley));

		fm.doMove(bisley);
		assertEquals("5H",bisley.kings[3].peek().toString());
		assertEquals("4H", bisley.tableau[1].peek().toString());
		bisley.getContainer().repaint();
		
		
		//Case: Move card to king foundation with King already there (not less than 9 on King of Hearts)
		ModelFactory.init(bisley.kings[2], "KD QD JD 10D 9D");
		ModelFactory.init(bisley.tableau[1], "QS 4D 8H 8D");
		assertEquals ("9D", bisley.kings[2].peek().toString());
		assertEquals ("8D", bisley.tableau[1].peek().toString());

		fm = new FoundationMove(bisley,bisley.tableau[1],bisley.tableau[1].peek(),bisley.kings[2],false);
		assertFalse(fm.valid(bisley));

		fm.doMove(bisley);
		assertEquals("9D",bisley.kings[2].peek().toString());
		assertEquals("8D", bisley.tableau[1].peek().toString());
		bisley.getContainer().repaint();
		
		//Case: Move card to king foundation with King already there (not less than 9 on King of Hearts)
		ModelFactory.init(bisley.kings[1], "KC QC JC 10C 9C 8C 7C 6C 5C 4C 3C 2C");
		ModelFactory.init(bisley.tableau[1], "QS 4D 8H AC"); //this case would never happen
		assertEquals ("2C", bisley.kings[1].peek().toString());
		assertEquals ("AC", bisley.tableau[1].peek().toString());

		fm = new FoundationMove(bisley,bisley.tableau[1],bisley.tableau[1].peek(),bisley.kings[1],false);
		assertFalse(fm.valid(bisley));

		fm.doMove(bisley);
		assertEquals("2C",bisley.kings[1].peek().toString());
		assertEquals("AC", bisley.tableau[1].peek().toString());
		bisley.getContainer().repaint();
		
		
	}
	
	/**
	 * 
	 */
	public void testAutoMove(){
		//Case: Move tableau to King Foundation (Case is true)
		Card topCard = bisley.tableau[2].peek(); //look at the KH on tableau 2
		//try to move the KH to KH foundation
		FoundationMove fm = new FoundationMove(bisley,bisley.tableau[2], topCard, bisley.kings[3], false); //try to move the card to the foundation
		assertTrue(fm.valid(bisley));
		fm.doMove(bisley);
		assertEquals(1, bisley.getScoreValue());				
	}
	
	/**
	 * 
	 */
	public void testWon(){
		ModelFactory.init(bisley.aces[1], "AC");
		ModelFactory.init(bisley.aces[2], "AD 2D 3D 4D 5D 6D 7D 8D"); 
		bisley.updateScore(+7);
		ModelFactory.init(bisley.aces[3], "AH 2H 3H 4H");
		bisley.updateScore(+3);
		ModelFactory.init(bisley.aces[4], "AS 2S 3S 4S 5S 6S 7S 8S 9S 10S JS QS");
		bisley.updateScore(+11);
		
		ModelFactory.init(bisley.kings[1], "KC QC JC 10C 9C 8C 7C 6C 5C 4C 3C 2C");
		bisley.updateScore(+11);
		ModelFactory.init(bisley.kings[2], "KD QD JD 10D 9D"); 
		bisley.updateScore(+4);
		ModelFactory.init(bisley.kings[3], "KH QH JH 10H 9H 8H 7H 6H 5H"); 
		bisley.updateScore(+8);
		ModelFactory.init(bisley.kings[4], "KS");
		
		assertEquals(44, bisley.getScoreValue());
	}
	
	/**
	 * 
	 */
	public void testTableauToTableauMove(){	
		//Case: Move 10D card from tableau 1 to JD tableau 9
		Card topCard = bisley.tableau[1].peek();
		TableauToTableauMove ttm = new TableauToTableauMove(bisley.tableau[1], topCard, bisley.tableau[9]);
		assertTrue(ttm.valid(bisley));
		bisley.getContainer().repaint();
		
		ttm.doMove(bisley);
		assertEquals(2,bisley.tableau[9].count());
		ttm.undo(bisley);
		
		// Case: Move JD from tableau 9 to 10D tableau 1 
		topCard = bisley.tableau[9].peek();
		ttm = new TableauToTableauMove(bisley.tableau[9], topCard, bisley.tableau[1]);
		ttm.doMove(bisley);
		ttm.undo(bisley);
		
		//Case: Move 10D from tableau 1 to KH tableau 2		
		ttm = new TableauToTableauMove(bisley.tableau[1], topCard, bisley.tableau[2]);
		assertFalse(ttm.valid(bisley));
		bisley.getContainer().repaint();
		
		//Case: Move card to an empty tableau
		bisley.tableau[1].removeAll(); //remove all cards from tableau 1
		
		topCard = bisley.tableau[2].peek(); //obtain top card of tableau 2
		
		ttm = new TableauToTableauMove(bisley.tableau[2], topCard, bisley.tableau[1]);
		assertFalse(ttm.valid(bisley)); //validate that no card can be moved on an empty tableau pile
		bisley.getContainer().repaint();
		
		ttm.doMove(bisley);
		assertEquals(topCard, bisley.tableau[2].peek());
		bisley.getContainer().repaint();	
		
		//Case: Move card to king foundation with King already there (not less than 9 on King of Hearts)
		ModelFactory.init(bisley.tableau[9], "KC 5S 7H JD");
		ModelFactory.init(bisley.tableau[1], "QS 4D 8H 10D"); 
		assertEquals ("JD", bisley.tableau[9].peek().toString());
		assertEquals ("10D", bisley.tableau[1].peek().toString());

		ttm = new TableauToTableauMove(bisley.tableau[1], bisley.tableau[1].peek(), bisley.tableau[9]);
		assertTrue(ttm.valid(bisley));
		bisley.getContainer().repaint();

		//Case: Move card to empty tableau
		ModelFactory.init(bisley.tableau[9], "KC 5S 7H JD");
		bisley.tableau[1].removeAll();

		ttm = new TableauToTableauMove(bisley.tableau[9], bisley.tableau[9].peek(), bisley.tableau[1]);
		assertFalse(ttm.valid(bisley));
		assertFalse(ttm.doMove(bisley));
		bisley.getContainer().repaint();

		//				// Case: Move JD from tableau 9 to 10D tableau 1 
		topCard = bisley.tableau[9].peek();
		ttm = new TableauToTableauMove(bisley.tableau[9], topCard, bisley.tableau[1]);
		assertFalse(ttm.valid(bisley));
		assertFalse(ttm.undo(bisley));	

		//Case: Move 10D from tableau 1 to KH tableau 2		
		ttm = new TableauToTableauMove(bisley.tableau[1], topCard, bisley.tableau[2]);
		assertFalse(ttm.valid(bisley));
		bisley.getContainer().repaint();

		//Case: Move card to an empty tableau
		bisley.tableau[1].removeAll(); //remove all cards from tableau 1

		topCard = bisley.tableau[2].peek(); //obtain top card of tableau 2

		ttm = new TableauToTableauMove(bisley.tableau[2], topCard, bisley.tableau[1]);
		assertFalse(ttm.valid(bisley)); //validate that no card can be moved on an empty tableau pile
		bisley.getContainer().repaint();

		ttm.doMove(bisley);
		assertEquals(topCard, bisley.tableau[2].peek());
		bisley.getContainer().repaint();
		
		bisley.tableau[1].removeAll();
		ttm = new TableauToTableauMove(bisley.tableau[1], topCard, bisley.tableau[2]);
		assertFalse (ttm.valid(bisley));
		assertFalse (ttm.doMove(bisley));
		assertFalse(ttm.undo(bisley));

	} 
	
	/**
	 * 
	 */
	public void testMoveColumnMove(){
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
		assertFalse(mcm.doMove(bisley));
		assertTrue(mcm.undo(bisley));
		assertEquals (3, bisley.tableau[1].count());
		
		mcm = new MoveColumnMove (bisley.tableau[7], bisley.tableau[1], (Column) cv.getModelElement(), 3); 
		assertFalse(mcm.doMove(bisley));
		assertTrue(mcm.undo(bisley));

		assertEquals (0, bisley.tableau[1].count());

		bisley.getContainer().repaint();	
		bisley.tableau[4].removeAll();
		mcm = new MoveColumnMove (bisley.tableau[4], bisley.tableau[7], (Column) cv.getModelElement(), 1); 
		assertFalse(mcm.doMove(bisley));
		
	}
	
	/**
	 * 
	 */
	public void testAceKingFoundationController(){		
		ModelFactory.init(bisley.tableau[1], "4D 3C 4S 2H");
		ModelFactory.init(bisley.aces[3], "AH");

		int overlap =bisley.getCardImages().getOverlap();
		
		// press on tableau 1
		MouseEvent me = createPressed (bisley, bisley.tableauView[1], 3*overlap, 3*overlap);
		bisley.tableauView[1].getMouseManager().handleMouseEvent(me);
		
		// Release mouse on ace foundation
		MouseEvent rel = createReleased (bisley, bisley.acesViews[3], 0, 0);
		bisley.acesViews[3].getMouseManager().handleMouseEvent(rel);
		
		assertEquals (3, bisley.tableau[1].count()); //verify that the 2H has been moved to the AH foundation.

	}   
	/**
	 * 
	 */
	public void testTableauToTableauController(){
		int overlap =bisley.getCardImages().getOverlap();
		
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
		ModelFactory.init(bisley.tableau[1], "4D 3C 4S 2H");
		ModelFactory.init(bisley.tableau[2], "10S 7S 5D 5H");
		
		assertEquals ("2H", bisley.tableau[1].peek().toString());
		assertEquals ("5H", bisley.tableau[2].peek().toString());
		
		//Case: Move card to tableau (false case, not a match)
		// press on tableau 2
		MouseEvent press = createPressed (bisley, bisley.tableauView[2], 3*overlap, 3*overlap);
		bisley.tableauView[2].getMouseManager().handleMouseEvent(press);
		
		assertEquals ("2H", bisley.tableau[1].peek().toString());
		assertEquals ("5D", bisley.tableau[2].peek().toString());
		
		// release onto tableau 1
		MouseEvent release = createReleased (bisley, bisley.tableauView[1], 3*overlap, 3*overlap);
		bisley.tableauView[1].getMouseManager().handleMouseEvent(release);

		// move is made.
		assertEquals ("2H", bisley.tableau[1].peek().toString());
		assertEquals ("5H", bisley.tableau[2].peek().toString());
	
	}

	
}
