package miller;

import miller.FoundationMove;
import java.awt.event.MouseEvent;
import ks.common.model.Column;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.ColumnView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.PileView;
import ks.common.view.Widget;


public class TableauToFoundationController extends java.awt.event.MouseAdapter {
	
	Bisley theGame;
	PileView src;
	boolean isAce;

	public TableauToFoundationController(Bisley theGame, PileView src, boolean isAce) {
		
		this.theGame = theGame;
		this.src = src;
		this.isAce = isAce;

	}

	public void mouseReleased(MouseEvent me) {
		Container c = theGame.getContainer();

		/** Return if there is no card being dragged chosen. */
		Widget draggingWidget = c.getActiveDraggingObject();
		if (draggingWidget == Container.getNothingBeingDragged()) {
			System.err.println ("FoundationController::mouseReleased() unexpectedly found nothing being dragged.");
			c.releaseDraggingObject();		
			return;
		}

		/** Recover the from Column OR waste Pile */
		Widget fromWidget = c.getDragSource();
		if (fromWidget == null) {
			System.err.println ("FoundationController::mouseReleased(): somehow no dragSource in container.");
			c.releaseDraggingObject();
			return;
		}

		// Determine the To Pile
		Pile foundation = (Pile) src.getModelElement();

		if (fromWidget instanceof ColumnView) {
			// coming from a buildable pile [user may be trying to move multiple cards]
			Column fromTableau = (Column) fromWidget.getModelElement();

			/** Must be the ColumnView widget being dragged. */
			ColumnView columnView = (ColumnView) draggingWidget;
			Column col = (Column) columnView.getModelElement();
			if (col == null) {
				System.err.println ("BisleyFoundationController::mouseReleased(): somehow ColumnView model element is null.");
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
				Move m = new FoundationMove (fromTableau, col.peek(), foundation, isAce);

				if (m.doMove (theGame)) {
					// Success
					theGame.pushMove (m);
					theGame.refreshWidgets();
				} else {
					fromWidget.returnWidget (draggingWidget);
				}
			}
		} 
		// release the dragging object, (this will reset dragSource)
		c.releaseDraggingObject();
		
		// finally repaint
		c.repaint();
	}
}