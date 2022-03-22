package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.RemeasuringLinearLayout;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSPanelControllerBase;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Utils;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public class QSPanel extends LinearLayout implements TunerService.Tunable {
    public static final /* synthetic */ int $r8$clinit = 0;
    public View mBrightnessView;
    public int mContentMarginEnd;
    public final Context mContext;
    public boolean mExpanded;
    public View mFgsManagerFooter;
    public View mFooter;
    public PageIndicator mFooterPageIndicator;
    public ViewGroup mHeaderContainer;
    public RemeasuringLinearLayout mHorizontalContentContainer;
    public RemeasuringLinearLayout mHorizontalLinearLayout;
    public boolean mListening;
    public View mSecurityFooter;
    public QSTileLayout mTileLayout;
    public boolean mUsingHorizontalLayout;
    public boolean mUsingMediaPlayer;
    public final ArrayList mOnConfigurationChangedListeners = new ArrayList();
    public float mSquishinessFraction = 1.0f;
    public final ArrayMap<View, Integer> mChildrenLayoutTop = new ArrayMap<>();
    public final Rect mClippingRect = new Rect();
    public boolean mUseNewFooter = false;
    public final int mMediaTotalBottomMargin = getResources().getDimensionPixelSize(2131166923);
    public final int mMediaTopMargin = getResources().getDimensionPixelSize(2131166914);
    public int mMovableContentStartIndex = getChildCount();

    /* loaded from: classes.dex */
    public class H extends Handler {
        public H() {
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            if (message.what == 1) {
                QSPanel.this.announceForAccessibility((CharSequence) message.obj);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface OnConfigurationChangedListener {
        void onConfigurationChange(Configuration configuration);
    }

    /* loaded from: classes.dex */
    public interface QSTileLayout {
        void addTile(QSPanelControllerBase.TileRecord tileRecord);

        int getHeight();

        int getNumVisibleTiles();

        int getTilesHeight();

        void removeTile(QSPanelControllerBase.TileRecord tileRecord);

        default void restoreInstanceState(Bundle bundle) {
        }

        default void saveInstanceState(Bundle bundle) {
        }

        default void setExpansion(float f, float f2) {
        }

        void setListening(boolean z, UiEventLogger uiEventLogger);

        default boolean setMaxColumns(int i) {
            return false;
        }

        default boolean setMinRows(int i) {
            return false;
        }

        void setSquishinessFraction(float f);

        boolean updateResources();
    }

    public String getDumpableTag() {
        return "QSPanel";
    }

    public static void switchToParent(View view, ViewGroup viewGroup, int i, String str) {
        if (viewGroup == null) {
            Log.w(str, "Trying to move view to null parent", new IllegalStateException());
            return;
        }
        ViewGroup viewGroup2 = (ViewGroup) view.getParent();
        if (viewGroup2 != viewGroup) {
            if (viewGroup2 != null) {
                viewGroup2.removeView(view);
            }
            viewGroup.addView(view, i);
        } else if (viewGroup.indexOfChild(view) != i) {
            viewGroup.removeView(view);
            viewGroup.addView(view, i);
        }
    }

    public void drawTile(QSPanelControllerBase.TileRecord tileRecord, QSTile.State state) {
        tileRecord.tileView.onStateChanged(state);
    }

    public QSTileLayout getOrCreateTileLayout() {
        if (this.mTileLayout == null) {
            QSTileLayout qSTileLayout = (QSTileLayout) LayoutInflater.from(this.mContext).inflate(2131624429, (ViewGroup) this, false);
            this.mTileLayout = qSTileLayout;
            qSTileLayout.setSquishinessFraction(this.mSquishinessFraction);
        }
        return this.mTileLayout;
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        QSTileLayout qSTileLayout = this.mTileLayout;
        if (qSTileLayout instanceof PagedTileLayout) {
            PageIndicator pageIndicator = this.mFooterPageIndicator;
            if (pageIndicator != null) {
                pageIndicator.setNumPages(((PagedTileLayout) qSTileLayout).getNumPages());
            }
            if (((View) this.mTileLayout).getParent() == this) {
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(10000, 1073741824);
                PagedTileLayout pagedTileLayout = (PagedTileLayout) this.mTileLayout;
                Objects.requireNonNull(pagedTileLayout);
                pagedTileLayout.mExcessHeight = 10000 - View.MeasureSpec.getSize(i2);
                i2 = makeMeasureSpec;
            }
        }
        super.onMeasure(i, i2);
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8) {
                int measuredHeight = childAt.getMeasuredHeight() + paddingTop;
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                paddingTop = marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + measuredHeight;
            }
        }
        setMeasuredDimension(getMeasuredWidth(), paddingTop);
    }

    public final void setBrightnessViewMargin() {
        View view = this.mBrightnessView;
        if (view != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            marginLayoutParams.topMargin = this.mContext.getResources().getDimensionPixelSize(2131166845);
            marginLayoutParams.bottomMargin = this.mContext.getResources().getDimensionPixelSize(2131166844);
            this.mBrightnessView.setLayoutParams(marginLayoutParams);
        }
    }

    public void setHorizontalContentContainerClipping() {
        this.mHorizontalContentContainer.setClipChildren(true);
        this.mHorizontalContentContainer.setClipToPadding(false);
        this.mHorizontalContentContainer.addOnLayoutChangeListener(new QSPanel$$ExternalSyntheticLambda0(this, 0));
        Rect rect = this.mClippingRect;
        rect.top = -1000;
        this.mHorizontalContentContainer.setClipBounds(rect);
    }

    public final void switchSecurityFooter(boolean z) {
        int i;
        ViewGroup viewGroup;
        if (this.mSecurityFooter != null) {
            if (z || this.mContext.getResources().getConfiguration().orientation != 2 || (viewGroup = this.mHeaderContainer) == null) {
                View view = this.mFgsManagerFooter;
                if (view != null) {
                    i = indexOfChild(view);
                } else {
                    i = indexOfChild(this.mFooter);
                }
                switchToParent(this.mSecurityFooter, this, i + 1, getDumpableTag());
                return;
            }
            switchToParent(this.mSecurityFooter, viewGroup, 0, getDumpableTag());
        }
    }

    public final void updateMediaHostContentMargins(UniqueObjectHostView uniqueObjectHostView) {
        int i;
        if (this.mUsingMediaPlayer) {
            if (this.mUsingHorizontalLayout) {
                i = this.mContentMarginEnd;
            } else {
                i = 0;
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) uniqueObjectHostView.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.setMarginStart(0);
                layoutParams.setMarginEnd(i);
                uniqueObjectHostView.setLayoutParams(layoutParams);
            }
        }
    }

    public void updatePadding() {
        int i;
        Resources resources = this.mContext.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(2131166900);
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        if (this.mUseNewFooter) {
            i = resources.getDimensionPixelSize(2131166899);
        } else {
            i = 0;
        }
        setPaddingRelative(paddingStart, dimensionPixelSize, paddingEnd, i);
    }

    public final void updatePageIndicator() {
        PageIndicator pageIndicator;
        if ((this.mTileLayout instanceof PagedTileLayout) && (pageIndicator = this.mFooterPageIndicator) != null) {
            pageIndicator.setVisibility(8);
            PagedTileLayout pagedTileLayout = (PagedTileLayout) this.mTileLayout;
            PageIndicator pageIndicator2 = this.mFooterPageIndicator;
            Objects.requireNonNull(pagedTileLayout);
            pagedTileLayout.mPageIndicator = pageIndicator2;
            pageIndicator2.setNumPages(pagedTileLayout.mPages.size());
            pagedTileLayout.mPageIndicator.setLocation(pagedTileLayout.mPageIndicatorPosition);
        }
    }

    public final void updateViewPositions() {
        int tilesHeight = this.mTileLayout.getTilesHeight() - this.mTileLayout.getHeight();
        boolean z = false;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (z) {
                Integer num = this.mChildrenLayoutTop.get(childAt);
                Objects.requireNonNull(num);
                int intValue = num.intValue() + tilesHeight;
                childAt.setLeftTopRightBottom(childAt.getLeft(), intValue, childAt.getRight(), childAt.getHeight() + intValue);
            }
            if (childAt == this.mTileLayout) {
                z = true;
            }
        }
    }

    public QSPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        new H();
        this.mUsingMediaPlayer = Utils.useQsMediaPlayer(context);
        this.mContext = context;
        setOrientation(1);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mOnConfigurationChangedListeners.forEach(new QSPanel$$ExternalSyntheticLambda1(configuration, 0));
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mFooter = findViewById(2131428649);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            this.mChildrenLayoutTop.put(childAt, Integer.valueOf(childAt.getTop()));
        }
        updateViewPositions();
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        View view;
        int i;
        if ("qs_show_brightness".equals(str) && (view = this.mBrightnessView) != null) {
            if (TunerService.parseIntegerSwitch(str2, true)) {
                i = 0;
            } else {
                i = 8;
            }
            view.setVisibility(i);
        }
    }

    public final void updateResources() {
        updatePadding();
        updatePageIndicator();
        setBrightnessViewMargin();
        QSTileLayout qSTileLayout = this.mTileLayout;
        if (qSTileLayout != null) {
            qSTileLayout.updateResources();
        }
    }

    public QSEvent closePanelEvent() {
        return QSEvent.QS_PANEL_COLLAPSED;
    }

    public QSEvent openPanelEvent() {
        return QSEvent.QS_PANEL_EXPANDED;
    }
}
