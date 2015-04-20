package miller;

import java.awt.Dimension;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.Column;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.common.model.Pile;
import ks.common.view.ColumnView;
import ks.common.view.CardImages;
import ks.common.view.DeckView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;
import ks.launcher.Main;

public class Bisley extends Solitaire {

	Deck deck;
	Column tableau[] = new Column[14];
	Pile aces[] = new Pile[5];
	Pile kings[] = new Pile[5];
	
	DeckView deckView;
	ColumnView[] tableauView = new ColumnView[14];
	PileView[] acesViews = new PileView[5];
	PileView[] kingsViews = new PileView[5];
	
	IntegerView scoreView;
	IntegerView numLeftView;
	
	@Override
	public String getName() {
		return "miller-bisley";
	}

	@Override
	public boolean hasWon() {
		return getScore().getValue() == 52;
	}
	
	@Override
	 public Dimension getPreferredSize() {
		return new Dimension (1800, 1000);
	}

	@Override
	public void initialize() {
		// initialize model
		initializeModel(getSeed());
		initializeView();
		initializeControllers();

		Card ace1 = new Card(1, 1);
		Card ace2 = new Card(1, 2);
		Card ace3 = new Card(1, 3);
		Card ace4 = new Card(1, 4);

		aces[1].add(ace1);
		aces[2].add(ace2);
		aces[3].add(ace3);
		aces[4].add(ace4);
		
		for (int pileNum=1; pileNum < 10; pileNum++){
			for (int i=0; i<=3; i++){
				Card c = deck.get();

				while(c != null && c.getRank()==1) c = deck.get();
				
				if(c != null){
					c.setFaceUp(true);
					
					//System.out.println("Adding (" + c.getSuit() + ", " + c.getRank() + ") to tableau " + pileNum);
					tableau[pileNum].add(c);
				}
			}
		
		}
		
		for (int pileNum=10; pileNum < 14; pileNum++){
			for (int i=0; i<=2; i++){
				Card c = deck.get();

				while(c != null && c.getRank()==1) c = deck.get();
				
				if(c != null){
					c.setFaceUp(true);
					
					//System.out.println("Adding (" + c.getSuit() + ", " + c.getRank() + ") to tableau " + pileNum);
					tableau[pileNum].add(c);
				}
			}
		
		}
		updateNumberCardsLeft(-52);
	}

	private void initializeControllers() {
		
		// Now for each Column (Tableau).
		for (int i = 1; i <= 13; i++) {
			tableauView[i].setMouseAdapter (new TableauToTableauController (this, tableauView[i]));
			tableauView[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			tableauView[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}

		// Now for each Foundation (Aces and Kings).
		for (int i = 1; i <= 4; i++) {
			acesViews[i].setMouseAdapter (new TableauToFoundationController (this, acesViews[i], true));
			acesViews[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			acesViews[i].setUndoAdapter (new SolitaireUndoAdapter(this));
			
			kingsViews[i].setMouseAdapter (new TableauToFoundationController (this, kingsViews[i], false));
			kingsViews[i].setMouseMotionAdapter (new SolitaireMouseMotionAdapter (this));
			kingsViews[i].setUndoAdapter (new SolitaireUndoAdapter(this));
		}

		// Ensure that any releases (and movement) are handled by the non-interactive widgets
		numLeftView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		numLeftView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		numLeftView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// same for scoreView
		scoreView.setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
		scoreView.setMouseAdapter (new SolitaireReleasedAdapter(this));
		scoreView.setUndoAdapter (new SolitaireUndoAdapter(this));

		// Finally, cover the Container for any events not handled by a widget:
		getContainer().setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
		getContainer().setMouseAdapter (new SolitaireReleasedAdapter(this));
		getContainer().setUndoAdapter (new SolitaireUndoAdapter(this));
		
	}

	private void initializeView() {
		CardImages ci = getCardImages();
		
		/* Build tableau */
		//top row 6 piles, #0-5
		for (int pileNum = 1; pileNum <=6; pileNum++) {
			tableau[pileNum] = new Column();
			tableauView[pileNum] = new ColumnView (tableau[pileNum]);
			tableauView[pileNum].setBounds (20*pileNum + (pileNum-1)*ci.getWidth(), 10, 5+ci.getWidth(), 2*ci.getHeight());
			container.addWidget (tableauView[pileNum]);
		}
		//bottom row 7 piles, #6-12
		for (int pileNum = 7; pileNum <=13; pileNum++) {
			tableau[pileNum] = new Column();
			tableauView[pileNum] = new ColumnView (tableau[pileNum]);
			tableauView[pileNum].setBounds (20*(pileNum+5) + ci.getWidth()*(pileNum-10), 20+3*ci.getWidth(), 5+ci.getWidth(), 2*ci.getHeight());
			container.addWidget (tableauView[pileNum]);
		}
		
		/* Build King Foundations */
		kings[1] = new Pile();
		kings[2] = new Pile();
		kings[3] = new Pile();
		kings[4] = new Pile();
		
		kingsViews[1] = new PileView(kings[1]);
		kingsViews[1].setBounds(400+5*ci.getWidth(),10,5+ci.getWidth(), 5+ci.getHeight());
		container.addWidget(kingsViews[1]);
		
		kingsViews[2] = new PileView(kings[2]);
		kingsViews[2].setBounds(400+5*ci.getWidth(),30+ci.getHeight(), 5+ci.getWidth(), 5+ci.getHeight());
		container.addWidget(kingsViews[2]);
		
		kingsViews[3] = new PileView(kings[3]);
		kingsViews[3].setBounds(400+5*ci.getWidth(),150+ci.getHeight(),5+ci.getWidth(), 5+ci.getHeight());
		container.addWidget(kingsViews[3]);
		
		kingsViews[4] = new PileView(kings[4]);
		kingsViews[4].setBounds(400+5*ci.getWidth(),270+ci.getHeight(),5+ci.getWidth(), 5+ci.getHeight());
		container.addWidget(kingsViews[4]);
		

		/* Build Ace Foundations */
		aces[1] = new Pile();
		aces[2] = new Pile();
		aces[3] = new Pile();
		aces[4] = new Pile();
		
		acesViews[1] = new PileView(aces[1]);
		acesViews[1].setBounds(500+5*ci.getWidth(),10,5+ci.getWidth(), 5+ci.getHeight());
		container.addWidget(acesViews[1]);
		
		acesViews[2] = new PileView(aces[2]);
		acesViews[2].setBounds(500+5*ci.getWidth(),30+ci.getHeight(), 5+ci.getWidth(), 5+ci.getHeight());
		container.addWidget(acesViews[2]);
		
		acesViews[3] = new PileView(aces[3]);
		acesViews[3].setBounds(500+5*ci.getWidth(),150+ci.getHeight(),5+ci.getWidth(), 5+ci.getHeight());
		container.addWidget(acesViews[3]);
		
		acesViews[4] = new PileView(aces[4]);
		acesViews[4].setBounds(500+5*ci.getWidth(),270+ci.getHeight(),5+ci.getWidth(), 5+ci.getHeight());
		container.addWidget(acesViews[4]);
		
		/* Build Score and Cards Left Views */
		scoreView = new IntegerView (getScore());
		scoreView.setFontSize (16);
		scoreView.setBounds (625, 0, 60, 60);
		container.addWidget (scoreView);

		numLeftView = new IntegerView (getNumLeft());
		numLeftView.setFontSize (16);
		numLeftView.setBounds (625, 95, 60, 60);
		container.addWidget (numLeftView);		
	}
	
	private void initializeModel(int seed) {
		deck = new Deck("deck");
		deck.create(seed);
		deck.shuffle(seed);

		//System.out.println("Deck cards: " + deck.count());
		model.addElement (deck);   // add to our model (as defined within our superclass).

		for (int i = 1; i <= 4; i++) {
			aces[i] = new Pile("aces" + i);
			model.addElement(aces[i]);
			
			kings[i] = new Pile("kings"+ i);
			model.addElement(kings[i]);
		}
					
		for (int i = 1; i < 14; i++){
			tableau[i] = new Column("tableau" + i);
			model.addElement(tableau[i]);
		}	
		
		updateNumberCardsLeft(52);
		updateScore(0);
	}
	
	/** Code to launch solitaire variation. */
	public static void main (String []args) {
		// Seed is to ensure we get the same initial cards every time.
		// Here the seed is to "order by suit."
		Main.generateWindow(new Bisley(), Deck.OrderBySuit);
	}

}
