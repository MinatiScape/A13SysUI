package androidx.leanback.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.leanback.widget.GridLayoutManager;
import androidx.leanback.widget.ItemAlignmentFacet;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ItemAlignmentFacetHelper {
    public static Rect sRect = new Rect();

    public static int getAlignmentPosition(View view, ItemAlignmentFacet.ItemAlignmentDef itemAlignmentDef, int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        Objects.requireNonNull(itemAlignmentDef);
        View findViewById = view.findViewById(-1);
        if (findViewById == null) {
            findViewById = view;
        }
        if (i != 0) {
            if (findViewById == view) {
                Objects.requireNonNull(layoutParams);
                i4 = (findViewById.getHeight() - layoutParams.mTopInset) - layoutParams.mBottomInset;
            } else {
                i4 = findViewById.getHeight();
            }
            int i8 = ((int) ((i4 * 50.0f) / 100.0f)) + 0;
            if (view == findViewById) {
                return i8;
            }
            Rect rect = sRect;
            rect.top = i8;
            ((ViewGroup) view).offsetDescendantRectToMyCoords(findViewById, rect);
            i3 = sRect.top;
            Objects.requireNonNull(layoutParams);
            i2 = layoutParams.mTopInset;
        } else if (view.getLayoutDirection() == 1) {
            if (findViewById == view) {
                Objects.requireNonNull(layoutParams);
                i6 = (findViewById.getWidth() - layoutParams.mLeftInset) - layoutParams.mRightInset;
            } else {
                i6 = findViewById.getWidth();
            }
            int i9 = i6 + 0;
            if (findViewById == view) {
                Objects.requireNonNull(layoutParams);
                i7 = (findViewById.getWidth() - layoutParams.mLeftInset) - layoutParams.mRightInset;
            } else {
                i7 = findViewById.getWidth();
            }
            int i10 = i9 - ((int) ((i7 * 50.0f) / 100.0f));
            if (view == findViewById) {
                return i10;
            }
            Rect rect2 = sRect;
            rect2.right = i10;
            ((ViewGroup) view).offsetDescendantRectToMyCoords(findViewById, rect2);
            int i11 = sRect.right;
            Objects.requireNonNull(layoutParams);
            return i11 + layoutParams.mRightInset;
        } else {
            if (findViewById == view) {
                Objects.requireNonNull(layoutParams);
                i5 = (findViewById.getWidth() - layoutParams.mLeftInset) - layoutParams.mRightInset;
            } else {
                i5 = findViewById.getWidth();
            }
            int i12 = ((int) ((i5 * 50.0f) / 100.0f)) + 0;
            if (view == findViewById) {
                return i12;
            }
            Rect rect3 = sRect;
            rect3.left = i12;
            ((ViewGroup) view).offsetDescendantRectToMyCoords(findViewById, rect3);
            i3 = sRect.left;
            Objects.requireNonNull(layoutParams);
            i2 = layoutParams.mLeftInset;
        }
        return i3 - i2;
    }
}
