package fpoly.nhanhhph47395.weather.subviews;

import static android.view.Gravity.CENTER;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import fpoly.nhanhhph47395.weather.R;

public class NoResultsView extends LinearLayout {
    private TextView tvMessage;
    private ImageView imgIcon;
    private Button btnRetry;

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
                100, 100
        ));

        // Tạo TextView
        tvMessage = new TextView(context);
        tvMessage.setText(context.getString(R.string.noResult));
        tvMessage.setTextSize(22);
        tvMessage.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_medium));
        tvMessage.setTextColor(context.getResources().getColor(R.color.mainTextColor));
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        tvParams.topMargin = 20;
        addView(tvMessage, tvParams);

        // Tạo Button "Thử lại"
        btnRetry = new Button(context);
        btnRetry.setText(context.getString(R.string.retry));
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        btnParams.topMargin = 20;
        addView(btnRetry, btnParams);

        setVisibility(GONE);
    }

    public void setRetryClickListener(OnClickListener listener) {
        btnRetry.setOnClickListener(listener);
    }

    public void showNoResults() {
        imgIcon.setImageResource(R.drawable.ic_search);

        tvMessage.setText(getContext().getString(R.string.noResult));
        btnRetry.setVisibility(GONE);
        setVisibility(VISIBLE);
    }

    public void showNetworkError() {
        imgIcon.setImageResource(R.drawable.ic_network_error);
        tvMessage.setText(getContext().getString(R.string.networkError));
        btnRetry.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
    }

    public void showNetworkErrorWithoutButton() {
        imgIcon.setImageResource(R.drawable.ic_network_error);
        tvMessage.setText(getContext().getString(R.string.networkError));
        btnRetry.setVisibility(GONE);
        setVisibility(VISIBLE);
    }
}
