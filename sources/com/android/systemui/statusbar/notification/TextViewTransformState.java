package com.android.systemui.statusbar.notification;

import android.text.Layout;
import android.text.TextUtils;
import android.util.Pools;
import android.view.View;
import android.widget.TextView;
import com.android.systemui.statusbar.notification.TransformState;
/* loaded from: classes.dex */
public final class TextViewTransformState extends TransformState {
    public static Pools.SimplePool<TextViewTransformState> sInstancePool = new Pools.SimplePool<>(40);
    public TextView mText;

    @Override // com.android.systemui.statusbar.notification.TransformState
    public final int getContentHeight() {
        return this.mText.getLineHeight();
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public final int getContentWidth() {
        Layout layout = this.mText.getLayout();
        if (layout != null) {
            return (int) layout.getLineWidth(0);
        }
        return super.getContentWidth();
    }

    public final int getEllipsisCount() {
        Layout layout = this.mText.getLayout();
        if (layout == null || layout.getLineCount() <= 0) {
            return 0;
        }
        return layout.getEllipsisCount(0);
    }

    /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    @Override // com.android.systemui.statusbar.notification.TransformState
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean sameAs(com.android.systemui.statusbar.notification.TransformState r10) {
        /*
            r9 = this;
            boolean r0 = r9.mSameAsAny
            r1 = 1
            if (r0 == 0) goto L_0x0006
            return r1
        L_0x0006:
            boolean r0 = r10 instanceof com.android.systemui.statusbar.notification.TextViewTransformState
            r2 = 0
            if (r0 == 0) goto L_0x0099
            com.android.systemui.statusbar.notification.TextViewTransformState r10 = (com.android.systemui.statusbar.notification.TextViewTransformState) r10
            android.widget.TextView r0 = r10.mText
            java.lang.CharSequence r0 = r0.getText()
            android.widget.TextView r3 = r9.mText
            java.lang.CharSequence r3 = r3.getText()
            boolean r0 = android.text.TextUtils.equals(r0, r3)
            if (r0 == 0) goto L_0x0099
            int r0 = r9.getEllipsisCount()
            int r3 = r10.getEllipsisCount()
            if (r0 != r3) goto L_0x0097
            android.widget.TextView r0 = r9.mText
            int r0 = r0.getLineCount()
            android.widget.TextView r3 = r10.mText
            int r3 = r3.getLineCount()
            if (r0 != r3) goto L_0x0097
            java.lang.Class<java.lang.Object> r0 = java.lang.Object.class
            android.widget.TextView r9 = r9.mText
            boolean r3 = r9 instanceof android.text.Spanned
            android.widget.TextView r4 = r10.mText
            boolean r4 = r4 instanceof android.text.Spanned
            if (r3 == r4) goto L_0x0044
            goto L_0x0091
        L_0x0044:
            if (r3 != 0) goto L_0x0047
            goto L_0x0093
        L_0x0047:
            android.text.Spanned r9 = (android.text.Spanned) r9
            int r3 = r9.length()
            java.lang.Object[] r3 = r9.getSpans(r2, r3, r0)
            android.widget.TextView r10 = r10.mText
            android.text.Spanned r10 = (android.text.Spanned) r10
            int r4 = r10.length()
            java.lang.Object[] r0 = r10.getSpans(r2, r4, r0)
            int r4 = r3.length
            int r5 = r0.length
            if (r4 == r5) goto L_0x0062
            goto L_0x0091
        L_0x0062:
            r4 = r2
        L_0x0063:
            int r5 = r3.length
            if (r4 >= r5) goto L_0x0093
            r5 = r3[r4]
            r6 = r0[r4]
            java.lang.Class r7 = r5.getClass()
            java.lang.Class r8 = r6.getClass()
            boolean r7 = r7.equals(r8)
            if (r7 != 0) goto L_0x0079
            goto L_0x0091
        L_0x0079:
            int r7 = r9.getSpanStart(r5)
            int r8 = r10.getSpanStart(r6)
            if (r7 != r8) goto L_0x0091
            int r5 = r9.getSpanEnd(r5)
            int r6 = r10.getSpanEnd(r6)
            if (r5 == r6) goto L_0x008e
            goto L_0x0091
        L_0x008e:
            int r4 = r4 + 1
            goto L_0x0063
        L_0x0091:
            r9 = r2
            goto L_0x0094
        L_0x0093:
            r9 = r1
        L_0x0094:
            if (r9 == 0) goto L_0x0097
            goto L_0x0098
        L_0x0097:
            r1 = r2
        L_0x0098:
            return r1
        L_0x0099:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.TextViewTransformState.sameAs(com.android.systemui.statusbar.notification.TransformState):boolean");
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public final boolean transformScale(TransformState transformState) {
        int lineCount;
        if (!(transformState instanceof TextViewTransformState)) {
            return false;
        }
        TextViewTransformState textViewTransformState = (TextViewTransformState) transformState;
        if (TextUtils.equals(this.mText.getText(), textViewTransformState.mText.getText()) && (lineCount = this.mText.getLineCount()) == 1 && lineCount == textViewTransformState.mText.getLineCount() && getEllipsisCount() == textViewTransformState.getEllipsisCount() && getContentHeight() != textViewTransformState.getContentHeight()) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public final void initFrom(View view, TransformState.TransformInfo transformInfo) {
        super.initFrom(view, transformInfo);
        this.mText = (TextView) view;
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public final void recycle() {
        super.recycle();
        sInstancePool.release(this);
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public final void reset() {
        super.reset();
        this.mText = null;
    }
}
