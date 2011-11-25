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
    private static final int MIN_RADIUS_THRESHOLD = 25;

    private GeoPoint geoPoint;
    private double score;
    private double accuracy;

    private Paint paint1;
    private Paint paint2;

    public MapCircleOverlay(GeoPoint geoPoint, double score, int A, int R, int G, int B) {
        _init(geoPoint, score, 0, A, R, G, B);
    }

    public MapCircleOverlay(Barcode barcode, int A, int R, int G, int B) {
        _init(barcode.getGeoPoint(), barcode.getScore(), barcode.getAccuracy(), A, R, G, B);
    }

    private void _init(GeoPoint geoPoint, double score, double accuracy, int A, int R, int G, int B) {
        this.geoPoint = geoPoint;
        this.score = score;
        this.accuracy = accuracy;

        paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setDither(false);
        paint1.setARGB(A, R, G, B);

        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setDither(false);
        paint2.setARGB(0xDD, 0xDD, 0xDD, 0xDD);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        if (geoPoint != null) {
            Point p = mapView.getProjection().toPixels(geoPoint, null);
            //float radius = (float) Math.pow(2, mapView.getZoomLevel() - 10);
            float radius = mapView.getProjection().metersToEquatorPixels((float)score);
            float accuracyIndicator = mapView.getProjection().metersToEquatorPixels((float)accuracy);

            //[FIXME: is this the correct min radius threshold?]
            if (radius < canvas.getHeight()/MIN_RADIUS_THRESHOLD){
                radius = canvas.getHeight()/MIN_RADIUS_THRESHOLD;
            }

            //canvas.drawCircle(p.x, p.y, accuracyIndicator, paint2);
            canvas.drawCircle(p.x, p.y, radius, paint1);
        }
    }
}

