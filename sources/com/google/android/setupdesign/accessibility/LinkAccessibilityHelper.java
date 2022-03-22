package com.google.android.setupdesign.accessibility;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class LinkAccessibilityHelper extends AccessibilityDelegateCompat {
    public final AccessibilityDelegateCompat delegate;

    /* loaded from: classes.dex */
    public static class PreOLinkAccessibilityHelper extends ExploreByTouchHelper {
        public final Rect tempRect;
        public final TextView view;

        public final ClickableSpan getSpanForOffset(int i) {
            CharSequence text = this.view.getText();
            if (!(text instanceof Spanned)) {
                return null;
            }
            ClickableSpan[] clickableSpanArr = (ClickableSpan[]) ((Spanned) text).getSpans(i, i, ClickableSpan.class);
            if (clickableSpanArr.length == 1) {
                return clickableSpanArr[0];
            }
            return null;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final int getVirtualViewAt(float f, float f2) {
            CharSequence text = this.view.getText();
            if (!(text instanceof Spanned)) {
                return Integer.MIN_VALUE;
            }
            Spanned spanned = (Spanned) text;
            TextView textView = this.view;
            int i = -1;
            if (textView.getLayout() != null) {
                int lineForVertical = textView.getLayout().getLineForVertical((int) (Math.min((textView.getHeight() - textView.getTotalPaddingBottom()) - 1, Math.max(0.0f, f2 - textView.getTotalPaddingTop())) + textView.getScrollY()));
                float max = Math.max(0.0f, f - textView.getTotalPaddingLeft());
                i = textView.getLayout().getOffsetForHorizontal(lineForVertical, Math.min((textView.getWidth() - textView.getTotalPaddingRight()) - 1, max) + textView.getScrollX());
            }
            ClickableSpan[] clickableSpanArr = (ClickableSpan[]) spanned.getSpans(i, i, ClickableSpan.class);
            if (clickableSpanArr.length == 1) {
                return spanned.getSpanStart(clickableSpanArr[0]);
            }
            return Integer.MIN_VALUE;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final void getVisibleVirtualViews(ArrayList arrayList) {
            CharSequence text = this.view.getText();
            if (text instanceof Spanned) {
                Spanned spanned = (Spanned) text;
                for (ClickableSpan clickableSpan : (ClickableSpan[]) spanned.getSpans(0, spanned.length(), ClickableSpan.class)) {
                    arrayList.add(Integer.valueOf(spanned.getSpanStart(clickableSpan)));
                }
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
            if (i2 != 16) {
                return false;
            }
            ClickableSpan spanForOffset = getSpanForOffset(i);
            if (spanForOffset != null) {
                spanForOffset.onClick(this.view);
                return true;
            }
            KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("LinkSpan is null for offset: ", i, "LinkAccessibilityHelper");
            return false;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final void onPopulateEventForVirtualView(int i, AccessibilityEvent accessibilityEvent) {
            ClickableSpan spanForOffset = getSpanForOffset(i);
            if (spanForOffset != null) {
                CharSequence text = this.view.getText();
                if (text instanceof Spanned) {
                    Spanned spanned = (Spanned) text;
                    text = spanned.subSequence(spanned.getSpanStart(spanForOffset), spanned.getSpanEnd(spanForOffset));
                }
                accessibilityEvent.setContentDescription(text);
                return;
            }
            KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("LinkSpan is null for offset: ", i, "LinkAccessibilityHelper");
            accessibilityEvent.setContentDescription(this.view.getText());
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public final void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            Layout layout;
            ClickableSpan spanForOffset = getSpanForOffset(i);
            if (spanForOffset != null) {
                CharSequence text = this.view.getText();
                if (text instanceof Spanned) {
                    Spanned spanned = (Spanned) text;
                    text = spanned.subSequence(spanned.getSpanStart(spanForOffset), spanned.getSpanEnd(spanForOffset));
                }
                accessibilityNodeInfoCompat.setContentDescription(text);
            } else {
                KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("LinkSpan is null for offset: ", i, "LinkAccessibilityHelper");
                accessibilityNodeInfoCompat.setContentDescription(this.view.getText());
            }
            accessibilityNodeInfoCompat.mInfo.setFocusable(true);
            accessibilityNodeInfoCompat.mInfo.setClickable(true);
            Rect rect = this.tempRect;
            CharSequence text2 = this.view.getText();
            rect.setEmpty();
            if ((text2 instanceof Spanned) && (layout = this.view.getLayout()) != null) {
                Spanned spanned2 = (Spanned) text2;
                int spanStart = spanned2.getSpanStart(spanForOffset);
                int spanEnd = spanned2.getSpanEnd(spanForOffset);
                float primaryHorizontal = layout.getPrimaryHorizontal(spanStart);
                float primaryHorizontal2 = layout.getPrimaryHorizontal(spanEnd);
                int lineForOffset = layout.getLineForOffset(spanStart);
                int lineForOffset2 = layout.getLineForOffset(spanEnd);
                layout.getLineBounds(lineForOffset, rect);
                if (lineForOffset2 == lineForOffset) {
                    rect.left = (int) Math.min(primaryHorizontal, primaryHorizontal2);
                    rect.right = (int) Math.max(primaryHorizontal, primaryHorizontal2);
                } else if (layout.getParagraphDirection(lineForOffset) == -1) {
                    rect.right = (int) primaryHorizontal;
                } else {
                    rect.left = (int) primaryHorizontal;
                }
                rect.offset(this.view.getTotalPaddingLeft(), this.view.getTotalPaddingTop());
            }
            if (this.tempRect.isEmpty()) {
                KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("LinkSpan bounds is empty for: ", i, "LinkAccessibilityHelper");
                this.tempRect.set(0, 0, 1, 1);
            }
            accessibilityNodeInfoCompat.setBoundsInParent(this.tempRect);
            accessibilityNodeInfoCompat.addAction(16);
        }
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        return this.delegate.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        return this.delegate.getAccessibilityNodeProvider(view);
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        this.delegate.onInitializeAccessibilityEvent(view, accessibilityEvent);
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        this.delegate.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        this.delegate.onPopulateAccessibilityEvent(view, accessibilityEvent);
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
        return this.delegate.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        return this.delegate.performAccessibilityAction(view, i, bundle);
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final void sendAccessibilityEvent(View view, int i) {
        this.delegate.sendAccessibilityEvent(view, i);
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public final void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
        this.delegate.sendAccessibilityEventUnchecked(view, accessibilityEvent);
    }

    public LinkAccessibilityHelper(AccessibilityDelegateCompat accessibilityDelegateCompat) {
        this.delegate = accessibilityDelegateCompat;
    }
}
