package com.openfile.checkmetourist;

import java.util.Locale;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class DataReflashHandler extends BaseHandler
{
	private final int	EMPTY_RUN	= 666;
	TextView			tvPercent	= null;
	private int			mnProgress	= 0;

	public DataReflashHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
		if (null != activity)
		{
			activity.setContentView(R.layout.data_reflash);
		}
	}

	public void init()
	{
		if (null == theActivity)
			return;
		tvPercent = (TextView) theActivity.findViewById(R.id.textViewPercent);
		updateField();
	}

	public void updateResult(final int nApiType, final int nResult)
	{
		switch (nApiType)
		{
			case CheckmeApi.API_FIELD:
				mnProgress = 20;
				setLoadPercent(mnProgress);
				updateBanner();
				break;
			case CheckmeApi.API_BANNER:
				mnProgress = 30;
				setLoadPercent(mnProgress);
				updateGift();
				break;
			case CheckmeApi.API_GIFT:
				mnProgress = 40;
				setLoadPercent(mnProgress);
				selfHandler.sendEmptyMessageDelayed(EMPTY_RUN, 500);
				break;
		}
	}

	public void setLoadPercent(int nPercent)
	{
		if (null != tvPercent)
		{
			String strPercent = String.format(Locale.getDefault(), "%d%%", nPercent);
			tvPercent.setText(strPercent);
		}
	}

	public void updateBanner()
	{
		Global.theApplication.checkmeApi.runApi(CheckmeApi.API_BANNER, theHandler);
	}

	public void updateGift()
	{
		Global.theApplication.checkmeApi.runApi(CheckmeApi.API_GIFT, theHandler);
	}

	public void updateField()
	{
		Global.theApplication.checkmeApi.runApi(CheckmeApi.API_FIELD, theHandler);
	}

	private Handler	selfHandler	= new Handler()
								{
									@Override
									public void handleMessage(Message msg)
									{
										switch (msg.what)
										{
											case EMPTY_RUN:
												if (100 <= mnProgress)
												{
													postMsg(MSG.DATA_REFLASH_OK, 0, 0, null);
													return;
												}
												mnProgress += 10;
												setLoadPercent(mnProgress);
												selfHandler.sendEmptyMessageDelayed(EMPTY_RUN, 500);
												break;
										}
									}
								};
}
