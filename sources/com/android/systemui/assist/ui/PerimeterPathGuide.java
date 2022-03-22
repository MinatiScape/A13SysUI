package com.android.systemui.assist.ui;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;
import android.util.Pair;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.systemui.R$id;
import com.android.systemui.assist.ui.CornerPathRenderer;
import kotlinx.atomicfu.AtomicFU;
/* loaded from: classes.dex */
public final class PerimeterPathGuide {
    public final int mBottomCornerRadiusPx;
    public final CornerPathRenderer mCornerPathRenderer;
    public final int mDeviceHeightPx;
    public final int mDeviceWidthPx;
    public final int mEdgeInset;
    public final int mTopCornerRadiusPx;
    public final PathMeasure mScratchPathMeasure = new PathMeasure(new Path(), false);
    public int mRotation = 0;
    public RegionAttributes[] mRegions = new RegionAttributes[8];

    /* loaded from: classes.dex */
    public enum Region {
        BOTTOM,
        /* JADX INFO: Fake field, exist only in values array */
        BOTTOM_RIGHT,
        /* JADX INFO: Fake field, exist only in values array */
        RIGHT,
        /* JADX INFO: Fake field, exist only in values array */
        TOP_RIGHT,
        TOP,
        /* JADX INFO: Fake field, exist only in values array */
        TOP_LEFT,
        /* JADX INFO: Fake field, exist only in values array */
        LEFT,
        BOTTOM_LEFT
    }

    /* loaded from: classes.dex */
    public class RegionAttributes {
        public float absoluteLength;
        public float endCoordinate;
        public float normalizedLength;
        public Path path;
    }

    public final void computeRegions() {
        int i;
        int i2 = this.mDeviceWidthPx;
        int i3 = this.mDeviceHeightPx;
        int i4 = this.mRotation;
        int i5 = 0;
        if (i4 == 1) {
            i = -90;
        } else if (i4 == 2) {
            i = -180;
        } else if (i4 != 3) {
            i = 0;
        } else {
            i = -270;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(i, this.mDeviceWidthPx / 2, this.mDeviceHeightPx / 2);
        int i6 = this.mRotation;
        if (i6 == 1 || i6 == 3) {
            i3 = this.mDeviceWidthPx;
            i2 = this.mDeviceHeightPx;
            matrix.postTranslate((i2 - i3) / 2, (i3 - i2) / 2);
        }
        CornerPathRenderer.Corner rotatedCorner = getRotatedCorner(CornerPathRenderer.Corner.BOTTOM_LEFT);
        CornerPathRenderer.Corner rotatedCorner2 = getRotatedCorner(CornerPathRenderer.Corner.BOTTOM_RIGHT);
        CornerPathRenderer.Corner rotatedCorner3 = getRotatedCorner(CornerPathRenderer.Corner.TOP_LEFT);
        CornerPathRenderer.Corner rotatedCorner4 = getRotatedCorner(CornerPathRenderer.Corner.TOP_RIGHT);
        this.mRegions[7].path = this.mCornerPathRenderer.getInsetPath(rotatedCorner, this.mEdgeInset);
        this.mRegions[1].path = this.mCornerPathRenderer.getInsetPath(rotatedCorner2, this.mEdgeInset);
        this.mRegions[3].path = this.mCornerPathRenderer.getInsetPath(rotatedCorner4, this.mEdgeInset);
        this.mRegions[5].path = this.mCornerPathRenderer.getInsetPath(rotatedCorner3, this.mEdgeInset);
        this.mRegions[7].path.transform(matrix);
        this.mRegions[1].path.transform(matrix);
        this.mRegions[3].path.transform(matrix);
        this.mRegions[5].path.transform(matrix);
        Path path = new Path();
        path.moveTo(getPhysicalCornerRadius(rotatedCorner), i3 - this.mEdgeInset);
        path.lineTo(i2 - getPhysicalCornerRadius(rotatedCorner2), i3 - this.mEdgeInset);
        this.mRegions[0].path = path;
        Path path2 = new Path();
        path2.moveTo(i2 - getPhysicalCornerRadius(rotatedCorner4), this.mEdgeInset);
        path2.lineTo(getPhysicalCornerRadius(rotatedCorner3), this.mEdgeInset);
        this.mRegions[4].path = path2;
        Path path3 = new Path();
        path3.moveTo(i2 - this.mEdgeInset, i3 - getPhysicalCornerRadius(rotatedCorner2));
        path3.lineTo(i2 - this.mEdgeInset, getPhysicalCornerRadius(rotatedCorner4));
        this.mRegions[2].path = path3;
        Path path4 = new Path();
        path4.moveTo(this.mEdgeInset, getPhysicalCornerRadius(rotatedCorner3));
        path4.lineTo(this.mEdgeInset, i3 - getPhysicalCornerRadius(rotatedCorner));
        this.mRegions[6].path = path4;
        PathMeasure pathMeasure = new PathMeasure();
        float f = 0.0f;
        float f2 = 0.0f;
        int i7 = 0;
        while (true) {
            RegionAttributes[] regionAttributesArr = this.mRegions;
            if (i7 < regionAttributesArr.length) {
                pathMeasure.setPath(regionAttributesArr[i7].path, false);
                this.mRegions[i7].absoluteLength = pathMeasure.getLength();
                f2 += this.mRegions[i7].absoluteLength;
                i7++;
            }
        }
        while (true) {
            RegionAttributes[] regionAttributesArr2 = this.mRegions;
            if (i5 < regionAttributesArr2.length) {
                regionAttributesArr2[i5].normalizedLength = regionAttributesArr2[i5].absoluteLength / f2;
                f += regionAttributesArr2[i5].absoluteLength;
                regionAttributesArr2[i5].endCoordinate = f / f2;
                i5++;
            } else {
                regionAttributesArr2[regionAttributesArr2.length - 1].endCoordinate = 1.0f;
                return;
            }
        }
    }

    public final int getPhysicalCornerRadius(CornerPathRenderer.Corner corner) {
        if (corner == CornerPathRenderer.Corner.BOTTOM_LEFT || corner == CornerPathRenderer.Corner.BOTTOM_RIGHT) {
            return this.mBottomCornerRadiusPx;
        }
        return this.mTopCornerRadiusPx;
    }

    public final float getRegionCenter(Region region) {
        RegionAttributes regionAttributes = this.mRegions[region.ordinal()];
        return regionAttributes.endCoordinate - ((1.0f - AtomicFU.clamp(0.5f, 0.0f, 1.0f)) * regionAttributes.normalizedLength);
    }

    public final float getRegionWidth(Region region) {
        return this.mRegions[region.ordinal()].normalizedLength;
    }

    public final Pair<Region, Float> placePoint(float f) {
        float f2;
        Region region;
        Region region2 = Region.BOTTOM;
        if (0.0f > f || f > 1.0f) {
            f = ((f % 1.0f) + 1.0f) % 1.0f;
        }
        if (f < 0.0f || f > 1.0f) {
            f2 = ((f % 1.0f) + 1.0f) % 1.0f;
        } else {
            f2 = f;
        }
        Region[] values = Region.values();
        int length = values.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                Log.e("PerimeterPathGuide", "Fell out of getRegionForPoint");
                region = region2;
                break;
            }
            region = values[i];
            if (f2 <= this.mRegions[region.ordinal()].endCoordinate) {
                break;
            }
            i++;
        }
        if (region.equals(region2)) {
            return Pair.create(region, Float.valueOf(f / this.mRegions[region.ordinal()].normalizedLength));
        }
        return Pair.create(region, Float.valueOf((f - this.mRegions[region.ordinal() - 1].endCoordinate) / this.mRegions[region.ordinal()].normalizedLength));
    }

    public final void setRotation(int i) {
        if (i == this.mRotation) {
            return;
        }
        if (i == 0 || i == 1 || i == 2 || i == 3) {
            this.mRotation = i;
            computeRegions();
            return;
        }
        KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Invalid rotation provided: ", i, "PerimeterPathGuide");
    }

    public final void strokeRegion(Path path, Region region, float f, float f2) {
        if (f != f2) {
            this.mScratchPathMeasure.setPath(this.mRegions[region.ordinal()].path, false);
            PathMeasure pathMeasure = this.mScratchPathMeasure;
            pathMeasure.getSegment(pathMeasure.getLength() * f, this.mScratchPathMeasure.getLength() * f2, path, true);
        }
    }

    public PerimeterPathGuide(Context context, CornerPathRenderer cornerPathRenderer, int i, int i2, int i3) {
        int i4 = 0;
        this.mCornerPathRenderer = cornerPathRenderer;
        this.mDeviceWidthPx = i2;
        this.mDeviceHeightPx = i3;
        this.mTopCornerRadiusPx = R$id.getCornerRadiusTop(context);
        this.mBottomCornerRadiusPx = R$id.getCornerRadiusBottom(context);
        this.mEdgeInset = i;
        while (true) {
            RegionAttributes[] regionAttributesArr = this.mRegions;
            if (i4 < regionAttributesArr.length) {
                regionAttributesArr[i4] = new RegionAttributes();
                i4++;
            } else {
                computeRegions();
                return;
            }
        }
    }

    public final CornerPathRenderer.Corner getRotatedCorner(CornerPathRenderer.Corner corner) {
        int ordinal = corner.ordinal();
        int i = this.mRotation;
        if (i == 1) {
            ordinal += 3;
        } else if (i == 2) {
            ordinal += 2;
        } else if (i == 3) {
            ordinal++;
        }
        return CornerPathRenderer.Corner.values()[ordinal % 4];
    }

    public final void strokeSegmentInternal(Path path, float f, float f2) {
        Region[] values;
        Pair<Region, Float> placePoint = placePoint(f);
        Pair<Region, Float> placePoint2 = placePoint(f2);
        if (((Region) placePoint.first).equals(placePoint2.first)) {
            strokeRegion(path, (Region) placePoint.first, ((Float) placePoint.second).floatValue(), ((Float) placePoint2.second).floatValue());
            return;
        }
        strokeRegion(path, (Region) placePoint.first, ((Float) placePoint.second).floatValue(), 1.0f);
        boolean z = false;
        for (Region region : Region.values()) {
            if (region.equals(placePoint.first)) {
                z = true;
            } else if (!z) {
                continue;
            } else if (!region.equals(placePoint2.first)) {
                strokeRegion(path, region, 0.0f, 1.0f);
            } else {
                strokeRegion(path, region, 0.0f, ((Float) placePoint2.second).floatValue());
                return;
            }
        }
    }
}
