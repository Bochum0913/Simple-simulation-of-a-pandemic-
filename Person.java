import java.awt.Color;

public class Person {
	private final int WIDTH = 800, HEIGHT = 700, DIAM = 10;
	private boolean isAlive = true;
	private boolean isInfected = false;
	private boolean wasInfected = false;
	private int immunityStatus;
	private Color color;
	private int xCoordinate;
	private int yCoordinate;
	private int xIncrement;
	private int yIncrement;
	private int cycleCounter;

	public Person(boolean _isAlive, boolean _isInfected, boolean _wasInfected, int _immunityStatus, Color _color, int _cycleCounter) {
		// TODO Auto-generated constructor stub
		isAlive = _isAlive;
		isInfected = _isInfected;
		wasInfected = _wasInfected;
		immunityStatus = _immunityStatus;
		color = _color;

		cycleCounter = _cycleCounter;
		int randomX, randomY;
		boolean loopflag1 = true;

		// loop
		while (loopflag1) {
			// generate a random value using widthValue
			randomX = (int) (Math.random() * WIDTH - 25);
			if (randomX >= 0 && randomX <= WIDTH - 25) {
				// we have a valid x value, assign it to xCoord
				this.xCoordinate = randomX;
				// System.out.println("STUB:Valid random xCoord value of " + randomX);
				loopflag1 = false;
			}
		} // end while

		// reset flag1 to true to start second loop
		loopflag1 = true;
		while (loopflag1) {
			// repeat for yCoord
			randomY = (int) (Math.random() * HEIGHT - 25);
			if (randomY >= 0 && randomY <= HEIGHT - 15 - this.DIAM) {
				// we have a valid y value, assign it to yCoord
				this.yCoordinate = randomY;
				// System.out.println("STUB:Valid random yCoord value of " + randomY);
				loopflag1 = false;
			}
		} // end while

		// Added July 15 to get the values for the increments
		boolean loopFlag = true;
		while (loopFlag) {
			this.xIncrement = (int) (Math.random() * 11 - 5);
			this.yIncrement = (int) (Math.random() * 11 - 5);
			if (this.xIncrement == 0 && this.xIncrement == 0) {
				// run it again
				this.xIncrement = (int) (Math.random() * 11 - 5);
				this.yIncrement = (int) (Math.random() * 11 - 5);
			} else {
				loopFlag = false;
			}
		} // end loop
	}

	public Person(boolean _isAlive, boolean _isInfected, boolean _wasInfected, int _xCoordinate, int _yCoordinate, int _immunityStatus,
			Color _color, int _cycleCounter) {
		// TODO Auto-generated constructor stub
		isAlive = _isAlive;
		isInfected = _isInfected;
		wasInfected = _wasInfected;
		immunityStatus = _immunityStatus;
		color = _color;
		this.xCoordinate = _xCoordinate;
		this.yCoordinate = _yCoordinate;

		setCycleCounter(_cycleCounter);
		boolean loopFlag = true;
		while (loopFlag) {
			this.xIncrement = (int) (Math.random() * 10 - 5);
			this.yIncrement = (int) (Math.random() * 10 - 5);
			if (this.xIncrement == 0 && this.xIncrement == 0) {
				// run it again
				this.xIncrement = (int) (Math.random() * 10 - 5);
				this.yIncrement = (int) (Math.random() * 10 - 5);
			} else {
				loopFlag = false;
			}
		} // end loop

	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public boolean isInfected() {
		return isInfected;
	}

	public void setInfected(boolean isInfected) {
		this.isInfected = isInfected;
	}

	public boolean wasInfected() {
		return wasInfected;
	}

	public void setWasInfected(boolean wasInfected) {
		this.wasInfected = wasInfected;
	}
	public int getImmunityStatus() {
		return immunityStatus;
	}

	public void setImmunityStatus(int immunityStatus) {
		this.immunityStatus = immunityStatus;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public int getxIncrement() {
		return xIncrement;
	}

	public void setxIncrement(int xIncrement) {
		this.xIncrement = xIncrement;
	}

	public int getyIncrement() {
		return yIncrement;
	}

	public void setyIncrement(int yIncrement) {
		this.yIncrement = yIncrement;
	}

	public int getCycleCounter() {
		return cycleCounter;
	}

	public void setCycleCounter(int cycleCounter) {
		this.cycleCounter = cycleCounter;
	}

}
