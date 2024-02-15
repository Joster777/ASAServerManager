package com.stevejavas.console;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.stevejavas.system.JavasSystem;

/**
 * コンソール処理を行うクラス
 * 主にサーバーデータのインストールと起動に用いる
 */
public class ConsoleProcess {
	
	/**
	 * 指定のプロファイルのパス
	 */
	static final String ASM_DIR = JavasSystem.CURRENT_DIRECTORY + "\\servers\\";
	
	/**
	 * batファイルを格納してあるフォルダのパス
	 */
	static final String BAT_DIR = JavasSystem.CURRENT_DIRECTORY + "\\bat\\";
	
	/**
	 * コンソールを開くためのProcessクラス
	 */
	private static Process p;
	
	/**
	 * 指定のコマンドを実行
	 * ファイルURL/ファイル名でファイルを実行することも可能
	 * @param command コマンド名
	 */
    public static void executeCommand( String command ) {
        try {
        	ProcessBuilder pb = new ProcessBuilder( "cmd", "/c", "start " + command );
            pb.redirectErrorStream( true );
            p = pb.start();
            int exitCode = p.waitFor();
            System.out.println("プロセスがコード " + exitCode + " で終了しました。");
        } catch (IOException | InterruptedException ex) { ex.printStackTrace(); }
    }
    
	/**
	 * プロファイルを作成しサーバーをインストールする
	 * 指定の名前でサーバーデータを格納するためのフォルダを作成し
	 * その名前でサーバーデータをインストールするBatファイルを起動
	 * サーバー名とはまた別
	 * @param profileName プロファイル名
	 */
	public static void createProfile( String profileName ) {
		
		boolean file_already_exists = false;
		File[] files = new File( ASM_DIR ).listFiles();
		for( int f = 0; f < files.length; f++ ) {
			if ( profileName.equals( files[f].getName() ) ) {
				file_already_exists = true;
			}
		}
		if ( file_already_exists == false ) {
			try {
				Files.createDirectories( Paths.get( JavasSystem.CURRENT_DIRECTORY + "\\servers\\" + profileName + "\\ShooterGame\\Saved\\Config\\WindowsServer" ) );
				Files.copy( 
						Paths.get( JavasSystem.CURRENT_DIRECTORY + "\\source\\GameUserSettings.ini" ), 
						Paths.get( JavasSystem.CURRENT_DIRECTORY + "\\servers\\" + profileName + "\\ShooterGame\\Saved\\Config\\WindowsServer\\GameUserSettings.ini" ) );
			} catch ( IOException e ) { e.printStackTrace(); }
		}
		installServer( profileName );
	}
	
	/**
	 * サーバーを起動するBatファイルを起動
	 * @param profileName プロファイル名
	 * @param mapName マップ名
	 * @param mod_id ModのID
	 */
	public static void startServer( String profileName, String mapName, String mod_id ) {
		
		executeCommand( BAT_DIR + "startServer.bat " + ASM_DIR + profileName + " " + mapName + " " + mod_id );
	}
	
	/**
	 * 指定のプロファイルにサーバーをインストールするBatファイルを呼び出す
	 * @param profileName サーバーをインストールするプロファイル名
	 */
	public static void installServer( String profileName ) {
		
//		executeCommand( ASM_DIR + "SteamCMD\\steamcmd.exe +force_install_dir ../" + profileName + "/ +login anonymous +app_update 2430930 validate + quit + exit" );
		executeCommand( BAT_DIR + "installServer.bat " + JavasSystem.CURRENT_DIRECTORY + "\\ servers\\" + profileName );
	}
	
	/**
	 * ディレクトリを作成するメソッド
	 * @param path 作成するディレクトリのパス
	 */
	public static void mkdir( String path ) {
		
//		executeCommand( BAT_DIR + "mkdir.bat " + commandLine );
		try {
			Files.createDirectory( Paths.get( path ) );
		} catch (IOException e) { e.printStackTrace(); }
	}
}