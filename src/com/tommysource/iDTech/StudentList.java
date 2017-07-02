package com.tommysource.iDTech;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class StudentList
{
	public static final String FILE_NAME = "idbackuplist";

	ArrayList<Student> students = new ArrayList<>();

	public StudentList()
	{

	}

	public void loadFromFile()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(getDatabaseFile()));

			students.clear();

			String curLine = null;

			while ((curLine = in.readLine()) != null)
			{
				Student s = Student.studentFromSaveLine(curLine);

				File dir = getProjectFolder(s);

				Git git = Git.open(getGitDir(s));
				s.git = git;

				if (!dir.exists() || git == null)
				{
					JOptionPane.showMessageDialog(null, "Error: Not project folder found for " + s.name + ", creating new one.", "Error", JOptionPane.INFORMATION_MESSAGE);
					addStudent(s.name);
				}
				else
				{
					students.add(s);
				}
			}

			in.close();
			
			updateList();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void saveToFile()
	{
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(getDatabaseFile()));

			for (Student s : students)
			{
				out.write(s.getSaveLine() + "\n");
			}

			out.close();
			
			updateList();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void updateList()
	{
		IDTechBackup.studentMenu.removeAll();
		IDTechBackup.removeMenu.removeAll();
		
		for (Student s : IDTechBackup.studentList.students)
		{
			MenuItem item = new MenuItem(s.name + " - " + s.getLastBackup());
			item.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					s.backup();
					IDTechBackup.studentList.saveToFile();
				}
			});
			
			
			//Remove code
			MenuItem rItem = new MenuItem(s.name);
			rItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					IDTechBackup.studentList.students.remove(s);
					IDTechBackup.studentList.saveToFile();
					IDTechBackup.displayToast("Student successfully removed");
				}
			});
			
			IDTechBackup.studentMenu.add(item);
			IDTechBackup.removeMenu.add(rItem);
		}
	}
	
	public void addStudent(String name)
	{
		Student s = Student.newStudent(name);

		File dir = getProjectFolder(s);
		dir.mkdir();
		getGitDir(s).mkdir();

		try
		{
			Git git = Git.init().setGitDir(getGitDir(s)).setDirectory(dir).call();
			s.git = git;

			students.add(s);
			saveToFile();
			IDTechBackup.displayToast("Student successfully created.");
		}
		catch (IllegalStateException | GitAPIException e)
		{
			e.printStackTrace();
		}
	}

	public static File getDatabaseFile()
	{
		return new File(System.getProperty("user.home") + "/" + FILE_NAME);
	}

	public static File getGitDir(Student s)
	{
		return new File(System.getProperty("user.home") + "/" + s.name);
	}

	public static File getProjectFolder(Student s)
	{
		return new File(System.getProperty("user.home") + "/Desktop/" + s.name);
	}
}
