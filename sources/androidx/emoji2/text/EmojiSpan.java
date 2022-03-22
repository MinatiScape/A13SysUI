package androidx.emoji2.text;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;
import androidx.emoji2.text.flatbuffer.MetadataItem;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class EmojiSpan extends ReplacementSpan {
    public final EmojiMetadata mMetadata;
    public final Paint.FontMetricsInt mTmpFontMetrics = new Paint.FontMetricsInt();
    public float mRatio = 1.0f;

    @Override // android.text.style.ReplacementSpan
    public final int getSize(Paint paint, @SuppressLint({"UnknownNullness"}) CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
        short s;
        paint.getFontMetricsInt(this.mTmpFontMetrics);
        Paint.FontMetricsInt fontMetricsInt2 = this.mTmpFontMetrics;
        float abs = Math.abs(fontMetricsInt2.descent - fontMetricsInt2.ascent) * 1.0f;
        EmojiMetadata emojiMetadata = this.mMetadata;
        Objects.requireNonNull(emojiMetadata);
        MetadataItem metadataItem = emojiMetadata.getMetadataItem();
        int __offset = metadataItem.__offset(14);
        short s2 = 0;
        if (__offset != 0) {
            s = metadataItem.bb.getShort(__offset + metadataItem.bb_pos);
        } else {
            s = 0;
        }
        this.mRatio = abs / s;
        EmojiMetadata emojiMetadata2 = this.mMetadata;
        Objects.requireNonNull(emojiMetadata2);
        MetadataItem metadataItem2 = emojiMetadata2.getMetadataItem();
        int __offset2 = metadataItem2.__offset(14);
        if (__offset2 != 0) {
            metadataItem2.bb.getShort(__offset2 + metadataItem2.bb_pos);
        }
        EmojiMetadata emojiMetadata3 = this.mMetadata;
        Objects.requireNonNull(emojiMetadata3);
        MetadataItem metadataItem3 = emojiMetadata3.getMetadataItem();
        int __offset3 = metadataItem3.__offset(12);
        if (__offset3 != 0) {
            s2 = metadataItem3.bb.getShort(__offset3 + metadataItem3.bb_pos);
        }
        short s3 = (short) (s2 * this.mRatio);
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fontMetricsInt3 = this.mTmpFontMetrics;
            fontMetricsInt.ascent = fontMetricsInt3.ascent;
            fontMetricsInt.descent = fontMetricsInt3.descent;
            fontMetricsInt.top = fontMetricsInt3.top;
            fontMetricsInt.bottom = fontMetricsInt3.bottom;
        }
        return s3;
    }

    public EmojiSpan(EmojiMetadata emojiMetadata) {
        Objects.requireNonNull(emojiMetadata, "metadata cannot be null");
        this.mMetadata = emojiMetadata;
    }
}
