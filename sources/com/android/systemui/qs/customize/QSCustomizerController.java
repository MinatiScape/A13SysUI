package com.android.systemui.qs.customize;

import android.animation.Animator;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSDetailClipper;
import com.android.systemui.qs.QSEditEvent;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.customize.QSCustomizer;
import com.android.systemui.qs.customize.TileAdapter;
import com.android.systemui.qs.customize.TileQueryHelper;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda4;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class QSCustomizerController extends ViewController<QSCustomizer> {
    public final ConfigurationController mConfigurationController;
    public final KeyguardStateController mKeyguardStateController;
    public final LightBarController mLightBarController;
    public final QSTileHost mQsTileHost;
    public final ScreenLifecycle mScreenLifecycle;
    public final TileAdapter mTileAdapter;
    public final TileQueryHelper mTileQueryHelper;
    public final Toolbar mToolbar;
    public final UiEventLogger mUiEventLogger;
    public final AnonymousClass1 mOnMenuItemClickListener = new Toolbar.OnMenuItemClickListener() { // from class: com.android.systemui.qs.customize.QSCustomizerController.1
        @Override // android.widget.Toolbar.OnMenuItemClickListener
        public final boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() != 1) {
                return false;
            }
            QSCustomizerController.this.mUiEventLogger.log(QSEditEvent.QS_EDIT_RESET);
            QSCustomizerController qSCustomizerController = QSCustomizerController.this;
            Objects.requireNonNull(qSCustomizerController);
            TileAdapter tileAdapter = qSCustomizerController.mTileAdapter;
            ArrayList defaultSpecs = QSTileHost.getDefaultSpecs(qSCustomizerController.getContext());
            Objects.requireNonNull(tileAdapter);
            tileAdapter.mHost.changeTiles(tileAdapter.mCurrentSpecs, defaultSpecs);
            if (defaultSpecs.equals(tileAdapter.mCurrentSpecs)) {
                return false;
            }
            tileAdapter.mCurrentSpecs = defaultSpecs;
            tileAdapter.recalcSpecs();
            return false;
        }
    };
    public final AnonymousClass2 mKeyguardCallback = new KeyguardStateController.Callback() { // from class: com.android.systemui.qs.customize.QSCustomizerController.2
        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onKeyguardShowingChanged() {
            if (((QSCustomizer) QSCustomizerController.this.mView).isAttachedToWindow() && QSCustomizerController.this.mKeyguardStateController.isShowing()) {
                QSCustomizer qSCustomizer = (QSCustomizer) QSCustomizerController.this.mView;
                Objects.requireNonNull(qSCustomizer);
                if (!qSCustomizer.mOpening) {
                    QSCustomizerController.this.hide();
                }
            }
        }
    };
    public final AnonymousClass3 mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.qs.customize.QSCustomizerController.3
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onConfigChanged(Configuration configuration) {
            boolean z;
            QSCustomizerController qSCustomizerController = QSCustomizerController.this;
            ((QSCustomizer) qSCustomizerController.mView).updateNavBackDrop(configuration, qSCustomizerController.mLightBarController);
            ((QSCustomizer) QSCustomizerController.this.mView).updateResources();
            TileAdapter tileAdapter = QSCustomizerController.this.mTileAdapter;
            Objects.requireNonNull(tileAdapter);
            int integer = tileAdapter.mContext.getResources().getInteger(2131493032);
            if (integer != tileAdapter.mNumColumns) {
                tileAdapter.mNumColumns = integer;
                z = true;
            } else {
                z = false;
            }
            if (z) {
                QSCustomizer qSCustomizer = (QSCustomizer) QSCustomizerController.this.mView;
                Objects.requireNonNull(qSCustomizer);
                RecyclerView recyclerView = qSCustomizer.mRecyclerView;
                Objects.requireNonNull(recyclerView);
                RecyclerView.LayoutManager layoutManager = recyclerView.mLayout;
                if (layoutManager instanceof GridLayoutManager) {
                    TileAdapter tileAdapter2 = QSCustomizerController.this.mTileAdapter;
                    Objects.requireNonNull(tileAdapter2);
                    ((GridLayoutManager) layoutManager).setSpanCount(tileAdapter2.mNumColumns);
                }
            }
        }
    };

    public final void hide() {
        boolean z;
        ScreenLifecycle screenLifecycle = this.mScreenLifecycle;
        Objects.requireNonNull(screenLifecycle);
        if (screenLifecycle.mScreenState != 0) {
            z = true;
        } else {
            z = false;
        }
        if (((QSCustomizer) this.mView).isShown()) {
            this.mUiEventLogger.log(QSEditEvent.QS_EDIT_CLOSED);
            this.mToolbar.dismissPopupMenus();
            QSCustomizer qSCustomizer = (QSCustomizer) this.mView;
            Objects.requireNonNull(qSCustomizer);
            qSCustomizer.mCustomizing = false;
            qSCustomizer.mQs.notifyCustomizeChanged();
            TileQueryHelper tileQueryHelper = this.mTileQueryHelper;
            Objects.requireNonNull(tileQueryHelper);
            if (tileQueryHelper.mFinished) {
                this.mTileAdapter.saveSpecs(this.mQsTileHost);
            }
            QSCustomizer qSCustomizer2 = (QSCustomizer) this.mView;
            Objects.requireNonNull(qSCustomizer2);
            if (qSCustomizer2.isShown) {
                qSCustomizer2.isShown = false;
                QSDetailClipper qSDetailClipper = qSCustomizer2.mClipper;
                Objects.requireNonNull(qSDetailClipper);
                Animator animator = qSDetailClipper.mAnimator;
                if (animator != null) {
                    animator.cancel();
                }
                qSCustomizer2.mOpening = false;
                if (z) {
                    qSCustomizer2.mClipper.animateCircularClip(qSCustomizer2.mX, qSCustomizer2.mY, false, qSCustomizer2.mCollapseAnimationListener);
                } else {
                    qSCustomizer2.setVisibility(8);
                }
                qSCustomizer2.mQsContainerController.setCustomizerAnimating(z);
                qSCustomizer2.mQsContainerController.setCustomizerShowing(false);
            }
            ((QSCustomizer) this.mView).updateNavColors(this.mLightBarController);
            this.mKeyguardStateController.removeCallback(this.mKeyguardCallback);
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        ((QSCustomizer) this.mView).updateNavBackDrop(getResources().getConfiguration(), this.mLightBarController);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        TileQueryHelper tileQueryHelper = this.mTileQueryHelper;
        TileAdapter tileAdapter = this.mTileAdapter;
        Objects.requireNonNull(tileQueryHelper);
        tileQueryHelper.mListener = tileAdapter;
        TileAdapter tileAdapter2 = this.mTileAdapter;
        Objects.requireNonNull(tileAdapter2);
        TileAdapter.MarginTileDecoration marginTileDecoration = tileAdapter2.mMarginDecoration;
        Objects.requireNonNull(marginTileDecoration);
        marginTileDecoration.mHalfMargin = getResources().getDimensionPixelSize(2131166912) / 2;
        QSCustomizer qSCustomizer = (QSCustomizer) this.mView;
        Objects.requireNonNull(qSCustomizer);
        final RecyclerView recyclerView = qSCustomizer.mRecyclerView;
        recyclerView.setAdapter(this.mTileAdapter);
        TileAdapter tileAdapter3 = this.mTileAdapter;
        Objects.requireNonNull(tileAdapter3);
        tileAdapter3.mItemTouchHelper.attachToRecyclerView(recyclerView);
        getContext();
        TileAdapter tileAdapter4 = this.mTileAdapter;
        Objects.requireNonNull(tileAdapter4);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(tileAdapter4.mNumColumns) { // from class: com.android.systemui.qs.customize.QSCustomizerController.4
            @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public final void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            }

            @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
            public final void calculateItemDecorationsForChild(View view, Rect rect) {
                if (!(view instanceof TextView)) {
                    rect.setEmpty();
                    TileAdapter tileAdapter5 = QSCustomizerController.this.mTileAdapter;
                    Objects.requireNonNull(tileAdapter5);
                    tileAdapter5.mMarginDecoration.getItemOffsets(rect, view, recyclerView, new RecyclerView.State());
                    ((ViewGroup.MarginLayoutParams) ((GridLayoutManager.LayoutParams) view.getLayoutParams())).leftMargin = rect.left;
                    ((ViewGroup.MarginLayoutParams) ((GridLayoutManager.LayoutParams) view.getLayoutParams())).rightMargin = rect.right;
                }
            }
        };
        TileAdapter tileAdapter5 = this.mTileAdapter;
        Objects.requireNonNull(tileAdapter5);
        gridLayoutManager.mSpanSizeLookup = tileAdapter5.mSizeLookup;
        recyclerView.setLayoutManager(gridLayoutManager);
        TileAdapter tileAdapter6 = this.mTileAdapter;
        Objects.requireNonNull(tileAdapter6);
        recyclerView.addItemDecoration(tileAdapter6.mDecoration);
        TileAdapter tileAdapter7 = this.mTileAdapter;
        Objects.requireNonNull(tileAdapter7);
        recyclerView.addItemDecoration(tileAdapter7.mMarginDecoration);
        this.mToolbar.setOnMenuItemClickListener(this.mOnMenuItemClickListener);
        this.mToolbar.setNavigationOnClickListener(new BubbleStackView$$ExternalSyntheticLambda4(this, 2));
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        TileQueryHelper tileQueryHelper = this.mTileQueryHelper;
        Objects.requireNonNull(tileQueryHelper);
        tileQueryHelper.mListener = null;
        this.mToolbar.setOnMenuItemClickListener(null);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    public final void show(int i, int i2, boolean z) {
        String[] split;
        QSTile createTile;
        if (!((QSCustomizer) this.mView).isShown()) {
            ArrayList arrayList = new ArrayList();
            QSTileHost qSTileHost = this.mQsTileHost;
            Objects.requireNonNull(qSTileHost);
            for (QSTile qSTile : qSTileHost.mTiles.values()) {
                arrayList.add(qSTile.getTileSpec());
            }
            TileAdapter tileAdapter = this.mTileAdapter;
            Objects.requireNonNull(tileAdapter);
            if (!arrayList.equals(tileAdapter.mCurrentSpecs)) {
                tileAdapter.mCurrentSpecs = arrayList;
                tileAdapter.recalcSpecs();
            }
            if (z) {
                QSCustomizer qSCustomizer = (QSCustomizer) this.mView;
                Objects.requireNonNull(qSCustomizer);
                if (!qSCustomizer.isShown) {
                    RecyclerView recyclerView = qSCustomizer.mRecyclerView;
                    Objects.requireNonNull(recyclerView);
                    recyclerView.mLayout.scrollToPosition(0);
                    qSCustomizer.setVisibility(0);
                    QSDetailClipper qSDetailClipper = qSCustomizer.mClipper;
                    Objects.requireNonNull(qSDetailClipper);
                    Animator animator = qSDetailClipper.mAnimator;
                    if (animator != null) {
                        animator.cancel();
                    }
                    QSDetailClipper qSDetailClipper2 = qSCustomizer.mClipper;
                    Objects.requireNonNull(qSDetailClipper2);
                    qSDetailClipper2.mBackground.showSecondLayer();
                    qSCustomizer.isShown = true;
                    qSCustomizer.mCustomizing = true;
                    qSCustomizer.mQs.notifyCustomizeChanged();
                    qSCustomizer.mQsContainerController.setCustomizerAnimating(false);
                    qSCustomizer.mQsContainerController.setCustomizerShowing(true);
                }
            } else {
                QSCustomizer qSCustomizer2 = (QSCustomizer) this.mView;
                TileAdapter tileAdapter2 = this.mTileAdapter;
                Objects.requireNonNull(qSCustomizer2);
                if (!qSCustomizer2.isShown) {
                    RecyclerView recyclerView2 = qSCustomizer2.mRecyclerView;
                    Objects.requireNonNull(recyclerView2);
                    recyclerView2.mLayout.scrollToPosition(0);
                    int[] locationOnScreen = qSCustomizer2.findViewById(2131427793).getLocationOnScreen();
                    qSCustomizer2.mX = i - locationOnScreen[0];
                    qSCustomizer2.mY = i2 - locationOnScreen[1];
                    qSCustomizer2.isShown = true;
                    qSCustomizer2.mOpening = true;
                    qSCustomizer2.setVisibility(0);
                    qSCustomizer2.mClipper.animateCircularClip(qSCustomizer2.mX, qSCustomizer2.mY, true, new QSCustomizer.ExpandAnimatorListener(tileAdapter2));
                    qSCustomizer2.mQsContainerController.setCustomizerAnimating(true);
                    qSCustomizer2.mQsContainerController.setCustomizerShowing(true);
                }
                this.mUiEventLogger.log(QSEditEvent.QS_EDIT_OPEN);
            }
            TileQueryHelper tileQueryHelper = this.mTileQueryHelper;
            QSTileHost qSTileHost2 = this.mQsTileHost;
            Objects.requireNonNull(tileQueryHelper);
            tileQueryHelper.mTiles.clear();
            tileQueryHelper.mSpecs.clear();
            tileQueryHelper.mFinished = false;
            String string = tileQueryHelper.mContext.getString(2131953141);
            String string2 = Settings.Secure.getString(tileQueryHelper.mContext.getContentResolver(), "sysui_qs_tiles");
            ArrayList arrayList2 = new ArrayList();
            if (string2 != null) {
                arrayList2.addAll(Arrays.asList(string2.split(",")));
            } else {
                string2 = "";
            }
            for (String str : string.split(",")) {
                if (!string2.contains(str)) {
                    arrayList2.add(str);
                }
            }
            if (Build.IS_DEBUGGABLE && !string2.contains("dbg:mem")) {
                arrayList2.add("dbg:mem");
            }
            ArrayList arrayList3 = new ArrayList();
            arrayList2.remove("cell");
            arrayList2.remove("wifi");
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                String str2 = (String) it.next();
                if (!str2.startsWith("custom(") && (createTile = qSTileHost2.createTile(str2)) != null) {
                    if (!createTile.isAvailable()) {
                        createTile.setTileSpec(str2);
                        createTile.destroy();
                    } else {
                        createTile.setTileSpec(str2);
                        arrayList3.add(createTile);
                    }
                }
            }
            TileQueryHelper.TileCollector tileCollector = new TileQueryHelper.TileCollector(arrayList3, qSTileHost2);
            Iterator it2 = tileCollector.mQSTileList.iterator();
            while (it2.hasNext()) {
                TileQueryHelper.TilePair tilePair = (TileQueryHelper.TilePair) it2.next();
                tilePair.mTile.addCallback(tileCollector);
                tilePair.mTile.setListening(tileCollector, true);
                tilePair.mTile.refreshState();
            }
            this.mKeyguardStateController.addCallback(this.mKeyguardCallback);
            ((QSCustomizer) this.mView).updateNavColors(this.mLightBarController);
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.qs.customize.QSCustomizerController$1] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.qs.customize.QSCustomizerController$2] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.qs.customize.QSCustomizerController$3] */
    public QSCustomizerController(QSCustomizer qSCustomizer, TileQueryHelper tileQueryHelper, QSTileHost qSTileHost, TileAdapter tileAdapter, ScreenLifecycle screenLifecycle, KeyguardStateController keyguardStateController, LightBarController lightBarController, ConfigurationController configurationController, UiEventLogger uiEventLogger) {
        super(qSCustomizer);
        this.mTileQueryHelper = tileQueryHelper;
        this.mQsTileHost = qSTileHost;
        this.mTileAdapter = tileAdapter;
        this.mScreenLifecycle = screenLifecycle;
        this.mKeyguardStateController = keyguardStateController;
        this.mLightBarController = lightBarController;
        this.mConfigurationController = configurationController;
        this.mUiEventLogger = uiEventLogger;
        this.mToolbar = (Toolbar) qSCustomizer.findViewById(16908729);
    }
}
