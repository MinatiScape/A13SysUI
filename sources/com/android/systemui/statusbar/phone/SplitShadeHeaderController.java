package com.android.systemui.statusbar.phone;

import android.view.View;
import androidx.constraintlayout.motion.widget.MotionLayout;
import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.qs.HeaderPrivacyIconsController;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SplitShadeHeaderController.kt */
/* loaded from: classes.dex */
public final class SplitShadeHeaderController implements Dumpable {
    public final List<String> carrierIconSlots;
    public final SplitShadeHeaderController$chipVisibilityListener$1 chipVisibilityListener;
    public final boolean combinedHeaders;
    public final StatusIconContainer iconContainer;
    public final StatusBarIconController.TintedIconManager iconManager;
    public final HeaderPrivacyIconsController privacyIconsController;
    public final QSCarrierGroupController qsCarrierGroupController;
    public int qsScrollY;
    public boolean shadeExpanded;
    public boolean splitShadeMode;
    public final View statusBar;
    public final StatusBarIconController statusBarIconController;
    public boolean visible;
    public float shadeExpandedFraction = -1.0f;
    public float qsExpandedFraction = -1.0f;

    /* JADX WARN: Type inference failed for: r6v4, types: [com.android.systemui.qs.ChipVisibilityListener, com.android.systemui.statusbar.phone.SplitShadeHeaderController$chipVisibilityListener$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SplitShadeHeaderController(android.view.View r15, com.android.systemui.statusbar.phone.StatusBarIconController r16, com.android.systemui.qs.HeaderPrivacyIconsController r17, com.android.systemui.qs.carrier.QSCarrierGroupController.Builder r18, com.android.systemui.flags.FeatureFlags r19, com.android.systemui.battery.BatteryMeterViewController r20, com.android.systemui.dump.DumpManager r21) {
        /*
            Method dump skipped, instructions count: 315
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.SplitShadeHeaderController.<init>(android.view.View, com.android.systemui.statusbar.phone.StatusBarIconController, com.android.systemui.qs.HeaderPrivacyIconsController, com.android.systemui.qs.carrier.QSCarrierGroupController$Builder, com.android.systemui.flags.FeatureFlags, com.android.systemui.battery.BatteryMeterViewController, com.android.systemui.dump.DumpManager):void");
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.visible, "visible: ", printWriter);
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.shadeExpanded, "shadeExpanded: ", printWriter);
        printWriter.println(Intrinsics.stringPlus("shadeExpandedFraction: ", Float.valueOf(this.shadeExpandedFraction)));
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.splitShadeMode, "splitShadeMode: ", printWriter);
        printWriter.println(Intrinsics.stringPlus("qsExpandedFraction: ", Float.valueOf(this.qsExpandedFraction)));
        printWriter.println(Intrinsics.stringPlus("qsScrollY: ", Integer.valueOf(this.qsScrollY)));
        if (this.combinedHeaders) {
            View view = this.statusBar;
            MotionLayout motionLayout = (MotionLayout) view;
            MotionLayout motionLayout2 = (MotionLayout) view;
            Objects.requireNonNull(motionLayout2);
            int i = motionLayout2.mCurrentState;
            if (i == 2131428641) {
                str = "QQS Header";
            } else if (i == 2131428652) {
                str = "QS Header";
            } else if (i == 2131428898) {
                str = "Split Header";
            } else {
                str = "Unknown state";
            }
            printWriter.println(Intrinsics.stringPlus("currentState: ", str));
        }
    }

    public final void updateConstraints() {
        if (this.combinedHeaders) {
            MotionLayout motionLayout = (MotionLayout) this.statusBar;
            if (this.splitShadeMode) {
                motionLayout.setTransition(2131428899);
                return;
            }
            motionLayout.setTransition(2131428087);
            MotionLayout motionLayout2 = (MotionLayout) this.statusBar;
            Objects.requireNonNull(motionLayout2);
            motionLayout2.animateTo(0.0f);
            updatePosition$3();
            if (!this.splitShadeMode && this.combinedHeaders) {
                this.statusBar.setScrollY(this.qsScrollY);
            }
        }
    }

    public final void updatePosition$3() {
        View view = this.statusBar;
        if ((view instanceof MotionLayout) && !this.splitShadeMode && this.visible) {
            ((MotionLayout) view).setProgress(this.qsExpandedFraction);
        }
    }

    public final void updateVisibility() {
        int i;
        boolean z = false;
        if (!this.splitShadeMode && !this.combinedHeaders) {
            i = 8;
        } else if (this.shadeExpanded) {
            i = 0;
        } else {
            i = 4;
        }
        if (this.statusBar.getVisibility() != i) {
            this.statusBar.setVisibility(i);
            if (i == 0) {
                z = true;
            }
            if (this.visible != z) {
                this.visible = z;
                this.qsCarrierGroupController.setListening(z);
                if (this.visible) {
                    QSCarrierGroupController qSCarrierGroupController = this.qsCarrierGroupController;
                    Objects.requireNonNull(qSCarrierGroupController);
                    if (qSCarrierGroupController.mIsSingleCarrier) {
                        this.iconContainer.removeIgnoredSlots(this.carrierIconSlots);
                    } else {
                        this.iconContainer.addIgnoredSlots(this.carrierIconSlots);
                    }
                    QSCarrierGroupController qSCarrierGroupController2 = this.qsCarrierGroupController;
                    QSCarrierGroupController.OnSingleCarrierChangedListener splitShadeHeaderController$updateListeners$1 = new QSCarrierGroupController.OnSingleCarrierChangedListener() { // from class: com.android.systemui.statusbar.phone.SplitShadeHeaderController$updateListeners$1
                        @Override // com.android.systemui.qs.carrier.QSCarrierGroupController.OnSingleCarrierChangedListener
                        public final void onSingleCarrierChanged(boolean z2) {
                            SplitShadeHeaderController splitShadeHeaderController = SplitShadeHeaderController.this;
                            Objects.requireNonNull(splitShadeHeaderController);
                            if (z2) {
                                splitShadeHeaderController.iconContainer.removeIgnoredSlots(splitShadeHeaderController.carrierIconSlots);
                            } else {
                                splitShadeHeaderController.iconContainer.addIgnoredSlots(splitShadeHeaderController.carrierIconSlots);
                            }
                        }
                    };
                    Objects.requireNonNull(qSCarrierGroupController2);
                    qSCarrierGroupController2.mOnSingleCarrierChangedListener = splitShadeHeaderController$updateListeners$1;
                    this.statusBarIconController.addIconGroup(this.iconManager);
                    return;
                }
                QSCarrierGroupController qSCarrierGroupController3 = this.qsCarrierGroupController;
                Objects.requireNonNull(qSCarrierGroupController3);
                qSCarrierGroupController3.mOnSingleCarrierChangedListener = null;
                this.statusBarIconController.removeIconGroup(this.iconManager);
            }
        }
    }
}
