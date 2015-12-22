package com.openfile.checkmetourist;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

public class TencentHandler extends BaseHandler
{
	public static QQAuth	mQQAuth;
	private UserInfo		mInfo;
	private Tencent			mTencent;

	public TencentHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
	}

	public void init()
	{
		mQQAuth = QQAuth.createInstance(Global.TENCENT_APP_ID, theActivity.getApplicationContext());
		mTencent = Tencent.createInstance(Global.TENCENT_APP_ID, theActivity);
	}

	public void login()
	{
		if (!mQQAuth.isSessionValid())
		{
			IUiListener listener = new BaseUiListener()
			{
				@Override
				protected void doComplete(JSONObject values)
				{
					Logs.showTrace("QQ Login Complete: " + values.toString());
					updateUserInfo();
					if (null != values)
					{
						try
						{
							Global.mAccountId = Global.qq_openid = values.getString("openid");
							
							Logs.showTrace("QQ Open Id: " + Global.qq_openid);
							//Logs.showTrace("QQ Open token: " + Global.qq_token);
							
							TencentHandler.this.postMsg(MSG.QQ_LOGIN_COMPLETE, MSG.SUCCESS, 0, null);

							return;
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
					}
					TencentHandler.this.postMsg(MSG.QQ_LOGIN_COMPLETE, MSG.FAIL, 0, null);
				}
			};
			mQQAuth.login(theActivity, "all", listener);
			mTencent.login(theActivity, "all", listener);
			Logs.showTrace("QQ Login");
		}
		else
		{
			logout();
		}

	}

	public void logout()
	{
		mQQAuth.logout(theActivity);
		updateUserInfo();
		Logs.showTrace("QQ Logout");
	}

	private class BaseUiListener implements IUiListener
	{

		@Override
		public void onComplete(Object response)
		{
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values)
		{

		}

		@Override
		public void onError(UiError e)
		{
			Logs.showTrace("QQ Error: " + e.errorDetail);
		}

		@Override
		public void onCancel()
		{
			Logs.showTrace("QQ Cancel");
		}
	}

	private void updateUserInfo()
	{
		if (mQQAuth != null && mQQAuth.isSessionValid())
		{
			IUiListener listener = new IUiListener()
			{
				@Override
				public void onError(UiError e)
				{

				}

				@Override
				public void onComplete(final Object response)
				{
					Logs.showTrace("updateUserInfo QQ Response: " + response.toString());
					new Thread()
					{
						@Override
						public void run()
						{
							JSONObject json = (JSONObject) response;
							if (json.has("nickname"))
							{
								try
								{
									Global.qq_nickname = json.getString("nickname");
								}
								catch (JSONException e)
								{
									e.printStackTrace();
								}
							}

							if (json.has("figureurl"))
							{
								if (null != Global.qq_picture && !Global.qq_picture.isRecycled())
								{
									Global.qq_picture.recycle();
									Global.qq_picture = null;
								}
								try
								{
									Global.qq_picture = getbitmap(json.getString("figureurl_qq_2"));
								}
								catch (JSONException e)
								{

								}
							}
						}

					}.start();
				}

				@Override
				public void onCancel()
				{
				}
			};
			mInfo = new UserInfo(theActivity, mQQAuth.getQQToken());
			Global.qq_token = mQQAuth.getQQToken().getAccessToken();
			Logs.showTrace("qq_token"+Global.qq_token);//();
			mInfo.getUserInfo(listener);
		}
		else
		{

		}
	}

	private Bitmap getbitmap(String imageUri)
	{
		Bitmap bitmap = null;
		try
		{
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}
}
