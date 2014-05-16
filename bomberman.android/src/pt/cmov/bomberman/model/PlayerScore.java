package pt.cmov.bomberman.model;

import java.io.Serializable;

public class PlayerScore implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int robotsKilled;
	private int playersKilled;
	private int timesKilled;
	
	public PlayerScore() {
		playersKilled = robotsKilled = timesKilled = 0;
	}

	public int getRobotsKilled() {
		return robotsKilled;
	}

	public int getPlayersKilled() {
		return playersKilled;
	}

	public int getTimesKilled() {
		return timesKilled;
	}
}
