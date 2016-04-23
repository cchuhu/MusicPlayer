package huhu.com.musicplayer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    //获取音乐信息使用的ContentResolver
    private ContentResolver contentResolver;
    //存放查询出来的歌曲信息的List.
    private ArrayList<MusicInfo> musiclist;
    //列表实例
    private RecyclerView recyclerView;
    //列表适配器实例
    private RecyclerViewAdapter recyclerViewAdapter;
    //按钮实例
    private Button btn_left, btn_right, btn_switch;
    //当前播放状态
    private boolean isPlaying = false;
    //标题实例
    private TextView tv_title;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Intent i = new Intent();
                    i.setAction(Constants.ACTION_PLAY_LIST);
                    i.putExtra("uri", musiclist.get(msg.arg1).getUrl());
                    i.putExtra("position", msg.arg1 + "");
                    sendBroadcast(i);
                    break;
                case 1:
                    sendBroadcast(buildIntent(Constants.ACTION_PLAY));
                    isPlaying = true;
                    break;
                case 2:
                    sendBroadcast(buildIntent(Constants.ACTION_PREVIOUS));
                    break;
                case 3:
                    sendBroadcast(buildIntent(Constants.ACTION_NEXT));
                    break;
                case 4:
                    isPlaying = false;
                    sendBroadcast(buildIntent(Constants.ACTION_PAUSE));
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSource();
        initView();
        //启动服务
        Intent i = new Intent();
        i.setClass(MainActivity.this, MusicService.class);
        startService(i);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_switch = (Button) findViewById(R.id.btn_switch);
        tv_title = (TextView) findViewById(R.id.tv_name);
        //为按钮设置监听器
        //btn_left.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        btn_switch.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        //设置适配器
        recyclerViewAdapter = new RecyclerViewAdapter(musiclist, this);
        //列表设置监听器
        recyclerViewAdapter.setOnClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //获取到位置
                tv_title.setText(musiclist.get(position).getTitle());
                //第一次播放音乐的标志位为0
                Message message = new Message();
                message.what = 0;
                message.arg1 = position;
                mHandler.sendMessage(message);

            }
        });
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        recyclerView.setAdapter(recyclerViewAdapter);
    }


    /**
     * 构建Intent的方法
     *
     * @param action 要广播的action
     * @return intent
     */
    private Intent buildIntent(String action) {

        Intent i = new Intent();
        i.setAction(action);
        return i;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_left:
                Message message1 = new Message();
                message1.what = 2;
                mHandler.sendMessage(message1);
                break;
            case R.id.btn_right:
                Message message2 = new Message();
                message2.what = 3;
                mHandler.sendMessage(message2);
                break;
            case R.id.btn_switch:
                Message message3 = new Message();
                //如果正在播放，则切换到pause状态
                if (isPlaying) {
                    message3.what = 4;
                } else {
                    message3.what = 1;
                }
                mHandler.sendMessage(message3);
                break;

        }
    }


    /**
     * 初始化资源
     */
    private void initSource() {
        contentResolver = this.getContentResolver();
        musiclist = new ArrayList<>();
        //开启线程获取音乐信息
        MyThread myThread = new MyThread();
        myThread.start();

    }

    /**
     * 查询音乐信息的线程内部类
     */
    class MyThread extends Thread {
        @Override
        public void run() {
            getMusicInfo();
        }

        /**
         * 获取音乐信息,并规格化为数组。
         */
        private void getMusicInfo() {
            //定义要查询的列的名称
            String[] mediaColumns = new String[]{MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.TITLE};
            //使用游标查询信息

            Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            //循环提取信息
            while (cursor.moveToNext()) {
                MusicInfo info = new MusicInfo();
                info.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                info.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                info.setDuration(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                musiclist.add(info);
            }

        }
    }


}
