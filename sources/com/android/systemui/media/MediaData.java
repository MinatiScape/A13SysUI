package com.android.systemui.media;

import android.app.PendingIntent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaData.kt */
/* loaded from: classes.dex */
public final class MediaData {
    public final List<MediaAction> actions;
    public final List<Integer> actionsToShowInCompact;
    public boolean active;
    public final String app;
    public final Icon appIcon;
    public final CharSequence artist;
    public final Icon artwork;
    public final int backgroundColor;
    public final PendingIntent clickIntent;
    public final MediaDeviceData device;
    public boolean hasCheckedForResume;
    public final boolean initialized;
    public final boolean isClearable;
    public final Boolean isPlaying;
    public long lastActive;
    public final String notificationKey;
    public final String packageName;
    public int playbackLocation;
    public Runnable resumeAction;
    public boolean resumption;
    public final MediaButton semanticActions;
    public final CharSequence song;
    public final MediaSession.Token token;
    public final int userId;

    public MediaData(int i, boolean z, int i2, String str, Icon icon, CharSequence charSequence, CharSequence charSequence2, Icon icon2, List<MediaAction> list, List<Integer> list2, MediaButton mediaButton, String str2, MediaSession.Token token, PendingIntent pendingIntent, MediaDeviceData mediaDeviceData, boolean z2, Runnable runnable, int i3, boolean z3, String str3, boolean z4, Boolean bool, boolean z5, long j) {
        this.userId = i;
        this.initialized = z;
        this.backgroundColor = i2;
        this.app = str;
        this.appIcon = icon;
        this.artist = charSequence;
        this.song = charSequence2;
        this.artwork = icon2;
        this.actions = list;
        this.actionsToShowInCompact = list2;
        this.semanticActions = mediaButton;
        this.packageName = str2;
        this.token = token;
        this.clickIntent = pendingIntent;
        this.device = mediaDeviceData;
        this.active = z2;
        this.resumeAction = runnable;
        this.playbackLocation = i3;
        this.resumption = z3;
        this.notificationKey = str3;
        this.hasCheckedForResume = z4;
        this.isPlaying = bool;
        this.isClearable = z5;
        this.lastActive = j;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaData)) {
            return false;
        }
        MediaData mediaData = (MediaData) obj;
        return this.userId == mediaData.userId && this.initialized == mediaData.initialized && this.backgroundColor == mediaData.backgroundColor && Intrinsics.areEqual(this.app, mediaData.app) && Intrinsics.areEqual(this.appIcon, mediaData.appIcon) && Intrinsics.areEqual(this.artist, mediaData.artist) && Intrinsics.areEqual(this.song, mediaData.song) && Intrinsics.areEqual(this.artwork, mediaData.artwork) && Intrinsics.areEqual(this.actions, mediaData.actions) && Intrinsics.areEqual(this.actionsToShowInCompact, mediaData.actionsToShowInCompact) && Intrinsics.areEqual(this.semanticActions, mediaData.semanticActions) && Intrinsics.areEqual(this.packageName, mediaData.packageName) && Intrinsics.areEqual(this.token, mediaData.token) && Intrinsics.areEqual(this.clickIntent, mediaData.clickIntent) && Intrinsics.areEqual(this.device, mediaData.device) && this.active == mediaData.active && Intrinsics.areEqual(this.resumeAction, mediaData.resumeAction) && this.playbackLocation == mediaData.playbackLocation && this.resumption == mediaData.resumption && Intrinsics.areEqual(this.notificationKey, mediaData.notificationKey) && this.hasCheckedForResume == mediaData.hasCheckedForResume && Intrinsics.areEqual(this.isPlaying, mediaData.isPlaying) && this.isClearable == mediaData.isClearable && this.lastActive == mediaData.lastActive;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v37, types: [java.lang.Runnable] */
    public static MediaData copy$default(MediaData mediaData, List list, List list2, String str, MediaDeviceData mediaDeviceData, boolean z, MediaResumeListener$getResumeAction$1 mediaResumeListener$getResumeAction$1, boolean z2, boolean z3, Boolean bool, boolean z4, int i) {
        int i2;
        boolean z5;
        int i3;
        String str2;
        Icon icon;
        CharSequence charSequence;
        CharSequence charSequence2;
        Icon icon2;
        List<MediaAction> list3;
        List<Integer> list4;
        MediaButton mediaButton;
        String str3;
        MediaSession.Token token;
        PendingIntent pendingIntent;
        MediaDeviceData mediaDeviceData2;
        boolean z6;
        MediaResumeListener$getResumeAction$1 mediaResumeListener$getResumeAction$12;
        boolean z7;
        String str4;
        boolean z8;
        Boolean bool2;
        boolean z9;
        long j;
        int i4 = 0;
        if ((i & 1) != 0) {
            i2 = mediaData.userId;
        } else {
            i2 = 0;
        }
        if ((i & 2) != 0) {
            z5 = mediaData.initialized;
        } else {
            z5 = false;
        }
        if ((i & 4) != 0) {
            i3 = mediaData.backgroundColor;
        } else {
            i3 = 0;
        }
        if ((i & 8) != 0) {
            str2 = mediaData.app;
        } else {
            str2 = null;
        }
        if ((i & 16) != 0) {
            icon = mediaData.appIcon;
        } else {
            icon = null;
        }
        if ((i & 32) != 0) {
            charSequence = mediaData.artist;
        } else {
            charSequence = null;
        }
        if ((i & 64) != 0) {
            charSequence2 = mediaData.song;
        } else {
            charSequence2 = null;
        }
        if ((i & 128) != 0) {
            icon2 = mediaData.artwork;
        } else {
            icon2 = null;
        }
        if ((i & 256) != 0) {
            list3 = mediaData.actions;
        } else {
            list3 = list;
        }
        if ((i & 512) != 0) {
            list4 = mediaData.actionsToShowInCompact;
        } else {
            list4 = list2;
        }
        if ((i & 1024) != 0) {
            mediaButton = mediaData.semanticActions;
        } else {
            mediaButton = null;
        }
        if ((i & 2048) != 0) {
            str3 = mediaData.packageName;
        } else {
            str3 = str;
        }
        if ((i & 4096) != 0) {
            token = mediaData.token;
        } else {
            token = null;
        }
        if ((i & 8192) != 0) {
            pendingIntent = mediaData.clickIntent;
        } else {
            pendingIntent = null;
        }
        if ((i & 16384) != 0) {
            mediaDeviceData2 = mediaData.device;
        } else {
            mediaDeviceData2 = mediaDeviceData;
        }
        if ((32768 & i) != 0) {
            z6 = mediaData.active;
        } else {
            z6 = z;
        }
        if ((65536 & i) != 0) {
            mediaResumeListener$getResumeAction$12 = mediaData.resumeAction;
        } else {
            mediaResumeListener$getResumeAction$12 = mediaResumeListener$getResumeAction$1;
        }
        if ((131072 & i) != 0) {
            i4 = mediaData.playbackLocation;
        }
        if ((262144 & i) != 0) {
            z7 = mediaData.resumption;
        } else {
            z7 = z2;
        }
        if ((524288 & i) != 0) {
            str4 = mediaData.notificationKey;
        } else {
            str4 = null;
        }
        if ((1048576 & i) != 0) {
            z8 = mediaData.hasCheckedForResume;
        } else {
            z8 = z3;
        }
        if ((2097152 & i) != 0) {
            bool2 = mediaData.isPlaying;
        } else {
            bool2 = bool;
        }
        if ((4194304 & i) != 0) {
            z9 = mediaData.isClearable;
        } else {
            z9 = z4;
        }
        if ((i & 8388608) != 0) {
            j = mediaData.lastActive;
        } else {
            j = 0;
        }
        Objects.requireNonNull(mediaData);
        return new MediaData(i2, z5, i3, str2, icon, charSequence, charSequence2, icon2, list3, list4, mediaButton, str3, token, pendingIntent, mediaDeviceData2, z6, mediaResumeListener$getResumeAction$12, i4, z7, str4, z8, bool2, z9, j);
    }

    public final int hashCode() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int hashCode = Integer.hashCode(this.userId) * 31;
        boolean z = this.initialized;
        int i12 = 1;
        if (z) {
            z = true;
        }
        int i13 = z ? 1 : 0;
        int i14 = z ? 1 : 0;
        int m = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.backgroundColor, (hashCode + i13) * 31, 31);
        String str = this.app;
        int i15 = 0;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        int i16 = (m + i) * 31;
        Icon icon = this.appIcon;
        if (icon == null) {
            i2 = 0;
        } else {
            i2 = icon.hashCode();
        }
        int i17 = (i16 + i2) * 31;
        CharSequence charSequence = this.artist;
        if (charSequence == null) {
            i3 = 0;
        } else {
            i3 = charSequence.hashCode();
        }
        int i18 = (i17 + i3) * 31;
        CharSequence charSequence2 = this.song;
        if (charSequence2 == null) {
            i4 = 0;
        } else {
            i4 = charSequence2.hashCode();
        }
        int i19 = (i18 + i4) * 31;
        Icon icon2 = this.artwork;
        if (icon2 == null) {
            i5 = 0;
        } else {
            i5 = icon2.hashCode();
        }
        int hashCode2 = (this.actionsToShowInCompact.hashCode() + ((this.actions.hashCode() + ((i19 + i5) * 31)) * 31)) * 31;
        MediaButton mediaButton = this.semanticActions;
        if (mediaButton == null) {
            i6 = 0;
        } else {
            i6 = mediaButton.hashCode();
        }
        int hashCode3 = (this.packageName.hashCode() + ((hashCode2 + i6) * 31)) * 31;
        MediaSession.Token token = this.token;
        if (token == null) {
            i7 = 0;
        } else {
            i7 = token.hashCode();
        }
        int i20 = (hashCode3 + i7) * 31;
        PendingIntent pendingIntent = this.clickIntent;
        if (pendingIntent == null) {
            i8 = 0;
        } else {
            i8 = pendingIntent.hashCode();
        }
        int i21 = (i20 + i8) * 31;
        MediaDeviceData mediaDeviceData = this.device;
        if (mediaDeviceData == null) {
            i9 = 0;
        } else {
            i9 = mediaDeviceData.hashCode();
        }
        int i22 = (i21 + i9) * 31;
        boolean z2 = this.active;
        if (z2) {
            z2 = true;
        }
        int i23 = z2 ? 1 : 0;
        int i24 = z2 ? 1 : 0;
        int i25 = (i22 + i23) * 31;
        Runnable runnable = this.resumeAction;
        if (runnable == null) {
            i10 = 0;
        } else {
            i10 = runnable.hashCode();
        }
        int m2 = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.playbackLocation, (i25 + i10) * 31, 31);
        boolean z3 = this.resumption;
        if (z3) {
            z3 = true;
        }
        int i26 = z3 ? 1 : 0;
        int i27 = z3 ? 1 : 0;
        int i28 = (m2 + i26) * 31;
        String str2 = this.notificationKey;
        if (str2 == null) {
            i11 = 0;
        } else {
            i11 = str2.hashCode();
        }
        int i29 = (i28 + i11) * 31;
        boolean z4 = this.hasCheckedForResume;
        if (z4) {
            z4 = true;
        }
        int i30 = z4 ? 1 : 0;
        int i31 = z4 ? 1 : 0;
        int i32 = (i29 + i30) * 31;
        Boolean bool = this.isPlaying;
        if (bool != null) {
            i15 = bool.hashCode();
        }
        int i33 = (i32 + i15) * 31;
        boolean z5 = this.isClearable;
        if (!z5) {
            i12 = z5 ? 1 : 0;
        }
        return Long.hashCode(this.lastActive) + ((i33 + i12) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("MediaData(userId=");
        m.append(this.userId);
        m.append(", initialized=");
        m.append(this.initialized);
        m.append(", backgroundColor=");
        m.append(this.backgroundColor);
        m.append(", app=");
        m.append((Object) this.app);
        m.append(", appIcon=");
        m.append(this.appIcon);
        m.append(", artist=");
        m.append((Object) this.artist);
        m.append(", song=");
        m.append((Object) this.song);
        m.append(", artwork=");
        m.append(this.artwork);
        m.append(", actions=");
        m.append(this.actions);
        m.append(", actionsToShowInCompact=");
        m.append(this.actionsToShowInCompact);
        m.append(", semanticActions=");
        m.append(this.semanticActions);
        m.append(", packageName=");
        m.append(this.packageName);
        m.append(", token=");
        m.append(this.token);
        m.append(", clickIntent=");
        m.append(this.clickIntent);
        m.append(", device=");
        m.append(this.device);
        m.append(", active=");
        m.append(this.active);
        m.append(", resumeAction=");
        m.append(this.resumeAction);
        m.append(", playbackLocation=");
        m.append(this.playbackLocation);
        m.append(", resumption=");
        m.append(this.resumption);
        m.append(", notificationKey=");
        m.append((Object) this.notificationKey);
        m.append(", hasCheckedForResume=");
        m.append(this.hasCheckedForResume);
        m.append(", isPlaying=");
        m.append(this.isPlaying);
        m.append(", isClearable=");
        m.append(this.isClearable);
        m.append(", lastActive=");
        m.append(this.lastActive);
        m.append(')');
        return m.toString();
    }

    public /* synthetic */ MediaData(int i, boolean z, int i2, String str, Icon icon, CharSequence charSequence, CharSequence charSequence2, Icon icon2, List list, List list2, MediaButton mediaButton, String str2, MediaSession.Token token, PendingIntent pendingIntent, MediaDeviceData mediaDeviceData, boolean z2, Runnable runnable, int i3, boolean z3, String str3, boolean z4, Boolean bool, boolean z5, long j, int i4) {
        this(i, (i4 & 2) != 0 ? false : z, i2, str, icon, charSequence, charSequence2, icon2, list, list2, (i4 & 1024) != 0 ? null : mediaButton, str2, token, pendingIntent, mediaDeviceData, z2, runnable, (131072 & i4) != 0 ? 0 : i3, (262144 & i4) != 0 ? false : z3, (524288 & i4) != 0 ? null : str3, (1048576 & i4) != 0 ? false : z4, (2097152 & i4) != 0 ? null : bool, (4194304 & i4) != 0 ? true : z5, (i4 & 8388608) != 0 ? 0L : j);
    }
}
