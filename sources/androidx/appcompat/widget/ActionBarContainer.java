package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.R$styleable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class ActionBarContainer extends FrameLayout {
    public View mActionBarView;
    public Drawable mBackground;
    public View mContextView;
    public int mHeight;
    public boolean mIsSplit;
    public boolean mIsStacked;
    public boolean mIsTransitioning;
    public Drawable mSplitBackground;
    public Drawable mStackedBackground;

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final ActionMode startActionModeForChild(View view, ActionMode.Callback callback) {
        return null;
    }

    public ActionBarContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ActionBarBackgroundDrawable actionBarBackgroundDrawable = new ActionBarBackgroundDrawable(this);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.setBackground(this, actionBarBackgroundDrawable);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ActionBar);
        boolean z = false;
        this.mBackground = obtainStyledAttributes.getDrawable(0);
        this.mStackedBackground = obtainStyledAttributes.getDrawable(2);
        this.mHeight = obtainStyledAttributes.getDimensionPixelSize(13, -1);
        if (getId() == 2131428897) {
            this.mIsSplit = true;
            this.mSplitBackground = obtainStyledAttributes.getDrawable(1);
        }
        obtainStyledAttributes.recycle();
        if (!this.mIsSplit ? this.mBackground == null && this.mStackedBackground == null : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mIsTransitioning || super.onInterceptTouchEvent(motionEvent)) {
            return true;
        }
        return false;
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        int i3;
        if (this.mActionBarView == null && View.MeasureSpec.getMode(i2) == Integer.MIN_VALUE && (i3 = this.mHeight) >= 0) {
            i2 = View.MeasureSpec.makeMeasureSpec(Math.min(i3, View.MeasureSpec.getSize(i2)), Integer.MIN_VALUE);
        }
        super.onMeasure(i, i2);
        if (this.mActionBarView != null) {
            View.MeasureSpec.getMode(i2);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final ActionMode startActionModeForChild(View view, ActionMode.Callback callback, int i) {
        if (i != 0) {
            return super.startActionModeForChild(view, callback, i);
        }
        return null;
    }

    @Override // android.view.View
    public final boolean verifyDrawable(Drawable drawable) {
        if ((drawable != this.mBackground || this.mIsSplit) && ((drawable != this.mStackedBackground || !this.mIsStacked) && ((drawable != this.mSplitBackground || !this.mIsSplit) && !super.verifyDrawable(drawable)))) {
            return false;
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mBackground;
        if (drawable != null && drawable.isStateful()) {
            this.mBackground.setState(getDrawableState());
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null && drawable2.isStateful()) {
            this.mStackedBackground.setState(getDrawableState());
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 != null && drawable3.isStateful()) {
            this.mSplitBackground.setState(getDrawableState());
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 != null) {
            drawable3.jumpToCurrentState();
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = findViewById(2131427412);
        this.mContextView = findViewById(2131427420);
    }

    @Override // android.view.View
    public final boolean onHoverEvent(MotionEvent motionEvent) {
        super.onHoverEvent(motionEvent);
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onLayout(boolean r3, int r4, int r5, int r6, int r7) {
        /*
            r2 = this;
            super.onLayout(r3, r4, r5, r6, r7)
            boolean r3 = r2.mIsSplit
            r4 = 0
            r5 = 1
            if (r3 == 0) goto L_0x0019
            android.graphics.drawable.Drawable r3 = r2.mSplitBackground
            if (r3 == 0) goto L_0x0075
            int r6 = r2.getMeasuredWidth()
            int r7 = r2.getMeasuredHeight()
            r3.setBounds(r4, r4, r6, r7)
            goto L_0x0076
        L_0x0019:
            android.graphics.drawable.Drawable r3 = r2.mBackground
            if (r3 == 0) goto L_0x0071
            android.view.View r3 = r2.mActionBarView
            int r3 = r3.getVisibility()
            if (r3 != 0) goto L_0x0043
            android.graphics.drawable.Drawable r3 = r2.mBackground
            android.view.View r6 = r2.mActionBarView
            int r6 = r6.getLeft()
            android.view.View r7 = r2.mActionBarView
            int r7 = r7.getTop()
            android.view.View r0 = r2.mActionBarView
            int r0 = r0.getRight()
            android.view.View r1 = r2.mActionBarView
            int r1 = r1.getBottom()
            r3.setBounds(r6, r7, r0, r1)
            goto L_0x0072
        L_0x0043:
            android.view.View r3 = r2.mContextView
            if (r3 == 0) goto L_0x006b
            int r3 = r3.getVisibility()
            if (r3 != 0) goto L_0x006b
            android.graphics.drawable.Drawable r3 = r2.mBackground
            android.view.View r6 = r2.mContextView
            int r6 = r6.getLeft()
            android.view.View r7 = r2.mContextView
            int r7 = r7.getTop()
            android.view.View r0 = r2.mContextView
            int r0 = r0.getRight()
            android.view.View r1 = r2.mContextView
            int r1 = r1.getBottom()
            r3.setBounds(r6, r7, r0, r1)
            goto L_0x0072
        L_0x006b:
            android.graphics.drawable.Drawable r3 = r2.mBackground
            r3.setBounds(r4, r4, r4, r4)
            goto L_0x0072
        L_0x0071:
            r5 = r4
        L_0x0072:
            r2.mIsStacked = r4
            r4 = r5
        L_0x0075:
            r5 = r4
        L_0x0076:
            if (r5 == 0) goto L_0x007b
            r2.invalidate()
        L_0x007b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ActionBarContainer.onLayout(boolean, int, int, int, int):void");
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    @Override // android.view.View
    public final void setVisibility(int i) {
        boolean z;
        super.setVisibility(i);
        if (i == 0) {
            z = true;
        } else {
            z = false;
        }
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setVisible(z, false);
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null) {
            drawable2.setVisible(z, false);
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 != null) {
            drawable3.setVisible(z, false);
        }
    }
}
