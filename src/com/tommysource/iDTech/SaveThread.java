package com.tommysource.iDTech;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SaveThread
{
	int delay = 900 * 1000; // Default 15 mins;

	TimerTask task = new TimerTask()
	{
		@Override
		public void run()
		{
			for (Student s : IDTechBackup.studentList.students)
				s.backup();
			IDTechBackup.studentList.saveToFile();
			IDTechBackup.displayToast("Backups created");
		}
	};

	Timer timer = new Timer("Backup Timer");

	public void start()
	{
		File timeFile = new File(System.getProperty("user.home") + "/backuptime");
		if (!timeFile.exists())
		{
			try
			{
				DataOutputStream out = new DataOutputStream(new FileOutputStream(timeFile));
				out.writeInt(delay / 60 / 1000);
				out.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			DataInputStream in = new DataInputStream(new FileInputStream(timeFile));
			delay = in.readInt() * 60 * 1000;
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		timer.cancel();
		timer = new Timer("Backup Timer");
		Date executionDate = new Date();
		timer.scheduleAtFixedRate(task, executionDate, delay);
	}

	public void stop()
	{
		timer.cancel();
	}
}
