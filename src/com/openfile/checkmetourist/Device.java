package com.openfile.checkmetourist;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;

public class Device
{
	public Device()
	{

	}

	public String getIMEI(Activity activity)
	{
		TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		String strDeviceId = telephonyManager.getDeviceId();
		if (null == strDeviceId)
		{
			strDeviceId = Build.SERIAL;

			if (strDeviceId.equals("unknown"))
			{
				WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				strDeviceId = Utility.md5(wifiInfo.getMacAddress());
			}
		}
		return strDeviceId;
	}

	public String getLocalIpAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();

					// for getting IPV4 format
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
					{
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		}
		catch (Exception ex)
		{
			Logs.showTrace("IP Address" + ex.toString());
		}
		return null;
	}

	public static int getSdkVer()
	{
		int sdkInt;
		try
		{
			sdkInt = android.os.Build.VERSION.SDK_INT;
		}
		catch (NumberFormatException nfe)
		{
			sdkInt = 10000;
		}
		Logs.showTrace("Android SDK: " + String.valueOf(sdkInt));
		return sdkInt;
	}

	public static int getWidth(Context context)
	{
		int width = 0;
		width = getScreenResolution(context).x;
		return width;
	}

	public static int getHeight(Context context)
	{
		int height = 0;
		height = getScreenResolution(context).y;
		return height;
	}

	public static Point getScreenResolution(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}

}
