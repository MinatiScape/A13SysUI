package com.google.android.setupdesign.view;

import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;
/* loaded from: classes.dex */
public interface TouchableMovementMethod {

    /* loaded from: classes.dex */
    public static class TouchableLinkMovementMethod extends LinkMovementMethod implements TouchableMovementMethod {
        public MotionEvent lastEvent;
        public boolean lastEventResult = false;

        @Override // android.text.method.LinkMovementMethod, android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod, android.text.method.MovementMethod
        public final boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
            boolean z;
            this.lastEvent = motionEvent;
            boolean onTouchEvent = super.onTouchEvent(textView, spannable, motionEvent);
            if (motionEvent.getAction() == 0) {
                if (Selection.getSelectionStart(spannable) != -1) {
                    z = true;
                } else {
                    z = false;
                }
                this.lastEventResult = z;
            } else {
                this.lastEventResult = onTouchEvent;
            }
            return onTouchEvent;
        }

        @Override // com.google.android.setupdesign.view.TouchableMovementMethod
        public final MotionEvent getLastTouchEvent() {
            return this.lastEvent;
        }

        @Override // com.google.android.setupdesign.view.TouchableMovementMethod
        public final boolean isLastTouchEventHandled() {
            return this.lastEventResult;
        }
    }

    MotionEvent getLastTouchEvent();

    boolean isLastTouchEventHandled();
}
