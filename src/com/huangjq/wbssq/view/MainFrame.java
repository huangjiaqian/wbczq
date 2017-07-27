package com.huangjq.wbssq.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.huangjq.wbssq.commons.StringUtils;
import com.huangjq.wbssq.services.SearchService;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = -7927739240031577489L;
	private JTextField dirField;
	private JTextField valueField;
	private JScrollPane scrollPane;
	private JTextArea resultTextArea;
	private JLabel statusLabel;
	private JTextArea resultView = new JTextArea();
	
	private SearchService searchService = new SearchService();
	
	public MainFrame() {
		setBackground(Color.WHITE);
		initialize();
		//setVisible(true);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 967, 717);
		setResizable(false);
		setTitle("文本查找器");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("选择目录：");
		lblNewLabel.setBounds(68, 74, 85, 18);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("搜索内容：");
		lblNewLabel_1.setBounds(68, 117, 85, 18);
		panel.add(lblNewLabel_1);
		
		dirField = new JTextField();
		dirField.setBounds(167, 71, 536, 24);
		panel.add(dirField);
		dirField.setColumns(10);
		
		valueField = new JTextField();
		valueField.setBounds(167, 114, 536, 24);
		panel.add(valueField);
		valueField.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(28, 183, 884, 332);
		panel.add(scrollPane);
		
		resultTextArea = new JTextArea();
		resultTextArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
		scrollPane.setViewportView(resultTextArea);
		
		statusLabel = new JLabel("");
		statusLabel.setBounds(28, 528, 884, 18);
		panel.add(statusLabel);
		
		
		JButton dirButton = new JButton("目录");
		dirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = null;
				if(!StringUtils.isBlank(dirField.getText())){
					fileChooser = new JFileChooser(dirField.getText());
				}else{
					fileChooser = new JFileChooser();
				}
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.showOpenDialog(panel);
				File file = fileChooser.getSelectedFile();
				if(file != null && !StringUtils.isBlank(file.getPath())){
					dirField.setText(file.getPath());					
				}
			}
		});
		dirButton.setBounds(717, 70, 113, 27);
		panel.add(dirButton);
		
		
		
		searchService.setResultArea(resultTextArea);					
		searchService.setStatusLabel(statusLabel);
		
		JButton searchButton = new JButton("搜索");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dirValue = dirField.getText();
				String value = valueField.getText();
				if(StringUtils.isBlank(dirValue) || StringUtils.isBlank(value)) return;
				searchService.setSearchNr(value);
				searchService.setResultMap(new HashMap<String, Object>());
				new Thread(new Runnable() {
					@Override
					public void run() {
						searchService.setResultCount(0);
						resultTextArea.setText("");
						long start = System.currentTimeMillis();
						searchService.searchSubFiles(new File(dirValue));
						long end = System.currentTimeMillis();
						statusLabel.setText("搜索结束！ 耗时"+(double)(end-start)/1000+"s  结果数："+searchService.getResultCount()+"条");
						Map<String, Object> resultMap = searchService.getResultMap();
						StringBuffer codeLineText = new StringBuffer("代码总行数：");
						StringBuffer codeFileText = new StringBuffer("文件个数：");
						int i = 1;
						int j = 1;
						for (String key : resultMap.keySet()) {
							if(key.indexOf("fileNumber")!=-1){
								if(j%8==0){
									codeFileText.append("\n    ");
								}
								j++;
								codeFileText.append(key.substring(10,key.length())).append(":").append(resultMap.get(key)).append("个;  ");
								
							}else{
								if(i%7==0){
									codeLineText.append("\n    ");
								}
								i++;
								codeLineText.append(key).append(":").append(resultMap.get(key)).append("行;  ");								
							}
						}
						resultView.setText(codeLineText.toString()+"\n"+codeFileText);
					}
				}).start();
			}
		});
		searchButton.setBounds(717, 113, 113, 27);
		panel.add(searchButton);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 131, 26);
		panel.add(menuBar);
		
		JMenu menu = new JMenu("工具");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("文本编辑器");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new HEditFrame().setVisible(true);
			}
		});
		menu.add(menuItem);
		
		JLabel label = new JLabel("统计结果：");
		label.setBounds(28, 559, 85, 18);
		panel.add(label);
		
		
		resultView.setEditable(false);
		resultView.setBounds(106, 559, 806, 94);
		Font resultViewFont = resultView.getFont();
		resultView.setFont(new Font(resultViewFont.getName(), resultViewFont.getStyle(), resultViewFont.getSize()+3));
		panel.add(resultView);
		
		
		
	}
}
