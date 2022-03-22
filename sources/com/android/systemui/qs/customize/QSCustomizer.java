package com.android.systemui.qs.customize;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.qs.QSContainerController;
import com.android.systemui.qs.QSDetailClipper;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.util.Utils;
import java.util.Objects;
/* loaded from: classes.dex */
public class QSCustomizer extends LinearLayout {
    public boolean isShown;
    public boolean mCustomizing;
    public boolean mIsShowingNavBackdrop;
    public boolean mOpening;
    public QS mQs;
    public QSContainerController mQsContainerController;
    public final RecyclerView mRecyclerView;
    public int mX;
    public int mY;
    public final AnonymousClass1 mCollapseAnimationListener = new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.customize.QSCustomizer.1
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
            QSCustomizer qSCustomizer = QSCustomizer.this;
            if (!qSCustomizer.isShown) {
                qSCustomizer.setVisibility(8);
            }
            QSCustomizer.this.mQsContainerController.setCustomizerAnimating(false);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            QSCustomizer qSCustomizer = QSCustomizer.this;
            if (!qSCustomizer.isShown) {
                qSCustomizer.setVisibility(8);
            }
            QSCustomizer.this.mQsContainerController.setCustomizerAnimating(false);
        }
    };
    public final QSDetailClipper mClipper = new QSDetailClipper(findViewById(2131427793));
    public final View mTransparentView = findViewById(2131427794);

    /* loaded from: classes.dex */
    public class ExpandAnimatorListener extends AnimatorListenerAdapter {
        public final TileAdapter mTileAdapter;

        public ExpandAnimatorListener(TileAdapter tileAdapter) {
            this.mTileAdapter = tileAdapter;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
            QSCustomizer qSCustomizer = QSCustomizer.this;
            qSCustomizer.mOpening = false;
            qSCustomizer.mQs.notifyCustomizeChanged();
            QSCustomizer.this.mQsContainerController.setCustomizerAnimating(false);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            QSCustomizer qSCustomizer = QSCustomizer.this;
            if (qSCustomizer.isShown) {
                qSCustomizer.mCustomizing = true;
                qSCustomizer.mQs.notifyCustomizeChanged();
            }
            QSCustomizer qSCustomizer2 = QSCustomizer.this;
            qSCustomizer2.mOpening = false;
            qSCustomizer2.mQsContainerController.setCustomizerAnimating(false);
            QSCustomizer.this.mRecyclerView.setAdapter(this.mTileAdapter);
        }
    }

    public final boolean isCustomizing() {
        if (this.mCustomizing || this.mOpening) {
            return true;
        }
        return false;
    }

    public final void updateNavColors(LightBarController lightBarController) {
        boolean z;
        if (!this.mIsShowingNavBackdrop || !this.isShown) {
            z = false;
        } else {
            z = true;
        }
        Objects.requireNonNull(lightBarController);
        if (lightBarController.mQsCustomizing != z) {
            lightBarController.mQsCustomizing = z;
            lightBarController.reevaluate();
        }
    }

    public final void updateResources() {
        int i;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mTransparentView.getLayoutParams();
        Context context = ((LinearLayout) this).mContext;
        Resources resources = context.getResources();
        if (Utils.shouldUseSplitNotificationShade(resources)) {
            i = resources.getDimensionPixelSize(2131166872);
        } else {
            i = SystemBarUtils.getQuickQsOffsetHeight(context);
        }
        layoutParams.height = i;
        this.mTransparentView.setLayoutParams(layoutParams);
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [com.android.systemui.qs.customize.QSCustomizer$1] */
    public QSCustomizer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(getContext()).inflate(2131624424, this);
        Toolbar toolbar = (Toolbar) findViewById(16908729);
        TypedValue typedValue = new TypedValue();
        ((LinearLayout) this).mContext.getTheme().resolveAttribute(16843531, typedValue, true);
        toolbar.setNavigationIcon(getResources().getDrawable(typedValue.resourceId, ((LinearLayout) this).mContext.getTheme()));
        toolbar.getMenu().add(0, 1, 0, ((LinearLayout) this).mContext.getString(17041371));
        toolbar.setTitle(2131953048);
        RecyclerView recyclerView = (RecyclerView) findViewById(16908298);
        this.mRecyclerView = recyclerView;
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.mMoveDuration = 150L;
        Objects.requireNonNull(recyclerView);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.mItemAnimator;
        if (itemAnimator != null) {
            itemAnimator.endAnimations();
            RecyclerView.ItemAnimator itemAnimator2 = recyclerView.mItemAnimator;
            Objects.requireNonNull(itemAnimator2);
            itemAnimator2.mListener = null;
        }
        recyclerView.mItemAnimator = defaultItemAnimator;
        defaultItemAnimator.mListener = recyclerView.mItemAnimatorListener;
    }

    public final void updateNavBackDrop(Configuration configuration, LightBarController lightBarController) {
        boolean z;
        View findViewById = findViewById(2131428477);
        int i = 0;
        if (configuration.smallestScreenWidthDp >= 600 || configuration.orientation != 2) {
            z = true;
        } else {
            z = false;
        }
        this.mIsShowingNavBackdrop = z;
        if (findViewById != null) {
            if (!z) {
                i = 8;
            }
            findViewById.setVisibility(i);
        }
        updateNavColors(lightBarController);
    }

    @Override // android.view.View
    public final boolean isShown() {
        return this.isShown;
    }
}
