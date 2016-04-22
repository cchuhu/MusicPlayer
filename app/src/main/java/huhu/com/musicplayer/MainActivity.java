package huhu.com.musicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    //获取音乐信息使用的ContentResolver
    ContentResolver contentResolver;
    //存放查询出来的歌曲信息的List.
    ArrayList<MusicInfo> musiclist;
    //列表实例
    RecyclerView recyclerView;
    //列表适配器实例
    RecyclerViewAdapter recyclerViewAdapter;
    //按钮实例
    Button btn_left, btn_right, btn_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSource();
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_switch = (Button) findViewById(R.id.btn_switch);
        //设置适配器
        recyclerViewAdapter = new RecyclerViewAdapter(musiclist, this);
        recyclerViewAdapter.setOnClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //获取到位置
                //dosomething
            }


        });
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        recyclerView.setAdapter(recyclerViewAdapter);

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
            String[] mediaColumns = new String[]{MediaStore.Video.Media.ARTIST, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.TITLE};
            //使用游标查询信息
            Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            //循环提取信息
            while (cursor.moveToNext()) {
                MusicInfo info = new MusicInfo();
                info.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                info.setDuration(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                musiclist.add(info);
            }

        }
    }


}
