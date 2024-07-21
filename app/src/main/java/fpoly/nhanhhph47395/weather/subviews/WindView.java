package fpoly.nhanhhph47395.weather.subviews;

import static android.graphics.Paint.Cap.ROUND;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class WindView extends View {

    private final int STROKE_WIDTH = 20;
    private Paint circlePaint;
    private Paint markerPaint;
    private Paint arrowPaint;
    private Path arrowPath;
    private Paint linePaint;
    private Paint directionPaint;
//    private Paint windPaint;
//    StaticLayout staticLayout;

    private double mDegrees;
    private float radius, dashLength;

    private final String[] CARDINAL_DIRECTIONS = {"Đ", "N", "T", "B"};

    public WindView(Context context) {
        super(context);
        init();
    }

    public WindView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WindView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float xCenter = w / 2;
        float yCenter = h / 2;
        radius = Math.min(xCenter, yCenter) - STROKE_WIDTH;
        float realRadius = (radius+STROKE_WIDTH-28.0f)*2.0f;
        dashLength = realRadius / 5;

        PathEffect dashEffect = new DashPathEffect(new float[]{dashLength * 1.5f, dashLength * 2}, 0);
        linePaint.setPathEffect(dashEffect);
    }

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.parseColor("#B0EFF4"));
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(STROKE_WIDTH);
        circlePaint.setPathEffect(new android.graphics.DashPathEffect(new float[]{2, 3}, 0));

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(Color.parseColor("#DAECF4"));
        markerPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setStrokeWidth(STROKE_WIDTH / 2);

        directionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        directionPaint.setColor(Color.parseColor("#DAECF4"));
        directionPaint.setTextSize(50);
        directionPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        directionPaint.setTextAlign(Paint.Align.CENTER);

//        windPaint.setColor(Color.BLACK);
//        windPaint.setTextSize(40);
//        windPaint.setTextAlign(Paint.Align.CENTER);
//        String windSpeed = "10 km/h";
//        staticLayout = new StaticLayout(windSpeed, (TextPaint) windPaint, (int) (2 * radius), Layout.Alignment.ALIGN_CENTER, 1.0f, 0, false);

        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(ROUND);
        linePaint.setStrokeWidth(STROKE_WIDTH);
        linePaint.setAntiAlias(true);

        arrowPaint = new Paint();
        arrowPaint.setColor(Color.WHITE);
        arrowPaint.setStyle(Paint.Style.FILL);
        arrowPaint.setAntiAlias(true);

        arrowPath = new Path();
    }

    private Path createArrowPath() {
        float arrowSize = STROKE_WIDTH * 2;
        float arrowAngle = 30; // Góc mở của mũi tên

        Path path = new Path();
        path.moveTo(0, -arrowSize / 2);
        path.lineTo(arrowSize / 2, arrowSize / 2);
        path.lineTo(-arrowSize / 2, arrowSize / 2);
        path.close();

        Matrix matrix = new Matrix();
        matrix.postRotate(arrowAngle); // Xoay mũi tên
        path.transform(matrix);

        return path;
    }

    public void setWindDirection(double degrees, String windSpeed) {
        mDegrees = degrees;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = canvas.getWidth();
        float height = canvas.getHeight();
        float xCenter = width / 2;
        float yCenter = height / 2;

        float radius = Math.min(xCenter, yCenter) - (2 * STROKE_WIDTH);

        // Vẽ vòng tròn đứt nét
        canvas.drawCircle(xCenter, yCenter, radius, circlePaint);

        // Vẽ các chữ cái chỉ hướng gió bên trong vòng tròn và vạch trắng tại mỗi chữ
        for (int i = 0; i < CARDINAL_DIRECTIONS.length; i++) {
            double angle = Math.toRadians(i * 90); // 0, 90, 180, 270 độ cho D, N, T, B
            float x = xCenter + (float) (Math.cos(angle) * (radius * 0.75)); // Điều chỉnh vị trí chữ cái vào trong vòng tròn
            float y = yCenter + (float) (Math.sin(angle) * (radius * 0.75)) + (directionPaint.getTextSize() / 2);
            canvas.drawText(CARDINAL_DIRECTIONS[i], x, y, directionPaint);

            // Vẽ vạch trắng
            float xMarkerStart = xCenter + (float) (Math.cos(angle) * (radius - STROKE_WIDTH / 2));
            float yMarkerStart = yCenter + (float) (Math.sin(angle) * (radius - STROKE_WIDTH / 2));
            float xMarkerEnd = xCenter + (float) (Math.cos(angle) * (radius + STROKE_WIDTH / 4));
            float yMarkerEnd = yCenter + (float) (Math.sin(angle) * (radius + STROKE_WIDTH / 4));

            canvas.drawLine(xMarkerStart, yMarkerStart, xMarkerEnd, yMarkerEnd, markerPaint);
        }

        // Vẽ text hiển thị tốc độ gió
         // Thay đổi tốc độ gió theo dữ liệu thực tế

        // Tạo một StaticLayout để xử lý text đa dòng

//        float textY = yCenter - staticLayout.getHeight() / 2;
//        canvas.translate(xCenter, textY); // Di chuyển đến vị trí giữa của vòng tròn
//        staticLayout.draw(canvas);
//        canvas.restore();

        // Thiết lập Matrix để xoay linePaint và arrowPaint
        Matrix matrix = new Matrix();
        matrix.setRotate((float) mDegrees, xCenter, yCenter);

        // Lưu lại trạng thái hiện tại của Canvas
        canvas.save();

        // Áp dụng Matrix vào linePaint và arrowPaint
        canvas.setMatrix(matrix);

        // Vẽ đường mũi tên nét đứt
        canvas.drawLine(xCenter, yCenter + radius + STROKE_WIDTH - 14, xCenter, yCenter - radius + 14, linePaint);

        // Vẽ mũi tên
        arrowPath.reset();
        arrowPath.moveTo(xCenter, yCenter - (radius + STROKE_WIDTH) + 14);
        arrowPath.lineTo(xCenter - 20, yCenter - radius + 20);
        arrowPath.lineTo(xCenter + 20, yCenter - radius + 20);
        arrowPath.close();
        canvas.drawPath(arrowPath, arrowPaint);

        // Khôi phục lại trạng thái của Canvas
        canvas.restore();

    }
}


//
//        // Vẽ chữ tốc độ gió ở trung tâm
//        if (windSpeed != null) {
//            canvas.drawText(windSpeed, xCenter, yCenter + (textPaint.getTextSize() / 3), textPaint);
//        }
