package com.openfile.checkmetourist;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FlipperHandler extends BaseHandler
{
	private final int	RESOURCE_FLIPPER		= R.id.flipperViewOption;
	private FlipperView	flipper					= null;
	public static int	VIEW_ID_MISSION			= 0;
	public static int	VIEW_ID_GIFT_EXCHANGE	= 0;
	public static int	VIEW_ID_ABOUT_CONTENT	= 0;
	public static int	VIEW_ID_POINT_HISTORY	= 0;
	public static int	VIEW_ID_MY_REWARD		= 0;
	public static int	VIEW_ID_WEBVIEW			= 0;

	public FlipperHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
	}

	public boolean init()
	{
		flipper = (FlipperView) theActivity.findViewById(RESOURCE_FLIPPER);
		if (null == flipper)
		{
			Logs.showTrace("Flipper View Init Fail");
			return false;
		}

		flipper.setNotifyHandler(theHandler);
		VIEW_ID_MISSION = flipper.addChild(R.layout.mission);
		VIEW_ID_GIFT_EXCHANGE = flipper.addChild(R.layout.gift_exchange);
		VIEW_ID_ABOUT_CONTENT = flipper.addChild(R.layout.page_about_sub);
		VIEW_ID_POINT_HISTORY = flipper.addChild(R.layout.point_history);
		VIEW_ID_MY_REWARD = flipper.addChild(R.layout.my_reward);
		VIEW_ID_WEBVIEW = flipper.addChild(R.layout.webview);

		WebView web = (WebView) getView(VIEW_ID_WEBVIEW).findViewById(R.id.webView100);
		WebSettings websettings = web.getSettings();
		websettings.setSupportZoom(true);
		websettings.setBuiltInZoomControls(true);
		websettings.setJavaScriptEnabled(true);
		web.setWebViewClient(new WebViewClient());

		return true;
	}

	public View getView(final int nId)
	{
		return flipper.getChildView(nId);
	}

	public void showView(final int nViewId)
	{
		if (null != flipper)
		{
			flipper.showView(nViewId);
		}
	}

	public void close()
	{
		if (null != flipper)
		{
			flipper.close();
		}
	}
}
