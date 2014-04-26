package pt.cmov.bomberman.model;

import java.util.List;

import pt.cmov.bomberman.R;
import pt.cmov.bomberman.util.Misc;
import pt.cmov.bomberman.util.Tuple;

public class Enemy extends GameObject {
	private int x;
	private int y;
	private int target_x;
	private int target_y;
	private List<Tuple<Integer, Integer>> targetPath;

	public Enemy(int x, int y) {
		super(R.drawable.enemy);
		this.x = x;
		this.y = y;
		this.target_x = x;
		this.target_y = y;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "E";
	}

	/**
	 * @return the target_x
	 */
	public int getTarget_x() {
		return target_x;
	}

	/**
	 * @param target_x
	 *            the target_x to set
	 */
	public void setTarget_x(int target_x) {
		this.target_x = target_x;
	}

	/**
	 * @return the target_y
	 */
	public int getTarget_y() {
		return target_y;
	}

	/**
	 * @param target_y
	 *            the target_y to set
	 */
	public void setTarget_y(int target_y) {
		this.target_y = target_y;
	}

	/**
	 * @return the targetPath
	 */
	public List<Tuple<Integer, Integer>> getTargetPath() {
		return targetPath;
	}

	/**
	 * @param targetPath
	 *            the targetPath to set
	 */
	public void setTargetPath(List<Tuple<Integer, Integer>> targetPath) {
		this.targetPath = targetPath;
>>>>>>> Stashed changes
	}
}
