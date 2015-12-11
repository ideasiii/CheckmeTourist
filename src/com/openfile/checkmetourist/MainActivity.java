package com.openfile.checkmetourist;

import org.json.JSONObject;

import com.openfile.checkmetourist.ListGiftHandler.GiftData;
import com.openfile.checkmetourist.MissionHandler.MissionData;
import com.zbar.lib.CaptureActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.WebView;
import app.sensor.AppSensor;

public class MainActivity extends Activity
{
	private ActionbarHandler	actionbarHandler	= null;
	private DrawerMenuHandler	drawerMenu			= null;
	private LoginHandler		loginHandler		= null;
	private InviteHandler		inviteHandler		= null;
	private ViewPagerHandler	viewPagerHandler	= null;
	private ListMenuHandler		listMenuHandler		= null;
	private ListGiftHandler		listGiftHandler		= null;
	private AdBannerHandler		adBannerHandler		= null;
	private ListFieldHandler	listFieldHandler	= null;
	private DataReflashHandler	dataReflashHandler	= null;
	private MainApplication		theApplication		= null;
	private ApiResponseHandler	apiResponse			= null;
	private FlipperHandler		flipperHandler		= null;
	private MissionHandler		missionHandler		= null;
	private GiftExchangeHandler	giftExchangeHandler	= null;
	private AboutHandler		aboutHandler		= null;
	private AccountHandler		accountHandler		= null;
				//騰訊
	private TencentHandler		tencentHandler		= null;
	private PointHistoryHandler	pointHistoryHandler	= null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		theApplication = (MainApplication) this.getApplication();
		theApplication.init(MainActivity.this);

		this.getActionBar().hide();

		apiResponse = new ApiResponseHandler(this, theHandler);
		tencentHandler = new TencentHandler(this, theHandler);

		setWelcomeView();
	}

	@Override
	protected void onStart()
	{
		if (null != tencentHandler)
		{
			tencentHandler.init();
		}
		super.onStart();
	}

	@Override
	protected void onDestroy()
	{
		if (null != tencentHandler)
		{
			tencentHandler.logout();
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (KeyEvent.KEYCODE_BACK == keyCode)
		{
			DialogHandler.showAlert(this, this.getString(R.string.leave_checkme), MSG.ID_LEAVE, theHandler);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setWelcomeView()
	{
		setContentView(R.layout.welcome);
		if (!theApplication.checkmeApi.runApi(CheckmeApi.API_SYSTEM_PRELOAD, theHandler))
		{
			DialogHandler.showNetworkError(this, true);
		}
	}

	private void setLoginView()
	{
		loginHandler = new LoginHandler(this, theHandler);
		loginHandler.init();
		Global.theApplication.submitLog("0", "CheckMe", "Login Layout", "", "");
	}

	private void setInviteView()
	{
		loginHandler = null;
		tencentHandler = null;
		inviteHandler = new InviteHandler(this, theHandler);
		inviteHandler.init();
		Global.theApplication.submitLog("0", "CheckMe", "Invite Layout", "", "");
	}

	private void setDataReflashView()
	{
		inviteHandler = null;
		dataReflashHandler = new DataReflashHandler(this, theHandler);
		dataReflashHandler.init();
		Global.theApplication.submitLog("0", "CheckMe", "Data Reflash Layout", "", "");
	}

	private void setMainView()
	{
		Global.theApplication.submitLog("0", "CheckMe", "start main view", "", "");

		dataReflashHandler = null;

		setContentView(R.layout.activity_main);

		actionbarHandler = new ActionbarHandler(this, theHandler);
		actionbarHandler.init();

		drawerMenu = new DrawerMenuHandler(this, theHandler);
		drawerMenu.init(R.id.drawer_layout);

		listMenuHandler = new ListMenuHandler(this, theHandler);
		listMenuHandler.init();

		viewPagerHandler = new ViewPagerHandler(this, theHandler);
		if (viewPagerHandler.init())
		{
			adBannerHandler = new AdBannerHandler(this, theHandler);
			adBannerHandler.init(viewPagerHandler.getView(ViewPagerHandler.PAGE_FIELD));

			listFieldHandler = new ListFieldHandler(this, theHandler);
			listFieldHandler.init(viewPagerHandler.getView(ViewPagerHandler.PAGE_FIELD));

			listGiftHandler = new ListGiftHandler(this, theHandler);
			listGiftHandler.init(viewPagerHandler.getView(ViewPagerHandler.PAGE_GIFT));

			accountHandler = new AccountHandler(this, theHandler);
			accountHandler.init(viewPagerHandler.getView(ViewPagerHandler.PAGE_ACCOUNT));

			aboutHandler = new AboutHandler(this, theHandler);
			aboutHandler.init(viewPagerHandler.getView(ViewPagerHandler.PAGE_ABOUT));
		}

		flipperHandler = new FlipperHandler(this, theHandler);
		if (flipperHandler.init())
		{
			missionHandler = new MissionHandler(this, theHandler);
			missionHandler.init(flipperHandler.getView(FlipperHandler.VIEW_ID_MISSION));

			giftExchangeHandler = new GiftExchangeHandler(this, theHandler);
			giftExchangeHandler.init(flipperHandler.getView(FlipperHandler.VIEW_ID_GIFT_EXCHANGE));

			pointHistoryHandler = new PointHistoryHandler(this, theHandler);
			pointHistoryHandler.init(flipperHandler.getView(FlipperHandler.VIEW_ID_POINT_HISTORY));
		}

		this.getActionBar().show();
		actionbarHandler.setPoint(Global.mPoint);
	}
	
	private void setMemberProgram(JSONObject memberData,int nfag)
	{
		
		theApplication.checkmeApi.runApi(nfag, theHandler,memberData);
 
	}
	private void setQQMemberProgram()
	{
		theApplication.checkmeApi.runApi(CheckmeApi.API_QQ_USER_LOGIN, theHandler);
	}
	
	private void setMemberInfoData()
	{
		theApplication.checkmeApi.runApi(CheckmeApi.API_USER_INFO, theHandler);
		
	}
	
	

	private void showMissionView(final String strChannelId)
	{
		missionHandler.setMissionData(strChannelId);
		flipperHandler.showView(FlipperHandler.VIEW_ID_MISSION);
		actionbarHandler.showBackBtn(true);
	}

	private void showAboutContent(int nContent)
	{
		aboutHandler.setContent(flipperHandler.getView(FlipperHandler.VIEW_ID_ABOUT_CONTENT), nContent);
		flipperHandler.showView(FlipperHandler.VIEW_ID_ABOUT_CONTENT);
		actionbarHandler.showBackBtn(true);
	}

	private void showAccountContent(int nConTent)
	{
		switch(nConTent)
		{
		case AccountHandler.ID_POINT_HISTORY: // �I�ƾ�{
			pointHistoryHandler.updateData();
			flipperHandler.showView(FlipperHandler.VIEW_ID_POINT_HISTORY);
			actionbarHandler.showBackBtn(true);
			Global.theApplication.submitLog("0", "CheckMe", "Point History Layout", "", "");
			break;
		case AccountHandler.ID_MY_REWARD: // �I������
			theApplication.checkmeApi.runApi(CheckmeApi.API_COUPON, theHandler);
			flipperHandler.showView(FlipperHandler.VIEW_ID_MY_REWARD);
			actionbarHandler.showBackBtn(true);
			Global.theApplication.submitLog("0", "CheckMe", "My Reward Layout", "", "");
			break;
		}
	}

	private void showBannerAdWeb(final String strUrl)
	{
		WebView webView = (WebView) flipperHandler.getView(FlipperHandler.VIEW_ID_WEBVIEW)
				.findViewById(R.id.webView100);
		webView.loadUrl(strUrl);
		flipperHandler.showView(FlipperHandler.VIEW_ID_WEBVIEW);
		actionbarHandler.showBackBtn(true);
		Global.theApplication.submitLog("0", "CheckMe", "AD Banner Layout", "", "");
	}

	private void showGiftExchangeView(final String strName, final String strInfo, final String strPic,
			final String strReward)
	{
		giftExchangeHandler.setGiftExchangeData(strName, strInfo, strPic, strReward);
		flipperHandler.showView(FlipperHandler.VIEW_ID_GIFT_EXCHANGE);
		actionbarHandler.showBackBtn(true);
		Global.theApplication.submitLog("0", "CheckMe", "Gift Exchange Layout", "", "");
	}

	private void showQrScanner(final int nMissionDataIndex, final String strReward)
	{
		// Intent openCameraIntent = new Intent(MainActivity.this,
		// CaptureActivity.class);
		Intent openCameraIntent = new Intent(MainActivity.this, InvoiceScanHandler.class);
		openCameraIntent.putExtra("missionindex", nMissionDataIndex);
		startActivityForResult(openCameraIntent, Global.SCANNER_REQUEST_CODE);
	}

	private void switchPage(final int nPage)
	{
		if (missionHandler.isShowMissionEnter())
		{
			missionHandler.closeMissionEnter();
		}

		flipperHandler.close();
		actionbarHandler.showBackBtn(false);

		drawerMenu.switchDisplay();

		switch(nPage)
		{
		case ListMenuHandler.PAGE_GIFT:
			viewPagerHandler.showPage(ViewPagerHandler.PAGE_GIFT);
			Global.theApplication.submitLog("0", "CheckMe", "Gift Page", "", "");
			break;
		case ListMenuHandler.PAGE_STORE:
			viewPagerHandler.showPage(ViewPagerHandler.PAGE_FIELD);
			Global.theApplication.submitLog("0", "CheckMe", "Field Page", "", "");
			break;
		case ListMenuHandler.PAGE_ACCOUNT:
			viewPagerHandler.showPage(ViewPagerHandler.PAGE_ACCOUNT);
			Global.theApplication.submitLog("0", "CheckMe", "Account Page", "", "");
			break;
		case ListMenuHandler.PAGE_SUPPORT:
			break;
		case ListMenuHandler.PAGE_ABOUNT:
			viewPagerHandler.showPage(ViewPagerHandler.PAGE_ABOUT);
			Global.theApplication.submitLog("0", "CheckMe", "About Page", "", "");
			break;
		}
	}
// need to add
	private void apiResponseFinish(final int nApiType, final int nResult)
	{
		if (MSG.RESULT_SUCCESS != nResult)
		{
			
			//以下為 login 錯誤 處理response error 事件
			//密碼錯誤
			if (nResult == 5104)
			{
				Logs.showTrace("密碼錯誤 5104");
				DialogHandler.showAlert(this, this.getString(R.string.password_error)+"\n"+
			this.getString(R.string.input_password_again), false);

			}
			//未註冊
			else if (nResult == 5101)
			{
				Logs.showTrace("未註冊 5101");
				DialogHandler.showAlert(this, this.getString(R.string.unregister_account), false);
				
			}
			//不同的手機進行登入  identifier 不一樣
			else if (nResult == 5103)
			{
				DialogHandler.showAlert(this, this.getString(R.string.login_fail), false);
	
			}
			//以下為register 錯誤 處理  response error 事件
			//重複性的帳號
			else if (nResult == 5123)
			{
				Logs.showTrace("重複性的帳號  5123");
				DialogHandler.showAlert(this, this.getString(R.string.repeat_regist_account), false);
				
			}
			//當user_status <0 時 此帳戶已被停權
			else if(nResult == -1111)
			{
				Logs.showTrace("帳號  已被停權");
				DialogHandler.showAlert(this, this.getString(R.string.suspended_account), false);
			}
			//遇到無法處理的 回傳 網路問題
			else
			{
				DialogHandler.showNetworkError(this, true);
			}
			
			return;
		}
		Logs.showTrace("type " + nApiType);
		switch (nApiType)
		{
		case CheckmeApi.API_SYSTEM_PRELOAD:
			setLoginView();
			break;
		case CheckmeApi.API_INVITE:
			setDataReflashView();
			break;
		case CheckmeApi.API_BANNER:
		case CheckmeApi.API_GIFT:
		case CheckmeApi.API_FIELD:
			dataReflashHandler.updateResult(nApiType, nResult);
			break;
		case CheckmeApi.API_INVOICE:
			break;
		case CheckmeApi.API_COUPON:
			break;
		case CheckmeApi.API_USER_LOGIN:
			Logs.showTrace("done to login");
			setMemberInfoData();
			//setInviteView();
			break;
		case CheckmeApi.API_USER_REGIST:
			Logs.showTrace("done to register");
			loginHandler.flipperViewClose();
			
			break;
		case CheckmeApi.API_USER_FORGET_PASSWD:
			Logs.showTrace("done to forget password");
			DialogHandler.showAlert(this, this.getString(R.string.get_forget_passwd_succ), false);
			loginHandler.flipperViewClose();
			break;
		case CheckmeApi.API_QQ_USER_LOGIN:
			Logs.showTrace("done to use QQ login");
			setMemberInfoData();
			//setInviteView();
			break;
		case CheckmeApi.API_USER_INFO:
			setInviteView();
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (Global.SCANNER_REQUEST_CODE == requestCode)
		{
			if (resultCode == RESULT_OK)
			{
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("barcode");
				String scanResult2 = bundle.getString("barcode2");
				int nMissionDataIndex = bundle.getInt("missionindex");

				Logs.showTrace("QR Code:" + scanResult);
				Logs.showTrace("QR Code2:" + scanResult2);

				missionHandler.missionResult(nMissionDataIndex, scanResult);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	private final Handler theHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case MSG.MENU_CLICK:
				drawerMenu.switchDisplay();
				break;
			case MSG.DRAWER_OPEN:
				actionbarHandler.setMenuState(true);
				break;
			case MSG.DRAWER_CLOSE:
				actionbarHandler.setMenuState(false);
				break;
			case MSG.LOGIN_SKIP:
				setInviteView();
				break;
			case MSG.API_RESPONSE:
				if (null == msg.obj)
					DialogHandler.showNetworkError(MainActivity.this, true);
				else
					apiResponse.response(msg.arg1, (String) msg.obj);
				break;
			case MSG.API_RESPONSE_FINISH:
				apiResponseFinish(msg.arg1, msg.arg2);
				break;
			case MSG.DATA_REFLASH_OK:
				setMainView();
				break;
			case MSG.FIELD_SELECTED:
				String strChannelId = (String) msg.obj;
				if (null != strChannelId)
				{
					showMissionView(strChannelId);
				}
				break;
			case MSG.BACK_CLICK:
				if (missionHandler.isShowMissionEnter())
				{
					missionHandler.closeMissionEnter();
				}
				else
				{
					flipperHandler.close();
					actionbarHandler.showBackBtn(false);
				}
				break;
			case MSG.MISSION_ITEM_SELECT:
				MissionHandler.MissionData mData = (MissionData) msg.obj;
				Logs.showTrace("Mission Data name:" + mData.strInfo);
				break;
			case MSG.QR_SCANNER:
				showQrScanner(msg.arg1, (String) msg.obj);
				break;
			case MSG.GIFT_SELECTED:
				ListGiftHandler.GiftData gData = (GiftData) msg.obj;
				Logs.showTrace("Gift Selected Name:" + gData.strName);
				showGiftExchangeView(gData.strName, gData.strInfo, gData.strPicUrl, gData.strPoint);
				break;
			case MSG.MENU_SELECTED:
				switchPage(msg.arg1);
				break;
			case MSG.ABOUT_CONTENT_SELECTED:
				showAboutContent(msg.arg1);
				break;
			case MSG.ACCOUNT_ITEM_SELECTED:
				showAccountContent(msg.arg1);
				break;
			case MSG.BANNER_SELECTED:
				showBannerAdWeb((String) msg.obj);
				break;
			case MSG.QQ_LOGIN:
				tencentHandler.login();
				break;
			case MSG.QQ_LOGIN_COMPLETE:
				if (MSG.SUCCESS == msg.arg1)
				{
					//setInviteView();
					setQQMemberProgram();
				}
				else
				{
					DialogHandler.showAlert(MainActivity.this, "QQ ", false);
				}
				break;
			case MSG.MEMBER_LOGIN:
				
				setMemberProgram((JSONObject)msg.obj, CheckmeApi.API_USER_LOGIN);

				break;
			case MSG.MEMBER_REGISTER:
				
				setMemberProgram((JSONObject)msg.obj,CheckmeApi.API_USER_REGIST);
				
				break;
			case MSG.MEMBER_FORGET_PASSWD:
				setMemberProgram((JSONObject)msg.obj,CheckmeApi.API_USER_FORGET_PASSWD);
				
			case MSG.DIALOG_CLICKED:
				if (MSG.ID_LEAVE == msg.arg1)
				{
					theApplication.Terminate();
				}
				break;
			
			}
		}
	};

}
