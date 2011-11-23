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
import fi.hiit.serenghetto.dto.Barcode;


/* from: http://stackoverflow.com/questions/5722490/android-maps-circle-overlay-dynamically-change-radius */
public class MapCircleOverlay extends Overlay
{
    private Barcode barcode;
    private GeoPoint geoPoint;
    private Paint paint1;

    public MapCircleOverlay(Barcode barcode, int R, int G, int B) {
        this.barcode = barcode;
        this.geoPoint = barcode.getGeoPoint();

        paint1 = new Paint();
        paint1.setARGB(0x20, R, G, B);  
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        GeoPoint gp = barcode.getGeoPoint();
        if (gp != null) {
            Point p = mapView.getProjection().toPixels(gp, null);
            //float radius = (float) Math.pow(2, mapView.getZoomLevel() - 10);
            float radius = mapView.getProjection().metersToEquatorPixels((float)barcode.getScore());

            //[FIXME: is this the correct min radius threshold?]
            if (radius < canvas.getHeight()/25){
                radius = canvas.getHeight()/25;
            }

            canvas.drawCircle(p.x, p.y, radius, paint1);
        }
    }
}

