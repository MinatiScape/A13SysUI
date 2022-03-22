package com.android.launcher3.icons;

import android.util.SparseArray;
/* loaded from: classes.dex */
public final class ColorExtractor {
    public final float[] mTmpHsv = new float[3];
    public final float[] mTmpHueScoreHistogram = new float[360];
    public final int[] mTmpPixels = new int[20];
    public final SparseArray<Float> mTmpRgbScores = new SparseArray<>();
}
