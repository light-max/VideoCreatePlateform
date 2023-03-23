package com.lifengqiang.video.jni.player;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;

import com.lifengqiang.video.jni.interfaces.NativeInterface;

import java.nio.ByteBuffer;

public class JniAudioTrack implements NativeInterface {
    private AudioTrack track = null;

    public void onAudioPrepare(int channel, int sampleRate, int pcmBit) {
        if (track != null) {
            track.stop();
            track.release();
        }
        int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channel, pcmBit);
        channel = channel == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO;
        track = new AudioTrack.Builder()
                .setAudioFormat(new AudioFormat.Builder()
                        .setChannelMask(channel)
                        .setSampleRate(sampleRate)
                        .setEncoding(pcmBit)
                        .build())
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                        .build()
                )
                .setTransferMode(AudioTrack.MODE_STREAM)
                .setBufferSizeInBytes(minBufferSize)
                .build();
        track.play();
    }

    public void writeData(ByteBuffer buffer, int size) {
        if (track != null) {
            track.write(buffer, size, AudioTrack.WRITE_NON_BLOCKING);
        }
    }
}
