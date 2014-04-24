package pt.cmov.bomberman.net;

import pt.cmov.bomberman.model.GameLevel;

public class GameBoardControllerServer extends GameBoardController {

	@Override
	public boolean requestMovePlayer(int pid, int dir) {
		boolean res = GameLevel.getInstance().requestMovePlayer(pid, dir);
		if (res) {
			Server.notifyMovePlayer(pid, dir);
		}
		return res;
	}

	@Override
	public boolean requestPlaceBomb(int player) {
		boolean res = GameLevel.getInstance().requestPlaceBomb(player);
		if (res) {
			Server.notifyPlaceBomb(player);
		}
		return res;
	}
	
	public void sendEnemiesPositions(String new_positions) {
		Server.sendEnemies(new_positions);
	}
}
