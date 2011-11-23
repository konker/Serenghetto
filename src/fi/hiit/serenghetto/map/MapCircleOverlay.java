package fi.hiit.serenghetto.map;

import android.util.Log;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import fi.hiit.serenghetto.SerenghettoApplication;


/* from: http://stackoverflow.com/questions/5722490/android-maps-circle-overlay-dynamically-change-radius */
public class MapCircleOverlay extends Overlay
{
    private GeoPoint point;
    private double score;
    private Paint paint1, paint2;

    public MapCircleOverlay(GeoPoint point, double score, int R, int G, int B) {
        this.point = point;
        this.score = score;

        paint1 = new Paint();
        paint1.setARGB(0x40, 0x00, 0x00, 0xFF);
        paint1.setStrokeWidth(2);
        paint1.setStrokeCap(Paint.Cap.ROUND);
        paint1.setAntiAlias(true);
        paint1.setDither(false);
        paint1.setStyle(Paint.Style.STROKE);

        paint2 = new Paint();
        paint2.setARGB(0x20, R, G, B);  
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {

        Point pt = mapView.getProjection().toPixels(point, null);
        float radius = (float) Math.pow(2, mapView.getZoomLevel() - 10);

        if (radius < canvas.getHeight()/25){
            radius = canvas.getHeight()/25;
        }

        canvas.drawCircle(pt.x, pt.y, radius, paint2);
        //canvas.drawCircle(pt.x, pt.y, radius, paint1);
    }
}

