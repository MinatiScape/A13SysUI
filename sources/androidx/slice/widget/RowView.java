package androidx.slice.widget;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceQuery;
import androidx.slice.widget.SliceActionView;
import androidx.slice.widget.SliceView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class RowView extends SliceChildView implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public final ProgressBar mActionSpinner;
    public boolean mAllowTwoLines;
    public final LinearLayout mContent;
    public final LinearLayout mEndContainer;
    public Handler mHandler;
    public List<SliceAction> mHeaderActions;
    public boolean mIsHeader;
    public boolean mIsRangeSliding;
    public boolean mIsStarRating;
    public long mLastSentRangeUpdate;
    public int mMeasuredRangeHeight;
    public View mRangeBar;
    public SliceItem mRangeItem;
    public int mRangeMaxValue;
    public int mRangeMinValue;
    public boolean mRangeUpdaterRunning;
    public int mRangeValue;
    public final LinearLayout mRootView;
    public SliceActionImpl mRowAction;
    public RowContent mRowContent;
    public int mRowIndex;
    public Button mSeeMoreView;
    public SliceItem mSelectionItem;
    public ArrayList<String> mSelectionOptionKeys;
    public ArrayList<CharSequence> mSelectionOptionValues;
    public Spinner mSelectionSpinner;
    public boolean mShowActionSpinner;
    public SliceItem mStartItem;
    public final ArrayMap<SliceActionImpl, SliceActionView> mToggles = new ArrayMap<>();
    public final ArrayMap<SliceActionImpl, SliceActionView> mActions = new ArrayMap<>();
    public Set<SliceItem> mLoadingActions = new HashSet();
    public AnonymousClass2 mRangeUpdater = new Runnable() { // from class: androidx.slice.widget.RowView.2
        @Override // java.lang.Runnable
        public final void run() {
            RowView.this.sendSliderValue();
            RowView.this.mRangeUpdaterRunning = false;
        }
    };
    public final AnonymousClass3 mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() { // from class: androidx.slice.widget.RowView.3
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            RowView rowView = RowView.this;
            rowView.mRangeValue = i + rowView.mRangeMinValue;
            long currentTimeMillis = System.currentTimeMillis();
            RowView rowView2 = RowView.this;
            long j = rowView2.mLastSentRangeUpdate;
            if (j != 0 && currentTimeMillis - j > 200) {
                rowView2.mRangeUpdaterRunning = false;
                rowView2.mHandler.removeCallbacks(rowView2.mRangeUpdater);
                RowView.this.sendSliderValue();
            } else if (!rowView2.mRangeUpdaterRunning) {
                rowView2.mRangeUpdaterRunning = true;
                rowView2.mHandler.postDelayed(rowView2.mRangeUpdater, 200L);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onStartTrackingTouch(SeekBar seekBar) {
            RowView.this.mIsRangeSliding = true;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onStopTrackingTouch(SeekBar seekBar) {
            RowView rowView = RowView.this;
            rowView.mIsRangeSliding = false;
            if (rowView.mRangeUpdaterRunning) {
                rowView.mRangeUpdaterRunning = false;
                rowView.mHandler.removeCallbacks(rowView.mRangeUpdater);
                RowView rowView2 = RowView.this;
                int progress = seekBar.getProgress();
                RowView rowView3 = RowView.this;
                rowView2.mRangeValue = progress + rowView3.mRangeMinValue;
                rowView3.sendSliderValue();
            }
        }
    };
    public final AnonymousClass4 mRatingBarChangeListener = new RatingBar.OnRatingBarChangeListener() { // from class: androidx.slice.widget.RowView.4
        @Override // android.widget.RatingBar.OnRatingBarChangeListener
        public final void onRatingChanged(RatingBar ratingBar, float f, boolean z) {
            RowView rowView = RowView.this;
            rowView.mRangeValue = Math.round(f + rowView.mRangeMinValue);
            long currentTimeMillis = System.currentTimeMillis();
            RowView rowView2 = RowView.this;
            long j = rowView2.mLastSentRangeUpdate;
            if (j != 0 && currentTimeMillis - j > 200) {
                rowView2.mRangeUpdaterRunning = false;
                rowView2.mHandler.removeCallbacks(rowView2.mRangeUpdater);
                RowView.this.sendSliderValue();
            } else if (!rowView2.mRangeUpdaterRunning) {
                rowView2.mRangeUpdaterRunning = true;
                rowView2.mHandler.postDelayed(rowView2.mRangeUpdater, 200L);
            }
        }
    };
    public int mIconSize = getContext().getResources().getDimensionPixelSize(2131165258);
    public int mImageSize = getContext().getResources().getDimensionPixelSize(2131165273);
    public final LinearLayout mStartContainer = (LinearLayout) findViewById(2131428104);
    public final LinearLayout mSubContent = (LinearLayout) findViewById(2131428943);
    public final TextView mPrimaryText = (TextView) findViewById(16908310);
    public final TextView mSecondaryText = (TextView) findViewById(16908304);
    public final TextView mLastUpdatedText = (TextView) findViewById(2131428204);
    public final View mBottomDivider = findViewById(2131427589);
    public final View mActionDivider = findViewById(2131427421);

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
                    sliceItem.fireActionInternal(RowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    SliceView.OnSliceActionListener onSliceActionListener = RowView.this.mObserver;
                    if (onSliceActionListener != null) {
                        onSliceActionListener.onSliceAction();
                    }
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
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
                    sliceItem.fireActionInternal(RowView.this.getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", time.getTime()));
                    SliceView.OnSliceActionListener onSliceActionListener = RowView.this.mObserver;
                    if (onSliceActionListener != null) {
                        onSliceActionListener.onSliceAction();
                    }
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
                }
            }
        }
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public final void onNothingSelected(AdapterView<?> adapterView) {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00a7, code lost:
        if (r2.mShowTitleItems != false) goto L_0x00a9;
     */
    /* JADX WARN: Removed duplicated region for block: B:191:0x037f  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x041f  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00f4  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x010d  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x012e  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x018a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void populateViews(boolean r11) {
        /*
            Method dump skipped, instructions count: 1086
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.populateViews(boolean):void");
    }

    public final void setViewClickable(LinearLayout linearLayout, boolean z) {
        View.OnClickListener onClickListener;
        Drawable drawable = null;
        if (z) {
            onClickListener = this;
        } else {
            onClickListener = null;
        }
        linearLayout.setOnClickListener(onClickListener);
        if (z) {
            drawable = SliceViewUtil.getDrawable(getContext(), 16843534);
        }
        linearLayout.setBackground(drawable);
        linearLayout.setClickable(z);
    }

    public static void setViewSidePaddings(View view, int i, int i2) {
        boolean z;
        if (i >= 0 || i2 >= 0) {
            z = false;
        } else {
            z = true;
        }
        if (view != null && !z) {
            if (i < 0) {
                i = view.getPaddingStart();
            }
            int paddingTop = view.getPaddingTop();
            if (i2 < 0) {
                i2 = view.getPaddingEnd();
            }
            view.setPaddingRelative(i, paddingTop, i2, view.getPaddingBottom());
        }
    }

    public final void addAction(SliceActionImpl sliceActionImpl, int i, LinearLayout linearLayout, boolean z) {
        int i2;
        SliceActionView sliceActionView = new SliceActionView(getContext(), this.mRowStyle);
        linearLayout.addView(sliceActionView);
        if (linearLayout.getVisibility() == 8) {
            linearLayout.setVisibility(0);
        }
        boolean isToggle = sliceActionImpl.isToggle();
        int i3 = !isToggle;
        if (isToggle != 0) {
            i2 = 3;
        } else {
            i2 = 0;
        }
        EventInfo eventInfo = new EventInfo(i3, i2, this.mRowIndex);
        if (z) {
            eventInfo.actionPosition = 0;
            eventInfo.actionIndex = 0;
            eventInfo.actionCount = 1;
        }
        sliceActionView.setAction(sliceActionImpl, eventInfo, this.mObserver, i, this.mLoadingListener);
        if (this.mLoadingActions.contains(sliceActionImpl.mSliceItem)) {
            sliceActionView.setLoading();
        }
        if (isToggle != 0) {
            this.mToggles.put(sliceActionImpl, sliceActionView);
        } else {
            this.mActions.put(sliceActionImpl, sliceActionView);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0110  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0121  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0139  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean addItem(androidx.slice.SliceItem r10, int r11, boolean r12) {
        /*
            Method dump skipped, instructions count: 371
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.addItem(androidx.slice.SliceItem, int, boolean):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0174  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void addSubtitle(boolean r10) {
        /*
            Method dump skipped, instructions count: 392
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.addSubtitle(boolean):void");
    }

    public final int getRowContentHeight() {
        int height = this.mRowContent.getHeight(this.mSliceStyle, this.mViewPolicy);
        if (this.mRangeBar != null && this.mStartItem == null) {
            SliceStyle sliceStyle = this.mSliceStyle;
            Objects.requireNonNull(sliceStyle);
            height -= sliceStyle.mRowRangeHeight;
        }
        if (this.mSelectionSpinner == null) {
            return height;
        }
        SliceStyle sliceStyle2 = this.mSliceStyle;
        Objects.requireNonNull(sliceStyle2);
        return height - sliceStyle2.mRowSelectionHeight;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        SliceActionView sliceActionView;
        SliceActionView.SliceActionLoadingListener sliceActionLoadingListener;
        SliceActionImpl sliceActionImpl;
        SliceActionImpl sliceActionImpl2 = this.mRowAction;
        if (sliceActionImpl2 != null && sliceActionImpl2.mActionItem != null) {
            if (sliceActionImpl2.getSubtype() != null) {
                String subtype = this.mRowAction.getSubtype();
                Objects.requireNonNull(subtype);
                char c = 65535;
                switch (subtype.hashCode()) {
                    case -868304044:
                        if (subtype.equals("toggle")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 759128640:
                        if (subtype.equals("time_picker")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1250407999:
                        if (subtype.equals("date_picker")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        sliceActionView = this.mToggles.get(this.mRowAction);
                        break;
                    case 1:
                        onClickPicker(false);
                        return;
                    case 2:
                        onClickPicker(true);
                        return;
                    default:
                        sliceActionView = this.mActions.get(this.mRowAction);
                        break;
                }
            } else {
                sliceActionView = this.mActions.get(this.mRowAction);
            }
            if (sliceActionView == null || (view instanceof SliceActionView)) {
                RowContent rowContent = this.mRowContent;
                Objects.requireNonNull(rowContent);
                if (rowContent.mIsHeader) {
                    performClick();
                    return;
                }
                try {
                    SliceActionImpl sliceActionImpl3 = this.mRowAction;
                    Objects.requireNonNull(sliceActionImpl3);
                    this.mShowActionSpinner = sliceActionImpl3.mActionItem.fireActionInternal(getContext(), null);
                    SliceView.OnSliceActionListener onSliceActionListener = this.mObserver;
                    if (onSliceActionListener != null) {
                        Objects.requireNonNull(this.mRowAction);
                        onSliceActionListener.onSliceAction();
                    }
                    if (this.mShowActionSpinner && (sliceActionLoadingListener = this.mLoadingListener) != null) {
                        SliceActionImpl sliceActionImpl4 = this.mRowAction;
                        Objects.requireNonNull(sliceActionImpl4);
                        ((SliceAdapter) sliceActionLoadingListener).onSliceActionLoading(sliceActionImpl4.mSliceItem, this.mRowIndex);
                        Set<SliceItem> set = this.mLoadingActions;
                        SliceActionImpl sliceActionImpl5 = this.mRowAction;
                        Objects.requireNonNull(sliceActionImpl5);
                        set.add(sliceActionImpl5.mSliceItem);
                    }
                    updateActionSpinner();
                } catch (PendingIntent.CanceledException e) {
                    Log.e("RowView", "PendingIntent for slice cannot be sent", e);
                }
            } else {
                SliceActionImpl sliceActionImpl6 = sliceActionView.mSliceAction;
                if (sliceActionImpl6 != null) {
                    if (!sliceActionImpl6.isToggle()) {
                        sliceActionView.sendActionInternal();
                    } else if (sliceActionView.mActionView != null && (sliceActionImpl = sliceActionView.mSliceAction) != null && sliceActionImpl.isToggle()) {
                        ((Checkable) sliceActionView.mActionView).toggle();
                    }
                }
            }
        }
    }

    public final void onClickPicker(boolean z) {
        if (this.mRowAction != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("ASDF");
            sb.append(z);
            sb.append(":");
            SliceActionImpl sliceActionImpl = this.mRowAction;
            Objects.requireNonNull(sliceActionImpl);
            sb.append(sliceActionImpl.mSliceItem);
            Log.d("ASDF", sb.toString());
            SliceActionImpl sliceActionImpl2 = this.mRowAction;
            Objects.requireNonNull(sliceActionImpl2);
            SliceItem findSubtype = SliceQuery.findSubtype(sliceActionImpl2.mSliceItem, "long", "millis");
            if (findSubtype != null) {
                int i = this.mRowIndex;
                Calendar instance = Calendar.getInstance();
                instance.setTime(new Date(findSubtype.getLong()));
                if (z) {
                    Context context = getContext();
                    SliceActionImpl sliceActionImpl3 = this.mRowAction;
                    Objects.requireNonNull(sliceActionImpl3);
                    new DatePickerDialog(context, 2132017480, new DateSetListener(sliceActionImpl3.mSliceItem, i), instance.get(1), instance.get(2), instance.get(5)).show();
                    return;
                }
                Context context2 = getContext();
                SliceActionImpl sliceActionImpl4 = this.mRowAction;
                Objects.requireNonNull(sliceActionImpl4);
                new TimePickerDialog(context2, 2132017480, new TimeSetListener(sliceActionImpl4.mSliceItem, i), instance.get(11), instance.get(12), false).show();
            }
        }
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public final void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.mSelectionItem != null && adapterView == this.mSelectionSpinner && i >= 0 && i < this.mSelectionOptionKeys.size()) {
            SliceView.OnSliceActionListener onSliceActionListener = this.mObserver;
            if (onSliceActionListener != null) {
                onSliceActionListener.onSliceAction();
            }
            try {
                if (this.mSelectionItem.fireActionInternal(getContext(), new Intent().addFlags(268435456).putExtra("android.app.slice.extra.SELECTION", this.mSelectionOptionKeys.get(i)))) {
                    this.mShowActionSpinner = true;
                    SliceActionView.SliceActionLoadingListener sliceActionLoadingListener = this.mLoadingListener;
                    if (sliceActionLoadingListener != null) {
                        SliceActionImpl sliceActionImpl = this.mRowAction;
                        Objects.requireNonNull(sliceActionImpl);
                        ((SliceAdapter) sliceActionLoadingListener).onSliceActionLoading(sliceActionImpl.mSliceItem, this.mRowIndex);
                        Set<SliceItem> set = this.mLoadingActions;
                        SliceActionImpl sliceActionImpl2 = this.mRowAction;
                        Objects.requireNonNull(sliceActionImpl2);
                        set.add(sliceActionImpl2.mSliceItem);
                    }
                    updateActionSpinner();
                }
            } catch (PendingIntent.CanceledException e) {
                Log.e("RowView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    public final void resetViewState() {
        this.mRootView.setVisibility(0);
        setLayoutDirection(2);
        setViewClickable(this.mRootView, false);
        setViewClickable(this.mContent, false);
        this.mStartContainer.removeAllViews();
        this.mEndContainer.removeAllViews();
        this.mEndContainer.setVisibility(8);
        this.mPrimaryText.setText((CharSequence) null);
        this.mSecondaryText.setText((CharSequence) null);
        this.mLastUpdatedText.setText((CharSequence) null);
        this.mLastUpdatedText.setVisibility(8);
        this.mToggles.clear();
        this.mActions.clear();
        this.mRowAction = null;
        this.mBottomDivider.setVisibility(8);
        this.mActionDivider.setVisibility(8);
        Button button = this.mSeeMoreView;
        if (button != null) {
            this.mRootView.removeView(button);
            this.mSeeMoreView = null;
        }
        this.mIsRangeSliding = false;
        this.mRangeItem = null;
        this.mRangeMinValue = 0;
        this.mRangeMaxValue = 0;
        this.mRangeValue = 0;
        this.mLastSentRangeUpdate = 0L;
        this.mHandler = null;
        View view = this.mRangeBar;
        if (view != null) {
            if (this.mStartItem == null) {
                removeView(view);
            } else {
                this.mContent.removeView(view);
            }
            this.mRangeBar = null;
        }
        this.mSubContent.setVisibility(0);
        this.mStartItem = null;
        this.mActionSpinner.setVisibility(8);
        Spinner spinner = this.mSelectionSpinner;
        if (spinner != null) {
            removeView(spinner);
            this.mSelectionSpinner = null;
        }
        this.mSelectionItem = null;
    }

    public final void sendSliderValue() {
        if (this.mRangeItem != null) {
            try {
                this.mLastSentRangeUpdate = System.currentTimeMillis();
                SliceItem sliceItem = this.mRangeItem;
                Context context = getContext();
                Intent putExtra = new Intent().addFlags(268435456).putExtra("android.app.slice.extra.RANGE_VALUE", this.mRangeValue);
                Objects.requireNonNull(sliceItem);
                sliceItem.fireActionInternal(context, putExtra);
                SliceView.OnSliceActionListener onSliceActionListener = this.mObserver;
                if (onSliceActionListener != null) {
                    onSliceActionListener.onSliceAction();
                }
            } catch (PendingIntent.CanceledException e) {
                Log.e("RowView", "PendingIntent for slice cannot be sent", e);
            }
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setAllowTwoLines(boolean z) {
        this.mAllowTwoLines = z;
        if (this.mRowContent != null) {
            populateViews(true);
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setLastUpdated(long j) {
        boolean z;
        this.mLastUpdated = j;
        RowContent rowContent = this.mRowContent;
        if (rowContent != null) {
            if (rowContent.mTitleItem != null) {
                Objects.requireNonNull(rowContent);
                if (TextUtils.isEmpty(rowContent.mTitleItem.getSanitizedText())) {
                    z = true;
                    addSubtitle(z);
                }
            }
            z = false;
            addSubtitle(z);
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setLoadingActions(Set<SliceItem> set) {
        if (set == null) {
            this.mLoadingActions.clear();
            this.mShowActionSpinner = false;
        } else {
            this.mLoadingActions = set;
        }
        updateEndItems();
        updateActionSpinner();
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setShowLastUpdated(boolean z) {
        this.mShowLastUpdated = z;
        if (this.mRowContent != null) {
            populateViews(true);
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setSliceActions(List<SliceAction> list) {
        this.mHeaderActions = list;
        if (this.mRowContent != null) {
            updateEndItems();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0044, code lost:
        if (r2 != false) goto L_0x0048;
     */
    @Override // androidx.slice.widget.SliceChildView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setSliceItem(androidx.slice.widget.SliceContent r4, boolean r5, int r6, int r7, androidx.slice.widget.SliceView.OnSliceActionListener r8) {
        /*
            r3 = this;
            r3.mObserver = r8
            androidx.slice.widget.RowContent r7 = r3.mRowContent
            r8 = 0
            if (r7 == 0) goto L_0x0047
            boolean r7 = r7.isValid()
            if (r7 == 0) goto L_0x0047
            androidx.slice.widget.RowContent r7 = r3.mRowContent
            if (r7 == 0) goto L_0x0019
            androidx.slice.SliceStructure r0 = new androidx.slice.SliceStructure
            androidx.slice.SliceItem r7 = r7.mSliceItem
            r0.<init>(r7)
            goto L_0x001a
        L_0x0019:
            r0 = 0
        L_0x001a:
            androidx.slice.SliceStructure r7 = new androidx.slice.SliceStructure
            androidx.slice.SliceItem r1 = r4.mSliceItem
            androidx.slice.Slice r1 = r1.getSlice()
            r7.<init>(r1)
            r1 = 1
            if (r0 == 0) goto L_0x0030
            boolean r2 = r0.equals(r7)
            if (r2 == 0) goto L_0x0030
            r2 = r1
            goto L_0x0031
        L_0x0030:
            r2 = r8
        L_0x0031:
            if (r0 == 0) goto L_0x0041
            android.net.Uri r0 = r0.mUri
            if (r0 == 0) goto L_0x0041
            android.net.Uri r7 = r7.mUri
            boolean r7 = r0.equals(r7)
            if (r7 == 0) goto L_0x0041
            r7 = r1
            goto L_0x0042
        L_0x0041:
            r7 = r8
        L_0x0042:
            if (r7 == 0) goto L_0x0047
            if (r2 == 0) goto L_0x0047
            goto L_0x0048
        L_0x0047:
            r1 = r8
        L_0x0048:
            r3.mShowActionSpinner = r8
            r3.mIsHeader = r5
            androidx.slice.widget.RowContent r4 = (androidx.slice.widget.RowContent) r4
            r3.mRowContent = r4
            r3.mRowIndex = r6
            r3.populateViews(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.setSliceItem(androidx.slice.widget.SliceContent, boolean, int, int, androidx.slice.widget.SliceView$OnSliceActionListener):void");
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setStyle(SliceStyle sliceStyle, RowStyle rowStyle) {
        boolean z;
        this.mSliceStyle = sliceStyle;
        this.mRowStyle = rowStyle;
        if (sliceStyle != null) {
            setViewSidePaddings(this.mStartContainer, rowStyle.mTitleItemStartPadding, rowStyle.mTitleItemEndPadding);
            LinearLayout linearLayout = this.mContent;
            RowStyle rowStyle2 = this.mRowStyle;
            Objects.requireNonNull(rowStyle2);
            int i = rowStyle2.mContentStartPadding;
            RowStyle rowStyle3 = this.mRowStyle;
            Objects.requireNonNull(rowStyle3);
            setViewSidePaddings(linearLayout, i, rowStyle3.mContentEndPadding);
            TextView textView = this.mPrimaryText;
            RowStyle rowStyle4 = this.mRowStyle;
            Objects.requireNonNull(rowStyle4);
            int i2 = rowStyle4.mTitleStartPadding;
            RowStyle rowStyle5 = this.mRowStyle;
            Objects.requireNonNull(rowStyle5);
            setViewSidePaddings(textView, i2, rowStyle5.mTitleEndPadding);
            LinearLayout linearLayout2 = this.mSubContent;
            RowStyle rowStyle6 = this.mRowStyle;
            Objects.requireNonNull(rowStyle6);
            int i3 = rowStyle6.mSubContentStartPadding;
            RowStyle rowStyle7 = this.mRowStyle;
            Objects.requireNonNull(rowStyle7);
            setViewSidePaddings(linearLayout2, i3, rowStyle7.mSubContentEndPadding);
            LinearLayout linearLayout3 = this.mEndContainer;
            RowStyle rowStyle8 = this.mRowStyle;
            Objects.requireNonNull(rowStyle8);
            int i4 = rowStyle8.mEndItemStartPadding;
            RowStyle rowStyle9 = this.mRowStyle;
            Objects.requireNonNull(rowStyle9);
            setViewSidePaddings(linearLayout3, i4, rowStyle9.mEndItemEndPadding);
            View view = this.mBottomDivider;
            RowStyle rowStyle10 = this.mRowStyle;
            Objects.requireNonNull(rowStyle10);
            int i5 = rowStyle10.mBottomDividerStartPadding;
            RowStyle rowStyle11 = this.mRowStyle;
            Objects.requireNonNull(rowStyle11);
            int i6 = rowStyle11.mBottomDividerEndPadding;
            if (i5 >= 0 || i6 >= 0) {
                z = false;
            } else {
                z = true;
            }
            if (view != null && !z) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                if (i5 >= 0) {
                    marginLayoutParams.setMarginStart(i5);
                }
                if (i6 >= 0) {
                    marginLayoutParams.setMarginEnd(i6);
                }
                view.setLayoutParams(marginLayoutParams);
            }
            View view2 = this.mActionDivider;
            RowStyle rowStyle12 = this.mRowStyle;
            Objects.requireNonNull(rowStyle12);
            int i7 = rowStyle12.mActionDividerHeight;
            if (view2 != null && i7 >= 0) {
                ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
                layoutParams.height = i7;
                view2.setLayoutParams(layoutParams);
            }
            if (this.mRowStyle.getTintColor() != -1) {
                setTint(this.mRowStyle.getTintColor());
            }
        }
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setTint(int i) {
        this.mTintColor = i;
        if (this.mRowContent != null) {
            populateViews(true);
        }
    }

    public final void updateActionSpinner() {
        int i;
        ProgressBar progressBar = this.mActionSpinner;
        if (this.mShowActionSpinner) {
            i = 0;
        } else {
            i = 8;
        }
        progressBar.setVisibility(i);
    }

    /* JADX WARN: Code restructure failed: missing block: B:59:0x00c8, code lost:
        if (r7 != false) goto L_0x00ca;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:94:0x014d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateEndItems() {
        /*
            Method dump skipped, instructions count: 340
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.widget.RowView.updateEndItems():void");
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [androidx.slice.widget.RowView$2] */
    /* JADX WARN: Type inference failed for: r0v4, types: [androidx.slice.widget.RowView$3] */
    /* JADX WARN: Type inference failed for: r0v5, types: [androidx.slice.widget.RowView$4] */
    public RowView(Context context) {
        super(context);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(2131623979, (ViewGroup) this, false);
        this.mRootView = linearLayout;
        addView(linearLayout);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(16908290);
        this.mContent = linearLayout2;
        ProgressBar progressBar = (ProgressBar) findViewById(2131427445);
        this.mActionSpinner = progressBar;
        int colorAttr = SliceViewUtil.getColorAttr(getContext(), 2130968819);
        Drawable indeterminateDrawable = progressBar.getIndeterminateDrawable();
        if (!(indeterminateDrawable == null || colorAttr == 0)) {
            indeterminateDrawable.setColorFilter(colorAttr, PorterDuff.Mode.MULTIPLY);
            progressBar.setProgressDrawable(indeterminateDrawable);
        }
        this.mEndContainer = (LinearLayout) findViewById(16908312);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.setImportantForAccessibility(this, 2);
        ViewCompat.Api16Impl.setImportantForAccessibility(linearLayout2, 2);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int paddingLeft = getPaddingLeft();
        LinearLayout linearLayout = this.mRootView;
        linearLayout.layout(paddingLeft, this.mInsetTop, linearLayout.getMeasuredWidth() + paddingLeft, getRowContentHeight() + this.mInsetTop);
        if (this.mRangeBar != null && this.mStartItem == null) {
            SliceStyle sliceStyle = this.mSliceStyle;
            Objects.requireNonNull(sliceStyle);
            int rowContentHeight = getRowContentHeight() + ((sliceStyle.mRowRangeHeight - this.mMeasuredRangeHeight) / 2) + this.mInsetTop;
            View view = this.mRangeBar;
            view.layout(paddingLeft, rowContentHeight, view.getMeasuredWidth() + paddingLeft, this.mMeasuredRangeHeight + rowContentHeight);
        } else if (this.mSelectionSpinner != null) {
            int rowContentHeight2 = getRowContentHeight() + this.mInsetTop;
            Spinner spinner = this.mSelectionSpinner;
            spinner.layout(paddingLeft, rowContentHeight2, spinner.getMeasuredWidth() + paddingLeft, this.mSelectionSpinner.getMeasuredHeight() + rowContentHeight2);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int rowContentHeight = getRowContentHeight();
        if (rowContentHeight != 0) {
            this.mRootView.setVisibility(0);
            measureChild(this.mRootView, i, View.MeasureSpec.makeMeasureSpec(rowContentHeight + this.mInsetTop + this.mInsetBottom, 1073741824));
            i3 = this.mRootView.getMeasuredWidth();
        } else {
            this.mRootView.setVisibility(8);
            i3 = 0;
        }
        View view = this.mRangeBar;
        if (view == null || this.mStartItem != null) {
            Spinner spinner = this.mSelectionSpinner;
            if (spinner != null) {
                SliceStyle sliceStyle = this.mSliceStyle;
                Objects.requireNonNull(sliceStyle);
                measureChild(spinner, i, View.MeasureSpec.makeMeasureSpec(sliceStyle.mRowSelectionHeight + this.mInsetTop + this.mInsetBottom, 1073741824));
                i3 = Math.max(i3, this.mSelectionSpinner.getMeasuredWidth());
            }
        } else {
            SliceStyle sliceStyle2 = this.mSliceStyle;
            Objects.requireNonNull(sliceStyle2);
            measureChild(view, i, View.MeasureSpec.makeMeasureSpec(sliceStyle2.mRowRangeHeight + this.mInsetTop + this.mInsetBottom, 1073741824));
            this.mMeasuredRangeHeight = this.mRangeBar.getMeasuredHeight();
            i3 = Math.max(i3, this.mRangeBar.getMeasuredWidth());
        }
        int max = Math.max(i3 + this.mInsetStart + this.mInsetEnd, getSuggestedMinimumWidth());
        RowContent rowContent = this.mRowContent;
        if (rowContent != null) {
            i4 = rowContent.getHeight(this.mSliceStyle, this.mViewPolicy);
        } else {
            i4 = 0;
        }
        setMeasuredDimension(View.resolveSizeAndState(max, i, 0), i4 + this.mInsetTop + this.mInsetBottom);
    }

    @Override // androidx.slice.widget.SliceChildView
    public final void setInsets(int i, int i2, int i3, int i4) {
        super.setInsets(i, i2, i3, i4);
        setPadding(i, i2, i3, i4);
    }
}
