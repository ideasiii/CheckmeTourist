package com.openfile.checkmetourist;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapHandler
{
	public static HashMap<String, Bitmap>	bitmaps	= null;

	public BitmapHandler()
	{
		super();
		bitmaps = new HashMap<String, Bitmap>();
	}

	@Override
	protected void finalize() throws Throwable
	{
		release();
		super.finalize();
	}

	public void release()
	{
		if (null != bitmaps)
		{
			for (Object key : bitmaps.keySet())
			{
				if (null != bitmaps.get(key))
				{
					bitmaps.get(key).recycle();
				}
			}
		}
		bitmaps.clear();
	}

	public boolean isExist(final String strUrl)
	{
		boolean bExist = false;
		if (null != bitmaps)
		{
			bExist = bitmaps.containsKey(strUrl);
			if (bExist)
			{
				bExist = false;
				if (null != bitmaps.get(strUrl) && !bitmaps.get(strUrl).isRecycled())
					bExist = true;
			}
		}
		return bExist;
	}

	public void getBitmapFromURL(final String strURL)
	{
		if (isExist(strURL))
		{
			Logs.showTrace("Get Bitmap From URL Fail, Bitmap Exist:" + strURL);
			return;
		}

		Thread t = new Thread(new BitmapRunnable(strURL));
		t.start();
	}

	class BitmapRunnable implements Runnable
	{
		private String	mstrURL;

		public BitmapRunnable(final String strURL)
		{
			mstrURL = strURL;
		}

		@Override
		public void run()
		{
			URL url;
			try
			{

				url = new URL(mstrURL);
				URLConnection conn = url.openConnection();
				HttpURLConnection httpConn = (HttpURLConnection) conn;
				httpConn.setRequestMethod("GET");
				httpConn.connect();
				if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
				{
					InputStream inputStream = httpConn.getInputStream();
					bitmaps.put(mstrURL, BitmapFactory.decodeStream(inputStream));
					inputStream.close();
					Logs.showTrace("Get Bitmap From URL Success:" + mstrURL);
				}
			}
			catch (Exception e)
			{
				Logs.showTrace("Exception" + e.getMessage());
			}
		}
	}

	public Bitmap getBitmap(final String strUrl)
	{
		if (null != bitmaps && isExist(strUrl))
		{
			Logs.showTrace("Get Bitmap Success:" + strUrl);
			return bitmaps.get(strUrl);
		}
		Logs.showTrace("Get Bitmap Fail:" + strUrl);
		return null;
	}

	public static void saveImage(String imageUrl, String destinationFile) throws IOException
	{
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1)
		{
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
}
