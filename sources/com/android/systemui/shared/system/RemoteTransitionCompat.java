package com.android.systemui.shared.system;

import android.annotation.NonNull;
import android.os.Parcel;
import android.os.Parcelable;
import android.window.RemoteTransition;
import android.window.TransitionFilter;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AnnotationValidations;
/* loaded from: classes.dex */
public class RemoteTransitionCompat implements Parcelable {
    public static final Parcelable.Creator<RemoteTransitionCompat> CREATOR = new Parcelable.Creator<RemoteTransitionCompat>() { // from class: com.android.systemui.shared.system.RemoteTransitionCompat.3
        @Override // android.os.Parcelable.Creator
        public final RemoteTransitionCompat createFromParcel(Parcel parcel) {
            return new RemoteTransitionCompat(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final RemoteTransitionCompat[] newArray(int i) {
            return new RemoteTransitionCompat[i];
        }
    };
    public TransitionFilter mFilter;
    public final RemoteTransition mTransition;

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class RecentsControllerWrap extends RecentsAnimationControllerCompat {
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        byte b;
        if (this.mFilter != null) {
            b = (byte) 2;
        } else {
            b = 0;
        }
        parcel.writeByte(b);
        parcel.writeTypedObject(this.mTransition, i);
        TransitionFilter transitionFilter = this.mFilter;
        if (transitionFilter != null) {
            parcel.writeTypedObject(transitionFilter, i);
        }
    }

    public RemoteTransitionCompat(Parcel parcel) {
        TransitionFilter transitionFilter;
        this.mFilter = null;
        byte readByte = parcel.readByte();
        RemoteTransition remoteTransition = (RemoteTransition) parcel.readTypedObject(RemoteTransition.CREATOR);
        if ((readByte & 2) == 0) {
            transitionFilter = null;
        } else {
            transitionFilter = (TransitionFilter) parcel.readTypedObject(TransitionFilter.CREATOR);
        }
        this.mTransition = remoteTransition;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, remoteTransition);
        this.mFilter = transitionFilter;
    }
}
