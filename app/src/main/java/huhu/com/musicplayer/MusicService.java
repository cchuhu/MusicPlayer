package huhu.com.musicplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

/**
 * 播放音乐的service
 */
public class MusicService extends Service {
    private MediaPlayer mediaPlayer;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        //onCreate方法只会执行一次
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PLAY);
        filter.addAction(Constants.ACTION_PLAY_LIST);
        filter.addAction(Constants.ACTION_NEXT);
        filter.addAction(Constants.ACTION_PREVIOUS);
        filter.addAction(Constants.ACTION_PAUSE);
        registerReceiver(new MyReceiver(), filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //每次startService都会执行一次
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 监听状态更新的广播接收器
     */
    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constants.ACTION_PLAY_LIST:
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(MusicService.this, Uri.parse(intent.getStringExtra("uri").toString()));
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                    break;
                case Constants.ACTION_PLAY:
                    mediaPlayer.start();
                    break;
                case Constants.ACTION_PAUSE:
                    mediaPlayer.pause();
                    break;
                case Constants.ACTION_PREVIOUS:
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(MusicService.this, Uri.parse(intent.getStringExtra("uri").toString()));
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.ACTION_NEXT:
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(MusicService.this, Uri.parse(intent.getStringExtra("uri").toString()));
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
        }
    }
}
