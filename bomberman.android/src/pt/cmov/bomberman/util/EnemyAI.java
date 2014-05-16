package pt.cmov.bomberman.util;

public class EnemyAI {
	/**
	 * Enemy AI Movement - Unfinished, is too slow! 
	 *
	
	private synchronized Tuple<Integer, Integer> chooseNextEnemyPosition(Enemy e, int e_x, int e_y) {
		int x;
		int y;

		// target reached or no more path to it, let's choose another point for to enemy to focus on.
		if(e.getTargetPath() == null || e.getTargetPath().isEmpty()){
			boolean valid_pos = false;
			
			while(!valid_pos){
				e.setTarget_x(Misc.randInt(0, this.nCols-1));
				e.setTarget_y(Misc.randInt(0, this.nRows-1));
				valid_pos = validPosition(e.getTarget_x(), e.getTarget_y());
				
				// tracing initial route to target.
				if(valid_pos) 
					e.setTargetPath(find_path(e_x, e_y, e.getTarget_x(), e.getTarget_y()));
			}
		}
		
		List<Tuple<Integer, Integer>> path = e.getTargetPath();
		
		if(path == null || path.isEmpty())
			return null;
		
		// if route still valid follow it.
		if(validPosition(path.get(0).x, path.get(0).y)){
			x = path.get(0).x;
			y = path.get(0).y;
		}
		else return null;
		
		// traveled position is removed to allow the enemy to move on down the path.
		e.setTargetPath(path.subList(1, path.size()));
		
		return new Tuple<Integer, Integer>(x, y);
	}
		 
	private List<Tuple<Integer, Integer>> find_path(int e_x, int e_y, int t_x, int t_y){
	 
		List<Tuple<Integer, Integer>> path;
		boolean visit[][] = new boolean[nRows][nCols];
		
		do{
			path = find_path(e_x, e_y, t_x, t_y, visit);
			
			if(path.isEmpty())
				continue;
		}
		while(path.get(path.size()-1).x != t_x && path.get(path.size()-1).y != t_y);
		
		return path;
	}

	private List<Tuple<Integer, Integer>> find_path(int e_x, int e_y, int t_x, int t_y, boolean[][] visit) {
		List<Tuple<Integer, Integer>> result = new LinkedList<Tuple<Integer, Integer>>();
		
		if(e_x == t_x && e_y == t_y){
			result.add(new Tuple<Integer, Integer>(t_x, t_y));
			return result;
		}
		
		int directions[] = { -1, 0, 1, 0, 0, -1, 0, 1 };

		// Up | Down | Left | Right
		boolean directions_tried[] = { false, false, false, false };
		int x=0,y=0,direction;
		int total_attempts=0;
		
		while (total_attempts < directions_tried.length) {
			direction = Misc.randInt(0, directions_tried.length - 1);
			if (directions_tried[direction] == false) {
				directions_tried[direction] = true;
				total_attempts++;
			}
			x = directions[direction * 2] + e_x;
			y = directions[direction * 2 + 1] + e_y;
			
			if(!validPosition(x,y))
				continue;
			
			if(visit[x][y])
				continue;
		} 
			
		// blocked, return empty list to append to the previous call.
		if(!validPosition(x, y) || (validPosition(x, y) && visit[x][y]))
			return result;
		
		visit[x][y] = true;
		
		// add new position to path.
		result.add(new Tuple<Integer, Integer>(x, y));
		
		// keep trying to find the target from the current (x,y) pos.
		result.addAll(find_path(x, y, t_x, t_y, visit));
		
		return result;
	}*/
}
