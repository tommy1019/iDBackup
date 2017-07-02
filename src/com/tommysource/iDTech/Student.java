package com.tommysource.iDTech;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;

public class Student
{
	public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");

	String name;
	Date lastBackup;

	Git git;

	public static Student studentFromSaveLine(String line)
	{
		String[] parts = line.split("\\|");
		Date d;
		try
		{
			d = DATE_FORMAT.parse(parts[1]);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			d = new Date();
			return null;
		}

		return new Student(parts[0], d);

	}

	public static Student newStudent(String name)
	{
		return new Student(name, Date.from(Instant.now()));
	}

	private Student(String name, Date lastBackup)
	{
		super();
		this.name = name;
		this.lastBackup = lastBackup;
	}

	public void backup()
	{
		try
		{
			git.add().addFilepattern(".").call();
			if (git.status().call().hasUncommittedChanges())
			{
				git.commit().setMessage("Backup @" + DATE_FORMAT.format(new Date())).call();
			}
			lastBackup = Date.from(Instant.now());
		}
		catch (NoFilepatternException e)
		{
			e.printStackTrace();
		}
		catch (GitAPIException e)
		{
			e.printStackTrace();
		}

	}

	public String getSaveLine()
	{
		return name + "|" + DATE_FORMAT.format(lastBackup);
	}

	public String getLastBackup()
	{
		return DATE_FORMAT.format(lastBackup);
	}
}
