package androidx.slice.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class LocationBasedViewTracker implements Runnable, View.OnLayoutChangeListener {
    public final Rect mFocusRect;
    public final ViewGroup mParent;
    public final SelectionLogic mSelectionLogic;
    public static final AnonymousClass1 INPUT_FOCUS = new SelectionLogic() { // from class: androidx.slice.widget.LocationBasedViewTracker.1
        @Override // androidx.slice.widget.LocationBasedViewTracker.SelectionLogic
        public final void selectView(View view) {
            view.requestFocus();
        }
    };
    public static final AnonymousClass2 A11Y_FOCUS = new SelectionLogic() { // from class: androidx.slice.widget.LocationBasedViewTracker.2
        @Override // androidx.slice.widget.LocationBasedViewTracker.SelectionLogic
        public final void selectView(View view) {
            view.performAccessibilityAction(64, null);
        }
    };

    /* loaded from: classes.dex */
    public interface SelectionLogic {
        void selectView(View view);
    }

    @Override // android.view.View.OnLayoutChangeListener
    public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.mParent.removeOnLayoutChangeListener(this);
        this.mParent.post(this);
    }

    @Override // java.lang.Runnable
    public final void run() {
        ArrayList<View> arrayList = new ArrayList<>();
        this.mParent.addFocusables(arrayList, 2, 0);
        Rect rect = new Rect();
        Iterator<View> it = arrayList.iterator();
        int i = Integer.MAX_VALUE;
        View view = null;
        while (it.hasNext()) {
            View next = it.next();
            next.getDrawingRect(rect);
            this.mParent.offsetDescendantRectToMyCoords(next, rect);
            if (this.mFocusRect.intersect(rect)) {
                int abs = Math.abs(this.mFocusRect.bottom - rect.bottom) + Math.abs(this.mFocusRect.top - rect.top) + Math.abs(this.mFocusRect.right - rect.right) + Math.abs(this.mFocusRect.left - rect.left);
                if (i > abs) {
                    view = next;
                    i = abs;
                }
            }
        }
        if (view != null) {
            this.mSelectionLogic.selectView(view);
        }
    }

    public LocationBasedViewTracker(ViewGroup viewGroup, View view, SelectionLogic selectionLogic) {
        Rect rect = new Rect();
        this.mFocusRect = rect;
        this.mParent = viewGroup;
        this.mSelectionLogic = selectionLogic;
        view.getDrawingRect(rect);
        viewGroup.offsetDescendantRectToMyCoords(view, rect);
        viewGroup.addOnLayoutChangeListener(this);
        viewGroup.requestLayout();
    }
}
