package com.stevejavas.config;

import java.io.File;
import java.io.IOException;

import com.stevejavas.system.JavasFileSystem;
import com.stevejavas.system.JavasSystem;

/**
 * modify*()メソッドは仮変数にテキストフィールドから取得した値を入れて変更する
 * ASE サーバマネージャー コンフィグ
 * "Administration", "Rules", "Chat and Notifications", "HUD and Visuals" = ワールド設定
 * "playerSettings"
 */
public class GameUserSettingBuilder extends ConfigText {
	
	/**
	 * GameUserSettings.ini
	 */
	File GUS;
	
	/**
	 * JavasFileSystemでGameUserSettings.iniを開く
	 */
	JavasFileSystem jfs;
	
	/**
	 * GameUserSettings.iniの項目名と値を二次元配列でまとめたもの
	 */
	String[] all;
	
	/**
	 * [ServerSettings]
	 */
	private String[][] serverSettings;
	
	/**
	 * [SessionSettings]
	 */
	private String[][] sessionSettings;
	
	/**
	 * [/Script/Engine.GameSession]
	 */
	private String[][] engineGameSession;
	
	/**
	 * [MessageOfTheDay]
	 */
	private String[][] messageOfTheDay;
	
	/**
	 * テスト用でGameUserSettingsBuilderを開く
	 * デバッグ用
	 */
	public GameUserSettingBuilder() {
		
		GUS = new File( JavasSystem.CURRENT_DIRECTORY + "\\example\\GameUserSettings.ini" );
		jfs = new JavasFileSystem( GUS );
		all = jfs.getAllText().split( "\n" );
		initAll();
	}

	/**
	 * プロファイルを指定してGameUserSettingsBuilderを開く
	 * @param profileName プロファイル名
	 */
	public GameUserSettingBuilder( String profileName ) {
		
		GUS = new File( JavasSystem.CURRENT_DIRECTORY + "\\servers\\" + profileName + "\\ShooterGame\\Saved\\Config\\WindowsServer\\GameUserSettings.ini" );
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
		sum += "[ServerSettings]\n";
		for ( int i = 0; i < serverSettings.length; i++ ) {
			sum += serverSettings[i][0] + "=" + serverSettings[i][1] + "\n";
		}
		sum += "\n[SessionSettings]\n";
		for ( int i = 0; i < sessionSettings.length; i++ ) {
			sum += sessionSettings[i][0] + "=" + sessionSettings[i][1] + "\n";
		}
		sum += "\n[/Script/Engine.GameSession]\n";
		for ( int i = 0; i < engineGameSession.length; i++ ) {
			sum += engineGameSession[i][0] + "=" + engineGameSession[i][1] + "\n";
		}
		sum += "\n[MessageOfTheDay]\n";
		for ( int i = 0; i < messageOfTheDay.length; i++ ) {
			sum += messageOfTheDay[i][0] + "=" + messageOfTheDay[i][1] + "\n";
		}
		try {
			jfs.resetFile();
			jfs.write( 1, sum );
		} catch ( IOException e ) { e.printStackTrace(); }
	}
	
	/**
	 * ServerSettingsとかの値を格納する二次元配列型変数に値をファイルから読み取って格納する
	 */
	void initAll() {
		
		serverSettings = new String[getServerSettingsRowNum()][2];
		sessionSettings = new String[getSesionSettingsRowNum()][2];
		engineGameSession = new String[getEngineGameSessionRowNum()][2];
		messageOfTheDay = new String[getMessageOfTheDayRowNum()][2];
		
		initServerSettings();
		initSessionSettings();
		initEngineGameSession();
		initMessageOfTheDay();
	}
	
	/**
	 * GameUserSettings.iniから[ServerSettings]の部分だけ抽出する
	 */
	void initServerSettings() {
		
		for ( int j = 0; j < all.length; j++ ) {
			
			if ( all[j].equals( "[ServerSettings]" ) ) {
				
				for ( int k = 0, i = j + 1;;k++, i++ ) {
					try {
						if ( all[i].equals("") || all[i] == null ) {	// 空白行を検知したら終了
							break;
						}
						
						if ( all[i].split("=").length != 1 ) {
							serverSettings[k] = all[i].split("=");
						}else if ( all[i].split("=").length == 1 ) {
							serverSettings[k][0] = all[i].split("=")[0];
							serverSettings[k][1] = "";
						}
					}catch ( ArrayIndexOutOfBoundsException err ) {
						break;
					}
				}
			}
		}
	}
	
	/**
	 * GameUserSettings.iniから[SessionSettings]の部分だけ抽出する
	 */
	void initSessionSettings() {
		
		for ( int j = 0; j < all.length; j++ ) {
			
			if ( all[j].equals( "[SessionSettings]" ) ) {
				
				for ( int k = 0, i = j + 1;;k++, i++ ) {
					try {
						if ( all[i].equals("") || all[i] == null ) {	// 空白行を検知したら終了
							break;
						}
						
						if ( all[i].split("=").length != 1 ) {
							sessionSettings[k] = all[i].split("=");
						}else if ( all[i].split("=").length == 1 ) {
							sessionSettings[k][0] = all[i].split("=")[0];
							sessionSettings[k][1] = "";
						}
					}catch ( ArrayIndexOutOfBoundsException err ) {
						break;
					}
				}
			}
		}
	}
	
	/**
	 * GameUserSettings.iniから[/Script/Engine.GameSession]の部分だけ抽出する
	 */
	void initEngineGameSession() {
		
		for ( int j = 0; j < all.length; j++ ) {
			
			if ( all[j].equals( "[/Script/Engine.GameSession]" ) ) {
				
				for ( int k = 0, i = j + 1;;k++, i++ ) {
					try {
						if ( all[i].equals("") || all[i] == null ) {	// 空白行を検知したら終了
							break;
						}
						
						if ( all[i].split("=").length != 1 ) {
							engineGameSession[k] = all[i].split("=");
						}else if ( all[i].split("=").length == 1 ) {
							engineGameSession[k][0] = all[i].split("=")[0];
							engineGameSession[k][1] = "";
						}
					}catch ( ArrayIndexOutOfBoundsException err ) {
						break;
					}
				}
			}
		}
	}
	
	/**
	 * GameUserSettings.iniから[MessageOfTheDay]の部分だけ抽出する
	 */
	void initMessageOfTheDay() {
		
		for ( int j = 0; j < all.length; j++ ) {
			
			if ( all[j].equals( "[MessageOfTheDay]" ) ) {
				
				for ( int k = 0, i = j + 1;;k++, i++ ) {
					try {
						if ( all[i].equals("") || all[i] == null ) {	// 空白行を検知したら終了
							break;
						}
						
						if ( all[i].split("=").length != 1 ) {
							messageOfTheDay[k] = all[i].split("=");
						}else if ( all[i].split("=").length == 1 ) {
							messageOfTheDay[k][0] = all[i].split("=")[0];
							messageOfTheDay[k][1] = "";
						}
					}catch ( ArrayIndexOutOfBoundsException err ) {
						break;
					}
				}
			}
		}
	}
	
	/**
	 * [ServerSettings]の行数を取得
	 * @return [ServerSettings]の行数
	 */
	public int getServerSettingsRowNum() {
		
		int row = 0;
		for ( int j = 0; j < all.length; j++ ) {
			
			if ( all[j].equals( "[ServerSettings]" ) ) {
				
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
	 * [SessionSettings]の行数を取得
	 * @return [SessionSettings]の行数
	 */
	public int getSesionSettingsRowNum() {
		
		int row = 0;
		for ( int j = 0; j < all.length; j++ ) {
			
			if ( all[j].equals( "[SessionSettings]" ) ) {
				
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
	 * [/Script/Engine.GameSession]の行数を取得
	 * @return [/Script/Engine.GameSession]の行数
	 */
	public int getEngineGameSessionRowNum() {
		
		int row = 0;
		for ( int j = 0; j < all.length; j++ ) {
			
			if ( all[j].equals( "[/Script/Engine.GameSession]" ) ) {
				
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
	 * [MessageOfTheDay]の行数を取得
	 * @return [MessageOfTheDay]の行数
	 */
	public int getMessageOfTheDayRowNum() {
		
		int row = 0;
		for ( int j = 0; j < all.length; j++ ) {
			
			if ( all[j].equals( "[MessageOfTheDay]" ) ) {
				
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
	 * [ServerSettings]の項目から指定した名前の項目の値を取得する
	 * @param settingName ConfigTextクラスからコンフィグ名を取得する
	 * @return 指定したコンフィグの値
	 */
	public String getServerSettingsValue( String settingName ) {
		
		for ( int i = 0; i < serverSettings.length; i++ ) {
			
			if ( serverSettings[i][0].equals( settingName ) ) {
				return serverSettings[i][1];
			}
		}
		return "";
	}
	
	/**
	 * [SessionSettings]の項目から指定した名前の項目の値を取得する
	 * @param settingName ConfigTextクラスからコンフィグ名を取得する
	 * @return 指定したコンフィグの値
	 */
	public String getSessionSettingsValue( String settingName ) {
		
		for ( int i = 0; i < sessionSettings.length; i++ ) {
			
			if ( sessionSettings[i][0].equals( settingName) ) {
				return sessionSettings[i][1];
			}
		}
		return "";
	}
	
	/**
	 * [EngineGameSession]の項目から指定した名前の項目の値を取得する
	 * @param settingName ConfigTextクラスからコンフィグ名を取得する
	 * @return 指定したコンフィグの値
	 */
	public String getEngineGameSessionValue( String settingName ) {
		
		for ( int i = 0; i < engineGameSession.length; i++ ) {
			
			if ( engineGameSession[i][0].equals( settingName) ) {
				return engineGameSession[i][1];
			}
		}
		return "";
	}
	
	/**
	 * [MessageOfTheDay]の項目から指定した名前の項目の値を取得する
	 * @param settingName ConfigTextクラスからコンフィグ名を取得する
	 * @return 指定したコンフィグの値
	 */
	public String getMessageOfTheDayValue( String settingName ) {
		
		for ( int i = 0; i < messageOfTheDay.length; i++ ) {
			
			if ( messageOfTheDay[i][0].equals( settingName) ) {
				return messageOfTheDay[i][1];
			}
		}
		return "";
	}
	
	/**
	 * [ServerSettings]の項目から指定した名前の項目の値を書き換える
	 * @param settingName ConfigTextクラスからコンフィグ名を取得する
	 * @param value 書き換える値
	 */
	public void setServerSettingsValue( String settingName, String value ) {
		
		for ( int i = 0; i < serverSettings.length; i++ ) {
			
			if ( serverSettings[i][0].equals( settingName) ) {
				serverSettings[i][1] = value;
			}
		}
	}
	
	/**
	 * [SessionSettings]の項目から指定した名前の項目の値を書き換える
	 * @param settingName ConfigTextクラスからコンフィグ名を取得する
	 * @param value 書き換える値
	 */
	public void setSessionSettingsValue( String settingName, String value ) {
		
		for ( int i = 0; i < sessionSettings.length; i++ ) {
			
			if ( sessionSettings[i][0].equals( settingName ) ) {
				sessionSettings[i][1] = value;
			}
		}
	}
	
	/**
	 * [EngineGameSession]の項目から指定した名前の項目の値を書き換える
	 * @param settingName ConfigTextクラスからコンフィグ名を取得する
	 * @param value 書き換える値
	 */
	public void setEngineGameSessionValue( String settingName, String value ) {
		
		for ( int i = 0; i < engineGameSession.length; i++ ) {
			
			if ( engineGameSession[i][0].equals( settingName ) ) {
				engineGameSession[i][1] = value;
			}
		}
	}
	
	/**
	 * [MessageOfTheDay]の項目から指定した名前の項目の値を書き換える
	 * @param settingName ConfigTextクラスからコンフィグ名を取得する
	 * @param value 書き換える値
	 */
	public void setMessageOfTheDayValue( String settingName, String value ) {
		
		for ( int i = 0; i < messageOfTheDay.length; i++ ) {
			
			if ( messageOfTheDay[i][0].equals( settingName ) ) {
				messageOfTheDay[i][1] = value;
			}
		}
	}
}