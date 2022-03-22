package android.support.v4.media;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaDescription;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
@SuppressLint({"BanParcelableUsage"})
/* loaded from: classes.dex */
public final class MediaDescriptionCompat implements Parcelable {
    public static final Parcelable.Creator<MediaDescriptionCompat> CREATOR = new Parcelable.Creator<MediaDescriptionCompat>() { // from class: android.support.v4.media.MediaDescriptionCompat.1
        @Override // android.os.Parcelable.Creator
        public final MediaDescriptionCompat createFromParcel(Parcel parcel) {
            return MediaDescriptionCompat.fromMediaDescription(MediaDescription.CREATOR.createFromParcel(parcel));
        }

        @Override // android.os.Parcelable.Creator
        public final MediaDescriptionCompat[] newArray(int i) {
            return new MediaDescriptionCompat[i];
        }
    };
    public final CharSequence mDescription;
    public MediaDescription mDescriptionFwk;
    public final Bundle mExtras;
    public final Bitmap mIcon;
    public final Uri mIconUri;
    public final String mMediaId;
    public final Uri mMediaUri;
    public final CharSequence mSubtitle;
    public final CharSequence mTitle;

    /* JADX WARN: Removed duplicated region for block: B:20:0x0050  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static android.support.v4.media.MediaDescriptionCompat fromMediaDescription(java.lang.Object r13) {
        /*
            r0 = 0
            if (r13 == 0) goto L_0x005d
            android.media.MediaDescription r13 = (android.media.MediaDescription) r13
            java.lang.String r2 = r13.getMediaId()
            java.lang.CharSequence r3 = r13.getTitle()
            java.lang.CharSequence r4 = r13.getSubtitle()
            java.lang.CharSequence r5 = r13.getDescription()
            android.graphics.Bitmap r6 = r13.getIconBitmap()
            android.net.Uri r7 = r13.getIconUri()
            android.os.Bundle r1 = r13.getExtras()
            if (r1 == 0) goto L_0x0027
            android.os.Bundle r1 = android.support.v4.media.session.MediaSessionCompat.unparcelWithClassLoader(r1)
        L_0x0027:
            java.lang.String r8 = "android.support.v4.media.description.MEDIA_URI"
            if (r1 == 0) goto L_0x0032
            android.os.Parcelable r9 = r1.getParcelable(r8)
            android.net.Uri r9 = (android.net.Uri) r9
            goto L_0x0033
        L_0x0032:
            r9 = r0
        L_0x0033:
            if (r9 == 0) goto L_0x004c
            java.lang.String r10 = "android.support.v4.media.description.NULL_BUNDLE_FLAG"
            boolean r11 = r1.containsKey(r10)
            if (r11 == 0) goto L_0x0046
            int r11 = r1.size()
            r12 = 2
            if (r11 != r12) goto L_0x0046
            r8 = r0
            goto L_0x004d
        L_0x0046:
            r1.remove(r8)
            r1.remove(r10)
        L_0x004c:
            r8 = r1
        L_0x004d:
            if (r9 == 0) goto L_0x0050
            goto L_0x0055
        L_0x0050:
            android.net.Uri r0 = r13.getMediaUri()
            r9 = r0
        L_0x0055:
            android.support.v4.media.MediaDescriptionCompat r0 = new android.support.v4.media.MediaDescriptionCompat
            r1 = r0
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9)
            r0.mDescriptionFwk = r13
        L_0x005d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.media.MediaDescriptionCompat.fromMediaDescription(java.lang.Object):android.support.v4.media.MediaDescriptionCompat");
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final String toString() {
        return ((Object) this.mTitle) + ", " + ((Object) this.mSubtitle) + ", " + ((Object) this.mDescription);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        MediaDescription mediaDescription = this.mDescriptionFwk;
        if (mediaDescription == null) {
            MediaDescription.Builder builder = new MediaDescription.Builder();
            builder.setMediaId(this.mMediaId);
            builder.setTitle(this.mTitle);
            builder.setSubtitle(this.mSubtitle);
            builder.setDescription(this.mDescription);
            builder.setIconBitmap(this.mIcon);
            builder.setIconUri(this.mIconUri);
            builder.setExtras(this.mExtras);
            builder.setMediaUri(this.mMediaUri);
            mediaDescription = builder.build();
            this.mDescriptionFwk = mediaDescription;
        }
        mediaDescription.writeToParcel(parcel, i);
    }

    public MediaDescriptionCompat(String str, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, Bitmap bitmap, Uri uri, Bundle bundle, Uri uri2) {
        this.mMediaId = str;
        this.mTitle = charSequence;
        this.mSubtitle = charSequence2;
        this.mDescription = charSequence3;
        this.mIcon = bitmap;
        this.mIconUri = uri;
        this.mExtras = bundle;
        this.mMediaUri = uri2;
    }
}
