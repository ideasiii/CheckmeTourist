package com.openfile.checkmetourist;

import java.util.HashMap;
import android.app.Activity;
import android.os.Handler;
import sdk.ideas.tracker.Tracker;
import sdk.ideas.tracker.Tracker.TransferMessage;

public class AppSensorHandler extends BaseHandler
{

	Tracker tracker = null;

	public AppSensorHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
		tracker = new Tracker(activity);
	}

	public void init()
	{
		tracker.setOnTransferMessageListener(new TransferMessage()
		{

			@Override
			public void showLinkServerMessageResult(int result, int from, String message)
			{
<<<<<<< HEAD
=======
				// TODO Auto-generated method stub
>>>>>>> 7dcab12bcf42d1c155f1974267be6772726dcc2d
				Logs.showTrace(
						"result :" + String.valueOf(result) + "from :" + String.valueOf(from) + " message: " + message);
			}

		});

		// tracker.startTracker("1349333576093", "1001512501", "tester",
		// "tester@hotmail.com");
		tracker.startTracker("1349333576093");
	}

	@Override
	protected void finalize() throws Throwable
	{
		tracker.stopTracker();
		tracker = null;
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
<<<<<<< HEAD
		Logs.showTrace("track:" + type + " " + source_from + " " + page + " " + production + " " + price);
=======
		Logs.showTrace("track:" + type + " " + source_from + " " + page + " " + production + " " + price + " "
				+ parm.get("DATE"));
>>>>>>> 7dcab12bcf42d1c155f1974267be6772726dcc2d
		tracker.track(parm);

	}

}
