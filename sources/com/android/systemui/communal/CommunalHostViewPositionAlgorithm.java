package com.android.systemui.communal;

import android.util.Log;
/* loaded from: classes.dex */
public final class CommunalHostViewPositionAlgorithm {
    public static final boolean DEBUG = Log.isLoggable("CommunalPositionAlg", 3);
    public int mCommunalHeight;
    public float mPanelExpansion;

    /* loaded from: classes.dex */
    public static class Result {
        public int communalY;
    }
}
