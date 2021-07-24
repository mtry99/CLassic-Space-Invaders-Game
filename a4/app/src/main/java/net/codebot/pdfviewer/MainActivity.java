package net.codebot.pdfviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
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


    // Toolbar actions

    boolean undo, redo, highlight, draw, erase = false;


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
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;


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
        LinearLayout layout = findViewById(R.id.pdfLayout);
        pageImage = new PDFimage(this);
        layout.addView(pageImage);
        layout.setEnabled(true);

        pageImage.setMinimumWidth(1500);
        pageImage.setMinimumHeight(2000);
        Log.d(LOGNAME, "Page is: " + page);

        /* PAGE UP DOWN */
        pageDown = (Button) findViewById(R.id.pagedown);
        pageUp = (Button) findViewById(R.id.pageup);
        pageNum = findViewById(R.id.pagenum);

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        pageDown.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Log.d(LOGNAME, "Page down");
                if (page > 0){
                    page --;
                    showPage(page);
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
                    showPage(page);
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
            showPage(page);
        } catch (IOException exception) {
            Log.d(LOGNAME, "Error opening PDF");
        }


    }


    // method to inflate the options menu when
    // the user opens the menu for the first time
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.undo:
                Log.d(LOGNAME, "clicked undo");
                if (!undo){
                    undo = true;
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.undo_selected));
                }else{
                    undo = false;
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.undo_unselected));
                }
                return true;

            case R.id.redo:
                Log.d(LOGNAME, "clicked redo");
                if (!redo){
                    redo = true;
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.redo_selected));
                }else{
                    redo = false;
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.redo_unselected));
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
    private void showPage(int index) {
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
        pageImage.setImageBitmap(bitmap);
        pageImage.setCanvas(bitmap);
    }




    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Page", page);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        page = savedInstanceState.getInt("Page");
        if (page < 3 && page >= 0){
            showPage(page);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        float X = event.getX();
        float Y = event.getY();

        boolean isToolbarAction = undo || redo || highlight || draw || erase;
        if (isToolbarAction){

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(LOGNAME, "Action down");
                    pageImage.path  = new Path();

                    if (draw){
                        pageImage.line = new PDFimage.Stroke(Color.argb(255,0,0,128), 8, pageImage.path, false);

                    }else if (highlight){
                        pageImage.line = new PDFimage.Stroke(Color.YELLOW, 30, pageImage.path, false);
                    }else if (erase){
                        pageImage.erase = true;
                        pageImage.line = new PDFimage.Stroke(Color.TRANSPARENT, 30, pageImage.path, true);
                    }

                    pageImage.path.moveTo(X, Y);

                    break;
                case MotionEvent.ACTION_MOVE:
                    pageImage.path.lineTo(X, Y);
                    Log.d(LOGNAME, "Action move");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(LOGNAME, "Action up");
                    pageImage.path.lineTo(X, Y);
                    pageImage.paths.add(pageImage.line);
                    break;
            }
        }else{
            mScaleGestureDetector.onTouchEvent(event);
        }
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            pageImage.setScaleX(mScaleFactor);
            pageImage.setScaleY(mScaleFactor);
            Log.d(LOGNAME, "Scaling");
            return true;
        }
    }



}


