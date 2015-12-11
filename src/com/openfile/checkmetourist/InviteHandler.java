package com.openfile.checkmetourist;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class InviteHandler extends BaseHandler
{

	private TextView	tvInviteOK		= null;
	private EditText	etInviteCode	= null;

	public InviteHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
		if (null != activity)
		{
			activity.setContentView(R.layout.invite);
		}
	}

	public void init()
	{
		if (null == theActivity)
			return;

		tvInviteOK = (TextView) theActivity.findViewById(R.id.textViewInviteOK);
		etInviteCode = (EditText) theActivity.findViewById(R.id.editTextInviteCode);
		if (null != tvInviteOK)
		{
			tvInviteOK.setOnClickListener(inviteClickListener);
		}
	}

	private OnClickListener	inviteClickListener	= new OnClickListener()
												{
													@Override
													public void onClick(View v)
													{
														hideKeyboard();
														Global.mInviteCode = etInviteCode.getText().toString();
														if (null != Global.mInviteCode
																&& 0 < Global.mInviteCode.trim().length())
														{
															tvInviteOK.setVisibility(View.INVISIBLE);
															Global.theApplication.checkmeApi.runApi(
																	CheckmeApi.API_INVITE, theHandler);
														}
														else
														{
															DialogHandler.showAlert(theActivity, theActivity.getString(R.string.invite_error), false);
														}
													}
												};

}
