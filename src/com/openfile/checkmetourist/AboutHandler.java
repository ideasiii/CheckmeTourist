package com.openfile.checkmetourist;

import android.app.Activity;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutHandler extends BaseHandler
{
	public final static int	ID_CONTENT_PRIVACY	= 0;
	public final static int	ID_CONTENT_SERVICE	= 1;
	private TextView		tvPrivateBtn		= null;
	private TextView		tvServiceBtn		= null;

	public AboutHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
	}

	public void init(View viewAbout)
	{
		if (null == viewAbout)
			return;

		tvPrivateBtn = (TextView) viewAbout.findViewById(R.id.textViewPageAboutPrivateBtn);
		tvServiceBtn = (TextView) viewAbout.findViewById(R.id.textViewPageAboutServiceBtn);

		if (null != tvPrivateBtn)
		{
			tvPrivateBtn.setTag(ID_CONTENT_PRIVACY);
			tvPrivateBtn.setOnTouchListener(buttonTouchListener);
		}

		if (null != tvServiceBtn)
		{
			tvServiceBtn.setTag(ID_CONTENT_SERVICE);
			tvServiceBtn.setOnTouchListener(buttonTouchListener);
		}

	}

	public void setContent(View view, final int nContent)
	{
		if (null == view)
			return;

		ImageView ivContent = (ImageView) view.findViewById(R.id.imageViewPageAboutContent);
		TextView tvContentTitle = (TextView) view.findViewById(R.id.textViewPageAboutContentTitle);

		switch (nContent)
		{
			case ID_CONTENT_PRIVACY:
				tvContentTitle.setText(R.string.privacy_content);
				ivContent.setImageResource(R.drawable.a_privacy_content);
				break;
			case ID_CONTENT_SERVICE:
				tvContentTitle.setText(R.string.service_content);
				ivContent.setImageResource(R.drawable.a_service_content);
				break;
		}
	}

	private OnTouchListener	buttonTouchListener	= new OnTouchListener()
												{
													@Override
													public boolean onTouch(View v, MotionEvent event)
													{
														if (v instanceof TextView)
														{
															switch (event.getAction())
															{
																case MotionEvent.ACTION_DOWN:
																	AboutHandler.this.postMsg(
																			MSG.ABOUT_CONTENT_SELECTED,
																			(Integer) v.getTag(), 0, null);
																	break;
															}
														}
														return true;
													}
												};
}
