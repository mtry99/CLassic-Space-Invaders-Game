package net.codebot.pdfviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class PDFimage extends ImageView {

    final String LOGNAME = "pdf_image";

    // drawing path
    Path path = null;
    ArrayList<PaintLine> paths = new ArrayList();
    ArrayList<PaintLine> undopaths = new ArrayList();
    ArrayList<PaintLine> redopaths = new ArrayList();
    //Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    PaintLine line;

    // image to display
    Bitmap background;
    Paint paint = new Paint();

    Canvas canvas;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float X = event.getX();
        float Y = event.getY();

        boolean isToolbarAction = MainActivity.highlight || MainActivity.draw || MainActivity.erase;

        if (isToolbarAction){
            Log.d(LOGNAME, "Highlight: " + MainActivity.highlight);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(LOGNAME, "Action down");
                    path  = new Path();

                    if (MainActivity.draw && !MainActivity.highlight && !MainActivity.erase){
                        line = new PaintLine(Color.argb(255,0,0,128), 5, path, false);
                        path.moveTo(X, Y);
                    }else if (MainActivity.highlight && !MainActivity.draw && !MainActivity.erase){
                        line = new PaintLine(Color.YELLOW, 30, path, false);
                        path.moveTo(X, Y);
                    }else if ( MainActivity.erase && !MainActivity.highlight && ! MainActivity.draw){
                        PaintLine removeLine = null;
                        for (PaintLine pl: paths){
                            RectF pBounds = new RectF();
                            pl.path.computeBounds(pBounds, true);
                            if (pBounds.contains(X, Y)){
                                removeLine = pl;
                                Log.d(LOGNAME, "eraser selected line to delete");
                            }
                        }
                        if (removeLine != null){
                            removeLine.erase = true;
                            undopaths.add(removeLine);
                            paths.remove(removeLine);

                        }

                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!MainActivity.erase && (MainActivity.draw|| MainActivity.highlight)){
                        path.lineTo(X, Y);
                        Log.d(LOGNAME, "Action move");
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if (!MainActivity.erase && (MainActivity.draw|| MainActivity.highlight)){
                        Log.d(LOGNAME, "Action up");
                        path.lineTo(X, Y);
                        paths.add(line);
                        undopaths.add(line);
                    }
                    break;
            }

            if (undopaths.size() > 0){
                (MainActivity.undo).setIcon(R.drawable.undo_selected);
            }else {
                (MainActivity.undo).setIcon(R.drawable.undo_unselected);
            }

            if (redopaths.size() > 0){
                (MainActivity.redo).setIcon(R.drawable.redo_selected);
            }else{
                (MainActivity.redo).setIcon(R.drawable.redo_unselected);
            }
        }else{

            MainActivity.mScaleGestureDetector.onTouchEvent(event);
       //     mScaleGestureDetector.onTouchEvent(event);
        }
        return true;
    }

    // constructor
    public PDFimage(Context context) {
        super(context);
    //    mScaleGestureDetector =  new ScaleGestureDetector(context, new ScaleListener());
        setBrush();
    }

    // set image as background

    public void setImage(Bitmap bitmap) {
        this.background = bitmap;
    }

    public void setCanvas(Bitmap bitmap){
        canvas = new Canvas(bitmap);
    }

    public void undo(){
        if (undopaths.size() != 0){
            PaintLine obj = undopaths.get(undopaths.size() - 1);

            if (obj.erase){
                paths.add(obj);
            }else{
                paths.remove(obj);

            }
            undopaths.remove(obj);

            redopaths.add(obj);
        }
    }
    public void redo(){
        if (redopaths.size() != 0){
            PaintLine obj = redopaths.get(redopaths.size() - 1);
            if (obj.erase) {
                paths.remove(obj);
            }else{
                paths.add(obj);
            }

            redopaths.remove(obj);
            undopaths.add(obj);
        }
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
        if (background != null) {
            this.setImageBitmap(background);
        }

        // draw lines over it
        for (PaintLine line : paths) {
            paint.setColor(line.color);
            paint.setStrokeWidth(line.strokeWidth);
           // paint.setAlpha(255);
            canvas.drawPath(line.path, paint);
        }
        super.onDraw(canvas);
        //canvas.drawBitmap(background, 0, 0, mBitmapPaint);
    }

}
