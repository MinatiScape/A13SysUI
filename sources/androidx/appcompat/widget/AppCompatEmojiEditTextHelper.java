package androidx.appcompat.widget;

import android.content.res.TypedArray;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import androidx.appcompat.R$styleable;
import androidx.emoji2.text.EmojiCompat;
import androidx.emoji2.viewsintegration.EmojiEditTextHelper;
import androidx.emoji2.viewsintegration.EmojiInputConnection;
import androidx.emoji2.viewsintegration.EmojiKeyListener;
import androidx.emoji2.viewsintegration.EmojiTextWatcher;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AppCompatEmojiEditTextHelper {
    public final EmojiEditTextHelper mEmojiEditTextHelper;
    public final EditText mView;

    public final KeyListener getKeyListener(KeyListener keyListener) {
        EmojiEditTextHelper emojiEditTextHelper = this.mEmojiEditTextHelper;
        Objects.requireNonNull(emojiEditTextHelper);
        Objects.requireNonNull(emojiEditTextHelper.mHelper);
        if (keyListener instanceof EmojiKeyListener) {
            return keyListener;
        }
        if (keyListener == null) {
            return null;
        }
        return new EmojiKeyListener(keyListener);
    }

    public final void initKeyListener() {
        boolean isFocusable = this.mView.isFocusable();
        int inputType = this.mView.getInputType();
        EditText editText = this.mView;
        editText.setKeyListener(editText.getKeyListener());
        this.mView.setRawInputType(inputType);
        this.mView.setFocusable(isFocusable);
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
            EmojiEditTextHelper emojiEditTextHelper = this.mEmojiEditTextHelper;
            Objects.requireNonNull(emojiEditTextHelper);
            EmojiEditTextHelper.HelperInternal19 helperInternal19 = emojiEditTextHelper.mHelper;
            Objects.requireNonNull(helperInternal19);
            EmojiTextWatcher emojiTextWatcher = helperInternal19.mTextWatcher;
            Objects.requireNonNull(emojiTextWatcher);
            if (emojiTextWatcher.mEnabled != z) {
                if (emojiTextWatcher.mInitCallback != null) {
                    EmojiCompat emojiCompat = EmojiCompat.get();
                    EmojiTextWatcher.InitCallbackImpl initCallbackImpl = emojiTextWatcher.mInitCallback;
                    Objects.requireNonNull(emojiCompat);
                    Objects.requireNonNull(initCallbackImpl, "initCallback cannot be null");
                    emojiCompat.mInitLock.writeLock().lock();
                    try {
                        emojiCompat.mInitCallbacks.remove(initCallbackImpl);
                    } finally {
                        emojiCompat.mInitLock.writeLock().unlock();
                    }
                }
                emojiTextWatcher.mEnabled = z;
                if (z) {
                    EmojiTextWatcher.processTextOnEnablingEvent(emojiTextWatcher.mEditText, EmojiCompat.get().getLoadState());
                }
            }
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    public final InputConnection onCreateInputConnection(InputConnection inputConnection, EditorInfo editorInfo) {
        EmojiEditTextHelper emojiEditTextHelper = this.mEmojiEditTextHelper;
        if (inputConnection == null) {
            Objects.requireNonNull(emojiEditTextHelper);
            return null;
        }
        EmojiEditTextHelper.HelperInternal19 helperInternal19 = emojiEditTextHelper.mHelper;
        Objects.requireNonNull(helperInternal19);
        if (!(inputConnection instanceof EmojiInputConnection)) {
            inputConnection = new EmojiInputConnection(helperInternal19.mEditText, inputConnection, editorInfo);
        }
        return inputConnection;
    }

    public AppCompatEmojiEditTextHelper(EditText editText) {
        this.mView = editText;
        this.mEmojiEditTextHelper = new EmojiEditTextHelper(editText);
    }
}
