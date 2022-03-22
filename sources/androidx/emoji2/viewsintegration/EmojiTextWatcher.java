package androidx.emoji2.viewsintegration;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.emoji2.text.EmojiCompat;
import java.lang.ref.WeakReference;
import java.util.Objects;
/* loaded from: classes.dex */
public final class EmojiTextWatcher implements TextWatcher {
    public final EditText mEditText;
    public InitCallbackImpl mInitCallback;
    public final boolean mExpectInitializedEmojiCompat = false;
    public boolean mEnabled = true;

    /* loaded from: classes.dex */
    public static class InitCallbackImpl extends EmojiCompat.InitCallback {
        public final WeakReference mViewRef;

        @Override // androidx.emoji2.text.EmojiCompat.InitCallback
        public final void onInitialized() {
            EmojiTextWatcher.processTextOnEnablingEvent((EditText) this.mViewRef.get(), 1);
        }

        public InitCallbackImpl(EditText editText) {
            this.mViewRef = new WeakReference(editText);
        }
    }

    public static void processTextOnEnablingEvent(EditText editText, int i) {
        int i2;
        if (i == 1 && editText != null && editText.isAttachedToWindow()) {
            Editable editableText = editText.getEditableText();
            int selectionStart = Selection.getSelectionStart(editableText);
            int selectionEnd = Selection.getSelectionEnd(editableText);
            EmojiCompat emojiCompat = EmojiCompat.get();
            if (editableText == null) {
                i2 = 0;
            } else {
                Objects.requireNonNull(emojiCompat);
                i2 = editableText.length();
            }
            emojiCompat.process(editableText, 0, i2);
            if (selectionStart >= 0 && selectionEnd >= 0) {
                Selection.setSelection(editableText, selectionStart, selectionEnd);
            } else if (selectionStart >= 0) {
                Selection.setSelection(editableText, selectionStart);
            } else if (selectionEnd >= 0) {
                Selection.setSelection(editableText, selectionEnd);
            }
        }
    }

    @Override // android.text.TextWatcher
    public final void afterTextChanged(Editable editable) {
    }

    @Override // android.text.TextWatcher
    public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0019, code lost:
        if (r0 == false) goto L_0x001b;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0048  */
    @Override // android.text.TextWatcher
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onTextChanged(java.lang.CharSequence r4, int r5, int r6, int r7) {
        /*
            r3 = this;
            android.widget.EditText r0 = r3.mEditText
            boolean r0 = r0.isInEditMode()
            if (r0 != 0) goto L_0x0056
            boolean r0 = r3.mEnabled
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x001b
            boolean r0 = r3.mExpectInitializedEmojiCompat
            if (r0 != 0) goto L_0x001c
            androidx.emoji2.text.EmojiCompat r0 = androidx.emoji2.text.EmojiCompat.sInstance
            if (r0 == 0) goto L_0x0018
            r0 = r2
            goto L_0x0019
        L_0x0018:
            r0 = r1
        L_0x0019:
            if (r0 != 0) goto L_0x001c
        L_0x001b:
            r1 = r2
        L_0x001c:
            if (r1 == 0) goto L_0x001f
            goto L_0x0056
        L_0x001f:
            if (r6 > r7) goto L_0x0056
            boolean r6 = r4 instanceof android.text.Spannable
            if (r6 == 0) goto L_0x0056
            androidx.emoji2.text.EmojiCompat r6 = androidx.emoji2.text.EmojiCompat.get()
            int r6 = r6.getLoadState()
            if (r6 == 0) goto L_0x0040
            if (r6 == r2) goto L_0x0035
            r4 = 3
            if (r6 == r4) goto L_0x0040
            goto L_0x0056
        L_0x0035:
            android.text.Spannable r4 = (android.text.Spannable) r4
            androidx.emoji2.text.EmojiCompat r3 = androidx.emoji2.text.EmojiCompat.get()
            int r7 = r7 + r5
            r3.process(r4, r5, r7)
            goto L_0x0056
        L_0x0040:
            androidx.emoji2.text.EmojiCompat r4 = androidx.emoji2.text.EmojiCompat.get()
            androidx.emoji2.viewsintegration.EmojiTextWatcher$InitCallbackImpl r5 = r3.mInitCallback
            if (r5 != 0) goto L_0x0051
            androidx.emoji2.viewsintegration.EmojiTextWatcher$InitCallbackImpl r5 = new androidx.emoji2.viewsintegration.EmojiTextWatcher$InitCallbackImpl
            android.widget.EditText r6 = r3.mEditText
            r5.<init>(r6)
            r3.mInitCallback = r5
        L_0x0051:
            androidx.emoji2.viewsintegration.EmojiTextWatcher$InitCallbackImpl r3 = r3.mInitCallback
            r4.registerInitCallback(r3)
        L_0x0056:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.emoji2.viewsintegration.EmojiTextWatcher.onTextChanged(java.lang.CharSequence, int, int, int):void");
    }

    public EmojiTextWatcher(EditText editText) {
        this.mEditText = editText;
    }
}
