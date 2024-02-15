package com.stevejavas.layout;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.stevejavas.system.Button;
import com.stevejavas.system.JavasSystem;
import com.stevejavas.system.Panel;

/**
 * コンフィグ画面切り替え用のボタンのレイアウト
 * 別クラスに移動する理由はソースコードが長くなるため
 */
public class ConfigButtonsLayout extends JPanel {
	
	/**
	 * 切り替え用のカードレイアウト
	 */
	private CardLayout clayout;
	
	/**
	 * 既にレイアウトされたパネル
	 * @param clayout カードレイアウト
	 * @param mainPanel 貼り付けるパネル( mainPanel )
	 */
	public ConfigButtonsLayout( CardLayout clayout, JPanel mainPanel ) {
		
		this.clayout = clayout;
		setBackground( Panel.CREAM );
		setLayout( new GridLayout( 1, 12 ) );
		
//		サーバー用の設定(主にゲーム内設定にない設定)
		JButton serverSettings = new Button( "サーバー", new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				clayout.show( mainPanel, "server" );				
			}
		});
		
//		ゲームルール
//		プレイヤー、恐竜、構造物、ワールド、ルール
		JButton playerSettings = new Button( "プレイヤー", new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				clayout.show( mainPanel, "player" );				
			}
		});
		playerSettings.setFont( new Font( null, Font.BOLD, 10 ) );
		
		JButton dinoSettings = new Button( "恐竜", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clayout.show( mainPanel, "dino" );				
			}
		});
		
		JButton structureSettings = new Button( "構造物", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clayout.show( mainPanel, "structure" );				
			}
		});
		
		JButton worldSettings = new Button( "ワールド", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clayout.show( mainPanel, "world" );				
			}
		});
		
		JButton ruleSettings = new Button( "ルール", new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				clayout.show( mainPanel, "rule" );				
			}
		});
		
		JavasSystem.addAll( this, new JButton[] { 
				serverSettings, 
				playerSettings, 
				dinoSettings, 
				structureSettings, 
				worldSettings, 
				ruleSettings 
		} );
		
//		詳細
//		PvE, PvP, テイム済みの恐竜, ワールド, 経験値倍率, その他
		JButton PvESettings = new Button( "PvE", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clayout.show( mainPanel, "pve" );				
			}
		});
		
		JButton PvPSettings = new Button( "PvP", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clayout.show( mainPanel, "pvp" );				
			}
		});
		
		JButton worldDetailedSettings = new Button( "<html>ワールド<br>　詳細</html>", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clayout.show( mainPanel, "worldDetailed" );				
			}
		});
		
		JButton dinoWildSettings = new Button( "<html>野生恐竜</html>", new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				clayout.show( mainPanel, "dinoWild" );
			}
		});
		dinoWildSettings.setFont( new Font( null, Font.BOLD, 11 ) );
		
		JButton tamedDinoSettings = new Button( "<html>テイム済み<br>　の恐竜</html>", new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				clayout.show( mainPanel, "tamedDino" );	
			}
		});
		tamedDinoSettings.setFont( new Font( null, Font.BOLD, 10 ) );
		
		JButton playerDetailedSettings = new Button( "<html>プレイヤー<br>　(詳細)</html>", new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				clayout.show( mainPanel, "playerDetailed" );
			}
		});
		playerDetailedSettings.setFont( new Font( null, Font.BOLD, 10 ) );
		
		JButton experienceSettings = new Button( "経験値", new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				clayout.show( mainPanel, "experience" );				
			}
		});
		
		JButton otherSettings = new Button( "その他", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clayout.show( mainPanel, "other" );
			}
		});
		JavasSystem.addAll( this, new JButton[] { 
				PvESettings, 
				PvPSettings, 
				worldDetailedSettings,
				dinoWildSettings,
				tamedDinoSettings,
				playerDetailedSettings,
				experienceSettings, 
				otherSettings 
		} );
	}
	
	/**
	 * このクラスで使用しているレイアウトマネージャーを返す
	 * @return CardLayout
	 */
	public CardLayout getLayoutManager() {
		return this.clayout;
	}
}