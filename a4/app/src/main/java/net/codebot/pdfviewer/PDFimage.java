package net.codebot.pdfviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class PDFimage extends ImageView {
    public Stroke line;
    public boolean erase = false;
    final String LOGNAME = "pdf_image";

    // drawing path
    Path path = null;
    ArrayList<Stroke> paths = new ArrayList();




    // image to display
    Bitmap bitmap;
    Paint paint = new Paint();

    Canvas canvas;

    float mX, mY;

    // constructor
    public PDFimage(Context context) {
        super(context);
    }

    // capture touch events (down/move/up) to create a path
    // and use that to create a stroke that we can draw
    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
       /*
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(LOGNAME, "Action down");
                path = new Path();
                path.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(LOGNAME, "Action move");
                path.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                Log.d(LOGNAME, "Action up");
                paths.add(path);
                break;
        }


        return true;
    }
    */
    // set image as background
    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setCanvas(Bitmap bitmap){
        canvas = new Canvas(this.bitmap);
    }

    // set brush characteristics
    // e.g. color, thickness, alpha
    public void setBrush() {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw background
        if (bitmap != null) {
            this.setImageBitmap(bitmap);
        }
        // draw lines over it
        for (Stroke line : paths) {
            paint.setColor(line.color);
            paint.setStrokeWidth(line.strokeWidth);
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            if (line.erase){
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            }
           // paint.setAlpha(255);
            canvas.drawPath(line.path, paint);

        }

        super.onDraw(canvas);
    }

    public static class Stroke {
        public int color;
        public int strokeWidth;
        public boolean erase;
        public Path path;

        public Stroke(int color, int strokeWidth, Path path, Boolean erase){
            this.color = color;
            this.strokeWidth = strokeWidth;
            this.path = path;
            this.erase = erase;

        }

    }
}
