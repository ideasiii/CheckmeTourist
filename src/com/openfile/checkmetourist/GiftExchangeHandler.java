package com.openfile.checkmetourist;

import android.app.Activity;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.sensor.AppSensor;

public class GiftExchangeHandler extends BaseHandler
{

	private TextView		tvName				= null;
	private TextView		tvInfo				= null;
	private TextView		tvReward			= null;
	private TextView		tvExchangeSubmit	= null;
	private TextView		tvExchangeAfter		= null;
	private TextView		tvAlipayCancel		= null;
	private TextView		tvAlipaySend		= null;
	private EditText		etAccount			= null;
	private EditText		etInputAgain		= null;
	private ImageView		ivPic				= null;
	private RelativeLayout	rlBeforeSubmit		= null;
	private RelativeLayout	rlAfterSubmit		= null;
	private FlipperView		flipper				= null;
	private int				ID_VIEW_ALIPAY		= 0;

	public GiftExchangeHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
	}

	public void init(View viewGiftExchange)
	{
		if (null == viewGiftExchange)
			return;
		tvName = (TextView) viewGiftExchange.findViewById(R.id.textViewGiftExchangeTitle);
		tvInfo = (TextView) viewGiftExchange.findViewById(R.id.textViewGiftExchangeUseInfo);
		tvReward = (TextView) viewGiftExchange.findViewById(R.id.textViewGiftExchangePoint);
		ivPic = (ImageView) viewGiftExchange.findViewById(R.id.imageViewGiftExchangeProd);
		tvExchangeSubmit = (TextView) viewGiftExchange.findViewById(R.id.textViewGiftExchangeSubmit);
		rlBeforeSubmit = (RelativeLayout) viewGiftExchange.findViewById(R.id.relativeLayoutGiftExchangeEXBefore);
		rlAfterSubmit = (RelativeLayout) viewGiftExchange.findViewById(R.id.relativeLayoutGiftExchangeEXAfter);
		tvExchangeAfter = (TextView) viewGiftExchange.findViewById(R.id.textViewGiftExchangeTitleAfter);
		flipper = (FlipperView) viewGiftExchange.findViewById(R.id.flipperViewAlipay);
		if (null != tvExchangeSubmit)
		{
			tvExchangeSubmit.setOnTouchListener(itemTouchListener);
		}

		if (null != flipper)
		{
			ID_VIEW_ALIPAY = flipper.addChild(R.layout.alipay);
			tvAlipayCancel = (TextView) flipper.findViewById(R.id.textViewAlipayCancel);
			tvAlipaySend = (TextView) flipper.findViewById(R.id.textViewAlipaySend);
			etAccount = (EditText) flipper.findViewById(R.id.editTextAlipayAccount);
			etInputAgain = (EditText) flipper.findViewById(R.id.editTextAlipayInputAgain);
			if (null != tvAlipayCancel)
			{
				tvAlipayCancel.setOnTouchListener(itemTouchListener);
			}

			if (null != tvAlipaySend)
			{
				tvAlipaySend.setOnTouchListener(itemTouchListener);
			}
		}
	}

	public void setGiftExchangeData(final String strName, final String strInfo, final String strPic,
			final String strReward)
	{
		if (null != rlBeforeSubmit && null != rlAfterSubmit)
		{
			rlAfterSubmit.setVisibility(View.GONE);
			rlBeforeSubmit.setVisibility(View.VISIBLE);
		}

		if (null != tvName && null != strName)
		{
			tvName.setText(strName);
			if (null != tvExchangeAfter)
			{
				tvExchangeAfter.setText(strName);
			}
		}

		if (null != tvInfo && null != strInfo)
		{
			tvInfo.setText(strInfo);
		}

		if (null != tvReward && null != strReward)
		{
			tvReward.setText(strReward);
		}

		if (null != ivPic && null != strPic)
		{
			Global.theApplication.imageLoader.loadImage(strPic, ivPic);
		}
	}

	private void submitExchange()
	{
		showAlipayView();
		if (null != rlBeforeSubmit && null != rlAfterSubmit)
		{
			rlBeforeSubmit.setVisibility(View.GONE);
			rlAfterSubmit.setVisibility(View.VISIBLE);
		}
		Global.theApplication.submitLog("9", "CheckMe", "Gift Exchange Layout", tvName.getText().toString(), "");
	}

	private void showAlipayView()
	{
		if (null != etAccount)
		{
			etAccount.setText("");
		}
		if (null != etInputAgain)
		{
			etInputAgain.setText("");
		}
		flipper.showView(ID_VIEW_ALIPAY);
	}

	private OnTouchListener itemTouchListener = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			if (MotionEvent.ACTION_DOWN == event.getAction())
			{
				int nResId = v.getId();
				switch(nResId)
				{
				case R.id.textViewGiftExchangeSubmit:
					submitExchange();
					break;
				case R.id.textViewAlipayCancel:
					flipper.close();
					break;
				case R.id.textViewAlipaySend:
					flipper.close();
					break;
				}
			}
			return true;
		}
	};

}
