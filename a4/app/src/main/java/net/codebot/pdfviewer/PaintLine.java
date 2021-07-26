package net.codebot.pdfviewer;

import android.graphics.Path;

public class PaintLine {

    public int color;
    public int strokeWidth;
    public boolean erase;
    public Path path;

    public PaintLine(int color, int strokeWidth, Path path, Boolean erase){
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
        this.erase = erase;

    }

}
