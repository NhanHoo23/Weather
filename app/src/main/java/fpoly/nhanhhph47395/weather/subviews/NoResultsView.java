package fpoly.nhanhhph47395.weather.subviews;

import static android.view.Gravity.CENTER;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import fpoly.nhanhhph47395.weather.R;

public class NoResultsView extends LinearLayout {
    private TextView tvMessage, tvDescription, tvOptional;
    private ImageView imgIcon;

    private int icon;
    private String message, description, optional;

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

        imgIcon = new ImageView(context);
        imgIcon.setImageResource(R.drawable.ic_search);
        addView(imgIcon, new LayoutParams(
                100,100
        ));

        // Tạo TextView
        tvMessage = new TextView(context);
        tvMessage.setText(context.getString(R.string.noResult));
        tvMessage.setTextSize(22);
        tvMessage.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_medium));
        tvMessage.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        tvParams.topMargin = 20;
        addView(tvMessage, tvParams);

//        tvDescription = new TextView(context);
//        tvDescription.setText("Ứng dụng Thời tiết không được kết nối vào internet.");
//        tvDescription.setTextSize(18);
//        tvDescription.setTextColor(Color.BLACK);
//        tvParams.topMargin = 20;
//        addView(tvDescription, tvParams);
//
//        tvOptional = new TextView(context);
//        tvOptional.setText("Đi tới cài đặt");
//        tvOptional.setTextSize(18);
//        tvOptional.setTextColor(Color.BLACK);
//        tvParams.topMargin = 20;
//        addView(tvOptional, tvParams);

        setVisibility(GONE);
    }

    public void setProperties(int icon, String message, Color messageColor, String description, Color descriptionColor, String optional, Color optionalColor) {
        imgIcon.setImageResource(icon);
        tvMessage.setText(message);

        if (description == null) {
            tvDescription.setVisibility(GONE);
        } else {
            tvDescription.setVisibility(VISIBLE);
            tvDescription.setText(description);
        }

        if (optional == null) {
            tvOptional.setVisibility(GONE);
        } else {
            tvOptional.setVisibility(VISIBLE);
            tvOptional.setText(optional);
        }
    }
}
