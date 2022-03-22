package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ActionMode;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.CheckedTextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.TextViewCompat;
import java.util.Objects;
import kotlin.io.CloseableKt;
/* loaded from: classes.dex */
public class AppCompatCheckedTextView extends CheckedTextView {
    public AppCompatEmojiTextHelper mAppCompatEmojiTextHelper;
    public final AppCompatBackgroundHelper mBackgroundTintHelper;
    public final AppCompatCheckedTextViewHelper mCheckedHelper;
    public final AppCompatTextHelper mTextHelper;

    @Override // android.widget.CheckedTextView
    public final void setCheckMarkDrawable(Drawable drawable) {
        super.setCheckMarkDrawable(drawable);
        AppCompatCheckedTextViewHelper appCompatCheckedTextViewHelper = this.mCheckedHelper;
        if (appCompatCheckedTextViewHelper != null) {
            Objects.requireNonNull(appCompatCheckedTextViewHelper);
            if (appCompatCheckedTextViewHelper.mSkipNextApply) {
                appCompatCheckedTextViewHelper.mSkipNextApply = false;
                return;
            }
            appCompatCheckedTextViewHelper.mSkipNextApply = true;
            appCompatCheckedTextViewHelper.applyCheckMarkTint();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0086 A[Catch: all -> 0x00b9, TryCatch #0 {all -> 0x00b9, blocks: (B:3:0x004a, B:5:0x0051, B:7:0x0057, B:10:0x0066, B:12:0x006c, B:14:0x0072, B:15:0x007f, B:17:0x0086, B:18:0x008f, B:20:0x0096), top: B:30:0x004a }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0096 A[Catch: all -> 0x00b9, TRY_LEAVE, TryCatch #0 {all -> 0x00b9, blocks: (B:3:0x004a, B:5:0x0051, B:7:0x0057, B:10:0x0066, B:12:0x006c, B:14:0x0072, B:15:0x007f, B:17:0x0086, B:18:0x008f, B:20:0x0096), top: B:30:0x004a }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00ac  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public AppCompatCheckedTextView(android.content.Context r10, android.util.AttributeSet r11) {
        /*
            r9 = this;
            androidx.appcompat.widget.TintContextWrapper.wrap(r10)
            r0 = 2130968764(0x7f0400bc, float:1.754619E38)
            r9.<init>(r10, r11, r0)
            android.content.Context r10 = r9.getContext()
            androidx.appcompat.widget.ThemeUtils.checkAppCompatTheme(r9, r10)
            androidx.appcompat.widget.AppCompatTextHelper r10 = new androidx.appcompat.widget.AppCompatTextHelper
            r10.<init>(r9)
            r9.mTextHelper = r10
            r10.loadFromAttributes(r11, r0)
            r10.applyCompoundDrawablesTints()
            androidx.appcompat.widget.AppCompatBackgroundHelper r10 = new androidx.appcompat.widget.AppCompatBackgroundHelper
            r10.<init>(r9)
            r9.mBackgroundTintHelper = r10
            r10.loadFromAttributes(r11, r0)
            androidx.appcompat.widget.AppCompatCheckedTextViewHelper r10 = new androidx.appcompat.widget.AppCompatCheckedTextViewHelper
            r10.<init>(r9)
            r9.mCheckedHelper = r10
            android.content.Context r1 = r9.getContext()
            int[] r4 = androidx.appcompat.R$styleable.CheckedTextView
            androidx.appcompat.widget.TintTypedArray r1 = androidx.appcompat.widget.TintTypedArray.obtainStyledAttributes(r1, r11, r4, r0)
            android.content.Context r3 = r9.getContext()
            android.content.res.TypedArray r6 = r1.mWrapped
            java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r2 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
            r7 = 2130968764(0x7f0400bc, float:1.754619E38)
            r8 = 0
            r2 = r9
            r5 = r11
            androidx.core.view.ViewCompat.Api29Impl.saveAttributeDataForStyleable(r2, r3, r4, r5, r6, r7, r8)
            r2 = 1
            boolean r3 = r1.hasValue(r2)     // Catch: all -> 0x00b9
            r4 = 0
            if (r3 == 0) goto L_0x0063
            int r3 = r1.getResourceId(r2, r4)     // Catch: all -> 0x00b9
            if (r3 == 0) goto L_0x0063
            android.content.Context r5 = r9.getContext()     // Catch: NotFoundException -> 0x0063, all -> 0x00b9
            android.graphics.drawable.Drawable r3 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r5, r3)     // Catch: NotFoundException -> 0x0063, all -> 0x00b9
            r9.setCheckMarkDrawable(r3)     // Catch: NotFoundException -> 0x0063, all -> 0x00b9
            goto L_0x0064
        L_0x0063:
            r2 = r4
        L_0x0064:
            if (r2 != 0) goto L_0x007f
            boolean r2 = r1.hasValue(r4)     // Catch: all -> 0x00b9
            if (r2 == 0) goto L_0x007f
            int r2 = r1.getResourceId(r4, r4)     // Catch: all -> 0x00b9
            if (r2 == 0) goto L_0x007f
            android.widget.CheckedTextView r3 = r10.mView     // Catch: all -> 0x00b9
            android.content.Context r4 = r3.getContext()     // Catch: all -> 0x00b9
            android.graphics.drawable.Drawable r2 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r4, r2)     // Catch: all -> 0x00b9
            r3.setCheckMarkDrawable(r2)     // Catch: all -> 0x00b9
        L_0x007f:
            r2 = 2
            boolean r3 = r1.hasValue(r2)     // Catch: all -> 0x00b9
            if (r3 == 0) goto L_0x008f
            android.widget.CheckedTextView r3 = r10.mView     // Catch: all -> 0x00b9
            android.content.res.ColorStateList r2 = r1.getColorStateList(r2)     // Catch: all -> 0x00b9
            r3.setCheckMarkTintList(r2)     // Catch: all -> 0x00b9
        L_0x008f:
            r2 = 3
            boolean r3 = r1.hasValue(r2)     // Catch: all -> 0x00b9
            if (r3 == 0) goto L_0x00a5
            android.widget.CheckedTextView r10 = r10.mView     // Catch: all -> 0x00b9
            r3 = -1
            int r2 = r1.getInt(r2, r3)     // Catch: all -> 0x00b9
            r3 = 0
            android.graphics.PorterDuff$Mode r2 = androidx.appcompat.widget.DrawableUtils.parseTintMode(r2, r3)     // Catch: all -> 0x00b9
            r10.setCheckMarkTintMode(r2)     // Catch: all -> 0x00b9
        L_0x00a5:
            r1.recycle()
            androidx.appcompat.widget.AppCompatEmojiTextHelper r10 = r9.mAppCompatEmojiTextHelper
            if (r10 != 0) goto L_0x00b3
            androidx.appcompat.widget.AppCompatEmojiTextHelper r10 = new androidx.appcompat.widget.AppCompatEmojiTextHelper
            r10.<init>(r9)
            r9.mAppCompatEmojiTextHelper = r10
        L_0x00b3:
            androidx.appcompat.widget.AppCompatEmojiTextHelper r9 = r9.mAppCompatEmojiTextHelper
            r9.loadFromAttributes(r11, r0)
            return
        L_0x00b9:
            r9 = move-exception
            r1.recycle()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatCheckedTextView.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    @Override // android.widget.CheckedTextView, android.widget.TextView, android.view.View
    public final void drawableStateChanged() {
        super.drawableStateChanged();
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.applyCompoundDrawablesTints();
        }
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.applySupportBackgroundTint();
        }
        AppCompatCheckedTextViewHelper appCompatCheckedTextViewHelper = this.mCheckedHelper;
        if (appCompatCheckedTextViewHelper != null) {
            appCompatCheckedTextViewHelper.applyCheckMarkTint();
        }
    }

    @Override // android.widget.TextView
    public final ActionMode.Callback getCustomSelectionActionModeCallback() {
        return TextViewCompat.unwrapCustomSelectionActionModeCallback(super.getCustomSelectionActionModeCallback());
    }

    @Override // android.widget.TextView, android.view.View
    public final InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
        CloseableKt.onCreateInputConnection(onCreateInputConnection, editorInfo, this);
        return onCreateInputConnection;
    }

    @Override // android.widget.TextView
    public final void setAllCaps(boolean z) {
        super.setAllCaps(z);
        if (this.mAppCompatEmojiTextHelper == null) {
            this.mAppCompatEmojiTextHelper = new AppCompatEmojiTextHelper(this);
        }
        this.mAppCompatEmojiTextHelper.setAllCaps(z);
    }

    @Override // android.view.View
    public final void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundDrawable();
        }
    }

    @Override // android.view.View
    public final void setBackgroundResource(int i) {
        super.setBackgroundResource(i);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundResource(i);
        }
    }

    @Override // android.widget.TextView
    public final void setTextAppearance(Context context, int i) {
        super.setTextAppearance(context, i);
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.onSetTextAppearance(context, i);
        }
    }

    @Override // android.widget.CheckedTextView
    public final void setCheckMarkDrawable(int i) {
        setCheckMarkDrawable(AppCompatResources.getDrawable(getContext(), i));
    }

    @Override // android.widget.TextView
    public final void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(callback);
    }
}
