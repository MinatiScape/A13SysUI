package androidx.emoji2.viewsintegration;

import android.widget.EditText;
import java.util.Objects;
/* loaded from: classes.dex */
public final class EmojiEditTextHelper {
    public final HelperInternal19 mHelper;

    /* loaded from: classes.dex */
    public static class HelperInternal {
    }

    /* loaded from: classes.dex */
    public static class HelperInternal19 extends HelperInternal {
        public final EditText mEditText;
        public final EmojiTextWatcher mTextWatcher;

        public HelperInternal19(EditText editText) {
            this.mEditText = editText;
            EmojiTextWatcher emojiTextWatcher = new EmojiTextWatcher(editText);
            this.mTextWatcher = emojiTextWatcher;
            editText.addTextChangedListener(emojiTextWatcher);
            if (EmojiEditableFactory.sInstance == null) {
                synchronized (EmojiEditableFactory.INSTANCE_LOCK) {
                    if (EmojiEditableFactory.sInstance == null) {
                        EmojiEditableFactory.sInstance = new EmojiEditableFactory();
                    }
                }
            }
            editText.setEditableFactory(EmojiEditableFactory.sInstance);
        }
    }

    public EmojiEditTextHelper(EditText editText) {
        Objects.requireNonNull(editText, "editText cannot be null");
        this.mHelper = new HelperInternal19(editText);
    }
}
