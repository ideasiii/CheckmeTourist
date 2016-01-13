package com.openfile.checkmetourist;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

public class MainApplication extends Application
{
	public CheckmeApi			checkmeApi	= null;
	public SqliteHandler		checkmeDB	= null;
	public ImageLoaderHandler	imageLoader	= null;
	private AppSensorHandler	appSensor	= null;
	public String				IMEI		= null;
	public String				IP			= null;

	public MainApplication()
	{
		Global.theApplication = this;
	}

	public void init(Activity activity)
	{
		checkmeApi = new CheckmeApi(activity);
		checkmeDB = new SqliteHandler(activity);
		// initAndroidSerial(activity);
		imageLoader = new ImageLoaderHandler(activity);
		imageLoader.init();
		appSensor = new AppSensorHandler(activity, null);
		if (Utility.checkInternet(this))
		{
			appSensor.init();
		}

		Device device = new Device();
		IMEI = device.getIMEI(activity);
		Logs.showTrace("IMEI:" + IMEI);
		IP = device.getLocalIpAddress();
		Logs.showTrace("IP:" + IP);
		device = null;
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
	}

	public void Terminate()
	{
		imageLoader = null;
		checkmeApi = null;
		appSensor = null;
		checkmeDB.close();
		Logs.showTrace("Checkme Tourist Terminate!!");
		System.exit(0);
	}

	@SuppressWarnings("unused")
	private void initAndroidSerial(Context context)
	{
		Global.mAndroidSerial = Build.SERIAL;

		if (Global.mAndroidSerial.equals("unknown"))
		{
			Logs.showError("in unknown serial");
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			Global.mAndroidSerial = Utility.md5(wifiInfo.getMacAddress());
		}
	}

	public void submitLog(String type, String source_from, String page, String production, String price)
	{
		appSensor.submit(type, source_from, page, production, price);
	}
}
