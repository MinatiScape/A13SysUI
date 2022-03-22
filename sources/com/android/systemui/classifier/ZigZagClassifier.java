package com.android.systemui.classifier;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Point;
import android.provider.DeviceConfig;
import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ZigZagClassifier extends FalsingClassifier {
    public float mLastDevianceX;
    public float mLastDevianceY;
    public float mLastMaxXDeviance;
    public float mLastMaxYDeviance;
    public final float mMaxXPrimaryDeviance = DeviceConfig.getFloat("systemui", "brightline_falsing_zigzag_x_primary_deviance", 0.05f);
    public final float mMaxYPrimaryDeviance = DeviceConfig.getFloat("systemui", "brightline_falsing_zigzag_y_primary_deviance", 0.15f);
    public final float mMaxXSecondaryDeviance = DeviceConfig.getFloat("systemui", "brightline_falsing_zigzag_x_secondary_deviance", 0.4f);
    public final float mMaxYSecondaryDeviance = DeviceConfig.getFloat("systemui", "brightline_falsing_zigzag_y_secondary_deviance", 0.3f);

    public static ArrayList rotateMotionEvents(TimeLimitedMotionEventBuffer timeLimitedMotionEventBuffer, double d) {
        ArrayList arrayList = new ArrayList();
        double cos = Math.cos(d);
        double sin = Math.sin(d);
        MotionEvent motionEvent = (MotionEvent) timeLimitedMotionEventBuffer.get(0);
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Iterator<MotionEvent> it = timeLimitedMotionEventBuffer.iterator();
        while (it.hasNext()) {
            MotionEvent next = it.next();
            double x2 = next.getX() - x;
            double y2 = next.getY() - y;
            arrayList.add(new Point((int) (x + (sin * y2) + (cos * x2)), (int) ((y2 * cos) + ((-sin) * x2) + y)));
            it = it;
            motionEvent = motionEvent;
            x = x;
        }
        MotionEvent motionEvent2 = (MotionEvent) timeLimitedMotionEventBuffer.get(timeLimitedMotionEventBuffer.size() - 1);
        Point point = (Point) arrayList.get(0);
        Point point2 = (Point) arrayList.get(arrayList.size() - 1);
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Before: (");
        m.append(motionEvent.getX());
        m.append(",");
        m.append(motionEvent.getY());
        m.append("), (");
        m.append(motionEvent2.getX());
        m.append(",");
        m.append(motionEvent2.getY());
        m.append(")");
        BrightLineFalsingManager.logDebug(m.toString());
        BrightLineFalsingManager.logDebug("After: (" + point.x + "," + point.y + "), (" + point2.x + "," + point2.y + ")");
        return arrayList;
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    public final FalsingClassifier.Result calculateFalsingResult(int i) {
        ArrayList<Point> arrayList;
        float f;
        float f2;
        float f3;
        if (i == 10 || i == 11 || i == 14) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        if (getRecentMotionEvents().size() < 3) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        if (this.mDataProvider.isHorizontal()) {
            double atan2LastPoint = getAtan2LastPoint();
            BrightLineFalsingManager.logDebug("Rotating to horizontal by: " + atan2LastPoint);
            arrayList = rotateMotionEvents(getRecentMotionEvents(), atan2LastPoint);
        } else {
            double atan2LastPoint2 = 1.5707963267948966d - getAtan2LastPoint();
            BrightLineFalsingManager.logDebug("Rotating to vertical by: " + atan2LastPoint2);
            arrayList = rotateMotionEvents(getRecentMotionEvents(), -atan2LastPoint2);
        }
        float abs = Math.abs(((Point) arrayList.get(0)).x - ((Point) arrayList.get(arrayList.size() - 1)).x);
        float abs2 = Math.abs(((Point) arrayList.get(0)).y - ((Point) arrayList.get(arrayList.size() - 1)).y);
        BrightLineFalsingManager.logDebug("Actual: (" + abs + "," + abs2 + ")");
        float f4 = 0.0f;
        boolean z = true;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        for (Point point : arrayList) {
            if (z) {
                f6 = point.x;
                f7 = point.y;
                z = false;
            } else {
                f4 += Math.abs(point.x - f6);
                f5 += Math.abs(point.y - f7);
                f6 = point.x;
                f7 = point.y;
                BrightLineFalsingManager.logDebug("(x, y, runningAbsDx, runningAbsDy) - (" + f6 + ", " + f7 + ", " + f4 + ", " + f5 + ")");
            }
        }
        float f8 = f4 - abs;
        float f9 = f5 - abs2;
        FalsingDataProvider falsingDataProvider = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider);
        float f10 = abs / falsingDataProvider.mXdpi;
        FalsingDataProvider falsingDataProvider2 = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider2);
        float f11 = abs2 / falsingDataProvider2.mYdpi;
        float sqrt = (float) Math.sqrt((f11 * f11) + (f10 * f10));
        if (abs > abs2) {
            FalsingDataProvider falsingDataProvider3 = this.mDataProvider;
            Objects.requireNonNull(falsingDataProvider3);
            f2 = falsingDataProvider3.mXdpi * this.mMaxXPrimaryDeviance * sqrt;
            f3 = this.mMaxYSecondaryDeviance * sqrt;
            FalsingDataProvider falsingDataProvider4 = this.mDataProvider;
            Objects.requireNonNull(falsingDataProvider4);
            f = falsingDataProvider4.mYdpi;
        } else {
            FalsingDataProvider falsingDataProvider5 = this.mDataProvider;
            Objects.requireNonNull(falsingDataProvider5);
            f2 = falsingDataProvider5.mXdpi * this.mMaxXSecondaryDeviance * sqrt;
            f3 = this.mMaxYPrimaryDeviance * sqrt;
            FalsingDataProvider falsingDataProvider6 = this.mDataProvider;
            Objects.requireNonNull(falsingDataProvider6);
            f = falsingDataProvider6.mYdpi;
        }
        float f12 = f * f3;
        this.mLastDevianceX = f8;
        this.mLastDevianceY = f9;
        this.mLastMaxXDeviance = f2;
        this.mLastMaxYDeviance = f12;
        BrightLineFalsingManager.logDebug("Straightness Deviance: (" + f8 + "," + f9 + ") vs (" + f2 + "," + f12 + ")");
        if (f8 > f2 || f9 > f12) {
            return falsed(0.5d, String.format(null, "{devianceX=%f, maxDevianceX=%s, devianceY=%s, maxDevianceY=%s}", Float.valueOf(this.mLastDevianceX), Float.valueOf(this.mLastMaxXDeviance), Float.valueOf(this.mLastDevianceY), Float.valueOf(this.mLastMaxYDeviance)));
        }
        return FalsingClassifier.Result.passed(0.5d);
    }

    public final float getAtan2LastPoint() {
        FalsingDataProvider falsingDataProvider = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider);
        falsingDataProvider.recalculateData();
        MotionEvent motionEvent = falsingDataProvider.mFirstRecentMotionEvent;
        FalsingDataProvider falsingDataProvider2 = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider2);
        falsingDataProvider2.recalculateData();
        MotionEvent motionEvent2 = falsingDataProvider2.mLastMotionEvent;
        float x = motionEvent.getX();
        return (float) Math.atan2(motionEvent2.getY() - motionEvent.getY(), motionEvent2.getX() - x);
    }

    public ZigZagClassifier(FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        super(falsingDataProvider);
        Objects.requireNonNull(deviceConfigProxy);
    }
}
