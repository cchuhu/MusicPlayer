package huhu.com.musicplayer;

import java.io.Serializable;

/**
 * Created by Huhu on 4/21/16.
 * 音乐信息类
 */
public class MusicInfo implements Serializable {
    //歌曲作者
    private String author;
    //歌曲名称
    private String title;
    //歌曲时长
    private int duration;


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


}
