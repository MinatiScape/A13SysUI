package com.google.android.setupdesign.span;

import android.content.Context;
import android.content.ContextWrapper;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
/* loaded from: classes.dex */
public final class LinkSpan extends ClickableSpan {

    @Deprecated
    /* loaded from: classes.dex */
    public interface OnClickListener {
        void onClick();
    }

    /* loaded from: classes.dex */
    public interface OnLinkClickListener {
        void onLinkClick();
    }

    @Override // android.text.style.ClickableSpan
    public final void onClick(View view) {
        OnClickListener onClickListener;
        boolean z;
        if (view instanceof OnLinkClickListener) {
            ((OnLinkClickListener) view).onLinkClick();
        }
        Context context = view.getContext();
        while (true) {
            if (!(context instanceof OnClickListener)) {
                if (!(context instanceof ContextWrapper)) {
                    onClickListener = null;
                    break;
                }
                context = ((ContextWrapper) context).getBaseContext();
            } else {
                onClickListener = (OnClickListener) context;
                break;
            }
        }
        if (onClickListener != null) {
            onClickListener.onClick();
            z = true;
        } else {
            z = false;
        }
        if (z) {
            view.cancelPendingInputEvents();
        } else {
            Log.w("LinkSpan", "Dropping click event. No listener attached.");
        }
        if (view instanceof TextView) {
            CharSequence text = ((TextView) view).getText();
            if (text instanceof Spannable) {
                Selection.setSelection((Spannable) text, 0);
            }
        }
    }

    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
    public final void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setUnderlineText(false);
    }
}
