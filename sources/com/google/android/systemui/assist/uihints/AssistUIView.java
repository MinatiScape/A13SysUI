package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public class AssistUIView extends FrameLayout {
    public TouchOutsideHandler mTouchOutside;

    public AssistUIView(Context context) {
        this(context, null);
    }

    public AssistUIView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AssistUIView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        TouchOutsideHandler touchOutsideHandler;
        if (motionEvent.getAction() != 4 || (touchOutsideHandler = this.mTouchOutside) == null) {
            return super.dispatchTouchEvent(motionEvent);
        }
        Objects.requireNonNull(touchOutsideHandler);
        PendingIntent pendingIntent = touchOutsideHandler.mTouchOutside;
        if (pendingIntent == null) {
            return false;
        }
        try {
            pendingIntent.send();
            return false;
        } catch (PendingIntent.CanceledException unused) {
            Log.w("TouchOutsideHandler", "Touch outside PendingIntent canceled");
            return false;
        }
    }

    public AssistUIView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setClipChildren(false);
    }
}
