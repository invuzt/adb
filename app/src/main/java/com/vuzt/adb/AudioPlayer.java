package com.vuzt.adb;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import java.io.IOException;

class AudioPlayer extends Thread implements MediaPlayer.OnCompletionListener {
  private final Service service;
  private final MediaPlayer mediaPlayer;

  public AudioPlayer(Service service, Uri location) throws IOException {
    this.service = service;
    mediaPlayer = new MediaPlayer();
    mediaPlayer.setDataSource(service, location);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
          .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
          .setUsage(AudioAttributes.USAGE_MEDIA).build());
    }
    mediaPlayer.setOnCompletionListener(this);
  }

  public void run() {
    try { mediaPlayer.prepare(); mediaPlayer.start(); } catch (IOException e) { e.printStackTrace(); }
  }

  public boolean isPlaying() { return mediaPlayer.isPlaying(); }
  public boolean isLooping() { return mediaPlayer.isLooping(); }

  public void setState(boolean playing, boolean looping) {
    if (playing) mediaPlayer.start(); else mediaPlayer.pause();
    mediaPlayer.setLooping(looping);
  }

  @Override
  public void onCompletion(MediaPlayer mp) { service.onMediaPlayerComplete(); }
  public void interrupt() { mediaPlayer.release(); super.interrupt(); }
}
