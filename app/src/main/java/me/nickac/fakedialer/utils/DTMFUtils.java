package me.nickac.fakedialer.utils;

import android.media.ToneGenerator;

import java.util.HashMap;

public enum DTMFUtils {
    INSTANCE;

    private HashMap<Character, Integer> toneChars = new HashMap<>();

    private ToneGenerator toneGenerator;

    DTMFUtils() {
        initGenerator();
    }

    private void initGenerator() {
        if (toneGenerator != null) return;
        toneGenerator = new ToneGenerator(0, ToneGenerator.MAX_VOLUME);
        toneChars.put('0', ToneGenerator.TONE_DTMF_0);
        toneChars.put('1', ToneGenerator.TONE_DTMF_1);
        toneChars.put('2', ToneGenerator.TONE_DTMF_2);
        toneChars.put('3', ToneGenerator.TONE_DTMF_3);
        toneChars.put('4', ToneGenerator.TONE_DTMF_4);
        toneChars.put('5', ToneGenerator.TONE_DTMF_5);
        toneChars.put('6', ToneGenerator.TONE_DTMF_6);
        toneChars.put('7', ToneGenerator.TONE_DTMF_7);
        toneChars.put('8', ToneGenerator.TONE_DTMF_8);
        toneChars.put('9', ToneGenerator.TONE_DTMF_9);
        toneChars.put('*', ToneGenerator.TONE_DTMF_S);
        toneChars.put('#', ToneGenerator.TONE_DTMF_P);
    }

    public void playTone(int tone) {
        initGenerator();
        toneGenerator.startTone(tone);
    }
    public void playTone(char c) {
        initGenerator();
        if (toneChars.containsKey(c)) {
            //noinspection ConstantConditions
            int toneType = toneChars.get(c);
            toneGenerator.startTone(toneType);
        }

    }

    public void stopTone() {
        initGenerator();
        toneGenerator.stopTone();
    }

    public void closeGenerator() {
        if (toneGenerator == null) return;
        toneGenerator.release();
        toneGenerator = null;
    }

}
