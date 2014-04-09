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
 * @author filipe
 *
 */
public class Bitmaps {
	
	private static Hashtable<Integer, Bitmap> bitmapTable = new Hashtable<Integer, Bitmap>();
	private static Resources resources;
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
			res = BitmapFactory.decodeResource(resources, code);
			bitmapTable.put(code, res);
		}
		if (width == -1) {
			initializeWidth(res.getWidth(), res.getHeight());
		}
		return res;
	}
	
	public static void loadBitmap(int code) {
		if (bitmapTable.get(code) != null)
			return;
		Bitmap b;
		bitmapTable.put(code, b = BitmapFactory.decodeResource(resources, code));
		if (width == -1) {
			initializeWidth(b.getWidth(), b.getHeight());
		}
	}
	
	public static int width() {
		if (width == -1)
			throw new InternalError("width and height are not initialized.");
		return width;
	}
	public static int height() {
		if (height == -1)
			throw new InternalError("width and height are not initialized.");
		return height;
	}
	
	private static void initializeWidth(int w, int h) {
		width = w;
		height = h;
	}
}
