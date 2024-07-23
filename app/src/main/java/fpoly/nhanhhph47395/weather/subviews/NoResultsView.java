package fpoly.nhanhhph47395.weather.subviews;

import static android.view.Gravity.CENTER;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import fpoly.nhanhhph47395.weather.R;

public class NoResultsView extends LinearLayout {
    private TextView tvNoResults;
    private ImageView icon;

    public NoResultsView(Context context) {
        super(context);
        init(context);
    }

    public NoResultsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoResultsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        setGravity(CENTER);

        icon = new ImageView(context);
        icon.setImageResource(R.drawable.ic_search);
        addView(icon, new LayoutParams(
                100,100
        ));

        // Tạo TextView
        tvNoResults = new TextView(context);
        tvNoResults.setText("Không có kết quả");
        tvNoResults.setTextSize(18);
        tvNoResults.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        tvParams.topMargin = 20;
        addView(tvNoResults, tvParams);

        setVisibility(GONE);
    }
}
