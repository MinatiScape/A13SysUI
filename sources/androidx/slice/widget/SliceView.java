package androidx.slice.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import androidx.lifecycle.Observer;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.SliceMetadata;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import okio.Okio;
/* loaded from: classes.dex */
public class SliceView extends ViewGroup implements Observer<Slice>, View.OnClickListener {
    public static final AnonymousClass3 SLICE_ACTION_PRIORITY_COMPARATOR = new Comparator<SliceAction>() { // from class: androidx.slice.widget.SliceView.3
        @Override // java.util.Comparator
        public final int compare(SliceAction sliceAction, SliceAction sliceAction2) {
            int priority = sliceAction.getPriority();
            int priority2 = sliceAction2.getPriority();
            if (priority < 0 && priority2 < 0) {
                return 0;
            }
            if (priority >= 0) {
                if (priority2 >= 0) {
                    if (priority2 >= priority) {
                        if (priority2 <= priority) {
                            return 0;
                        }
                    }
                }
                return -1;
            }
            return 1;
        }
    };
    public ActionRow mActionRow;
    public ArrayList mActions;
    public Slice mCurrentSlice;
    public SliceMetricsWrapper mCurrentSliceMetrics;
    public TemplateView mCurrentView;
    public int mDownX;
    public int mDownY;
    public Handler mHandler;
    public boolean mInLongpress;
    public ListContent mListContent;
    public View.OnLongClickListener mLongClickListener;
    public View.OnClickListener mOnClickListener;
    public boolean mPressing;
    public SliceMetadata mSliceMetadata;
    public SliceStyle mSliceStyle;
    public int mThemeTintColor;
    public int mTouchSlopSquared;
    public boolean mShowLastUpdated = true;
    public boolean mCurrentSliceLoggedVisible = false;
    public boolean mShowTitleItems = false;
    public boolean mShowHeaderDivider = false;
    public boolean mShowActionDividers = false;
    public AnonymousClass1 mLongpressCheck = new Runnable() { // from class: androidx.slice.widget.SliceView.1
        @Override // java.lang.Runnable
        public final void run() {
            View.OnLongClickListener onLongClickListener;
            SliceView sliceView = SliceView.this;
            if (sliceView.mPressing && (onLongClickListener = sliceView.mLongClickListener) != null) {
                sliceView.mInLongpress = true;
                onLongClickListener.onLongClick(sliceView);
                SliceView.this.performHapticFeedback(0);
            }
        }
    };
    public AnonymousClass2 mRefreshLastUpdated = new Runnable() { // from class: androidx.slice.widget.SliceView.2
        @Override // java.lang.Runnable
        public final void run() {
            SliceMetadata sliceMetadata = SliceView.this.mSliceMetadata;
            if (sliceMetadata != null && sliceMetadata.isExpired()) {
                SliceView.this.mCurrentView.setShowLastUpdated(true);
                SliceView sliceView = SliceView.this;
                TemplateView templateView = sliceView.mCurrentView;
                ListContent listContent = sliceView.mListContent;
                Objects.requireNonNull(templateView);
                templateView.mListContent = listContent;
                listContent.getHeight(templateView.mSliceStyle, templateView.mViewPolicy);
                templateView.updateDisplayedItems();
            }
            SliceView.this.mHandler.postDelayed(this, 60000L);
        }
    };
    public int mMinTemplateHeight = getContext().getResources().getDimensionPixelSize(2131165263);
    public int mLargeHeight = getResources().getDimensionPixelSize(2131165259);
    public int mActionRowHeight = getResources().getDimensionPixelSize(2131165245);
    public SliceViewPolicy mViewPolicy = new SliceViewPolicy();

    /* loaded from: classes.dex */
    public interface OnSliceActionListener {
        void onSliceAction();
    }

    public final int getTintColor() {
        int i = this.mThemeTintColor;
        if (i != -1) {
            return i;
        }
        SliceItem findSubtype = SliceQuery.findSubtype(this.mCurrentSlice, "int", "color");
        if (findSubtype != null) {
            return findSubtype.getInt();
        }
        return SliceViewUtil.getColorAttr(getContext(), 16843829);
    }

    public final void logSliceMetricsVisibilityChange(boolean z) {
        SliceMetricsWrapper sliceMetricsWrapper = this.mCurrentSliceMetrics;
        if (sliceMetricsWrapper != null) {
            if (z && !this.mCurrentSliceLoggedVisible) {
                sliceMetricsWrapper.mSliceMetrics.logVisible();
                this.mCurrentSliceLoggedVisible = true;
            }
            if (!z && this.mCurrentSliceLoggedVisible) {
                SliceMetricsWrapper sliceMetricsWrapper2 = this.mCurrentSliceMetrics;
                Objects.requireNonNull(sliceMetricsWrapper2);
                sliceMetricsWrapper2.mSliceMetrics.logHidden();
                this.mCurrentSliceLoggedVisible = false;
            }
        }
    }

    @Override // androidx.lifecycle.Observer
    public final void onChanged(Slice slice) {
        boolean z;
        SliceMetadata sliceMetadata;
        ListContent listContent;
        RowContent rowContent;
        View view;
        Slice slice2 = slice;
        View findFocus = findFocus();
        if (findFocus != null) {
            new LocationBasedViewTracker(this, findFocus, LocationBasedViewTracker.INPUT_FOCUS);
        }
        boolean z2 = false;
        if (((AccessibilityManager) getContext().getSystemService("accessibility")).isTouchExplorationEnabled()) {
            ArrayList<View> arrayList = new ArrayList<>();
            addFocusables(arrayList, 2, 0);
            Iterator<View> it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    view = null;
                    break;
                }
                view = it.next();
                if (view.isAccessibilityFocused()) {
                    break;
                }
            }
            if (view != null) {
                new LocationBasedViewTracker(this, view, LocationBasedViewTracker.A11Y_FOCUS);
            }
        }
        if (slice2 == null || slice2.getUri() == null) {
            logSliceMetricsVisibilityChange(false);
            this.mCurrentSliceMetrics = null;
        } else {
            Slice slice3 = this.mCurrentSlice;
            if (slice3 == null || !slice3.getUri().equals(slice2.getUri())) {
                logSliceMetricsVisibilityChange(false);
                this.mCurrentSliceMetrics = new SliceMetricsWrapper(getContext(), slice2.getUri());
            }
        }
        if (slice2 == null || this.mCurrentSlice == null || !slice2.getUri().equals(this.mCurrentSlice.getUri())) {
            z = false;
        } else {
            z = true;
        }
        SliceMetadata sliceMetadata2 = this.mSliceMetadata;
        this.mCurrentSlice = slice2;
        if (slice2 != null) {
            sliceMetadata = new SliceMetadata(getContext(), this.mCurrentSlice);
        } else {
            sliceMetadata = null;
        }
        this.mSliceMetadata = sliceMetadata;
        if (!z) {
            this.mCurrentView.resetView();
        } else if (sliceMetadata2.getLoadingState() == 2 && sliceMetadata.getLoadingState() == 0) {
            return;
        }
        SliceMetadata sliceMetadata3 = this.mSliceMetadata;
        if (sliceMetadata3 != null) {
            listContent = sliceMetadata3.mListContent;
        } else {
            listContent = null;
        }
        this.mListContent = listContent;
        if (this.mShowTitleItems) {
            this.mShowTitleItems = true;
            if (!(listContent == null || (rowContent = listContent.mHeaderContent) == null)) {
                rowContent.mShowTitleItems = true;
            }
        }
        if (this.mShowHeaderDivider) {
            this.mShowHeaderDivider = true;
            if (!(listContent == null || listContent.mHeaderContent == null || listContent.mRowItems.size() <= 1)) {
                RowContent rowContent2 = listContent.mHeaderContent;
                Objects.requireNonNull(rowContent2);
                rowContent2.mShowBottomDivider = true;
            }
        }
        if (this.mShowActionDividers) {
            this.mShowActionDividers = true;
            ListContent listContent2 = this.mListContent;
            if (listContent2 != null) {
                Iterator<SliceContent> it2 = listContent2.mRowItems.iterator();
                while (it2.hasNext()) {
                    SliceContent next = it2.next();
                    if (next instanceof RowContent) {
                        RowContent rowContent3 = (RowContent) next;
                        Objects.requireNonNull(rowContent3);
                        rowContent3.mShowActionDivider = true;
                    }
                }
            }
        }
        ListContent listContent3 = this.mListContent;
        if (listContent3 == null || !listContent3.isValid()) {
            this.mActions = null;
            this.mCurrentView.resetView();
            updateActions();
            return;
        }
        this.mCurrentView.setLoadingActions(null);
        SliceMetadata sliceMetadata4 = this.mSliceMetadata;
        Objects.requireNonNull(sliceMetadata4);
        this.mActions = sliceMetadata4.mSliceActions;
        TemplateView templateView = this.mCurrentView;
        SliceMetadata sliceMetadata5 = this.mSliceMetadata;
        Objects.requireNonNull(sliceMetadata5);
        templateView.setLastUpdated(sliceMetadata5.mLastUpdated);
        TemplateView templateView2 = this.mCurrentView;
        if (this.mShowLastUpdated && this.mSliceMetadata.isExpired()) {
            z2 = true;
        }
        templateView2.setShowLastUpdated(z2);
        TemplateView templateView3 = this.mCurrentView;
        SliceMetadata sliceMetadata6 = this.mSliceMetadata;
        Objects.requireNonNull(sliceMetadata6);
        Slice slice4 = sliceMetadata6.mSlice;
        Objects.requireNonNull(slice4);
        templateView3.setAllowTwoLines(Okio.contains(slice4.mHints, "permission_request"));
        this.mCurrentView.setTint(getTintColor());
        if (this.mListContent.getLayoutDir() != -1) {
            this.mCurrentView.setLayoutDirection(this.mListContent.getLayoutDir());
        } else {
            this.mCurrentView.setLayoutDirection(2);
        }
        TemplateView templateView4 = this.mCurrentView;
        ListContent listContent4 = this.mListContent;
        Objects.requireNonNull(templateView4);
        templateView4.mListContent = listContent4;
        listContent4.getHeight(templateView4.mSliceStyle, templateView4.mViewPolicy);
        templateView4.updateDisplayedItems();
        updateActions();
        logSliceMetricsVisibilityChange(true);
        refreshLastUpdatedLabel(true);
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        boolean z;
        ListContent listContent = this.mListContent;
        if (listContent == null || listContent.getShortcut(getContext()) == null) {
            View.OnClickListener onClickListener = this.mOnClickListener;
            if (onClickListener != null) {
                onClickListener.onClick(this);
                return;
            }
            return;
        }
        try {
            SliceActionImpl sliceActionImpl = (SliceActionImpl) this.mListContent.getShortcut(getContext());
            Objects.requireNonNull(sliceActionImpl);
            SliceItem sliceItem = sliceActionImpl.mActionItem;
            if (sliceItem == null || !sliceItem.fireActionInternal(getContext(), null)) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                TemplateView templateView = this.mCurrentView;
                SliceItem sliceItem2 = sliceActionImpl.mSliceItem;
                Objects.requireNonNull(templateView);
                templateView.mAdapter.onSliceActionLoading(sliceItem2, 0);
            }
        } catch (PendingIntent.CanceledException e) {
            Log.e("SliceView", "PendingIntent for slice cannot be sent", e);
        }
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if ((this.mLongClickListener == null || !handleTouchForLongpress(motionEvent)) && !super.onInterceptTouchEvent(motionEvent)) {
            return false;
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        TemplateView templateView = this.mCurrentView;
        templateView.layout(0, 0, templateView.getMeasuredWidth(), templateView.getMeasuredHeight());
        if (this.mActionRow.getVisibility() != 8) {
            int measuredHeight = templateView.getMeasuredHeight();
            ActionRow actionRow = this.mActionRow;
            actionRow.layout(0, measuredHeight, actionRow.getMeasuredWidth(), this.mActionRow.getMeasuredHeight() + measuredHeight);
        }
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        if ((this.mLongClickListener == null || !handleTouchForLongpress(motionEvent)) && !super.onTouchEvent(motionEvent)) {
            return false;
        }
        return true;
    }

    public final void refreshLastUpdatedLabel(boolean z) {
        SliceMetadata sliceMetadata;
        boolean z2;
        if (this.mShowLastUpdated && (sliceMetadata = this.mSliceMetadata) != null) {
            if (sliceMetadata.mExpiry == -1) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                return;
            }
            if (z) {
                Handler handler = this.mHandler;
                AnonymousClass2 r1 = this.mRefreshLastUpdated;
                long j = 60000;
                if (!sliceMetadata.isExpired()) {
                    SliceMetadata sliceMetadata2 = this.mSliceMetadata;
                    Objects.requireNonNull(sliceMetadata2);
                    long currentTimeMillis = System.currentTimeMillis();
                    long j2 = sliceMetadata2.mExpiry;
                    long j3 = 0;
                    if (!(j2 == 0 || j2 == -1 || currentTimeMillis > j2)) {
                        j3 = j2 - currentTimeMillis;
                    }
                    j = 60000 + j3;
                }
                handler.postDelayed(r1, j);
                return;
            }
            this.mHandler.removeCallbacks(this.mRefreshLastUpdated);
        }
    }

    public final void updateActions() {
        if (this.mActions == null) {
            this.mActionRow.setVisibility(8);
            this.mCurrentView.setSliceActions(null);
            this.mCurrentView.setInsets(getPaddingStart(), getPaddingTop(), getPaddingEnd(), getPaddingBottom());
            return;
        }
        ArrayList arrayList = new ArrayList(this.mActions);
        Collections.sort(arrayList, SLICE_ACTION_PRIORITY_COMPARATOR);
        this.mCurrentView.setSliceActions(arrayList);
        this.mCurrentView.setInsets(getPaddingStart(), getPaddingTop(), getPaddingEnd(), getPaddingBottom());
        this.mActionRow.setVisibility(8);
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [androidx.slice.widget.SliceView$1] */
    /* JADX WARN: Type inference failed for: r2v1, types: [androidx.slice.widget.SliceView$2] */
    public SliceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 2130969743);
        this.mThemeTintColor = -1;
        SliceStyle sliceStyle = new SliceStyle(context, attributeSet);
        this.mSliceStyle = sliceStyle;
        this.mThemeTintColor = sliceStyle.mTintColor;
        getContext().getResources().getDimensionPixelSize(2131165271);
        TemplateView templateView = new TemplateView(getContext());
        this.mCurrentView = templateView;
        templateView.setPolicy(this.mViewPolicy);
        addView(this.mCurrentView, new ViewGroup.LayoutParams(-1, -1));
        TemplateView templateView2 = this.mCurrentView;
        Objects.requireNonNull(templateView2);
        templateView2.mObserver = null;
        SliceAdapter sliceAdapter = templateView2.mAdapter;
        if (sliceAdapter != null) {
            sliceAdapter.mSliceObserver = null;
        }
        TemplateView templateView3 = this.mCurrentView;
        SliceStyle sliceStyle2 = this.mSliceStyle;
        templateView3.setStyle(sliceStyle2, sliceStyle2.getRowStyle(null));
        this.mCurrentView.setTint(getTintColor());
        ListContent listContent = this.mListContent;
        if (listContent == null || listContent.getLayoutDir() == -1) {
            this.mCurrentView.setLayoutDirection(2);
        } else {
            this.mCurrentView.setLayoutDirection(this.mListContent.getLayoutDir());
        }
        ActionRow actionRow = new ActionRow(getContext());
        this.mActionRow = actionRow;
        actionRow.setBackground(new ColorDrawable(-1118482));
        addView(this.mActionRow, new ViewGroup.LayoutParams(-1, -1));
        updateActions();
        int scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mTouchSlopSquared = scaledTouchSlop * scaledTouchSlop;
        this.mHandler = new Handler();
        setClipToPadding(false);
        super.setOnClickListener(this);
    }

    public final boolean handleTouchForLongpress(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int rawX = ((int) motionEvent.getRawX()) - this.mDownX;
                    int rawY = ((int) motionEvent.getRawY()) - this.mDownY;
                    if ((rawY * rawY) + (rawX * rawX) > this.mTouchSlopSquared) {
                        this.mPressing = false;
                        this.mHandler.removeCallbacks(this.mLongpressCheck);
                    }
                    return this.mInLongpress;
                } else if (actionMasked != 3) {
                    return false;
                }
            }
            boolean z = this.mInLongpress;
            this.mPressing = false;
            this.mInLongpress = false;
            this.mHandler.removeCallbacks(this.mLongpressCheck);
            return z;
        }
        this.mHandler.removeCallbacks(this.mLongpressCheck);
        this.mDownX = (int) motionEvent.getRawX();
        this.mDownY = (int) motionEvent.getRawY();
        this.mPressing = true;
        this.mInLongpress = false;
        this.mHandler.postDelayed(this.mLongpressCheck, ViewConfiguration.getLongPressTimeout());
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isShown()) {
            logSliceMetricsVisibilityChange(true);
            refreshLastUpdatedLabel(true);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        logSliceMetricsVisibilityChange(false);
        refreshLastUpdatedLabel(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0089  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onMeasure(int r8, int r9) {
        /*
            Method dump skipped, instructions count: 296
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.SliceView.onMeasure(int, int):void");
    }

    @Override // android.view.View
    public final void onVisibilityChanged(View view, int i) {
        boolean z;
        super.onVisibilityChanged(view, i);
        if (isAttachedToWindow()) {
            boolean z2 = true;
            if (i == 0) {
                z = true;
            } else {
                z = false;
            }
            logSliceMetricsVisibilityChange(z);
            if (i != 0) {
                z2 = false;
            }
            refreshLastUpdatedLabel(z2);
        }
    }

    @Override // android.view.View
    public final void onWindowVisibilityChanged(int i) {
        boolean z;
        super.onWindowVisibilityChanged(i);
        boolean z2 = true;
        if (i == 0) {
            z = true;
        } else {
            z = false;
        }
        logSliceMetricsVisibilityChange(z);
        if (i != 0) {
            z2 = false;
        }
        refreshLastUpdatedLabel(z2);
    }

    @Override // android.view.View
    public final void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        super.setOnLongClickListener(onLongClickListener);
        this.mLongClickListener = onLongClickListener;
    }

    @Override // android.view.View
    public final void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setSliceViewPolicy(SliceViewPolicy sliceViewPolicy) {
        this.mViewPolicy = sliceViewPolicy;
    }
}
