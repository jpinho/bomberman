package pt.cmov.bomberman.util;

import java.util.Hashtable;

import android.content.res.Resources;
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
 * The initialization is performed by calling init() and by making sure that setWidht() and setHeight()
 * are called before any getBitmap() request is issued. This is necessary because the class needs to
 * know the width and height for a bitmap so that it can resize bitmaps when they are created.
 * 
 * setWidht() and setHeight() are called by the level generator when reading a level configuration's file.
 * Thus, it is imperative that level parsing is ALWAYS performed before activating the render loop or even before
 * calling getBitmap().
 */
public class Bitmaps {
	/* Width and height of raw image files */
	public static final int ORIGINAL_WIDTH = 43;
	public static final int ORIGINAL_HEIGHT = 46;
	
	private static Hashtable<Integer, Bitmap> bitmapTable = new Hashtable<Integer, Bitmap>();
	private static Resources resources;
	
	/* Width and height of the bitmaps. This is determined by the level file parser when reading the map
	 * configuration, which calculates the final width and height each bitmap image must have in order to draw
	 * the map on the device's screen.
	 */
	private static int width;
	private static int height;

	public static void init(Resources r) {
		resources = r;
		width = -1;
		height = -1;
	}
	
	public static Bitmap getBitmap(int code) {
		Bitmap res;
		if ((res = bitmapTable.get(code)) == null) {
			Bitmap b = BitmapFactory.decodeResource(resources, code); 
			res = Bitmap.createScaledBitmap(b, width, height, false);
			bitmapTable.put(code, res);
		}
		return res;
	}
	
	public static int width() {
		return width;
	}
	public static int height() {
		return height;
	}
	public static void setWidth(int w) {
		width = w;
	}
	public static void setHeight(int h) {
		height = h;
	}
}
