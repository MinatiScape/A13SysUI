package com.airbnb.lottie.model.content;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PointF;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.airbnb.lottie.model.CubicCurveData;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class ShapeData {
    public boolean closed;
    public final ArrayList curves;
    public PointF initialPoint;

    public ShapeData(PointF pointF, boolean z, List<CubicCurveData> list) {
        this.initialPoint = pointF;
        this.closed = z;
        this.curves = new ArrayList(list);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ShapeData{numCurves=");
        m.append(this.curves.size());
        m.append("closed=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.closed, '}');
    }

    public ShapeData() {
        this.curves = new ArrayList();
    }
}
