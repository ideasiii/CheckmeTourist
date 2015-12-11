package com.openfile.checkmetourist;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class DialogHandler
{
	public static void showNetworkError(Context context, final boolean bTerminate)
	{
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.setContentView(R.layout.dialog);

		TextView tvTitle = (TextView) dialog.findViewById(R.id.textViewDlgTitle);
		TextView tvMessage = (TextView) dialog.findViewById(R.id.textViewDlgMessage);
		tvTitle.setText(R.string.error);
		tvTitle.setTextColor(0xFFFF0000);
		tvMessage.setText(R.string.network_error);
		Button btn = (Button) dialog.findViewById(R.id.dialog_button_ok);
		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				if (bTerminate)
					Global.theApplication.Terminate();
			}
		});
		dialog.show();
	}

	public static void showAlert(Context context, final String strMsg, final boolean bTerminate)
	{
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.setContentView(R.layout.dialog);

		TextView tvTitle = (TextView) dialog.findViewById(R.id.textViewDlgTitle);
		TextView tvMessage = (TextView) dialog.findViewById(R.id.textViewDlgMessage);
		tvTitle.setText(R.string.attention);
		tvMessage.setText(strMsg);
		Button btn = (Button) dialog.findViewById(R.id.dialog_button_ok);
		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				if (bTerminate)
					Global.theApplication.Terminate();
			}
		});
		dialog.show();
	}

	public static void showAlert(Context context, final String strMsg, final int nId, final Handler handler)
	{
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.setContentView(R.layout.dialog);
		TextView tvTitle = (TextView) dialog.findViewById(R.id.textViewDlgTitle);
		TextView tvMessage = (TextView) dialog.findViewById(R.id.textViewDlgMessage);
		tvTitle.setText(R.string.attention);
		tvMessage.setText(strMsg);
		Button btnOK = (Button) dialog.findViewById(R.id.dialog_button_ok);
		Button btnCancel = (Button) dialog.findViewById(R.id.dialog_button_cancel);
		btnCancel.setVisibility(View.VISIBLE);
		btnOK.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (MotionEvent.ACTION_DOWN == event.getAction())
				{
					Common.postMessage(handler, MSG.DIALOG_CLICKED, nId, 0, null);
				}
				return true;
			}
		});

		btnCancel.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (MotionEvent.ACTION_DOWN == event.getAction())
				{
					dialog.dismiss();
				}
				return true;
			}
		});
		dialog.show();
	}

	public static void showReward(Context context, final String strReward)
	{
		final Dialog dialog = new Dialog(context, R.style.DialogReward);
		dialog.setContentView(R.layout.dialog_reward);
		TextView tvReward = (TextView) dialog.findViewById(R.id.textViewDialogReward);
		tvReward.setText(strReward);
		RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.relativeLayoutDialogRewardMain);
		rlMain.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				dialog.dismiss();
				return true;
			}
		});
		dialog.show();
	}
}
