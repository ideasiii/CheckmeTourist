package com.openfile.checkmetourist;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageLoaderHandler
{
	private ImageLoader	imageLoader	= null;
	private Context		theContext;

	public ImageLoaderHandler(Context context)
	{
		super();
		theContext = context;
	}

	public void init()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.downloadwait)
				.showImageForEmptyUri(R.drawable.a_loadimg).showImageOnFail(R.drawable.a_noimg).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(theContext.getApplicationContext())
				.defaultDisplayImageOptions(options).build();

		ImageLoader.getInstance().init(config);
		imageLoader = ImageLoader.getInstance();
	}

	public void loadImage(final String strImageUrl, ImageView ivPic)
	{
		if (null != imageLoader)
		{
			imageLoader.displayImage(strImageUrl, ivPic);
		}
	}

}
