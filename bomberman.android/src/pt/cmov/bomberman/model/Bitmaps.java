package pt.cmov.bomberman.model;

import java.util.Hashtable;

import pt.cmov.bomberman.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Utility class that provides access to the game's graphical components in the form of a Bitmap.
 * Bitmaps for a given component are only instantiated once, although they are used multiple times
 * when building a map.
 * 
 * NOTE: This class MUST be initialized before any other event takes place, namely, map generation,
 * canvas drawing, etc.
 * 
 * @author filipe
 *
 */
public class Bitmaps {
	private static final int[] components = {
		R.drawable.bman_down,
		R.drawable.bman_left,
		R.drawable.bman_right,
		R.drawable.bman_up,
		R.drawable.bomb,
		R.drawable.box_wall,
		R.drawable.paviment,
		R.drawable.rock
	};
	
	private static Hashtable<Integer, Bitmap> bitmapTable = new Hashtable<Integer, Bitmap>();
	
	public static void init(Context ctx) {
		for (int component : components) {
			bitmapTable.put(component, BitmapFactory.decodeResource(ctx.getResources(), component));
		}
	}
	
	public static Bitmap getBitmap(int code) {
		return bitmapTable.get(code);
	}
	
	public static int width() {
		return bitmapTable.get(components[0]).getWidth();
	}
	public static int height() {
		return bitmapTable.get(components[0]).getHeight();
	}
}
