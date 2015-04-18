package miller;

import java.awt.Dimension;

import heineman.klondike.KlondikeDeckController;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.common.model.Pile;
import ks.common.view.BuildablePileView;
import ks.common.view.CardImages;
import ks.common.view.DeckView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;
import ks.launcher.Main;

public class Bisley extends Solitaire {

	Deck deck;
	BuildablePile tableau[] = new BuildablePile[14];
	Pile aces[] = new Pile[5];
	Pile kings[] = new Pile[5];
	
	DeckView deckView;
	BuildablePileView[] tableauView = new BuildablePileView[14];
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

		
		// prepare game by dealing 3 cards faceup on each tableau pile
			for (int pileNum=0; pileNum <14; pileNum++) {
				for (int num = 1; num <= 3; num++) {
					Card c = deck.get();
					
					// check if dealt card is an ace. if so, move to ace foundation.
					for(int suit = 1; suit<5; suit++){
						if(c.getRank()==1 && c.getSuit()==suit){
							c.setFaceUp(true);
							aces[suit-1].add(c);
						}
					}
					
				    c.setFaceUp (true);
					tableau[pileNum].add (c);	
				}
			}
		updateNumberCardsLeft (-52);
	}

	private void initializeControllers() {//
//		tableauView[14].setMouseAdapter(new TableauPileController (this, tableauView[14]));
//		tableauView[14].setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
//		tableauView[14].setUndoAdapter (new SolitaireUndoAdapter(this));
//		
//		acesViews[5].setMouseAdapter(new FoundationController (this, acesViews[5], true));
//		acesViews[5].setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
//		acesViews[5].setUndoAdapter (new SolitaireUndoAdapter(this));
//		
//		kingsViews[5].setMouseAdapter(new FoundationController (this, kingsViews[5], false));
//		kingsViews[5].setMouseMotionAdapter (new SolitaireMouseMotionAdapter(this));
//		kingsViews[5].setUndoAdapter (new SolitaireUndoAdapter(this));
	}

	private void initializeView() {
		CardImages ci = getCardImages();
		
		/* Build tableau */
		//top row 6 piles, #0-5
		for (int pileNum = 1; pileNum <=6; pileNum++) { 
			tableauView[pileNum] = new BuildablePileView (tableau[pileNum]);
			tableauView[pileNum].setBounds (20*pileNum + (pileNum-1)*ci.getWidth(), 10, 5+ci.getWidth(), 2*ci.getHeight());
			container.addWidget (tableauView[pileNum]);
		}
		//bottom row 7 piles, #6-12
		for (int pileNum = 7; pileNum <=13; pileNum++) {
			tableauView[pileNum] = new BuildablePileView (tableau[pileNum]);
			tableauView[pileNum].setBounds (20*(pileNum+5) + ci.getWidth()*(pileNum-10), 20+3*ci.getWidth(), 5+ci.getWidth(), 2*ci.getHeight());
			container.addWidget (tableauView[pileNum]);
		}
		
		/* Build King Foundations */
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
		scoreView.setBounds (50, 500, 160, 60);
		container.addWidget (scoreView);

		numLeftView = new IntegerView (getNumLeft());
		numLeftView.setFontSize (16);
		numLeftView.setBounds (300, 500, 160, 60);
		container.addWidget (numLeftView);		
	}
	
	private void initializeModel(int seed) {
		deck = new Deck("deck");
		deck.create(seed);
		model.addElement (deck);   // add to our model (as defined within our superclass).

		for (int i = 0; i <= 4; i++) {
			aces[i] = new Pile("aces" + i);
			model.addElement(aces[i]);
			
			kings[i] = new Pile("kings"+ i);
			model.addElement(kings[i]);
		}
					
		for (int i = 0; i < 14; i++){
			tableau[i] = new BuildablePile("tableau" + i);
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
