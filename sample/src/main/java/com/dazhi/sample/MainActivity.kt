package com.dazhi.sample

import android.content.Intent
import android.widget.TextView
import com.dazhi.libroot.root.RootSimpActivity
import com.dazhi.pictureselecter.PictureSelecterAdapter
import com.dazhi.pictureselecter.UtLibPicture
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RootSimpActivity() {
    private val lsPicture: MutableList<LocalMedia> = arrayListOf()
    private lateinit var adapterPicture: PictureSelecterAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initConfig(tvToolTitle: TextView?) {
        tvToolTitle?.setText("图片选择器演示")
    }

    override fun initViewAndDataAndEvent() {
        adapterPicture = PictureSelecterAdapter(this, lsPicture, 6)
        mGridView.adapter = adapterPicture
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == PictureConfig.REQUEST_CAMERA || requestCode == PictureConfig.CHOOSE_REQUEST) && resultCode == RESULT_OK) {
            lsPicture.clear()
            lsPicture.addAll(PictureSelector.obtainMultipleResult(data))
            adapterPicture.notifyDataSetChanged()
            return
        }
    }

}
