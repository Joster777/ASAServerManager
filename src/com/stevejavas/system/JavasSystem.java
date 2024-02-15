package com.stevejavas.system;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.nio.file.Paths;

/**
 * よく使う変数とかメソッドを格納するクラス
 */
public class JavasSystem {
	
	/**
	 * デスクトップの幅
	 */
	public static final int DESKTOP_WIDTH = GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice()
			.getDisplayMode()
			.getWidth();
	
	/**
	 * デスクトップの高さ
	 */
	public static final int DESKTOP_HEIGHT = GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice()
			.getDisplayMode()
			.getHeight();
	
	/**
	 * パソコンのユーザ名
	 */
	public static final String USER_NAME = System.getProperty("user.name");
	
	/**
	 * アプリが入っているカレントディレクトリ
	 * eclipseの場合はプロジェクトフォルダ
	 */
	public static final String CURRENT_DIRECTORY = Paths.get("").toAbsolutePath().toString();
	
	/**
	 * 複数のコンポーネントをまとめて指定のコンテナに追加
	 * @param baseContainer ベースとなるコンテナ
	 * @param contents	追加するコンポーネント(配列型で指定)
	 */
	public static void addAll( Container baseContainer, Component[] contents ) {
		
		for ( int i = 0; i < contents.length; i++ ) {
			baseContainer.add( contents[i] );
		}
	}
}