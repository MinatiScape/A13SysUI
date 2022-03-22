package androidx.slice.widget;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceQuery;
import androidx.slice.widget.SliceView;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
/* loaded from: classes.dex */
public class GridRowView extends SliceChildView implements View.OnClickListener, View.OnTouchListener {
    public final View mForeground;
    public GridContent mGridContent;
    public final int mGutter;
    public final int mIconSize;
    public final int mLargeImageHeight;
    public final int[] mLoc;
    public boolean mMaxCellUpdateScheduled;
    public int mMaxCells;
    public final AnonymousClass2 mMaxCellsUpdater;
    public int mRowCount;
    public int mRowIndex;
    public final int mSmallImageMinWidth;
    public final int mSmallImageSize;
    public final int mTextPadding;
    public final LinearLayout mViewContainer;

    /* loaded from: classes.dex */
    public class DateSetListener implements DatePickerDialog.OnDateSetListener {
        public final SliceItem mActionItem;
        public final int mRowIndex;

        public DateSetListener(SliceItem sliceItem, int i) {
            this.mActionItem = sliceItem;
            this.mRowIndex = i;
        }

        @Override // android.app.DatePickerDialog.OnDateSetListener
        public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            Calendar instance = Calendar.getInstance();
            instance.set(i, i2, i3);
            Date time = instance.getTime();
            SliceItem sliceItem = this.mActionItem;
            if (sliceItem != null) {
                try {
                    sliceItem.fireActionInternal(GridRowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    SliceView.OnSliceActionListener onSliceActionListener = GridRowView.this.mObserver;
                    if (onSliceActionListener != null) {
                        onSliceActionListener.onSliceAction();
                    }
                } catch (PendingIntent.CanceledException e) {
                    Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class TimeSetListener implements TimePickerDialog.OnTimeSetListener {
        public final SliceItem mActionItem;
        public final int mRowIndex;

        public TimeSetListener(SliceItem sliceItem, int i) {
            this.mActionItem = sliceItem;
            this.mRowIndex = i;
        }

        @Override // android.app.TimePickerDialog.OnTimeSetListener
        public final void onTimeSet(TimePicker timePicker, int i, int i2) {
            Date time = Calendar.getInstance().getTime();
            time.setHours(i);
            time.setMinutes(i2);
            SliceItem sliceItem = this.mActionItem;
            if (sliceItem != null) {
                try {
                    sliceItem.fireActionInternal(GridRowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    SliceView.OnSliceActionListener onSliceActionListener = GridRowView.this.mObserver;
                    if (onSliceActionListener != null) {
                        onSliceActionListener.onSliceAction();
                    }
                } catch (PendingIntent.CanceledException e) {
                    Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    public GridRowView(Context context) {
        this(context, null);
    }

    public final int determinePadding(SliceItem sliceItem) {
        SliceStyle sliceStyle;
        if (sliceItem == null) {
            return 0;
        }
        if ("image".equals(sliceItem.mFormat)) {
            return this.mTextPadding;
        }
        if ((!"text".equals(sliceItem.mFormat) && !"long".equals(sliceItem.mFormat)) || (sliceStyle = this.mSliceStyle) == null) {
            return 0;
        }
        Objects.requireNonNull(sliceStyle);
        return sliceStyle.mVerticalGridTextPadding;
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [androidx.slice.widget.GridRowView$2] */
    public GridRowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLoc = new int[2];
        this.mMaxCells = -1;
        this.mMaxCellsUpdater = new ViewTreeObserver.OnPreDrawListener() { // from class: androidx.slice.widget.GridRowView.2
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public final boolean onPreDraw() {
                GridRowView gridRowView = GridRowView.this;
                gridRowView.mMaxCells = gridRowView.getMaxCells();
                GridRowView.this.populateViews();
                GridRowView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                GridRowView.this.mMaxCellUpdateScheduled = false;
                return true;
            }
        };
        Resources resources = getContext().getResources();
        LinearLayout linearLayout = new LinearLayout(getContext());
        this.mViewContainer = linearLayout;
        linearLayout.setOrientation(0);
        addView(linearLayout, new FrameLayout.LayoutParams(-1, -1));
        linearLayout.setGravity(16);
        this.mIconSize = resources.getDimensionPixelSize(2131165258);
        this.mSmallImageSize = resources.getDimensionPixelSize(2131165273);
        this.mLargeImageHeight = resources.getDimensionPixelSize(2131165250);
        this.mSmallImageMinWidth = resources.getDimensionPixelSize(2131165249);
        this.mGutter = resources.getDimensionPixelSize(2131165248);
        this.mTextPadding = resources.getDimensionPixelSize(2131165257);
        View view = new View(getContext());
        this.mForeground = view;
        addView(view, new FrameLayout.LayoutParams(-1, -1));
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x0236  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void addCell(androidx.slice.widget.GridContent.CellContent r24, int r25, int r26) {
        /*
            Method dump skipped, instructions count: 786
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.GridRowView.addCell(androidx.slice.widget.GridContent$CellContent, int, int):void");
    }

    public final boolean addPickerItem(final SliceItem sliceItem, LinearLayout linearLayout, int i, final boolean z) {
        SliceItem findSubtype = SliceQuery.findSubtype(sliceItem, "long", "millis");
        if (findSubtype == null) {
            return false;
        }
        long j = findSubtype.getLong();
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(2131623981, (ViewGroup) null);
        SliceStyle sliceStyle = this.mSliceStyle;
        if (sliceStyle != null) {
            textView.setTextSize(0, sliceStyle.mGridTitleSize);
            SliceStyle sliceStyle2 = this.mSliceStyle;
            Objects.requireNonNull(sliceStyle2);
            textView.setTextColor(sliceStyle2.mTitleColor);
        }
        final Date date = new Date(j);
        SliceItem find = SliceQuery.find(sliceItem, "text", "title");
        if (find != null) {
            textView.setText((CharSequence) find.mObj);
        }
        final int i2 = this.mRowIndex;
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: androidx.slice.widget.GridRowView.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(date);
                if (z) {
                    new DatePickerDialog(GridRowView.this.getContext(), 2132017480, new DateSetListener(sliceItem, i2), instance.get(1), instance.get(2), instance.get(5)).show();
                } else {
                    new TimePickerDialog(GridRowView.this.getContext(), 2132017480, new TimeSetListener(sliceItem, i2), instance.get(11), instance.get(12), false).show();
                }
            }
        });
        linearLayout.setClickable(true);
        linearLayout.setBackground(SliceViewUtil.getDrawable(getContext(), 16843868));
        linearLayout.addView(textView);
        textView.setPadding(0, i, 0, 0);
        return true;
    }

    public final int getExtraBottomPadding() {
        SliceStyle sliceStyle;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.mAllImages || this.mRowIndex != this.mRowCount - 1 || (sliceStyle = this.mSliceStyle) == null) {
            return 0;
        }
        return sliceStyle.mGridBottomPadding;
    }

    public final int getMaxCells() {
        int i;
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid() || getWidth() == 0) {
            return -1;
        }
        GridContent gridContent2 = this.mGridContent;
        Objects.requireNonNull(gridContent2);
        if (gridContent2.mGridContent.size() <= 1) {
            return 1;
        }
        GridContent gridContent3 = this.mGridContent;
        Objects.requireNonNull(gridContent3);
        int i2 = gridContent3.mLargestImageMode;
        if (i2 == 2) {
            i = this.mLargeImageHeight;
        } else if (i2 != 4) {
            i = this.mSmallImageMinWidth;
        } else {
            i = this.mGridContent.getFirstImageSize(getContext()).x;
        }
        return getWidth() / (i + this.mGutter);
    }

    public final void makeEntireGridClickable(boolean z) {
        GridRowView gridRowView;
        GridRowView gridRowView2;
        LinearLayout linearLayout = this.mViewContainer;
        Drawable drawable = null;
        if (z) {
            gridRowView = this;
        } else {
            gridRowView = null;
        }
        linearLayout.setOnTouchListener(gridRowView);
        LinearLayout linearLayout2 = this.mViewContainer;
        if (z) {
            gridRowView2 = this;
        } else {
            gridRowView2 = null;
        }
        linearLayout2.setOnClickListener(gridRowView2);
        View view = this.mForeground;
        if (z) {
            drawable = SliceViewUtil.getDrawable(getContext(), 16843534);
        }
        view.setBackground(drawable);
        this.mViewContainer.setClickable(z);
        setClickable(z);
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        int height = this.mGridContent.getHeight(this.mSliceStyle, this.mViewPolicy) + this.mInsetTop + this.mInsetBottom;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, 1073741824);
        this.mViewContainer.getLayoutParams().height = height;
        super.onMeasure(i, makeMeasureSpec);
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        this.mForeground.getLocationOnScreen(this.mLoc);
        this.mForeground.getBackground().setHotspot((int) (motionEvent.getRawX() - this.mLoc[0]), (int) (motionEvent.getRawY() - this.mLoc[1]));
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mForeground.setPressed(true);
        } else if (actionMasked == 3 || actionMasked == 1 || actionMasked == 2) {
            this.mForeground.setPressed(false);
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void populateViews() {
        /*
            Method dump skipped, instructions count: 470
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.GridRowView.populateViews():void");
    }

    public final void resetView() {
        if (this.mMaxCellUpdateScheduled) {
            this.mMaxCellUpdateScheduled = false;
            getViewTreeObserver().removeOnPreDrawListener(this.mMaxCellsUpdater);
        }
        this.mViewContainer.removeAllViews();
        setLayoutDirection(2);
        makeEntireGridClickable(false);
    }

    public final boolean scheduleMaxCellsUpdate() {
        GridContent gridContent = this.mGridContent;
        if (gridContent == null || !gridContent.isValid()) {
            return true;
        }
        if (getWidth() == 0) {
            this.mMaxCellUpdateScheduled = true;
            getViewTreeObserver().addOnPreDrawListener(this.mMaxCellsUpdater);
            return true;
        }
        this.mMaxCells = getMaxCells();
        return false;
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setTint(int i) {
        this.mTintColor = i;
        if (this.mGridContent != null) {
            resetView();
            populateViews();
        }
    }

    public final void makeClickable(ViewGroup viewGroup) {
        viewGroup.setOnClickListener(this);
        viewGroup.setBackground(SliceViewUtil.getDrawable(getContext(), 16843868));
        viewGroup.setClickable(true);
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        SliceItem find;
        Pair pair = (Pair) view.getTag();
        SliceItem sliceItem = (SliceItem) pair.first;
        EventInfo eventInfo = (EventInfo) pair.second;
        if (sliceItem != null && (find = SliceQuery.find(sliceItem, "action", (String) null)) != null) {
            try {
                find.fireActionInternal(null, null);
                SliceView.OnSliceActionListener onSliceActionListener = this.mObserver;
                if (onSliceActionListener != null) {
                    onSliceActionListener.onSliceAction();
                }
            } catch (PendingIntent.CanceledException e) {
                Log.e("GridRowView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setInsets(int i, int i2, int i3, int i4) {
        SliceStyle sliceStyle;
        super.setInsets(i, i2, i3, i4);
        LinearLayout linearLayout = this.mViewContainer;
        GridContent gridContent = this.mGridContent;
        int i5 = 0;
        if (gridContent != null && gridContent.mAllImages && this.mRowIndex == 0 && (sliceStyle = this.mSliceStyle) != null) {
            i5 = sliceStyle.mGridTopPadding;
        }
        linearLayout.setPadding(i, i5 + i2, i3, getExtraBottomPadding() + i4);
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setSliceItem(SliceContent sliceContent, boolean z, int i, int i2, SliceView.OnSliceActionListener onSliceActionListener) {
        SliceStyle sliceStyle;
        resetView();
        this.mObserver = onSliceActionListener;
        this.mRowIndex = i;
        this.mRowCount = i2;
        this.mGridContent = (GridContent) sliceContent;
        if (!scheduleMaxCellsUpdate()) {
            populateViews();
        }
        LinearLayout linearLayout = this.mViewContainer;
        int i3 = this.mInsetStart;
        int i4 = this.mInsetTop;
        GridContent gridContent = this.mGridContent;
        int i5 = 0;
        if (gridContent != null && gridContent.mAllImages && this.mRowIndex == 0 && (sliceStyle = this.mSliceStyle) != null) {
            i5 = sliceStyle.mGridTopPadding;
        }
        linearLayout.setPadding(i3, i5 + i4, this.mInsetEnd, getExtraBottomPadding() + this.mInsetBottom);
    }
}
