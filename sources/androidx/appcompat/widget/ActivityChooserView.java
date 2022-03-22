package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.appcompat.R$styleable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class ActivityChooserView extends ViewGroup {
    public final View mActivityChooserContent;
    public final ActivityChooserViewAdapter mAdapter;
    public final Callbacks mCallbacks;
    public final FrameLayout mDefaultActivityButton;
    public final FrameLayout mExpandActivityOverflowButton;
    public int mInitialActivityCount;
    public boolean mIsAttachedToWindow;
    public ListPopupWindow mListPopupWindow;
    public final AnonymousClass1 mModelDataSetObserver = new DataSetObserver() { // from class: androidx.appcompat.widget.ActivityChooserView.1
        @Override // android.database.DataSetObserver
        public final void onChanged() {
            super.onChanged();
            ActivityChooserView.this.mAdapter.notifyDataSetChanged();
        }

        @Override // android.database.DataSetObserver
        public final void onInvalidated() {
            super.onInvalidated();
            ActivityChooserView.this.mAdapter.notifyDataSetInvalidated();
        }
    };
    public final AnonymousClass2 mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: androidx.appcompat.widget.ActivityChooserView.2
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public final void onGlobalLayout() {
            ActivityChooserView activityChooserView = ActivityChooserView.this;
            Objects.requireNonNull(activityChooserView);
            if (!activityChooserView.getListPopupWindow().isShowing()) {
                return;
            }
            if (!ActivityChooserView.this.isShown()) {
                ActivityChooserView.this.getListPopupWindow().dismiss();
                return;
            }
            ActivityChooserView.this.getListPopupWindow().show();
            Objects.requireNonNull(ActivityChooserView.this);
        }
    };

    /* loaded from: classes.dex */
    public class ActivityChooserViewAdapter extends BaseAdapter {
        public int mMaxActivityCount = 4;
        public boolean mShowDefaultActivity;
        public boolean mShowFooterView;

        @Override // android.widget.Adapter
        public final long getItemId(int i) {
            return i;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public final int getViewTypeCount() {
            return 3;
        }

        public ActivityChooserViewAdapter() {
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public final int getItemViewType(int i) {
            if (!this.mShowFooterView) {
                return 0;
            }
            throw null;
        }

        @Override // android.widget.Adapter
        public final Object getItem(int i) {
            getItemViewType(i);
            if (!this.mShowDefaultActivity) {
                throw null;
            }
            throw null;
        }

        @Override // android.widget.Adapter
        public final View getView(int i, View view, ViewGroup viewGroup) {
            getItemViewType(i);
            if (view == null || view.getId() != 2131428264) {
                view = LayoutInflater.from(ActivityChooserView.this.getContext()).inflate(2131623943, viewGroup, false);
            }
            ActivityChooserView.this.getContext().getPackageManager();
            ImageView imageView = (ImageView) view.findViewById(2131428102);
            getItem(i);
            throw null;
        }

        @Override // android.widget.Adapter
        public final int getCount() {
            throw null;
        }
    }

    /* loaded from: classes.dex */
    public class Callbacks implements AdapterView.OnItemClickListener, View.OnClickListener, View.OnLongClickListener, PopupWindow.OnDismissListener {
        public Callbacks() {
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            ActivityChooserView activityChooserView = ActivityChooserView.this;
            if (view == activityChooserView.mDefaultActivityButton) {
                activityChooserView.dismissPopup();
                Objects.requireNonNull(ActivityChooserView.this.mAdapter);
                throw null;
            } else if (view == activityChooserView.mExpandActivityOverflowButton) {
                Objects.requireNonNull(activityChooserView.mAdapter);
                throw new IllegalStateException("No data model. Did you call #setDataModel?");
            } else {
                throw new IllegalArgumentException();
            }
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public final void onDismiss() {
            Objects.requireNonNull(ActivityChooserView.this);
            Objects.requireNonNull(ActivityChooserView.this);
        }

        @Override // android.view.View.OnLongClickListener
        public final boolean onLongClick(View view) {
            ActivityChooserView activityChooserView = ActivityChooserView.this;
            if (view == activityChooserView.mDefaultActivityButton) {
                Objects.requireNonNull(activityChooserView.mAdapter);
                throw null;
            }
            throw new IllegalArgumentException();
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public final void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            ((ActivityChooserViewAdapter) adapterView.getAdapter()).getItemViewType(i);
            ActivityChooserView.this.dismissPopup();
            ActivityChooserView activityChooserView = ActivityChooserView.this;
            Objects.requireNonNull(activityChooserView);
            Objects.requireNonNull(activityChooserView.mAdapter);
            Objects.requireNonNull(ActivityChooserView.this.mAdapter);
            throw null;
        }
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [androidx.appcompat.widget.ActivityChooserView$1] */
    /* JADX WARN: Type inference failed for: r1v1, types: [androidx.appcompat.widget.ActivityChooserView$2] */
    public ActivityChooserView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        this.mInitialActivityCount = 4;
        int[] iArr = R$styleable.ActivityChooserView;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, 0, 0);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, 0, 0);
        this.mInitialActivityCount = obtainStyledAttributes.getInt(1, 4);
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        LayoutInflater.from(getContext()).inflate(2131623942, (ViewGroup) this, true);
        Callbacks callbacks = new Callbacks();
        this.mCallbacks = callbacks;
        View findViewById = findViewById(2131427460);
        this.mActivityChooserContent = findViewById;
        findViewById.getBackground();
        FrameLayout frameLayout = (FrameLayout) findViewById(2131427806);
        this.mDefaultActivityButton = frameLayout;
        frameLayout.setOnClickListener(callbacks);
        frameLayout.setOnLongClickListener(callbacks);
        ImageView imageView = (ImageView) frameLayout.findViewById(2131428115);
        FrameLayout frameLayout2 = (FrameLayout) findViewById(2131427943);
        frameLayout2.setOnClickListener(callbacks);
        frameLayout2.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: androidx.appcompat.widget.ActivityChooserView.3
            @Override // android.view.View.AccessibilityDelegate
            public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setCanOpenPopup(true);
            }
        });
        frameLayout2.setOnTouchListener(new ForwardingListener(frameLayout2) { // from class: androidx.appcompat.widget.ActivityChooserView.4
            @Override // androidx.appcompat.widget.ForwardingListener
            public final ShowableListMenu getPopup() {
                return ActivityChooserView.this.getListPopupWindow();
            }

            @Override // androidx.appcompat.widget.ForwardingListener
            public final boolean onForwardingStarted() {
                ActivityChooserView activityChooserView = ActivityChooserView.this;
                Objects.requireNonNull(activityChooserView);
                if (activityChooserView.getListPopupWindow().isShowing() || !activityChooserView.mIsAttachedToWindow) {
                    return true;
                }
                Objects.requireNonNull(activityChooserView.mAdapter);
                throw new IllegalStateException("No data model. Did you call #setDataModel?");
            }

            @Override // androidx.appcompat.widget.ForwardingListener
            public final boolean onForwardingStopped() {
                ActivityChooserView.this.dismissPopup();
                return true;
            }
        });
        this.mExpandActivityOverflowButton = frameLayout2;
        ((ImageView) frameLayout2.findViewById(2131428115)).setImageDrawable(drawable);
        ActivityChooserViewAdapter activityChooserViewAdapter = new ActivityChooserViewAdapter();
        this.mAdapter = activityChooserViewAdapter;
        activityChooserViewAdapter.registerDataSetObserver(new DataSetObserver() { // from class: androidx.appcompat.widget.ActivityChooserView.5
            @Override // android.database.DataSetObserver
            public final void onChanged() {
                super.onChanged();
                ActivityChooserView activityChooserView = ActivityChooserView.this;
                Objects.requireNonNull(activityChooserView);
                Objects.requireNonNull(activityChooserView.mAdapter);
                throw null;
            }
        });
        Resources resources = context.getResources();
        Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(2131165209));
    }

    /* loaded from: classes.dex */
    public static class InnerLayout extends LinearLayout {
        public static final int[] TINT_ATTRS = {16842964};

        public InnerLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            Drawable drawable;
            int resourceId;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, TINT_ATTRS);
            if (!obtainStyledAttributes.hasValue(0) || (resourceId = obtainStyledAttributes.getResourceId(0, 0)) == 0) {
                drawable = obtainStyledAttributes.getDrawable(0);
            } else {
                drawable = AppCompatResources.getDrawable(context, resourceId);
            }
            setBackgroundDrawable(drawable);
            obtainStyledAttributes.recycle();
        }
    }

    public final ListPopupWindow getListPopupWindow() {
        if (this.mListPopupWindow == null) {
            ListPopupWindow listPopupWindow = new ListPopupWindow(getContext());
            this.mListPopupWindow = listPopupWindow;
            listPopupWindow.setAdapter(this.mAdapter);
            ListPopupWindow listPopupWindow2 = this.mListPopupWindow;
            Objects.requireNonNull(listPopupWindow2);
            listPopupWindow2.mDropDownAnchorView = this;
            ListPopupWindow listPopupWindow3 = this.mListPopupWindow;
            Objects.requireNonNull(listPopupWindow3);
            listPopupWindow3.mModal = true;
            listPopupWindow3.mPopup.setFocusable(true);
            ListPopupWindow listPopupWindow4 = this.mListPopupWindow;
            Callbacks callbacks = this.mCallbacks;
            Objects.requireNonNull(listPopupWindow4);
            listPopupWindow4.mItemClickListener = callbacks;
            ListPopupWindow listPopupWindow5 = this.mListPopupWindow;
            Callbacks callbacks2 = this.mCallbacks;
            Objects.requireNonNull(listPopupWindow5);
            listPopupWindow5.mPopup.setOnDismissListener(callbacks2);
        }
        return this.mListPopupWindow;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mActivityChooserContent.layout(0, 0, i3 - i, i4 - i2);
        if (!getListPopupWindow().isShowing()) {
            dismissPopup();
        }
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        View view = this.mActivityChooserContent;
        if (this.mDefaultActivityButton.getVisibility() != 0) {
            i2 = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824);
        }
        measureChild(view, i, i2);
        setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    public final void dismissPopup() {
        if (getListPopupWindow().isShowing()) {
            getListPopupWindow().dismiss();
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        Objects.requireNonNull(this.mAdapter);
        this.mIsAttachedToWindow = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Objects.requireNonNull(this.mAdapter);
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
        }
        if (getListPopupWindow().isShowing()) {
            dismissPopup();
        }
        this.mIsAttachedToWindow = false;
    }
}
