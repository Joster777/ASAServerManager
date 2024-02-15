package com.stevejavas.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ファイルの読み書きなどを行うクラス
 */
public class JavasFileSystem {
	
	/**
	 * 操作中のファイル
	 */
	File file;
	
	/**
	 * 読み取るためのファイルリーダー
	 */
	FileReader fr 		= null;
	
	/**
	 * 読み取りを速くするためのリーダー
	 */
	BufferedReader br 	= null;
	
	/**
	 * 書き込むためのファイルライター
	 */
	FileWriter fw 		= null;
	
	/**
	 * 書き込みを速くするためのライター
	 */
	BufferedWriter bw 	= null;

	/**
	 * デフォルトコンストラクタ
	 */
	public JavasFileSystem() {	}
	
	/**
	 * 操作するファイルを指定
	 * @param file 操作するファイル
	 */
	public JavasFileSystem( File file ) {
		this.file = file;
	}
	
	/**
	 * Reader系をすべて開く
	 */
	public void openReader() {
		
		try {
			fr = new FileReader( file);
			br = new BufferedReader( fr );
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
			System.out.println( "ファイルがありません" );
		}
	}
	
	/**
	 * Reader系をすべて閉じる
	 * @throws IOException 例外
	 */
	public void closeReader() throws IOException {
		
		if ( br != null ) br.close();
		if ( fr != null ) fr.close();
	}
	
	/**
	 * Writer系をすべて開く
	 * @throws IOException 例外
	 */
	public void openWriter() throws IOException {
		
		try {
			fw = new FileWriter( file );
			bw = new BufferedWriter( fw );
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
			System.out.println( "ファイルがありません" );
		}
	}
	
	/**
	 * Writer系をすべて閉じる
	 * @throws IOException 例外
	 */
	public void closeWriter() throws IOException {
		
		if ( bw != null ) bw.close();
		if ( fw != null ) fw.close();
	}
	
	/**
	 * 指定のファイルに書いてある文字列をすべて消去する
	 * ( 一度ファイルを削除して新規作成するため一時的にファイルが消えます )
	 * @throws IOException 例外
	 */
	public void resetFile() throws IOException {
		
		if ( file != null ) {
			file.delete();
			file.createNewFile();
		}
	}
	
	/**
	 * ファイルのすべてのテキストをString型で取得
	 * @return すべてのテキスト
	 */
	public String getAllText() {
		
		String sum = "";
		String line;
		try {
			openReader();
			while ( ( line = br.readLine() ) != null ) {
				if ( line != null ) {
					sum += line + "\n";
				}
			}
		}catch ( IOException err ) {
			err.printStackTrace();
		}finally { 
			try {
			closeReader();
			} catch ( IOException err ) {
				err.printStackTrace();
			} 
		}
		return sum;
	}
	
	/**
	 * ファイルの行数を取得
	 * @return ファイルの行数
	 * @throws IOException 例外
	 */
	public int getRowNum() throws IOException {
		return ( getAllText().split( "\n" ).length );
	}
	
	/**
	 * ファイルのテキストを全行標準出力する
	 * 行番号:行のテキスト
	 * @throws IOException 例外
	 */
	public void print() throws IOException {
		
		String line = null;
		int c_row = 1;
		try {
			openReader();
			while ( ( line = br.readLine() ) != null ) {
				System.out.println( c_row + ":" + line );
				c_row++;
			}
		}finally {
			closeReader();
		}
	}
	
	/**
	 * 指定の行を取得
	 * @param row 取得する行番号
	 * @return 取得した行の文字列
	 * @throws IOException 例外
	 */
	public String read( int row ) throws IOException {
		
		String line = null;
		int c_row = 1;
		try {
			openReader();
			while ( ( line = br.readLine() ) != null ) {
				if ( c_row == row ) {
					break;
				}
				c_row++;
			}
		}finally {
			closeReader();
			if ( line == null ) {
				System.out.println( "指定の行がありませんでした" );
			}
		}
		return line;
	}
	
	/**
	 * 指定の行を指定の文字列に書き換える
	 * @param row 指定の行番号
	 * @param text 書き換える文字列
	 * @throws IOException 例外
	 */
	public void write( int row, String text ) throws IOException {
		
		String[] all = getAllText().split( "\n" );
		file.delete();
		file.createNewFile();
		row -= 1;
		try { 
			openWriter();
			for ( int i = 0; i < all.length; i++ ) {
				
				if ( i == row ) {
					bw.write( text + "\n" );
				}else {
					bw.write( all[i] + "\n" );
				}
			}
			bw.flush();
		}finally {
			closeWriter();
		}
	}
	
	/**
	 * 現在開いているファイルを取得
	 * @return 現在開いているファイル
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * 現在開いているファイルを変更
	 * @param file 変更後のファイル
	 */
	public void setFile( File file ) {
		this.file = file;
	}
	
	/**
	 * FileReaderを取得
	 * @return FileReader JavasFileSystemで使用しているFileReader
	 */
	public FileReader getFileReader() {
		return fr;
	}
}