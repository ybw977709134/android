package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.onemeter.R;
import com.onemeter.entity.VishowInfo;

import java.util.List;


/**
 * 微秀热榜适配器
 */
public class VishowRedlistAdapter extends BaseAdapter {
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    LayoutInflater inflater;
    /**
     * 图片资源
     **/
    int[] text_img = new int[]{R.mipmap.main_no1, R.mipmap.main_no2, R.mipmap.main_no3,};

    /**
     * 图片的url地址
     **/
    private String[] url = {
            "assets/processedimages/main_banner.png",
            "assets/processedimages/main_banner1.png",
            "assets/processedimages/main_banner2.png",
            "assets/processedimages/main_banner3.png",
            "assets/processedimages/main_banner4.png",
            "assets/processedimages/main_banner.png",
            "assets/processedimages/main_banner1.png",
            "assets/processedimages/main_banner2.png",
            "assets/processedimages/main_banner3.png",
            "assets/processedimages/main_banner4.png",
            "assets/processedimages/main_banner4.png",
    };
    public List<VishowInfo> mvishow;

    private BitmapUtils bitmapUtils;

    // 上下文对象
    private Context mContext;

    // 构造函数
    public VishowRedlistAdapter(Context mContext, List<VishowInfo> mvishow) {
        this.mContext = mContext;
        this.mvishow = mvishow;
        bitmapUtils = new BitmapUtils(mContext);
    }


    @Override
    public int getCount() {
        if (mvishow == null) {
            return 0;
        } else {
            return mvishow.size();
        }

    }

    @Override
    public Object getItem(int position) {
        if (mvishow == null) {
            return null;
        } else {
            return mvishow.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //
    // 每个convert view都会调用此方法，获得当前所需要的view样式
    @Override
    public int getItemViewType(int position) {
        int p = position;
        if (p == 0 || p == 1 || p == 2) {
            return TYPE_1;
        } else {
            return TYPE_2;
        }

    }

    //
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder2 holder2 = null;
        ViewHolder1 holder1 = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            inflater = LayoutInflater.from(mContext);
            // 按当前所需的样式，确定new的布局
            switch (type) {
                case TYPE_1:
                    holder1 = new ViewHolder1();
                    convertView = inflater.inflate(R.layout.vishow_redlist_listview_item, parent, false);
                    holder1.vishow_item_recording_text = (TextView) convertView.findViewById(R.id.vishow_item_recording_text);
                    holder1.vishow_item_recording_pageviews = (TextView) convertView.findViewById(R.id.vishow_item_recording_pageviews);
                    holder1.vishow_item_recording_ranking = (ImageView) convertView.findViewById(R.id.vishow_item_recording_ranking);
                    holder1.vishow_item_bg = (RelativeLayout) convertView.findViewById(R.id.vishow_item_bg);
                    convertView.setTag(holder1);

                    break;
                case TYPE_2:
                    holder2 = new ViewHolder2();
                    convertView = inflater.inflate(R.layout.vishow_redlist_listview_item, parent, false);
                    holder2.vishow_item_recording_text = (TextView) convertView.findViewById(R.id.vishow_item_recording_text);
                    holder2.vishow_item_recording_pageviews = (TextView) convertView.findViewById(R.id.vishow_item_recording_pageviews);
                    holder2.vishow_item_recording_ranking = (ImageView) convertView.findViewById(R.id.vishow_item_recording_ranking);
                    holder2.vishow_item_bg = (RelativeLayout) convertView.findViewById(R.id.vishow_item_bg);
                    convertView.setTag(holder2);

            }

        } else {
            switch (type) {
                case TYPE_1:
                    holder1 = (ViewHolder1) convertView.getTag();
                    break;
                case TYPE_2:
                    holder2 = (ViewHolder2) convertView.getTag();
                    break;
            }
        }

        // 设置资源
        switch (type) {
            case TYPE_1:
                if (this.mvishow != null) {
                    VishowInfo vfo = this.mvishow.get(position);
                    holder1.vishow_item_recording_pageviews.setText(vfo.getScanCount());
                    holder1.vishow_item_recording_ranking.setImageResource(text_img[position]);
                    bitmapUtils.display(holder1.vishow_item_bg, vfo.getHdTemplate());
                }
                break;
            case TYPE_2:
                if (this.mvishow != null) {
                    VishowInfo vfo = this.mvishow.get(position);
                    holder2.vishow_item_recording_pageviews.setText(vfo.getScanCount());
                        bitmapUtils.display(holder2.vishow_item_bg, vfo.getHdTemplate());
                }
                break;
        }

        return convertView;
    }


    public class ViewHolder1 {
        private ImageView vishow_item_recording_ranking;
        private TextView vishow_item_recording_text;
        private TextView vishow_item_recording_pageviews;
        private RelativeLayout vishow_item_bg;


    }

    public class ViewHolder2 {
        private ImageView vishow_item_recording_ranking;
        private TextView vishow_item_recording_text;
        private TextView vishow_item_recording_pageviews;
        private RelativeLayout vishow_item_bg;

    }

}



