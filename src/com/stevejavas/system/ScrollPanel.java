package com.stevejavas.system;

import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * スクロール機能付きパネルを作成するクラス
 * 主にコンフィグを配置するところで使用している
 */
public class ScrollPanel extends JPanel {
	
	/**
	 * パネルに使用しているスクロールペイン
	 */
	JScrollPane sp;
	
	/**
	 * スクロールペインを適用させたJPanelを作成する
	 */
	public ScrollPanel() {
		
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
		setBackground( new Color( 255, 255, 200 ) );
		sp = new JScrollPane( this );
		sp.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	}
	
	/**
	 * 背景色を指定してスクロールペインを適用させたJPanelを作成する
	 * @param color 背景色
	 */
	public ScrollPanel( Color color ) {
		
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
		setBackground( color );
		sp = new JScrollPane( this );
		sp.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	}
	
	/**
	 * 背景色は白でレイアウトを指定してスクロールするパネルを作成
	 * @param layout 適用するレイアウト
	 */
	public ScrollPanel( LayoutManager layout ) {
		
		setLayout( layout );
		setBackground( new Color( 255, 255, 200 ) );
		sp = new JScrollPane( this );
		sp.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	}
	
	/**
	 * 背景色とレイアウトを指定してスクロールするパネルを作成
	 * @param color 背景色
	 * @param layout 適用するレイアウト
	 */
	public ScrollPanel( Color color, LayoutManager layout ) {
		
		setLayout( layout );
		setBackground( color );
		sp = new JScrollPane( this );
		sp.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	}
	
	/**
	 * パネルに適用しているスクロールペインを返す
	 * @return JScrollPane
	 */
	public JScrollPane getScrollPane() {
		return sp;
	}
}