package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import androidx.leanback.widget.SearchBar;
import java.util.Objects;
/* loaded from: classes.dex */
public class SearchEditText extends StreamingTextView {
    public OnKeyboardDismissListener mKeyboardDismissListener;

    /* loaded from: classes.dex */
    public interface OnKeyboardDismissListener {
    }

    public SearchEditText(Context context) {
        this(context, null);
    }

    public SearchEditText(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2132017941);
    }

    public SearchEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.widget.TextView, android.view.View
    public final boolean onKeyPreIme(int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 4 && this.mKeyboardDismissListener != null) {
            post(new Runnable() { // from class: androidx.leanback.widget.SearchEditText.1
                @Override // java.lang.Runnable
                public final void run() {
                    OnKeyboardDismissListener onKeyboardDismissListener = SearchEditText.this.mKeyboardDismissListener;
                    if (onKeyboardDismissListener != null) {
                        SearchBar.AnonymousClass4 r0 = (SearchBar.AnonymousClass4) onKeyboardDismissListener;
                        Objects.requireNonNull(r0);
                        Objects.requireNonNull(SearchBar.this);
                    }
                }
            });
        }
        return super.onKeyPreIme(i, keyEvent);
    }
}
