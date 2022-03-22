package com.google.android.systemui.columbus.sensors;

import java.util.ArrayDeque;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class EventIMURT {
    public int mNumberFeature;
    public int mSizeFeatureWindow;
    public long mSizeWindowNs;
    public ArrayList<Float> mFeatureVector = new ArrayList<>();
    public TfClassifier mClassifier = null;
    public ArrayDeque mAccXs = new ArrayDeque();
    public ArrayDeque mAccYs = new ArrayDeque();
    public ArrayDeque mAccZs = new ArrayDeque();
    public ArrayDeque mGyroXs = new ArrayDeque();
    public ArrayDeque mGyroYs = new ArrayDeque();
    public ArrayDeque mGyroZs = new ArrayDeque();
    public boolean mGotAcc = false;
    public boolean mGotGyro = false;
    public long mSyncTime = 0;
    public Resample3C mResampleAcc = new Resample3C();
    public Resample3C mResampleGyro = new Resample3C();
    public Slope3C mSlopeAcc = new Slope3C();
    public Slope3C mSlopeGyro = new Slope3C();
    public Lowpass3C mLowpassAcc = new Lowpass3C();
    public Lowpass3C mLowpassGyro = new Lowpass3C();
    public Highpass3C mHighpassAcc = new Highpass3C();
    public Highpass3C mHighpassGyro = new Highpass3C();
}
