package miller;
import heineman.klondike.MoveCardToFoundationMove;
import heineman.klondike.MoveWasteToFoundationMove;

import java.awt.event.MouseEvent;

/*
 * AceKingFoundationController
 * 		MouseRelease
 * 
 * May receive 1 or more cards in ascending/descending order of the same suit to a King Pile or Ace Pile
 * Makes instances of:
 * 		FoundationMove
 * 
 * Cards cannot be moved from the King Pile or Ace Pile
 */



import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.BuildablePileView;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;

public class AceKingFoundationController extends java.awt.event.MouseAdapter {
	/** The RedBlack Game. */
	protected Bisley theGame;

	/** The specific Foundation pileView being controlled. */
	protected PileView src;
	
	/** Am I red ? */
	boolean isAce;
	
	/**
	 * RedBlackFoundationController constructor comment.
	 */
	public AceKingFoundationController (Bisley theGame, PileView foundation, boolean isAce) {
		
		this.theGame = theGame;
		this.src = foundation;
		this.isAce = isAce;
	}
	/**
	 * Coordinate reaction to the completion of a Drag Event.
	 * <p>
	 * A bit of a challenge to construct the appropriate move, because cards
	 * can be dragged both from the WastePile (as a CardView object) and the 
	 * BuildablePileView (as a ColumnView).
	 * @param me java.awt.event.MouseEvent
	 */
	public void mouseReleased(MouseEvent me) {
		Container c = theGame.getContainer();

		/** Return if there is no card being dragged chosen. */
		Widget draggingWidget = c.getActiveDraggingObject();
		if (draggingWidget == Container.getNothingBeingDragged()) {
			System.err.println ("FoundationController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();		
			return;
		}

		/** Recover the from BuildablePile OR waste Pile */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println ("FoundationController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		// Determine the To Pile
		Pile foundation = (Pile) src.getModelElement();

		if (fromWidget instanceof BuildablePileView) {
			// coming from a buildable pile [user may be trying to move multiple cards]
			BuildablePile fromPile = (BuildablePile) fromWidget.getModelElement();

			/** Must be the ColumnView widget being dragged. */
			ColumnView columnView = (ColumnView) draggingWidget;
			Column col = (Column) columnView.getModelElement();
			if (col == null) {
				System.err.println ("FoundationController::mouseReleased(): somehow ColumnView model element is null.");
				c.releaseDraggingObject();			
				return;
			}

			// must use peek() so we don't modify col prematurely. Here is a HACK! Presumably
			// we only want the Move object to know things about the move, but we have to put
			// in a check to verify that Column is of size one. NO good solution that I can
			// see right now.
			if (col.count() != 1) {
				fromWidget.returnWidget (draggingWidget);  // return home
			} else {
				Move m = new FoundationMove (fromPile, col.peek(), foundation, isAce);

				if (m.doMove (theGame)) {
					// Success
					theGame.pushMove (m);
					theGame.refreshWidgets();
				} else {
					fromWidget.returnWidget (draggingWidget);
				}
			}
		} //else {
//			// Coming from the waste [number of cards being dragged must be one]
//			Pile  = (Pile) fromWidget.getModelElement();
//
//			/** Must be the CardView widget being dragged. */
//			CardView cardView = (CardView) draggingWidget;
//			Card theCard = (Card) cardView.getModelElement();
//			if (theCard == null) {
//				System.err.println ("FoundationController::mouseReleased(): somehow CardView model element is null.");
//				c.releaseDraggingObject();
//				return;
//			}
//
//			// must use peek() so we don't modify col prematurely
//			Move m = new MoveWasteToFoundationMove (wastePile, theCard, foundation);
//			if (m.doMove (theGame)) {
//				// Success
//				theGame.pushMove (m);
//			} else {
//				fromWidget.returnWidget (draggingWidget);
//			}
//		}

		// Ahhhh. Instead of dealing with multiple 'instanceof' difficulty, why don't we allow
		// for multiple controllers to be set on the same widget? Each will be invoked, one
		// at a time, until someone returns TRUE (stating that they are processing the event).
		// Then we have controllers for each MOVE TYPE, not just for each entity. In this way,
		// I wouldn't have to convert the CardView from wastePile into a ColumnView. I would
		// still have to do some sort of instanceOf check, however, to validate: But if the
		// instanceof failed, the controller could safely return and say NOT ME! See! There
		// always is a way to avoid layered if statements in OO.

		// release the dragging object, (this will reset dragSource)
		c.releaseDraggingObject();
		
		// finally repaint
		c.repaint();
	}
}

