
/**
 * Program Name: CovidSimulator.java
 * Purpose: A very simple simulation of a pandemic. It should let user enter
 * 			immunity status of individuals and population size.
 * 			Simulation starts with one(1) patient zero and there is a chance of infection
 * 		    when persons collide.
 *          Chances are determined by:
 *          1) If an infected person collides with an uninfected person who has no immunity, then there is an 80% chance that the disease will be passed on to the uninfected person.
 *			2) If an infected person collides with an uninfected person who has had ONE shot of the vaccine, then there is a 40% chance that the uninfected person will be passed on to the uninfected person.
 *			3) If an infected person collides with an uninfected person who has had BOTH shots of the vaccine, then there is only a 10% chance that the uninfected person will be passed on to the uninfected person.
 *			4) If an infected person collides with a person who has had the disease and recovered ( a green dot on the screen shot) then there is only a 10% chance that the disease will be passed on to the uninfected person.
 *			5) An infected person has a 10% chance of dying from the disease.
 *
 * Coder: Harry Jiao 0945414 section 02, Donghyun Lee 0875734 Section 03, Bohong Liu 0954478 section 04, Jia Zeng 0886052 section 04
 * Date: August 7, 2021
 */
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CovidSimulator extends JPanel {
	// CLASS WIDE SCOPE AREA
	private final int WIDTH = 800, HEIGHT = 700;// size of JPanel
	private final int LAG_TIME = 125; // time in milliseconds between re-paints of screen
	private static Timer time;// Timer class object that will fire events every LAG_TIME interval
	private final int IMG_DIM = 10; // size of ball to be drawn
	// private final int TOTAL_CYCLES =450;
	private final int TOTAL_CYCLES = 450;
	// REVISION July 14 : create an array of Ball objects here in class scope
	private static int DEFAULT_POPULATION = 1000;
	private static int COUNTER = 0;
	private static Person[] popArray;

	private static int totalInfected = 1;

	private static int notVaccine = 0;

	private static int partVaccine = 0;

	private static int fullVaccine = 0;
	private static int recovNum = 0;
	private static int deathNum = 0;
	private static int noVacDeathNum = 0;
	private static int oneVacDeathNum = 0;
	private static int twoVacDeathNum = 0;
	private static int natDeathNum = 0;

	JLabel titleField;
	JLabel nInfectedField;
	JLabel nNonVField;
	JLabel nPartiallyVField;
	JLabel nFullyVField;
	JLabel nInfectedRecoveredField;
	JLabel nInfectedDiedField;

	// constructor
	public CovidSimulator() {
		// create Timer and register a listener for it.
		CovidSimulator.time = new Timer(LAG_TIME, new ContactListener());
		popArray = new Person[DEFAULT_POPULATION];
		popArray[0] = new Person(true, true, true, 1, Color.RED, 0);
		// now set color of remaining balls to BLUE

		for (int i = 1; i < 250; i++) {
			popArray[i] = new Person(true, false, false, 1, Color.BLUE, 0);
		} // end for
		for (int i = 250; i < 500; i++) {
			popArray[i] = new Person(true, false, false, 2, Color.CYAN, 0);
		} // end for
		for (int i = 500; i < 750; i++) {
			popArray[i] = new Person(true, false, false, 3, Color.YELLOW, 0);
		} // end for
		for (int i = 750; i < 1000; i++) {
			popArray[i] = new Person(true, false, true, 4, Color.GREEN, 0);
		} // end for

		// set preferred size of panel using an ANONYMOUS Dimension object
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(new Color(165, 165, 165));

	}// end constructor

	// OVER-RIDE the JPanel's paintComponent() method
	@Override
	public void paintComponent(Graphics g)// The Graphics object 'g' is your paint brush
	{

		super.paintComponent(g);

		// set brush color
		g.setColor(Color.PINK);

//		// draw a circle shape
		for (Person element : popArray) {
			// get the color
			g.setColor(element.getColor());
			g.fillOval(element.getxCoordinate(), element.getyCoordinate(), IMG_DIM, IMG_DIM);
		}

		repaint();

	}// end paintComponent over-ride

	// Draws Rectangles for ResultFrame Bar graphs
	private class MyPanel extends JPanel {

		public void paint(Graphics g) {

			//Graphics2D g2 = (Graphics2D) g;

			// Construct a rectangle then <strong class="highlight">draw</strong> it
			if(deathNum != 0) {
			g.setColor(Color.BLUE);
			g.drawRect(10, 325,  (noVacDeathNum * 10)/deathNum * 100, 35);
			g.setColor(Color.CYAN);
			g.drawRect(10, 250, (oneVacDeathNum * 10)/deathNum * 100,35);
			g.setColor(Color.YELLOW);
			g.drawRect(10, 175, (twoVacDeathNum * 10)/deathNum * 100,35);
			g.setColor(Color.GREEN);
			g.drawRect(10, 100, (natDeathNum * 10)/deathNum * 100, 35);
			}
		}
	}

	private class ContactListener extends JPanel implements ActionListener, ChangeListener {

		JMenuBar menu;
		JMenu about, run;
		JMenuItem version, start, stop;

		JSlider noShotSlider;
		JSlider oneShotSlider;
		JSlider twoShotSlider;
		JSlider natSlider;

		JSpinner popField;
		JButton startBtn;
		JButton pauseBtn;

		JLabel noShotLabel;
		JLabel oneShotLabel;
		JLabel twoShotLabel;
		JLabel natLabel;
		JLabel popLabel;
		JLabel errorLabel;

		JPanel configureP;
		JPanel reportP;

		JFrame aboutFrame;
		JPanel aboutP;
		JLabel name1;
		JLabel name2;
		JLabel name3;
		JLabel name4;

		JFrame resultFrame;
		JPanel resultPanel;
		JLabel resltInfect;
		JLabel resltNoVac;
		JLabel resltOneVac;
		JLabel resltFullVac;
		JLabel resltRecover;
		JLabel resltNoVacDeath;
		JLabel resltOneVacDeath;
		JLabel resltTwoVacDeath;
		JLabel resltNatDeath;

		MyPanel reportInfected;
	

		public ContactListener() {
			// Menu
			menu = new JMenuBar();
			about = new JMenu("About");
			run = new JMenu("Run");

			version = new JMenuItem("Version");
			start = new JMenuItem("Start Simulation");
			stop = new JMenuItem("Stop Simulation");

			start.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					totalInfected = 1;

					notVaccine = 0;

					partVaccine = 0;

					fullVaccine = 0;

					recovNum = natSlider.getValue();

					deathNum = 0;

					pauseBtn.setText("Pause Simulation");
					DEFAULT_POPULATION = (int) popField.getValue();
					int noVacPop = (int) ((noShotSlider.getValue() / 100.0) * popArray.length);
					int oneVacPop = (int) ((oneShotSlider.getValue() / 100.0) * popArray.length);
					int twoVacPop = (int) ((twoShotSlider.getValue() / 100.0) * popArray.length);
					int natPop = (int) ((natSlider.getValue() / 100.0) * popArray.length);
					COUNTER = 0;
					popArray = new Person[DEFAULT_POPULATION];
					popArray[0] = new Person(true, true, false, 1, Color.RED, 0);

					for (int i = 1; i < noVacPop; i++) {
						popArray[i] = new Person(true, false, false, 1, Color.BLUE, 0);
					} // end for
					for (int i = noVacPop; i < noVacPop + oneVacPop; i++) {
						popArray[i] = new Person(true, false, false, 2, Color.CYAN, 0);
					} // end for
					for (int i = noVacPop + oneVacPop; i < noVacPop + oneVacPop + twoVacPop; i++) {
						popArray[i] = new Person(true, false, false, 3, Color.YELLOW, 0);
					} // end for
					for (int i = noVacPop + oneVacPop + twoVacPop; i < noVacPop + oneVacPop + twoVacPop + natPop; i++) {
						popArray[i] = new Person(true, false, true, 4, Color.GREEN, 0);
					} // end for

					CovidSimulator.time.start();
				}

			});
			stop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					time.stop();
				}
			});
			version.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					aboutFrame.setVisible(true);
				}

			});

			about.add(version);
			run.add(start);
			run.add(stop);
			menu.add(run);
			menu.add(about);

			menu.setBackground(Color.BLACK);
			about.setForeground(Color.GREEN);
			run.setForeground(Color.GREEN);

			// PANEL: User Inputs (Slider, TextFields)
			configureP = new JPanel(new GridLayout(13, 1, 10, 10));

			// PANEL: User Report
			reportP = new JPanel(new GridLayout(13, 1, 10, 10));

			// LABELS:
			noShotLabel = new JLabel();
			oneShotLabel = new JLabel();
			twoShotLabel = new JLabel();
			natLabel = new JLabel();
			popLabel = new JLabel();
			errorLabel = new JLabel();

			noShotSlider = new JSlider(0, 100, 25);
			oneShotSlider = new JSlider(0, 100, 25);
			twoShotSlider = new JSlider(0, 100, 25);
			natSlider = new JSlider(0, 100, 25);

			popField = new JSpinner();
			popField.setValue(DEFAULT_POPULATION);
			popField.setBackground(Color.BLACK);
			popField.setForeground(Color.GREEN);
			popField.setFont(new Font("Monofonto", Font.PLAIN, 18));
			startBtn = new JButton("Start Simulation");
			startBtn.setBackground(Color.BLACK);
			startBtn.setForeground(Color.GREEN);
			startBtn.setOpaque(true);
			startBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					totalInfected = 1;

					notVaccine = 0;

					partVaccine = 0;

					fullVaccine = 0;

					recovNum = natSlider.getValue();

					deathNum = 0;

					DEFAULT_POPULATION = (int) popField.getValue();
					int noVacPop = (int) ((noShotSlider.getValue() / 100.0) * popArray.length);
					int oneVacPop = (int) ((oneShotSlider.getValue() / 100.0) * popArray.length);
					int twoVacPop = (int) ((twoShotSlider.getValue() / 100.0) * popArray.length);
					int natPop = (int) ((natSlider.getValue() / 100.0) * popArray.length);
					COUNTER = 0;
					popArray = new Person[DEFAULT_POPULATION];
					popArray[0] = new Person(true, true, false, 1, Color.RED, 0);

					for (int i = 1; i < noVacPop; i++) {
						popArray[i] = new Person(true, false, false, 1, Color.BLUE, 0);
					} // end for
					for (int i = noVacPop; i < noVacPop + oneVacPop; i++) {
						popArray[i] = new Person(true, false, false, 2, Color.CYAN, 0);
					} // end for
					for (int i = noVacPop + oneVacPop; i < noVacPop + oneVacPop + twoVacPop; i++) {
						popArray[i] = new Person(true, false, false, 3, Color.YELLOW, 0);
					} // end for
					for (int i = noVacPop + oneVacPop + twoVacPop; i < noVacPop + oneVacPop + twoVacPop + natPop; i++) {
						popArray[i] = new Person(true, false, true, 4, Color.GREEN, 0);
					} // end for

					CovidSimulator.time.start();
				}

			});

			pauseBtn = new JButton();
			// Button Label Logic
			if (time != null && time.isRunning())
				pauseBtn.setText("Pause Simulation");
			else
				pauseBtn.setText("Resume Simulation");

			pauseBtn.setBackground(Color.BLACK);
			pauseBtn.setForeground(Color.GREEN);

			// Slider Settings
			noShotSlider.setPreferredSize(new Dimension(300, 50));

			noShotSlider.setPaintTicks(true);
			noShotSlider.setMinorTickSpacing(10);

			noShotSlider.setPaintTrack(true);
			noShotSlider.setMajorTickSpacing(25);

			noShotSlider.setPaintLabels(true);
			noShotSlider.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
			noShotSlider.setBackground(Color.BLACK);
			noShotSlider.setForeground(Color.GREEN);

			oneShotSlider.setPreferredSize(new Dimension(300, 50));

			oneShotSlider.setPaintTicks(true);
			oneShotSlider.setMinorTickSpacing(10);

			oneShotSlider.setPaintTrack(true);
			oneShotSlider.setMajorTickSpacing(25);
			oneShotSlider.setBackground(Color.BLACK);
			oneShotSlider.setForeground(Color.GREEN);

			oneShotSlider.setPaintLabels(true);
			oneShotSlider.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));

			twoShotSlider.setPreferredSize(new Dimension(300, 50));

			twoShotSlider.setPaintTicks(true);
			twoShotSlider.setMinorTickSpacing(10);
			twoShotSlider.setBackground(Color.BLACK);
			twoShotSlider.setForeground(Color.GREEN);

			twoShotSlider.setPaintTrack(true);
			twoShotSlider.setMajorTickSpacing(25);

			twoShotSlider.setPaintLabels(true);
			twoShotSlider.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));

			natSlider.setPreferredSize(new Dimension(300, 50));

			natSlider.setPaintTicks(true);
			natSlider.setMinorTickSpacing(10);
			natSlider.setBackground(Color.BLACK);
			natSlider.setForeground(Color.GREEN);

			natSlider.setPaintTrack(true);
			natSlider.setMajorTickSpacing(25);

			natSlider.setPaintLabels(true);
			natSlider.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));

			noShotSlider.setOrientation(SwingConstants.HORIZONTAL);
			oneShotSlider.setOrientation(SwingConstants.HORIZONTAL);
			twoShotSlider.setOrientation(SwingConstants.HORIZONTAL);
			natSlider.setOrientation(SwingConstants.HORIZONTAL);

			noShotLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			noShotLabel.setText("Not Vaccinated: " + noShotSlider.getValue());
			noShotLabel.setForeground(Color.GREEN);

			oneShotLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			oneShotLabel.setText("Partially Vaccinated: " + oneShotSlider.getValue());
			oneShotLabel.setForeground(Color.GREEN);

			twoShotLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			twoShotLabel.setText("Fully Vaccinated: " + twoShotSlider.getValue());
			twoShotLabel.setForeground(Color.GREEN);

			natLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			natLabel.setText("Natural Immunity: " + natSlider.getValue());
			natLabel.setForeground(Color.GREEN);

			// Text Field Settings
			popLabel.setForeground(Color.GREEN);
			popLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			popLabel.setText("Enter Population:");

			errorLabel.setForeground(Color.RED);
			errorLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 10));
			errorLabel.setText("Please Enter an Integer.");
			errorLabel.setVisible(false);

			noShotLabel.setHorizontalAlignment(SwingConstants.CENTER);
			oneShotLabel.setHorizontalAlignment(SwingConstants.CENTER);
			twoShotLabel.setHorizontalAlignment(SwingConstants.CENTER);
			natLabel.setHorizontalAlignment(SwingConstants.CENTER);
			popLabel.setHorizontalAlignment(SwingConstants.CENTER);

			noShotSlider.addChangeListener(this);
			oneShotSlider.addChangeListener(this);
			twoShotSlider.addChangeListener(this);
			natSlider.addChangeListener(this);

			// VERSION - GROUP MEMEBER NAMES
			name1 = new JLabel();
			name2 = new JLabel();
			name3 = new JLabel();
			name4 = new JLabel();

			name1.setForeground(Color.GREEN);
			name1.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			name1.setText("Jiahui Jiao");

			name2.setForeground(Color.GREEN);
			name2.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			name2.setText("Donghyun Lee");

			name3.setForeground(Color.GREEN);
			name3.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			name3.setText("Jia Zeng");

			name4.setForeground(Color.GREEN);
			name4.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			name4.setText("Bohong Liu");

			name1.setHorizontalAlignment(SwingConstants.CENTER);
			name2.setHorizontalAlignment(SwingConstants.CENTER);
			name3.setHorizontalAlignment(SwingConstants.CENTER);
			name4.setHorizontalAlignment(SwingConstants.CENTER);

			Border greenBorder;
			greenBorder = BorderFactory.createLineBorder(Color.GREEN);

			aboutP = new JPanel(new GridLayout(5, 1, 10, 10));
			aboutP.setBackground(Color.BLACK);
			aboutP.setBorder(greenBorder);
			aboutP.add(name1);
			aboutP.add(name2);
			aboutP.add(name3);
			aboutP.add(name4);
			aboutFrame = new JFrame();
			aboutFrame.getContentPane().setBackground(Color.BLACK);

			aboutFrame.add(aboutP);
			aboutFrame.setLayout(new FlowLayout());
			aboutFrame.setSize(150, 200);
			aboutFrame.setLocationRelativeTo(null);

			aboutFrame.setVisible(false);

			// User Input panel Settings
			configureP.setBackground(Color.BLACK);
			configureP.setForeground(Color.GREEN);
			configureP.setOpaque(true);

			// Report Area (Ideally appears when simulation is running, and hides the
			reportP.setPreferredSize(new Dimension(300, 700));
			reportP.setBorder(greenBorder);
			reportP.setBackground(Color.BLACK);

			titleField = new JLabel("Real Time Result");
			nInfectedField = new JLabel(" Infected persons: ");
			nNonVField = new JLabel(" Non-vaccinated persons infected: ");
			nPartiallyVField = new JLabel(" Partially-vaccinated people infected: ");
			nFullyVField = new JLabel(" Fully-vaccinated people infected: ");
			nInfectedRecoveredField = new JLabel(" Infected people who have recovered: ");
			nInfectedDiedField = new JLabel(" Infected people who have died: ");

			titleField.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			titleField.setForeground(Color.GREEN);
			titleField.setHorizontalAlignment(SwingConstants.CENTER);
			nInfectedField.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
			nInfectedField.setForeground(Color.GREEN);
			nNonVField.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
			nNonVField.setForeground(Color.GREEN);
			nPartiallyVField.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
			nPartiallyVField.setForeground(Color.GREEN);
			nFullyVField.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
			nFullyVField.setForeground(Color.GREEN);
			nInfectedRecoveredField.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
			nInfectedRecoveredField.setForeground(Color.GREEN);
			nInfectedDiedField.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
			nInfectedDiedField.setForeground(Color.GREEN);

			reportInfected = new MyPanel();
			reportInfected.setSize(300, 450);
			reportInfected.setBackground(Color.BLACK);

			reportP.add(reportInfected);
			reportP.add(titleField);
			reportP.add(nInfectedField);
			reportP.add(nNonVField);
			reportP.add(nPartiallyVField);
			reportP.add(nFullyVField);
			reportP.add(nInfectedRecoveredField);
			reportP.add(nInfectedDiedField);

			popField.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					DEFAULT_POPULATION = (int) popField.getValue();

					popArray = new Person[DEFAULT_POPULATION];
					popArray[0] = new Person(true, true, true, 1, Color.RED, 0);
					Color color = Color.BLUE;// color to pass in to Ball constructor
					for (int i = 1; i < popArray.length; i++) {
						popArray[i] = new Person(true, false, false, 1, color, 0);
					} // end for

				}
			});
			configureP.add(noShotLabel);
			configureP.add(noShotSlider);
			configureP.add(oneShotLabel);
			configureP.add(oneShotSlider);
			configureP.add(twoShotLabel);
			configureP.add(twoShotSlider);
			configureP.add(natLabel);
			configureP.add(natSlider);
			configureP.add(popLabel);
			configureP.add(popField);
			configureP.add(errorLabel);
			configureP.add(startBtn);
			configureP.add(pauseBtn);

			resultPanel = new JPanel();
			resultPanel.setLayout(new GridLayout(18, 1, 10, 10));
			resultFrame = new JFrame("Pandemic Summary");
			resultFrame.setSize(new Dimension(500, 400));
			resultFrame.setLayout(new GridLayout(1, 2, 10, 10));
			resultFrame.setLocationRelativeTo(null);

			resltInfect = new JLabel();
			resltNoVac = new JLabel();
			resltOneVac = new JLabel();
			resltFullVac = new JLabel();
			resltRecover = new JLabel();
			resltNoVacDeath = new JLabel();
			resltOneVacDeath = new JLabel();
			resltTwoVacDeath = new JLabel();
			resltNatDeath = new JLabel();
			resultFrame.add(reportInfected);
			resultPanel.add(resltInfect);
			resultPanel.add(resltNoVac);
			resultPanel.add(resltOneVac);
			resultPanel.add(resltFullVac);
			resultPanel.add(resltRecover);
			resultPanel.add(resltNoVacDeath);
			resultPanel.add(resltOneVacDeath);
			resultPanel.add(resltTwoVacDeath);
			resultPanel.add(resltNatDeath);
			resultPanel.setBackground(Color.BLACK);
			resultPanel.setForeground(Color.GREEN);
			resultFrame.getContentPane().setBackground(Color.BLACK);
			resultFrame.add(resultPanel);
			resultFrame.setSize(new Dimension(1200,400));
			resultFrame.setVisible(false);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//OVERALL CYCLE COUNTER
			COUNTER++;
			//IF REACH 450 CYCLES STOP
			if (COUNTER == TOTAL_CYCLES) {
				time.stop();
			}
			
			// DEATH or LIFE LOOP 
			// CHECK TO SEE IF A PERSON DIES OR RECOVERS BASED ON PERCENTAGE CHANCE
			// THEN INCREMENTS THE RESPECTIVE COUNTERS FOR RECOVERED, DEATH, PERSON 
			// INFECTED WTIH NO IMMUNITY, ONE VACCINE, TWO VACCINE, NATURAL IMMUNITY
			for (int i = 0; i < popArray.length; i++) {
				if (popArray[i].isAlive() == true) {

					popArray[i].setCycleCounter(popArray[i].getCycleCounter() + 1);
					
					if (popArray[i].isInfected()) // Not vaccinated (No immunity)
					{
						if (popArray[i].getCycleCounter() == 150) {
							
							if (popArray[i].getImmunityStatus() == 1) {
								if (percentChance(0.1)) {
									popArray[i].setColor(Color.BLACK);
									popArray[i].setAlive(false);
									deathNum++;
									noVacDeathNum++;
								} else {
									popArray[i].setColor(Color.GREEN);
									popArray[i].setInfected(false);
									popArray[i].setWasInfected(true);
									recovNum++;
								}
							} else if (popArray[i].getImmunityStatus() == 2) {
								if (percentChance(0.05)) {
									popArray[i].setColor(Color.BLACK);
									popArray[i].setAlive(false);
									deathNum++;
									oneVacDeathNum++;
								} else {
									popArray[i].setColor(Color.GREEN);
									popArray[i].setInfected(false);
									popArray[i].setWasInfected(true);
									recovNum++;
								}
							} else if (popArray[i].getImmunityStatus() == 3) {
								if (percentChance(0.01)) {
									popArray[i].setColor(Color.BLACK);
									popArray[i].setAlive(false);
									deathNum++;
									twoVacDeathNum++;
								} else {
									popArray[i].setColor(Color.GREEN);
									popArray[i].setInfected(false);
									popArray[i].setWasInfected(true);
									recovNum++;

								}
							} else if (popArray[i].getImmunityStatus() == 4 && popArray[i].wasInfected()) {
								if (percentChance(0.003)) {
									popArray[i].setColor(Color.BLACK);
									popArray[i].setAlive(false);
									deathNum++;
									natDeathNum++;
								} else {
									popArray[i].setColor(Color.GREEN);
									popArray[i].setInfected(false);
									popArray[i].setWasInfected(true);
									recovNum++;

								}
							}

						}

					}
				}
			}

			// Live output
			nInfectedField.setText(" Infected persons: " + totalInfected);
			nNonVField.setText(" Non-Vaccinated persons: " + notVaccine);
			nPartiallyVField.setText(" Part-Vaccinated persons: " + partVaccine);
			nFullyVField.setText(" Full-vaccinated persons: " + fullVaccine);
			nInfectedRecoveredField.setText(" Recovered persons: " + recovNum);
			nInfectedDiedField.setText(" Dead persons: " + deathNum);

			// Final output count

			double notV = 0.0;
			double partV = 0.0;
			double fullV = 0.0;
			double recovered = 0.0;
			double noVacDeath = 0.0;
			double oneVacDeath = 0.0;
			double twoVacDeath = 0.0;
			double natDeath = 0.0;
			double total = 0.0;

			if (!time.isRunning()) {
				resultFrame.setVisible(true);
				pauseBtn.setText("Resume Simulation");
				total = ((double) totalInfected / (double) popArray.length) * 100;
				notV = ((double) notVaccine / (double) popArray.length) * 100;
				partV = ((double) partVaccine / (double) popArray.length) * 100;
				fullV = ((double) fullVaccine / (double) popArray.length) * 100;
				recovered = ((double) recovNum / (double) popArray.length) * 100;
				noVacDeath = ((double) noVacDeathNum / (double) popArray.length) * 100;
				oneVacDeath = ((double) oneVacDeathNum / (double) popArray.length) * 100;
				twoVacDeath = ((double) twoVacDeathNum / (double) popArray.length) * 100;
				natDeath = ((double) natDeathNum / (double) popArray.length) * 100;
				resltInfect.setForeground(Color.WHITE);
				resltInfect.setText("Infected Population: " + String.format("%.2f", total));
				resltNoVac.setForeground(Color.WHITE);
				resltNoVac.setText("Unvaccinated: " + String.format("%.2f", notV));
				resltOneVac.setForeground(Color.WHITE);
				resltOneVac.setText("Partially-Vaccinated: " + String.format("%.2f", partV));
				resltFullVac.setForeground(Color.WHITE);
				resltFullVac.setText("Fully-Vaccinated: " + String.format("%.2f", fullV));
				resltRecover.setForeground(Color.WHITE);
				resltRecover.setText("Recovered: " + recovered);
				resltNoVacDeath.setForeground(Color.BLUE);
				resltNoVacDeath.setText("Non-Vaccinated Death: " + String.format("%.2f",noVacDeath));
				resltOneVacDeath.setForeground(Color.CYAN);
				resltOneVacDeath.setText("Part-Vaccinated Death: " + String.format("%.2f",oneVacDeath));
				resltTwoVacDeath.setForeground(Color.YELLOW);
				resltTwoVacDeath.setText("Full-Vaccinated Death: " + String.format("%.2f",twoVacDeath));
				resltNatDeath.setForeground(Color.GREEN);
				resltNatDeath.setText("Natural Immune Death: " + String.format("%.2f",natDeath));
				// End final output count
			}
			// LET THE PERSON MOVE!
			for (Person element : popArray) {
				CovidSimulator.this.move(element);
			}
			// DID YOU CAME INTO CONTACT (SOCIAL DISTANCE!!!)
			checkCollision(popArray);
				

		}// end method
		// METHOD: stateChanged Override
		// RETURN: none
		// ACCEPTS: ChangeEvent object
		// PURPOSE: Listen to change events from sliders and set the value for labels.
		//			Also limits the sliders, so that users cannot have > 100%.
		@Override
		public void stateChanged(ChangeEvent e) {
			
			// SLider Limits;
			oneShotSlider
					.setExtent(100 - (100 - natSlider.getValue() - twoShotSlider.getValue() - noShotSlider.getValue()));
			twoShotSlider
					.setExtent(100 - (100 - natSlider.getValue() - oneShotSlider.getValue() - noShotSlider.getValue()));
			natSlider.setExtent(
					100 - (100 - twoShotSlider.getValue() - oneShotSlider.getValue() - noShotSlider.getValue()));
			noShotSlider.setExtent(
					100 - (100 - oneShotSlider.getValue() - twoShotSlider.getValue() - natSlider.getValue()));
			
			
			errorLabel.setVisible(false);
			
			// Change the balls' colors to it's respective slider values
			int noShot = (int) (noShotSlider.getValue() / 100.0 * (int) popField.getValue());
			int oneShot = (int) (oneShotSlider.getValue() / 100.0 * (int) popField.getValue());
			int twoShot = (int) (twoShotSlider.getValue() / 100.0 * (int) popField.getValue());
			int natImm = (int) (natSlider.getValue() / 100.0 * (int) popField.getValue());

			for (int i = 1; i < noShot; ++i) {
				popArray[i].setColor(Color.BLUE);
				popArray[i].setImmunityStatus(1);
				popArray[i].setWasInfected(false);
			}

			for (int i = noShot; i < noShot + oneShot; ++i) {
				popArray[i].setColor(Color.CYAN);
				popArray[i].setImmunityStatus(2);
				popArray[i].setWasInfected(false);
			}
			for (int i = noShot + oneShot; i < noShot + oneShot + twoShot; ++i) {
				popArray[i].setColor(Color.YELLOW);
				popArray[i].setImmunityStatus(3);
				popArray[i].setWasInfected(false);
			}
			for (int i = noShot + oneShot + twoShot; i < noShot + oneShot + twoShot + natImm; ++i) {
				popArray[i].setColor(Color.GREEN);
				popArray[i].setImmunityStatus(4);
				popArray[i].setWasInfected(true);
			}
			//first person is infected 
			popArray[0].setColor(Color.RED);
			
			//display the value of the slider to the user in configureP
			noShotLabel.setText("Not Vaccinated: " + noShotSlider.getValue());
			oneShotLabel.setText("Partially Vaccinated: " + oneShotSlider.getValue());
			twoShotLabel.setText("Fully Vaccinated: " + twoShotSlider.getValue());
			natLabel.setText("Natural Immunity: " + natSlider.getValue());
			// Font
			noShotLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			oneShotLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			twoShotLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
			natLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));

		}

	}// end inner class

	// if the status is not alive, make dot doesn't move
	public void move(Person p) {

		if (p.isAlive() == true) {
			if (p.getxCoordinate() >= WIDTH - IMG_DIM) {
				// we are at right side, so change xIncrement to a negative
				p.setxIncrement(p.getxIncrement() * -1);
			}
			if (p.getxCoordinate() <= 0)// changed operator to <=
			{
				// if true, we're at left edge, flip the flag
				p.setxIncrement(p.getxIncrement() * -1);

			}
			if (p.getyCoordinate() >= HEIGHT - IMG_DIM) {
				p.setyIncrement(p.getyIncrement() * -1);
			}
			if (p.getyCoordinate() <= 0) {
				// if true, we're at left edge, flip the flag
				p.setyIncrement(p.getyIncrement() * -1);

			}
			// adjust the p positions using the getters and setters
			p.setxCoordinate(p.getxCoordinate() + p.getxIncrement());
			p.setyCoordinate(p.getyCoordinate() + p.getyIncrement());
		} else {
			p.setxIncrement(0);
			p.setyIncrement(0);
		}

	}// end move
	// Method: percentChance
	// ACCEPTS: double
	// RETURNS: boolean
	// PURPOSE: returns true if within PERCENTAGE CHANCE. Ex. if 80% of getting infected, you 
    //			enter 0.8, if it returns true, you get infected. 
	public static Boolean percentChance(double chance) {

		return Math.random() <= chance;
	}

	public void checkCollision(Person p[]) {
		int deltaX;// difference in pixels of the x coordinates of the two balls being compared.
		int deltaY;// difference in pixels of the y coordinates of the two balls being compared.

		// temp variables to hold the x and y coords of both balls in the pair.
		// The balls will be referred to as firstBall and secondBall
		int firstPersonX, firstPersonY, secondPersonX, secondPersonY;

		// outer loop gets the firstBall of the pair and its coordinates.
		for (int i = 0; i < p.length - 1; i++)// LCC to length-1 to avoid out of bounds
		{

			// get the x and y co-ords of first pop of the pair
			firstPersonX = p[i].getxCoordinate();
			firstPersonY = p[i].getyCoordinate();

			// Inner loop gets the second pop of the pair
			// start inner loop counter at i+1 so we don't compare the first pop to itself.
			for (int j = i + 1; j < p.length; j++) {
				secondPersonX = p[j].getxCoordinate();
				secondPersonY = p[j].getyCoordinate();

				// now calculate deltaX and deltaY for the pair of pops
				deltaX = firstPersonX - secondPersonX;
				deltaY = firstPersonY - secondPersonY;
				// square them to get rid of negative values, then add them and take square root
				// of total
				// and compare it to pop diameter held in IMG_DIM
				if (Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= IMG_DIM)// if true, they have touched
				{
					// REVSION HERE: not using the xFlag and yFlag anymore, so now we adjust
					// the xIncrement and yIncrement by multiplying by -1
					if (p[j].isAlive() == false) {
						continue;
					} else {
						p[i].setxIncrement(p[i].getxIncrement() * -1);
						p[i].setyIncrement(p[i].getyIncrement() * -1);

						// now do the secondPerson
						p[j].setxIncrement(p[j].getxIncrement() * -1);
						p[j].setyIncrement(p[j].getyIncrement() * -1);
					}

					// ALSO, to get a bit of directional change generate a new set of random values
					// for the xIncrement
					// and yIncrement of each pop involved in the collision and assign them.
					int firstPersonnewxIncrement = (int) (Math.random() * 11 - 5);
					int firstPersonnewyIncrement = (int) (Math.random() * 11 - 5);
					int secondPersonnewxIncrement = (int) (Math.random() * 11 - 5);
					int secondPersonnewyIncrement = (int) (Math.random() * 11 - 5);

					// this will prevent pops from "getting stuck" on the borders.
					p[i].setxIncrement(firstPersonnewxIncrement);
					p[i].setyIncrement(firstPersonnewyIncrement);
					p[j].setxIncrement(secondPersonnewxIncrement);
					p[j].setyIncrement(secondPersonnewyIncrement);

					// If it's dead, don't check for collision, else check
					if (p[j].isAlive() == false) {
						continue;
					} else {
						if (p[i].getColor().equals(Color.RED) && p[j].getColor().equals(Color.BLUE)) {
							if (percentChance(0.8)) {
								p[j].setColor(p[i].getColor());
								p[j].setInfected(true);
								p[j].setCycleCounter(0);
								//if wasn't already infected, increment counters
								if (!p[j].wasInfected()) {
									totalInfected++;
									notVaccine++;
								}

							}

						}
						if (p[j].getColor().equals(Color.RED) && p[i].getColor().equals(Color.BLUE)) {
							// second pop is red, so change first pop to color of second pop
							if (percentChance(0.8)) {
								p[i].setColor(p[j].getColor());
								p[i].setInfected(true);
								p[i].setCycleCounter(0);
								
								if (!p[i].wasInfected()) {
									totalInfected++;
									notVaccine++;
								}
								p[i].setWasInfected(true);

							}
						}
						if (p[i].getColor().equals(Color.RED) && p[j].getColor().equals(Color.CYAN)) {
							if (percentChance(0.4)) {
								p[j].setColor(p[i].getColor());
								p[j].setInfected(true);
								p[j].setCycleCounter(0);
								
								if (!p[j].wasInfected()) {
									totalInfected++;
									partVaccine++;
								}
								p[j].setWasInfected(true);

							}

						}
						if (p[j].getColor().equals(Color.RED) && p[i].getColor().equals(Color.CYAN)) {
							// second pop is red, so change first pop to color of second pop
							if (percentChance(0.4)) {
								p[i].setColor(p[j].getColor());
								p[i].setInfected(true);
								p[i].setCycleCounter(0);
							
								if (!p[i].wasInfected()) {
									totalInfected++;
									partVaccine++;
								}
								p[i].setWasInfected(true);
							}

						}
						if (p[i].getColor().equals(Color.RED) && p[j].getColor().equals(Color.YELLOW)) {
							if (percentChance(0.1)) {
								p[j].setColor(p[i].getColor());
								p[j].setInfected(true);
								p[j].setCycleCounter(0);
							
								if (!p[j].wasInfected()) {
									totalInfected++;
									fullVaccine++;
								}
								p[j].setWasInfected(true);
							}
						}
						if (p[j].getColor().equals(Color.RED) && p[i].getColor().equals(Color.YELLOW)) {
							// second pop is red, so change first pop to color of second pop
							if (percentChance(0.1)) {
								p[i].setColor(p[j].getColor());
								p[i].setInfected(true);
								p[i].setCycleCounter(0);
								
								if (!p[i].wasInfected()) {
									totalInfected++;
									fullVaccine++;
								}
								p[i].setWasInfected(true);
							}

						}
						if (p[i].getColor().equals(Color.RED) && p[j].getColor().equals(Color.GREEN)) {
							if (percentChance(0.1)) {
								p[j].setColor(p[i].getColor());
								p[j].setInfected(true);
								p[j].setCycleCounter(0);
								
								if (!p[j].wasInfected()) {
									totalInfected++;
								
								}
								p[j].setWasInfected(true);

							}
						}
						if (p[j].getColor().equals(Color.RED) && p[i].getColor().equals(Color.GREEN)) {
							// second pop is red, so change first pop to color of second pop
							if (percentChance(0.1)) {
								p[i].setColor(p[j].getColor());
								p[i].setInfected(true);
								p[i].setCycleCounter(0);
								
								if (!p[i].wasInfected()) {
									totalInfected++;
								}
								p[i].setWasInfected(true);

							}

						}
					}

				} // end if
			} // end inner for
		} // end outer loop

	}
	// Main method
	public static void main(String[] args) {
		//used to create a fancy compounded border
		JPanel borderPanelFrame = new JPanel();
		borderPanelFrame.setBackground(Color.BLACK);
		
		// Objects to display the simulator
		CovidSimulator cs = new CovidSimulator();
		ContactListener cl = cs.new ContactListener();

		// create a JFrame to hold the JPanel
		JFrame frame = new JFrame("PANDEMIC SIM V1");
		// if Pause button is pressed.
		cl.pauseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (time.isRunning()) {
					time.stop();

					cl.pauseBtn.setText("Resume Simulation");
				} else {
					time.start();
					cl.pauseBtn.setText("Pause Simulation");
				}
			}

		});
		// Add a border around the simulator and Live Report
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		Border greenBorder;
		greenBorder = BorderFactory.createLineBorder(Color.GREEN);
		Border compound;
		compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
		// boilerplate
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());// ANONYMOUS object
		frame.setSize(1500, 1000);
		frame.setLocationRelativeTo(null);
		// set background color of contentPane
		frame.getContentPane().setBackground(Color.BLACK);
		borderPanelFrame.add(cs);
		// Border stuff
		compound = BorderFactory.createCompoundBorder(greenBorder, compound);

		compound = BorderFactory.createTitledBorder(compound, "Simulator", TitledBorder.CENTER,
				TitledBorder.BELOW_BOTTOM, new Font(Font.MONOSPACED, Font.PLAIN, 18), Color.GREEN);

		borderPanelFrame.setBorder(compound);

		// Add items to frame.
		frame.add(borderPanelFrame);
		frame.add(cl.reportInfected);
		frame.add(cl.configureP);
		frame.add(cl.reportP);
		frame.setJMenuBar(cl.menu);
		frame.pack();// shrinks the JFrame to the smallest size possible to conserve
						// screen real estate. Comment it out to see its effect
		frame.setVisible(true);

	}// end main

}// end class
