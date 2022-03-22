package androidx.emoji2.viewsintegration;

import android.text.Editable;
import android.text.method.KeyListener;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyEvent;
import android.view.View;
import androidx.emoji2.text.EmojiCompat;
import androidx.emoji2.text.EmojiProcessor;
import java.util.Objects;
/* loaded from: classes.dex */
public final class EmojiKeyListener implements KeyListener {
    public final EmojiCompatHandleKeyDownHelper mEmojiCompatHandleKeyDownHelper;
    public final KeyListener mKeyListener;

    /* loaded from: classes.dex */
    public static class EmojiCompatHandleKeyDownHelper {
    }

    public EmojiKeyListener(KeyListener keyListener) {
        EmojiCompatHandleKeyDownHelper emojiCompatHandleKeyDownHelper = new EmojiCompatHandleKeyDownHelper();
        this.mKeyListener = keyListener;
        this.mEmojiCompatHandleKeyDownHelper = emojiCompatHandleKeyDownHelper;
    }

    @Override // android.text.method.KeyListener
    public final void clearMetaKeyState(View view, Editable editable, int i) {
        this.mKeyListener.clearMetaKeyState(view, editable, i);
    }

    @Override // android.text.method.KeyListener
    public final int getInputType() {
        return this.mKeyListener.getInputType();
    }

    @Override // android.text.method.KeyListener
    public final boolean onKeyDown(View view, Editable editable, int i, KeyEvent keyEvent) {
        boolean z;
        boolean z2;
        Objects.requireNonNull(this.mEmojiCompatHandleKeyDownHelper);
        Object obj = EmojiCompat.INSTANCE_LOCK;
        if (i == 67) {
            z = EmojiProcessor.delete(editable, keyEvent, false);
        } else if (i != 112) {
            z = false;
        } else {
            z = EmojiProcessor.delete(editable, keyEvent, true);
        }
        if (z) {
            MetaKeyKeyListener.adjustMetaAfterKeypress(editable);
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2 || this.mKeyListener.onKeyDown(view, editable, i, keyEvent)) {
            return true;
        }
        return false;
    }

    @Override // android.text.method.KeyListener
    public final boolean onKeyOther(View view, Editable editable, KeyEvent keyEvent) {
        return this.mKeyListener.onKeyOther(view, editable, keyEvent);
    }

    @Override // android.text.method.KeyListener
    public final boolean onKeyUp(View view, Editable editable, int i, KeyEvent keyEvent) {
        return this.mKeyListener.onKeyUp(view, editable, i, keyEvent);
    }
}
