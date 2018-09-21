package com.onemeter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.onemeter.R;
import com.onemeter.entity.hotschoolInfo;
import com.onemeter.view.CustomImageView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * 推荐订阅学校适配器
 */
public class TuiJianDingYueDetailAdapter extends BaseAdapter {
    public List<hotschoolInfo> mhsf;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private ImageLoadingListenerImpl mImageLoadingListenerImpl;

    public TuiJianDingYueDetailAdapter(Context mContext,List<hotschoolInfo> mhsf, ImageLoader imageLoader) {
        this.mContext = mContext;
        this.mhsf = mhsf;
        this.mImageLoader = imageLoader;
        int defaultImageId = R.mipmap.ic_launcher;
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(defaultImageId)
                .showImageForEmptyUri(defaultImageId)
                .showImageOnFail(defaultImageId)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .resetViewBeforeLoading()
                .build();
        mImageLoadingListenerImpl = new ImageLoadingListenerImpl();

    }


    @Override
    public int getCount() {
        if (mhsf == null) {
            return 0;
        } else {
            return mhsf.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mhsf == null) {
            return null;
        } else {
            return mhsf.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_dingyue_group_list_item, null);
            holder.img_my_dingyue_title = (CustomImageView) convertView.findViewById(R.id.img_my_dingyue_title);
            holder.text_my_dingyue_title = (TextView) convertView.findViewById(R.id.text_my_dingyue_title);
            holder.my_dingyue_status = (TextView) convertView.findViewById(R.id.my_dingyue_status);
            holder.text_my_number_jiulanliang = (TextView) convertView.findViewById(R.id.text_my_number_jiulanliang);
            holder.my_dingyue_number = (TextView) convertView.findViewById(R.id.my_dingyue_number);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置数据
        if (this.mhsf != null) {
            hotschoolInfo mhotdyItem = this.mhsf.get(position);
            //学校名称
            if (holder.text_my_dingyue_title != null) {
                holder.text_my_dingyue_title.setText(mhotdyItem.getSchoolName());
            }
            //浏览量
            if (holder.text_my_number_jiulanliang != null) {
                holder.text_my_number_jiulanliang.setText(mhotdyItem.getAllScanCount() + "");
            }
            //订阅数
            if (holder.my_dingyue_number != null) {
                holder.my_dingyue_number.setText(mhotdyItem.getSubCount() + "");
            }


            if (holder.img_my_dingyue_title != null) {
                try {
                    //加载图片
                    mImageLoader.displayImage(
                            mhotdyItem.getHeadImage(),
                            holder.img_my_dingyue_title,
                            mDisplayImageOptions,
                            mImageLoadingListenerImpl);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        return convertView;
    }


    //监听图片异步加载
    public static class ImageLoadingListenerImpl extends SimpleImageLoadingListener {

        public static final List<String> displayedImages =
                Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
            if (bitmap != null) {
                ImageView imageView = (ImageView) view;
                boolean isFirstDisplay = !displayedImages.contains(imageUri);
                if (isFirstDisplay) {
                    //图片的淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                    System.out.println("===> loading " + imageUri);
                }
            }
        }
    }


    public class ViewHolder {
        /**
         * 图标设置
         **/
        private CustomImageView img_my_dingyue_title;

        /**
         * 标题
         **/
        private TextView text_my_dingyue_title;
        /**
         * 状态
         **/
        private TextView my_dingyue_status;
        /**
         * 浏览量
         **/
        private TextView text_my_number_jiulanliang;
        /**
         * 订阅数
         **/
        private TextView my_dingyue_number;
    }
}
