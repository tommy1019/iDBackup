package com.tommysource.iDTech;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfigureScreen
{
	
	JFrame window;
	
	JPanel contentPane;
	
	JLabel titleLogo;
	
	public ConfigureScreen()
	{
		window = new JFrame("iDBackup");
		
		Dimension windowSize = new Dimension(700, 600);
		
		window.setMaximumSize(windowSize);
		window.setMinimumSize(windowSize);
		
		contentPane = new JPanel();
		
		window.setContentPane(contentPane);
		
		contentPane.setLayout(new BorderLayout());
		
		//Load all images
		try
		{
			titleLogo = new JLabel(new ImageIcon(ImageIO.read(this.getClass().getResource("/iDBackupTitle.png"))));
			
		}
		catch (IOException e)
		{
			System.out.println("Error loading assets");
			e.printStackTrace();
		}
		
		contentPane.add(titleLogo, BorderLayout.CENTER);
		
		window.pack();
		window.setVisible(true);
	}
	
}
