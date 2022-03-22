package com.android.wm.shell.hidedisplaycutout;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Insets;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.Log;
import android.util.RotationUtils;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.SurfaceControl;
import android.window.DisplayAreaInfo;
import android.window.DisplayAreaOrganizer;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.policy.SystemBarUtils;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import java.util.Objects;
/* loaded from: classes.dex */
public final class HideDisplayCutoutOrganizer extends DisplayAreaOrganizer {
    public final Context mContext;
    public Insets mCurrentCutoutInsets;
    public Insets mDefaultCutoutInsets;
    public final DisplayController mDisplayController;
    public int mOffsetX;
    public int mOffsetY;
    public int mRotation;
    public int mStatusBarHeight;
    public ArrayMap<WindowContainerToken, SurfaceControl> mDisplayAreaMap = new ArrayMap<>();
    public final Rect mDefaultDisplayBounds = new Rect();
    public final Rect mCurrentDisplayBounds = new Rect();
    public final AnonymousClass1 mListener = new DisplayController.OnDisplaysChangedListener() { // from class: com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutOrganizer.1
        @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
        public final void onDisplayConfigurationChanged(int i, Configuration configuration) {
            if (i == 0) {
                boolean z = false;
                DisplayLayout displayLayout = HideDisplayCutoutOrganizer.this.mDisplayController.getDisplayLayout(0);
                if (displayLayout != null) {
                    HideDisplayCutoutOrganizer hideDisplayCutoutOrganizer = HideDisplayCutoutOrganizer.this;
                    int i2 = hideDisplayCutoutOrganizer.mRotation;
                    int i3 = displayLayout.mRotation;
                    if (i2 != i3) {
                        z = true;
                    }
                    hideDisplayCutoutOrganizer.mRotation = i3;
                    if (z || hideDisplayCutoutOrganizer.isDisplayBoundsChanged()) {
                        HideDisplayCutoutOrganizer.this.updateBoundsAndOffsets(true);
                        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                        HideDisplayCutoutOrganizer hideDisplayCutoutOrganizer2 = HideDisplayCutoutOrganizer.this;
                        Objects.requireNonNull(hideDisplayCutoutOrganizer2);
                        synchronized (hideDisplayCutoutOrganizer2) {
                            hideDisplayCutoutOrganizer2.mDisplayAreaMap.forEach(new HideDisplayCutoutOrganizer$$ExternalSyntheticLambda0(hideDisplayCutoutOrganizer2, windowContainerTransaction, transaction));
                        }
                        HideDisplayCutoutOrganizer.this.applyTransaction(windowContainerTransaction, transaction);
                    }
                }
            }
        }
    };

    public boolean addDisplayAreaInfoAndLeashToMap(DisplayAreaInfo displayAreaInfo, SurfaceControl surfaceControl) {
        synchronized (this) {
            if (displayAreaInfo.displayId != 0) {
                return false;
            }
            if (this.mDisplayAreaMap.containsKey(displayAreaInfo.token)) {
                Log.w("HideDisplayCutoutOrganizer", "Already appeared token: " + displayAreaInfo.token);
                return false;
            }
            this.mDisplayAreaMap.put(displayAreaInfo.token, surfaceControl);
            return true;
        }
    }

    public final void onDisplayAreaVanished(DisplayAreaInfo displayAreaInfo) {
        synchronized (this) {
            if (!this.mDisplayAreaMap.containsKey(displayAreaInfo.token)) {
                Log.w("HideDisplayCutoutOrganizer", "Unrecognized token: " + displayAreaInfo.token);
                return;
            }
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
            WindowContainerToken windowContainerToken = displayAreaInfo.token;
            applyBoundsAndOffsets(windowContainerToken, this.mDisplayAreaMap.get(windowContainerToken), windowContainerTransaction, transaction);
            applyTransaction(windowContainerTransaction, transaction);
            this.mDisplayAreaMap.remove(displayAreaInfo.token);
        }
    }

    public void updateBoundsAndOffsets(boolean z) {
        boolean z2 = false;
        if (!z) {
            this.mCurrentDisplayBounds.setEmpty();
            this.mOffsetX = 0;
            this.mOffsetY = 0;
            return;
        }
        if (isDisplayBoundsChanged()) {
            this.mDefaultDisplayBounds.set(getDisplayBoundsOfNaturalOrientation());
            this.mDefaultCutoutInsets = getDisplayCutoutInsetsOfNaturalOrientation();
            this.mDefaultDisplayBounds.width();
            this.mDefaultDisplayBounds.height();
        }
        this.mCurrentDisplayBounds.set(this.mDefaultDisplayBounds);
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mCurrentCutoutInsets = RotationUtils.rotateInsets(this.mDefaultCutoutInsets, this.mRotation);
        int i = this.mRotation;
        if (i == 1 || i == 3) {
            z2 = true;
        }
        if (z2) {
            Rect rect = this.mCurrentDisplayBounds;
            rect.set(rect.top, rect.left, rect.bottom, rect.right);
        }
        this.mCurrentDisplayBounds.inset(this.mCurrentCutoutInsets);
        int statusBarHeight = getStatusBarHeight();
        this.mStatusBarHeight = statusBarHeight;
        int i2 = this.mCurrentCutoutInsets.top;
        if (i2 != 0) {
            this.mCurrentDisplayBounds.top = Math.max(statusBarHeight, i2);
        }
        Rect rect2 = this.mCurrentDisplayBounds;
        this.mOffsetX = rect2.left;
        this.mOffsetY = rect2.top;
    }

    public void applyBoundsAndOffsets(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
        windowContainerTransaction.setBounds(windowContainerToken, this.mCurrentDisplayBounds);
        transaction.setPosition(surfaceControl, this.mOffsetX, this.mOffsetY);
        transaction.setWindowCrop(surfaceControl, this.mCurrentDisplayBounds.width(), this.mCurrentDisplayBounds.height());
    }

    public Rect getDisplayBoundsOfNaturalOrientation() {
        int i;
        int i2;
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(0);
        if (displayLayout == null) {
            return new Rect();
        }
        int i3 = this.mRotation;
        boolean z = true;
        if (!(i3 == 1 || i3 == 3)) {
            z = false;
        }
        if (z) {
            i = displayLayout.mHeight;
        } else {
            i = displayLayout.mWidth;
        }
        if (z) {
            i2 = displayLayout.mWidth;
        } else {
            i2 = displayLayout.mHeight;
        }
        return new Rect(0, 0, i, i2);
    }

    public Insets getDisplayCutoutInsetsOfNaturalOrientation() {
        Insets insets;
        Display display = this.mDisplayController.getDisplay(0);
        if (display == null) {
            return Insets.NONE;
        }
        DisplayCutout cutout = display.getCutout();
        if (cutout != null) {
            insets = Insets.of(cutout.getSafeInsets());
        } else {
            insets = Insets.NONE;
        }
        int i = this.mRotation;
        if (i != 0) {
            return RotationUtils.rotateInsets(insets, 4 - i);
        }
        return insets;
    }

    public int getStatusBarHeight() {
        return SystemBarUtils.getStatusBarHeight(this.mContext);
    }

    public final boolean isDisplayBoundsChanged() {
        boolean z;
        int i;
        int i2;
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(0);
        if (displayLayout == null) {
            return false;
        }
        int i3 = this.mRotation;
        if (i3 == 1 || i3 == 3) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            i = displayLayout.mHeight;
        } else {
            i = displayLayout.mWidth;
        }
        if (z) {
            i2 = displayLayout.mWidth;
        } else {
            i2 = displayLayout.mHeight;
        }
        if (!this.mDefaultDisplayBounds.isEmpty() && this.mDefaultDisplayBounds.width() == i && this.mDefaultDisplayBounds.height() == i2) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r3v4, types: [com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutOrganizer$1] */
    public HideDisplayCutoutOrganizer(Context context, DisplayController displayController, ShellExecutor shellExecutor) {
        super(shellExecutor);
        this.mContext = context;
        this.mDisplayController = displayController;
    }

    public void applyTransaction(WindowContainerTransaction windowContainerTransaction, SurfaceControl.Transaction transaction) {
        applyTransaction(windowContainerTransaction);
        transaction.apply();
    }

    public final void onDisplayAreaAppeared(DisplayAreaInfo displayAreaInfo, SurfaceControl surfaceControl) {
        if (addDisplayAreaInfoAndLeashToMap(displayAreaInfo, surfaceControl)) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
            applyBoundsAndOffsets(displayAreaInfo.token, surfaceControl, windowContainerTransaction, transaction);
            applyTransaction(windowContainerTransaction, transaction);
        }
    }
}
