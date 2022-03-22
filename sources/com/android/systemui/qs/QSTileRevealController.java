package com.android.systemui.qs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Handler;
import android.util.ArraySet;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.QSPanelControllerBase;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda1;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class QSTileRevealController {
    public final Context mContext;
    public final PagedTileLayout mPagedTileLayout;
    public final QSPanelController mQSPanelController;
    public final QSCustomizerController mQsCustomizerController;
    public final ArraySet<String> mTilesToReveal = new ArraySet<>();
    public final Handler mHandler = new Handler();
    public final AnonymousClass1 mRevealQsTiles = new AnonymousClass1();

    /* renamed from: com.android.systemui.qs.QSTileRevealController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        public AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            QSTileRevealController qSTileRevealController = QSTileRevealController.this;
            final PagedTileLayout pagedTileLayout = qSTileRevealController.mPagedTileLayout;
            ArraySet<String> arraySet = qSTileRevealController.mTilesToReveal;
            final SuggestController$$ExternalSyntheticLambda1 suggestController$$ExternalSyntheticLambda1 = new SuggestController$$ExternalSyntheticLambda1(this, 1);
            Objects.requireNonNull(pagedTileLayout);
            if (!arraySet.isEmpty() && pagedTileLayout.mPages.size() >= 2 && pagedTileLayout.getScrollX() == 0 && pagedTileLayout.beginFakeDrag()) {
                int size = pagedTileLayout.mPages.size() - 1;
                ArrayList arrayList = new ArrayList();
                Iterator<QSPanelControllerBase.TileRecord> it = pagedTileLayout.mPages.get(size).mRecords.iterator();
                while (it.hasNext()) {
                    QSPanelControllerBase.TileRecord next = it.next();
                    if (arraySet.contains(next.tile.getTileSpec())) {
                        QSTileView qSTileView = next.tileView;
                        int size2 = arrayList.size();
                        qSTileView.setAlpha(0.0f);
                        qSTileView.setScaleX(0.0f);
                        qSTileView.setScaleY(0.0f);
                        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(qSTileView, PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f), PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f), PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f));
                        ofPropertyValuesHolder.setDuration(450L);
                        ofPropertyValuesHolder.setStartDelay(size2 * 85);
                        ofPropertyValuesHolder.setInterpolator(new OvershootInterpolator(1.3f));
                        arrayList.add(ofPropertyValuesHolder);
                    }
                }
                if (arrayList.isEmpty()) {
                    pagedTileLayout.endFakeDrag();
                    return;
                }
                AnimatorSet animatorSet = new AnimatorSet();
                pagedTileLayout.mBounceAnimatorSet = animatorSet;
                animatorSet.playTogether(arrayList);
                pagedTileLayout.mBounceAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.PagedTileLayout.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        pagedTileLayout.mBounceAnimatorSet = null;
                        suggestController$$ExternalSyntheticLambda1.run();
                    }
                });
                pagedTileLayout.setOffscreenPageLimit(size);
                int width = pagedTileLayout.getWidth() * size;
                Scroller scroller = pagedTileLayout.mScroller;
                int scrollX = pagedTileLayout.getScrollX();
                int scrollY = pagedTileLayout.getScrollY();
                if (pagedTileLayout.isLayoutRtl()) {
                    width = -width;
                }
                scroller.startScroll(scrollX, scrollY, width, 0, 750);
                pagedTileLayout.postInvalidateOnAnimation();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class Factory {
        public final Context mContext;
        public final QSCustomizerController mQsCustomizerController;

        public Factory(Context context, QSCustomizerController qSCustomizerController) {
            this.mContext = context;
            this.mQsCustomizerController = qSCustomizerController;
        }
    }

    public final void addTileSpecsToRevealed(ArraySet<String> arraySet) {
        Context context = this.mContext;
        ArraySet arraySet2 = new ArraySet(context.getSharedPreferences(context.getPackageName(), 0).getStringSet("QsTileSpecsRevealed", Collections.EMPTY_SET));
        arraySet2.addAll((ArraySet) arraySet);
        Context context2 = this.mContext;
        context2.getSharedPreferences(context2.getPackageName(), 0).edit().putStringSet("QsTileSpecsRevealed", arraySet2).apply();
    }

    public QSTileRevealController(Context context, QSPanelController qSPanelController, PagedTileLayout pagedTileLayout, QSCustomizerController qSCustomizerController) {
        this.mContext = context;
        this.mQSPanelController = qSPanelController;
        this.mPagedTileLayout = pagedTileLayout;
        this.mQsCustomizerController = qSCustomizerController;
    }
}
