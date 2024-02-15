package com.stevejavas.system;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * このプロジェクト用のパネルクラスを作成
 */
public class Panel extends JPanel {
	
	/**
	 * パネルの背景色
	 */
	Color color;
	
	/**
	 * パネルに適応するレイアウト
	 */
	LayoutManager layout;
	
	/**
	 * チェックボックス
	 */
	private JCheckBox jcb;
	
	/**
	 * チェックボックスの状態
	 */
	private boolean check = false;
	
	/**
	 * テキストフィールドを使用する際に使用するラベル
	 */
	private JLabel label;
	
	/**
	 * クリーム色
	 */
	public static final Color CREAM = new Color( 255, 255, 200 );
	
	/**
	 * パネルの背景色を白、レイアウトをFlowLayoutでパネルを作成
	 */
	public Panel() {
		setBackground( Color.WHITE );
		setLayout( new FlowLayout() );
	}
	
	/**
	 * 背景色を指定して、レイアウトをFlowLayoutでパネルを作成
	 * @param color 背景色
	 */
	public Panel( Color color ) {
		this.color = color;
		setBackground( color );
		setLayout( layout );
	}
	
	/**
	 * 背景色とレイアウトを指定してパネルを作成
	 * @param color 背景色
	 * @param layout レイアウト
	 */
	public Panel( Color color, LayoutManager layout ) {
		this.color = color;
		this.layout = layout;
		setBackground( color );
		setLayout( layout );
	}
	
	/**
	 * これがこのクラスの本題。
	 * コンポーネントの数に合わせてGridLayoutの分割数を指定して
	 * コンポーネントを追加している状態のパネルを作成。
	 * 背景色は汎用性を持たせるためよく使う「Color.WHITE」を使用。
	 * ・追加される順番はコンポーネントの配列に格納した順番になる。
	 * 「字幕、テキストエリア」の順番に配置したい場合は、
	 * [字幕変数, テキストエリア変数]の順番に配列を作成。
	 * @param components パネルに追加するコンポーネント達
	 */
	public Panel( Component[] components ) {
		
		setBackground( Color.WHITE );
		setLayout( new GridLayout( 1, components.length ) );
		for ( int i = 0; i < components.length; i++ ) {
			add( components[i] );
		}
	}
	
	/**
	 * FlowLayoutパネルを作成し、コンポーネント配列の数だけ追加する
	 * 追加される順番は配列の順番になる。
	 * int型でFlowLayoutの型を指定する
	 * 例：FlowLayout.LEFT
	 * int型の仮引数を追加している理由は上のコンストラクタと差別化するため。
	 * @param flowLayout FlowLayoutクラス内で定義されているLayoutの値を使用して指定
	 * @param components パネルに追加するコンポーネント達
	 */
	public Panel( int flowLayout, Component[] components ) {
		
		setBackground( new Color( 255, 255, 200 ) );
		setLayout( new FlowLayout( flowLayout ) );
		for ( int i = 0; i < components.length; i++ ) {
			add( components[i] );
		}
	}
	
	/**
	 * 指定のテキストフィールドと文字列がついた状態のパネルを取得
	 * @param jta テキストフィールド
	 * @param str 文字列
	 */
	public Panel( JTextField jta, String str ) {
		
		setBackground( CREAM );
		setLayout( new GridLayout( 1, 2 ) );
		
		JPanel jlp = new JPanel();
		jlp.setBackground( CREAM );
		jlp.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		label = new JLabel( str );
		label.setFont( new Font( null, 0, 15 ) );
		jlp.add( label );
		
		JPanel jtfp = new JPanel();
		jtfp.setBackground( CREAM );
		jtfp.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
        jtfp.add( jta );
        
        add( jlp );
        add( jtfp );
	}
	
	/**
	 * 指定のテキストフィールドと文字列がついた状態のパネルを比率を指定して取得
	 * @param jta テキストフィールド
	 * @param str 文字列
	 * @param ratio 文字列とテキストフィールドの比率 { 文字列, テキストフィールド }
	 */
	public Panel( JTextField jta, String str, double[] ratio ) {
		setLayout( new GridBagLayout() );
		setBackground( CREAM );
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		JPanel jlp = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		jlp.setBackground( CREAM );
		gbc.gridx = 0;
		gbc.weightx = ratio[0]; // JLabelの割合を指定
		JLabel label = new JLabel( str );
		label.setFont( new Font( null, Font.PLAIN, 15 ) );
		jlp.add( label );
		add( jlp, gbc );
		
		JPanel jtfp = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		jtfp.setBackground( CREAM );
		gbc.gridx = 1;
		gbc.weightx = ratio[1]; // JTextFieldの割合を指定
		jtfp.add( jta );
		add( jtfp, gbc );
	}
	
	/**
	 * チェックボックスがついているパネルを作成
	 * @param jcb チェックボックスの名前
	 * @param initCheck 初期のチェック状態
	 */
	public Panel( JCheckBox jcb, boolean initCheck ) {
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		setBackground( CREAM );
		setLayout( new GridBagLayout() );
		
		String text = jcb.getText();
		jcb.setText("");
		
		JPanel jlp = new JPanel();
		jlp.setBackground( CREAM );
		jlp.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		gbc.gridx = 0;
        gbc.weightx = 0.9;
		JLabel jl = new JLabel( text );
		jl.setFont( new Font( null, 0, 15 ) );
		jl.setForeground( Color.black );
		jlp.add( jl );
		add( jlp, gbc );
		
		JPanel jcbp = new JPanel();
		jcbp.setBackground( CREAM );
		jcbp.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
		gbc.gridx = 1;
        gbc.weightx = 0.1;
		jcb.setBackground( CREAM );
		jcb.setSelected( initCheck );
		jcb.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ( jcb.isSelected() == true ) {

					check = true;
				}else {
					check = false;
				}
			}
		});
		jcbp.add( jcb );
		add( jcbp, gbc );
	}
	
	/**
	 * パネル内にパネルの内容の説明用ラベル、コンポーネントを作成するパネル
	 * 背景色、グリッドレイアウトの比率、パネルのサイズ、パネルのボーダーのサイズを指定して作成できる
	 * グリッドレイアウトの比率、パネルのサイズ、パネルのボーダーのサイズ、コンポーネントは一時配列の形で入れる
	 * また、最初の行はパネルの説明用ラベルだけになるようにしてある。
	 * @param panelDescription パネル内の説明文
	 * @param textSize パネル内の説明文の文字のサイズ
	 * @param background このパネルの背景色
	 * @param gridLayout [ 行, 列 ] : GridLayoutの比率、コンポーネントの数、配置の仕方に応じて変える. とりあえず ( 行x列 > コンポーネントの総数 ) になればいい
	 * @param size [ 幅, 高さ ]
	 * @param borderSize [ top, left, bottom, right ]
	 * @param components 追加するコンポーネント
	 */
	public Panel( String panelDescription, int textSize, Color background, int[] gridLayout, int[] size, int[] borderSize, Component[] components) {
		
		setBackground( background );
		setLayout( new GridLayout( gridLayout[0] + 1, gridLayout[1] ) );
		setAlignmentX( Component.CENTER_ALIGNMENT );
		setBorder( BorderFactory.createMatteBorder( borderSize[0], borderSize[1], borderSize[2], borderSize[3], Color.gray ) );
		setMaximumSize( new Dimension( size[0], size[1] ) );
		
		JLabel desc = new Label( "<html>&emsp;" + panelDescription, textSize );
		
		this.add( desc );
		for ( int j = 0; j < gridLayout[1] - 1; j++ ) {	// 最初の行は文字列だけにしたいため背景と同じ色のパネルで埋める
			this.add( new Panel( background ) );
		}
		for( int i = 0; i < components.length; i++ ) {
			this.add( components[i] );
		}
	}
	
	/**
	 * チェックボックスを取得します
	 * @return checkbox
	 */
	public JCheckBox getCheckBox() {
		return jcb;
	}

	/**
	 * チェックボックスの状態を文字列型で返す
	 * @return 文字列 "True" か "False"
	 */
	public String getCheck() {
		if ( check == true ) {
			return "True";
		}
		return "False";
	}
	
	/**
	 * テキストフィールドに使用しているラベルクラスを取得します
	 * @return label
	 */
	public JLabel getLabel() {
		return label;
	}
}