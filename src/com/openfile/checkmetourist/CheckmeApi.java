package com.openfile.checkmetourist;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;

public class CheckmeApi
{
	private Context			theContext			= null;
	public final static int	API_SYSTEM_PRELOAD	= 0;
	public final static int	API_INVITE			= 1;
	public final static int	API_BANNER			= 2;
	public final static int	API_GIFT			= 4;
	public final static int	API_FIELD			= 5;
	public final static int	API_INVOICE			= 6;
	public final static int	API_COUPON			= 7;

	public final static int	API_USER_REGIST			= 8;
	public final static int	API_USER_LOGIN			= 9;
	public final static int	API_USER_FORGET_PASSWD	= 10;
	public final static int	API_QQ_USER_LOGIN		= 11;
	public final static int	API_USER_INFO			= 12;

	private final String openlife_server = "http://devapi.openlife.co";
	// private final String openlife_server = "https://web1.openlife.co";

	public final String	URL_API_SYSTEM_PRELOAD	= openlife_server + "/cmapi/v1/system/preload";
	public final String	URL_API_INVITE			= openlife_server + "/cmapi/v1/user/invite_code";
	public final String	URL_API_BANNER			= openlife_server + "/cmapi/v1/promotion/list";
	public final String	URL_API_GIFT			= openlife_server + "/cmapi/v1/gift/list";
	public final String	URL_API_FIELD			= openlife_server + "/cmapi/v1/assign/list";
	public final String	URL_API_INVOICE			= openlife_server + "/cmapi/v1/assign/commit_batch";
	public final String	URL_API_COUPON			= openlife_server + "/cmapi/v1/user/redeem_record";

	public final String	URL_API_USER_REGIST			= openlife_server + "/cmapi/v1/user/regist";
	public final String	URL_API_USER_LOGIN			= openlife_server + "/cmapi/v1/user/login";
	public final String	URL_API_USER_FORGET_PASSWD	= openlife_server + "/cmapi/v1/user/passwd/forget";
	public final String	URL_API_QQ_USER_LOGIN		= openlife_server + "/cmapi/v1/user/fb_login";
	public final String	URL_API_USER_INFO			= openlife_server + "/cmapi/v1/user/info";

	public CheckmeApi(Context context)
	{
		theContext = context;
	}

	public static String Code()
	{
		final String strNow = Utility.getTime();
		String secretKey = Utility.md5("Open0620Liiife$");
		String auth_key = Utility.md5(secretKey + strNow + ",@OpenLife@");
		return auth_key;
	}

	class sendPostRunnable implements Runnable
	{
		private int			nAPIIndex	= -1;
		private Handler		theHandler	= null;
		private String		strTaskId	= null;
		private String		strQrCode	= null;
		private JSONObject	strData		= null;

		@Override
		public void run()
		{
			String strResponse = null;
			switch(nAPIIndex)
			{
			case API_SYSTEM_PRELOAD:
				strResponse = systemPreload();
				break;
			case API_INVITE:
				strResponse = invite();
				break;
			case API_BANNER:
				strResponse = banner();
				break;
			case API_GIFT:
				strResponse = gift();
				break;
			case API_FIELD:
				strResponse = field();
				break;
			case API_INVOICE:
				strResponse = invoice(strTaskId, strQrCode);
				break;
			case API_COUPON:
				strResponse = coupon();
				break;
			case API_USER_REGIST:
				strResponse = userLoginAndRegist(strData, API_USER_REGIST);
				break;
			case API_USER_LOGIN:
				strResponse = userLoginAndRegist(strData, API_USER_LOGIN);
				break;
			case API_USER_FORGET_PASSWD:
				strResponse = userForgetPasswd(strData);
				break;
			case API_QQ_USER_LOGIN:
				strResponse = qqUserLogin();
				break;
			case API_USER_INFO:
				strResponse = userInfo();
			default:

				break;
			}

			if (null != theHandler)
			{
				Common.postMessage(theHandler, MSG.API_RESPONSE, nAPIIndex, 0, strResponse);
			}
		}

		public sendPostRunnable(final int nAPI, Handler handler)
		{
			nAPIIndex = nAPI;
			theHandler = handler;
		}

		public sendPostRunnable(final int nAPI, Handler handler, JSONObject data)
		{
			nAPIIndex = nAPI;
			theHandler = handler;
			strData = data;
		}

		public sendPostRunnable(final int nAPI, Handler handler, final String taskId, final String QrCode)
		{
			nAPIIndex = nAPI;
			theHandler = handler;
			strTaskId = taskId;
			strQrCode = QrCode;
		}

	}

	public boolean runApi(int nAPI, Handler handler)
	{
		if (Utility.checkInternet(theContext))
		{
			Thread t = new Thread(new sendPostRunnable(nAPI, handler));
			t.start();
			return true;
		}
		return false;
	}

	public boolean runApi(int nAPI, Handler handler, final String taskid, final String qrcode)
	{
		if (Utility.checkInternet(theContext))
		{
			Thread t = new Thread(new sendPostRunnable(nAPI, handler, taskid, qrcode));
			t.start();
			return true;
		}
		return false;
	}

	public boolean runApi(int nAPI, Handler handler, JSONObject memberData)
	{
		if (nAPI == API_USER_LOGIN || nAPI == API_USER_REGIST || nAPI == API_USER_FORGET_PASSWD)
		{
			Thread t = new Thread(new sendPostRunnable(nAPI, handler, memberData));
			t.start();
			return true;
		}

		return false;
	}

	public String userInfo()
	{
		String strResponse = null;
		try
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("account_id", Global.mAccountId);
			jsonObject.put("identifier", Global.mAndroidSerial);
			jsonObject.put("src", 1);
			jsonObject.put("status", 1);
			jsonObject.put("date", new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.TAIWAN).format(new Date()));
			jsonObject.put("auth_code", Code());

			strResponse = use_http(URL_API_USER_INFO, jsonObject);
		}
		catch (Exception e)
		{
			Logs.showTrace("Exception:" + e.getMessage());
		}

		return strResponse;

	}

	public String systemPreload()
	{
		String strResponse = null;
		try
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("auth_code", Code());
			jsonObject.put("date", Utility.getTime());
			jsonObject.put("app_ver", Global.Version);
			strResponse = use_http(URL_API_SYSTEM_PRELOAD, jsonObject);
		}
		catch (Exception e)
		{
			Logs.showTrace("Exception:" + e.getMessage());
		}
		return strResponse;
	}

	public String qqUserLogin()
	{
		String strResponse = null;
		try
		{
			JSONObject jsonObject = new JSONObject();
			Global.mAndroidSerial = Global.qq_openid;
			jsonObject.put("fbuid", Global.qq_openid);
			jsonObject.put("fb_token", Global.qq_token);
			jsonObject.put("identifier", Global.qq_openid);
			jsonObject.put("src", 1);
			strResponse = use_http(URL_API_QQ_USER_LOGIN, jsonObject);

		}
		catch (Exception e)
		{
			Logs.showTrace("Exception:" + e.getMessage());
		}

		return strResponse;

	}

	public String invite()
	{
		String strResponse = null;
		try
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("auth_code", Code());
			jsonObject.put("date", Utility.getTime());
			jsonObject.put("invite_code", Global.mInviteCode);
			jsonObject.put("account_id", Global.mAccountId);
			jsonObject.put("identifier", Global.mAndroidSerial);
			jsonObject.put("src", 1);
			strResponse = use_http(URL_API_INVITE, jsonObject);
		}
		catch (Exception e)
		{
			Logs.showTrace("Exception:" + e.toString());
		}
		return strResponse;
	}

	public String banner()
	{
		String strResponse = null;
		try
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("src", "1");
			jsonObject.put("account_id", Global.mAccountId);
			jsonObject.put("identifier", Global.mAndroidSerial);
			jsonObject.put("auth_code", Code());
			jsonObject.put("date", Utility.getTime());
			strResponse = use_http(URL_API_BANNER, jsonObject);
		}
		catch (Exception e)
		{
			Logs.showTrace("Exception:" + e.getMessage());
		}
		return strResponse;
	}

	public String gift()
	{
		String strResponse = null;
		try
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("account_id", Global.mAccountId);
			jsonObject.put("identifier", Global.mAndroidSerial);
			jsonObject.put("auth_code", Code());
			jsonObject.put("date", Utility.getTime());
			strResponse = use_http(URL_API_GIFT, jsonObject);
		}
		catch (Exception e)
		{
			Logs.showTrace("Exception:" + e.getMessage());
		}
		return strResponse;
	}

	public String field()
	{
		String strResponse = null;
		try
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("src", "1");
			jsonObject.put("date", Utility.getTime());
			jsonObject.put("auth_code", Code());
			jsonObject.put("account_id", Global.mAccountId);
			jsonObject.put("identifier", Global.mAndroidSerial);
			jsonObject.put("status", "1");
			strResponse = use_http(URL_API_FIELD, jsonObject);
		}
		catch (Exception e)
		{
			Logs.showTrace("Exception:" + e.getMessage());
		}
		return strResponse;
	}

	public String coupon()
	{
		String strResponse = null;
		try
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("src", "1");
			jsonObject.put("date", Utility.getTime());
			jsonObject.put("auth_code", Code());
			jsonObject.put("account_id", Global.mAccountId);
			jsonObject.put("identifier", Global.mAndroidSerial);
			strResponse = use_http(URL_API_COUPON, jsonObject);
		}
		catch (Exception e)
		{
			Logs.showTrace("Exception:" + e.getMessage());
		}
		return strResponse;
	}

	public String invoice(final String strTaskId, final String strQrCode)
	{
		String strResponse = null;
		try
		{
			String strDate = Utility.getTime();
			String strChecksum = Utility.md5(strTaskId + "Open0620Liiife$" + strDate);
			JSONObject jsonObject = new JSONObject();
			JSONObject jsonObject1 = new JSONObject();
			JSONObject jsonObject2 = new JSONObject();
			JSONArray jsonArray = new JSONArray();

			jsonObject2.put("invoice_qr_code", strQrCode);
			jsonObject2.put("is_debug", "0");

			jsonObject1.put("execution", jsonObject2);
			jsonObject1.put("mission_type", "I");
			jsonObject1.put("finish_date", strDate);
			jsonObject1.put("assign_id", strTaskId);
			jsonObject1.put("checksum", strChecksum);

			jsonArray.put(jsonObject1);

			jsonObject.put("account_id", Global.mAccountId);
			jsonObject.put("identifier", Global.mAndroidSerial);
			jsonObject.put("user_point", Global.mPoint);
			jsonObject.put("src", "1");
			jsonObject.put("auth_code", Code());
			jsonObject.put("date", strDate);
			jsonObject.put("assign_records", jsonArray);
			jsonObject.put("status", "1");

			strResponse = use_http(URL_API_INVOICE, jsonObject);
		}
		catch (Exception e)
		{
			Logs.showTrace("Exception:" + e.getMessage());
		}
		return strResponse;
	}

	public String userForgetPasswd(JSONObject jsonObject)
	{
		String strResponse = null;
		Logs.showTrace("in API_USER_FORGET_PASSWD");
		try
		{
			jsonObject.put("status", 1);
			jsonObject.put("auth_code", Code());
			jsonObject.put("date", new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.TAIWAN).format(new Date()));
			strResponse = use_http(URL_API_USER_FORGET_PASSWD, jsonObject);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strResponse;
	}

	public String userLoginAndRegist(JSONObject jsonObject, int nfag)
	{
		String strResponse = null;

		if (nfag == API_USER_LOGIN)
		{
			Logs.showTrace("in API_USER_LOGIN");
			try
			{
				jsonObject.put("identifier", Global.mAndroidSerial);
				jsonObject.put("src", 1);  
				jsonObject.put("auth_code", Code());
				jsonObject.put("date",new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));

				strResponse = use_http(URL_API_USER_LOGIN, jsonObject);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}

		}
		else if (nfag == API_USER_REGIST)
		{
			try
			{
				Logs.showTrace("in napi = API_USER_REGIST");
				String date = new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.TAIWAN).format(new Date());
				Logs.showTrace("in napi = API_USER_REGIST2");
				jsonObject.put("identifier", Global.mAndroidSerial);
				jsonObject.put("src", 1);
				jsonObject.put("date", date);
				jsonObject.put("status", 1);
				jsonObject.put("auth_code", Code());
				strResponse = use_http(URL_API_USER_REGIST, jsonObject);
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return strResponse;
	}

	public String use_http(String url, JSONObject jsonObject)
	{
		HttpResponse response = null;
		String mJsonText = null;

		try
		{
			Logs.showTrace("Checkme Request:" + url + jsonObject.toString());
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("App-Ver", Global.Version);
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 20000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 20000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			StringEntity entity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			HttpClient client = new DefaultHttpClient(httpParameters);
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Android");
			response = client.execute(httpPost);
			if (HttpURLConnection.HTTP_OK == response.getStatusLine().getStatusCode())
			{
				mJsonText = EntityUtils.toString(response.getEntity());
			}

			Logs.showTrace("Checkme Response:" + mJsonText);
			JSONObject jsonResp = new JSONObject(mJsonText);
			if (null != jsonResp)
			{
				Logs.showTrace("Checkme Response Status:" + jsonResp.getBoolean("status") + " Message:"
						+ jsonResp.getString("message"));
			}
		}
		catch (Exception e)
		{
			Logs.showTrace("Exception:" + e.getMessage());
		}
		return mJsonText;
	}
}
