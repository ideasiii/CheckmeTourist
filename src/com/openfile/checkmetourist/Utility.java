package com.openfile.checkmetourist;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.ImageView;

public abstract class Utility
{

	static class ImageFromUrl extends AsyncTask<String, Integer, Bitmap>
	{
		ImageView	imgView	= null;
		String		strURL	= null;

		public ImageFromUrl(ImageView imageView)
		{
			imgView = imageView;
		}

		protected Bitmap doInBackground(String... strUrl)
		{
			strURL = strUrl[0];
			Bitmap bitmap = null;
			try
			{
				URL url = new URL(strURL);
				URLConnection conn = url.openConnection();
				HttpURLConnection httpConn = (HttpURLConnection) conn;
				httpConn.setRequestMethod("GET");
				httpConn.connect();

				if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
				{
					InputStream inputStream = httpConn.getInputStream();
					bitmap = BitmapFactory.decodeStream(inputStream);
					inputStream.close();
				}
			}
			catch (MalformedURLException e1)
			{
				e1.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			return bitmap;
		}

		protected void onPostExecute(Bitmap bitmap)
		{
			if (null != bitmap && null != imgView)
			{
				imgView.setImageBitmap(bitmap);
			}
		}
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx)
	{
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static boolean checkInvoice(final String strCode)
	{
		boolean bSuccess = false;

		if (null != strCode && 53 < strCode.length())
		{
			try
			{
				// 1. �o���r�y (10)�G�O���o������Q�X���X�C
				String strSerial = strCode.substring(0, 10);
				Logs.showTrace("�o���r�y (10)�G" + strSerial);

				// 2. �o���}�ߤ�� (7)�G�O���o���T�X����~���G�X����G�X����C
				String strDate = strCode.substring(10, 17);
				Logs.showTrace("�o���}�ߤ�� (7)�G" + strDate);

				// 3. �H���X (4)�G�O���o���W�H���X�|�X�C
				String strRandomCode = strCode.substring(17, 21);
				Logs.showTrace("�H���X (4)�G" + strRandomCode);

				// 4. �P���B
				// (8)�G�O���o���W���|�����B�`�p�K�X�A�N���B�ഫ�H�Q���i��覡�O���C�Y��~�H�P��t�εL�k���Q�N�|�������p��A�h�H
				// 00000000 �O���C
				String strNoTaxPrice = strCode.substring(21, 29);
				Logs.showTrace("���|�����B (8)�G" + strNoTaxPrice);
				if (!strNoTaxPrice.trim().equals("00000000"))
				{
					int nNoTaxPrice = Integer.parseInt(strNoTaxPrice.trim(), 16);
					Logs.showTrace("���|�����B (8) Int �G" + String.valueOf(nNoTaxPrice));
				}

				// 5. �`�p�B (8)�G�O���o���W�t�|�`���B�`�p�K�X�A�N���B�ഫ�H�Q���i ��覡�O���C
				String strTaxPrice = strCode.substring(29, 37);
				Logs.showTrace("�t�|�����B (8)�G" + strTaxPrice);
				int nTaxPrice = Integer.parseInt(strTaxPrice.trim(), 16);
				Logs.showTrace("�t�|�����B (8) Int �G" + String.valueOf(nTaxPrice));

				// 6. �R��Τ@�s�� (8)�G�O���o���W�R���H�Τ@�s���A�Y�R���H���@��� �O�̫h�H 00000000 �O���C
				String strBuyerUniform = strCode.substring(37, 45);
				Logs.showTrace("�R��Τ@�s�� (8)�G" + strBuyerUniform);

				// 7. ���Τ@�s�� (8)�G�O���o���W���Τ@�s���C
				String strSellUniform = strCode.substring(45, 53);
				Logs.showTrace("���Τ@�s�� (8)�G" + strSellUniform);

				if (null != strSerial && 10 == strSerial.trim().length() && 0 < nTaxPrice)
				{
					bSuccess = true;
				}
			}
			catch (Exception e)
			{
				Logs.showTrace("Exception:" + e.toString());
			}

		}
		return bSuccess;
	}

	public static boolean isValidStr(String strStr)
	{
		if (null != strStr && 0 < strStr.trim().length())
			return true;
		return false;
	}

	public static String convertNull(String strStr)
	{
		String strValue = strStr;
		if (null == strValue)
		{
			strValue = "";
		}
		return strValue;
	}

	/** convert null string to default string **/
	public static String convertNull(String strStr, String strDefault)
	{
		String strValue = strStr;

		if (!isValidStr(strStr))
		{
			strValue = strDefault;
		}

		return strValue;
	}

	/** Check String is numeric data type **/
	public static boolean isNumeric(String str)
	{
		if (!isValidStr(str))
			return false;
		return str.matches("[-+]?\\d*\\.?\\d+");
	}

	public static String UrlEncode(final String strText)
	{
		try
		{
			return URLEncoder.encode(convertNull(strText), HTTP.UTF_8);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static boolean checkInternet(Context conetxt)
	{
		boolean bValid = true;
		ConnectivityManager conManager = (ConnectivityManager) conetxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networInfo = conManager.getActiveNetworkInfo();

		if (null == networInfo || !networInfo.isAvailable())
		{
			bValid = false;
		}

		return bValid;
	}

	public static String getTime()
	{
		SimpleDateFormat formatter = null;
		formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate);
	}

	public static String md5(String string)
	{
		byte[] hash;
		try
		{
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash)
		{
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
}
