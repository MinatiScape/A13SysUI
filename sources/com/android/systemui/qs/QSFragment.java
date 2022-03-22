package com.android.systemui.qs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Trace;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda1;
import com.android.systemui.DejankUtils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.qs.QSContainerController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.customize.QSCustomizer;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.dagger.QSFragmentComponent;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.MultiUserSwitchController;
import com.android.systemui.statusbar.phone.MultiUserSwitchController$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.systemui.util.LifecycleFragment;
import com.android.systemui.util.animation.UniqueObjectHostView;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda2;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class QSFragment extends LifecycleFragment implements QS, CommandQueue.Callbacks, StatusBarStateController.StateListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final KeyguardBypassController mBypassController;
    public QSContainerImpl mContainer;
    public DumpManager mDumpManager;
    public final FalsingManager mFalsingManager;
    public QSFooter mFooter;
    public float mFullShadeProgress;
    public QuickStatusBarHeader mHeader;
    public boolean mHeaderAnimating;
    public boolean mInSplitShade;
    public float mLastHeaderTranslation;
    public boolean mLastKeyguardAndExpanded;
    public float mLastPanelFraction;
    public int mLastViewHeight;
    public int mLayoutDirection;
    public boolean mListening;
    public QS.HeightListener mPanelView;
    public QSAnimator mQSAnimator;
    public QSContainerImplController mQSContainerImplController;
    public QSCustomizerController mQSCustomizerController;
    public FooterActionsController mQSFooterActionController;
    public QSPanelController mQSPanelController;
    public NonInterceptingScrollView mQSPanelScrollView;
    public QSSquishinessController mQSSquishinessController;
    public final MediaHost mQqsMediaHost;
    public final QSFragmentComponent.Factory mQsComponentFactory;
    public boolean mQsDisabled;
    public boolean mQsExpanded;
    public final QSFragmentDisableFlagsLogger mQsFragmentDisableFlagsLogger;
    public final MediaHost mQsMediaHost;
    public QuickQSPanelController mQuickQSPanelController;
    public final RemoteInputQuickSettingsDisabler mRemoteInputQuickSettingsDisabler;
    public QS.ScrollListener mScrollListener;
    public boolean mShowCollapsedOnKeyguard;
    public boolean mStackScrollerOverscrolling;
    public int mState;
    public final StatusBarStateController mStatusBarStateController;
    public boolean mTransitioningToFullShade;
    public final Rect mQsBounds = new Rect();
    public float mLastQSExpansion = -1.0f;
    public float mSquishinessFraction = 1.0f;
    public int[] mTmpLocation = new int[2];
    public final AnonymousClass3 mAnimateHeaderSlidingInListener = new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.QSFragment.3
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            QSFragment qSFragment = QSFragment.this;
            qSFragment.mHeaderAnimating = false;
            qSFragment.updateQsState();
            QSFragment.this.getView().animate().setListener(null);
        }
    };

    @Override // com.android.systemui.plugins.qs.QS
    public final void setHasNotifications(boolean z) {
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setHeaderClickable(boolean z) {
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setTransitionToFullShadeAmount(float f, float f2) {
        boolean z;
        if (f > 0.0f) {
            z = true;
        } else {
            z = false;
        }
        if (z != this.mTransitioningToFullShade) {
            this.mTransitioningToFullShade = z;
            updateShowCollapsedOnKeyguard();
        }
        this.mFullShadeProgress = f2;
        float f3 = this.mLastQSExpansion;
        float f4 = this.mLastPanelFraction;
        float f5 = this.mLastHeaderTranslation;
        if (!z) {
            f2 = this.mSquishinessFraction;
        }
        setQsExpansion(f3, f4, f5, f2);
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void closeCustomizer() {
        this.mQSCustomizerController.hide();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void closeDetail() {
        QSPanelController qSPanelController = this.mQSPanelController;
        Objects.requireNonNull(qSPanelController);
        QSCustomizerController qSCustomizerController = ((QSPanelControllerBase) qSPanelController).mQsCustomizerController;
        Objects.requireNonNull(qSCustomizerController);
        if (((QSCustomizer) qSCustomizerController.mView).isShown()) {
            ((QSPanelControllerBase) qSPanelController).mQsCustomizerController.hide();
        }
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final int getDesiredHeight() {
        QSCustomizerController qSCustomizerController = this.mQSCustomizerController;
        Objects.requireNonNull(qSCustomizerController);
        if (((QSCustomizer) qSCustomizerController.mView).isCustomizing()) {
            return getView().getHeight();
        }
        return getView().getMeasuredHeight();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final int getHeightDiff() {
        return this.mHeader.getPaddingBottom() + (this.mQSPanelScrollView.getBottom() - this.mHeader.getBottom());
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final int getQsMinExpansionHeight() {
        return this.mHeader.getHeight();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final boolean isCustomizing() {
        QSCustomizerController qSCustomizerController = this.mQSCustomizerController;
        Objects.requireNonNull(qSCustomizerController);
        return ((QSCustomizer) qSCustomizerController.mView).isCustomizing();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final boolean isFullyCollapsed() {
        float f = this.mLastQSExpansion;
        if (f == 0.0f || f == -1.0f) {
            return true;
        }
        return false;
    }

    public final boolean isKeyguardState() {
        if (this.mStatusBarStateController.getState() == 1) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final boolean isShowingDetail() {
        QSCustomizerController qSCustomizerController = this.mQSCustomizerController;
        Objects.requireNonNull(qSCustomizerController);
        return ((QSCustomizer) qSCustomizerController.mView).isCustomizing();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void notifyCustomizeChanged() {
        int i;
        int i2;
        this.mContainer.updateExpansion();
        boolean isCustomizing = isCustomizing();
        NonInterceptingScrollView nonInterceptingScrollView = this.mQSPanelScrollView;
        int i3 = 0;
        if (!isCustomizing) {
            i = 0;
        } else {
            i = 4;
        }
        nonInterceptingScrollView.setVisibility(i);
        QSFooter qSFooter = this.mFooter;
        if (!isCustomizing) {
            i2 = 0;
        } else {
            i2 = 4;
        }
        qSFooter.setVisibility(i2);
        this.mQSFooterActionController.setVisible(!isCustomizing);
        QuickStatusBarHeader quickStatusBarHeader = this.mHeader;
        if (isCustomizing) {
            i3 = 4;
        }
        quickStatusBarHeader.setVisibility(i3);
        this.mPanelView.onQsHeightChanged();
    }

    @Override // android.app.Fragment
    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        try {
            Trace.beginSection("QSFragment#onCreateView");
            return layoutInflater.cloneInContext(new ContextThemeWrapper(getContext(), 2132018192)).inflate(2131624430, viewGroup, false);
        } finally {
            Trace.endSection();
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onStateChanged(int i) {
        this.mState = i;
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        this.mLastQSExpansion = -1.0f;
        QSAnimator qSAnimator = this.mQSAnimator;
        if (qSAnimator != null) {
            qSAnimator.mOnKeyguard = z;
            qSAnimator.updateQQSVisibility();
            if (qSAnimator.mOnKeyguard) {
                qSAnimator.clearAnimationState();
            }
        }
        this.mFooter.setKeyguardShowing(z);
        FooterActionsController footerActionsController = this.mQSFooterActionController;
        Objects.requireNonNull(footerActionsController);
        footerActionsController.setExpansion(footerActionsController.lastExpansion);
        updateQsState();
        updateShowCollapsedOnKeyguard();
    }

    @Override // android.app.Fragment
    public final void onViewCreated(View view, Bundle bundle) {
        QSFragmentComponent create = this.mQsComponentFactory.create(this);
        this.mQSPanelController = create.getQSPanelController();
        this.mQuickQSPanelController = create.getQuickQSPanelController();
        this.mQSFooterActionController = create.getQSFooterActionController();
        this.mQSPanelController.init();
        this.mQuickQSPanelController.init();
        this.mQSFooterActionController.init();
        NonInterceptingScrollView nonInterceptingScrollView = (NonInterceptingScrollView) view.findViewById(2131427951);
        this.mQSPanelScrollView = nonInterceptingScrollView;
        nonInterceptingScrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.qs.QSFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                QSFragment qSFragment = QSFragment.this;
                Objects.requireNonNull(qSFragment);
                qSFragment.updateQsBounds();
            }
        });
        this.mQSPanelScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() { // from class: com.android.systemui.qs.QSFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnScrollChangeListener
            public final void onScrollChange(View view2, int i, int i2, int i3, int i4) {
                QSFragment qSFragment = QSFragment.this;
                Objects.requireNonNull(qSFragment);
                QSAnimator qSAnimator = qSFragment.mQSAnimator;
                Objects.requireNonNull(qSAnimator);
                qSAnimator.mNeedsAnimatorUpdate = true;
                QuickStatusBarHeader quickStatusBarHeader = qSFragment.mHeader;
                Objects.requireNonNull(quickStatusBarHeader);
                quickStatusBarHeader.mStatusIconsView.setScrollY(i2);
                quickStatusBarHeader.mDatePrivacyView.setScrollY(i2);
                QS.ScrollListener scrollListener = qSFragment.mScrollListener;
                if (scrollListener != null) {
                    scrollListener.onQsPanelScrollChanged(i2);
                }
            }
        });
        this.mHeader = (QuickStatusBarHeader) view.findViewById(2131428079);
        QSPanelController qSPanelController = this.mQSPanelController;
        Objects.requireNonNull(qSPanelController);
        QSPanel qSPanel = (QSPanel) qSPanelController.mView;
        Objects.requireNonNull(qSPanel);
        qSPanel.mHeaderContainer = (ViewGroup) view.findViewById(2131428085);
        this.mFooter = create.getQSFooter();
        QSContainerImplController qSContainerImplController = create.getQSContainerImplController();
        this.mQSContainerImplController = qSContainerImplController;
        qSContainerImplController.init();
        QSContainerImplController qSContainerImplController2 = this.mQSContainerImplController;
        Objects.requireNonNull(qSContainerImplController2);
        QSContainerImpl qSContainerImpl = (QSContainerImpl) qSContainerImplController2.mView;
        this.mContainer = qSContainerImpl;
        this.mDumpManager.registerDumpable(qSContainerImpl.getClass().getName(), this.mContainer);
        this.mQSAnimator = create.getQSAnimator();
        this.mQSSquishinessController = create.getQSSquishinessController();
        QSCustomizerController qSCustomizerController = create.getQSCustomizerController();
        this.mQSCustomizerController = qSCustomizerController;
        qSCustomizerController.init();
        QSCustomizerController qSCustomizerController2 = this.mQSCustomizerController;
        Objects.requireNonNull(qSCustomizerController2);
        QSCustomizer qSCustomizer = (QSCustomizer) qSCustomizerController2.mView;
        Objects.requireNonNull(qSCustomizer);
        qSCustomizer.mQs = this;
        if (bundle != null) {
            setExpanded(bundle.getBoolean("expanded"));
            setListening(bundle.getBoolean("listening"));
            setEditLocation(view);
            final QSCustomizerController qSCustomizerController3 = this.mQSCustomizerController;
            Objects.requireNonNull(qSCustomizerController3);
            if (bundle.getBoolean("qs_customizing")) {
                ((QSCustomizer) qSCustomizerController3.mView).setVisibility(0);
                ((QSCustomizer) qSCustomizerController3.mView).addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.qs.customize.QSCustomizerController.5
                    @Override // android.view.View.OnLayoutChangeListener
                    public final void onLayoutChange(View view2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                        ((QSCustomizer) qSCustomizerController3.mView).removeOnLayoutChangeListener(this);
                        qSCustomizerController3.show(0, 0, true);
                    }
                });
            }
            if (this.mQsExpanded) {
                this.mQSPanelController.getTileLayout().restoreInstanceState(bundle);
            }
        }
        this.mStatusBarStateController.addCallback(this);
        onStateChanged(this.mStatusBarStateController.getState());
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.qs.QSFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                boolean z;
                QSFragment qSFragment = QSFragment.this;
                Objects.requireNonNull(qSFragment);
                if (i6 - i8 != i2 - i4) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    qSFragment.setQsExpansion(qSFragment.mLastQSExpansion, qSFragment.mLastPanelFraction, qSFragment.mLastHeaderTranslation, qSFragment.mSquishinessFraction);
                }
            }
        });
        QSPanelController qSPanelController2 = this.mQSPanelController;
        TaskView$$ExternalSyntheticLambda2 taskView$$ExternalSyntheticLambda2 = new TaskView$$ExternalSyntheticLambda2(this, 3);
        Objects.requireNonNull(qSPanelController2);
        qSPanelController2.mUsingHorizontalLayoutChangedListener = taskView$$ExternalSyntheticLambda2;
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setCollapsedMediaVisibilityChangedListener(Consumer<Boolean> consumer) {
        QuickQSPanelController quickQSPanelController = this.mQuickQSPanelController;
        Objects.requireNonNull(quickQSPanelController);
        quickQSPanelController.mMediaVisibilityChangedListener = consumer;
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setContainerController(QSContainerController qSContainerController) {
        QSCustomizerController qSCustomizerController = this.mQSCustomizerController;
        Objects.requireNonNull(qSCustomizerController);
        QSCustomizer qSCustomizer = (QSCustomizer) qSCustomizerController.mView;
        Objects.requireNonNull(qSCustomizer);
        qSCustomizer.mQsContainerController = qSContainerController;
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setExpandClickListener(View.OnClickListener onClickListener) {
        this.mFooter.setExpandClickListener(onClickListener);
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setExpanded(boolean z) {
        this.mQsExpanded = z;
        this.mQSPanelController.setListening(this.mListening, z);
        updateQsState();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setHeaderListening(boolean z) {
        QSContainerImplController qSContainerImplController = this.mQSContainerImplController;
        Objects.requireNonNull(qSContainerImplController);
        qSContainerImplController.mQuickStatusBarHeaderController.setListening(z);
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setHeightOverride(int i) {
        QSContainerImpl qSContainerImpl = this.mContainer;
        Objects.requireNonNull(qSContainerImpl);
        qSContainerImpl.mHeightOverride = i;
        qSContainerImpl.updateExpansion();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setInSplitShade(boolean z) {
        this.mInSplitShade = z;
        QSAnimator qSAnimator = this.mQSAnimator;
        Objects.requireNonNull(qSAnimator);
        qSAnimator.mTranslateWhileExpanding = z;
        updateShowCollapsedOnKeyguard();
        updateQsState();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setListening(boolean z) {
        this.mListening = z;
        QSContainerImplController qSContainerImplController = this.mQSContainerImplController;
        Objects.requireNonNull(qSContainerImplController);
        qSContainerImplController.mQuickStatusBarHeaderController.setListening(z);
        this.mQSFooterActionController.setListening(z);
        this.mQSPanelController.setListening(this.mListening, this.mQsExpanded);
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setOverscrolling(boolean z) {
        this.mStackScrollerOverscrolling = z;
        updateQsState();
    }

    /* JADX WARN: Code restructure failed: missing block: B:129:0x01c5, code lost:
        if (r6 == false) goto L_0x01c7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x01d7, code lost:
        if (r4 != false) goto L_0x01d9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x01d9, code lost:
        r4 = r2.qsAnimator;
        java.util.Objects.requireNonNull(r4);
        r4.mNeedsAnimatorUpdate = true;
     */
    @Override // com.android.systemui.plugins.qs.QS
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setQsExpansion(float r17, float r18, float r19, float r20) {
        /*
            Method dump skipped, instructions count: 558
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.QSFragment.setQsExpansion(float, float, float, float):void");
    }

    public final void updateQsBounds() {
        if (this.mLastQSExpansion == 1.0f) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mQSPanelScrollView.getLayoutParams();
            this.mQsBounds.set(-marginLayoutParams.leftMargin, 0, this.mQSPanelScrollView.getWidth() + marginLayoutParams.rightMargin, this.mQSPanelScrollView.getHeight());
        }
        this.mQSPanelScrollView.setClipBounds(this.mQsBounds);
    }

    public final void updateQsState() {
        boolean z;
        boolean z2;
        int i;
        boolean z3;
        boolean z4;
        boolean z5;
        int i2;
        boolean z6 = true;
        int i3 = 0;
        if (this.mQsExpanded || this.mInSplitShade) {
            z = true;
        } else {
            z = false;
        }
        if (z || this.mStackScrollerOverscrolling || this.mHeaderAnimating) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mQSPanelController.setExpanded(z);
        boolean isKeyguardState = isKeyguardState();
        QuickStatusBarHeader quickStatusBarHeader = this.mHeader;
        if (z || !isKeyguardState || this.mHeaderAnimating || this.mShowCollapsedOnKeyguard) {
            i = 0;
        } else {
            i = 4;
        }
        quickStatusBarHeader.setVisibility(i);
        QuickStatusBarHeader quickStatusBarHeader2 = this.mHeader;
        if ((!isKeyguardState || this.mHeaderAnimating || this.mShowCollapsedOnKeyguard) && (!z || this.mStackScrollerOverscrolling)) {
            z3 = false;
        } else {
            z3 = true;
        }
        QuickQSPanelController quickQSPanelController = this.mQuickQSPanelController;
        Objects.requireNonNull(quickStatusBarHeader2);
        if (quickStatusBarHeader2.mExpanded != z3) {
            quickStatusBarHeader2.mExpanded = z3;
            quickQSPanelController.setExpanded(z3);
            quickStatusBarHeader2.post(new LockIconViewController$$ExternalSyntheticLambda1(quickStatusBarHeader2, 1));
        }
        if (this.mQsDisabled || !z2) {
            z4 = false;
        } else {
            z4 = true;
        }
        if (!z4 || (!z && isKeyguardState && !this.mHeaderAnimating && !this.mShowCollapsedOnKeyguard)) {
            z5 = false;
        } else {
            z5 = true;
        }
        QSFooter qSFooter = this.mFooter;
        if (z5) {
            i2 = 0;
        } else {
            i2 = 4;
        }
        qSFooter.setVisibility(i2);
        this.mQSFooterActionController.setVisible(z5);
        QSFooter qSFooter2 = this.mFooter;
        if ((!isKeyguardState || this.mHeaderAnimating || this.mShowCollapsedOnKeyguard) && (!z || this.mStackScrollerOverscrolling)) {
            z6 = false;
        }
        qSFooter2.setExpanded(z6);
        QSPanelController qSPanelController = this.mQSPanelController;
        if (!z4) {
            i3 = 4;
        }
        Objects.requireNonNull(qSPanelController);
        ((QSPanel) qSPanelController.mView).setVisibility(i3);
    }

    public final void updateShowCollapsedOnKeyguard() {
        boolean z;
        if (this.mBypassController.getBypassEnabled() || (this.mTransitioningToFullShade && !this.mInSplitShade)) {
            z = true;
        } else {
            z = false;
        }
        if (z != this.mShowCollapsedOnKeyguard) {
            this.mShowCollapsedOnKeyguard = z;
            updateQsState();
            QSAnimator qSAnimator = this.mQSAnimator;
            if (qSAnimator != null) {
                qSAnimator.mShowCollapsedOnKeyguard = z;
                qSAnimator.updateQQSVisibility();
                qSAnimator.setPosition(qSAnimator.mLastPosition);
            }
            if (!z && isKeyguardState()) {
                setQsExpansion(this.mLastQSExpansion, this.mLastPanelFraction, 0.0f, this.mSquishinessFraction);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.systemui.qs.QSFragment$3] */
    public QSFragment(RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler, StatusBarStateController statusBarStateController, CommandQueue commandQueue, MediaHost mediaHost, MediaHost mediaHost2, KeyguardBypassController keyguardBypassController, QSFragmentComponent.Factory factory, QSFragmentDisableFlagsLogger qSFragmentDisableFlagsLogger, FalsingManager falsingManager, DumpManager dumpManager) {
        this.mRemoteInputQuickSettingsDisabler = remoteInputQuickSettingsDisabler;
        this.mQsMediaHost = mediaHost;
        this.mQqsMediaHost = mediaHost2;
        this.mQsComponentFactory = factory;
        this.mQsFragmentDisableFlagsLogger = qSFragmentDisableFlagsLogger;
        commandQueue.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) this);
        this.mFalsingManager = falsingManager;
        this.mBypassController = keyguardBypassController;
        this.mStatusBarStateController = statusBarStateController;
        this.mDumpManager = dumpManager;
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void animateHeaderSlidingOut() {
        if (getView().getY() != (-this.mHeader.getHeight())) {
            this.mHeaderAnimating = true;
            getView().animate().y(-this.mHeader.getHeight()).setStartDelay(0L).setDuration(360L).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.QSFragment.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    if (QSFragment.this.getView() != null) {
                        QSFragment.this.getView().animate().setListener(null);
                    }
                    QSFragment qSFragment = QSFragment.this;
                    qSFragment.mHeaderAnimating = false;
                    qSFragment.updateQsState();
                }
            }).start();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void disable(int i, int i2, int i3, boolean z) {
        int i4;
        boolean z2;
        boolean z3;
        boolean z4;
        int i5;
        if (i == getContext().getDisplayId()) {
            RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler = this.mRemoteInputQuickSettingsDisabler;
            Objects.requireNonNull(remoteInputQuickSettingsDisabler);
            if (!remoteInputQuickSettingsDisabler.remoteInputActive || !remoteInputQuickSettingsDisabler.isLandscape || remoteInputQuickSettingsDisabler.shouldUseSplitNotificationShade) {
                i4 = i3;
            } else {
                i4 = i3 | 1;
            }
            QSFragmentDisableFlagsLogger qSFragmentDisableFlagsLogger = this.mQsFragmentDisableFlagsLogger;
            Objects.requireNonNull(qSFragmentDisableFlagsLogger);
            LogBuffer logBuffer = qSFragmentDisableFlagsLogger.buffer;
            LogLevel logLevel = LogLevel.INFO;
            QSFragmentDisableFlagsLogger$logDisableFlagChange$2 qSFragmentDisableFlagsLogger$logDisableFlagChange$2 = new QSFragmentDisableFlagsLogger$logDisableFlagChange$2(qSFragmentDisableFlagsLogger);
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("QSFragmentDisableFlagsLog", logLevel, qSFragmentDisableFlagsLogger$logDisableFlagChange$2);
                obtain.int1 = i2;
                obtain.int2 = i3;
                obtain.long1 = i2;
                obtain.long2 = i4;
                logBuffer.push(obtain);
            }
            int i6 = i4 & 1;
            boolean z5 = false;
            if (i6 != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2 != this.mQsDisabled) {
                this.mQsDisabled = z2;
                QSContainerImpl qSContainerImpl = this.mContainer;
                Objects.requireNonNull(qSContainerImpl);
                if (i6 != 0) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (z3 != qSContainerImpl.mQsDisabled) {
                    qSContainerImpl.mQsDisabled = z3;
                }
                QuickStatusBarHeader quickStatusBarHeader = this.mHeader;
                Objects.requireNonNull(quickStatusBarHeader);
                if (i6 != 0) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                if (z4 != quickStatusBarHeader.mQsDisabled) {
                    quickStatusBarHeader.mQsDisabled = z4;
                    QuickQSPanel quickQSPanel = quickStatusBarHeader.mHeaderQsPanel;
                    Objects.requireNonNull(quickQSPanel);
                    int i7 = 8;
                    if (z4 != quickQSPanel.mDisabledByPolicy) {
                        quickQSPanel.mDisabledByPolicy = z4;
                        if (z4) {
                            i5 = 8;
                        } else {
                            i5 = 0;
                        }
                        quickQSPanel.setVisibility(i5);
                    }
                    View view = quickStatusBarHeader.mStatusIconsView;
                    if (!quickStatusBarHeader.mQsDisabled) {
                        i7 = 0;
                    }
                    view.setVisibility(i7);
                    quickStatusBarHeader.updateResources();
                }
                this.mFooter.disable(i4);
                FooterActionsController footerActionsController = this.mQSFooterActionController;
                Objects.requireNonNull(footerActionsController);
                FooterActionsView footerActionsView = (FooterActionsView) footerActionsController.mView;
                MultiUserSwitchController multiUserSwitchController = footerActionsController.multiUserSwitchController;
                Objects.requireNonNull(multiUserSwitchController);
                boolean booleanValue = ((Boolean) DejankUtils.whitelistIpcs(new MultiUserSwitchController$$ExternalSyntheticLambda1(multiUserSwitchController))).booleanValue();
                Objects.requireNonNull(footerActionsView);
                if (i6 != 0) {
                    z5 = true;
                }
                if (z5 != footerActionsView.qsDisabled) {
                    footerActionsView.qsDisabled = z5;
                    footerActionsView.post(new FooterActionsView$updateEverything$1(footerActionsView, booleanValue));
                }
                updateQsState();
            }
        }
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void hideImmediately() {
        getView().animate().cancel();
        getView().setY(-this.mHeader.getHeight());
    }

    @Override // android.app.Fragment, android.content.ComponentCallbacks
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setEditLocation(getView());
        if (configuration.getLayoutDirection() != this.mLayoutDirection) {
            this.mLayoutDirection = configuration.getLayoutDirection();
            QSAnimator qSAnimator = this.mQSAnimator;
            if (qSAnimator != null) {
                Objects.requireNonNull(qSAnimator);
                qSAnimator.updateAnimators();
            }
        }
        updateQsState();
    }

    @Override // com.android.systemui.util.LifecycleFragment, android.app.Fragment
    public final void onDestroy() {
        super.onDestroy();
        this.mStatusBarStateController.removeCallback(this);
        if (this.mListening) {
            setListening(false);
        }
        QSCustomizerController qSCustomizerController = this.mQSCustomizerController;
        Objects.requireNonNull(qSCustomizerController);
        QSCustomizer qSCustomizer = (QSCustomizer) qSCustomizerController.mView;
        Objects.requireNonNull(qSCustomizer);
        qSCustomizer.mQs = null;
        this.mScrollListener = null;
        this.mDumpManager.unregisterDumpable(this.mContainer.getClass().getName());
    }

    @Override // android.app.Fragment
    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("expanded", this.mQsExpanded);
        bundle.putBoolean("listening", this.mListening);
        QSCustomizerController qSCustomizerController = this.mQSCustomizerController;
        Objects.requireNonNull(qSCustomizerController);
        if (((QSCustomizer) qSCustomizerController.mView).isShown()) {
            qSCustomizerController.mKeyguardStateController.removeCallback(qSCustomizerController.mKeyguardCallback);
        }
        bundle.putBoolean("qs_customizing", ((QSCustomizer) qSCustomizerController.mView).isCustomizing());
        if (this.mQsExpanded) {
            this.mQSPanelController.getTileLayout().saveInstanceState(bundle);
        }
    }

    public final void pinToBottom(float f, MediaHost mediaHost, boolean z) {
        float f2;
        UniqueObjectHostView hostView = mediaHost.getHostView();
        if (this.mLastQSExpansion <= 0.0f || isKeyguardState() || !this.mQqsMediaHost.getVisible()) {
            hostView.setTranslationY(0.0f);
            return;
        }
        int i = 0;
        UniqueObjectHostView uniqueObjectHostView = hostView;
        for (View view = (View) hostView.getParent(); !(view instanceof QSContainerImpl) && view != null; view = (View) view.getParent()) {
            i += view.getHeight() - uniqueObjectHostView.getBottom();
            uniqueObjectHostView = view;
        }
        float height = ((f - i) - hostView.getHeight()) - (mediaHost.getCurrentBounds().top - hostView.getTranslationY());
        if (z) {
            f2 = Math.min(height, 0.0f);
        } else {
            f2 = Math.max(height, 0.0f);
        }
        hostView.setTranslationY(f2);
    }

    public final void setEditLocation(View view) {
        View findViewById = view.findViewById(16908291);
        int[] locationOnScreen = findViewById.getLocationOnScreen();
        int i = locationOnScreen[0];
        int height = (findViewById.getHeight() / 2) + locationOnScreen[1];
        QSCustomizerController qSCustomizerController = this.mQSCustomizerController;
        Objects.requireNonNull(qSCustomizerController);
        QSCustomizer qSCustomizer = (QSCustomizer) qSCustomizerController.mView;
        Objects.requireNonNull(qSCustomizer);
        int[] locationOnScreen2 = qSCustomizer.findViewById(2131427793).getLocationOnScreen();
        qSCustomizer.mX = ((findViewById.getWidth() / 2) + i) - locationOnScreen2[0];
        qSCustomizer.mY = height - locationOnScreen2[1];
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setFancyClipping(int i, int i2, int i3, boolean z) {
        if (getView() instanceof QSContainerImpl) {
            QSContainerImpl qSContainerImpl = (QSContainerImpl) getView();
            Objects.requireNonNull(qSContainerImpl);
            float[] fArr = qSContainerImpl.mFancyClippingRadii;
            boolean z2 = false;
            float f = i3;
            boolean z3 = true;
            if (fArr[0] != f) {
                fArr[0] = f;
                fArr[1] = f;
                fArr[2] = f;
                fArr[3] = f;
                z2 = true;
            }
            if (qSContainerImpl.mFancyClippingTop != i) {
                qSContainerImpl.mFancyClippingTop = i;
                z2 = true;
            }
            if (qSContainerImpl.mFancyClippingBottom != i2) {
                qSContainerImpl.mFancyClippingBottom = i2;
                z2 = true;
            }
            if (qSContainerImpl.mClippingEnabled != z) {
                qSContainerImpl.mClippingEnabled = z;
            } else {
                z3 = z2;
            }
            if (z3) {
                qSContainerImpl.updateClippingPath();
            }
        }
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setPanelView(QS.HeightListener heightListener) {
        this.mPanelView = heightListener;
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final void setScrollListener(QS.ScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    @Override // com.android.systemui.plugins.qs.QS
    public final View getHeader() {
        return this.mHeader;
    }

    public boolean isExpanded() {
        return this.mQsExpanded;
    }

    public boolean isListening() {
        return this.mListening;
    }
}
