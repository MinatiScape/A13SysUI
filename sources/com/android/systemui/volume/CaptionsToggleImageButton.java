package com.android.systemui.volume;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.android.keyguard.AlphaOptimizedImageButton;
import java.util.Objects;
/* loaded from: classes.dex */
public class CaptionsToggleImageButton extends AlphaOptimizedImageButton {
    public static final int[] OPTED_OUT_STATE = {2130969541};
    public ConfirmedTapListener mConfirmedTapListener;
    public GestureDetector mGestureDetector;
    public boolean mCaptionsEnabled = false;
    public boolean mOptedOut = false;
    public AnonymousClass1 mGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: com.android.systemui.volume.CaptionsToggleImageButton.1
        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public final boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            CaptionsToggleImageButton captionsToggleImageButton = CaptionsToggleImageButton.this;
            int[] iArr = CaptionsToggleImageButton.OPTED_OUT_STATE;
            return captionsToggleImageButton.tryToSendTapConfirmedEvent();
        }
    };

    /* loaded from: classes.dex */
    public interface ConfirmedTapListener {
    }

    @Override // android.widget.ImageView, android.view.View
    public final int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
        if (this.mOptedOut) {
            View.mergeDrawableStates(onCreateDrawableState, OPTED_OUT_STATE);
        }
        return onCreateDrawableState;
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        GestureDetector gestureDetector = this.mGestureDetector;
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(motionEvent);
        }
        return super.onTouchEvent(motionEvent);
    }

    public final boolean tryToSendTapConfirmedEvent() {
        ConfirmedTapListener confirmedTapListener = this.mConfirmedTapListener;
        if (confirmedTapListener == null) {
            return false;
        }
        VolumeDialogImpl$$ExternalSyntheticLambda9 volumeDialogImpl$$ExternalSyntheticLambda9 = (VolumeDialogImpl$$ExternalSyntheticLambda9) confirmedTapListener;
        Objects.requireNonNull(volumeDialogImpl$$ExternalSyntheticLambda9);
        VolumeDialogImpl volumeDialogImpl = (VolumeDialogImpl) volumeDialogImpl$$ExternalSyntheticLambda9.f$0;
        Objects.requireNonNull(volumeDialogImpl);
        volumeDialogImpl.mController.setCaptionsEnabled(!volumeDialogImpl.mController.areCaptionsEnabled());
        volumeDialogImpl.updateCaptionsIcon();
        Events.writeEvent(21, new Object[0]);
        return true;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.volume.CaptionsToggleImageButton$1] */
    public CaptionsToggleImageButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setContentDescription(getContext().getString(2131953504));
    }
}
