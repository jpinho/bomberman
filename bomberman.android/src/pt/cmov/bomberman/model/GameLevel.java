package pt.cmov.bomberman.model;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.DONUT)
public class GameLevel {
	
	private static GameLevel INSTANCE = new GameLevel();
	
	private GameBoard board;
	private int timeLeft;
	private String player_name;
	private String level_name;
	private int explosion_timeout;
	private int explosion_range;
	private int explosion_duration;
	private int enemy_speed;
	private int robot_score;
	private int opponent_score;

	private GameLevel() {

	}
	
	public int getPlayerScore() {
		if (board.getPlayer() != null)
			return board.getPlayer().getScore();
		return -1;
	}

	public void setBoard(GameBoard board) {
		this.board = board;
	}

	public synchronized void setTimeLeft(int duration) {
		this.timeLeft = duration;
	}

	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}

	public void setExplosion_timeout(int explosion_timeout) {
		this.explosion_timeout = explosion_timeout;
	}

	public void setExplosion_range(int explosion_range) {
		this.explosion_range = explosion_range;
	}

	public void setExplosion_duration(int explosion_duration) {
		this.explosion_duration = explosion_duration;
	}

	public void setEnemy_speed(int enemy_speed) {
		this.enemy_speed = enemy_speed;
	}

	public void setRobot_score(int robot_score) {
		this.robot_score = robot_score;
	}

	public void setOpponent_score(int opponent_score) {
		this.opponent_score = opponent_score;
	}

	public void draw(Canvas canvas) {
		if (board != null)
			board.draw(canvas);
	}

	public synchronized int getTimeLeft() {
		return timeLeft;
	}

	public String getLevel_name() {
		return level_name;
	}

	public int getExplosion_timeout() {
		return explosion_timeout;
	}

	public int getExplosion_range() {
		return explosion_range;
	}

	public int getExplosion_duration() {
		return explosion_duration;
	}

	public int getEnemy_speed() {
		return enemy_speed;
	}

	public int getRobot_score() {
		return robot_score;
	}

	public int getOpponent_score() {
		return opponent_score;
	}

	public GameBoard getBoard() {
		return board;
	}

    public int isJoinable() {
    	Player p = board.newPlayer();
    	if (p != null)
    		return p.getPlayer_number();
    	else
    		return -1;
    }
    
	public static GameLevel getInstance() {
        return INSTANCE;
    }

	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}
}
