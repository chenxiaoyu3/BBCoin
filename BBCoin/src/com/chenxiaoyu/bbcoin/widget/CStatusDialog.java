package com.chenxiaoyu.bbcoin.widget;

import com.chenxiaoyu.bbcoin.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public enum CStatusDialog {

	Shared;
	
	private Dialog mDialog;
	public void show(Context context){
		if (mDialog == null) {
			mDialog = createLoadingDialog(context, "");
		}
		mDialog.show();
	}
	public void hide(){
		mDialog.hide();
	}

	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.layout_loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.v_loadingDialog);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.iv_loadingDiaglog);
		TextView tipTextView = (TextView) v.findViewById(R.id.tv_loadingDialog);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.c_loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;

	}
}
