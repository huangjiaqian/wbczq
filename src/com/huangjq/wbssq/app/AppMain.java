package com.huangjq.wbssq.app;

import java.awt.EventQueue;

import com.huangjq.wbssq.view.MainFrame;

public class AppMain {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainFrame().setVisible(true);;
				//new HEditFrame();
			}
		});
	}


}
