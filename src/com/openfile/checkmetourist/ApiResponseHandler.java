package com.openfile.checkmetourist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;


public class ApiResponseHandler extends BaseHandler
{
	public ApiResponseHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
	}

	public void response(final int nApiType, final String strResponse)
	{
		if (null == strResponse)
			return;

		int nResult = MSG.RESULT_FAIL;
		switch(nApiType)
		{
		case CheckmeApi.API_SYSTEM_PRELOAD:
			nResult = systemPreload(strResponse);
			break;
		case CheckmeApi.API_INVITE:
			nResult = invite(strResponse);
			break;
		case CheckmeApi.API_BANNER:
			nResult = banner(strResponse);
			break;
		case CheckmeApi.API_GIFT:
			nResult = gift(strResponse);
			break;
		case CheckmeApi.API_FIELD:
			nResult = field(strResponse);
			break;
		case CheckmeApi.API_INVOICE:
			nResult = invoice(strResponse);
			break;
		case CheckmeApi.API_COUPON:
			nResult = coupon(strResponse);
			break;
		case CheckmeApi.API_USER_LOGIN:
			nResult = userLogin(strResponse);
			break;
		case CheckmeApi.API_USER_REGIST:
			nResult = userRegist(strResponse);
			break;
		case CheckmeApi.API_USER_FORGET_PASSWD:
			nResult = userForgetPasswd (strResponse);
			break;
		case CheckmeApi.API_QQ_USER_LOGIN:
			nResult = userQQLogin (strResponse);
			break;
		case CheckmeApi.API_USER_INFO:
			nResult = userInfo (strResponse);
			break;
		default:
			return;
		}

		postMsg(MSG.API_RESPONSE_FINISH, nApiType, nResult, null);
	}
	
	private int userInfo(final String strResponse)
	{
		int nResult = MSG.RESULT_FAIL;
		try
		{
			JSONObject jsonobj = new JSONObject(strResponse);
			int nError = jsonobj.getInt("error");
			if(nError == 0)
			{
				Global.mPoint = jsonobj.getInt("point");
				nResult = MSG.RESULT_SUCCESS;
			}
		
		}
		catch (JSONException e)
		{
			Logs.showError("Exception:" + e.toString());
		}
		
		return nResult;
		
	}
	
	private int userQQLogin(final String strResponse)
	{
		int nResult = MSG.RESULT_FAIL;
		try
		{
			JSONObject jsonobj = new JSONObject(strResponse);
			nResult = jsonobj.getInt("error");
			if(nResult == 0)
			{
				Global.mAccountId = jsonobj.getString("account_id");
				
				if (jsonobj.getInt("user_status") < 0)
				{
					nResult = -1111;
				} 
				else
				{
					nResult = MSG.RESULT_SUCCESS;
					Logs.showTrace("get QQlogin success response");
				}
			}
			
		}
		catch (JSONException e)
		{
			Logs.showError("Exception:" + e.toString());
		}
		
		return nResult;
		
		
		
	}
	
	// nUserStatus 待處理
	private int userLogin(final String strResponse)
	{
		int nResult = MSG.RESULT_FAIL;
		try
		{
			JSONObject jsonobj = new JSONObject(strResponse);
			int nError = jsonobj.getInt("error");
			int nUserStatus = 0; 
			boolean nStatus = jsonobj.getBoolean("status");
			
			
			
			if(nError == 0 && nStatus == true)
			{
			 Global.mAccountId = jsonobj.getString("account_id");
			 Global.mInviteCode = jsonobj.getString("invite_code");
			 nUserStatus = jsonobj.getInt("user_status");
			
			 if(nUserStatus < 0)
			 {
				 nResult = -1111;
			 }
			 else
			 {
				nResult = MSG.RESULT_SUCCESS;
				Logs.showTrace("get login success response"); 
			 }
			 
			 
			}
			else 
			{
				nResult = nError;
				
				
			}
			
			
		}
		catch (JSONException e)
		{
			Logs.showError("Exception:" + e.toString());
		}
		
		return nResult;
			
	}
	// nUserStatus 待處理
	private int userRegist(final String strResponse)
	{
		int nResult = MSG.RESULT_FAIL;
		JSONObject jsonobj;
		try
		{
			jsonobj = new JSONObject(strResponse);
			int nError = jsonobj.getInt("error");
			
			boolean nStatus = jsonobj.getBoolean("status");
			
			if(nError == 0 && nStatus == true)
			{
			 Global.mAccountId = jsonobj.getString("account_id");
			 Global.mInviteCode = jsonobj.getString("invite_code");
			
			
			 nResult = MSG.RESULT_SUCCESS;
			 Logs.showTrace("get register response");
			}
			else 
			{
				nResult = nError;
				
			}
			/*
			//以下為register 錯誤 處理  response error 事件
			//重複性的帳號
			else if (nError == 5123)
			{
				
				
				
			}*/
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return nResult;
	}
	private int userForgetPasswd(final String strResponse)
	{
		int nResult = MSG.RESULT_FAIL;
		try
		{
			JSONObject jsonobj = new JSONObject(strResponse);
			int nError = jsonobj.getInt("error");
			if (0 == nError)
			{
				nResult = MSG.RESULT_SUCCESS;
			} else
			{
				nResult = nError;
			}
		} 
		catch (JSONException e)
		{
			Logs.showError("Exception:" + e.toString());
		}
		return nResult;
	}

	private int invite(final String strResponse)
	{
		// int nResult = MSG.RESULT_FAIL;
		int nResult = MSG.RESULT_SUCCESS; // for test
		try
		{
			JSONObject jsonobj = new JSONObject(strResponse);
			int nError = jsonobj.getInt("error");
			if (0 == nError)
			{
				/*
				 * if(Var.inv_api_status.equals("true")){ Var.inv_error_msg =
				 * job.getString("response_msg"); Var.point =
				 * Integer.valueOf(job.getString("user_point"));
				 * Var.inv_bonus_point =
				 * Integer.valueOf(job.getString("bonus_point"));
				 * 
				 * Global.mMobileBonus = jsonobj.getString("mobile_bonus");
				 * Global.mNeedUpdate = jsonobj.getString("need_update");
				 * Global.mShowMsg = jsonobj.getString("show_msg");
				 * Logs.showTrace("Data Mobile Bonus:" + Global.mMobileBonus);
				 * Logs.showTrace("Data Need Update:" + Global.mNeedUpdate);
				 * Logs.showTrace("Data Show Msg:" + Global.mShowMsg);
				 */
				nResult = MSG.RESULT_SUCCESS;
			}
			else
			{
				String strMessage = jsonobj.getString("message");
				Logs.showTrace("API Response Error:" + strMessage);
			}
		}
		catch (JSONException e)
		{
			Logs.showError("Exception:" + e.toString());
		}
		return nResult;
	}

	private int systemPreload(final String strResponse)
	{
		int nResult = MSG.RESULT_FAIL;
		try
		{
			JSONObject jsonobj = new JSONObject(strResponse);
			int nError = jsonobj.getInt("error");
			if (0 == nError)
			{
				Global.mMobileBonus = jsonobj.getString("mobile_bonus");
				Global.mNeedUpdate = jsonobj.getString("need_update");
				Global.mShowMsg = jsonobj.getString("show_msg");
				Global.mGiftDataVer = jsonobj.getString("gift_data_ver");
				Global.mSensorDataVer = jsonobj.getString("sensor_data_ver");
				Logs.showTrace("Data Mobile Bonus:" + Global.mMobileBonus);
				Logs.showTrace("Data Need Update:" + Global.mNeedUpdate);
				Logs.showTrace("Data Show Msg:" + Global.mShowMsg);
				Logs.showTrace("Gift Data Version:" + Global.mGiftDataVer);
				Logs.showTrace("Sensor Data Version:" + Global.mSensorDataVer);

				if (Global.mNeedUpdate.equals("1"))
				{
					Global.mUpdateURL = jsonobj.getString("update_url");
					Logs.showTrace("Data Update URL:" + Global.mUpdateURL);
				}
				nResult = MSG.RESULT_SUCCESS;
			}
			else
			{
				String strMessage = jsonobj.getString("message");
				Logs.showTrace("API Response Error:" + strMessage);
			}
		}
		catch (JSONException e)
		{
			Logs.showError("Exception:" + e.toString());
		}
		return nResult;
	}

	private int banner(final String strResponse)
	{
		int nResult = MSG.RESULT_FAIL;
		try
		{
			JSONObject jsonobj = new JSONObject(strResponse);
			if (0 == jsonobj.getInt("error"))
			{
				JSONArray bannerList = new JSONArray(jsonobj.getString("list"));
				Global.theApplication.checkmeDB.clearBannerData();
				int nCount = bannerList.length();
				for (int i = 0; i < nCount; ++i)
				{
					JSONObject banner = bannerList.getJSONObject(i);
					Global.theApplication.checkmeDB.addBannerData(banner.getString("promo_id"), banner.getString("img"),
							banner.getString("start_date"), banner.getString("end_date"),
							new JSONObject(banner.getString("content")).getString("url"));
				}
				nResult = MSG.RESULT_SUCCESS;
			}
		}
		catch (JSONException e)
		{
			Logs.showError("Exception:" + e.toString());
		}

		return nResult;
	}

	private int gift(final String strResponse)
	{
		int nResult = MSG.RESULT_FAIL;
		try
		{
			JSONObject jsonobj = new JSONObject(strResponse);
			if (0 == jsonobj.getInt("error"))
			{
				JSONArray gifts = new JSONArray(jsonobj.getString("gifts"));
				Global.theApplication.checkmeDB.clearGiftData();
				int nCount = gifts.length();
				for (int i = 0; i < nCount; ++i)
				{
					JSONObject gift = gifts.getJSONObject(i);
					Global.theApplication.checkmeDB.addGiftData(gift.getString("remote_pid"), gift.getString("name"),
							gift.getString("point_cost"), gift.getString("notice_txt"), gift.getString("img"),
							String.valueOf(gift.getInt("fav_times")), gift.getString("start_date"),
							gift.getString("end_date"), "NULL", gift.getString("ver_id"));
				}
				nResult = MSG.RESULT_SUCCESS;
			}
		}
		catch (JSONException e)
		{
			Logs.showError("Exception:" + e.toString());
		}
		return nResult;
	}

	private int field(final String strResponse)
	{
		int nResult = MSG.RESULT_FAIL;
		try
		{
			JSONObject jsonobj = new JSONObject(strResponse);
			if (0 == jsonobj.getInt("error"))
			{
				JSONArray fields = new JSONArray(jsonobj.getString("data"));
				Global.theApplication.checkmeDB.clearFieldData();
				Global.theApplication.checkmeDB.clearTaskData();
				int nCount = fields.length();
				String strFieldId = "";
				for (int i = 0; i < nCount; ++i)
				{
					JSONObject field = fields.getJSONObject(i);
					strFieldId = String.valueOf(field.getInt("channel_id"));
					Global.theApplication.checkmeDB.addFieldData(strFieldId, field.getString("name"),
							field.getString("img"), field.getString("desc"), field.getString("reward"),
							field.getString("img2"), field.getString("udt"));

					JSONArray field_datas = field.getJSONArray("data");
					for (int j = 0; j < field_datas.length(); ++j)
					{
						JSONObject field_data = field_datas.getJSONObject(j);
						JSONArray tasks = field_data.getJSONArray("data");
						for (int k = 0; k < tasks.length(); ++k)
						{
							JSONObject task = tasks.getJSONObject(k);
							if (task.getString("mission_type").equals("I"))
							{
								Global.theApplication.checkmeDB.addTaskData(task.getString("id"),
										task.getString("name"), task.getString("mission_type"), task.getString("img"),
										String.valueOf(task.getInt("reward")), task.getString("desc"),
										task.getString("start_date"), task.getString("end_date"), "", "", "0",
										strFieldId, String.valueOf(task.getInt("brand_id")), task.getString("udt"),
										String.valueOf(task.getInt("personal_once_freq")),
										String.valueOf(task.getInt("personal_max_time")), "", "0", "0",
										String.valueOf(task.getInt("rest_amount")),
										String.valueOf(task.getInt("daily_rest_amount")));
							}
						}
					}
				}
				nResult = MSG.RESULT_SUCCESS;
			}
		}
		catch (JSONException e)
		{
			Logs.showError("Exception:" + e.toString());
		}
		return nResult;
	}

	private int invoice(final String strResponse)
	{
		int nResult = MSG.RESULT_SUCCESS; // for test
		return nResult;
	}

	private int coupon(final String strResponse)
	{
		int nResult = MSG.RESULT_FAIL;
		try
		{
			JSONObject jsonobj = new JSONObject(strResponse);
			if (0 == jsonobj.getInt("error"))
			{
				Global.theApplication.checkmeDB.clearCouponData();
				JSONArray coupons = new JSONArray(jsonobj.getString("gift_list"));
				for (int i = 0; i < coupons.length(); ++i)
				{
					JSONObject jsoncoupon = coupons.getJSONObject(i);
					Global.theApplication.checkmeDB.addCouponData(jsoncoupon.getString("gift_remote_pid"),
							jsoncoupon.getString("gift_content"), jsoncoupon.getString("expire_date"));
				}
				nResult = MSG.RESULT_SUCCESS;
			}
		}
		catch (JSONException e)
		{
			Logs.showError("Exception:" + e.toString());
		}
		return nResult;
	}
}
