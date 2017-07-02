package com.tommysource.iDTech;

import java.awt.AWTException;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class IDTechBackup
{

	public static StudentList studentList;
	public static TrayIcon trayIcon;
	public static SaveThread saveThread;

	public static Menu studentMenu;
	public static Menu removeMenu;

	public static OS os;

	enum OS
	{
		OSX, WIN;
	}

	public static void main(String[] args)
	{
		// Stop Dock icon from appearing
		System.setProperty("apple.awt.UIElement", "true");

		if (!SystemTray.isSupported())
		{
			System.out.println("System Tray is not supported, yell at Link");
		}

		String osString = System.getProperty("os.name").toLowerCase();

		if (osString.startsWith("mac"))
			os = OS.OSX;
		else
			os = OS.WIN;

		System.out.println();

		SystemTray tray = SystemTray.getSystemTray();

		PopupMenu popupMenu = new PopupMenu();

		studentMenu = new Menu("Back Up");
		popupMenu.add(studentMenu);

		removeMenu = new Menu("Remove");
		popupMenu.add(removeMenu);

		MenuItem exit = new MenuItem("Exit");
		exit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SystemTray.getSystemTray().remove(trayIcon);

				System.exit(0);
			}
		});

		MenuItem add = new MenuItem("Add Student");
		add.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				studentList.addStudent(JOptionPane.showInputDialog("Student Name:"));
			}
		});

		popupMenu.add(add);

		popupMenu.addSeparator();
		popupMenu.add(exit);

		studentList = new StudentList();

		studentList.loadFromFile();

		saveThread = new SaveThread();

		// Load Tray Icon
		try
		{
			trayIcon = new TrayIcon(ImageIO.read(IDTechBackup.class.getResource("/idlogo.png")));
			trayIcon.setImageAutoSize(true);
			trayIcon.setPopupMenu(popupMenu);
			tray.add(trayIcon);
		}
		catch (AWTException | IOException e)
		{
			System.out.println("Error loading tray icon: ");
			e.printStackTrace();
		}

		saveThread.start();
		displayToast("Backups started.");
	}

	public static void displayToast(String message)
	{
		if (os == OS.OSX)
		{
			try
			{
				Runtime.getRuntime().exec(new String[] { "osascript", "-e", "display notification \"" + message + "\" with title \"iDBackup\" sound name \"Funk\"" });
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			trayIcon.displayMessage(null, message, MessageType.INFO);
		}
	}

}
