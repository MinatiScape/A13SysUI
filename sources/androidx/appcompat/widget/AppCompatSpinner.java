package androidx.appcompat.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ThemedSpinnerAdapter;
import androidx.appcompat.app.AlertController;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import java.util.Objects;
/* loaded from: classes.dex */
public class AppCompatSpinner extends Spinner {
    public static final int[] ATTRS_ANDROID_SPINNERMODE = {16843505};
    public int mDropDownWidth;
    public AnonymousClass1 mForwardingListener;
    public SpinnerPopup mPopup;
    public final Context mPopupContext;
    public final boolean mPopupSet;
    public SpinnerAdapter mTempAdapter;
    public final Rect mTempRect = new Rect();
    public final AppCompatBackgroundHelper mBackgroundTintHelper = new AppCompatBackgroundHelper(this);

    /* loaded from: classes.dex */
    public class DialogPopup implements SpinnerPopup, DialogInterface.OnClickListener {
        public ListAdapter mListAdapter;
        public AlertDialog mPopup;
        public CharSequence mPrompt;

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final Drawable getBackground() {
            return null;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final int getHorizontalOffset() {
            return 0;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final int getVerticalOffset() {
            return 0;
        }

        public DialogPopup() {
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final void dismiss() {
            AlertDialog alertDialog = this.mPopup;
            if (alertDialog != null) {
                alertDialog.dismiss();
                this.mPopup = null;
            }
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final boolean isShowing() {
            AlertDialog alertDialog = this.mPopup;
            if (alertDialog != null) {
                return alertDialog.isShowing();
            }
            return false;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public final void onClick(DialogInterface dialogInterface, int i) {
            AppCompatSpinner.this.setSelection(i);
            if (AppCompatSpinner.this.getOnItemClickListener() != null) {
                AppCompatSpinner.this.performItemClick(null, i, this.mListAdapter.getItemId(i));
            }
            dismiss();
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final void setBackgroundDrawable(Drawable drawable) {
            Log.e("AppCompatSpinner", "Cannot set popup background for MODE_DIALOG, ignoring");
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final void setHorizontalOffset(int i) {
            Log.e("AppCompatSpinner", "Cannot set horizontal offset for MODE_DIALOG, ignoring");
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final void setHorizontalOriginalOffset(int i) {
            Log.e("AppCompatSpinner", "Cannot set horizontal (original) offset for MODE_DIALOG, ignoring");
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final void setVerticalOffset(int i) {
            Log.e("AppCompatSpinner", "Cannot set vertical offset for MODE_DIALOG, ignoring");
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final void show(int i, int i2) {
            if (this.mListAdapter != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AppCompatSpinner.this.getPopupContext());
                CharSequence charSequence = this.mPrompt;
                if (charSequence != null) {
                    builder.P.mTitle = charSequence;
                }
                ListAdapter listAdapter = this.mListAdapter;
                int selectedItemPosition = AppCompatSpinner.this.getSelectedItemPosition();
                AlertController.AlertParams alertParams = builder.P;
                alertParams.mAdapter = listAdapter;
                alertParams.mOnClickListener = this;
                alertParams.mCheckedItem = selectedItemPosition;
                alertParams.mIsSingleChoice = true;
                AlertDialog create = builder.create();
                this.mPopup = create;
                AlertController alertController = create.mAlert;
                Objects.requireNonNull(alertController);
                AlertController.RecycleListView recycleListView = alertController.mListView;
                recycleListView.setTextDirection(i);
                recycleListView.setTextAlignment(i2);
                this.mPopup.show();
            }
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final void setAdapter(ListAdapter listAdapter) {
            this.mListAdapter = listAdapter;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final void setPromptText(CharSequence charSequence) {
            this.mPrompt = charSequence;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final CharSequence getHintText() {
            return this.mPrompt;
        }
    }

    /* loaded from: classes.dex */
    public static class DropDownAdapter implements ListAdapter, SpinnerAdapter {
        public SpinnerAdapter mAdapter;
        public ListAdapter mListAdapter;

        @Override // android.widget.Adapter
        public final int getItemViewType(int i) {
            return 0;
        }

        @Override // android.widget.Adapter
        public final int getViewTypeCount() {
            return 1;
        }

        @Override // android.widget.ListAdapter
        public final boolean areAllItemsEnabled() {
            ListAdapter listAdapter = this.mListAdapter;
            if (listAdapter != null) {
                return listAdapter.areAllItemsEnabled();
            }
            return true;
        }

        @Override // android.widget.Adapter
        public final int getCount() {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return 0;
            }
            return spinnerAdapter.getCount();
        }

        @Override // android.widget.SpinnerAdapter
        public final View getDropDownView(int i, View view, ViewGroup viewGroup) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return null;
            }
            return spinnerAdapter.getDropDownView(i, view, viewGroup);
        }

        @Override // android.widget.Adapter
        public final Object getItem(int i) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return null;
            }
            return spinnerAdapter.getItem(i);
        }

        @Override // android.widget.Adapter
        public final long getItemId(int i) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null) {
                return -1L;
            }
            return spinnerAdapter.getItemId(i);
        }

        @Override // android.widget.Adapter
        public final boolean hasStableIds() {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter == null || !spinnerAdapter.hasStableIds()) {
                return false;
            }
            return true;
        }

        @Override // android.widget.ListAdapter
        public final boolean isEnabled(int i) {
            ListAdapter listAdapter = this.mListAdapter;
            if (listAdapter != null) {
                return listAdapter.isEnabled(i);
            }
            return true;
        }

        @Override // android.widget.Adapter
        public final void registerDataSetObserver(DataSetObserver dataSetObserver) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null) {
                spinnerAdapter.registerDataSetObserver(dataSetObserver);
            }
        }

        @Override // android.widget.Adapter
        public final void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            SpinnerAdapter spinnerAdapter = this.mAdapter;
            if (spinnerAdapter != null) {
                spinnerAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }

        public DropDownAdapter(SpinnerAdapter spinnerAdapter, Resources.Theme theme) {
            this.mAdapter = spinnerAdapter;
            if (spinnerAdapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter) spinnerAdapter;
            }
            if (theme == null) {
                return;
            }
            if (spinnerAdapter instanceof ThemedSpinnerAdapter) {
                ThemedSpinnerAdapter themedSpinnerAdapter = (ThemedSpinnerAdapter) spinnerAdapter;
                if (themedSpinnerAdapter.getDropDownViewTheme() != theme) {
                    themedSpinnerAdapter.setDropDownViewTheme(theme);
                }
            } else if (spinnerAdapter instanceof ThemedSpinnerAdapter) {
                ThemedSpinnerAdapter themedSpinnerAdapter2 = (ThemedSpinnerAdapter) spinnerAdapter;
                if (themedSpinnerAdapter2.getDropDownViewTheme() == null) {
                    themedSpinnerAdapter2.setDropDownViewTheme();
                }
            }
        }

        @Override // android.widget.Adapter
        public final View getView(int i, View view, ViewGroup viewGroup) {
            return getDropDownView(i, view, viewGroup);
        }

        @Override // android.widget.Adapter
        public final boolean isEmpty() {
            if (getCount() == 0) {
                return true;
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    public class DropdownPopup extends ListPopupWindow implements SpinnerPopup {
        public ListAdapter mAdapter;
        public CharSequence mHintText;
        public int mOriginalHorizontalOffset;
        public final Rect mVisibleRect = new Rect();

        public DropdownPopup(Context context, AttributeSet attributeSet) {
            super(context, attributeSet, 2130969752, 0);
            this.mDropDownAnchorView = AppCompatSpinner.this;
            this.mModal = true;
            this.mPopup.setFocusable(true);
            this.mItemClickListener = new AdapterView.OnItemClickListener() { // from class: androidx.appcompat.widget.AppCompatSpinner.DropdownPopup.1
                @Override // android.widget.AdapterView.OnItemClickListener
                public final void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    AppCompatSpinner.this.setSelection(i);
                    if (AppCompatSpinner.this.getOnItemClickListener() != null) {
                        DropdownPopup dropdownPopup = DropdownPopup.this;
                        AppCompatSpinner.this.performItemClick(view, i, dropdownPopup.mAdapter.getItemId(i));
                    }
                    DropdownPopup.this.dismiss();
                }
            };
        }

        public final void computeContentWidth() {
            int i;
            int i2;
            Drawable background = getBackground();
            int i3 = 0;
            if (background != null) {
                background.getPadding(AppCompatSpinner.this.mTempRect);
                if (ViewUtils.isLayoutRtl(AppCompatSpinner.this)) {
                    i2 = AppCompatSpinner.this.mTempRect.right;
                } else {
                    i2 = -AppCompatSpinner.this.mTempRect.left;
                }
                i3 = i2;
            } else {
                Rect rect = AppCompatSpinner.this.mTempRect;
                rect.right = 0;
                rect.left = 0;
            }
            int paddingLeft = AppCompatSpinner.this.getPaddingLeft();
            int paddingRight = AppCompatSpinner.this.getPaddingRight();
            int width = AppCompatSpinner.this.getWidth();
            AppCompatSpinner appCompatSpinner = AppCompatSpinner.this;
            int i4 = appCompatSpinner.mDropDownWidth;
            if (i4 == -2) {
                int compatMeasureContentWidth = appCompatSpinner.compatMeasureContentWidth((SpinnerAdapter) this.mAdapter, getBackground());
                int i5 = AppCompatSpinner.this.getContext().getResources().getDisplayMetrics().widthPixels;
                Rect rect2 = AppCompatSpinner.this.mTempRect;
                int i6 = (i5 - rect2.left) - rect2.right;
                if (compatMeasureContentWidth > i6) {
                    compatMeasureContentWidth = i6;
                }
                setContentWidth(Math.max(compatMeasureContentWidth, (width - paddingLeft) - paddingRight));
            } else if (i4 == -1) {
                setContentWidth((width - paddingLeft) - paddingRight);
            } else {
                setContentWidth(i4);
            }
            if (ViewUtils.isLayoutRtl(AppCompatSpinner.this)) {
                i = (((width - paddingRight) - this.mDropDownWidth) - this.mOriginalHorizontalOffset) + i3;
            } else {
                i = paddingLeft + this.mOriginalHorizontalOffset + i3;
            }
            this.mDropDownHorizontalOffset = i;
        }

        @Override // androidx.appcompat.widget.ListPopupWindow, androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final void setAdapter(ListAdapter listAdapter) {
            super.setAdapter(listAdapter);
            this.mAdapter = listAdapter;
        }

        /* JADX WARN: Type inference failed for: r5v2, types: [androidx.appcompat.widget.AppCompatSpinner$DropdownPopup$2, android.view.ViewTreeObserver$OnGlobalLayoutListener] */
        /* JADX WARN: Unknown variable types count: 1 */
        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void show(int r4, int r5) {
            /*
                r3 = this;
                boolean r0 = r3.isShowing()
                r3.computeContentWidth()
                androidx.appcompat.widget.AppCompatPopupWindow r1 = r3.mPopup
                r2 = 2
                r1.setInputMethodMode(r2)
                r3.show()
                androidx.appcompat.widget.DropDownListView r1 = r3.mDropDownList
                r2 = 1
                r1.setChoiceMode(r2)
                r1.setTextDirection(r4)
                r1.setTextAlignment(r5)
                androidx.appcompat.widget.AppCompatSpinner r4 = androidx.appcompat.widget.AppCompatSpinner.this
                int r4 = r4.getSelectedItemPosition()
                androidx.appcompat.widget.DropDownListView r5 = r3.mDropDownList
                boolean r1 = r3.isShowing()
                if (r1 == 0) goto L_0x003b
                if (r5 == 0) goto L_0x003b
                r1 = 0
                r5.mListSelectionHidden = r1
                r5.setSelection(r4)
                int r1 = r5.getChoiceMode()
                if (r1 == 0) goto L_0x003b
                r5.setItemChecked(r4, r2)
            L_0x003b:
                if (r0 == 0) goto L_0x003e
                return
            L_0x003e:
                androidx.appcompat.widget.AppCompatSpinner r4 = androidx.appcompat.widget.AppCompatSpinner.this
                android.view.ViewTreeObserver r4 = r4.getViewTreeObserver()
                if (r4 == 0) goto L_0x0058
                androidx.appcompat.widget.AppCompatSpinner$DropdownPopup$2 r5 = new androidx.appcompat.widget.AppCompatSpinner$DropdownPopup$2
                r5.<init>()
                r4.addOnGlobalLayoutListener(r5)
                androidx.appcompat.widget.AppCompatSpinner$DropdownPopup$3 r4 = new androidx.appcompat.widget.AppCompatSpinner$DropdownPopup$3
                r4.<init>()
                androidx.appcompat.widget.AppCompatPopupWindow r3 = r3.mPopup
                r3.setOnDismissListener(r4)
            L_0x0058:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatSpinner.DropdownPopup.show(int, int):void");
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final void setHorizontalOriginalOffset(int i) {
            this.mOriginalHorizontalOffset = i;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final void setPromptText(CharSequence charSequence) {
            this.mHintText = charSequence;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.SpinnerPopup
        public final CharSequence getHintText() {
            return this.mHintText;
        }
    }

    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.appcompat.widget.AppCompatSpinner.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public boolean mShowDropdown;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mShowDropdown = parcel.readByte() != 0;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeByte(this.mShowDropdown ? (byte) 1 : (byte) 0);
        }
    }

    /* loaded from: classes.dex */
    public interface SpinnerPopup {
        void dismiss();

        Drawable getBackground();

        CharSequence getHintText();

        int getHorizontalOffset();

        int getVerticalOffset();

        boolean isShowing();

        void setAdapter(ListAdapter listAdapter);

        void setBackgroundDrawable(Drawable drawable);

        void setHorizontalOffset(int i);

        void setHorizontalOriginalOffset(int i);

        void setPromptText(CharSequence charSequence);

        void setVerticalOffset(int i);

        void show(int i, int i2);
    }

    public final int compatMeasureContentWidth(SpinnerAdapter spinnerAdapter, Drawable drawable) {
        int i = 0;
        if (spinnerAdapter == null) {
            return 0;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
        int max = Math.max(0, getSelectedItemPosition());
        int min = Math.min(spinnerAdapter.getCount(), max + 15);
        View view = null;
        int i2 = 0;
        for (int max2 = Math.max(0, max - (15 - (min - max))); max2 < min; max2++) {
            int itemViewType = spinnerAdapter.getItemViewType(max2);
            if (itemViewType != i) {
                view = null;
                i = itemViewType;
            }
            view = spinnerAdapter.getView(max2, view, this);
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            }
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            i2 = Math.max(i2, view.getMeasuredWidth());
        }
        if (drawable == null) {
            return i2;
        }
        drawable.getPadding(this.mTempRect);
        Rect rect = this.mTempRect;
        return i2 + rect.left + rect.right;
    }

    @Override // android.widget.Spinner
    public final int getDropDownHorizontalOffset() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            return spinnerPopup.getHorizontalOffset();
        }
        return super.getDropDownHorizontalOffset();
    }

    @Override // android.widget.Spinner
    public final int getDropDownVerticalOffset() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            return spinnerPopup.getVerticalOffset();
        }
        return super.getDropDownVerticalOffset();
    }

    @Override // android.widget.Spinner
    public final int getDropDownWidth() {
        if (this.mPopup != null) {
            return this.mDropDownWidth;
        }
        return super.getDropDownWidth();
    }

    @Override // android.widget.Spinner
    public final Drawable getPopupBackground() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            return spinnerPopup.getBackground();
        }
        return super.getPopupBackground();
    }

    @Override // android.widget.Spinner
    public final CharSequence getPrompt() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            return spinnerPopup.getHintText();
        }
        return super.getPrompt();
    }

    @Override // android.widget.Spinner, android.widget.AbsSpinner, android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        ViewTreeObserver viewTreeObserver;
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.mShowDropdown && (viewTreeObserver = getViewTreeObserver()) != null) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: androidx.appcompat.widget.AppCompatSpinner.2
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public final void onGlobalLayout() {
                    if (!AppCompatSpinner.this.getInternalPopup().isShowing()) {
                        AppCompatSpinner appCompatSpinner = AppCompatSpinner.this;
                        Objects.requireNonNull(appCompatSpinner);
                        appCompatSpinner.mPopup.show(appCompatSpinner.getTextDirection(), appCompatSpinner.getTextAlignment());
                    }
                    ViewTreeObserver viewTreeObserver2 = AppCompatSpinner.this.getViewTreeObserver();
                    if (viewTreeObserver2 != null) {
                        viewTreeObserver2.removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    @Override // android.widget.Spinner, android.widget.AbsSpinner, android.view.View
    public final Parcelable onSaveInstanceState() {
        boolean z;
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup == null || !spinnerPopup.isShowing()) {
            z = false;
        } else {
            z = true;
        }
        savedState.mShowDropdown = z;
        return savedState;
    }

    @Override // android.widget.Spinner, android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        AnonymousClass1 r0 = this.mForwardingListener;
        if (r0 == null || !r0.onTouch(this, motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    @Override // android.widget.Spinner, android.view.View
    public final boolean performClick() {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup == null) {
            return super.performClick();
        }
        if (spinnerPopup.isShowing()) {
            return true;
        }
        this.mPopup.show(getTextDirection(), getTextAlignment());
        return true;
    }

    @Override // android.widget.Spinner, android.widget.AbsSpinner
    public final void setAdapter(SpinnerAdapter spinnerAdapter) {
        if (!this.mPopupSet) {
            this.mTempAdapter = spinnerAdapter;
            return;
        }
        super.setAdapter(spinnerAdapter);
        if (this.mPopup != null) {
            Context context = this.mPopupContext;
            if (context == null) {
                context = getContext();
            }
            this.mPopup.setAdapter(new DropDownAdapter(spinnerAdapter, context.getTheme()));
        }
    }

    @Override // android.widget.Spinner
    public final void setDropDownHorizontalOffset(int i) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setHorizontalOriginalOffset(i);
            this.mPopup.setHorizontalOffset(i);
            return;
        }
        super.setDropDownHorizontalOffset(i);
    }

    @Override // android.widget.Spinner
    public final void setDropDownVerticalOffset(int i) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setVerticalOffset(i);
        } else {
            super.setDropDownVerticalOffset(i);
        }
    }

    @Override // android.widget.Spinner
    public final void setDropDownWidth(int i) {
        if (this.mPopup != null) {
            this.mDropDownWidth = i;
        } else {
            super.setDropDownWidth(i);
        }
    }

    @Override // android.widget.Spinner
    public final void setPopupBackgroundDrawable(Drawable drawable) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setBackgroundDrawable(drawable);
        } else {
            super.setPopupBackgroundDrawable(drawable);
        }
    }

    @Override // android.widget.Spinner
    public final void setPrompt(CharSequence charSequence) {
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null) {
            spinnerPopup.setPromptText(charSequence);
        } else {
            super.setPrompt(charSequence);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0059, code lost:
        if (r5 == null) goto L_0x005e;
     */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00d2  */
    /* JADX WARN: Type inference failed for: r5v4, types: [androidx.appcompat.widget.AppCompatSpinner$1] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public AppCompatSpinner(android.content.Context r12, android.util.AttributeSet r13) {
        /*
            r11 = this;
            r0 = 2130969752(0x7f040498, float:1.7548195E38)
            r11.<init>(r12, r13, r0)
            android.graphics.Rect r1 = new android.graphics.Rect
            r1.<init>()
            r11.mTempRect = r1
            android.content.Context r1 = r11.getContext()
            androidx.appcompat.widget.ThemeUtils.checkAppCompatTheme(r11, r1)
            int[] r1 = androidx.appcompat.R$styleable.Spinner
            r2 = 0
            android.content.res.TypedArray r1 = r12.obtainStyledAttributes(r13, r1, r0, r2)
            androidx.appcompat.widget.AppCompatBackgroundHelper r3 = new androidx.appcompat.widget.AppCompatBackgroundHelper
            r3.<init>(r11)
            r11.mBackgroundTintHelper = r3
            r3 = 4
            int r3 = r1.getResourceId(r3, r2)
            if (r3 == 0) goto L_0x0031
            androidx.appcompat.view.ContextThemeWrapper r4 = new androidx.appcompat.view.ContextThemeWrapper
            r4.<init>(r12, r3)
            r11.mPopupContext = r4
            goto L_0x0033
        L_0x0031:
            r11.mPopupContext = r12
        L_0x0033:
            r3 = 0
            r4 = -1
            int[] r5 = androidx.appcompat.widget.AppCompatSpinner.ATTRS_ANDROID_SPINNERMODE     // Catch: all -> 0x004c, Exception -> 0x004f
            android.content.res.TypedArray r5 = r12.obtainStyledAttributes(r13, r5, r0, r2)     // Catch: all -> 0x004c, Exception -> 0x004f
            boolean r6 = r5.hasValue(r2)     // Catch: all -> 0x0046, Exception -> 0x004a
            if (r6 == 0) goto L_0x005b
            int r4 = r5.getInt(r2, r2)     // Catch: all -> 0x0046, Exception -> 0x004a
            goto L_0x005b
        L_0x0046:
            r11 = move-exception
            r3 = r5
            goto L_0x00d0
        L_0x004a:
            r6 = move-exception
            goto L_0x0052
        L_0x004c:
            r11 = move-exception
            goto L_0x00d0
        L_0x004f:
            r5 = move-exception
            r6 = r5
            r5 = r3
        L_0x0052:
            java.lang.String r7 = "AppCompatSpinner"
            java.lang.String r8 = "Could not read android:spinnerMode"
            android.util.Log.i(r7, r8, r6)     // Catch: all -> 0x0046
            if (r5 == 0) goto L_0x005e
        L_0x005b:
            r5.recycle()
        L_0x005e:
            r5 = 2
            r6 = 1
            if (r4 == 0) goto L_0x0098
            if (r4 == r6) goto L_0x0065
            goto L_0x00a5
        L_0x0065:
            androidx.appcompat.widget.AppCompatSpinner$DropdownPopup r4 = new androidx.appcompat.widget.AppCompatSpinner$DropdownPopup
            android.content.Context r7 = r11.mPopupContext
            r4.<init>(r7, r13)
            android.content.Context r7 = r11.mPopupContext
            int[] r8 = androidx.appcompat.R$styleable.Spinner
            androidx.appcompat.widget.TintTypedArray r7 = androidx.appcompat.widget.TintTypedArray.obtainStyledAttributes(r7, r13, r8, r0)
            r8 = 3
            r9 = -2
            android.content.res.TypedArray r10 = r7.mWrapped
            int r8 = r10.getLayoutDimension(r8, r9)
            r11.mDropDownWidth = r8
            android.graphics.drawable.Drawable r8 = r7.getDrawable(r6)
            r4.setBackgroundDrawable(r8)
            java.lang.String r5 = r1.getString(r5)
            r4.mHintText = r5
            r7.recycle()
            r11.mPopup = r4
            androidx.appcompat.widget.AppCompatSpinner$1 r5 = new androidx.appcompat.widget.AppCompatSpinner$1
            r5.<init>(r11)
            r11.mForwardingListener = r5
            goto L_0x00a5
        L_0x0098:
            androidx.appcompat.widget.AppCompatSpinner$DialogPopup r4 = new androidx.appcompat.widget.AppCompatSpinner$DialogPopup
            r4.<init>()
            r11.mPopup = r4
            java.lang.String r5 = r1.getString(r5)
            r4.mPrompt = r5
        L_0x00a5:
            java.lang.CharSequence[] r2 = r1.getTextArray(r2)
            if (r2 == 0) goto L_0x00bc
            android.widget.ArrayAdapter r4 = new android.widget.ArrayAdapter
            r5 = 17367048(0x1090008, float:2.5162948E-38)
            r4.<init>(r12, r5, r2)
            r12 = 2131624602(0x7f0e029a, float:1.8876388E38)
            r4.setDropDownViewResource(r12)
            r11.setAdapter(r4)
        L_0x00bc:
            r1.recycle()
            r11.mPopupSet = r6
            android.widget.SpinnerAdapter r12 = r11.mTempAdapter
            if (r12 == 0) goto L_0x00ca
            r11.setAdapter(r12)
            r11.mTempAdapter = r3
        L_0x00ca:
            androidx.appcompat.widget.AppCompatBackgroundHelper r11 = r11.mBackgroundTintHelper
            r11.loadFromAttributes(r13, r0)
            return
        L_0x00d0:
            if (r3 == 0) goto L_0x00d5
            r3.recycle()
        L_0x00d5:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatSpinner.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void drawableStateChanged() {
        super.drawableStateChanged();
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.applySupportBackgroundTint();
        }
    }

    @Override // android.widget.Spinner, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SpinnerPopup spinnerPopup = this.mPopup;
        if (spinnerPopup != null && spinnerPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    @Override // android.widget.Spinner, android.widget.AbsSpinner, android.view.View
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mPopup != null && View.MeasureSpec.getMode(i) == Integer.MIN_VALUE) {
            setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), compatMeasureContentWidth(getAdapter(), getBackground())), View.MeasureSpec.getSize(i)), getMeasuredHeight());
        }
    }

    @Override // android.view.View
    public final void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundDrawable();
        }
    }

    @Override // android.view.View
    public final void setBackgroundResource(int i) {
        super.setBackgroundResource(i);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundResource(i);
        }
    }

    @Override // android.widget.Spinner
    public final void setPopupBackgroundResource(int i) {
        setPopupBackgroundDrawable(AppCompatResources.getDrawable(getPopupContext(), i));
    }

    public final SpinnerPopup getInternalPopup() {
        return this.mPopup;
    }

    @Override // android.widget.Spinner
    public final Context getPopupContext() {
        return this.mPopupContext;
    }
}
