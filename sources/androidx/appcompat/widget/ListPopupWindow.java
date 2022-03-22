package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import androidx.appcompat.R$styleable;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class ListPopupWindow implements ShowableListMenu {
    public ListAdapter mAdapter;
    public Context mContext;
    public View mDropDownAnchorView;
    public int mDropDownGravity;
    public int mDropDownHeight;
    public int mDropDownHorizontalOffset;
    public DropDownListView mDropDownList;
    public int mDropDownVerticalOffset;
    public boolean mDropDownVerticalOffsetSet;
    public int mDropDownWidth;
    public int mDropDownWindowLayoutType;
    public Rect mEpicenterBounds;
    public final Handler mHandler;
    public final ListSelectorHider mHideSelector;
    public AdapterView.OnItemClickListener mItemClickListener;
    public int mListItemExpandMaximum;
    public boolean mModal;
    public PopupDataSetObserver mObserver;
    public boolean mOverlapAnchor;
    public boolean mOverlapAnchorSet;
    public AppCompatPopupWindow mPopup;
    public final ResizePopupRunnable mResizePopupRunnable;
    public final PopupScrollListener mScrollListener;
    public final Rect mTempRect;
    public final PopupTouchInterceptor mTouchInterceptor;

    /* loaded from: classes.dex */
    public class ListSelectorHider implements Runnable {
        public ListSelectorHider() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            ListPopupWindow listPopupWindow = ListPopupWindow.this;
            Objects.requireNonNull(listPopupWindow);
            DropDownListView dropDownListView = listPopupWindow.mDropDownList;
            if (dropDownListView != null) {
                dropDownListView.mListSelectionHidden = true;
                dropDownListView.requestLayout();
            }
        }
    }

    /* loaded from: classes.dex */
    public class PopupDataSetObserver extends DataSetObserver {
        public PopupDataSetObserver() {
        }

        @Override // android.database.DataSetObserver
        public final void onChanged() {
            if (ListPopupWindow.this.isShowing()) {
                ListPopupWindow.this.show();
            }
        }

        @Override // android.database.DataSetObserver
        public final void onInvalidated() {
            ListPopupWindow.this.dismiss();
        }
    }

    /* loaded from: classes.dex */
    public class PopupScrollListener implements AbsListView.OnScrollListener {
        @Override // android.widget.AbsListView.OnScrollListener
        public final void onScroll(AbsListView absListView, int i, int i2, int i3) {
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public final void onScrollStateChanged(AbsListView absListView, int i) {
            boolean z = true;
            if (i == 1) {
                ListPopupWindow listPopupWindow = ListPopupWindow.this;
                Objects.requireNonNull(listPopupWindow);
                if (listPopupWindow.mPopup.getInputMethodMode() != 2) {
                    z = false;
                }
                if (!z && ListPopupWindow.this.mPopup.getContentView() != null) {
                    ListPopupWindow listPopupWindow2 = ListPopupWindow.this;
                    listPopupWindow2.mHandler.removeCallbacks(listPopupWindow2.mResizePopupRunnable);
                    ListPopupWindow.this.mResizePopupRunnable.run();
                }
            }
        }

        public PopupScrollListener() {
        }
    }

    /* loaded from: classes.dex */
    public class PopupTouchInterceptor implements View.OnTouchListener {
        public PopupTouchInterceptor() {
        }

        @Override // android.view.View.OnTouchListener
        public final boolean onTouch(View view, MotionEvent motionEvent) {
            AppCompatPopupWindow appCompatPopupWindow;
            int action = motionEvent.getAction();
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (action == 0 && (appCompatPopupWindow = ListPopupWindow.this.mPopup) != null && appCompatPopupWindow.isShowing() && x >= 0 && x < ListPopupWindow.this.mPopup.getWidth() && y >= 0 && y < ListPopupWindow.this.mPopup.getHeight()) {
                ListPopupWindow listPopupWindow = ListPopupWindow.this;
                listPopupWindow.mHandler.postDelayed(listPopupWindow.mResizePopupRunnable, 250L);
                return false;
            } else if (action != 1) {
                return false;
            } else {
                ListPopupWindow listPopupWindow2 = ListPopupWindow.this;
                listPopupWindow2.mHandler.removeCallbacks(listPopupWindow2.mResizePopupRunnable);
                return false;
            }
        }
    }

    /* loaded from: classes.dex */
    public class ResizePopupRunnable implements Runnable {
        public ResizePopupRunnable() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            DropDownListView dropDownListView = ListPopupWindow.this.mDropDownList;
            if (dropDownListView != null) {
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                if (ViewCompat.Api19Impl.isAttachedToWindow(dropDownListView) && ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount()) {
                    int childCount = ListPopupWindow.this.mDropDownList.getChildCount();
                    ListPopupWindow listPopupWindow = ListPopupWindow.this;
                    if (childCount <= listPopupWindow.mListItemExpandMaximum) {
                        listPopupWindow.mPopup.setInputMethodMode(2);
                        ListPopupWindow.this.show();
                    }
                }
            }
        }
    }

    public ListPopupWindow(Context context) {
        this(context, null, 2130969376, 0);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130969376, 0);
    }

    public DropDownListView createDropDownListView(Context context, boolean z) {
        return new DropDownListView(context, z);
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public final void dismiss() {
        this.mPopup.dismiss();
        this.mPopup.setContentView(null);
        this.mDropDownList = null;
        this.mHandler.removeCallbacks(this.mResizePopupRunnable);
    }

    public final Drawable getBackground() {
        return this.mPopup.getBackground();
    }

    public final int getVerticalOffset() {
        if (!this.mDropDownVerticalOffsetSet) {
            return 0;
        }
        return this.mDropDownVerticalOffset;
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public final boolean isShowing() {
        return this.mPopup.isShowing();
    }

    public void setAdapter(ListAdapter listAdapter) {
        PopupDataSetObserver popupDataSetObserver = this.mObserver;
        if (popupDataSetObserver == null) {
            this.mObserver = new PopupDataSetObserver();
        } else {
            ListAdapter listAdapter2 = this.mAdapter;
            if (listAdapter2 != null) {
                listAdapter2.unregisterDataSetObserver(popupDataSetObserver);
            }
        }
        this.mAdapter = listAdapter;
        if (listAdapter != null) {
            listAdapter.registerDataSetObserver(this.mObserver);
        }
        DropDownListView dropDownListView = this.mDropDownList;
        if (dropDownListView != null) {
            dropDownListView.setAdapter(this.mAdapter);
        }
    }

    public final void setBackgroundDrawable(Drawable drawable) {
        this.mPopup.setBackgroundDrawable(drawable);
    }

    public final void setContentWidth(int i) {
        Drawable background = this.mPopup.getBackground();
        if (background != null) {
            background.getPadding(this.mTempRect);
            Rect rect = this.mTempRect;
            this.mDropDownWidth = rect.left + rect.right + i;
            return;
        }
        this.mDropDownWidth = i;
    }

    public final void setVerticalOffset(int i) {
        this.mDropDownVerticalOffset = i;
        this.mDropDownVerticalOffsetSet = true;
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public final void show() {
        int i;
        boolean z;
        int i2;
        boolean z2;
        DropDownListView dropDownListView;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        if (this.mDropDownList == null) {
            DropDownListView createDropDownListView = createDropDownListView(this.mContext, !this.mModal);
            this.mDropDownList = createDropDownListView;
            createDropDownListView.setAdapter(this.mAdapter);
            this.mDropDownList.setOnItemClickListener(this.mItemClickListener);
            this.mDropDownList.setFocusable(true);
            this.mDropDownList.setFocusableInTouchMode(true);
            this.mDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: androidx.appcompat.widget.ListPopupWindow.3
                @Override // android.widget.AdapterView.OnItemSelectedListener
                public final void onItemSelected(AdapterView<?> adapterView, View view, int i8, long j) {
                    DropDownListView dropDownListView2;
                    if (i8 != -1 && (dropDownListView2 = ListPopupWindow.this.mDropDownList) != null) {
                        dropDownListView2.mListSelectionHidden = false;
                    }
                }

                @Override // android.widget.AdapterView.OnItemSelectedListener
                public final void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            this.mDropDownList.setOnScrollListener(this.mScrollListener);
            this.mPopup.setContentView(this.mDropDownList);
        } else {
            ViewGroup viewGroup = (ViewGroup) this.mPopup.getContentView();
        }
        Drawable background = this.mPopup.getBackground();
        int i8 = 0;
        if (background != null) {
            background.getPadding(this.mTempRect);
            Rect rect = this.mTempRect;
            int i9 = rect.top;
            i = rect.bottom + i9;
            if (!this.mDropDownVerticalOffsetSet) {
                this.mDropDownVerticalOffset = -i9;
            }
        } else {
            this.mTempRect.setEmpty();
            i = 0;
        }
        if (this.mPopup.getInputMethodMode() == 2) {
            z = true;
        } else {
            z = false;
        }
        int maxAvailableHeight = this.mPopup.getMaxAvailableHeight(this.mDropDownAnchorView, this.mDropDownVerticalOffset, z);
        if (this.mDropDownHeight == -1) {
            i2 = maxAvailableHeight + i;
        } else {
            int i10 = this.mDropDownWidth;
            if (i10 == -2) {
                int i11 = this.mContext.getResources().getDisplayMetrics().widthPixels;
                Rect rect2 = this.mTempRect;
                i6 = View.MeasureSpec.makeMeasureSpec(i11 - (rect2.left + rect2.right), Integer.MIN_VALUE);
            } else if (i10 != -1) {
                i6 = View.MeasureSpec.makeMeasureSpec(i10, 1073741824);
            } else {
                int i12 = this.mContext.getResources().getDisplayMetrics().widthPixels;
                Rect rect3 = this.mTempRect;
                i6 = View.MeasureSpec.makeMeasureSpec(i12 - (rect3.left + rect3.right), 1073741824);
            }
            int measureHeightOfChildrenCompat = this.mDropDownList.measureHeightOfChildrenCompat(i6, maxAvailableHeight + 0);
            if (measureHeightOfChildrenCompat > 0) {
                i7 = this.mDropDownList.getPaddingBottom() + this.mDropDownList.getPaddingTop() + i + 0;
            } else {
                i7 = 0;
            }
            i2 = measureHeightOfChildrenCompat + i7;
        }
        if (this.mPopup.getInputMethodMode() == 2) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mPopup.setWindowLayoutType(this.mDropDownWindowLayoutType);
        if (this.mPopup.isShowing()) {
            View view = this.mDropDownAnchorView;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            if (ViewCompat.Api19Impl.isAttachedToWindow(view)) {
                int i13 = this.mDropDownWidth;
                if (i13 == -1) {
                    i13 = -1;
                } else if (i13 == -2) {
                    i13 = this.mDropDownAnchorView.getWidth();
                }
                int i14 = this.mDropDownHeight;
                if (i14 == -1) {
                    if (!z2) {
                        i2 = -1;
                    }
                    if (z2) {
                        AppCompatPopupWindow appCompatPopupWindow = this.mPopup;
                        if (this.mDropDownWidth == -1) {
                            i5 = -1;
                        } else {
                            i5 = 0;
                        }
                        appCompatPopupWindow.setWidth(i5);
                        this.mPopup.setHeight(0);
                    } else {
                        AppCompatPopupWindow appCompatPopupWindow2 = this.mPopup;
                        if (this.mDropDownWidth == -1) {
                            i8 = -1;
                        }
                        appCompatPopupWindow2.setWidth(i8);
                        this.mPopup.setHeight(-1);
                    }
                } else if (i14 != -2) {
                    i2 = i14;
                }
                this.mPopup.setOutsideTouchable(true);
                AppCompatPopupWindow appCompatPopupWindow3 = this.mPopup;
                View view2 = this.mDropDownAnchorView;
                int i15 = this.mDropDownHorizontalOffset;
                int i16 = this.mDropDownVerticalOffset;
                if (i13 < 0) {
                    i3 = -1;
                } else {
                    i3 = i13;
                }
                if (i2 < 0) {
                    i4 = -1;
                } else {
                    i4 = i2;
                }
                appCompatPopupWindow3.update(view2, i15, i16, i3, i4);
                return;
            }
            return;
        }
        int i17 = this.mDropDownWidth;
        if (i17 == -1) {
            i17 = -1;
        } else if (i17 == -2) {
            i17 = this.mDropDownAnchorView.getWidth();
        }
        int i18 = this.mDropDownHeight;
        if (i18 == -1) {
            i2 = -1;
        } else if (i18 != -2) {
            i2 = i18;
        }
        this.mPopup.setWidth(i17);
        this.mPopup.setHeight(i2);
        this.mPopup.setIsClippedToScreen(true);
        this.mPopup.setOutsideTouchable(true);
        this.mPopup.setTouchInterceptor(this.mTouchInterceptor);
        if (this.mOverlapAnchorSet) {
            this.mPopup.setOverlapAnchor(this.mOverlapAnchor);
        }
        this.mPopup.setEpicenterBounds(this.mEpicenterBounds);
        this.mPopup.showAsDropDown(this.mDropDownAnchorView, this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
        this.mDropDownList.setSelection(-1);
        if ((!this.mModal || this.mDropDownList.isInTouchMode()) && (dropDownListView = this.mDropDownList) != null) {
            dropDownListView.mListSelectionHidden = true;
            dropDownListView.requestLayout();
        }
        if (!this.mModal) {
            this.mHandler.post(this.mHideSelector);
        }
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet, int i, int i2) {
        this.mDropDownHeight = -2;
        this.mDropDownWidth = -2;
        this.mDropDownWindowLayoutType = 1002;
        this.mDropDownGravity = 0;
        this.mListItemExpandMaximum = Integer.MAX_VALUE;
        this.mResizePopupRunnable = new ResizePopupRunnable();
        this.mTouchInterceptor = new PopupTouchInterceptor();
        this.mScrollListener = new PopupScrollListener();
        this.mHideSelector = new ListSelectorHider();
        this.mTempRect = new Rect();
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper());
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ListPopupWindow, i, i2);
        this.mDropDownHorizontalOffset = obtainStyledAttributes.getDimensionPixelOffset(0, 0);
        int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(1, 0);
        this.mDropDownVerticalOffset = dimensionPixelOffset;
        if (dimensionPixelOffset != 0) {
            this.mDropDownVerticalOffsetSet = true;
        }
        obtainStyledAttributes.recycle();
        AppCompatPopupWindow appCompatPopupWindow = new AppCompatPopupWindow(context, attributeSet, i, i2);
        this.mPopup = appCompatPopupWindow;
        appCompatPopupWindow.setInputMethodMode(1);
    }

    public final void setHorizontalOffset(int i) {
        this.mDropDownHorizontalOffset = i;
    }

    public final int getHorizontalOffset() {
        return this.mDropDownHorizontalOffset;
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public final DropDownListView getListView() {
        return this.mDropDownList;
    }
}
