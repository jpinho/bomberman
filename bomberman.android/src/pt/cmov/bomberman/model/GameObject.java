package pt.cmov.bomberman.model;

import pt.cmov.bomberman.R;
import pt.cmov.bomberman.util.Bitmaps;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class GameObject {
	private int bitmapCode;
	
	public GameObject(int bitmapCode) {
		this.bitmapCode = bitmapCode;
	}
	
	public Bitmap getBitmap() {
		return Bitmaps.getBitmap(bitmapCode);
	}
	
	/* Used by the collision system.
	 * Indicates whether a game object is solid. If it is, other objects
	 * cannot pass through.
	 */
	public boolean isSolid() {
		return true;
	}
	
	// Screen coordinates
	public void draw(Canvas canvas, float x, float y) {		
		canvas.drawBitmap(getBitmap(), x, y, null);
	}
	
	public void draw(Canvas canvas, float x, float y, int floorLayer){
		canvas.drawBitmap(Bitmaps.getBitmap(floorLayer), x, y, null);
		draw(canvas, x, y);
	}
	
	/* Called when an explosion hits this object
	 * Returns true if the object must be destroyed; false otherwise
	 */
	public boolean notifyExplosion(Player responsible) {
		 return false;
	}
	
	public Player isLethal() {
		return null;
	}
	
	public void setBitmapCode(int bitmapCode) {
		this.bitmapCode = bitmapCode;
	}
	
	@Override
	public String toString() {
		return "-";
	}
}
