package pt.cmov.bomberman.model;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.DONUT)
public class GameLevel {

	private static GameLevel INSTANCE = new GameLevel();

	private GameBoard board;
	private int timeLeft;
	private String player_name;
	private String level_name;
	private float explosion_timeout;
	private int explosion_range;
	private int explosion_duration;
	private float enemy_speed;
	private int robot_score;
	private int opponent_score;

	private volatile boolean gameOver;

	private String game_winner;
	private int winner_score;

	public synchronized String getGameWinner() {
		return game_winner;
	}

	public synchronized int getWinnerScore() {
		return winner_score;
	}

	public synchronized void setWinnerScore(int score) {
		winner_score = score;
	}

	public synchronized void setGameWinner(String s) {
		game_winner = s;
	}

	private GameLevel() {
		gameOver = false;
		game_winner = null;
		winner_score = -1;
	}

	public synchronized void setGameOver() {
		Log.d("LevelFileParser", "Game over!!!");
		gameOver = true;
	}

	public synchronized boolean isGameOver() {
		return gameOver;
	}

	private Context context;

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

	public void setExplosion_timeout(float explosion_timeout) {
		this.explosion_timeout = explosion_timeout;
	}

	public void setExplosion_range(int explosion_range) {
		this.explosion_range = explosion_range;
	}

	public void setExplosion_duration(int explosion_duration) {
		this.explosion_duration = explosion_duration;
	}

	public void setEnemy_speed(float enemy_speed) {
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
			board.draw(context, canvas);
	}

	public synchronized int getTimeLeft() {
		return timeLeft;
	}

	public String getLevel_name() {
		return level_name;
	}

	public float getExplosion_timeout() {
		return explosion_timeout;
	}

	public int getExplosion_range() {
		return explosion_range;
	}

	public int getExplosion_duration() {
		return explosion_duration;
	}

	public float getEnemy_speed() {
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

	public void setContext(Context context) {
		this.context = context;
	}
}
