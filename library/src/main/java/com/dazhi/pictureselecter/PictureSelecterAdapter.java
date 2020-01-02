package com.dazhi.pictureselecter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import java.util.List;

/**
 * 功能：
 * 描述：
 * 作者：WangZezhi
 * 邮箱：wangzezhi528@163.com
 * 创建日期：2019-12-31 15:13
 * 修改日期：2019-12-31 15:13
 */
@SuppressWarnings("unused")
public class PictureSelecterAdapter extends BaseAdapter {
    private Activity activity;
    private List<LocalMedia> lsBn; //引入数据集合
    private int maxCount; //选择图片最大数


    public PictureSelecterAdapter(Activity activity, List<LocalMedia> lsBn, int maxCount) {
        if(activity==null) {
            throw new NullPointerException("activity can not be null");
        }
        if(lsBn==null) {
            throw new NullPointerException("lsBn can not be null");
        }
        if(maxCount<=0) {
            throw new UnsupportedOperationException("maxCount must be greater than 0");
        }
        this.activity = activity;
        this.lsBn = lsBn;
        this.maxCount = maxCount;
    }

    @Override
    public int getCount() {
        if (lsBn==null){
            return 1;
        }
        int curSize = lsBn.size();
        if (curSize < maxCount) {
            return curSize + 1;
        } else {
            return curSize;
        }
    }

    @Override
    public LocalMedia getItem(int position) {
        int size = (lsBn==null || lsBn.size()==0) ? 0 : lsBn.size();
        if(size==0 || position==size){
            return null;
        }else {
            return lsBn.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 如果是添加图片位置，则重用直接清空，使其去创建新的
        if(booPictureAdd(position)){
            convertView = null;
        }
        ViewHolder holder = ViewHolder.bind(position, convertView, parent,
                R.layout.item_picture_selecter);
        onBindView(holder, getItem(position));
        return holder.getConvertView();
    }

    // true显示添加图片； false显示选择图片
    private boolean booPictureAdd(int position) {
        int size = (lsBn==null || lsBn.size()==0) ? 0 : lsBn.size();
        return position == size;
    }

    private void onBindView(final ViewHolder viewHolder, LocalMedia localMedia) {
        viewHolder.setOnClickListener(R.id.ivPictureSelecterShow, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lsBn==null || lsBn.size()==0 || lsBn.size()==viewHolder.getItemPosition()){
                    UtLibPicture.showDialog(activity, lsBn, maxCount);
                    return;
                }
                PictureSelector.create(activity).externalPicturePreview(
                        viewHolder.getItemPosition(), lsBn, 0);
            }
        });
        //
        if (localMedia==null && booPictureAdd(viewHolder.getItemPosition())) {
            // 添加图片位置
            viewHolder.setVisibility(R.id.ivPictureSelecterDelete, View.INVISIBLE);
            viewHolder.setImageResource(R.id.ivPictureSelecterShow, R.drawable.ico_picture_add);
        } else {
            // 显示图片位置
            viewHolder.setVisibility(R.id.ivPictureSelecterDelete, View.VISIBLE);
            viewHolder.setOnClickListener(R.id.ivPictureSelecterDelete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = viewHolder.getItemPosition();
                    if (lsBn!=null && lsBn.size()>index) {
                        lsBn.remove(index);
                        notifyDataSetChanged();
                    }
                }
            });
            if (localMedia==null || TextUtils.isEmpty(localMedia.getPath())) {
                viewHolder.setImageResource(R.id.ivPictureSelecterShow, R.drawable.ico_picture_default);
                return;
            }
            String path;
            if (localMedia.isCut() && !localMedia.isCompressed()) {
                // 裁剪过
                path = localMedia.getCutPath();
            } else if (localMedia.isCompressed() || (localMedia.isCut() && localMedia.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = localMedia.getCompressPath();
            } else {
                // 原图
                path = localMedia.getPath();
            }
            Glide.with(viewHolder.getContext())
                    .load(path.startsWith("content://") && !localMedia.isCut() && !localMedia.isCompressed() ? Uri.parse(path)
                            : path)
                    .centerCrop()
                    .placeholder(R.drawable.ico_picture_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into((ImageView) viewHolder.getView(R.id.ivPictureSelecterShow));
        }
    }

    /*=======================================
     * 作者：WangZezhi  (2019-12-31  15:26)
     * 功能：视图持有器
     * 描述：
     *=======================================*/
    private static class ViewHolder {
        private SparseArray<View> saView; // 存储ListView中item中的View
        private Context context;
        private View convertView;  // 存放convertView
        private int position;      // 指针

        //构造方法，完成相关初始化
        private ViewHolder(ViewGroup parent, int layoutRes) {
            this.context = parent.getContext();
            saView = new SparseArray<>();
            View convertView = LayoutInflater.from(context).inflate(layoutRes, parent, false);
            this.convertView = convertView;
            convertView.setTag(this);
        }

        //绑定ViewHolder与item
        private static ViewHolder bind(int position, View convertView, ViewGroup parent,
                                      int layoutRes) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder(parent, layoutRes);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.position = position;
            return holder;
        }

        /**
         * 获取context
         */
        private Context getContext() {
            return context;
        }

        /**
         * 获取view
         */
        @SuppressWarnings("unchecked")
        private  <T extends View> T getView(int id) {
            T t = (T)saView.get(id);
            if (t == null) {
                t = convertView.findViewById(id);
                saView.put(id, t);
            }
            return t;
        }

        /**
         * 获取当前条目
         */
        private View getConvertView() {
            return convertView;
        }

        /**
         * 获取条目位置
         */
        private int getItemPosition() {
            return position;
        }

        /**
         * 设置文字
         */
        @SuppressWarnings("unused")
        private ViewHolder setText(int id, CharSequence text) {
            View view = getView(id);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
            return this;
        }

        /**
         * 设置图片
         */
        @SuppressWarnings("UnusedReturnValue")
        private ViewHolder setImageResource(int id, int drawableRes) {
            View view = getView(id);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(drawableRes);
            } else {
                view.setBackgroundResource(drawableRes);
            }
            return this;
        }

        /**
         * 设置可见
         */
        @SuppressWarnings("UnusedReturnValue")
        private ViewHolder setVisibility(int id, int visible) {
            getView(id).setVisibility(visible);
            return this;
        }

        /**
         * 设置点击监听
         */
        @SuppressWarnings("UnusedReturnValue")
        private ViewHolder setOnClickListener(int id, View.OnClickListener listener) {
            getView(id).setOnClickListener(listener);
            return this;
        }
    }

}
