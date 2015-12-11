package com.openfile.checkmetourist;

import java.util.HashMap;

import android.app.Activity;
import android.os.Handler;
import app.sensor.AppSensor;
import app.sensor.AppSensor.TransferMessage;

public class AppSensorHandler extends BaseHandler
{

	AppSensor appSensor = null;

	public AppSensorHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
		appSensor = new AppSensor(activity);
	}

	public void init()
	{
		appSensor.setOnTransferMessageListener(new TransferMessage()
		{

			@Override
			public void showLinkServerMessageResult(int result, int from, String message)
			{
				// TODO Auto-generated method stub
				Logs.showTrace("result :"+ String.valueOf(result) + "from :" +String.valueOf(from)+" message"+message);
				
			}

			

		});

		appSensor.startTracker("Test001", "1001512501", "tester", "tester@hotmail.com");
	}

	@Override
	protected void finalize() throws Throwable
	{
		appSensor.stopTracker();
		appSensor = null;
		super.finalize();
	}

	public void submit(String type, String source_from, String page, String production, String price)
	{
		HashMap<String, String> parm = new HashMap<String, String>();
		parm.put("TYPE", type);
		parm.put("SOURCE_FROM", source_from);
		parm.put("PAGE", page);
		parm.put("PRODUCTION", production);
		parm.put("PRICE", price);
		appSensor.track(parm);
		Logs.showTrace("track:" + type + " " + source_from + " " + page + " " + production + " " + price);
	}

}
