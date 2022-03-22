package android.support.v4.media.session;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.util.ArrayList;
/* loaded from: classes.dex */
public interface IMediaControllerCallback extends IInterface {
    void onExtrasChanged(Bundle bundle) throws RemoteException;

    void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) throws RemoteException;

    void onQueueChanged(ArrayList arrayList) throws RemoteException;

    void onQueueTitleChanged(CharSequence charSequence) throws RemoteException;

    void onSessionDestroyed() throws RemoteException;

    void onVolumeInfoChanged(ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IMediaControllerCallback {
        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "android.support.v4.media.session.IMediaControllerCallback");
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            PlaybackStateCompat playbackStateCompat;
            boolean z;
            if (i != 1598968902) {
                Bundle bundle = null;
                ParcelableVolumeInfo parcelableVolumeInfo = null;
                Bundle bundle2 = null;
                CharSequence charSequence = null;
                MediaMetadataCompat mediaMetadataCompat = null;
                switch (i) {
                    case 1:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        String readString = parcel.readString();
                        if (parcel.readInt() != 0) {
                            bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        MediaControllerCompat.Callback callback = ((MediaControllerCompat.Callback.StubCompat) this).mCallback.get();
                        if (callback != null) {
                            callback.postToHandler(1, readString, bundle);
                        }
                        return true;
                    case 2:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        onSessionDestroyed();
                        return true;
                    case 3:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        if (parcel.readInt() != 0) {
                            playbackStateCompat = PlaybackStateCompat.CREATOR.createFromParcel(parcel);
                        } else {
                            playbackStateCompat = null;
                        }
                        MediaControllerCompat.Callback callback2 = ((MediaControllerCompat.Callback.StubCompat) this).mCallback.get();
                        if (callback2 != null) {
                            callback2.postToHandler(2, playbackStateCompat, null);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        if (parcel.readInt() != 0) {
                            mediaMetadataCompat = MediaMetadataCompat.CREATOR.createFromParcel(parcel);
                        }
                        onMetadataChanged(mediaMetadataCompat);
                        return true;
                    case 5:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        onQueueChanged(parcel.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR));
                        return true;
                    case FalsingManager.VERSION /* 6 */:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        if (parcel.readInt() != 0) {
                            charSequence = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        }
                        onQueueTitleChanged(charSequence);
                        return true;
                    case 7:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        if (parcel.readInt() != 0) {
                            bundle2 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        onExtrasChanged(bundle2);
                        return true;
                    case 8:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        if (parcel.readInt() != 0) {
                            parcelableVolumeInfo = ParcelableVolumeInfo.CREATOR.createFromParcel(parcel);
                        }
                        onVolumeInfoChanged(parcelableVolumeInfo);
                        return true;
                    case 9:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        int readInt = parcel.readInt();
                        MediaControllerCompat.Callback callback3 = ((MediaControllerCompat.Callback.StubCompat) this).mCallback.get();
                        if (callback3 != null) {
                            callback3.postToHandler(9, Integer.valueOf(readInt), null);
                        }
                        return true;
                    case 10:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        parcel.readInt();
                        return true;
                    case QSTileImpl.H.STALE /* 11 */:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        if (parcel.readInt() != 0) {
                            z = true;
                        } else {
                            z = false;
                        }
                        MediaControllerCompat.Callback callback4 = ((MediaControllerCompat.Callback.StubCompat) this).mCallback.get();
                        if (callback4 != null) {
                            callback4.postToHandler(11, Boolean.valueOf(z), null);
                        }
                        return true;
                    case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        int readInt2 = parcel.readInt();
                        MediaControllerCompat.Callback callback5 = ((MediaControllerCompat.Callback.StubCompat) this).mCallback.get();
                        if (callback5 != null) {
                            callback5.postToHandler(12, Integer.valueOf(readInt2), null);
                        }
                        return true;
                    case QS.VERSION /* 13 */:
                        parcel.enforceInterface("android.support.v4.media.session.IMediaControllerCallback");
                        MediaControllerCompat.Callback callback6 = ((MediaControllerCompat.Callback.StubCompat) this).mCallback.get();
                        if (callback6 != null) {
                            callback6.postToHandler(13, null, null);
                        }
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString("android.support.v4.media.session.IMediaControllerCallback");
                return true;
            }
        }
    }
}
