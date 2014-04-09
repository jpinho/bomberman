package pt.cmov.bomberman.model;

public class PlayerScore {

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
