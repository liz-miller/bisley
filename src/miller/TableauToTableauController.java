package miller;

import java.awt.event.MouseEvent;

import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.BuildablePileView;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;
import miller.Bisley;

/**
 * TableauToTableauController: Controls TableauToTableauMoves.
 * @param Bisley
 * @param BuildablePileView
 * @author Liz Miller
 */
public class TableauToTableauController extends java.awt.event.MouseAdapter {

	/** The game that we are partly controlling. */
	protected Bisley theGame;

	/** The src BuildablePileView that initiated the event. */
	protected BuildablePileView src;
	/**
	 * BuildablePileController constructor comment.
	 */
	public TableauToTableauController(Bisley theGame, BuildablePileView bpv) {
		super();
		this.theGame = theGame;
		this.src = bpv;
	}

	/**
	 * Coordinate reaction to the beginning of a Drag Event.
	 *
	 * Note: There is no way to differentiate between a press that
	 *       will become part of a double click vs. a click that will
	 *       be held and dragged. Only mouseReleased will be able to 
	 *       help us out with that one.
	 *
	 * Creation date: (10/4/01 6:05:55 PM)
	 * @param pv ks.common.view.PileView
	 * @param me java.awt.event.MouseEvent
	 */
	public void mousePressed(MouseEvent me) {

		// The container manages several critical pieces of information; namely, it
		// is responsible for the draggingObject; in our case, this would be a CardView
		// Widget managing the card we are trying to drag between two piles.
		Container c = theGame.getContainer();

		/** Return if there is no card to be chosen. */
		BuildablePile theBP = (BuildablePile) src.getModelElement();
		if (theBP.count() == 0) {
			return;
		}

		// Get a column of cards to move from the BuildablePileView
		// Note that this method will alter the model for BuildablePileView if the condition is met.
		ColumnView colView = src.getColumnView (me);

//		// an invalid selection (either all facedown, or not in faceup region)
//		if (colView == null) {
//			return;
//		}

		// Check conditions
		Column col = (Column) colView.getModelElement();
//		if (col == null) {
//			System.err.println ("TableauToTableauController::mousePressed(): Unexpectedly encountered a ColumnView with no Column.");
//			return; // sanity check, but should never happen.
//		}

		// verify that Column has desired Bisley Properties to move
		if ((!col.descending()) || (!col.alternatingColors())) {
			theBP.push (col);
			java.awt.Toolkit.getDefaultToolkit().beep();
			return; // announce our displeasure
		}

		// If we get here, then the user has indeed clicked on the top card in the PileView and
		// we are able to now move it on the screen at will. For smooth action, the bounds for the
		// cardView widget reflect the original card location on the screen.
		//Widget w = c.getActiveDraggingObject();
//		if (w != Container.getNothingBeingDragged()) {
//			System.err.println ("BuildablePileController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
//			return;
//		}

		// Tell container which object is being dragged, and where in that widget the user clicked.
		c.setActiveDraggingObject (colView, me);
//
//		// Tell container which BuildablePileView is the source for this drag event.
		c.setDragSource (src);
//
//		// we simply redraw our source pile to avoid flicker,
//		// rather than refreshing all widgets...
		src.redraw();
	}
	/**
	 * Coordinate reaction to the completion of a Drag Event.
	 * <p>
	 * A bit of a challenge to construct the appropriate move, because cards
	 * can be dragged both from the WastePile (as a CardView widget) and the 
	 * BuildablePileView (as a ColumnView widget).
	 * <p>
	 * @param me java.awt.event.MouseEvent
	 */
	public void mouseReleased(MouseEvent me) {
		Container c = theGame.getContainer();

		/** Return if there is no card being dragged chosen. */
		Widget w = c.getActiveDraggingObject();

		/** Recover the from BuildablePile */
		Widget fromWidget = c.getDragSource();

		// Determine the To Pile
		BuildablePile toPile = (BuildablePile) src.getModelElement();

		if (fromWidget instanceof BuildablePileView) {
			// Must be a ColumnView widget being dragged.
			ColumnView columnView = (ColumnView) w;
			Column col = (Column) columnView.getModelElement();
			if (col == null) {
				System.err.println ("BuildablePileController::mouseReleased(): somehow ColumnView model element is null.");
				return;
			}

			if (fromWidget == src) {
				toPile.push (col);   // simply put right back where it came from. No move
			} else {
				BuildablePile fromPile = (BuildablePile) fromWidget.getModelElement();
				Move m = new MoveColumnMove (fromPile, toPile, col, col.count());

				if (m.doMove (theGame)) {
					// Successful move! add move to our set of moves
					theGame.pushMove (m);
					if(!fromPile.empty() && fromPile.peek().getRank()==Card.KING){
						theGame.kings[fromPile.peek().getSuit()].add(fromPile.get());
					}
					theGame.refreshWidgets();
				} else {
					// Invalid move. Restore to original column. NO MOVE MADE
					fromPile.push (col);
				}
			}		
		} else {
			CardView cardView = (CardView) w;
			Card theCard = (Card) cardView.getModelElement();
			if (theCard == null) {
				System.err.println ("BuildablePileController::mouseReleased(): somehow CardView model element is null.");
				return;
			}
			BuildablePile tableau = (BuildablePile) fromWidget.getModelElement();
			BuildablePile tableauEnd = (BuildablePile) src.getModelElement();
			Move m = new TableauToTableauMove(tableau, theCard, tableauEnd);
			if (m.doMove (theGame)) {
				// Successful move! add move to our set of moves
				theGame.pushMove (m); 
				if(!tableau.empty() && tableau.peek().getRank()==Card.KING){
					theGame.kings[tableau.peek().getSuit()].add(tableau.get());
				}
				theGame.refreshWidgets();
			} else { 
				// Invalid move. Restore to original waste pile. NO MOVE MADE
				tableau.add (theCard);
			}
		}
		// release the dragging object, (container will reset dragSource)
		c.releaseDraggingObject();
		
		c.repaint();
	}
}
