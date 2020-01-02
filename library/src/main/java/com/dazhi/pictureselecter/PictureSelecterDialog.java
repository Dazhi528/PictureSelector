package com.dazhi.pictureselecter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 图片选择对话框
 */
@SuppressWarnings("ALL")
public class PictureSelecterDialog extends Dialog {

    private PictureSelecterDialog(Context context) {
        super(context);
    }

    private PictureSelecterDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    /**
     * 构建静态内部类持有对象
     */
    public static class Builder {
        //上下文对象
        private Context mContext;
        //点击事件
        private View.OnClickListener onClickListener;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        /**
         * 调用此方法构建对话框
         */
        public PictureSelecterDialog create() {
            final PictureSelecterDialog dialog = new PictureSelecterDialog(mContext, R.style.style_pictureselecter_dialog);
            final View layout = View.inflate(mContext, R.layout.dialog_picture_selecter, null);
            RelativeLayout rlPhoto = layout.findViewById(R.id.photo_layout);
            rlPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if(onClickListener!=null){
                        onClickListener.onClick(v);
                    }
                }
            });
            RelativeLayout rlChoose = layout.findViewById(R.id.choose_layout);
            rlChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if(onClickListener!=null){
                        onClickListener.onClick(v);
                    }
                }
            });
            dialog.setContentView(layout);
            return dialog;
        }
    }


}
