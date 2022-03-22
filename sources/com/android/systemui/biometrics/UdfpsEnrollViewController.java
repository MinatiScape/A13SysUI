package com.android.systemui.biometrics;

import android.graphics.PointF;
import com.android.systemui.biometrics.UdfpsEnrollHelper;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class UdfpsEnrollViewController extends UdfpsAnimationViewController<UdfpsEnrollView> {
    public final UdfpsEnrollHelper mEnrollHelper;
    public final AnonymousClass1 mEnrollHelperListener = new AnonymousClass1();
    public final int mEnrollProgressBarRadius = getContext().getResources().getInteger(2131492910);

    /* renamed from: com.android.systemui.biometrics.UdfpsEnrollViewController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements UdfpsEnrollHelper.Listener {
        public AnonymousClass1() {
        }
    }

    public UdfpsEnrollViewController(UdfpsEnrollView udfpsEnrollView, UdfpsEnrollHelper udfpsEnrollHelper, StatusBarStateController statusBarStateController, PanelExpansionStateManager panelExpansionStateManager, SystemUIDialogManager systemUIDialogManager, DumpManager dumpManager) {
        super(udfpsEnrollView, statusBarStateController, panelExpansionStateManager, systemUIDialogManager, dumpManager);
        this.mEnrollHelper = udfpsEnrollHelper;
        UdfpsEnrollDrawable udfpsEnrollDrawable = udfpsEnrollView.mFingerprintDrawable;
        Objects.requireNonNull(udfpsEnrollDrawable);
        udfpsEnrollDrawable.mEnrollHelper = udfpsEnrollHelper;
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public final String getTag() {
        return "UdfpsEnrollViewController";
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public final PointF getTouchTranslation() {
        if (!this.mEnrollHelper.isGuidedEnrollmentStage()) {
            return new PointF(0.0f, 0.0f);
        }
        return this.mEnrollHelper.getNextGuidedEnrollmentPoint();
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController, com.android.systemui.util.ViewController
    public final void onViewAttached() {
        boolean z;
        int i;
        super.onViewAttached();
        UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
        Objects.requireNonNull(udfpsEnrollHelper);
        if (udfpsEnrollHelper.mEnrollReason == 2) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            UdfpsEnrollHelper udfpsEnrollHelper2 = this.mEnrollHelper;
            AnonymousClass1 r4 = this.mEnrollHelperListener;
            Objects.requireNonNull(udfpsEnrollHelper2);
            udfpsEnrollHelper2.mListener = r4;
            if (r4 != null && (i = udfpsEnrollHelper2.mTotalSteps) != -1) {
                int i2 = udfpsEnrollHelper2.mRemainingSteps;
                UdfpsEnrollView udfpsEnrollView = (UdfpsEnrollView) UdfpsEnrollViewController.this.mView;
                Objects.requireNonNull(udfpsEnrollView);
                udfpsEnrollView.mHandler.post(new UdfpsEnrollView$$ExternalSyntheticLambda1(udfpsEnrollView, i2, i));
            }
        }
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public final int getPaddingX() {
        return this.mEnrollProgressBarRadius;
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationViewController
    public final int getPaddingY() {
        return this.mEnrollProgressBarRadius;
    }
}
