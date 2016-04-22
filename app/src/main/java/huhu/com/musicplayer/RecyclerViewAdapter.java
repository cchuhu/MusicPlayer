package huhu.com.musicplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Huhu on 4/21/16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter {
    //数据列表
    private ArrayList<MusicInfo> musiclist;
    //上下文对象
    private Context mContext;
    //点击事件
    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(ArrayList<MusicInfo> arrayList, Context context) {
        this.musiclist = arrayList;
        this.mContext = context;
    }

    /**
     * 点击事件的回调接口，
     */
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(mContext).inflate(R.layout.item, null);
        return new MyHolder(itemview);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //设置数据
        ((MyHolder) holder).tv_title.setText(musiclist.get(position).getTitle());
        ((MyHolder) holder).tv_author.setText(musiclist.get(position).getAuthor());
        /**
         * 如果设置了点击事件,则添加监听，调用回调接口的执行函数
         */
        if (onItemClickListener != null) {
            ((MyHolder) holder).rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        return musiclist.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_author;
        public View rootView;

        public MyHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_author = (TextView) itemView.findViewById(R.id.tv_author);
        }

    }
}
