package com.openfile.checkmetourist;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.os.Environment;

public class FileHandler
{
	private static File fileExternalDir = null;

	public FileHandler()
	{

	}

	public void init(Context context)
	{
		if (isExtStorageWritable() && isExtStorageReadable())
		{
			fileExternalDir = new File(
					Environment.getExternalStorageDirectory() + "/Android/data/" + context.getPackageName() + "/Files");

			if (!fileExternalDir.exists())
			{
				if (!fileExternalDir.mkdirs())
				{
					fileExternalDir = null;
					return;
				}
			}

			Logs.showTrace("File Storage is Created:" + fileExternalDir.getAbsolutePath());
			File outFile = new File(fileExternalDir, "version.txt");
			writeToFile(outFile, Global.Version);
		}
	}

	//檢查外部儲存體是否可以進行寫入
	public boolean isExtStorageWritable()
	{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			return true;
		}
		return false;
	}

	//檢查外部儲存體是否可以進行讀取
	public boolean isExtStorageReadable()
	{
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
		{
			return true;
		}
		return false;
	}

	private void writeToFile(File fout, String data)
	{
		FileOutputStream osw = null;
		try
		{
			osw = new FileOutputStream(fout);
			osw.write(data.getBytes());
			osw.flush();
			Logs.showTrace("Write String:" + data);
		}
		catch (Exception e)
		{
			;
		}
		finally
		{
			try
			{
				osw.close();
			}
			catch (Exception e)
			{
				;
			}
		}
	}

}
