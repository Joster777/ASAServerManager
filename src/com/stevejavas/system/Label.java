package com.stevejavas.system;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

/**
 * このプログラム用のラベルを作成するクラス
 */
public class Label extends JLabel {
	
	/**
	 * ラベルの内容
	 */
	String text = "";
	
	/**
	 * ラベルのサイズ
	 */
	int size = 12;
	
	/**
	 * ラベルの色
	 */
	Color color = Color.black;
	
	/**
	 * ラベルのフォント
	 */
	Font font = new Font( null, Font.BOLD, size );
	
	/**
	 * テキストを指定してラベルを作成
	 * @param text ラベルのテキスト
	 */
	public Label( String text ) {
		this.text = text;
		init();
	}
	
	/**
	 * 文字列と色を指定してラベルを作成
	 * @param text ラベルのテキスト
	 * @param color ラベルの色
	 */
	public Label( String text, Color color ) {
		this.text = text;
		this.color = color;
		init();
	}
	
	/**
	 * テキストとサイズを指定してラベルを作成
	 * @param text ラベルのテキスト
	 * @param size ラベルのサイズ
	 */
	public Label( String text, int size ) {
		this.text = text;
		this.size = size;
		this.font = new Font( null, Font.BOLD, size );
		init();
	}
	
	/**
	 * テキストとフォントを指定してラベルを作成
	 * @param text ラベルのテキスト
	 * @param font ラベルのフォント
	 */
	public Label( String text, Font font ) {
		this.text = text;
		this.font = font;
		init();
	}
	
	/**
	 * 文字と色とフォントを指定してラベルを作成
	 * @param text ラベルのテキスト
	 * @param color ラベルの色
	 * @param font ラベルのフォント
	 */
	public Label( String text, Color color, Font font ) {
		this.text = text;
		this.color = color;
		this.font = font;
		init();
	}
	
	/**
	 * 文字と色とフォントを指定してラベルを作成
	 * @param text ラベルのテキスト
	 * @param font ラベルのフォント
	 * @param color ラベルの色
	 */
	public Label( String text, Font font, Color color ) {
		this.text = text;
		this.color = color;
		this.font = font;
		init();
	}
	
	/**
	 * 文字列と色とサイズを指定してラベルを作成
	 * @param text ラベルのテキスト
	 * @param color ラベルの色
	 * @param size ラベルのサイズ
	 */
	public Label( String text, Color color, int size ) {
		this.text = text;
		this.color = color;
		this.size = size;
		this.font = new Font( null, Font.BOLD, size );
		init();
	}
	
	/**
	 * 初期化
	 */
	void init() {
		setForeground( color );
		setFont( font );
		setText( text );
	}
}