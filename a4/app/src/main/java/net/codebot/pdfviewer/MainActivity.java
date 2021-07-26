package net.codebot.pdfviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


// PDF sample code from
// https://medium.com/@chahat.jain0/rendering-a-pdf-document-in-android-activity-fragment-using-pdfrenderer-442462cb8f9a
// Issues about cache etc. are not at all obvious from documentation, so we should expect people to need this.
// We may wish to provied them with this code.

public class MainActivity extends AppCompatActivity  {


    final String LOGNAME = "pdf_viewer";
    //final String LOGNAME = "pdf_image";
    final String FILENAME = "shannon1948.pdf";
    final int FILERESID = R.raw.shannon1948;


    PaintLine line;
    LinearLayout layout;

    static ScaleGestureDetector scaleGesture;
    float scaleBy = 1.0f;



    // Toolbar actions

    static boolean highlight;
    static boolean draw;
    static boolean erase = false;
    static MenuItem undo;
    static MenuItem redo;


    // Page Up/Down
    int page = 0;
    Button pageUp;
    Button pageDown;
    TextView pageNum;



    // manage the pages of the PDF, see below
    PdfRenderer pdfRenderer;
    private ParcelFileDescriptor parcelFileDescriptor;
    private PdfRenderer.Page currentPage;


    // custom ImageView class that captures strokes and draws them over the image

    PDFimage pageImage;
    PDFimage pageImage0;
    PDFimage pageImage1;
    PDFimage pageImage2;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* ACTION BAR */

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PDFReader");
        actionBar.setSubtitle("Shannon1984.pdf");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        /* PDF */

        pageImage0 = new PDFimage(this);

        pageImage1 = new PDFimage (this);

        pageImage2 = new PDFimage(this);


        layout = findViewById(R.id.pdfLayout);


        pageImage0.setMinimumWidth(1500);
        pageImage0.setMinimumHeight(2000);

        pageImage1.setMinimumWidth(1500);
        pageImage1.setMinimumHeight(2000);

        pageImage2.setMinimumWidth(1500);
        pageImage2.setMinimumHeight(2000);

        scaleGesture= new ScaleGestureDetector(this, new ScaleListener());


        Log.d(LOGNAME, "Page is: " + page);

        /* PAGE UP DOWN */
        pageDown = (Button) findViewById(R.id.pagedown);
        pageUp = (Button) findViewById(R.id.pageup);
        pageNum = findViewById(R.id.pagenum);


        pageDown.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Log.d(LOGNAME, "Page down");
                if (page > 0){
                    page --;
                    layout.removeViewAt(0);
                    layout.setEnabled(true);
                    if (page == 0){
                        pageImage = pageImage0;
                        layout.addView(pageImage0);
                    }else if (page == 1){
                        pageImage = pageImage1;
                        layout.addView(pageImage1);
                    }else if (page == 2){
                        pageImage = pageImage2;
                        layout.addView(pageImage2);
                    }
                    if (pageImage.undopaths.size() > 0){
                        undo.setIcon(R.drawable.undo_selected);
                    }else {
                        undo.setIcon(R.drawable.undo_unselected);
                    }
                    if (pageImage.redopaths.size() > 0){
                        redo.setIcon(R.drawable.undo_selected);
                    }else{
                        redo.setIcon(R.drawable.redo_unselected);
                    }

                    String pn = "Page: " + String.valueOf(page + 1) + "/3";
                    pageNum.setText(pn);

                }
            }
        });

        pageUp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Log.d(LOGNAME, "Page up");
                if (page < 2){
                    page ++;
                    layout.removeViewAt(0);
                    layout.setEnabled(true);
                    if (page == 0){
                        pageImage = pageImage0;
                        layout.addView(pageImage0);

                    }else if (page == 1){
                        pageImage = pageImage1;
                        layout.addView(pageImage1);
                    }else if (page == 2){
                        pageImage = pageImage2;
                        layout.addView(pageImage2);
                    }
                    if (pageImage.undopaths.size() > 0){
                        undo.setIcon( R.drawable.undo_selected);
                    }else {
                        undo.setIcon(R.drawable.undo_unselected);
                    }
                    if (pageImage.redopaths.size() > 0){
                        redo.setIcon(R.drawable.redo_selected);
                    }else{
                        redo.setIcon(R.drawable.redo_unselected);
                    }
                    String pn = "Page: " + String.valueOf(page + 1) + "/3";
                    pageNum.setText(pn);
                }
            }
        });

        try {
            openRenderer(this);
            page = savedInstanceState == null ? 0 : savedInstanceState.getInt("Page");
            Log.d(LOGNAME, "Opening Page " + page);
            String pn = "Page: " + String.valueOf(page + 1) + "/3";
            pageNum.setText(pn);

            showPage(0, pageImage0);
            showPage(1, pageImage1);
            showPage(2, pageImage2);

            pageImage = pageImage0;
            layout.addView(pageImage0);

        } catch (IOException exception) {
            Log.d(LOGNAME, "Error opening PDF");
        }

    }


    // method to inflate the options menu when
    // the user opens the menu for the first time
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate(R.menu.main, menu);
        undo = menu.getItem(0);
        redo = menu.getItem(1);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.undo:
                Log.d(LOGNAME, "clicked undo");

                if (pageImage != null){
                    pageImage.undo();
                }

                if (pageImage.undopaths.size() > 0){
                    undo.setIcon(ContextCompat.getDrawable(this, R.drawable.undo_selected));
                }else {
                    undo.setIcon(ContextCompat.getDrawable(this, R.drawable.undo_unselected));
                }
                if (pageImage.redopaths.size() > 0){
                    redo.setIcon(ContextCompat.getDrawable(this, R.drawable.redo_selected));
                }else{
                    redo.setIcon(ContextCompat.getDrawable(this, R.drawable.redo_unselected));
                }



                return true;


            case R.id.redo:
                Log.d(LOGNAME, "clicked redo");
                if (pageImage != null){
                    pageImage.redo();
                }
                if (pageImage.redopaths.size() > 0){
                    redo.setIcon(ContextCompat.getDrawable(this, R.drawable.redo_selected));
                }else{
                    redo.setIcon(ContextCompat.getDrawable(this, R.drawable.redo_unselected));
                }
                if (pageImage.undopaths.size() > 0){
                    undo.setIcon(ContextCompat.getDrawable(this, R.drawable.undo_selected));
                }else {
                    undo.setIcon(ContextCompat.getDrawable(this, R.drawable.undo_unselected));
                }

                return true;
            case R.id.highlight:

                if (!highlight){
                    highlight = true;
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.highlight_selected));
                }else{
                    highlight = false;
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.highlight_unselected));
                }

                Log.d(LOGNAME, "clicked highlight");
                return true;

            case R.id.draw:

                if (!draw){
                    draw = true;
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.pen_selected));
                }else{
                    draw = false;
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.pen_unselected));
                }
                Log.d(LOGNAME, "clicked draw");
                return true;
            case R.id.erase:
                if (!erase){
                    erase = true;
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.erase_selected));
                }else{
                    erase = false;
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.erase_unselected));
                }

                if (pageImage.undopaths.size() > 0){
                    undo.setIcon(ContextCompat.getDrawable(this, R.drawable.undo_selected));
                }else {
                    undo.setIcon(ContextCompat.getDrawable(this, R.drawable.undo_unselected));
                }
                if (pageImage.redopaths.size() > 0){
                    redo.setIcon(ContextCompat.getDrawable(this, R.drawable.redo_selected));
                }else{
                    redo.setIcon(ContextCompat.getDrawable(this, R.drawable.redo_unselected));
                }

                Log.d(LOGNAME, "clicked erase");
                return true;
            default:
                return true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStop() {
        super.onStop();
        try {
            closeRenderer();
        } catch (IOException ex) {
            Log.d(LOGNAME, "Unable to close PDF renderer");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openRenderer(Context context) throws IOException {
        // In this sample, we read a PDF from the assets directory.
        File file = new File(context.getCacheDir(), FILENAME);
        if (!file.exists()) {
            // pdfRenderer cannot handle the resource directly,
            // so extract it into the local cache directory.
            InputStream asset = this.getResources().openRawResource(FILERESID);
            FileOutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            asset.close();
            output.close();
        }

        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);

        // capture PDF data
        // all this just to get a handle to the actual PDF representation
        if (parcelFileDescriptor != null) {
            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
        }
    }

    // do this before you quit!
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void closeRenderer() throws IOException {
        if (null != currentPage) {
            currentPage.close();
        }
        pdfRenderer.close();
        parcelFileDescriptor.close();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showPage(int index, PDFimage pgimage) {
        if (pdfRenderer.getPageCount() <= index) {
            return;
        }
        // Close the current page before opening another one.
        if (null != currentPage) {
            currentPage.close();
        }
        // Use `openPage` to open a specific page in PDF.
        currentPage = pdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).


        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(), Bitmap.Config.ARGB_8888);

        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        // Display the page
        pgimage.setImage(bitmap);
        pgimage.setCanvas(bitmap);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Page", page);
    }

    /*
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        page = savedInstanceState.getInt("Page");
        if (page < 3 && page >= 0){
            showPage(page);
        }
    }
    */
    /*
    public boolean onTouchEvent(MotionEvent event) {
        float X = event.getX();
        float Y = event.getY();

        Log.d(LOGNAME, "Original Y " + Y);
        Y -= 270;

        Log.d(LOGNAME, "scaleFactor: " + mScaleFactor);

        Log.d(LOGNAME, "X: " + X + " Y: " + Y);

        boolean isToolbarAction = highlight || draw || erase;

        if (isToolbarAction){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(LOGNAME, "Action down");
                    pageImage.path  = new Path();

                    if (draw && !highlight && !erase){
                        line = new PaintLine(Color.argb(255,0,0,128), 8, pageImage.path, false);
                        pageImage.path.moveTo(X, Y);
                    }else if (highlight && !draw && !erase){
                        line = new PaintLine(Color.YELLOW, 30, pageImage.path, false);
                        pageImage.path.moveTo(X, Y);
                    }else if (erase && !highlight && !draw){
                        PaintLine removeLine = null;
                        for (PaintLine pl: pageImage.paths){
                            RectF pBounds = new RectF();
                            pl.path.computeBounds(pBounds, true);
                            if (pBounds.contains(X, Y)){
                                removeLine = pl;
                                Log.d(LOGNAME, "eraser selected line to delete");
                            }
                        }
                        if (removeLine != null){
                            removeLine.erase = true;
                            pageImage.undopaths.add(removeLine);
                            pageImage.paths.remove(removeLine);

                        }

                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!erase && (draw || highlight)){
                        pageImage.path.lineTo(X, Y);
                        Log.d(LOGNAME, "Action move");
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if (!erase && (draw || highlight)){
                        Log.d(LOGNAME, "Action up");
                        pageImage.path.lineTo(X, Y);
                        pageImage.paths.add(line);
                        pageImage.undopaths.add(line);
                    }
                    break;
            }
        }else{
            mScaleGestureDetector.onTouchEvent(event);
        }
        if (pageImage.undopaths.size() > 0){
            undo.setIcon(ContextCompat.getDrawable(this, R.drawable.undo_selected));
        }else {
            undo.setIcon(ContextCompat.getDrawable(this, R.drawable.undo_unselected));
        }

        if (pageImage.redopaths.size() > 0){
            redo.setIcon(ContextCompat.getDrawable(this, R.drawable.redo_selected));
        }else{
            redo.setIcon(ContextCompat.getDrawable(this, R.drawable.redo_unselected));
        }
        return true;
    }

     */



    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            scaleBy *= scaleGestureDetector.getScaleFactor();
            scaleBy = Math.max(0.1f, Math.min(scaleBy, 10.0f));
            ((ImageView) pageImage).setScaleX(scaleBy);
            ((ImageView) pageImage).setScaleY(scaleBy);
            Log.d(LOGNAME, "Scaling");
            return true;
        }
    }


}


