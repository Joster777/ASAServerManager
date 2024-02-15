package com.stevejavas.system;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * ボタンを作成する独自のクラス
 */
public class Button extends JButton {

	/**
	 * ボタンを作成
	 * @param text ボタンの文字列
	 * @param l 呼び出した時にボタンを押した時の処理を記述する
	 */
	public Button( String text, ActionListener l ) {
		setText( text );
		setBackground( new Color( 212, 255, 255 ) );
		addActionListener( l );
	}
}