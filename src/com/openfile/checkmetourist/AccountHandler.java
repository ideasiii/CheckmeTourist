package com.openfile.checkmetourist;

import android.app.Activity;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class AccountHandler extends BaseHandler
{
	public static final int	ID_POINT_HISTORY	= 0;
	public static final int	ID_MY_REWARD		= 1;

	private ImageView		ivPointHistory		= null;
	private ImageView		ivMyReward			= null;

	public AccountHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
	}

	public void init(View viewAccount)
	{
		if (null == viewAccount)
			return;

		ivPointHistory = (ImageView) viewAccount.findViewById(R.id.imageViewPointHistory);
		ivMyReward = (ImageView) viewAccount.findViewById(R.id.imageViewMyRewards);

		if (null != ivPointHistory)
		{
			ivPointHistory.setTag(ID_POINT_HISTORY);
			ivPointHistory.setOnTouchListener(itemTouchListener);
		}

		if (null != ivMyReward)
		{
			ivMyReward.setTag(ID_MY_REWARD);
			ivMyReward.setOnTouchListener(itemTouchListener);
		}
	}

	private OnTouchListener	itemTouchListener	= new OnTouchListener()
												{
													@Override
													public boolean onTouch(View v, MotionEvent event)
													{
														if (MotionEvent.ACTION_DOWN == event.getAction())
														{
															if (v instanceof ImageView)
															{
																AccountHandler.this.postMsg(MSG.ACCOUNT_ITEM_SELECTED,
																		(Integer) v.getTag(), 0, null);
															}
														}
														return true;
													}
												};
}
