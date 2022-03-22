package com.android.systemui.biometrics;

import android.graphics.Insets;
import android.graphics.Rect;
import android.hardware.biometrics.SensorLocationInternal;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.systemui.biometrics.AuthDialog;
/* loaded from: classes.dex */
public final class UdfpsDialogMeasureAdapter {
    public static final boolean DEBUG;
    public int mBottomSpacerHeight;
    public final FingerprintSensorPropertiesInternal mSensorProps;
    public final ViewGroup mView;
    public WindowManager mWindowManager;

    static {
        boolean z;
        if (Build.IS_USERDEBUG || Build.IS_ENG) {
            z = true;
        } else {
            z = false;
        }
        DEBUG = z;
    }

    @VisibleForTesting
    public static int calculateBottomSpacerHeightForLandscape(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        int i8 = ((((i + i2) + i3) + i4) - (i5 + i6)) - i7;
        if (DEBUG) {
            StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("Title height: ", i, ", Subtitle height: ", i2, ", Description height: ");
            m.append(i3);
            m.append(", Top spacer height: ");
            m.append(i4);
            m.append(", Text indicator height: ");
            m.append(i5);
            m.append(", Button bar height: ");
            m.append(i6);
            m.append(", Navbar bottom inset: ");
            m.append(i7);
            m.append(", Bottom spacer height (landscape): ");
            m.append(i8);
            Log.d("UdfpsDialogMeasurementAdapter", m.toString());
        }
        return i8;
    }

    public final int getViewHeightPx(int i) {
        View findViewById = this.mView.findViewById(i);
        if (findViewById == null || findViewById.getVisibility() == 8) {
            return 0;
        }
        return findViewById.getMeasuredHeight();
    }

    public final AuthDialog.LayoutParams onMeasureInternal(int i, int i2, AuthDialog.LayoutParams layoutParams) {
        Rect rect;
        Insets insets;
        Insets insets2;
        Rect rect2;
        int rotation = this.mView.getDisplay().getRotation();
        int i3 = 2131427579;
        int i4 = 2131428891;
        int i5 = 0;
        if (rotation == 0) {
            WindowMetrics maximumWindowMetrics = this.mWindowManager.getMaximumWindowMetrics();
            int viewHeightPx = getViewHeightPx(2131428120);
            int viewHeightPx2 = getViewHeightPx(2131427643);
            int dimensionPixelSize = this.mView.getResources().getDimensionPixelSize(2131165369);
            if (maximumWindowMetrics != null) {
                rect = maximumWindowMetrics.getBounds();
            } else {
                rect = new Rect();
            }
            int height = rect.height();
            if (maximumWindowMetrics != null) {
                insets = maximumWindowMetrics.getWindowInsets().getInsets(WindowInsets.Type.navigationBars());
            } else {
                insets = Insets.NONE;
            }
            this.mBottomSpacerHeight = calculateBottomSpacerHeightForPortrait(this.mSensorProps, height, viewHeightPx, viewHeightPx2, dimensionPixelSize, insets.bottom);
            int childCount = this.mView.getChildCount();
            int i6 = this.mSensorProps.getLocation().sensorRadius * 2;
            int i7 = 0;
            int i8 = 0;
            while (i8 < childCount) {
                View childAt = this.mView.getChildAt(i8);
                if (childAt.getId() == 2131427579) {
                    FrameLayout frameLayout = (FrameLayout) childAt;
                    frameLayout.getChildAt(0).measure(View.MeasureSpec.makeMeasureSpec(i6, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(i6, Integer.MIN_VALUE));
                    frameLayout.measure(View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().width, 1073741824), View.MeasureSpec.makeMeasureSpec(i6, 1073741824));
                } else if (childAt.getId() == i4) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
                } else if (childAt.getId() == 2131427643) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
                } else if (childAt.getId() == 2131428892) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.max(this.mBottomSpacerHeight, 0), 1073741824));
                } else {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
                }
                if (childAt.getVisibility() != 8) {
                    i7 = childAt.getMeasuredHeight() + i7;
                }
                i8++;
                i4 = 2131428891;
            }
            return new AuthDialog.LayoutParams(i, i7);
        } else if (rotation == 1 || rotation == 3) {
            WindowMetrics maximumWindowMetrics2 = this.mWindowManager.getMaximumWindowMetrics();
            int viewHeightPx3 = getViewHeightPx(2131429057);
            int viewHeightPx4 = getViewHeightPx(2131428947);
            int viewHeightPx5 = getViewHeightPx(2131427815);
            int viewHeightPx6 = getViewHeightPx(2131428891);
            int viewHeightPx7 = getViewHeightPx(2131428120);
            int viewHeightPx8 = getViewHeightPx(2131427643);
            if (maximumWindowMetrics2 != null) {
                insets2 = maximumWindowMetrics2.getWindowInsets().getInsets(WindowInsets.Type.navigationBars());
            } else {
                insets2 = Insets.NONE;
            }
            int calculateBottomSpacerHeightForLandscape = calculateBottomSpacerHeightForLandscape(viewHeightPx3, viewHeightPx4, viewHeightPx5, viewHeightPx6, viewHeightPx7, viewHeightPx8, insets2.bottom);
            if (maximumWindowMetrics2 != null) {
                rect2 = maximumWindowMetrics2.getBounds();
            } else {
                rect2 = new Rect();
            }
            int calculateHorizontalSpacerWidthForLandscape = calculateHorizontalSpacerWidthForLandscape(this.mSensorProps, rect2.width(), this.mView.getResources().getDimensionPixelSize(2131165369), insets2.left + insets2.right);
            int i9 = this.mSensorProps.getLocation().sensorRadius * 2;
            int i10 = (calculateHorizontalSpacerWidthForLandscape * 2) + i9;
            int childCount2 = this.mView.getChildCount();
            int i11 = 0;
            int i12 = 0;
            while (i5 < childCount2) {
                View childAt2 = this.mView.getChildAt(i5);
                if (childAt2.getId() == i3) {
                    FrameLayout frameLayout2 = (FrameLayout) childAt2;
                    frameLayout2.getChildAt(i12).measure(View.MeasureSpec.makeMeasureSpec(i9, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(i9, Integer.MIN_VALUE));
                    frameLayout2.measure(View.MeasureSpec.makeMeasureSpec(i10, 1073741824), View.MeasureSpec.makeMeasureSpec(i9, 1073741824));
                } else if (childAt2.getId() == 2131428891) {
                    childAt2.measure(View.MeasureSpec.makeMeasureSpec(i10, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt2.getLayoutParams().height - Math.min(calculateBottomSpacerHeightForLandscape, 0), 1073741824));
                } else if (childAt2.getId() == 2131427643) {
                    childAt2.measure(View.MeasureSpec.makeMeasureSpec(i10, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt2.getLayoutParams().height, 1073741824));
                } else if (childAt2.getId() == 2131428892) {
                    childAt2.measure(View.MeasureSpec.makeMeasureSpec(i10, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.max(calculateBottomSpacerHeightForLandscape, 0), 1073741824));
                } else {
                    childAt2.measure(View.MeasureSpec.makeMeasureSpec(i10, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
                }
                if (childAt2.getVisibility() != 8) {
                    i11 = childAt2.getMeasuredHeight() + i11;
                }
                i5++;
                i3 = 2131427579;
                i12 = 0;
            }
            return new AuthDialog.LayoutParams(i10, i11);
        } else {
            KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Unsupported display rotation: ", rotation, "UdfpsDialogMeasurementAdapter");
            return layoutParams;
        }
    }

    public UdfpsDialogMeasureAdapter(ViewGroup viewGroup, FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
        this.mView = viewGroup;
        this.mSensorProps = fingerprintSensorPropertiesInternal;
        this.mWindowManager = (WindowManager) viewGroup.getContext().getSystemService(WindowManager.class);
    }

    @VisibleForTesting
    public static int calculateBottomSpacerHeightForPortrait(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, int i, int i2, int i3, int i4, int i5) {
        SensorLocationInternal location = fingerprintSensorPropertiesInternal.getLocation();
        int i6 = (i - location.sensorLocationY) - location.sensorRadius;
        int i7 = (((i6 - i2) - i3) - i4) - i5;
        if (DEBUG) {
            StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("Display height: ", i, ", Distance from bottom: ", i6, ", Bottom margin: ");
            m.append(i4);
            m.append(", Navbar bottom inset: ");
            m.append(i5);
            m.append(", Bottom spacer height (portrait): ");
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(m, i7, "UdfpsDialogMeasurementAdapter");
        }
        return i7;
    }

    @VisibleForTesting
    public static int calculateHorizontalSpacerWidthForLandscape(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, int i, int i2, int i3) {
        SensorLocationInternal location = fingerprintSensorPropertiesInternal.getLocation();
        int i4 = (i - location.sensorLocationY) - location.sensorRadius;
        int i5 = (i4 - i2) - i3;
        if (DEBUG) {
            StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("Display width: ", i, ", Distance from edge: ", i4, ", Dialog margin: ");
            m.append(i2);
            m.append(", Navbar horizontal inset: ");
            m.append(i3);
            m.append(", Horizontal spacer width (landscape): ");
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(m, i5, "UdfpsDialogMeasurementAdapter");
        }
        return i5;
    }
}
