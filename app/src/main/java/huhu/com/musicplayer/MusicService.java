package huhu.com.musicplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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
      /*  try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, Uri.parse(intent.getExtras().get("url").toString()));
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
            //不能直接调用start方法，不然会有一定几率出现卡顿现象
            //mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return super.onStartCommand(intent, flags, startId);
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
        }
    }

    /**
     * 监听状态更新的广播接收器
     */
    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constants.ACTION_PLAY_LIST:
                    Log.e("播放列表", intent.getStringExtra("uri") + "序号为：" + intent.getStringExtra("position"));
                    break;
                case Constants.ACTION_PLAY:
                    Toast.makeText(context, "Action_play", Toast.LENGTH_SHORT).show();

                    break;
                case Constants.ACTION_PREVIOUS:
                    Toast.makeText(context, "previous", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.ACTION_NEXT:
                    Toast.makeText(context, "next", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.ACTION_PAUSE:
                    Toast.makeText(context, "pause", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
