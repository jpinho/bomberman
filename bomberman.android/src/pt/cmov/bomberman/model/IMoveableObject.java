package pt.cmov.bomberman.model;

public interface IMoveableObject {

	public void handleActionUp(int eventX, int eventY);

	public void handleActionDown(int eventX, int eventY);

	public void handleActionLeft(int eventX, int eventY);

	public void handleActionRight(int eventX, int eventY);
}