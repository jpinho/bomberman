package pt.cmov.bomberman.presenter.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayerLabelView {

	private TextView txtPlayerName = null;
	private boolean initialized = false;
	private LinearLayout container;
	private String playerName;

	public PlayerLabelView(String playerName) {
		this.playerName = playerName;
	}

	private void init(Context context) {
		container = new LinearLayout(context);
		txtPlayerName = new TextView(context);
		txtPlayerName.setVisibility(View.VISIBLE);
		txtPlayerName.setTextSize(6);
		txtPlayerName.setTextColor(Color.BLACK);
		txtPlayerName.setBackgroundColor(getPlayerNameBasedColor());
		txtPlayerName.setPadding(5, 2, 5, 2);
		//aaatxtPlayerName.setGravity(Gravity.CENTER_HORIZONTAL);
		txtPlayerName.setLines(1);
		txtPlayerName.setEllipsize(TextUtils.TruncateAt.END);
		txtPlayerName.setSingleLine(true);
		txtPlayerName.setAllCaps(true);
		txtPlayerName.measure(MeasureSpec.AT_MOST, MeasureSpec.AT_MOST);
		container.addView(txtPlayerName);
		initialized = true;
	}

	private int getPlayerNameBasedColor() {
		int opacity = 100;
		
		int pivot = playerName.hashCode() % 256;
		int red = pivot > 127 ? 255 : (pivot % 52);
		int blue = red != 255 ? 255 : ((pivot % 255) < 127 ? 0 : (pivot % 52));
		int green = pivot > 127 ? ((pivot % 255) < 127 ? 0 : (pivot % 52)) : 255;

		return Color.argb(opacity, red, green, blue);
	}

	public void draw(Context ctx, Canvas canvas, int row, int column, double offsetLeft,
			double offsetTop, double objWidth, double objHeight) {

		if (!initialized)
			init(ctx);

		txtPlayerName.setText(playerName);
		container.measure(canvas.getWidth(), canvas.getHeight());
		container.layout(0, 0, (int) ((objWidth * 2) + (objWidth * 2) / 2), (int) (objHeight / 2));
		txtPlayerName.setWidth((int) (objWidth * 2));

		float x = (float) (offsetLeft + objWidth * (column - 1) + objWidth / 2);
		float y = (float) (offsetTop + objHeight * (row - 1) + (objHeight / 4));
		
		canvas.save();
		canvas.translate(x,y);
		container.draw(canvas);
		canvas.restore();
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}