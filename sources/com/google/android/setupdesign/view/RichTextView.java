package com.google.android.setupdesign.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Annotation;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.google.android.setupdesign.accessibility.LinkAccessibilityHelper;
import com.google.android.setupdesign.span.LinkSpan;
import com.google.android.setupdesign.view.TouchableMovementMethod;
import java.util.Objects;
/* loaded from: classes.dex */
public class RichTextView extends AppCompatTextView implements LinkSpan.OnLinkClickListener {
    public static Typeface spanTypeface;
    public LinkAccessibilityHelper accessibilityHelper;

    public RichTextView(Context context) {
        super(context);
        if (!isInEditMode()) {
            LinkAccessibilityHelper linkAccessibilityHelper = new LinkAccessibilityHelper(new AccessibilityDelegateCompat());
            this.accessibilityHelper = linkAccessibilityHelper;
            ViewCompat.setAccessibilityDelegate(this, linkAccessibilityHelper);
        }
    }

    @Override // com.google.android.setupdesign.span.LinkSpan.OnLinkClickListener
    public final void onLinkClick() {
    }

    @Override // android.view.View
    public final boolean dispatchHoverEvent(MotionEvent motionEvent) {
        boolean z;
        LinkAccessibilityHelper linkAccessibilityHelper = this.accessibilityHelper;
        if (linkAccessibilityHelper != null) {
            Objects.requireNonNull(linkAccessibilityHelper);
            AccessibilityDelegateCompat accessibilityDelegateCompat = linkAccessibilityHelper.delegate;
            if (!(accessibilityDelegateCompat instanceof ExploreByTouchHelper) || !((ExploreByTouchHelper) accessibilityDelegateCompat).dispatchHoverEvent(motionEvent)) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                return true;
            }
        }
        return super.dispatchHoverEvent(motionEvent);
    }

    @Override // androidx.appcompat.widget.AppCompatTextView, android.widget.TextView, android.view.View
    public final void drawableStateChanged() {
        Drawable[] compoundDrawablesRelative;
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        for (Drawable drawable : getCompoundDrawablesRelative()) {
            if (drawable != null && drawable.setState(drawableState)) {
                invalidateDrawable(drawable);
            }
        }
    }

    @Override // android.widget.TextView, android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        MovementMethod movementMethod = getMovementMethod();
        if (movementMethod instanceof TouchableMovementMethod) {
            TouchableMovementMethod touchableMovementMethod = (TouchableMovementMethod) movementMethod;
            if (touchableMovementMethod.getLastTouchEvent() == motionEvent) {
                return touchableMovementMethod.isLastTouchEventHandled();
            }
        }
        return onTouchEvent;
    }

    @Override // android.widget.TextView
    public final void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        Annotation[] annotationArr;
        TypefaceSpan typefaceSpan;
        Context context = getContext();
        boolean z = true;
        if (charSequence instanceof Spanned) {
            SpannableString spannableString = new SpannableString(charSequence);
            for (Annotation annotation : (Annotation[]) spannableString.getSpans(0, spannableString.length(), Annotation.class)) {
                String key = annotation.getKey();
                if ("textAppearance".equals(key)) {
                    int identifier = context.getResources().getIdentifier(annotation.getValue(), "style", context.getPackageName());
                    if (identifier == 0) {
                        GridLayoutManager$$ExternalSyntheticOutline1.m("Cannot find resource: ", identifier, "RichTextView");
                    }
                    Object[] objArr = {new TextAppearanceSpan(context, identifier)};
                    int spanStart = spannableString.getSpanStart(annotation);
                    int spanEnd = spannableString.getSpanEnd(annotation);
                    spannableString.removeSpan(annotation);
                    for (int i = 0; i < 1; i++) {
                        spannableString.setSpan(objArr[i], spanStart, spanEnd, 0);
                    }
                } else if ("link".equals(key)) {
                    annotation.getValue();
                    LinkSpan linkSpan = new LinkSpan();
                    if (spanTypeface != null) {
                        typefaceSpan = new TypefaceSpan(spanTypeface);
                    } else {
                        typefaceSpan = new TypefaceSpan("sans-serif-medium");
                    }
                    Object[] objArr2 = {linkSpan, typefaceSpan};
                    int spanStart2 = spannableString.getSpanStart(annotation);
                    int spanEnd2 = spannableString.getSpanEnd(annotation);
                    spannableString.removeSpan(annotation);
                    for (int i2 = 0; i2 < 2; i2++) {
                        spannableString.setSpan(objArr2[i2], spanStart2, spanEnd2, 0);
                    }
                }
            }
            charSequence = spannableString;
        }
        super.setText(charSequence, bufferType);
        if (!(charSequence instanceof Spanned) || ((ClickableSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), ClickableSpan.class)).length <= 0) {
            z = false;
        }
        if (z) {
            setMovementMethod(new TouchableMovementMethod.TouchableLinkMovementMethod());
        } else {
            setMovementMethod(null);
        }
        setFocusable(z);
        setRevealOnFocusHint(false);
        setFocusableInTouchMode(z);
    }

    public RichTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            LinkAccessibilityHelper linkAccessibilityHelper = new LinkAccessibilityHelper(new AccessibilityDelegateCompat());
            this.accessibilityHelper = linkAccessibilityHelper;
            ViewCompat.setAccessibilityDelegate(this, linkAccessibilityHelper);
        }
    }
}
