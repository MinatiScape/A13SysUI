package com.android.systemui.clipboardoverlay;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.InputMonitor;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.textclassifier.TextClassifier;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.policy.PhoneWindow;
import com.android.systemui.screenshot.OverlayActionChip;
import com.android.systemui.screenshot.SwipeDismissHandler;
import com.android.systemui.screenshot.TimeoutHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ClipboardOverlayController {
    public final AccessibilityManager mAccessibilityManager;
    public final ArrayList<OverlayActionChip> mActionChips = new ArrayList<>();
    public final LinearLayout mActionContainer;
    public final View mActionContainerBackground;
    public boolean mBlockAttach;
    public AnonymousClass1 mCloseDialogsReceiver;
    public final FrameLayout mContainer;
    public final Context mContext;
    public final View mDismissButton;
    public final DisplayMetrics mDisplayMetrics;
    public final OverlayActionChip mEditChip;
    public final ImageView mImagePreview;
    public AnonymousClass3 mInputEventReceiver;
    public InputMonitor mInputMonitor;
    public Runnable mOnSessionCompleteListener;
    public final View mPreviewBorder;
    public AnonymousClass2 mScreenshotReceiver;
    public final TextClassifier mTextClassifier;
    public final TextView mTextPreview;
    public final TimeoutHandler mTimeoutHandler;
    public final DraggableConstraintLayout mView;
    public final PhoneWindow mWindow;
    public final WindowManager mWindowManager;

    public final void animateOut() {
        DraggableConstraintLayout draggableConstraintLayout = this.mView;
        Objects.requireNonNull(draggableConstraintLayout);
        SwipeDismissHandler swipeDismissHandler = draggableConstraintLayout.mSwipeDismissHandler;
        Objects.requireNonNull(swipeDismissHandler);
        swipeDismissHandler.dismiss(1.0f);
    }

    public final void resetActionChips() {
        Iterator<OverlayActionChip> it = this.mActionChips.iterator();
        while (it.hasNext()) {
            this.mActionContainer.removeView(it.next());
        }
        this.mActionChips.clear();
    }

    public final void showTextPreview(CharSequence charSequence) {
        this.mTextPreview.setVisibility(0);
        this.mImagePreview.setVisibility(8);
        this.mTextPreview.setText(charSequence);
        this.mEditChip.setVisibility(8);
    }

    /* JADX WARN: Type inference failed for: r12v10, types: [com.android.systemui.clipboardoverlay.ClipboardOverlayController$2, android.content.BroadcastReceiver] */
    /* JADX WARN: Type inference failed for: r12v9, types: [com.android.systemui.clipboardoverlay.ClipboardOverlayController$1, android.content.BroadcastReceiver] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ClipboardOverlayController(android.content.Context r12, com.android.systemui.screenshot.TimeoutHandler r13) {
        /*
            Method dump skipped, instructions count: 526
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.clipboardoverlay.ClipboardOverlayController.<init>(android.content.Context, com.android.systemui.screenshot.TimeoutHandler):void");
    }
}
