package com.stevejavas.system;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * このプログラム用のテキストフィールドを作成するクラス
 */
public class TextField extends JTextField {
	
	/**
	 * ボーダー
	 */
	Border border;
	
	/**
	 * ボーダーの色
	 */
	Color borderColor = Color.gray;
	
	/**
	 * ボーダーのサイズ
	 */
	int borderSize = 2;
	
	/**
	 * ボーダーをつけるかどうか
	 */
	boolean isBorder = false;
	
	/**
	 * デフォルトコンストラクタ
	 */
	public TextField() { }
	
	/**
	 * ボーダーを付けるか指定してテキストフィールドを作成
	 * @param isBorder trueでボーダーあり、falseでなし
	 */
	public TextField( boolean isBorder ) {
		this.isBorder = isBorder;
		if ( this.isBorder == true ) {
			border = new LineBorder( borderColor, borderSize );
		}
	}
	
	/**
	 * ボーダーの有無とテキストフィールドのサイズを指定してテキストフィールドを作成
	 * @param isBorder trueでボーダーあり、falseでなし
	 * @param size	テキストフィールドのサイズ
	 */
	public TextField( boolean isBorder, Dimension size ) {
		this.isBorder = isBorder;
		if ( this.isBorder == true ) {
			border = new LineBorder( borderColor, borderSize );
		}
		setPreferredSize( size );
	}
}