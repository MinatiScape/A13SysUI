package androidx.appcompat.widget;

import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.appcompat.R$styleable;
import androidx.emoji2.viewsintegration.EmojiTextViewHelper;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AppCompatEmojiTextHelper {
    public final EmojiTextViewHelper mEmojiTextViewHelper;
    public final TextView mView;

    public final InputFilter[] getFilters(InputFilter[] inputFilterArr) {
        EmojiTextViewHelper emojiTextViewHelper = this.mEmojiTextViewHelper;
        Objects.requireNonNull(emojiTextViewHelper);
        return emojiTextViewHelper.mHelper.getFilters(inputFilterArr);
    }

    /* JADX WARN: Finally extract failed */
    public final void loadFromAttributes(AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = this.mView.getContext().obtainStyledAttributes(attributeSet, R$styleable.AppCompatTextView, i, 0);
        try {
            boolean z = true;
            if (obtainStyledAttributes.hasValue(14)) {
                z = obtainStyledAttributes.getBoolean(14, true);
            }
            obtainStyledAttributes.recycle();
            EmojiTextViewHelper emojiTextViewHelper = this.mEmojiTextViewHelper;
            Objects.requireNonNull(emojiTextViewHelper);
            emojiTextViewHelper.mHelper.setEnabled(z);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    public final void setAllCaps(boolean z) {
        EmojiTextViewHelper emojiTextViewHelper = this.mEmojiTextViewHelper;
        Objects.requireNonNull(emojiTextViewHelper);
        emojiTextViewHelper.mHelper.setAllCaps(z);
    }

    public AppCompatEmojiTextHelper(TextView textView) {
        this.mView = textView;
        this.mEmojiTextViewHelper = new EmojiTextViewHelper(textView);
    }
}
