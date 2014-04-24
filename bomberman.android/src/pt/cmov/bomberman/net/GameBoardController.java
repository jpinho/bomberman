package pt.cmov.bomberman.net;

public abstract class GameBoardController {
	public abstract boolean requestMovePlayer(int pid, int dir);
	public abstract boolean requestPlaceBomb(int player);
}
