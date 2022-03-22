package androidx.emoji2.viewsintegration;

import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.widget.TextView;
import androidx.emoji2.text.EmojiCompat;
import java.lang.ref.WeakReference;
import java.util.Objects;
/* loaded from: classes.dex */
public final class EmojiInputFilter implements InputFilter {
    public InitCallbackImpl mInitCallback;
    public final TextView mTextView;

    /* loaded from: classes.dex */
    public static class InitCallbackImpl extends EmojiCompat.InitCallback {
        public final WeakReference mEmojiInputFilterReference;
        public final WeakReference mViewRef;

        @Override // androidx.emoji2.text.EmojiCompat.InitCallback
        public final void onInitialized() {
            boolean z;
            int i;
            InputFilter[] filters;
            TextView textView = (TextView) this.mViewRef.get();
            InputFilter inputFilter = (InputFilter) this.mEmojiInputFilterReference.get();
            if (!(inputFilter == null || textView == null || (filters = textView.getFilters()) == null)) {
                for (InputFilter inputFilter2 : filters) {
                    if (inputFilter2 == inputFilter) {
                        z = true;
                        break;
                    }
                }
            }
            z = false;
            if (z && textView.isAttachedToWindow()) {
                EmojiCompat emojiCompat = EmojiCompat.get();
                CharSequence text = textView.getText();
                if (text == null) {
                    i = 0;
                } else {
                    Objects.requireNonNull(emojiCompat);
                    i = text.length();
                }
                CharSequence process = emojiCompat.process(text, 0, i);
                int selectionStart = Selection.getSelectionStart(process);
                int selectionEnd = Selection.getSelectionEnd(process);
                textView.setText(process);
                if (process instanceof Spannable) {
                    Spannable spannable = (Spannable) process;
                    if (selectionStart >= 0 && selectionEnd >= 0) {
                        Selection.setSelection(spannable, selectionStart, selectionEnd);
                    } else if (selectionStart >= 0) {
                        Selection.setSelection(spannable, selectionStart);
                    } else if (selectionEnd >= 0) {
                        Selection.setSelection(spannable, selectionEnd);
                    }
                }
            }
        }

        public InitCallbackImpl(TextView textView, EmojiInputFilter emojiInputFilter) {
            this.mViewRef = new WeakReference(textView);
            this.mEmojiInputFilterReference = new WeakReference(emojiInputFilter);
        }
    }

    @Override // android.text.InputFilter
    public final CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
        if (this.mTextView.isInEditMode()) {
            return charSequence;
        }
        int loadState = EmojiCompat.get().getLoadState();
        if (loadState != 0) {
            boolean z = true;
            if (loadState == 1) {
                if (i4 == 0 && i3 == 0 && spanned.length() == 0 && charSequence == this.mTextView.getText()) {
                    z = false;
                }
                if (!z || charSequence == null) {
                    return charSequence;
                }
                if (!(i == 0 && i2 == charSequence.length())) {
                    charSequence = charSequence.subSequence(i, i2);
                }
                EmojiCompat emojiCompat = EmojiCompat.get();
                int length = charSequence.length();
                Objects.requireNonNull(emojiCompat);
                return emojiCompat.process(charSequence, 0, length);
            } else if (loadState != 3) {
                return charSequence;
            }
        }
        EmojiCompat emojiCompat2 = EmojiCompat.get();
        if (this.mInitCallback == null) {
            this.mInitCallback = new InitCallbackImpl(this.mTextView, this);
        }
        emojiCompat2.registerInitCallback(this.mInitCallback);
        return charSequence;
    }

    public EmojiInputFilter(TextView textView) {
        this.mTextView = textView;
    }
}
