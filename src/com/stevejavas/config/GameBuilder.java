package com.stevejavas.config;

import java.io.File;
import java.io.IOException;

import com.stevejavas.system.JavasFileSystem;
import com.stevejavas.system.JavasSystem;

/**
 * 
 */
public class GameBuilder extends ConfigText {
	
	/**
	 * Game.ini
	 */
	File GUS;
	
	/**
	 * JavasFileSystemでGame.iniを開く
	 */
	JavasFileSystem jfs;
	
	/**
	 * Game.iniの項目名と値を二次元配列でまとめたもの
	 */
	String[] all;
	
	/**
	 * [/Script/ShooterGame.ShooterGameMode]
	 */
	private String[][] shooterGameModeSettings;
	
	/**
	 * [ShooterGameMode_TEMPOverrides]
	 */
	private String[][] tmpOverrideSettings;
	
	/**
	 * テスト用でGameBuilderを開く
	 * デバッグ用
	 */
	public GameBuilder() {
		
		GUS = new File( JavasSystem.CURRENT_DIRECTORY + "\\example\\Game.ini" );
		jfs = new JavasFileSystem( GUS );
		all = jfs.getAllText().split( "\n" );
		initAll();
	}

	/**
	 * プロファイルを指定してGameBuilderを開く
	 * @param profileName プロファイル名
	 */
	public GameBuilder( String profileName ) {
		
		GUS = new File( JavasSystem.CURRENT_DIRECTORY + "\\servers\\" + profileName + "\\ShooterGame\\Saved\\Config\\WindowsServer\\Game.ini" );
		jfs = new JavasFileSystem( GUS );
		all = jfs.getAllText().split( "\n" );
		initAll();
	}
	
	/**
	 * 変更内容をコンフィグに書き込む
	 * プロファイルを保存を押したら呼び出されるようにする
	 */
	public void applySettings() {
		
		String sum = "";
		sum += "[/Script/ShooterGame.ShooterGameMode]\n";
		for ( int i = 0; i < shooterGameModeSettings.length; i++ ) {
			sum += shooterGameModeSettings[i][0] + "=" + shooterGameModeSettings[i][1] + "\n";
		}
		sum += "\n[ShooterGameMode_TEMPOverrides]\n";
		for ( int i = 0; i < tmpOverrideSettings.length; i++ ) {
			sum += tmpOverrideSettings[i][0] + "=" + tmpOverrideSettings[i][1] + "\n";
		}
		try {
			jfs.resetFile();
			jfs.write( 1, sum );
		} catch ( IOException e ) { e.printStackTrace(); }
	}
	
	/**
	 * shooterGameModeSettingsとかの値を格納する二次元配列型変数に値をファイルから読み取って格納する
	 */
	void initAll() {
		
		shooterGameModeSettings = new String[getShooterGameModeSettingsRowNum()][2];
		tmpOverrideSettings = new String[getTMPOverrideSettingsRowNum()][2];
		
		initShooterGameModeSettings();
		initTMPOverrideSettings();
	}
	
	/**
	 * GameUserSettings.iniから[shooterGameModeSettings]の部分だけ抽出する
	 */
	void initShooterGameModeSettings() {
		
		for ( int j = 0; j < all.length; j++ ) {
			
			if ( all[j].equals( "[/Script/ShooterGame.ShooterGameMode]" ) ) {
				
				for ( int k = 0, i = j + 1;;k++, i++ ) {
					try {
						if ( all[i].equals("") || all[i] == null ) {	// 空白行を検知したら終了
							break;
						}
						
						if ( all[i].split("=").length != 1 ) {
							shooterGameModeSettings[k] = all[i].split("=");
						}else if ( all[i].split("=").length == 1 ) {
							shooterGameModeSettings[k][0] = all[i].split("=")[0];
							shooterGameModeSettings[k][1] = "";
						}
					}catch ( ArrayIndexOutOfBoundsException err ) {
						break;
					}
				}
			}
		}
	}
	
	/**
	 * GameUserSettings.iniから[tmpOverrideSettings]の部分だけ抽出する
	 */
	void initTMPOverrideSettings() {
		
		for ( int j = 0; j < all.length; j++ ) {
			
			if ( all[j].equals( "[ShooterGameMode_TEMPOverrides]" ) ) {
				
				for ( int k = 0, i = j + 1;;k++, i++ ) {
					try {
						if ( all[i].equals("") || all[i] == null ) {	// 空白行を検知したら終了
							break;
						}
						
						if ( all[i].split("=").length != 1 ) {
							tmpOverrideSettings[k] = all[i].split("=");
						}else if ( all[i].split("=").length == 1 ) {
							tmpOverrideSettings[k][0] = all[i].split("=")[0];
							tmpOverrideSettings[k][1] = "";
						}
					}catch ( ArrayIndexOutOfBoundsException err ) {
						break;
					}
				}
			}
		}
	}
	
	/**
	 * [shooterGameModeSettings]の行数を取得
	 * @return [shooterGameModeSettings]の行数
	 */
	public int getShooterGameModeSettingsRowNum() {
		
		int row = 0;
		for ( int j = 0; j < all.length; j++ ) {
			
			if ( all[j].equals( "[/Script/ShooterGame.ShooterGameMode]" ) ) {
				
				for ( int i = j + 1;;i++ ) {
					try {
						if ( all[i].equals("") || all[i] == null ) {	// 空白行を検知したら終了
							break;
						}
						row++;
					}catch ( ArrayIndexOutOfBoundsException err ) {
						break;
					}
				}
				break;
			}
		}
		return row;
	}

	/**
	 * [tmpOverrideSettings]の行数を取得
	 * @return [tmpOverrideSettings]の行数
	 */
	public int getTMPOverrideSettingsRowNum() {
		
		int row = 0;
		for ( int j = 0; j < all.length; j++ ) {
			
			if ( all[j].equals( "[ShooterGameMode_TEMPOverrides]" ) ) {
				
				for ( int i = j + 1;;i++ ) {
					try {
						if ( all[i].equals("") || all[i] == null ) {	// 空白行を検知したら終了
							break;
						}
						row++;
					}catch ( ArrayIndexOutOfBoundsException err ) {
						break;
					}
				}
				break;
			}
		}
		return row;
	}
	
	/**
	 * [shooterGameModeSettings]の項目から指定した名前の項目
	 * @param settingName ConfigTextクラスからコンフィグ名を取得する
	 * @return 指定したコンフィグの値
	 */
	public String getShooterGameModeSettingsValue( String settingName ) {
		
		for ( int i = 0; i < shooterGameModeSettings.length; i++ ) {
			
			if ( shooterGameModeSettings[i][0].equals( settingName ) ) {
				return shooterGameModeSettings[i][1];
			}
		}
		return "";
	}
	
	/**
	 * [tmpOverrideSettings]の項目から指定した名前の項目
	 * @param settingName ConfigTextクラスからコンフィグ名を取得する
	 * @return 指定したコンフィグの値
	 */
	public String getTMPOverrideSettingsValue( String settingName ) {
		
		for ( int i = 0; i < tmpOverrideSettings.length; i++ ) {
			
			if ( tmpOverrideSettings[i][0].equals( settingName) ) {
				return tmpOverrideSettings[i][1];
			}
		}
		return "";
	}
	
	/**
	 * [shooterGameModeSettings]の項目から指定した名前の項目
	 * @param settingName ConfigTextクラスからコンフィグ名を取得する
	 * @param value 書き換える値
	 */
	public void setShooterGameModeSettingsValue( String settingName, String value ) {
		
		for ( int i = 0; i < shooterGameModeSettings.length; i++ ) {
			
			if ( shooterGameModeSettings[i][0].equals( settingName) ) {
				shooterGameModeSettings[i][1] = value;
			}
		}
	}
	
	/**
	 * [tmpOverrideSettings]の項目から指定した名前の項目
	 * @param settingName ConfigTextクラスからコンフィグ名を取得する
	 * @param value 書き換える値
	 */
	public void setTMPOverrideSettingsValue( String settingName, String value ) {
		
		for ( int i = 0; i < tmpOverrideSettings.length; i++ ) {
			
			if ( tmpOverrideSettings[i][0].equals( settingName ) ) {
				tmpOverrideSettings[i][1] = value;
			}
		}
	}
}