package com.google.android.systemui.dreamliner;

import com.android.systemui.volume.CaptionsToggleImageButton$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public abstract class WirelessCharger {

    /* loaded from: classes.dex */
    public interface AlignInfoListener {
    }

    /* loaded from: classes.dex */
    public interface ChallengeCallback {
    }

    /* loaded from: classes.dex */
    public interface GetFanInformationCallback {
    }

    /* loaded from: classes.dex */
    public interface GetFanSimpleInformationCallback {
    }

    /* loaded from: classes.dex */
    public interface GetFeaturesCallback {
    }

    /* loaded from: classes.dex */
    public interface GetInformationCallback {
    }

    /* loaded from: classes.dex */
    public interface GetWpcAuthCertificateCallback {
    }

    /* loaded from: classes.dex */
    public interface GetWpcAuthChallengeResponseCallback {
    }

    /* loaded from: classes.dex */
    public interface GetWpcAuthDigestsCallback {
    }

    /* loaded from: classes.dex */
    public interface IsDockPresentCallback {
    }

    /* loaded from: classes.dex */
    public interface KeyExchangeCallback {
    }

    /* loaded from: classes.dex */
    public interface SetFanCallback {
    }

    public abstract void asyncIsDockPresent(IsDockPresentCallback isDockPresentCallback);

    public abstract void challenge(byte b, byte[] bArr, ChallengeCallback challengeCallback);

    public abstract void getFanInformation(byte b, GetFanInformationCallback getFanInformationCallback);

    public abstract int getFanLevel();

    public abstract void getFanSimpleInformation(byte b, GetFanSimpleInformationCallback getFanSimpleInformationCallback);

    public abstract void getFeatures(long j, GetFeaturesCallback getFeaturesCallback);

    public abstract void getInformation(GetInformationCallback getInformationCallback);

    public abstract void getWpcAuthCertificate(byte b, short s, short s2, GetWpcAuthCertificateCallback getWpcAuthCertificateCallback);

    public abstract void getWpcAuthChallengeResponse(byte b, byte[] bArr, GetWpcAuthChallengeResponseCallback getWpcAuthChallengeResponseCallback);

    public abstract void getWpcAuthDigests(byte b, GetWpcAuthDigestsCallback getWpcAuthDigestsCallback);

    public abstract void keyExchange(byte[] bArr, KeyExchangeCallback keyExchangeCallback);

    public abstract void registerAlignInfo(AlignInfoListener alignInfoListener);

    public abstract void setFan(byte b, byte b2, int i, SetFanCallback setFanCallback);

    public abstract void setFeatures(long j, long j2, CaptionsToggleImageButton$$ExternalSyntheticLambda0 captionsToggleImageButton$$ExternalSyntheticLambda0);
}
