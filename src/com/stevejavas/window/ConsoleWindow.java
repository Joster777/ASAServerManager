package com.stevejavas.window;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.stevejavas.system.JavasSystem;

/**
 * ログを表示するための独自のコンソールウィンドウを作成
 * 本当はcmd.exeを起動してそこにログを出力したかったができなかったため
 * このような手段をとった
 */
public class ConsoleWindow extends JFrame implements Runnable {

	/**
	 * ログ表示用のコンソールウィンドウ用のスレッド
	 */
	Thread th;
	
	/**
	 * ログウィンドウに表示されているログ内容
	 */
	private String text = "<html>Microsoft Windows [Version 10.0.19045.3930]\n<br>(c) Microsoft Corporation. All rights reserved.<br><br>";
	
	/**
	 * ログを表示するためのラベルクラス
	 */
	JLabel label;
	
	/**
	 * コンソールウィンドウを呼び出す
	 */
	public ConsoleWindow() {
		
		setTitle( "ARK:Log Ascended" );
		setBounds( 300, 100, 900, 600 );
		setIconImage( new ImageIcon( JavasSystem.CURRENT_DIRECTORY + "\\images\\giganoto.png" ).getImage() );

		JPanel base = new JPanel();
		base.setBackground( Color.black );
		base.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		
		label = new JLabel( text );
		label.setFont( new Font( Font.DIALOG, 0, 16 ) );
		label.setForeground( Color.white );
		base.add( label );
		
		JScrollPane jsp = new JScrollPane( base );
		getContentPane().add( jsp );
		
		setVisible( true );
	}
	
	/**
	 * コンソールウィンドウのスレッド開始
	 */
	public void start() {
		if ( th == null ) {
			th = new Thread( this );
			th.start();
		}
	}
	
	/**
	 * コンソールウィンドウのスレッド停止
	 */
	public void stop() {
		if ( th != null ) {
			th = null;
		}
	}
	
	@Override
	public void run() {
		
		while ( th != null ) {
			label.setText( text );
			repaint();
		}
	}

	/**
	 * ログを出力
	 * @param text 出力するログ内容
	 */
	public void print( String text ) {
		this.text += ">" + text + "<br>";
	}
}