package com.android.systemui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.android.systemui.plugins.DarkIconDispatcher;
import java.util.ArrayList;
/* compiled from: DarkReceiverImpl.kt */
/* loaded from: classes.dex */
public final class DarkReceiverImpl extends View implements DarkIconDispatcher.DarkReceiver {
    public final DualToneHandler dualToneHandler;

    public DarkReceiverImpl(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0, 0);
        this.dualToneHandler = new DualToneHandler(context);
        onDarkChanged(new ArrayList<>(), 1.0f, -1);
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
    public final void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        if (!DarkIconDispatcher.isInAreas(arrayList, this)) {
            f = 0.0f;
        }
        setBackgroundColor(this.dualToneHandler.getSingleColor(f));
    }
}
