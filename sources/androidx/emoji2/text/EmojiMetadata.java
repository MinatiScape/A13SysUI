package androidx.emoji2.text;

import androidx.emoji2.text.flatbuffer.MetadataItem;
import androidx.emoji2.text.flatbuffer.MetadataList;
import java.nio.ByteBuffer;
import java.util.Objects;
/* loaded from: classes.dex */
public final class EmojiMetadata {
    public static final ThreadLocal<MetadataItem> sMetadataItem = new ThreadLocal<>();
    public volatile int mHasGlyph = 0;
    public final int mIndex;
    public final MetadataRepo mMetadataRepo;

    public final MetadataItem getMetadataItem() {
        ThreadLocal<MetadataItem> threadLocal = sMetadataItem;
        MetadataItem metadataItem = threadLocal.get();
        if (metadataItem == null) {
            metadataItem = new MetadataItem();
            threadLocal.set(metadataItem);
        }
        MetadataRepo metadataRepo = this.mMetadataRepo;
        Objects.requireNonNull(metadataRepo);
        MetadataList metadataList = metadataRepo.mMetadataList;
        int i = this.mIndex;
        Objects.requireNonNull(metadataList);
        int __offset = metadataList.__offset(6);
        if (__offset != 0) {
            int i2 = __offset + metadataList.bb_pos;
            int i3 = (i * 4) + metadataList.bb.getInt(i2) + i2 + 4;
            metadataItem.__reset(metadataList.bb.getInt(i3) + i3, metadataList.bb);
        }
        return metadataItem;
    }

    public final String toString() {
        int i;
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(", id:");
        MetadataItem metadataItem = getMetadataItem();
        int __offset = metadataItem.__offset(4);
        if (__offset != 0) {
            i = metadataItem.bb.getInt(__offset + metadataItem.bb_pos);
        } else {
            i = 0;
        }
        sb.append(Integer.toHexString(i));
        sb.append(", codepoints:");
        int codepointsLength = getCodepointsLength();
        for (int i2 = 0; i2 < codepointsLength; i2++) {
            sb.append(Integer.toHexString(getCodepointAt(i2)));
            sb.append(" ");
        }
        return sb.toString();
    }

    public EmojiMetadata(MetadataRepo metadataRepo, int i) {
        this.mMetadataRepo = metadataRepo;
        this.mIndex = i;
    }

    public final int getCodepointAt(int i) {
        MetadataItem metadataItem = getMetadataItem();
        int __offset = metadataItem.__offset(16);
        if (__offset == 0) {
            return 0;
        }
        ByteBuffer byteBuffer = metadataItem.bb;
        int i2 = __offset + metadataItem.bb_pos;
        return byteBuffer.getInt((i * 4) + byteBuffer.getInt(i2) + i2 + 4);
    }

    public final int getCodepointsLength() {
        MetadataItem metadataItem = getMetadataItem();
        int __offset = metadataItem.__offset(16);
        if (__offset == 0) {
            return 0;
        }
        int i = __offset + metadataItem.bb_pos;
        return metadataItem.bb.getInt(metadataItem.bb.getInt(i) + i);
    }
}
