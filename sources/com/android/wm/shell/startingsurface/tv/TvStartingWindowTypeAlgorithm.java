package com.android.wm.shell.startingsurface.tv;

import android.window.StartingWindowInfo;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
/* loaded from: classes.dex */
public final class TvStartingWindowTypeAlgorithm implements StartingWindowTypeAlgorithm {
    @Override // com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm
    public final int getSuggestedWindowType(StartingWindowInfo startingWindowInfo) {
        return 3;
    }
}
