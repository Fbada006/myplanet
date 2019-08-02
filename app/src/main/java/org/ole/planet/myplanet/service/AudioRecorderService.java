package org.ole.planet.myplanet.service;

import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;

import org.ole.planet.myplanet.utilities.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AudioRecorderService {

    private String outputFile;
    private MediaRecorder myAudioRecorder;
    private AudioRecordListener audioRecordListener;

    public interface AudioRecordListener {
        void onRecordStarted();

        void onRecordStopped(String outputFile);

        void onError(String error);
    }

    public AudioRecorderService() {
    }


    public AudioRecorderService setAudioRecordListener(AudioRecordListener audioRecordListener) {
        this.audioRecordListener = audioRecordListener;
        return this;
    }

    public void startRecording() {

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() +"/ole/audios/"+ UUID.randomUUID().toString()+".3gp";
        File f = new File(outputFile);
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
            if (audioRecordListener != null)
                audioRecordListener.onRecordStarted();
        } catch (Exception e) {
            myAudioRecorder = null;
            if (audioRecordListener != null) {
                audioRecordListener.onError(e.getMessage());
            }

        }
    }

    public boolean isRecording() {
        return myAudioRecorder != null;
    }

    public void stopRecording() {
        if (myAudioRecorder != null) {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;
            if (audioRecordListener != null)
                audioRecordListener.onRecordStopped(outputFile);
        }
    }
}