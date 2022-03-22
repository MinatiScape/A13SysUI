package com.android.systemui.qs;

import android.animation.ValueAnimator;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.PagedTileLayout;
import com.android.systemui.qs.PathInterpolatorBuilder;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelControllerBase;
import com.android.systemui.qs.TouchAnimator;
import com.android.systemui.qs.tileimpl.HeightOverrideable;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class QSAnimator implements QSHost.Callback, PagedTileLayout.PageListener, TouchAnimator.Listener, View.OnLayoutChangeListener, View.OnAttachStateChangeListener, TunerService.Tunable {
    public TouchAnimator mAllPagesDelayedAnimator;
    public boolean mAllowFancy;
    public TouchAnimator mBrightnessAnimator;
    public final Executor mExecutor;
    public final QSFgsManagerFooter mFgsManagerFooter;
    public TouchAnimator mFirstPageAnimator;
    public boolean mFullRows;
    public final QSTileHost mHost;
    public float mLastPosition;
    public int mLastQQSTileHeight;
    public TouchAnimator mNonfirstPageAlphaAnimator;
    public int mNumQuickTiles;
    public boolean mOnKeyguard;
    public HeightExpansionAnimator mOtherFirstPageTilesHeightAnimator;
    public PagedTileLayout mPagedLayout;
    public HeightExpansionAnimator mQQSTileHeightAnimator;
    public int mQQSTop;
    public TouchAnimator mQQSTranslationYAnimator;
    public final QSExpansionPathInterpolator mQSExpansionPathInterpolator;
    public TouchAnimator mQSTileLayoutTranslatorAnimator;
    public final QS mQs;
    public final QSPanelController mQsPanelController;
    public final QuickQSPanelController mQuickQSPanelController;
    public final QuickQSPanel mQuickQsPanel;
    public final QuickStatusBarHeader mQuickStatusBarHeader;
    public final QSSecurityFooter mSecurityFooter;
    public boolean mShowCollapsedOnKeyguard;
    public boolean mTranslateWhileExpanding;
    public TouchAnimator mTranslationXAnimator;
    public TouchAnimator mTranslationYAnimator;
    public final TunerService mTunerService;
    public final ArrayList<View> mAllViews = new ArrayList<>();
    public final ArrayList<View> mAnimatedQsViews = new ArrayList<>();
    public boolean mOnFirstPage = true;
    public int mCurrentPage = 0;
    public final SparseArray<Pair<HeightExpansionAnimator, TouchAnimator>> mNonFirstPageQSAnimators = new SparseArray<>();
    public boolean mNeedsAnimatorUpdate = false;
    public int[] mTmpLoc1 = new int[2];
    public int[] mTmpLoc2 = new int[2];
    public final AnonymousClass1 mNonFirstPageListener = new TouchAnimator.ListenerAdapter() { // from class: com.android.systemui.qs.QSAnimator.1
        @Override // com.android.systemui.qs.TouchAnimator.Listener
        public final void onAnimationAtEnd() {
            QSAnimator.this.mQuickQsPanel.setVisibility(4);
        }

        @Override // com.android.systemui.qs.TouchAnimator.Listener
        public final void onAnimationStarted() {
            QSAnimator.this.mQuickQsPanel.setVisibility(0);
        }
    };
    public final QSAnimator$$ExternalSyntheticLambda0 mUpdateAnimators = new QSAnimator$$ExternalSyntheticLambda0(this, 0);

    /* loaded from: classes.dex */
    public static class HeightExpansionAnimator {
        public final ValueAnimator mAnimator;
        public final TouchAnimator.Listener mListener;
        public final AnonymousClass1 mUpdateListener;
        public final ArrayList mViews = new ArrayList();

        public final void resetViewsHeights() {
            int size = this.mViews.size();
            for (int i = 0; i < size; i++) {
                View view = (View) this.mViews.get(i);
                if (view instanceof HeightOverrideable) {
                    ((HeightOverrideable) view).resetOverride();
                } else {
                    view.setBottom(view.getMeasuredHeight() + view.getTop());
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v1, types: [android.animation.ValueAnimator$AnimatorUpdateListener, com.android.systemui.qs.QSAnimator$HeightExpansionAnimator$1] */
        /* JADX WARN: Unknown variable types count: 1 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public HeightExpansionAnimator(com.android.systemui.qs.TouchAnimator.Listener r4, int r5, int r6) {
            /*
                r3 = this;
                r3.<init>()
                java.util.ArrayList r0 = new java.util.ArrayList
                r0.<init>()
                r3.mViews = r0
                com.android.systemui.qs.QSAnimator$HeightExpansionAnimator$1 r0 = new com.android.systemui.qs.QSAnimator$HeightExpansionAnimator$1
                r0.<init>()
                r3.mUpdateListener = r0
                r3.mListener = r4
                r4 = 2
                int[] r1 = new int[r4]
                r2 = 0
                r1[r2] = r5
                r5 = 1
                r1[r5] = r6
                android.animation.ValueAnimator r5 = android.animation.ValueAnimator.ofInt(r1)
                r3.mAnimator = r5
                r3 = -1
                r5.setRepeatCount(r3)
                r5.setRepeatMode(r4)
                r5.addUpdateListener(r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.QSAnimator.HeightExpansionAnimator.<init>(com.android.systemui.qs.TouchAnimator$Listener, int, int):void");
        }
    }

    public final void onPageChanged(boolean z, int i) {
        if (!(i == -1 || this.mCurrentPage == i)) {
            this.mCurrentPage = i;
            if (!z && !this.mNonFirstPageQSAnimators.contains(i)) {
                addNonFirstPageAnimators(i);
            }
        }
        if (this.mOnFirstPage != z) {
            if (!z) {
                clearAnimationState();
            }
            this.mOnFirstPage = z;
        }
    }

    public static void getRelativePositionInt(int[] iArr, View view, View view2) {
        if (view != view2 && view != null) {
            if (!view.getClass().equals(SideLabelTileLayout.class)) {
                iArr[0] = view.getLeft() + iArr[0];
                iArr[1] = view.getTop() + iArr[1];
            }
            if (!(view instanceof PagedTileLayout)) {
                iArr[0] = iArr[0] - view.getScrollX();
                iArr[1] = iArr[1] - view.getScrollY();
            }
            getRelativePositionInt(iArr, (View) view.getParent(), view2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x01bd A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void addNonFirstPageAnimators(int r23) {
        /*
            Method dump skipped, instructions count: 588
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.QSAnimator.addNonFirstPageAnimators(int):void");
    }

    public final void clearAnimationState() {
        int size = this.mAllViews.size();
        this.mQuickQsPanel.setAlpha(0.0f);
        for (int i = 0; i < size; i++) {
            View view = this.mAllViews.get(i);
            view.setAlpha(1.0f);
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
            view.setScaleY(1.0f);
            if (view instanceof SideLabelTileLayout) {
                SideLabelTileLayout sideLabelTileLayout = (SideLabelTileLayout) view;
                sideLabelTileLayout.setClipChildren(false);
                sideLabelTileLayout.setClipToPadding(false);
            }
        }
        HeightExpansionAnimator heightExpansionAnimator = this.mQQSTileHeightAnimator;
        if (heightExpansionAnimator != null) {
            heightExpansionAnimator.resetViewsHeights();
        }
        HeightExpansionAnimator heightExpansionAnimator2 = this.mOtherFirstPageTilesHeightAnimator;
        if (heightExpansionAnimator2 != null) {
            heightExpansionAnimator2.resetViewsHeights();
        }
        for (int i2 = 0; i2 < this.mNonFirstPageQSAnimators.size(); i2++) {
            ((HeightExpansionAnimator) this.mNonFirstPageQSAnimators.valueAt(i2).first).resetViewsHeights();
        }
        int size2 = this.mAnimatedQsViews.size();
        for (int i3 = 0; i3 < size2; i3++) {
            this.mAnimatedQsViews.get(i3).setVisibility(0);
        }
    }

    @Override // com.android.systemui.qs.TouchAnimator.Listener
    public final void onAnimationAtEnd() {
        this.mQuickQsPanel.setVisibility(4);
        int size = this.mAnimatedQsViews.size();
        for (int i = 0; i < size; i++) {
            this.mAnimatedQsViews.get(i).setVisibility(0);
        }
    }

    @Override // com.android.systemui.qs.TouchAnimator.Listener
    public final void onAnimationAtStart() {
        this.mQuickQsPanel.setVisibility(0);
    }

    @Override // android.view.View.OnLayoutChangeListener
    public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        boolean z;
        if (i == i5 && i2 == i6 && i3 == i7 && i4 == i8) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            this.mExecutor.execute(this.mUpdateAnimators);
        }
    }

    @Override // com.android.systemui.qs.QSHost.Callback
    public final void onTilesChanged() {
        this.mExecutor.execute(this.mUpdateAnimators);
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public final void onViewAttachedToWindow(View view) {
        this.mTunerService.addTunable(this, "sysui_qs_fancy_anim", "sysui_qs_move_whole_rows");
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public final void onViewDetachedFromWindow(View view) {
        QSTileHost qSTileHost = this.mHost;
        Objects.requireNonNull(qSTileHost);
        qSTileHost.mCallbacks.remove(this);
        this.mTunerService.removeTunable(this);
    }

    public final void setPosition(float f) {
        if (this.mNeedsAnimatorUpdate) {
            updateAnimators();
        }
        if (this.mFirstPageAnimator != null) {
            if (this.mOnKeyguard) {
                if (this.mShowCollapsedOnKeyguard) {
                    f = 0.0f;
                } else {
                    f = 1.0f;
                }
            }
            this.mLastPosition = f;
            if (this.mAllowFancy) {
                if (this.mOnFirstPage) {
                    this.mQuickQsPanel.setAlpha(1.0f);
                    this.mFirstPageAnimator.setPosition(f);
                    this.mTranslationYAnimator.setPosition(f);
                    this.mTranslationXAnimator.setPosition(f);
                    HeightExpansionAnimator heightExpansionAnimator = this.mOtherFirstPageTilesHeightAnimator;
                    if (heightExpansionAnimator != null) {
                        heightExpansionAnimator.mAnimator.setCurrentFraction(f);
                    }
                } else {
                    this.mNonfirstPageAlphaAnimator.setPosition(f);
                }
                for (int i = 0; i < this.mNonFirstPageQSAnimators.size(); i++) {
                    Pair<HeightExpansionAnimator, TouchAnimator> valueAt = this.mNonFirstPageQSAnimators.valueAt(i);
                    if (valueAt != null) {
                        HeightExpansionAnimator heightExpansionAnimator2 = (HeightExpansionAnimator) valueAt.first;
                        Objects.requireNonNull(heightExpansionAnimator2);
                        heightExpansionAnimator2.mAnimator.setCurrentFraction(f);
                        ((TouchAnimator) valueAt.second).setPosition(f);
                    }
                }
                HeightExpansionAnimator heightExpansionAnimator3 = this.mQQSTileHeightAnimator;
                if (heightExpansionAnimator3 != null) {
                    heightExpansionAnimator3.mAnimator.setCurrentFraction(f);
                }
                this.mQSTileLayoutTranslatorAnimator.setPosition(f);
                this.mQQSTranslationYAnimator.setPosition(f);
                this.mAllPagesDelayedAnimator.setPosition(f);
                TouchAnimator touchAnimator = this.mBrightnessAnimator;
                if (touchAnimator != null) {
                    touchAnimator.setPosition(f);
                }
            }
        }
    }

    public final void updateAnimators() {
        QSPanelController qSPanelController;
        QSPanel.QSTileLayout qSTileLayout;
        TouchAnimator.Builder builder;
        String str;
        TouchAnimator.Builder builder2;
        String str2;
        TouchAnimator.Builder builder3;
        QSPanel.QSTileLayout qSTileLayout2;
        boolean z;
        boolean z2;
        UniqueObjectHostView uniqueObjectHostView;
        QSTileView qSTileView;
        String str3;
        QSPanel.QSTileLayout qSTileLayout3;
        int i;
        TouchAnimator.Builder builder4;
        String str4;
        TouchAnimator.Builder builder5;
        int i2;
        QSTileView qSTileView2;
        String str5;
        TouchAnimator.Builder builder6;
        char c;
        int i3;
        SideLabelTileLayout sideLabelTileLayout;
        boolean z3;
        int i4;
        QSTileView qSTileView3;
        int i5;
        int i6;
        this.mNeedsAnimatorUpdate = false;
        TouchAnimator.Builder builder7 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder8 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder9 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder10 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder11 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder12 = new TouchAnimator.Builder();
        builder12.mInterpolator = Interpolators.ACCELERATE;
        QSTileHost qSTileHost = this.mHost;
        Objects.requireNonNull(qSTileHost);
        Collection<QSTile> values = qSTileHost.mTiles.values();
        clearAnimationState();
        this.mNonFirstPageQSAnimators.clear();
        this.mAllViews.clear();
        this.mAnimatedQsViews.clear();
        this.mQQSTileHeightAnimator = null;
        this.mOtherFirstPageTilesHeightAnimator = null;
        QuickQSPanel quickQSPanel = this.mQuickQsPanel;
        Objects.requireNonNull(quickQSPanel);
        this.mNumQuickTiles = quickQSPanel.mMaxTiles;
        QSPanel.QSTileLayout tileLayout = this.mQsPanelController.getTileLayout();
        this.mAllViews.add((View) tileLayout);
        int heightDiff = this.mQs.getHeightDiff();
        if (!this.mTranslateWhileExpanding) {
            heightDiff = (int) (heightDiff * 0.1f);
        }
        int i7 = heightDiff;
        TouchAnimator.Builder builder13 = new TouchAnimator.Builder();
        String str6 = "translationY";
        builder13.addFloat(tileLayout, str6, i7, 0.0f);
        this.mQSTileLayoutTranslatorAnimator = builder13.build();
        this.mLastQQSTileHeight = 0;
        Objects.requireNonNull(this.mQsPanelController);
        String str7 = "alpha";
        if (!qSPanelController.mRecords.isEmpty()) {
            int i8 = 0;
            for (QSTile qSTile : values) {
                QSPanelController qSPanelController2 = this.mQsPanelController;
                Objects.requireNonNull(qSPanelController2);
                Iterator<QSPanelControllerBase.TileRecord> it = qSPanelController2.mRecords.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        qSTileView = null;
                        break;
                    }
                    QSPanelControllerBase.TileRecord next = it.next();
                    if (next.tile == qSTile) {
                        qSTileView = next.tileView;
                        break;
                    }
                }
                if (qSTileView == null) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("tileView is null ");
                    m.append(qSTile.getTileSpec());
                    Log.e("QSAnimator", m.toString());
                    str3 = str7;
                } else {
                    PagedTileLayout pagedTileLayout = this.mPagedLayout;
                    if (pagedTileLayout != null) {
                        if (pagedTileLayout.mPages.size() == 0) {
                            i6 = 0;
                        } else {
                            i6 = pagedTileLayout.mPages.get(0).mRecords.size();
                        }
                        if (i8 >= i6) {
                            break;
                        }
                    }
                    View iconView = qSTileView.getIcon().getIconView();
                    View view = this.mQs.getView();
                    str3 = str7;
                    if (i8 >= this.mQuickQSPanelController.getTileLayout().getNumVisibleTiles() || !this.mAllowFancy) {
                        i = i7;
                        qSTileLayout3 = tileLayout;
                        builder6 = builder12;
                        qSTileView2 = qSTileView;
                        str5 = str3;
                        if (this.mFullRows) {
                            PagedTileLayout pagedTileLayout2 = this.mPagedLayout;
                            if (pagedTileLayout2 == null) {
                                c = 1;
                                z3 = false;
                                i2 = i8;
                            } else {
                                if (pagedTileLayout2.mPages.size() == 0) {
                                    i4 = 0;
                                } else {
                                    i4 = pagedTileLayout2.mPages.get(0).mColumns;
                                }
                                c = 1;
                                int i9 = (((this.mNumQuickTiles + i4) - 1) / i4) * i4;
                                i2 = i8;
                                if (i2 < i9) {
                                    z3 = true;
                                } else {
                                    z3 = false;
                                }
                            }
                            if (z3) {
                                float[] fArr = new float[2];
                                fArr[0] = -i;
                                fArr[c] = 0.0f;
                                builder5 = builder7;
                                str4 = str6;
                                builder5.addFloat(qSTileView2, str4, fArr);
                                this.mAllViews.add(iconView);
                                builder4 = builder11;
                            } else {
                                builder5 = builder7;
                                str4 = str6;
                                i3 = i;
                            }
                        } else {
                            builder5 = builder7;
                            i2 = i8;
                            str4 = str6;
                            i3 = i;
                            c = 1;
                        }
                        QuickQSPanel quickQSPanel2 = this.mQuickQsPanel;
                        Objects.requireNonNull(quickQSPanel2);
                        getRelativePosition(this.mTmpLoc1, (SideLabelTileLayout) quickQSPanel2.mTileLayout, view);
                        this.mQQSTop = this.mTmpLoc1[c];
                        getRelativePosition(this.mTmpLoc2, qSTileView2, view);
                        i = i3;
                        builder4 = builder11;
                        builder8.addFloat(qSTileView2, str4, -(this.mTmpLoc2[c] - (((int) (((sideLabelTileLayout.mCellHeight * sideLabelTileLayout.mSquishinessFraction) + sideLabelTileLayout.mCellMarginVertical) * (i2 / sideLabelTileLayout.mColumns))) + this.mTmpLoc1[c])), 0.0f);
                        if (this.mOtherFirstPageTilesHeightAnimator == null) {
                            this.mOtherFirstPageTilesHeightAnimator = new HeightExpansionAnimator(this, this.mLastQQSTileHeight, qSTileView2.getMeasuredHeight());
                        }
                        HeightExpansionAnimator heightExpansionAnimator = this.mOtherFirstPageTilesHeightAnimator;
                        Objects.requireNonNull(heightExpansionAnimator);
                        heightExpansionAnimator.mViews.add(qSTileView2);
                        qSTileView2.setClipChildren(true);
                        qSTileView2.setClipToPadding(true);
                        builder5.addFloat(qSTileView2.mo78getSecondaryLabel(), str5, 0.0f, 1.0f);
                        this.mAllViews.add(qSTileView2.mo78getSecondaryLabel());
                    } else {
                        QuickQSPanelController quickQSPanelController = this.mQuickQSPanelController;
                        Objects.requireNonNull(quickQSPanelController);
                        Iterator<QSPanelControllerBase.TileRecord> it2 = quickQSPanelController.mRecords.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                qSTileView3 = null;
                                break;
                            }
                            QSPanelControllerBase.TileRecord next2 = it2.next();
                            if (next2.tile == qSTile) {
                                qSTileView3 = next2.tileView;
                                break;
                            }
                            it2 = it2;
                        }
                        if (qSTileView3 != null) {
                            getRelativePosition(this.mTmpLoc1, qSTileView3, view);
                            getRelativePosition(this.mTmpLoc2, qSTileView, view);
                            int[] iArr = this.mTmpLoc2;
                            int i10 = iArr[1];
                            int[] iArr2 = this.mTmpLoc1;
                            int i11 = i10 - iArr2[1];
                            int i12 = iArr[0] - iArr2[0];
                            QuickStatusBarHeader quickStatusBarHeader = this.mQuickStatusBarHeader;
                            Objects.requireNonNull(quickStatusBarHeader);
                            builder9.addFloat(qSTileView3, str6, 0.0f, i11 - quickStatusBarHeader.mTopViewMeasureHeight);
                            builder8.addFloat(qSTileView, str6, -i5, 0.0f);
                            builder10.addFloat(qSTileView3, "translationX", 0.0f, i12);
                            builder10.addFloat(qSTileView, "translationX", -i12, 0.0f);
                            if (this.mQQSTileHeightAnimator == null) {
                                this.mQQSTileHeightAnimator = new HeightExpansionAnimator(this, qSTileView3.getMeasuredHeight(), qSTileView.getMeasuredHeight());
                                this.mLastQQSTileHeight = qSTileView3.getMeasuredHeight();
                            }
                            HeightExpansionAnimator heightExpansionAnimator2 = this.mQQSTileHeightAnimator;
                            Objects.requireNonNull(heightExpansionAnimator2);
                            heightExpansionAnimator2.mViews.add(qSTileView3);
                            i = i7;
                            qSTileLayout3 = tileLayout;
                            translateContent(qSTileView3.getIcon(), qSTileView.getIcon(), view, i12, i11, this.mTmpLoc1, builder10, builder8, builder9);
                            translateContent(qSTileView3.getLabelContainer(), qSTileView.getLabelContainer(), view, i12, i11, this.mTmpLoc1, builder10, builder8, builder9);
                            translateContent(qSTileView3.getSecondaryIcon(), qSTileView.getSecondaryIcon(), view, i12, i11, this.mTmpLoc1, builder10, builder8, builder9);
                            str5 = str3;
                            builder6 = builder12;
                            builder6.addFloat(qSTileView3.mo78getSecondaryLabel(), str5, 0.0f, 1.0f);
                            builder11.addFloat(qSTileView3.mo78getSecondaryLabel(), str5, 0.0f, 0.0f);
                            qSTileView2 = qSTileView;
                            this.mAnimatedQsViews.add(qSTileView2);
                            this.mAllViews.add(qSTileView3);
                            this.mAllViews.add(qSTileView3.mo78getSecondaryLabel());
                            builder4 = builder11;
                            builder5 = builder7;
                            i2 = i8;
                            str4 = str6;
                        }
                    }
                    this.mAllViews.add(qSTileView2);
                    i8 = i2 + 1;
                    str7 = str5;
                    builder7 = builder5;
                    str6 = str4;
                    builder11 = builder4;
                    i7 = i;
                    tileLayout = qSTileLayout3;
                    builder12 = builder6;
                }
                str7 = str3;
            }
            str2 = str7;
            qSTileLayout = tileLayout;
            builder3 = builder12;
            builder2 = builder7;
            builder = builder11;
            str = str6;
            int i13 = this.mCurrentPage;
            if (i13 != 0) {
                addNonFirstPageAnimators(i13);
            }
        } else {
            str2 = str7;
            qSTileLayout = tileLayout;
            builder3 = builder12;
            builder2 = builder7;
            builder = builder11;
            str = str6;
        }
        if (this.mAllowFancy) {
            QSPanelController qSPanelController3 = this.mQsPanelController;
            Objects.requireNonNull(qSPanelController3);
            QSPanel qSPanel = (QSPanel) qSPanelController3.mView;
            Objects.requireNonNull(qSPanel);
            View view2 = qSPanel.mBrightnessView;
            QuickQSPanelController quickQSPanelController2 = this.mQuickQSPanelController;
            Objects.requireNonNull(quickQSPanelController2);
            QSPanel qSPanel2 = (QSPanel) quickQSPanelController2.mView;
            Objects.requireNonNull(qSPanel2);
            View view3 = qSPanel2.mBrightnessView;
            if (view3 != null && view3.getVisibility() == 0) {
                this.mAnimatedQsViews.add(view2);
                this.mAllViews.add(view3);
                int[] iArr3 = new int[2];
                int[] iArr4 = new int[2];
                View view4 = this.mQs.getView();
                getRelativePositionInt(iArr3, view2, view4);
                getRelativePositionInt(iArr4, view3, view4);
                int i14 = iArr3[1] - iArr4[1];
                QuickStatusBarHeader quickStatusBarHeader2 = this.mQuickStatusBarHeader;
                Objects.requireNonNull(quickStatusBarHeader2);
                int i15 = i14 - quickStatusBarHeader2.mTopViewMeasureHeight;
                TouchAnimator.Builder builder14 = new TouchAnimator.Builder();
                builder14.addFloat(view2, "sliderScaleY", 0.3f, 1.0f);
                builder14.addFloat(view3, str, 0.0f, i15);
                this.mBrightnessAnimator = builder14.build();
                z2 = true;
                z = false;
            } else if (view2 != null) {
                builder2.addFloat(view2, str, view2.getMeasuredHeight() * 0.5f, 0.0f);
                TouchAnimator.Builder builder15 = new TouchAnimator.Builder();
                builder15.addFloat(view2, str2, 0.0f, 1.0f);
                builder15.addFloat(view2, "sliderScaleY", 0.3f, 1.0f);
                builder15.mInterpolator = Interpolators.ALPHA_IN;
                builder15.mStartDelay = 0.3f;
                this.mBrightnessAnimator = builder15.build();
                this.mAllViews.add(view2);
                z2 = true;
                z = false;
            } else {
                this.mBrightnessAnimator = null;
                z2 = true;
                z = false;
            }
            qSTileLayout2 = qSTileLayout;
            builder2.addFloat(qSTileLayout2, str2, 0.0f, 1.0f);
            builder2.addFloat(builder3.build(), "position", 0.0f, 1.0f);
            builder2.mListener = this;
            this.mFirstPageAnimator = builder2.build();
            TouchAnimator.Builder builder16 = new TouchAnimator.Builder();
            builder16.mStartDelay = 0.86f;
            QSFgsManagerFooter qSFgsManagerFooter = this.mFgsManagerFooter;
            Objects.requireNonNull(qSFgsManagerFooter);
            builder16.addFloat(qSFgsManagerFooter.mRootView, str2, 0.0f, 1.0f);
            QSSecurityFooter qSSecurityFooter = this.mSecurityFooter;
            Objects.requireNonNull(qSSecurityFooter);
            builder16.addFloat(qSSecurityFooter.mRootView, str2, 0.0f, 1.0f);
            QSPanelController qSPanelController4 = this.mQsPanelController;
            Objects.requireNonNull(qSPanelController4);
            if (qSPanelController4.mShouldUseSplitNotificationShade || !qSPanelController4.mUsingMediaPlayer || !qSPanelController4.mMediaHost.getVisible() || qSPanelController4.mLastOrientation != 2) {
                z2 = z;
            }
            if (!z2 || (uniqueObjectHostView = this.mQsPanelController.mMediaHost.hostView) == null) {
                this.mQsPanelController.mMediaHost.hostView.setAlpha(1.0f);
            } else {
                builder16.addFloat(uniqueObjectHostView, str2, 0.0f, 1.0f);
            }
            this.mAllPagesDelayedAnimator = builder16.build();
            ArrayList<View> arrayList = this.mAllViews;
            QSFgsManagerFooter qSFgsManagerFooter2 = this.mFgsManagerFooter;
            Objects.requireNonNull(qSFgsManagerFooter2);
            arrayList.add(qSFgsManagerFooter2.mRootView);
            ArrayList<View> arrayList2 = this.mAllViews;
            QSSecurityFooter qSSecurityFooter2 = this.mSecurityFooter;
            Objects.requireNonNull(qSSecurityFooter2);
            arrayList2.add(qSSecurityFooter2.mRootView);
            builder8.mInterpolator = this.mQSExpansionPathInterpolator.getYInterpolator();
            builder9.mInterpolator = this.mQSExpansionPathInterpolator.getYInterpolator();
            QSExpansionPathInterpolator qSExpansionPathInterpolator = this.mQSExpansionPathInterpolator;
            Objects.requireNonNull(qSExpansionPathInterpolator);
            PathInterpolatorBuilder pathInterpolatorBuilder = qSExpansionPathInterpolator.pathInterpolatorBuilder;
            Objects.requireNonNull(pathInterpolatorBuilder);
            builder10.mInterpolator = new PathInterpolatorBuilder.PathInterpolator(pathInterpolatorBuilder.mDist, pathInterpolatorBuilder.mX);
            if (this.mOnFirstPage) {
                this.mQQSTranslationYAnimator = builder9.build();
            }
            this.mTranslationYAnimator = builder8.build();
            this.mTranslationXAnimator = builder10.build();
            HeightExpansionAnimator heightExpansionAnimator3 = this.mQQSTileHeightAnimator;
            if (heightExpansionAnimator3 != null) {
                heightExpansionAnimator3.mAnimator.setInterpolator(this.mQSExpansionPathInterpolator.getYInterpolator());
            }
            HeightExpansionAnimator heightExpansionAnimator4 = this.mOtherFirstPageTilesHeightAnimator;
            if (heightExpansionAnimator4 != null) {
                heightExpansionAnimator4.mAnimator.setInterpolator(this.mQSExpansionPathInterpolator.getYInterpolator());
            }
        } else {
            qSTileLayout2 = qSTileLayout;
        }
        builder.addFloat(this.mQuickQsPanel, str2, 1.0f, 0.0f);
        builder.addFloat(qSTileLayout2, str2, 0.0f, 1.0f);
        builder.mListener = this.mNonFirstPageListener;
        builder.mEndDelay = 0.9f;
        this.mNonfirstPageAlphaAnimator = builder.build();
    }

    public final void updateQQSVisibility() {
        int i;
        QuickQSPanel quickQSPanel = this.mQuickQsPanel;
        if (!this.mOnKeyguard || this.mShowCollapsedOnKeyguard) {
            i = 0;
        } else {
            i = 4;
        }
        quickQSPanel.setVisibility(i);
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.systemui.qs.QSAnimator$1] */
    public QSAnimator(QS qs, QuickQSPanel quickQSPanel, QuickStatusBarHeader quickStatusBarHeader, QSPanelController qSPanelController, QuickQSPanelController quickQSPanelController, QSTileHost qSTileHost, QSFgsManagerFooter qSFgsManagerFooter, QSSecurityFooter qSSecurityFooter, Executor executor, TunerService tunerService, QSExpansionPathInterpolator qSExpansionPathInterpolator) {
        boolean z = true;
        this.mQs = qs;
        this.mQuickQsPanel = quickQSPanel;
        this.mQsPanelController = qSPanelController;
        this.mQuickQSPanelController = quickQSPanelController;
        this.mQuickStatusBarHeader = quickStatusBarHeader;
        this.mFgsManagerFooter = qSFgsManagerFooter;
        this.mSecurityFooter = qSSecurityFooter;
        this.mHost = qSTileHost;
        this.mExecutor = executor;
        this.mTunerService = tunerService;
        this.mQSExpansionPathInterpolator = qSExpansionPathInterpolator;
        Objects.requireNonNull(qSTileHost);
        qSTileHost.mCallbacks.add(this);
        Objects.requireNonNull(qSPanelController);
        T t = qSPanelController.mView;
        if (t != 0) {
            t.addOnAttachStateChangeListener(this);
        }
        qs.getView().addOnLayoutChangeListener(this);
        T t2 = qSPanelController.mView;
        if ((t2 == 0 || !t2.isAttachedToWindow()) ? false : z) {
            onViewAttachedToWindow(null);
        }
        QSPanel.QSTileLayout tileLayout = qSPanelController.getTileLayout();
        if (tileLayout instanceof PagedTileLayout) {
            this.mPagedLayout = (PagedTileLayout) tileLayout;
        } else {
            Log.w("QSAnimator", "QS Not using page layout");
        }
        QSPanel qSPanel = (QSPanel) qSPanelController.mView;
        Objects.requireNonNull(qSPanel);
        QSPanel.QSTileLayout qSTileLayout = qSPanel.mTileLayout;
        if (qSTileLayout instanceof PagedTileLayout) {
            PagedTileLayout pagedTileLayout = (PagedTileLayout) qSTileLayout;
            Objects.requireNonNull(pagedTileLayout);
            pagedTileLayout.mPageListener = this;
        }
    }

    public static void getRelativePosition(int[] iArr, View view, View view2) {
        iArr[0] = (view.getWidth() / 2) + 0;
        iArr[1] = 0;
        getRelativePositionInt(iArr, view, view2);
    }

    @Override // com.android.systemui.qs.TouchAnimator.Listener
    public final void onAnimationStarted() {
        updateQQSVisibility();
        if (this.mOnFirstPage) {
            int size = this.mAnimatedQsViews.size();
            for (int i = 0; i < size; i++) {
                this.mAnimatedQsViews.get(i).setVisibility(4);
            }
        }
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        if ("sysui_qs_fancy_anim".equals(str)) {
            boolean parseIntegerSwitch = TunerService.parseIntegerSwitch(str2, true);
            this.mAllowFancy = parseIntegerSwitch;
            if (!parseIntegerSwitch) {
                clearAnimationState();
            }
        } else if ("sysui_qs_move_whole_rows".equals(str)) {
            this.mFullRows = TunerService.parseIntegerSwitch(str2, true);
        }
        updateAnimators();
    }

    public final void translateContent(View view, View view2, View view3, int i, int i2, int[] iArr, TouchAnimator.Builder builder, TouchAnimator.Builder builder2, TouchAnimator.Builder builder3) {
        getRelativePosition(iArr, view, view3);
        int i3 = iArr[0];
        int i4 = iArr[1];
        getRelativePosition(iArr, view2, view3);
        int i5 = iArr[0];
        int i6 = iArr[1];
        int i7 = (i5 - i3) - i;
        builder.addFloat(view, "translationX", 0.0f, i7);
        builder.addFloat(view2, "translationX", -i7, 0.0f);
        int i8 = (i6 - i4) - i2;
        builder3.addFloat(view, "translationY", 0.0f, i8);
        builder2.addFloat(view2, "translationY", -i8, 0.0f);
        this.mAllViews.add(view);
        this.mAllViews.add(view2);
    }
}
