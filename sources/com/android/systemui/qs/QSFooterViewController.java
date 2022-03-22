package com.android.systemui.qs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.ViewController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class QSFooterViewController extends ViewController<QSFooterView> implements QSFooter {
    public final ActivityStarter mActivityStarter;
    public final TextView mBuildText;
    public final View mEditButton;
    public final FalsingManager mFalsingManager;
    public final PageIndicator mPageIndicator;
    public final QSPanelController mQsPanelController;
    public final UserTracker mUserTracker;

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
    }

    @Override // com.android.systemui.qs.QSFooter
    public final void disable(int i) {
        QSFooterView qSFooterView = (QSFooterView) this.mView;
        Objects.requireNonNull(qSFooterView);
        boolean z = true;
        if ((i & 1) == 0) {
            z = false;
        }
        if (z != qSFooterView.mQsDisabled) {
            qSFooterView.mQsDisabled = z;
            qSFooterView.post(new QSFooterView$$ExternalSyntheticLambda0(qSFooterView, 0));
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mBuildText.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.qs.QSFooterViewController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                QSFooterViewController qSFooterViewController = QSFooterViewController.this;
                Objects.requireNonNull(qSFooterViewController);
                CharSequence text = qSFooterViewController.mBuildText.getText();
                if (TextUtils.isEmpty(text)) {
                    return false;
                }
                ((ClipboardManager) qSFooterViewController.mUserTracker.getUserContext().getSystemService(ClipboardManager.class)).setPrimaryClip(ClipData.newPlainText(qSFooterViewController.getResources().getString(2131952078), text));
                Toast.makeText(qSFooterViewController.getContext(), 2131952079, 0).show();
                return true;
            }
        });
        this.mEditButton.setOnClickListener(new QSFooterViewController$$ExternalSyntheticLambda0(this, 0));
        QSPanelController qSPanelController = this.mQsPanelController;
        PageIndicator pageIndicator = this.mPageIndicator;
        Objects.requireNonNull(qSPanelController);
        QSPanel qSPanel = (QSPanel) qSPanelController.mView;
        Objects.requireNonNull(qSPanel);
        if (qSPanel.mTileLayout instanceof PagedTileLayout) {
            qSPanel.mFooterPageIndicator = pageIndicator;
            qSPanel.updatePageIndicator();
        }
        QSFooterView qSFooterView = (QSFooterView) this.mView;
        Objects.requireNonNull(qSFooterView);
        qSFooterView.post(new QSFooterView$$ExternalSyntheticLambda0(qSFooterView, 0));
    }

    @Override // com.android.systemui.qs.QSFooter
    public final void setExpandClickListener(View.OnClickListener onClickListener) {
        QSFooterView qSFooterView = (QSFooterView) this.mView;
        Objects.requireNonNull(qSFooterView);
        qSFooterView.mExpandClickListener = onClickListener;
    }

    @Override // com.android.systemui.qs.QSFooter
    public final void setExpanded(boolean z) {
        QSFooterView qSFooterView = (QSFooterView) this.mView;
        Objects.requireNonNull(qSFooterView);
        if (qSFooterView.mExpanded != z) {
            qSFooterView.mExpanded = z;
            qSFooterView.post(new QSFooterView$$ExternalSyntheticLambda0(qSFooterView, 0));
        }
    }

    @Override // com.android.systemui.qs.QSFooter
    public final void setExpansion(float f) {
        QSFooterView qSFooterView = (QSFooterView) this.mView;
        Objects.requireNonNull(qSFooterView);
        qSFooterView.mExpansionAmount = f;
        TouchAnimator touchAnimator = qSFooterView.mFooterAnimator;
        if (touchAnimator != null) {
            touchAnimator.setPosition(f);
        }
    }

    @Override // com.android.systemui.qs.QSFooter
    public final void setKeyguardShowing(boolean z) {
        QSFooterView qSFooterView = (QSFooterView) this.mView;
        Objects.requireNonNull(qSFooterView);
        float f = qSFooterView.mExpansionAmount;
        qSFooterView.mExpansionAmount = f;
        TouchAnimator touchAnimator = qSFooterView.mFooterAnimator;
        if (touchAnimator != null) {
            touchAnimator.setPosition(f);
        }
    }

    @Override // com.android.systemui.qs.QSFooter
    public final void setVisibility(int i) {
        boolean z;
        ((QSFooterView) this.mView).setVisibility(i);
        View view = this.mEditButton;
        if (i == 0) {
            z = true;
        } else {
            z = false;
        }
        view.setClickable(z);
    }

    public QSFooterViewController(QSFooterView qSFooterView, UserTracker userTracker, FalsingManager falsingManager, ActivityStarter activityStarter, QSPanelController qSPanelController) {
        super(qSFooterView);
        this.mUserTracker = userTracker;
        this.mQsPanelController = qSPanelController;
        this.mFalsingManager = falsingManager;
        this.mActivityStarter = activityStarter;
        this.mBuildText = (TextView) qSFooterView.findViewById(2131427636);
        this.mPageIndicator = (PageIndicator) qSFooterView.findViewById(2131427984);
        this.mEditButton = qSFooterView.findViewById(16908291);
    }
}
