package pt.cmov.bomberman.model;

public class PlayerScore {

	private int robotsKilled;
	private int playersKilled;
	private int timesKilled;
	
	public PlayerScore() {
		setPlayersKilled(0);
		setRobotsKilled(0);
		setTimesKilled(0);
	}

	/**
	 * @return the robotsKilled
	 */
	public int getRobotsKilled() {
		return robotsKilled;
	}

	/**
	 * @param robotsKilled the robotsKilled to set
	 */
	public void setRobotsKilled(int robotsKilled) {
		this.robotsKilled = robotsKilled;
	}

	/**
	 * @return the playersKilled
	 */
	public int getPlayersKilled() {
		return playersKilled;
	}

	/**
	 * @param playersKilled the playersKilled to set
	 */
	public void setPlayersKilled(int playersKilled) {
		this.playersKilled = playersKilled;
	}

	/**
	 * @return the timesKilled
	 */
	public int getTimesKilled() {
		return timesKilled;
	}

	/**
	 * @param timesKilled the timesKilled to set
	 */
	public void setTimesKilled(int timesKilled) {
		this.timesKilled = timesKilled;
	}

}
