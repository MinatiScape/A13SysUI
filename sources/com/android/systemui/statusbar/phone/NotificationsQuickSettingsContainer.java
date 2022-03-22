package com.android.systemui.statusbar.phone;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.statusbar.notification.AboveShelfObserver;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda28;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;
/* loaded from: classes.dex */
public class NotificationsQuickSettingsContainer extends ConstraintLayout implements FragmentHostManager.FragmentListener, AboveShelfObserver.HasViewAboveShelfChangedListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public View mKeyguardStatusBar;
    public View mQSContainer;
    public View mQSScrollView;
    public QS mQs;
    public View mQsFrame;
    public View mStackScroller;
    public ArrayList<View> mDrawingOrderedChildren = new ArrayList<>();
    public ArrayList<View> mLayoutDrawingOrder = new ArrayList<>();
    public final Comparator<View> mIndexComparator = Comparator.comparingInt(new ToIntFunction() { // from class: com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer$$ExternalSyntheticLambda1
        @Override // java.util.function.ToIntFunction
        public final int applyAsInt(Object obj) {
            return NotificationsQuickSettingsContainer.this.indexOfChild((View) obj);
        }
    });
    public Consumer<WindowInsets> mInsetsChangedListener = BubbleStackView$$ExternalSyntheticLambda28.INSTANCE$1;
    public Consumer<QS> mQSFragmentAttachedListener = NotificationsQuickSettingsContainer$$ExternalSyntheticLambda0.INSTANCE;

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public final void dispatchDraw(Canvas canvas) {
        this.mDrawingOrderedChildren.clear();
        this.mLayoutDrawingOrder.clear();
        if (this.mKeyguardStatusBar.getVisibility() == 0) {
            this.mDrawingOrderedChildren.add(this.mKeyguardStatusBar);
            this.mLayoutDrawingOrder.add(this.mKeyguardStatusBar);
        }
        if (this.mQsFrame.getVisibility() == 0) {
            this.mDrawingOrderedChildren.add(this.mQsFrame);
            this.mLayoutDrawingOrder.add(this.mQsFrame);
        }
        if (this.mStackScroller.getVisibility() == 0) {
            this.mDrawingOrderedChildren.add(this.mStackScroller);
            this.mLayoutDrawingOrder.add(this.mStackScroller);
        }
        this.mLayoutDrawingOrder.sort(this.mIndexComparator);
        super.dispatchDraw(canvas);
    }

    @Override // android.view.ViewGroup
    public final boolean drawChild(Canvas canvas, View view, long j) {
        int indexOf = this.mLayoutDrawingOrder.indexOf(view);
        if (indexOf >= 0) {
            return super.drawChild(canvas, this.mDrawingOrderedChildren.get(indexOf), j);
        }
        return super.drawChild(canvas, view, j);
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mInsetsChangedListener.accept(windowInsets);
        return windowInsets;
    }

    @Override // com.android.systemui.fragments.FragmentHostManager.FragmentListener
    public final void onFragmentViewCreated(Fragment fragment) {
        QS qs = (QS) fragment;
        this.mQs = qs;
        this.mQSFragmentAttachedListener.accept(qs);
        this.mQSScrollView = this.mQs.getView().findViewById(2131427951);
        this.mQSContainer = this.mQs.getView().findViewById(2131428661);
    }

    public NotificationsQuickSettingsContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        FragmentHostManager.get(this).addTagListener(QS.TAG, this);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        FragmentHostManager fragmentHostManager = FragmentHostManager.get(this);
        Objects.requireNonNull(fragmentHostManager);
        ArrayList<FragmentHostManager.FragmentListener> arrayList = fragmentHostManager.mListeners.get(QS.TAG);
        if (arrayList != null && arrayList.remove(this) && arrayList.size() == 0) {
            fragmentHostManager.mListeners.remove(QS.TAG);
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mQsFrame = findViewById(2131428651);
        this.mStackScroller = findViewById(2131428521);
        this.mKeyguardStatusBar = findViewById(2131428173);
    }

    @Override // com.android.systemui.statusbar.notification.AboveShelfObserver.HasViewAboveShelfChangedListener
    public final void onHasViewsAboveShelfChanged() {
        invalidate();
    }
}
