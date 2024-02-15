package com.stevejavas.main;

import com.stevejavas.window.SMWindow;

/**
 * プログラムメモ
 */
public class ARKSASM {
	
	/**
	 * サーバーマネージャーウィンドウ
	 */
	public static final SMWindow mainWindow = new SMWindow();
	
	/**
	 * メインメソッド
	 * @param args コマンドライン入力
	 */
	public static void main( String[] args ) {
		
		mainWindow.start();
	}
}