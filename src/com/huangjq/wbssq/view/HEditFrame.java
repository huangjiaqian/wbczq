package com.huangjq.wbssq.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import com.huangjq.wbssq.commons.FileUtil;
import javax.swing.JScrollPane;

public class HEditFrame extends JFrame{

	private static final long serialVersionUID = 8692732670172859996L;
	
	private JTextArea mainTextArea;
	private String currentFilePath = "";
	
	public HEditFrame(){
		setBackground(Color.WHITE);
		
		mainTextArea = new JTextArea();
		mainTextArea.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.isControlDown()){
					if(e.getWheelRotation() == -1){ //上移
						Font font = mainTextArea.getFont();
						Font fontNew = new Font(font.getName(), font.getStyle(), font.getSize()+1);
						mainTextArea.setFont(fontNew);
					}else{
						Font font = mainTextArea.getFont();
						Font fontNew = new Font(font.getName(), font.getStyle(), font.getSize()-1);
						mainTextArea.setFont(fontNew);
					}
					
				}
			}
		});
		mainTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()){
					System.out.println("保存");
					saveFile();
				}
			}
		});
		//getContentPane().add(mainTextArea, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		scrollPane.setViewportView(mainTextArea);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("文件");
		mnNewMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		menuBar.add(mnNewMenu);
		
		JMenuItem menuItem = new JMenuItem("打开");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.showOpenDialog(getContentPane());
				File file = fileChooser.getSelectedFile();
				String fileText = FileUtil.loadFile(file, FileUtil.simpleEncoding(file.getPath()));
				mainTextArea.setText(fileText);
				currentFilePath = file.getPath();
			}
		});
		mnNewMenu.add(menuItem);
		
		JMenuItem menuItem_2 = new JMenuItem("退出");
		mnNewMenu.add(menuItem_2);
		
		JMenu menu = new JMenu("编辑");
		menuBar.add(menu);
		
		JMenuItem menuItem_1 = new JMenuItem("删除");
		menu.add(menuItem_1);
		
		
		initialize();
	}
	
	private void saveFile(){
		File file = new File(currentFilePath);
		if(file == null || file.isDirectory()) return;
		FileUtil.write(file, mainTextArea.getText(), FileUtil.simpleEncoding(currentFilePath));
	}
	
	private void initialize() {
		setBounds(100, 100, 960, 599);
		//setResizable(false);
		setTitle("文本编辑器");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
