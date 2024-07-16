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
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class WindView extends View {

    private final int STROKE_WIDTH = 20;
    private final int ARROW_WIDTH = 8;
    private Paint circlePaint;
    private Paint markerPaint;
    private Paint arrowPaint;
    private Path arrowPath;
    private Paint linePaint;
    private Paint textPaint;

    private double mDegrees;
    private float radius, dashLength;
    private Matrix rotationMatrix; // Ma trận để xoay mũi tên

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
        markerPaint.setStrokeWidth(STROKE_WIDTH / 4);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#DAECF4"));
        textPaint.setTextSize(38);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setTextAlign(Paint.Align.CENTER);

        rotationMatrix = new Matrix(); // Khởi tạo ma trận xoay

        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(ROUND);
        linePaint.setStrokeWidth(10);
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
        mDegrees = 90;
        rotationMatrix.setRotate((float) mDegrees, getWidth() / 2f, getHeight() / 2f);
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

//        int arrowSize = (int) (radius * .1);
//        int yLineStart = yCenter - radius + 2 * STROKE_WIDTH;
//        int yLineEnd = yCenter + radius - 2 * STROKE_WIDTH;

        // Vẽ vòng tròn đứt nét
        canvas.drawCircle(xCenter, yCenter, radius, circlePaint);

        // Vẽ các chữ cái chỉ hướng gió bên trong vòng tròn và vạch trắng tại mỗi chữ
        for (int i = 0; i < CARDINAL_DIRECTIONS.length; i++) {
            double angle = Math.toRadians(i * 90); // 0, 90, 180, 270 độ cho N, E, S, W
            float x = xCenter + (float) (Math.cos(angle) * (radius * 0.75)); // Điều chỉnh vị trí chữ cái vào trong vòng tròn
            float y = yCenter + (float) (Math.sin(angle) * (radius * 0.75)) + (textPaint.getTextSize() / 2);
            canvas.drawText(CARDINAL_DIRECTIONS[i], x, y, textPaint);

            // Vẽ vạch trắng
            float xMarkerStart = xCenter + (float) (Math.cos(angle) * (radius - STROKE_WIDTH / 2));
            float yMarkerStart = yCenter + (float) (Math.sin(angle) * (radius - STROKE_WIDTH / 2));
            float xMarkerEnd = xCenter + (float) (Math.cos(angle) * (radius + STROKE_WIDTH / 4));
            float yMarkerEnd = yCenter + (float) (Math.sin(angle) * (radius + STROKE_WIDTH / 4));

            canvas.drawLine(xMarkerStart, yMarkerStart, xMarkerEnd, yMarkerEnd, markerPaint);
        }

        //Vẽ đường mũi tên nét đứt
        canvas.drawLine(xCenter, yCenter + radius + STROKE_WIDTH - 14, xCenter, yCenter - radius + 14, linePaint);

        // Vẽ mũi tên
        arrowPath.reset();
        arrowPath.moveTo(xCenter, yCenter - (radius+STROKE_WIDTH) + 14);
        arrowPath.lineTo(xCenter - 15, yCenter - radius + 20);
        arrowPath.lineTo(xCenter + 15, yCenter - radius + 20);
        arrowPath.close();
        canvas.drawPath(arrowPath, arrowPaint);

//
//        // Vẽ chữ tốc độ gió ở trung tâm
//        if (windSpeed != null) {
//            canvas.drawText(windSpeed, xCenter, yCenter + (textPaint.getTextSize() / 3), textPaint);
//        }
//
//        canvas.save(); // Lưu trạng thái của canvas
//        canvas.translate(xCenter, yCenter); // Di chuyển canvas đến tâm vòng tròn
//        canvas.concat(rotationMatrix); // Áp dụng ma trận xoay
//        canvas.drawPath(arrowPath, arrowPaint); // Vẽ mũi tên
//        canvas.restore();
    }
}
