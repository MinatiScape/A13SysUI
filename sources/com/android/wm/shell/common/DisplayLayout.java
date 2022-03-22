package com.android.wm.shell.common;

import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.Insets;
import android.graphics.Rect;
import android.util.RotationUtils;
import android.util.Size;
import android.view.DisplayCutout;
import android.view.InsetsSource;
import android.view.InsetsState;
import com.android.internal.policy.SystemBarUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DisplayLayout {
    public boolean mAllowSeamlessRotationDespiteNavBarMoving;
    public DisplayCutout mCutout;
    public int mDensityDpi;
    public boolean mHasNavigationBar;
    public boolean mHasStatusBar;
    public int mHeight;
    public InsetsState mInsetsState;
    public int mNavBarFrameHeight;
    public boolean mNavigationBarCanMove;
    public final Rect mNonDecorInsets;
    public boolean mReverseDefaultRotation;
    public int mRotation;
    public final Rect mStableInsets;
    public int mUiMode;
    public int mWidth;

    public DisplayLayout() {
        this.mNonDecorInsets = new Rect();
        this.mStableInsets = new Rect();
        this.mHasNavigationBar = false;
        this.mHasStatusBar = false;
        this.mNavBarFrameHeight = 0;
        this.mAllowSeamlessRotationDespiteNavBarMoving = false;
        this.mNavigationBarCanMove = false;
        this.mReverseDefaultRotation = false;
        this.mInsetsState = new InsetsState();
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DisplayLayout)) {
            return false;
        }
        DisplayLayout displayLayout = (DisplayLayout) obj;
        return this.mUiMode == displayLayout.mUiMode && this.mWidth == displayLayout.mWidth && this.mHeight == displayLayout.mHeight && Objects.equals(this.mCutout, displayLayout.mCutout) && this.mRotation == displayLayout.mRotation && this.mDensityDpi == displayLayout.mDensityDpi && Objects.equals(this.mNonDecorInsets, displayLayout.mNonDecorInsets) && Objects.equals(this.mStableInsets, displayLayout.mStableInsets) && this.mHasNavigationBar == displayLayout.mHasNavigationBar && this.mHasStatusBar == displayLayout.mHasStatusBar && this.mAllowSeamlessRotationDespiteNavBarMoving == displayLayout.mAllowSeamlessRotationDespiteNavBarMoving && this.mNavigationBarCanMove == displayLayout.mNavigationBarCanMove && this.mReverseDefaultRotation == displayLayout.mReverseDefaultRotation && this.mNavBarFrameHeight == displayLayout.mNavBarFrameHeight && Objects.equals(this.mInsetsState, displayLayout.mInsetsState);
    }

    public static DisplayCutout computeSafeInsets(DisplayCutout displayCutout, int i, int i2) {
        if (displayCutout == DisplayCutout.NO_CUTOUT) {
            return null;
        }
        Size size = new Size(i, i2);
        if (size.getWidth() != size.getHeight()) {
            return displayCutout.replaceSafeInsets(new Rect(Math.max(displayCutout.getWaterfallInsets().left, findCutoutInsetForSide(size, displayCutout.getBoundingRectLeft(), 3)), Math.max(displayCutout.getWaterfallInsets().top, findCutoutInsetForSide(size, displayCutout.getBoundingRectTop(), 48)), Math.max(displayCutout.getWaterfallInsets().right, findCutoutInsetForSide(size, displayCutout.getBoundingRectRight(), 5)), Math.max(displayCutout.getWaterfallInsets().bottom, findCutoutInsetForSide(size, displayCutout.getBoundingRectBottom(), 80))));
        }
        throw new UnsupportedOperationException("not implemented: display=" + size + " cutout=" + displayCutout);
    }

    public final float density() {
        return this.mDensityDpi * 0.00625f;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(this.mUiMode), Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight), this.mCutout, Integer.valueOf(this.mRotation), Integer.valueOf(this.mDensityDpi), this.mNonDecorInsets, this.mStableInsets, Boolean.valueOf(this.mHasNavigationBar), Boolean.valueOf(this.mHasStatusBar), Integer.valueOf(this.mNavBarFrameHeight), Boolean.valueOf(this.mAllowSeamlessRotationDespiteNavBarMoving), Boolean.valueOf(this.mNavigationBarCanMove), Boolean.valueOf(this.mReverseDefaultRotation), this.mInsetsState);
    }

    public final boolean isLandscape() {
        if (this.mWidth > this.mHeight) {
            return true;
        }
        return false;
    }

    public void recalcInsets(Resources resources) {
        int i;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        int i2;
        int i3;
        int i4;
        int i5 = this.mRotation;
        int i6 = this.mWidth;
        int i7 = this.mHeight;
        DisplayCutout displayCutout = this.mCutout;
        InsetsState insetsState = this.mInsetsState;
        int i8 = this.mUiMode;
        Rect rect = this.mNonDecorInsets;
        boolean z6 = this.mHasNavigationBar;
        rect.setEmpty();
        boolean z7 = false;
        if (z6) {
            InsetsSource source = insetsState.getSource(21);
            if (source == null || !source.isVisible()) {
                z = false;
            } else {
                z = true;
            }
            if (i6 == i7 || !resources.getBoolean(17891701)) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (!z2 || i6 <= i7) {
                z3 = true;
            } else if (i5 == 1) {
                z3 = true;
            } else {
                z3 = true;
            }
            if (i6 > i7) {
                z4 = true;
            } else {
                z4 = false;
            }
            if ((i8 & 15) == 3) {
                z5 = true;
            } else {
                z5 = false;
            }
            if (z5) {
                if (z3) {
                    if (z4) {
                        i4 = 17105367;
                    } else {
                        i4 = 17105365;
                    }
                    i2 = resources.getDimensionPixelSize(i4);
                } else {
                    i2 = resources.getDimensionPixelSize(17105370);
                }
            } else if (z3) {
                if (z4) {
                    i3 = 17105366;
                } else {
                    i3 = 17105364;
                }
                i2 = resources.getDimensionPixelSize(i3);
            } else {
                i2 = resources.getDimensionPixelSize(17105369);
            }
            if (z3) {
                if (z) {
                    i2 = Math.max(i2, source.getFrame().height());
                }
                rect.bottom = i2;
            } else if (z3) {
                if (z) {
                    i2 = Math.max(i2, source.getFrame().width());
                }
                rect.right = i2;
            } else if (z3) {
                if (z) {
                    i2 = Math.max(i2, source.getFrame().width());
                }
                rect.left = i2;
            }
        }
        if (displayCutout != null) {
            rect.left = displayCutout.getSafeInsetLeft() + rect.left;
            rect.top = displayCutout.getSafeInsetTop() + rect.top;
            rect.right = displayCutout.getSafeInsetRight() + rect.right;
            rect.bottom = displayCutout.getSafeInsetBottom() + rect.bottom;
        }
        this.mStableInsets.set(this.mNonDecorInsets);
        boolean z8 = this.mHasStatusBar;
        if (z8) {
            Rect rect2 = this.mStableInsets;
            DisplayCutout displayCutout2 = this.mCutout;
            if (z8) {
                rect2.top = Math.max(rect2.top, SystemBarUtils.getStatusBarHeight(resources, displayCutout2));
            }
        }
        if (this.mWidth > this.mHeight) {
            z7 = true;
        }
        if (z7) {
            i = 17105361;
        } else {
            i = 17105360;
        }
        this.mNavBarFrameHeight = resources.getDimensionPixelSize(i);
    }

    public final void rotateTo(Resources resources, int i) {
        boolean z;
        DisplayCutout displayCutout;
        boolean z2;
        int i2;
        int i3 = ((i - this.mRotation) + 4) % 4;
        if (i3 % 2 != 0) {
            z = true;
        } else {
            z = false;
        }
        int i4 = this.mWidth;
        int i5 = this.mHeight;
        this.mRotation = i;
        if (z) {
            this.mWidth = i5;
            this.mHeight = i4;
        }
        DisplayCutout displayCutout2 = this.mCutout;
        if (displayCutout2 != null && !displayCutout2.isEmpty()) {
            DisplayCutout displayCutout3 = this.mCutout;
            if (displayCutout3 == null || displayCutout3 == DisplayCutout.NO_CUTOUT) {
                displayCutout = null;
            } else if (i3 == 0) {
                displayCutout = computeSafeInsets(displayCutout3, i4, i5);
            } else {
                Insets rotateInsets = RotationUtils.rotateInsets(displayCutout3.getWaterfallInsets(), i3);
                if (i3 == 1 || i3 == 3) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                Rect[] boundingRectsAll = displayCutout3.getBoundingRectsAll();
                Rect[] rectArr = new Rect[boundingRectsAll.length];
                Rect rect = new Rect(0, 0, i4, i5);
                for (int i6 = 0; i6 < boundingRectsAll.length; i6++) {
                    Rect rect2 = new Rect(boundingRectsAll[i6]);
                    if (!rect2.isEmpty()) {
                        RotationUtils.rotateBounds(rect2, rect, i3);
                    }
                    int i7 = i6 - i3;
                    if (i7 < 0) {
                        i7 += 4;
                    }
                    rectArr[i7] = rect2;
                }
                DisplayCutout.CutoutPathParserInfo cutoutPathParserInfo = displayCutout3.getCutoutPathParserInfo();
                DisplayCutout constructDisplayCutout = DisplayCutout.constructDisplayCutout(rectArr, rotateInsets, new DisplayCutout.CutoutPathParserInfo(cutoutPathParserInfo.getDisplayWidth(), cutoutPathParserInfo.getDisplayHeight(), cutoutPathParserInfo.getDensity(), cutoutPathParserInfo.getCutoutSpec(), i3, cutoutPathParserInfo.getScale()));
                if (z2) {
                    i2 = i5;
                } else {
                    i2 = i4;
                }
                if (!z2) {
                    i4 = i5;
                }
                displayCutout = computeSafeInsets(constructDisplayCutout, i2, i4);
            }
            this.mCutout = displayCutout;
        }
        recalcInsets(resources);
    }

    public final void set(DisplayLayout displayLayout) {
        this.mUiMode = displayLayout.mUiMode;
        this.mWidth = displayLayout.mWidth;
        this.mHeight = displayLayout.mHeight;
        this.mCutout = displayLayout.mCutout;
        this.mRotation = displayLayout.mRotation;
        this.mDensityDpi = displayLayout.mDensityDpi;
        this.mHasNavigationBar = displayLayout.mHasNavigationBar;
        this.mHasStatusBar = displayLayout.mHasStatusBar;
        this.mAllowSeamlessRotationDespiteNavBarMoving = displayLayout.mAllowSeamlessRotationDespiteNavBarMoving;
        this.mNavigationBarCanMove = displayLayout.mNavigationBarCanMove;
        this.mReverseDefaultRotation = displayLayout.mReverseDefaultRotation;
        this.mNavBarFrameHeight = displayLayout.mNavBarFrameHeight;
        this.mNonDecorInsets.set(displayLayout.mNonDecorInsets);
        this.mStableInsets.set(displayLayout.mStableInsets);
        this.mInsetsState.set(displayLayout.mInsetsState, true);
    }

    public static int findCutoutInsetForSide(Size size, Rect rect, int i) {
        if (rect.isEmpty()) {
            return 0;
        }
        if (i == 3) {
            return Math.max(0, rect.right);
        }
        if (i == 5) {
            return Math.max(0, size.getWidth() - rect.left);
        }
        if (i == 48) {
            return Math.max(0, rect.bottom);
        }
        if (i == 80) {
            return Math.max(0, size.getHeight() - rect.top);
        }
        throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("unknown gravity: ", i));
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0081, code lost:
        if (r4 != false) goto L_0x0084;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public DisplayLayout(android.content.Context r7, android.view.Display r8) {
        /*
            r6 = this;
            r6.<init>()
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>()
            r6.mNonDecorInsets = r0
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>()
            r6.mStableInsets = r0
            r0 = 0
            r6.mHasNavigationBar = r0
            r6.mHasStatusBar = r0
            r6.mNavBarFrameHeight = r0
            r6.mAllowSeamlessRotationDespiteNavBarMoving = r0
            r6.mNavigationBarCanMove = r0
            r6.mReverseDefaultRotation = r0
            android.view.InsetsState r1 = new android.view.InsetsState
            r1.<init>()
            r6.mInsetsState = r1
            int r1 = r8.getDisplayId()
            android.view.DisplayInfo r2 = new android.view.DisplayInfo
            r2.<init>()
            r8.getDisplayInfo(r2)
            android.content.res.Resources r8 = r7.getResources()
            r3 = 1
            if (r1 != 0) goto L_0x005c
            java.lang.String r4 = "qemu.hw.mainkeys"
            java.lang.String r4 = android.os.SystemProperties.get(r4)
            java.lang.String r5 = "1"
            boolean r5 = r5.equals(r4)
            if (r5 == 0) goto L_0x0047
            goto L_0x0084
        L_0x0047:
            java.lang.String r5 = "0"
            boolean r4 = r5.equals(r4)
            if (r4 == 0) goto L_0x0050
            goto L_0x0086
        L_0x0050:
            android.content.res.Resources r7 = r7.getResources()
            r4 = 17891738(0x111019a, float:2.6633443E-38)
            boolean r7 = r7.getBoolean(r4)
            goto L_0x0087
        L_0x005c:
            int r4 = r2.type
            r5 = 5
            if (r4 != r5) goto L_0x0069
            int r4 = r2.ownerUid
            r5 = 1000(0x3e8, float:1.401E-42)
            if (r4 == r5) goto L_0x0069
            r4 = r3
            goto L_0x006a
        L_0x0069:
            r4 = r0
        L_0x006a:
            android.content.ContentResolver r7 = r7.getContentResolver()
            java.lang.String r5 = "force_desktop_mode_on_external_displays"
            int r7 = android.provider.Settings.Global.getInt(r7, r5, r0)
            if (r7 == 0) goto L_0x0078
            r7 = r3
            goto L_0x0079
        L_0x0078:
            r7 = r0
        L_0x0079:
            int r5 = r2.flags
            r5 = r5 & 64
            if (r5 != 0) goto L_0x0086
            if (r7 == 0) goto L_0x0084
            if (r4 != 0) goto L_0x0084
            goto L_0x0086
        L_0x0084:
            r7 = r0
            goto L_0x0087
        L_0x0086:
            r7 = r3
        L_0x0087:
            if (r1 != 0) goto L_0x008a
            r0 = r3
        L_0x008a:
            android.content.res.Configuration r1 = r8.getConfiguration()
            int r1 = r1.uiMode
            r6.mUiMode = r1
            int r1 = r2.logicalWidth
            r6.mWidth = r1
            int r1 = r2.logicalHeight
            r6.mHeight = r1
            int r1 = r2.rotation
            r6.mRotation = r1
            android.view.DisplayCutout r1 = r2.displayCutout
            r6.mCutout = r1
            int r1 = r2.logicalDensityDpi
            r6.mDensityDpi = r1
            r6.mHasNavigationBar = r7
            r6.mHasStatusBar = r0
            r7 = 17891349(0x1110015, float:2.6632353E-38)
            boolean r7 = r8.getBoolean(r7)
            r6.mAllowSeamlessRotationDespiteNavBarMoving = r7
            r7 = 17891701(0x1110175, float:2.663334E-38)
            boolean r7 = r8.getBoolean(r7)
            r6.mNavigationBarCanMove = r7
            r7 = 17891724(0x111018c, float:2.6633404E-38)
            boolean r7 = r8.getBoolean(r7)
            r6.mReverseDefaultRotation = r7
            r6.recalcInsets(r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.common.DisplayLayout.<init>(android.content.Context, android.view.Display):void");
    }

    public DisplayLayout(DisplayLayout displayLayout) {
        this.mNonDecorInsets = new Rect();
        this.mStableInsets = new Rect();
        this.mHasNavigationBar = false;
        this.mHasStatusBar = false;
        this.mNavBarFrameHeight = 0;
        this.mAllowSeamlessRotationDespiteNavBarMoving = false;
        this.mNavigationBarCanMove = false;
        this.mReverseDefaultRotation = false;
        this.mInsetsState = new InsetsState();
        set(displayLayout);
    }
}
