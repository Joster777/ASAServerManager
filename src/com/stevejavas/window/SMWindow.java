package com.stevejavas.window;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.stevejavas.config.ConfigText;
import com.stevejavas.config.GameBuilder;
import com.stevejavas.config.GameUserSettingBuilder;
import com.stevejavas.console.ConsoleProcess;
import com.stevejavas.layout.ConfigButtonsLayout;
import com.stevejavas.system.Button;
import com.stevejavas.system.JavasSystem;
import com.stevejavas.system.Panel;
import com.stevejavas.system.ScrollPanel;
import com.stevejavas.system.TextField;

/**
 * ほぼメインクラス
 * ここにボタン押した時の処理とか書く
 * メモ
 * ARKのコンフィグ画面を元にレイアウト書いてて思ったのは
 * 有効化や無効化、許可などいろんな言葉をコンフィグで使ってるせいで
 * 非常にわかりにくかった
 * 全部有効化でやるとか、無効化でやるとかみたいな感じにして一貫性を持たせてくれ
 * だからソースコードぐちゃぐちゃになるんやぞ
 */
public class SMWindow extends JFrame implements Runnable {
	
	/**
	 * GameUserSettings.iniを書き換える用
	 */
	GameUserSettingBuilder gusBuilder;
	
	/**
	 * Game.iniを書き換える用
	 */
	GameBuilder gBuilder;
	
	/**
	 * ログを表示するためのコンソールウィンドウ
	 */
	ConsoleWindow console;
	
	// ------------------------------------ //
	// ------- このプログラム関係 --------- //
	// ------------------------------------ //
	
	/**
	 * サーバーマネージャーウィンドウを常に再描画するためのやつ
	 */
	Thread th;
	
	/**
	 * ウィンドウのサイズ
	 */
	int[] windowSize = { 1280, 720 };
	
	/**
	 * 画面全体を二つに分割してある
	 */
	private JPanel base;
	
	/**
	 * GridBagLayoutを使用しているbaseパネルのレイアウトの比率を制御するための変数
	 */
	GridBagConstraints gbc;
	
	/**
	 * メインメニューパネル
	 * プロファイル名、プロファイル関連のボタン、サーバー開始ボタン
	 * サーバー名、サーバーパスワード、管理者パスワード
	 * ModID、マップ名、ポート、クエリポート入力欄を配置する
	 */
	private JPanel menu;
	
	/**
	 * プロファイル用パネル
	 * プロファイル名入力欄、プロファイル保存ボタン、
	 * プロファイルロードボタン、サーバーインストールボタンを配置
	 */
	private JPanel profilePanel;
	
	/**
	 * メインメニュー分割用1
	 * サーバー名, ModのID入力欄を配置
	 */
	JPanel menuPanel1;
	
	/**
	 * メインメニュー分割用2
	 * サーバーパスワード 管理者パス マップ名入力欄を配置
	 */
	JPanel menuPanel2;
	
	/**
	 * メインメニュー分割用3
	 * ポート クエリポート サーバー開始ボタン入力欄を配置
	 */
	JPanel menuPanel3;
	
	/**
	 * コンフィグ切り替えボタンを配置するパネル
	 */
	JPanel configButtonsPanel;
	
	/**
	 * プロファイル名入力欄
	 */
	private JTextField profTF;
	
	
	// -------------------------------------------- //
	// ------------ 入力時に必要な設定 ------------ //
	// -------------------------------------------- //
	
	/**
	 * マップ入力欄
	 */
	private JTextField mapTF;
	
	/**
	 * ModのID入力欄
	 */
	private JTextField modTF;
	
	// -------------------------------------------- //
	// -------------- ServerSettings -------------- //
	// -------------------------------------------- //
	
	/**
	 * 一日の経過速度
	 * 詳細：ワールド
	 */
	private JTextField dayCycleSpeedScaleTF;
	
	/**
	 * 日中の経過速度
	 * 詳細：ワールド
	 */
	private JTextField dayTimeSpeedScaleTF;
	
	/**
	 * 夜間の経過速度
	 * 詳細：ワールド
	 */
	private JTextField nightTimeSpeedScaleTF;
	
	/**
	 * マップにプレイヤー位置表示設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox showMapPlayerLocationCB;
	
	/**
	 * 三人称視点許可設定
	 * ゲームルール：ルール
	 */
	private JCheckBox allowThirdPersonPlayerCB;
	
	/**
	 * クロスヘアの表示設定？
	 * ゲームルール：ルール
	 */
	private JCheckBox serverCrosshairCB;
	
	/**
	 * RCONポート番号
	 * RCONはRemoveCONsoleの略
	 * リモートで管理できるとかできないとか
	 * ゲーム内設定に入力場所はない
	 */
	private JTextField RCONPortTF;
	
	/**
	 * 特定の範囲内での建築できる建築物の最大数の設定
	 * ゲーム内設定に存在しない
	 */
	private JTextField theMaxStructuresInRangeTF;
	
	/**
	 * 構造物周辺の資源リスポーン間隔
	 * 詳細：PvP
	 */
	private JTextField structurePreventResourceRadiusMultiplierTF;
	
	/**
	 * トライブ名変更のクールタイム
	 * ゲーム内設定画面にない
	 */
	private JTextField tribeNameChangeCooldownTF;
	
	/**
	 * プラットフォーム上での建築可能範囲の設定
	 * 設定場所不明
	 */
	private JTextField platformSaddleBuildAreaBoundsMultiplierTF;
	
	/**
	 * ゲーム内設定には存在しない(不要)
	 * 前のARK( ASE )で使われてたやつ(多分maybe)
	 * これは建築物を設置した後に拾えなくなるまでの時間だと思う
	 * というかASAだといつでも拾える設定になってるからいらない
	 * どうせ開発陣がめんどいからコピペした結果残ってるやつだと思う
	 */
	private JTextField structurePickupTimeAfterPlacementTF;
	
	/**
	 * ゲーム内設定に存在しない(不要)
	 */
	private JTextField structurePickupHoldDurationTF;
	
	/**
	 * TEKストレージを設置できるようにする設定？らしい
	 * 初期値trueだからfalseにすると多分設置できなくなる
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox allowIntegratedSPlusStructuresCB;
	
	/**
	 * トライブのログにダメージソース(誰にやられたか)を隠す設定
	 * 初期だとfalse
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox allowHideDamageSourceFromLogsCB;
	
	/**
	 * オートセーブ間隔(分)
	 * ゲーム内設定に存在しない
	 */
	private JTextField autoSavePeriodMinutesTF;
	
	/**
	 * 恐竜テイム上限数
	 * ゲーム内設定に存在しない
	 */
	private JTextField maxTamedDinosTF;
	
	/**
	 * RCONサーバーへゲームログを送信する際の速度？
	 * ゲーム内設定に存在しない
	 */
	private JTextField RCONServerGameLogBufferTF;
	
	/**
	 * ヒットマーカーの表示許可
	 * 攻撃がヒットしたら画面の真ん中にXマークが出るやつ
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox allowHitMarkersCB;
	
	/**
	 * 建築物の上にクレートを召喚していいかの許可
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox allowCrateSpawnsOnTopOfStructuresCB;
	
	/**
	 * サーバーログインに必要なパスワード
	 * 書かなかったらパスワードなしになるんじゃね
	 * ゲーム内設定に存在しない
	 */
	private JTextField serverPasswordTF;
	
	/**
	 * 管理者パスワード
	 * コマンド入力する際に必要なやつ
	 * ゲーム内設定に存在しない
	 */
	private JTextField adminPasswordTF;
	
	/**
	 * スペクターモードになるためのパスワード？
	 * 使ったことないからわからん
	 * ゲーム内設定に存在しない
	 */
	private JTextField spectatorPasswordTF;
	
	/**
	 * RCONを有効にするかどうか
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox RCONEnabledCB;
	
	/**
	 * すべての管理者コマンドをゲーム内チャットに記録するかどうかの設定
	 * ゲームルール：ルール
	 */
	private JCheckBox adminLoggingCB;
	
	/**
	 * アクティブ状態のMod設定
	 * id,id,...みたいな感じで記録されている
	 * ゲーム内設定に存在しない
	 */
	private JTextField activeModsTF;
	
	/**
	 * トライブメンバーが破壊した敵の建築物のログの表示設定
	 * 初期だとFalse
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox tribeLogDestroyedEnemyStructuresCB;
	
	/**
	 * 共有接続を許可(調べたけどよくわからん)
	 */
	private JCheckBox allowSharedConnectionsCB;
	
	/**
	 * ハードコアモード設定
	 * ゲームルール：ワールド
	 */
	private JCheckBox serverHardcoreCB;
	
	/**
	 * PvE設定
	 * ゲームルール：ワールド
	 */
	private JCheckBox serverPVECB;
	
	/**
	 * 洞窟内での建築許可設定
	 * 詳細：PvE
	 */
	private JCheckBox allowCaveBuildingPvECB;
	
	/**
	 * 資源豊富エリアでの建築制限の設定
	 * 詳細：PvE
	 */
	private JCheckBox enableExtraStructurePreventionVolumesCB;
	
	/**
	 * 野生恐竜の出現レベルの倍率
	 * ゲーム内設定に存在しない
	 */
	private JTextField overrideOfficialDifficultyTF;
	
	/**
	 * ゲーム難易度(0～1)
	 * ゲームルール：ワールド
	 */
	private JTextField difficultyOffsetTF;
	
	/**
	 * 他のサーバーからのサバイバーダウンロード許可設定
	 * ゲームルール：ルール
	 */
	private JCheckBox noTributeDownloadsCB;
	
	/**
	 * サバイバーのアップロード許可設定
	 * ゲームルール：ルール
	 */
	private JCheckBox preventUploadSurvivorsCB;
	
	/**
	 * アイテムのアップロード許可設定
	 * ゲームルール：ルール
	 */
	private JCheckBox preventUploadItemsCB;
	
	/**
	 * 恐竜のアップロード許可設定
	 * ゲームルール：ルール
	 */
	private JCheckBox preventUploadDinosCB;
	
	/**
	 * アベレーションなどの一部マップで制限されている
	 * 生物のダウンロード許可設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox crossARKAllowForeignDinoDownloadsCB;
	
	/**
	 * オフライン時でのPvP攻撃の許可設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox preventOfflinePvPCB;
	
	/**
	 * トライブ同盟を禁止
	 * 詳細：PvE
	 */
	private JCheckBox preventTribeAlliancesCB;
	
	/**
	 * 病気のデバフの許可設定
	 * 詳細：PvE
	 */
	private JCheckBox preventDiseasesCB;
	
	/**
	 * リスポーン後の感染症を無効化
	 */
	private JCheckBox nonPermanentDiseasesCB;
	
	/**
	 * スポーン時のアニメーション(手首掻くやつ)の許可設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox preventSpawnAnimationsCB;
	
	/**
	 * プラットフォームサドル上に設置できる恐竜ゲートの数の設定
	 * ゲーム内設定に存在しない
	 */
	private JTextField maxGateFrameOnSaddlesTF;
	
	/**
	 * ジェネシスでのTekスーツの使用許可設定
	 * ジェネシス1では飛行が禁止なためこの設定がある
	 * まだゲーム内設定に存在しない
	 * てかジェネシスがまだ実装されてない
	 */
	private JCheckBox allowTekSuitPowersInGenesisCB;
	
	/**
	 * クライオシックネス(召喚酔い)の許可設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox enableCryoSicknessPVECB;
	
	/**
	 * 1キャラクターあたりのヘキサゴンの最大所持数制限の設定
	 * ゲーム内設定に存在しない
	 */
	private JTextField maxHexagonsPerCharacterTF;
	
	/**
	 * フィヨルドでのテレポート許可設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox useFjordurTraversalBuffCB; 
	
	/**
	 * 全体ボイスチャットの許可設定
	 * ゲームルール：ルール
	 */
	private JCheckBox globalVoiceChatCB;
	
	/**
	 * 近接テキストチャットの許可設定
	 * ゲームルール：ルール
	 */
	private JCheckBox proximityChatCB;
	
	/**
	 * 他プレイヤーのログアウト通知の設定
	 * ゲームルール：ルール
	 */
	private JCheckBox alwaysNotifyPlayerLeftCB;
	
	/**
	 * 他プレイヤーのログイン通知の設定
	 * ゲームルール：ルール
	 */
	private JCheckBox alwaysNotifyPlayerJoinedCB;
	
	/**
	 * HUDの無効化設定
	 * ゲームルール：ルール
	 */
	private JCheckBox serverForceNoHudCB;
	
	/**
	 * PvPガンマの有効化設定
	 * ゲームルール：ルール
	 */
	private JCheckBox enablePVPGammaCB;
	
	/**
	 * PvEガンマの無効化設定
	 * 詳細：PvE
	 */
	private JCheckBox disablePvEGammaCB;
	
	/**
	 * ダメージテキストの表示設定
	 * 詳細：その他
	 */
	private JCheckBox showFloatingDamageTextCB;
	
	/**
	 * PvEでの飛行生物のつかみの許可設定
	 * 詳細：PvE
	 */
	private JCheckBox allowFlyerCarryPVECB;
	
	/**
	 * 経験値倍率設定
	 * ゲームルール：ワールド
	 */
	private JTextField XPMultiplierTF;
	
	/**
	 * レイド恐竜(ティタノサウルス)の食事制限
	 * 初期設定はFalseで食事を接種させない設定になっている
	 * 詳細：その他
	 */
	private JCheckBox allowRaidDinoFeedingCB;
	
	/**
	 * PvEでの恐竜の所有権失効時間の無効化設定
	 * 詳細：PvE
	 */
	private JCheckBox disableDinoDecayPvECB;
	
	/**
	 * PvPでの恐竜の所有権失効時間の設定
	 * 詳細：PvP
	 */
	private JCheckBox PvPDinoDecayCB;
	
	/**
	 * 恐竜の所有権が失効された場合に自動で恐竜を消すかどうかの設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox autoDestroyDecayedDinosCB;
	
	/**
	 * 恐竜ごとに複数のC4をつけれるようにする設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox allowMultipleAttachedC4CB;
	
	/**
	 * 個人がテイムできる恐竜の数の設定
	 * ゲーム内設定に存在しない
	 */
	private JTextField maxPersonalTamedDinosTF;
	
	/**
	 * プラットフォームサドル上の構造物の数の設定
	 * ゲーム内設定に存在しない
	 */
	private JTextField personalTamedDinosSaddleStructureCostTF;
	
	/**
	 * 恐竜の刷り込みバフの無効化設定
	 * 詳細：ワールド
	 */
	private JCheckBox disableImprintDinoBuffCB;
	
	/**
	 * 刷り込みを刷り込み主以外でもできるようにするかの設定
	 * 詳細：ワールド
	 */
	private JCheckBox allowAnyoneBabyImprintCuddleCB;
	
	/**
	 * テイム速度の設定
	 * ゲームルール：ワールド
	 */
	private JTextField tamingSpeedMultiplierTF;
	
	/**
	 * 資源の獲得量の設定
	 * ゲームルール：ワールド
	 */
	private JTextField harvestAmountMultiplierTF;
	
	/**
	 * レア度の低いアイテムが出やすくなり、レア度の高いアイテムが出にくくなる設定
	 * 例：ステゴの死体を殴ったときに、生肉の獲得量が増え、
	 * 霜降り肉の獲得量が減る
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox useOptimizedHarvestingHealthCB;
	
	/**
	 * ダメージ量(資源を殴った時の近接攻撃力)に応じて、
	 * 素材の獲得量の変更を許可するかの設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox clampResourceHarvestDamageCB;
	
	/**
	 * 腐敗時間が進んでいるものと進んでいないものとで纏めた時に、
	 * 腐敗時間が進んでいない方に統合されるかどうかの設定？
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox clampItemSpoilingTimesCB;
	
	/**
	 * 霧の無効化設定
	 * 火山とか霧になると何も見えなくなるからオフにした方が良い
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox disableWeatherFogCB;
	
	/**
	 * PvPでの建築物の自然消滅の設定
	 * ゲーム内設定に存在しない
	 * だけどPvP関連なのでPvPのところに入れる
	 */
	private JCheckBox PvPStructureDecayCB;
	
	/**
	 * クレートが落ちてくる場所での建築許可設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox PvEAllowStructuresAtSupplyDropsCB;
	
	/**
	 * PvEでの建築物の自然消滅の許可設定
	 * 詳細：PvE
	 */
	private JCheckBox disableStructureDecayPVECB;
	
	/**
	 * Trueにするとデフォルトですべての建築物がロックされる設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox forceAllStructureLockingCB;
	
	/**
	 * 建築物のコアとなる土台や柱が自然消滅しないようにするための設定？
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox onlyAutoDestroyCoreStructuresCB;
	
	/**
	 * 土台や柱のみの建築物のみ自然消滅させる設定。
	 * PvPとかで見られる柱置きまくってエリアとる戦法を破壊する設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox onlyDecayUnsnappedCoreStructuresCB;
	
	/**
	 * 土台や柱のみの建築物を5倍の速度で破壊する設定？
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox fastDecayUnsnappedCoreStructuresCB;
	
	/**
	 * 水源に接続されておらず、且つ近くに味方プレイヤーがいない状態が
	 * リアルタイムで二日間続いた状態になるとパイプを破壊する設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox destroyUnconnectedWaterPipesCB;
	
	/**
	 * 建築物を常に拾えるようにする設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox alwaysAllowStructurePickupCB;
	
	/**
	 * 操作していないプレイヤーをサーバーから蹴るまでの時間
	 * デフォルト3600s = 1時間
	 * ゲーム内設定に存在しない
	 */
	private JTextField kickIdlePlayersPeriodTF;
	
	/**
	 * サバイバーダウンロード許可設定
	 * ゲームルール：ルール
	 */
	private JCheckBox preventDownloadSurvivorsCB;
	
	/**
	 * アイテムのダウンロード許可設定
	 * ゲームルール：ルール
	 */
	private JCheckBox preventDownloadItemsCB;
	
	/**
	 * 恐竜のダウンロード許可設定
	 * ゲームルール：ルール
	 */
	private JCheckBox preventDownloadDinosCB;
	
	/**
	 * 転送可能最大恐竜数の設定
	 * ゲーム内設定に存在しない
	 */
	private JTextField maxTributeDinosTF;
	
	/**
	 * 転送可能最大アイテム数の設定
	 * ゲーム内設定に存在しない
	 */
	private JTextField maxTributeItemsTF;
	
	/**
	 * 野生恐竜を定期的に初期化する時間設定
	 * 初期値：604800s = 1週間
	 * つまり1週間ごとに野生恐竜が初期化される
	 * ゲーム内設定に存在しない
	 */
	private JTextField serverAutoForceRespawnWildDinosIntervalTF;
	
	/**
	 * 良くない名前のトライブ名を非表示にする設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox bFilterTribeNamesCB;
	
	/**
	 * 良くない名前のサバイバーの名前を非表示にする設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox bFilterCharacterNamesCB;
	
	/**
	 * 良くないチャットを非表示にする設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox bFilterChatCB;
	
	/**
	 * PvPでの洞窟内での建築許可設定
	 * ゲーム内設定に存在しないけど
	 * PvP関係だからPvPのところに入れる
	 */
	private JCheckBox allowCaveBuildingPvPCB;
	
	/**
	 * クレートの位置をランダムにする設定
	 * 注意：この設定をオンにするとラグナロクにてアーティファクトが
	 * 取れなくなるらしい。53運営。
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox randomSupplyCratePointsCB;
	
	/**
	 * プレイヤーの参加を常に通知しないようにする設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox dontAlwaysNotifyPlayerJoinedCB;
	
	/**
	 * 建築上限を無視して建築できるようにする設定
	 * ゲーム内設定に存在しない
	 */
	private JCheckBox ignoreLimitMaxStructuresInRangeTypeFlagCB; 
	
	/**
	 * マジでわからない
	 * いじるな
	 * ゲーム内設定に存在しない
	 */
	private JTextField startTimeHourTF;
	
	/**
	 * インプラント自滅のクールダウンの時間設定
	 * ゲーム内設定に存在しない
	 */
	private JTextField implantSuicideCDTF;
	
	/**
	 * 水中での酸素消費量の設定
	 * ゲーム内設定に存在しないけど
	 * プレイヤー関係だからそこにいれる
	 */
	private JTextField oxygenSwimSpeedStatMultiplierTF;
	
	/**
	 * レイド恐竜(ティタノ)の食料値消費量の設定
	 * これを0にすればティタノとかが実質餓死しない状態になる
	 * ゲーム内設定に存在しないけど
	 * 恐竜関係だからそこに入れる
	 */
	private JTextField raidDinoCharacterFoodDrainMultiplierTF;
	
	/**
	 * PvEで恐竜が自然消滅する時間の設定
	 * 詳細：PvE
	 */
	private JTextField PvEDinoDecayPeriodMultiplierTF;
	
	/**
	 * いかだやプラットフォームサドル上に配置できるアイテムの数の設定
	 * ゲーム内設定に存在しないけど建築関係だからそこに入れる
	 */
	private JTextField perPlatformMaxStructuresMultiplierTF;
	
	/**
	 * アイテムのスタック数の倍率
	 * 初期だと1
	 * これを例えば2にすると多分わらを1スタックで400持てるようになる
	 * ゲーム内設定に存在しない
	 * ワールド設定に入れる
	 */
	private JTextField itemStackSizeMultiplierTF;
	
	/**
	 * ソフトテイム上限の設定
	 * ソフトテイムがよくわからんけど多分
	 * 昏睡させて飯食わせている仮テイムの状態のことだと思う
	 * ゲーム内設定に存在しない
	 */
	private JTextField maxTamedDinos_SoftTameLimitTF;
	
	/**
	 * ソフトテイムの削除期間の設定
	 * ゲーム内設定に存在しない
	 */
	private JTextField maxTamedDinos_SoftTameLimit_CountdownForDeletionDurationprivateTF;

	/**
	 * 開始時刻をオーバーライド
	 * 詳細：ワールド
	 */
	private JCheckBox startTimeOverrideCB;
	
	/**
	 * プレイヤーの攻撃力
	 * ゲームルール：プレイヤー
	 */
	private JTextField playerDamageMultiplierTF;
	
	/**
	 * プレイヤーの防御力
	 * ゲームルール：プレイヤー
	 */
	private JTextField playerResistanceMultiplierTF;
	
	/**
	 * プレイヤーの水分値消費速度
	 * ゲームルール：プレイヤー
	 */
	private JTextField playerCharacterWaterDrainMultiplierTF;
	
	/**
	 * プレイヤーの食料値消費速度
	 * ゲームルール：プレイヤー
	 */
	private JTextField playerCharacterFoodDrainMultiplierTF;
	
	/**
	 * プレイヤーのスタミナ消費速度
	 * ゲームルール：プレイヤー
	 */
	private JTextField playerCharacterStaminaDrainMultiplierTF;
	
	/**
	 * プレイヤーの体力自動回復速度
	 * ゲームルール：プレイヤー
	 */
	private JTextField playerCharacterHealthRecoveryMultiplierTF;
	
	/**
	 * ワールドの恐竜の数 0 ～ 1
	 * ゲームルール：恐竜
	 */
	private JTextField dinoCountMultiplierTF;
	
	/**
	 * 恐竜の攻撃力
	 * ゲームルール：恐竜
	 */
	private JTextField dinoDamageMultiplierTF;
	
	/**
	 * 恐竜の防御力
	 * ゲームルール：恐竜
	 */
	private JTextField dinoResistanceMultiplierTF;
	
	/**
	 * 恐竜の食料値消費速度
	 * ゲームルール：恐竜
	 */
	private JTextField dinoCharacterFoodDrainMultiplierTF;
	
	/**
	 * 恐竜のスタミナ消費速度
	 * ゲームルール：恐竜
	 */
	private JTextField dinoCharacterStaminaDrainMultiplierTF;
	
	/**
	 * 恐竜の体力自動回復速度
	 * ゲームルール：恐竜
	 */
	private JTextField dinoCharacterHealthRecoveryMultiplierTF;
	
	/**
	 * 建造物の攻撃力 (タレットや防護柵のダメージ？)
	 * ゲームルール：建造物
	 */
	private JTextField structureDamageMultiplierTF;
	
	/**
	 * 建造物の防御力
	 * ゲームルール：建造物
	 */
	private JTextField structureResistanceMultiplierTF;
	
	/**
	 * 建造物の所有権失効時間
	 * 詳細：PvE
	 */
	private JTextField PvEStructureDecayPeriodMultiplierTF;
	
	/**
	 * 生物の洞窟内飛行を許可
	 * 詳細：PvE
	 */
	private JCheckBox forceAllowCaveFlyersCB;
	
	/**
	 * プラットフォームへの建造禁止を無効化 (タレットや防護柵など)
	 * 詳細：PvP
	 */
	private JCheckBox overrideStructurePlatformPreventionCB;
	
	/**
	 * 採取可能物の耐久力
	 * 詳細：ワールド
	 */
	private JTextField harvestHealthMultiplierTF;
	
	/**
	 * 資源のリスポーン間隔
	 * 詳細：ワールド
	 */
	private JTextField resourcesRespawnPeriodMultiplierTF;
	
	/**
	 * レイド恐竜の食料消費を許可(オンにするとインベントリ内の食料を消費する)
	 */
	private JTextField allowRaidDinoFeedingTF;
	
	/**
	 * 特定のエングラムの許可
	 */
	private JCheckBox onlyAllowSpecifiedEngramsCB;
	
	/**
	 * ConfigTextクラスの
	 * COLLECTIONS_SERVER変数に合うように配列を作る
	 */
	private JComponent[] TEXTFIELDS_SERVER;
	
	
	// ------------------------------------------- //
	// ------------- SessionSettings ------------- //
	// ------------------------------------------- //
	
	/**
	 * サーバー名入力欄
	 */
	private JTextField serverNameTF;
	
	/**
	 * ポート番号入力欄
	 */
	private JTextField portTF;
	
	/**
	 * クエリポート入力欄
	 */
	private JTextField queryPortTF;
	
	/**
	 * マルチホームの有効化設定
	 */
	private JCheckBox multiHomeCB;
	
	/**
	 * ConfigTextクラスの
	 * COLLECTIONS_SESSION変数に合うように配列を作る
	 */
	private JComponent[] TEXTFIELDS_SESSION;
	
	// ------------------------------------------- //
	// --------------- GameSession --------------- //
	// ------------------------------------------- //
	
	/**
	 * サーバーの最大プレイヤー数
	 */
	private JTextField maxPlayersTF;
	
	/**
	 * ConfigTextクラスの
	 * COLLECTIONS_GAMESESSION変数に合うように配列を作る
	 */
	private JComponent[] TEXTFIELDS_GAMESESSION;
	
	// ------------------------------------------- //
	// ------------- MessageOfTheDay ------------- //
	// ------------------------------------------- //
	
	/**
	 * サーバーにログインする度に表示されるメッセージ
	 */
	private JTextField messageTF;
	
	/**
	 * 
	 */
	private JTextField durationTF;
	
	/**
	 * メッセージを設定した人のID？
	 */
	private JTextField messageSetterIDTF;
	
	/**
	 * ConfigTextクラスの
	 * COLLECTIONS_MESSAGE変数に合うように配列を作る
	 */
	private JComponent[] TEXTFIELDS_MESSAGE;

	// ---------------- Game.ini ----------------- //
	// ------------------------------------------- //
	// ------------- ShooterGameMode ------------- //
	// ------------------------------------------- //
	
	/**
	 * 赤ちゃんへの刷り込み時のバフ効果倍率
	 * 詳細：ワールド
	 */
	private JTextField babyImprintingStatScaleMultiplierTF;
	
	/**
	 * 赤ちゃんの世話の間隔
	 * 詳細：ワールド
	 */
	private JTextField babyCuddleIntervalMultiplierTF;
	
	/**
	 * 赤ちゃんの世話の猶予間隔
	 * 詳細：ワールド
	 */
	private JTextField babyCuddleGracePeriodMultiplierTF;
	
	/**
	 * 赤ちゃんの世話の刷り込み時のバフ効果減少速度
	 * 詳細：ワールド
	 */
	private JTextField babyCuddleLoseImprintQualitySpeedMultiplierTF;
	
	/**
	 * レベル毎のステータス HP
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamedHPTF;
	
	/**
	 * レベル毎のステータス スタミナ
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamedSTTF;
	
	/**
	 * レベル毎のステータス 酸素値
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamedOXTF;
	
	/**
	 * レベル毎のステータス 食料値
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamedFDTF;
	
	/**
	 * レベル毎のステータス 水分値
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamedWTTF;
	
	/**
	 * レベル毎のステータス 重量
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamedWETF;
	
	/**
	 * レベル毎のステータス 近接攻撃力
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamedDMGTF;
	
	/**
	 * レベル毎のステータス 移動速度
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamedSPTF;
	
	/**
	 * レベル毎のステータス 忍耐力
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamedFTTF;
	
	/**
	 * レベル毎のステータス 気絶値
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamedTRPTF;
	
	/**
	 * レベル毎のステータス 温度
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamedTMPTF;
	
	/**
	 * レベル毎の追加ステータスの倍率 HP
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AddHPTF;
	
	/**
	 * レベル毎の追加ステータスの倍率 スタミナ
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AddSTTF;
	
	/**
	 * レベル毎の追加ステータスの倍率 酸素値
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AddOXTF;
	
	/**
	 * レベル毎の追加ステータスの倍率 食料値
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AddFDTF;
	
	/**
	 * レベル毎の追加ステータスの倍率 水分値
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AddWTTF;
	
	/**
	 * レベル毎の追加ステータスの倍率 重量
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AddWETF;
	
	/**
	 * レベル毎の追加ステータスの倍率 近接攻撃力
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AddDMGTF;
	
	/**
	 * レベル毎の追加ステータスの倍率 移動速度
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AddSPTF;
	
	/**
	 * レベル毎の追加ステータスの倍率 忍耐力
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AddFTTF;
	
	/**
	 * レベル毎の追加ステータスの倍率 気絶値
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AddTRPTF;
	
	/**
	 * レベル毎の追加ステータスの倍率 温度
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AddTMPTF;
	
	/**
	 * テイム後のステータスの変化の倍率 HP
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AffinityHPTF;
	
	/**
	 * テイム後のステータスの変化の倍率 スタミナ
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AffinitySTTF;
	
	/**
	 * テイム後のステータスの変化の倍率 酸素値
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AffinityOXTF;
	
	/**
	 * テイム後のステータスの変化の倍率 食料値
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AffinityFDTF;
	
	/**
	 * テイム後のステータスの変化の倍率 水分値
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AffinityWTTF;
	
	/**
	 * テイム後のステータスの変化の倍率 重量
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AffinityWETF;
	
	/**
	 * テイム後のステータスの変化の倍率 近接攻撃力
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AffinityDMGTF;
	
	/**
	 * テイム後のステータスの変化の倍率 移動速度
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AffinitySPTF;
	
	/**
	 * テイム後のステータスの変化の倍率 忍耐力
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AffinityFTTF;
	
	/**
	 * テイム後のステータスの変化の倍率 気絶値
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AffinityTRPTF;
	
	/**
	 * テイム後のステータスの変化の倍率 温度
	 * 詳細：テイム済みの恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoTamed_AffinityTMPTF;
	
	/**
	 * 野生恐竜のステータスの倍率 HP
	 * 詳細：野生の恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoWildHPTF;
	
	/**
	 * 野生恐竜のステータスの倍率 スタミナ
	 * 詳細：野生の恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoWildSTTF;
	
	/**
	 * 野生恐竜のステータスの倍率 酸素値
	 * 詳細：野生の恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoWildOXTF;
	
	/**
	 * 野生恐竜のステータスの倍率 食料値
	 * 詳細：野生の恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoWildFDTF;
	
	/**
	 * 野生恐竜のステータスの倍率 水分値
	 * 詳細：野生の恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoWildWTTF;
	
	/**
	 * 野生恐竜のステータスの倍率 重量
	 * 詳細：野生の恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoWildWETF;
	
	/**
	 * 野生恐竜のステータスの倍率 近接攻撃力
	 * 詳細：野生の恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoWildDMGTF;
	
	/**
	 * 野生恐竜のステータスの倍率 移動速度
	 * 詳細：野生の恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoWildSPTF;
	
	/**
	 * 野生恐竜のステータスの倍率 忍耐力
	 * 詳細：野生の恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoWildFTTF;
	
	/**
	 * 野生恐竜のステータスの倍率 気絶値
	 * 詳細：野生の恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoWildTRPTF;
	
	/**
	 * 野生恐竜のステータスの倍率 温度
	 * 詳細：野生の恐竜
	 */
	private JTextField perLevelStatsMultiplier_DinoWildTMPTF;
	
	/**
	 * プレイヤーのステータスの倍率 HP
	 * 詳細：プレイヤー
	 */
	private JTextField perLevelStatsMultiplier_PlayerHPTF;
	
	/**
	 * プレイヤーのステータスの倍率 スタミナ
	 * 詳細：プレイヤー
	 */
	private JTextField perLevelStatsMultiplier_PlayerSTTF;
	
	/**
	 * プレイヤーのステータスの倍率 酸素値
	 * 詳細：プレイヤー
	 */
	private JTextField perLevelStatsMultiplier_PlayerOXTF;
	
	/**
	 * プレイヤーのステータスの倍率 食料値
	 * 詳細：プレイヤー
	 */
	private JTextField perLevelStatsMultiplier_PlayerFDTF;
	
	/**
	 * プレイヤーのステータスの倍率 水分値
	 * 詳細：プレイヤー
	 */
	private JTextField perLevelStatsMultiplier_PlayerWTTF;
	
	/**
	 * プレイヤーのステータスの倍率 重量
	 * 詳細：プレイヤー
	 */
	private JTextField perLevelStatsMultiplier_PlayerWETF;
	
	/**
	 * プレイヤーのステータスの倍率 近接攻撃力
	 * 詳細：プレイヤー
	 */
	private JTextField perLevelStatsMultiplier_PlayerDMGTF;
	
	/**
	 * プレイヤーのステータスの倍率 移動速度
	 * 詳細：プレイヤー
	 */
	private JTextField perLevelStatsMultiplier_PlayerSPTF;
	
	/**
	 * プレイヤーのステータスの倍率 忍耐力
	 * 詳細：プレイヤー
	 */
	private JTextField perLevelStatsMultiplier_PlayerFTTF;
	
	/**
	 * プレイヤーのステータスの倍率 気絶値
	 * 詳細：プレイヤー
	 */
	private JTextField perLevelStatsMultiplier_PlayerTRPTF;
	
	/**
	 * プレイヤーのステータスの倍率 温度
	 * 詳細：プレイヤー
	 */
	private JTextField perLevelStatsMultiplier_PlayerTMPTF;
	
	/**
	 * 腐敗までの時間
	 * 肉やベリーなどの食料品の腐敗時間の倍率 (大きくすると時間が長くなる)
	 * 詳細：ワールド
	 */
	private JTextField globalSpoilingTimeMultiplierTF;
	
	/**
	 * アイテムが消滅するまでの時間の倍率 (大きくすると長くなる)
	 * 詳細：ワールド
	 */
	private JTextField globalItemDecompositionTimeMultiplierTF;
	
	/**
	 * 死体消失までの時間
	 * デスアイテムキャッシュが消えるまでの時間 (大きくすると長くなる)
	 * 詳細：ワールド
	 */
	private JTextField globalCorpseDecompositionTimeMultiplierTF;
	
	/**
	 * エリア建造物へのダメージ
	 * 詳細：PvP
	 */
	private JTextField PvPZoneStructureDamageMultiplierTF;
	
	/**
	 * 建造物の修理クールタイム
	 * ゲームルール：建造物
	 */
	private JTextField structureDamageRepairCooldownTF;
	
	/**
	 * リスポーン間隔の周期
	 * 詳細：PvP
	 */
	private JTextField increasePvPRespawnIntervalCheckPeriodTF;
	
	/**
	 * リスポーン間隔を変更
	 * 詳細：PvP
	 */
	private JTextField increasePvPRespawnIntervalMultiplierTF;
	
	/**
	 * リスポーン間隔の基準
	 * 詳細：PvP
	 */
	private JTextField increasePvPRespawnIntervalBaseAmountTF;
	
	/**
	 * 資源のリスポーンに必要なプレイヤーからの距離
	 * 詳細：ワールド
	 */
	private JTextField resourceNoReplenishRadiusPlayersTF;
	
	/**
	 * 資源のリスポーンに必要な建造物からの距離
	 * 詳細：ワールド
	 */
	private JTextField resourceNoReplenishRadiusStructuresTF;
	
	/**
	 * 作物の成長速度の倍率
	 * 詳細：ワールド
	 */
	private JTextField cropGrowthSpeedMultiplierTF;
	
	/**
	 * 産卵可能になるまでの期間
	 * 詳細：ワールド
	 */
	private JTextField layEggIntervalMultiplierTF;
	
	/**
	 * 排泄の間隔
	 * 詳細：ワールド
	 */
	private JTextField poopIntervalMultiplierTF;
	
	/**
	 * 作物の腐敗速度
	 * 詳細：ワールド
	 */
	private JTextField cropDecaySpeedMultiplierTF;
	
	/**
	 * 交配可能になるまでの期間
	 * 詳細：ワールド
	 */
	private JTextField matingIntervalMultiplierTF;
	
	/**
	 * 卵の孵化速度
	 * 詳細：ワールド
	 */
	private JTextField eggHatchSpeedMultiplierTF;
	
	/**
	 * 赤ちゃんの成長速度倍率
	 * 詳細：ワールド
	 */
	private JTextField babyMatureSpeedMultiplierTF;
	
	/**
	 * 赤ちゃんの食料消費速度
	 * 詳細：ワールド
	 */
	private JTextField babyFoodConsumptionSpeedMultiplierTF;
	
	/**
	 * 恐竜のタレット被ダメージ倍率
	 * ゲームルール：恐竜
	 */
	private JTextField dinoTurretDamageMultiplierTF;
	
	/**
	 * 恐竜の採取効率倍率
	 * ゲームルール：恐竜
	 */
	private JTextField dinoHarvestingDamageMultiplierTF;
	
	/**
	 * プレイヤーの採取効率倍率
	 * ゲームルール：プレイヤー
	 */
	private JTextField playerHarvestingDamageMultiplierTF;
	
	/**
	 * カスタムレシピの効果
	 * 詳細：その他
	 */
	private JTextField customRecipeEffectivenessMultiplierTF;
	
	/**
	 * 製作速度によるカスタムレシピの性能
	 * 詳細：その他
	 */
	private JTextField customRecipeSkillMultiplierTF;
	
	/**
	 * PvEの自動開始時間(秒)
	 * 詳細：PvE
	 */
	private JTextField autoPvEStartTimeSecondsTF;
	
	/**
	 * PvEの自動終了時間(秒)
	 * 詳細：PvE
	 */
	private JTextField autoPvEStopTimeSecondsTF;
	
	/**
	 * 経験値倍率：キル
	 * 詳細：経験値倍率
	 */
	private JTextField killXPMultiplierTF;
	
	/**
	 * 経験値倍率：採取
	 * 詳細：経験値倍率
	 */
	private JTextField harvestXPMultiplierTF;
	
	/**
	 * 経験値倍率：製作
	 * 詳細：経験値倍率
	 */
	private JTextField craftXPMultiplierTF;
	
	/**
	 * 経験値倍率：一般
	 * 詳細：経験値倍率
	 */
	private JTextField genericXPMultiplierTF;
	
	/**
	 * 経験値倍率：特殊
	 * 詳細：経験値倍率
	 */
	private JTextField specialXPMultiplierTF;
	
	/**
	 * 燃料消費の間隔
	 * 詳細：その他
	 */
	private JTextField fuelConsumptionIntervalMultiplierTF;
	
	/**
	 * フォトモードの範囲制限
	 * 詳細：その他
	 */
	private JTextField photoModeRangeLimitTF;
	
	/**
	 * フォトモードを無効化
	 * 詳細：その他
	 */
	private JCheckBox bDisablePhotoModeCB;
	
	/**
	 * 連続死亡時のリスポーン間隔(多分)
	 * 詳細：PvP
	 */
	private JTextField bIncreasePvPRespawnIntervalTF;
	
	/**
	 * タイマーを使用
	 * 詳細：PvE
	 */
	private JCheckBox bAutoPvETimerCB;
	
	/**
	 * システム時間を使用
	 * 詳細：PvE
	 */
	private JCheckBox bAutoPvEUseSystemTimeCB;
	
	/**
	 * フレンドリーファイアを無効化
	 * ゲームルール：ルール
	 */
	private JCheckBox bDisableFriendlyFireCB;
	
	/**
	 * 飛行生物のプラットフォーム上への生物の搭乗を許可
	 * 詳細：その他
	 */
	private JCheckBox bFlyerPlatformAllowUnalignedDinoBasingCB;
	
	/**
	 * クレートの無効化
	 * ゲームルール：ルール
	 */
	private JCheckBox bDisableLootCratesCB;
	
	/**
	 * カスタムレシピの許可
	 * 詳細：その他
	 */
	private JCheckBox bAllowCustomRecipesCB;
	
	/**
	 * 騎乗者のいない恐竜の保護の許可
	 * 詳細：その他
	 */
	private JCheckBox bPassiveDefensesDamageRiderlessDinosCB;
	
	/**
	 * PvEでのトライブ戦争の許可
	 * 詳細：PvE
	 */
	private JCheckBox bPvEAllowTribeWarCB;
	
	/**
	 * PvEでのトライブ戦争のキャンセルの許可
	 * 詳細：PvE
	 */
	private JCheckBox bPvEAllowTribeWarCancelCB;
	
	/**
	 * 最大難易度の設定 マップに関係なく野生生物の最大レベルを150にする
	 * ゲームルール：ワールド
	 */
	private JCheckBox maxDifficultyCB;
	
	/**
	 * シングルプレイヤー設定 一人でプレイする際に適切な設定に変更する
	 * ゲームルール：ルール
	 */
	private JCheckBox bUseSingleplayerSettingsCB;
	
	/**
	 * 死亡地点の表示 MAPに死亡地点を表示する
	 * ゲーム内設定にない
	 */
	private JCheckBox bUseCorpseLocatorCB;
	
	/**
	 * ポーズメニュー内にクリエイティブモード有効化ボタンを表示する
	 * ゲームルール：ルール
	 */
	private JCheckBox bShowCreativeModeCB;
	
	/**
	 * タレットの設置数が制限を超えた際に超えた分のタレットを停止する
	 * ゲームルール：建造物 (ゲーム内設定にない)
	 */
	private JCheckBox bHardLimitTurretsInRangeCB;
	
	/**
	 * 地形に食い込む建築物を作ることを許可する
	 * ゲームルール：建造物
	 */
	private JCheckBox bDisableStructurePlacementCollisionCB;
	
	/**
	 * プラットフォーム上に置かれた土台だけが土台として機能する様にする
	 * ゲームルール：建造物 (ない)
	 */
	private JCheckBox bAllowPlatformSaddleMultiFloorsCB;
	
	/**
	 * リスペックを無制限化
	 * オンにするとマインドワイプトニックの使用制限を無効化できる
	 * 詳細：その他 (ない)
	 */
	private JCheckBox bAllowUnlimitedRespecsCB;
	
	/**
	 * テイム済みの生物への騎乗の無効化
	 * ゲームルール：恐竜
	 */
	private JCheckBox bDisableDinoRidingCB;
	
	/**
	 * 生物のテイムの無効化
	 * ゲームルール：恐竜
	 */
	private JCheckBox bDisableDinoTamingCB;
	
	/**
	 * プレイヤーの経験値上限
	 * 詳細：経験値倍率 (ない)
	 */
	private JTextField overrideMaxExperiencePointsPlayerTF;
	
	/**
	 * 生物の経験値上限
	 * 詳細：経験値倍率 (ない)
	 */
	private JTextField overrideMaxExperiencePointsDinoTF;
	
	/**
	 * トライブに入れるプレイヤーの数を制限する(0で無制限)
	 * 詳細：その他
	 */
	private JTextField maxNumberOfPlayersInTribeTF;
	
	/**
	 * エクスプローラーノートを発見した際に得られる経験値の量
	 * 詳細：経験値倍率
	 */
	private JTextField explorerNoteXPMultiplierTF;
	
	/**
	 * BOSSを倒した際に得られる経験値の量
	 * 詳細：経験値倍率
	 */
	private JTextField bossKillXPMultiplierTF;
	
	/**
	 * アルファ生物を倒した際に得られる経験値の量
	 * 詳細：経験値倍率
	 */
	private JTextField alphaKillXPMultiplierTF;
	
	/**
	 * 野生生物を倒した際に得られる経験値の量
	 * 詳細：経験値倍率
	 */
	private JTextField wildKillXPMultiplierTF;
	
	/**
	 * 洞窟内の生物を倒した際に得られる経験値の量
	 * 詳細：経験値倍率
	 */
	private JTextField caveKillXPMultiplierTF;
	
	/**
	 * テイム済みの生物を倒した際に得られる経験値の量
	 * 詳細：経験値倍率
	 */
	private JTextField tamedKillXPMultiplierTF;
	
	/**
	 * 未所有の生物を倒した際に得られる経験値の量
	 * 詳細：経験値倍率
	 */
	private JTextField unclaimedKillXPMultiplierTF;
	
	/**
	 * クレートから得られるアイテムの品質を増減する
	 * 詳細：その他
	 */
	private JTextField supplyCrateLootQualityMultiplierTF;
	
	/**
	 * 釣り上げたアイテムの品質を増減する
	 * 詳細：その他
	 */
	private JTextField fishingLootQualityMultiplierTF;
	
	/**
	 * プレイヤーの製作スキルに応じた、製作アイテムの品質を増減する
	 * 詳細：その他
	 */
	private JTextField craftingSkillBonusMultiplierTF;
	
	/**
	 * プレイヤーと生物の移動速度のレベルを上げることを許可する(飛行生物以外)
	 * ゲームルール：ワルード
	 */
	private JCheckBox bAllowSpeedLevelingCB;
	
	/**
	 * 飛行生物の移動速度のレベルをあげることを許可する
	 * ゲームルール：ワールド
	 */
	private JCheckBox bAllowFlyerSpeedLevelingCB;
	
	/**
	 * ConfigTextクラスのCOLLECTION_SHOOTERGAMEMODEに合わせる
	 */
	private JComponent[] TEXTFIELDS_SHOOTERGAMEMODE;
	
	/**
	 * メイン用
	 */
	JPanel mainPanel;
	
	/**
	 * メイン切り替え用
	 */
	CardLayout clayout;
	
	/**
	 * ゲーム内設定に存在しないコンフィグを格納するパネル
	 */
	ScrollPanel serverSettingsPanel;
	
	/**
	 * ゲームルール：プレイヤーのパネル
	 */
	ScrollPanel playerSettingsPanel;
	
	/**
	 * ゲームルール：恐竜のパネル
	 */
	ScrollPanel dinoSettingsPanel;
	
	/**
	 * ゲームルール：建造物のパネル
	 */
	ScrollPanel structureSettingsPanel;
	
	/**
	 * ゲームルール：ワールドのパネル
	 */
	ScrollPanel worldSettingsPanel;
	
	/**
	 * ゲームルール：ルールのパネル
	 */
	ScrollPanel ruleSettingsPanel;
	
	/**
	 * 詳細：PvEのパネル
	 */
	ScrollPanel PvESettingsPanel;
	
	/**
	 * 詳細：PvPのパネル
	 */
	ScrollPanel PvPSettingsPanel;
	
	/**
	 * 詳細：ワールドのパネル
	 */
	ScrollPanel worldDetailedSettingsPanel;
	
	/**
	 * 詳細：野生恐竜のパネル
	 */
	ScrollPanel dinoWildSettingsPanel;
	
	/**
	 * 詳細：テイム済みの恐竜のパネル
	 */
	ScrollPanel tamedDinoSettingsPanel;
	
	/**
	 * 詳細：プレイヤーのパネル
	 */
	ScrollPanel playerDetailedSettingsPanel;
	
	/**
	 * 詳細：経験値倍率のパネル
	 */
	ScrollPanel experienceSettingsPanel;
	
	/**
	 * 詳細：その他のパネル
	 */
	ScrollPanel otherSettingsPanel;
	
	/**
	 * ウィンドウの初期化、レイアウトの生成、ウィンドウの可視化を行い、
	 * サーバーマネージャーウィンドウを作成する
	 * 開始するには start() メソッドを呼び出す必要がある。
	 * 停止するには stop() メソッドを呼び出す。
	 */
	public SMWindow() {
		
		console = new ConsoleWindow();
		console.start();
		
		init();
		baseLayout();
		TEXTFIELDS_SERVER = new JComponent[] {
				dayCycleSpeedScaleTF,
				dayTimeSpeedScaleTF,
				nightTimeSpeedScaleTF,
				showMapPlayerLocationCB,
				allowThirdPersonPlayerCB,
				serverCrosshairCB,
				RCONPortTF,
				theMaxStructuresInRangeTF,
				structurePreventResourceRadiusMultiplierTF,
				tribeNameChangeCooldownTF,
				platformSaddleBuildAreaBoundsMultiplierTF,
				structurePickupTimeAfterPlacementTF,
				structurePickupHoldDurationTF,
				allowIntegratedSPlusStructuresCB, 
				allowHideDamageSourceFromLogsCB, 
				autoSavePeriodMinutesTF,
				maxTamedDinosTF,
				RCONServerGameLogBufferTF,
				allowHitMarkersCB, 
				allowCrateSpawnsOnTopOfStructuresCB, 
				serverPasswordTF,
				adminPasswordTF,
				spectatorPasswordTF, 
				RCONEnabledCB, 
				adminLoggingCB, 
				activeModsTF, 
				tribeLogDestroyedEnemyStructuresCB, 
				allowSharedConnectionsCB, 
				serverHardcoreCB, 
				serverPVECB, 
				allowCaveBuildingPvECB, 
				enableExtraStructurePreventionVolumesCB, 
				overrideOfficialDifficultyTF,
				difficultyOffsetTF,
				noTributeDownloadsCB, 
				preventUploadSurvivorsCB, 
				preventUploadItemsCB, 
				preventUploadDinosCB, 
				crossARKAllowForeignDinoDownloadsCB, 
				preventOfflinePvPCB, 
				preventTribeAlliancesCB, 
				preventDiseasesCB, 
				nonPermanentDiseasesCB, 
				preventSpawnAnimationsCB, 
				maxGateFrameOnSaddlesTF,
				allowTekSuitPowersInGenesisCB, 
				enableCryoSicknessPVECB, 
				maxHexagonsPerCharacterTF,
				useFjordurTraversalBuffCB, 
				globalVoiceChatCB, 
				proximityChatCB, 
				alwaysNotifyPlayerLeftCB, 
				alwaysNotifyPlayerJoinedCB, 
				serverForceNoHudCB, 
				enablePVPGammaCB, 
				disablePvEGammaCB, 
				showFloatingDamageTextCB, 
				allowFlyerCarryPVECB, 
				XPMultiplierTF,
				allowRaidDinoFeedingCB, 
				disableDinoDecayPvECB, 
				PvPDinoDecayCB,
				autoDestroyDecayedDinosCB, 
				allowMultipleAttachedC4CB, 
				maxPersonalTamedDinosTF,
				personalTamedDinosSaddleStructureCostTF,
				disableImprintDinoBuffCB, 
				allowAnyoneBabyImprintCuddleCB, 
				tamingSpeedMultiplierTF,
				harvestAmountMultiplierTF,
				useOptimizedHarvestingHealthCB, 
				clampResourceHarvestDamageCB, 
				clampItemSpoilingTimesCB, 
				disableWeatherFogCB, 
				PvPStructureDecayCB, 
				PvEAllowStructuresAtSupplyDropsCB, 
				disableStructureDecayPVECB, 
				forceAllStructureLockingCB, 
				onlyAutoDestroyCoreStructuresCB, 
				onlyDecayUnsnappedCoreStructuresCB, 
				fastDecayUnsnappedCoreStructuresCB, 
				destroyUnconnectedWaterPipesCB, 
				alwaysAllowStructurePickupCB, 
				kickIdlePlayersPeriodTF,
				preventDownloadSurvivorsCB, 
				preventDownloadItemsCB, 
				preventDownloadDinosCB, 
				maxTributeDinosTF,
				maxTributeItemsTF,
				serverAutoForceRespawnWildDinosIntervalTF,
				bFilterTribeNamesCB, 
				bFilterCharacterNamesCB, 
				bFilterChatCB, 
				allowCaveBuildingPvPCB, 
				randomSupplyCratePointsCB, 
				dontAlwaysNotifyPlayerJoinedCB, 
				ignoreLimitMaxStructuresInRangeTypeFlagCB, 
				startTimeHourTF,
				implantSuicideCDTF,
				oxygenSwimSpeedStatMultiplierTF,
				raidDinoCharacterFoodDrainMultiplierTF,
				PvEDinoDecayPeriodMultiplierTF,
				perPlatformMaxStructuresMultiplierTF,
				itemStackSizeMultiplierTF,
				maxTamedDinos_SoftTameLimitTF,
				maxTamedDinos_SoftTameLimit_CountdownForDeletionDurationprivateTF,
				startTimeOverrideCB,
				playerDamageMultiplierTF,
				playerResistanceMultiplierTF,
				playerCharacterWaterDrainMultiplierTF,
				playerCharacterFoodDrainMultiplierTF,
				playerCharacterStaminaDrainMultiplierTF,
				playerCharacterHealthRecoveryMultiplierTF,
				dinoCountMultiplierTF,
				dinoDamageMultiplierTF,
				dinoResistanceMultiplierTF,
				dinoCharacterFoodDrainMultiplierTF,
				dinoCharacterStaminaDrainMultiplierTF,
				dinoCharacterHealthRecoveryMultiplierTF,
				structureDamageMultiplierTF,
				structureResistanceMultiplierTF,
				PvEStructureDecayPeriodMultiplierTF,
				forceAllowCaveFlyersCB,
				overrideStructurePlatformPreventionCB,
				harvestHealthMultiplierTF,
				resourcesRespawnPeriodMultiplierTF,
				allowRaidDinoFeedingTF,
				onlyAllowSpecifiedEngramsCB
		};
		
		TEXTFIELDS_SESSION = new JComponent[] {
				serverNameTF,
				portTF,
				queryPortTF,
				multiHomeCB
		};
		
		TEXTFIELDS_GAMESESSION = new JComponent[] {
				maxPlayersTF
		};
		
		TEXTFIELDS_MESSAGE = new JComponent[] {
				messageTF,
				durationTF,
				messageSetterIDTF
		};
		
		TEXTFIELDS_SHOOTERGAMEMODE = new JComponent[] {
				babyImprintingStatScaleMultiplierTF,
				babyCuddleIntervalMultiplierTF,
				babyCuddleGracePeriodMultiplierTF,
				babyCuddleLoseImprintQualitySpeedMultiplierTF,
				perLevelStatsMultiplier_DinoTamedHPTF,
				perLevelStatsMultiplier_DinoTamedSTTF,
				perLevelStatsMultiplier_DinoTamedOXTF,
				perLevelStatsMultiplier_DinoTamedFDTF,
				perLevelStatsMultiplier_DinoTamedWTTF,
				perLevelStatsMultiplier_DinoTamedWETF,
				perLevelStatsMultiplier_DinoTamedDMGTF,
				perLevelStatsMultiplier_DinoTamedSPTF,
				perLevelStatsMultiplier_DinoTamedFTTF,
				perLevelStatsMultiplier_DinoTamedTRPTF,
				perLevelStatsMultiplier_DinoTamedTMPTF,
				perLevelStatsMultiplier_DinoTamed_AddHPTF,
				perLevelStatsMultiplier_DinoTamed_AddSTTF,
				perLevelStatsMultiplier_DinoTamed_AddOXTF,
				perLevelStatsMultiplier_DinoTamed_AddFDTF,
				perLevelStatsMultiplier_DinoTamed_AddWTTF,
				perLevelStatsMultiplier_DinoTamed_AddWETF,
				perLevelStatsMultiplier_DinoTamed_AddDMGTF,
				perLevelStatsMultiplier_DinoTamed_AddSPTF,
				perLevelStatsMultiplier_DinoTamed_AddFTTF,
				perLevelStatsMultiplier_DinoTamed_AddTRPTF,
				perLevelStatsMultiplier_DinoTamed_AddTMPTF,
				perLevelStatsMultiplier_DinoTamed_AffinityHPTF,
				perLevelStatsMultiplier_DinoTamed_AffinitySTTF,
				perLevelStatsMultiplier_DinoTamed_AffinityOXTF,
				perLevelStatsMultiplier_DinoTamed_AffinityFDTF,
				perLevelStatsMultiplier_DinoTamed_AffinityWTTF,
				perLevelStatsMultiplier_DinoTamed_AffinityWETF,
				perLevelStatsMultiplier_DinoTamed_AffinityDMGTF,
				perLevelStatsMultiplier_DinoTamed_AffinitySPTF,
				perLevelStatsMultiplier_DinoTamed_AffinityFTTF,
				perLevelStatsMultiplier_DinoTamed_AffinityTRPTF,
				perLevelStatsMultiplier_DinoTamed_AffinityTMPTF,
				perLevelStatsMultiplier_DinoWildHPTF,
				perLevelStatsMultiplier_DinoWildSTTF,
				perLevelStatsMultiplier_DinoWildOXTF,
				perLevelStatsMultiplier_DinoWildFDTF,
				perLevelStatsMultiplier_DinoWildWTTF,
				perLevelStatsMultiplier_DinoWildWETF,
				perLevelStatsMultiplier_DinoWildDMGTF,
				perLevelStatsMultiplier_DinoWildSPTF,
				perLevelStatsMultiplier_DinoWildFTTF,
				perLevelStatsMultiplier_DinoWildTRPTF,
				perLevelStatsMultiplier_DinoWildTMPTF,
				perLevelStatsMultiplier_PlayerHPTF,
				perLevelStatsMultiplier_PlayerSTTF,
				perLevelStatsMultiplier_PlayerOXTF,
				perLevelStatsMultiplier_PlayerFDTF,
				perLevelStatsMultiplier_PlayerWTTF,
				perLevelStatsMultiplier_PlayerWETF,
				perLevelStatsMultiplier_PlayerDMGTF,
				perLevelStatsMultiplier_PlayerSPTF,
				perLevelStatsMultiplier_PlayerFTTF,
				perLevelStatsMultiplier_PlayerTRPTF,
				perLevelStatsMultiplier_PlayerTMPTF,
				globalSpoilingTimeMultiplierTF,
				globalItemDecompositionTimeMultiplierTF,
				globalCorpseDecompositionTimeMultiplierTF,
				PvPZoneStructureDamageMultiplierTF,
				structureDamageRepairCooldownTF,
				increasePvPRespawnIntervalCheckPeriodTF,
				increasePvPRespawnIntervalMultiplierTF,
				increasePvPRespawnIntervalBaseAmountTF,
				resourceNoReplenishRadiusPlayersTF,
				resourceNoReplenishRadiusStructuresTF,
				cropGrowthSpeedMultiplierTF,
				layEggIntervalMultiplierTF,
				poopIntervalMultiplierTF,
				cropDecaySpeedMultiplierTF,
				matingIntervalMultiplierTF,
				eggHatchSpeedMultiplierTF,
				babyMatureSpeedMultiplierTF,
				babyFoodConsumptionSpeedMultiplierTF,
				dinoTurretDamageMultiplierTF,
				dinoHarvestingDamageMultiplierTF,
				playerHarvestingDamageMultiplierTF,
				customRecipeEffectivenessMultiplierTF,
				customRecipeSkillMultiplierTF,
				autoPvEStartTimeSecondsTF,
				autoPvEStopTimeSecondsTF,
				killXPMultiplierTF,
				harvestXPMultiplierTF,
				craftXPMultiplierTF,
				genericXPMultiplierTF,
				specialXPMultiplierTF,
				fuelConsumptionIntervalMultiplierTF,
				photoModeRangeLimitTF,
				bDisablePhotoModeCB,
				bIncreasePvPRespawnIntervalTF,
				bAutoPvETimerCB,
				bAutoPvEUseSystemTimeCB,
				bDisableFriendlyFireCB,
				bFlyerPlatformAllowUnalignedDinoBasingCB,
				bDisableLootCratesCB,
				bAllowCustomRecipesCB,
				bPassiveDefensesDamageRiderlessDinosCB,
				bPvEAllowTribeWarCB,
				bPvEAllowTribeWarCancelCB,
				maxDifficultyCB,
				bUseSingleplayerSettingsCB,
				bUseCorpseLocatorCB,
				bShowCreativeModeCB,
				bHardLimitTurretsInRangeCB,
				bDisableStructurePlacementCollisionCB,
				bAllowPlatformSaddleMultiFloorsCB,
				bAllowUnlimitedRespecsCB,
				bDisableDinoRidingCB,
				bDisableDinoTamingCB,
				overrideMaxExperiencePointsPlayerTF,
				overrideMaxExperiencePointsDinoTF,
				maxNumberOfPlayersInTribeTF,
				explorerNoteXPMultiplierTF,
				bossKillXPMultiplierTF,
				alphaKillXPMultiplierTF,
				wildKillXPMultiplierTF,
				caveKillXPMultiplierTF,
				tamedKillXPMultiplierTF,
				unclaimedKillXPMultiplierTF,
				supplyCrateLootQualityMultiplierTF,
				fishingLootQualityMultiplierTF,
				craftingSkillBonusMultiplierTF,
				bAllowSpeedLevelingCB,
				bAllowFlyerSpeedLevelingCB,
		};
		
		setVisible( true );
	}
	
	/**
	 * ウィンドウ初期設定
	 */
	void init() {
		
		setLocation( 100, 100 );
		setSize( windowSize[0], windowSize[1] );
		setDefaultCloseOperation( 2 );
		// 勝手にウィンドウのサイズをいじられるとレイアウトがズレるため
		setResizable( false );
		ImageIcon ii = new ImageIcon( JavasSystem.CURRENT_DIRECTORY + "\\images\\dodo.png" );
		setIconImage( ii.getImage() );
		// ウィンドウを閉じる時はシステムも終了するようにする
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing( WindowEvent e ) {
				System.exit( 1 );
			}
		});
	}
	
	/**
	 * 基本的なレイアウト
	 * レイアウトは大まかに三つ
	 * 上から プロファイル メイン ログ のパネルになっている
	 * メニューとメインの比率は3.3:6.6になるようにする
	 */
	void baseLayout() {
		
		base = new JPanel();
		base.setBackground( Panel.CREAM );
		base.setLayout( new GridBagLayout() );
		getContentPane().add( base );
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		menu = new Panel( Panel.CREAM, new GridLayout( 5, 1 ) );
		gbc.gridx = 0;
		gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1;
		base.add( menu, gbc );
		
		clayout = new CardLayout();
		mainPanel = new JPanel();
		mainPanel.setLayout( clayout );
		gbc.gridx = 0;
		gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 12;
		base.add( mainPanel, gbc );
		
		mainLayout();
		
		profileLayout();		// プロファイルのパネルのレイアウト
		serverConfigLayout();
		
		// 他のレイアウトもクラスごとに分けてソースコードを短くする予定
		// コンフィグ切り替えボタンのレイアウト
		configButtonsPanel = new ConfigButtonsLayout( clayout, mainPanel );
		menu.add( configButtonsPanel );
	}

	/**
	 * プロファイル名とプロファイル保存ボタンとサーバーデータインストールボタンの配置を決めるレイアウト
	 */
	void profileLayout() {
		
		// プロファイル欄の元のパネル
		profilePanel = new JPanel();
		profilePanel.setLayout( new GridLayout( 1, 2 ) );
		menu.add( profilePanel );
		
		// プロファイルラベルとテキストフィールド
		JPanel textFieldLabel = new Panel( new Color( 255, 255, 200 ), new FlowLayout( FlowLayout.LEFT ) );
		profilePanel.add( textFieldLabel );
		
		JLabel profLabel = new JLabel("プロファイル名　　");
		textFieldLabel.add( profLabel );
		
		profTF = new TextField( true, new Dimension( 454, 30 ) );
		textFieldLabel.add( profTF );
		
		JPanel btns = new Panel( new Color( 255, 255, 200 ), new FlowLayout( FlowLayout.CENTER ) );
		profilePanel.add( btns );
		
		/**
		 * プロファイル保存ボタン
		 */
		JButton saveProfileButton = new Button( "保存", new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {		// プロファイルを記録する処理
				if ( gusBuilder != null || gBuilder != null ) {
					modifySettings();
					gusBuilder.applySettings();	
				}else {
					console.print( "プロファイルがロードされていません" );
				}
			}
		});
		
		/**
		 * プロファイルロードボタン
		 */
		JButton loadProfileButton = new Button( "ロード", new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {		// プロファイルを読み込む処理
				loadSettings();
			}
		});
		
		/**
		 * サーバーデータインストールボタン
		 */
		JButton installServerButton = new Button( "サーバーをインストール/アップデート", new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {		// プロファイルを新規で作成する処理
				if ( profTF.getText().equals( "" ) ) {
					console.print( "プロファイル名が入力されていないのでインストールできません" );
					return;
				}else {
					ConsoleProcess.createProfile( profTF.getText() );
					loadSettings();
				}
			}
		});
		
		JavasSystem.addAll( btns, new JButton[] { saveProfileButton, loadProfileButton, installServerButton } );
	}
	
	/**
	 * サーバー名やパスワードなどの基本的な設定入力欄のレイアウトを作成
	 */
	void serverConfigLayout() {
		
		/**
		 * パネル1 : サーバー名とMod欄をレイアウトしている
		 */
		// サーバー名
		serverNameTF = new TextField( true, new Dimension( 454, 30 ) );
		JLabel serverNameLabel = new JLabel( "サーバー名　　　　" );
		
		// Mod ID
		modTF = new TextField( true, new Dimension( 550, 30 ) );
		JLabel modLabel = new JLabel( "新規Mod ID" );
		
		menuPanel1 = new Panel( new Component[] { 
				new Panel( FlowLayout.LEFT, new Component[] { serverNameLabel, 	serverNameTF 	} ), 
				new Panel( FlowLayout.LEFT, new Component[] { modLabel, 		modTF 			} ) });
		/**
		 * パネル2 : サーバーパスワード、管理者パスワード、マップ名
		 */
		// マップ
		mapTF = new TextField( true, new Dimension( 450, 30 ) );
		mapTF.setText( "TheIsland_WP" );
		JLabel mapLabel = new JLabel( "マップ名" );

		// サーバーパスワード
		serverPasswordTF 		= new TextField( true, new Dimension( 150, 30 ) );
		JLabel serverPassLabel 	= new JLabel( "サーバーパスワード" );
		
		// 管理者パスワード
		adminPasswordTF 		= new TextField( true, new Dimension( 150, 30 ) );
		JLabel adminPassLabel 	= new JLabel( "管理者パスワード" );

		// パスワードパネルとマップパネル
		menuPanel2 = new Panel( new Component[] { 
				new Panel( new Component[] { 
						new Panel( FlowLayout.LEFT, new Component[] { serverPassLabel,	serverPasswordTF } ), 
						new Panel( FlowLayout.LEFT, new Component[] { adminPassLabel, adminPasswordTF } ) } ), 
				new Panel( FlowLayout.LEFT, new Component[] { mapLabel, mapTF } )	 } );
		
		/**
		 * パネル3 : ポート、クエリポート、サーバー開始ボタン
		 */
		// ポートのラベルと入力欄
		JLabel portLabel = new JLabel( "ポート番号　　　　" );
		portTF = new TextField( true );
		portTF.setPreferredSize( new Dimension( 150, 30 ) );
		portTF.setText( "7777" );
		
		// クエリポートのラベルと入力欄
		JLabel qportLabel = new JLabel( "クエリポート番号" );
		queryPortTF = new TextField( true );
		queryPortTF.setPreferredSize( new Dimension( 150, 30 ) );
		queryPortTF.setText( "27015" );
		
		// サーバー開始ボタン
		JButton button = new Button( "サーバーを開始", new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				ConsoleProcess.startServer( 
						profTF.getText(), 
						mapTF.getText(), 
						activeModsTF.getText() + "," + modTF.getText()
				);
			}
		});
		menuPanel3 = new Panel( new Component[] { 
				new Panel( new Component[] { 
						new Panel( FlowLayout.LEFT, new Component[] { portLabel, 	portTF 		} ), 
						new Panel( FlowLayout.LEFT, new Component[] { qportLabel, 	queryPortTF } ) } ), 
				new Panel( FlowLayout.RIGHT, new Component[] { button } ) } ); 

		// メニューパネルに三つのパネルを合体
		JavasSystem.addAll( menu, new JPanel[]{ menuPanel1, menuPanel2, menuPanel3 } );
	}
	
	/**
	 * メインのレイアウト
	 * それぞれのコンフィグ切り替えボタンを押すとChangePanelLayoutで
	 * 切り替えるようにする
	 */
	void mainLayout() {
		
		mainPanel.setBackground( Color.green );
		
		serverSettingsLayout();
		playerSettingsLayout();
		dinoSettingsLayout();
		structureSettingsLayout();
		worldSettingsLayout();
		ruleSettingsLayout();
		PvESettingsLayout();
		PvPSettingsLayout();
		worldDetailSettingsLayout();
		dinoWildSettingsLayout();
		tamedDinoSettingsLayout();
		playerDetailedSettingsLayout();
		experienceSettingsLayout();
		otherSettingsLayout();
		
		clayout.first( mainPanel );
	}
	
	/**
	 * サーバー用設定の画面のレイアウトを作成
	 * ゲーム内設定に存在しない設定を基本的にここに入れる
	 */
	void serverSettingsLayout() {
		
		serverSettingsPanel = new ScrollPanel();
		serverSettingsPanel.getScrollPane().setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		
		// RCON有効化
		RCONEnabledCB = new JCheckBox( "RCONを有効化" );
		Panel RCONEnabledPanel = new Panel( RCONEnabledCB, false );
		
		// RCONポート
		RCONPortTF = new JTextField( 8 );
		Panel RCONPortPanel = new Panel( RCONPortTF, "RCONポート" );
		
		// RCONログ転送速度
		RCONServerGameLogBufferTF = new JTextField( 10 );
		JPanel RCONServerGameLogBufferPanel = new Panel( 
				RCONServerGameLogBufferTF,
				"RCONサーバーにゲームログを送信する際の速度",
				new double[] { 0.65, 0.35 }
		);
		
		// RCON関係
		JPanel RCONPanel = new Panel( 
				"<html>RCON関係<br>　(よくわからなければ触らなくていい)</html>", 
				17, 
				Panel.CREAM, 
				new int[] { 2, 2 },
				new int[] { windowSize[0] - 200, 300 }, 
				new int[] { 1, 1, 0, 1 }, 
				new Component[] { 
						RCONEnabledPanel,
						RCONPortPanel,
						RCONServerGameLogBufferPanel
				} );
		RCONPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		
		activeModsTF = new JTextField( 40 );
		Panel activeModsPanel = new Panel( activeModsTF, "アクティブのMod (ID表示なのでどれがどのModかは自分で確認してください)" );
		
		JPanel modsPanel = new Panel(
				"Mod関係",
				17,
				Panel.CREAM,
				new int[] { 1, 1 },
				new int[] { windowSize[0] - 200, 300 },
				new int[] { 1, 1, 0, 1 },
				new Component[] {
						activeModsPanel
				} );
		modsPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		
		tribeLogDestroyedEnemyStructuresCB = new JCheckBox( "トライブメンバーが破壊した敵の構造物のログの表示許可" );
		Panel tribeLogDestroyedEnemyStructuresPanel = new Panel( tribeLogDestroyedEnemyStructuresCB, false );
		
		allowHideDamageSourceFromLogsCB = new JCheckBox( "トライブログでダメージソースの非表示" );
		Panel allowHideDamageSourceFromLogsPanel = new Panel( allowHideDamageSourceFromLogsCB, false );
		
		tribeNameChangeCooldownTF = new JTextField( 10 );
		Panel tribeNameChangeCooldownPanel = new Panel( tribeNameChangeCooldownTF, "トライブ名変更のクールタイム" );

		// トライブ関係
		JPanel tribePanel = new Panel( 
				"トライブ関係", 
				17, 
				Panel.CREAM, 
				new int[] { 2, 2 },
				new int[] { windowSize[0] - 200, 300 }, 
				new int[] { 1, 1, 0, 1 }, 
				new Component[] { 
						tribeLogDestroyedEnemyStructuresPanel,
						allowHideDamageSourceFromLogsPanel,
						tribeNameChangeCooldownPanel
				} );
		tribePanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		
		// 構造物を設置した後の拾えなくなるまでの秒数
		structurePickupTimeAfterPlacementTF = new JTextField( 10 );
		Panel structurePickupTimeAfterPlacementPanel = new Panel( 
				structurePickupTimeAfterPlacementTF, 
				"<html>構造物を設置したときに拾えなくなるまでの時間(秒)<br>今は常に拾える用になっているからいらない</html>",
				new double[] { 0.7, 0.3 }
		);
		
		// 構造物を拾うのにかかる時間
		structurePickupHoldDurationTF = new JTextField( 10 );
		Panel structurePickupHoldDurationPanel = new Panel( structurePickupHoldDurationTF, "構造物を拾うのに必要な時間(秒)" );
		
		// Tekストレージの設置許可
		allowIntegratedSPlusStructuresCB = new JCheckBox( "TEKストレージの設置許可" );
		Panel allowIntegratedSPlusStructuresPanel = new Panel( allowIntegratedSPlusStructuresCB, false );
		
		ignoreLimitMaxStructuresInRangeTypeFlagCB = new JCheckBox( "建築上限を無視して建築を可能にする設定" );
		Panel ignoreLimitMaxStructuresInRangeTypeFlagPanel = new Panel( ignoreLimitMaxStructuresInRangeTypeFlagCB, false );
		
		platformSaddleBuildAreaBoundsMultiplierTF = new JTextField( 10 );
		Panel platformSaddleBuildAreaBoundsMultiplierPanel = new Panel( 
				platformSaddleBuildAreaBoundsMultiplierTF, 
				"プラットフォーム上での建築可能範囲の設定",
				new double[] { 0.7, 0.3 } 
		);
		
		maxGateFrameOnSaddlesTF = new JTextField( 10 );
		Panel maxGateFrameOnSaddlesPanel = new Panel( 
				maxGateFrameOnSaddlesTF, 
				"<html>プラットフォームサドル上に<br>設置できる恐竜ゲートの数の設定</html>",
				new double[] { 0.7, 0.3}
		);
		
		allowCrateSpawnsOnTopOfStructuresCB = new JCheckBox("構造物の上でのクレート召喚の許可");
		Panel allowCrateSpawnsOnTopOfStructuresPanel = new Panel( allowCrateSpawnsOnTopOfStructuresCB, false );
		
		theMaxStructuresInRangeTF = new JTextField( 10 );
		Panel theMaxStructuresInRangePanel = new Panel( 
				theMaxStructuresInRangeTF, 
				"特定の範囲内で建築できる建造物の最大数",
				new double[] { 0.7, 0.3 }
		);
		
		forceAllStructureLockingCB = new JCheckBox( "デフォルトですべての建築物がロックされる設定" );
		Panel forceAllStructureLockingPanel = new Panel( forceAllStructureLockingCB, false );
		
		onlyAutoDestroyCoreStructuresCB = new JCheckBox( "構造物のコアの自然消滅の許可" );
		Panel onlyAutoDestroyCoreStructuresPanel = new Panel( onlyAutoDestroyCoreStructuresCB, false );
		
		onlyDecayUnsnappedCoreStructuresCB = new JCheckBox( "コアのみの構造物の自然消滅の許可" );
		Panel onlyDecayUnsnappedCoreStructuresPanel = new Panel( onlyDecayUnsnappedCoreStructuresCB, false );
		
		fastDecayUnsnappedCoreStructuresCB = new JCheckBox( "コアのみの構造物を5倍の速度で自然消滅させる設定" );
		Panel fastDecayUnsnappedCoreStructuresPanel = new Panel( fastDecayUnsnappedCoreStructuresCB, false );
		
		destroyUnconnectedWaterPipesCB = new JCheckBox( "水源に接続されていないパイプの自然消滅の許可" );
		Panel destroyUnconnectedWaterPipesPanel = new Panel( destroyUnconnectedWaterPipesCB, false );
		
		alwaysAllowStructurePickupCB = new JCheckBox( "構造物を常に拾えるようにする" );
		Panel alwaysAllowStructurePickupPanel = new Panel( alwaysAllowStructurePickupCB, false );
		
		personalTamedDinosSaddleStructureCostTF = new JTextField( 10 );
		Panel personalTamedDinosSaddleStructureCostPanel = new Panel( 
				personalTamedDinosSaddleStructureCostTF, 
				"プラットフォームサドルに建築した恐竜の数？",
				new double[] { 0.7, 0.3 }
		);
		
		// 構造物関係
		JPanel structurePanel = new Panel( 
				"建築関係", 
				17, 
				Panel.CREAM, 
				new int[] { 8, 2 },
				new int[] { windowSize[0] - 200, 300 }, 
				new int[] { 1, 1, 0, 1 }, 
				new Component[] { 
						alwaysAllowStructurePickupPanel,
						structurePickupTimeAfterPlacementPanel,
						structurePickupHoldDurationPanel,
						allowIntegratedSPlusStructuresPanel,
						ignoreLimitMaxStructuresInRangeTypeFlagPanel,
						platformSaddleBuildAreaBoundsMultiplierPanel,
						maxGateFrameOnSaddlesPanel,
						personalTamedDinosSaddleStructureCostPanel,
						theMaxStructuresInRangePanel,
						allowCrateSpawnsOnTopOfStructuresPanel,
						forceAllStructureLockingPanel,
						onlyAutoDestroyCoreStructuresPanel,
						onlyDecayUnsnappedCoreStructuresPanel,
						fastDecayUnsnappedCoreStructuresPanel,
						destroyUnconnectedWaterPipesPanel
				} );
		structurePanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		
		maxTamedDinosTF = new JTextField( 10 );
		Panel maxTamedDinosPanel = new Panel( maxTamedDinosTF, "恐竜のテイム上限数" );

		overrideOfficialDifficultyTF = new JTextField( 10 );
		Panel overrideOfficialDifficultyPanel = new Panel( overrideOfficialDifficultyTF, "野生恐竜の出現レベルの倍率" );
		
		enableCryoSicknessPVECB = new JCheckBox( "クライオシックネスの許可" );
		Panel enableCryoSicknessPVEPanel = new Panel( enableCryoSicknessPVECB, false );
		
		autoDestroyDecayedDinosCB = new JCheckBox( "恐竜の所有権が失効された場合に自動で恐竜を消すかどうかの設定" );
		Panel autoDestroyDecayedDinosPanel = new Panel( autoDestroyDecayedDinosCB, false );
		
		allowMultipleAttachedC4CB = new JCheckBox( "恐竜ごとに複数のC4をつけれるようにする設定" );
		Panel allowMultipleAttachedC4Panel = new Panel( allowMultipleAttachedC4CB, false );
		
		maxPersonalTamedDinosTF = new JTextField( 10 );
		Panel maxPersonalTamedDinosPanel = new Panel( maxPersonalTamedDinosTF, "個人がテイムできる恐竜の数" );
		
		serverAutoForceRespawnWildDinosIntervalTF = new JTextField( 10 );
		Panel serverAutoForceRespawnWildDinosIntervalPanel = new Panel( serverAutoForceRespawnWildDinosIntervalTF, "野生恐竜を定期的に初期化する時間(秒)" );
		
		maxTamedDinos_SoftTameLimitTF = new JTextField( 10 );
		Panel maxTamedDinos_SoftTameLimitPanel = new Panel( maxTamedDinos_SoftTameLimitTF, "ソフトテイム上限の設定" );
		
		maxTamedDinos_SoftTameLimit_CountdownForDeletionDurationprivateTF = new JTextField( 10 );
		Panel maxTamedDinos_SoftTameLimit_CountdownForDeletionDurationprivatePanel = new Panel( maxTamedDinos_SoftTameLimit_CountdownForDeletionDurationprivateTF, "ソフトテイムの削除期間の設定" );
		
		// 恐竜関係
		JPanel dinoPanel = new Panel( 
				"恐竜関係", 
				17, 
				Panel.CREAM, 
				new int[] { 5, 2 },
				new int[] { windowSize[0] - 200, 300 }, 
				new int[] { 1, 1, 0, 1 }, 
				new Component[] { 
						maxTamedDinosPanel,
						maxPersonalTamedDinosPanel,
						overrideOfficialDifficultyPanel,
						serverAutoForceRespawnWildDinosIntervalPanel,
						enableCryoSicknessPVEPanel,
						autoDestroyDecayedDinosPanel,
						allowMultipleAttachedC4Panel,
						maxTamedDinos_SoftTameLimitPanel,
						maxTamedDinos_SoftTameLimit_CountdownForDeletionDurationprivatePanel
				} );
		dinoPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		
		crossARKAllowForeignDinoDownloadsCB = new JCheckBox( "一部マップでの一部恐竜のダウンロード許可" );
		Panel crossARKAllowForeignDinoDownloadsPanel = new Panel( crossARKAllowForeignDinoDownloadsCB, false );
		
		maxTributeDinosTF = new JTextField( 10 );
		Panel maxTributeDinosPanel = new Panel( maxTributeDinosTF, "転送可能最大恐竜数" );
		
		maxTributeItemsTF = new JTextField( 10 );
		Panel maxTributeItemsPanel = new Panel( maxTributeItemsTF, "転送可能最大アイテム数" );
		
		// 転送関係
		JPanel tpPanel = new Panel( 
				"転送関係", 
				17, 
				Panel.CREAM, 
				new int[] { 2, 2 },
				new int[] { windowSize[0] - 200, 300 }, 
				new int[] { 1, 1, 0, 1 }, 
				new Component[] { 
						crossARKAllowForeignDinoDownloadsPanel,
						new Panel( Panel.CREAM ),
						maxTributeDinosPanel,
						maxTributeItemsPanel
				} );
		tpPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		
		// プレイヤー位置をマップに表示
		maxPlayersTF = new JTextField( 10 );
		Panel maxPlayersPanel = new Panel( maxPlayersTF, "サーバー最大人数" );
		
		showMapPlayerLocationCB = new JCheckBox( "プレイヤーの位置をマップに表示" );
		Panel showMapPlayerLocationPanel = new Panel( showMapPlayerLocationCB, false );
		
		itemStackSizeMultiplierTF = new JTextField( 10 );
		Panel itemStackSizeMultiplierPanel = new Panel( itemStackSizeMultiplierTF, "アイテムのスタック数の倍率" );
		
		bFilterTribeNamesCB = new JCheckBox( "良くない名前のトライブ名を非表示にする設定" );
		Panel bFilterTribeNamesPanel = new Panel( bFilterTribeNamesCB, false );
		
		bFilterCharacterNamesCB = new JCheckBox( "良くない名前のサバイバーの名前を非表示にする設定" );
		Panel bFilterCharacterNamesPanel = new Panel( bFilterCharacterNamesCB, false );
		
		bFilterChatCB = new JCheckBox( "良くないチャットを非表示にする設定" );
		Panel bFilterChatPanel = new Panel( bFilterChatCB, false );
		
		randomSupplyCratePointsCB = new JCheckBox( "クレートの位置をランダムにする設定" );
		Panel randomSupplyCratePointsPanel = new Panel( randomSupplyCratePointsCB, false );
		
		dontAlwaysNotifyPlayerJoinedCB = new JCheckBox( "プレイヤーの参加を常に通知しないようにする設定" );
		Panel dontAlwaysNotifyPlayerJoinedPanel = new Panel( dontAlwaysNotifyPlayerJoinedCB, false );
		
		implantSuicideCDTF = new JTextField( 10 );
		Panel implantSuicideCDPanel = new Panel( 
				implantSuicideCDTF,
				"インプラント自滅のクールダウンの時間",
				new double[] { 0.7, 0.3 }
		);
		
		kickIdlePlayersPeriodTF = new JTextField( 10 );
		Panel kickIdlePlayersPeriodPanel = new Panel( kickIdlePlayersPeriodTF, "<html>未操作プレイヤーを蹴るまでの時間(秒)<br>(全ロス対策)</html>" );
		
		useOptimizedHarvestingHealthCB = new JCheckBox( "<html>レア度の低いアイテムが出やすくなって<br>レア度の高いアイテムが出にくくなる設定</html>" );
		Panel useOptimizedHarvestingHealthPanel = new Panel( useOptimizedHarvestingHealthCB, false );
		
		clampResourceHarvestDamageCB = new JCheckBox( "ダメージ量に応じて資源の獲得量の変更許可" );
		Panel clampResourceHarvestDamagePanel = new Panel( clampResourceHarvestDamageCB, false );
		
		clampItemSpoilingTimesCB = new JCheckBox( "腐敗時間の統合許可" );
		Panel clampItemSpoilingTimesPanel = new Panel( clampItemSpoilingTimesCB, false );
		
		disableWeatherFogCB = new JCheckBox( "霧の表示許可" );
		Panel disableWeatherFogPanel = new Panel( disableWeatherFogCB, false );
		
		maxHexagonsPerCharacterTF = new JTextField( 10 );
		Panel maxHexagonsPerCharacterPanel = new Panel( maxHexagonsPerCharacterTF, "<html>1キャラクターあたりの<br>ヘキサゴンの最大所持数</html>" );
		
		useFjordurTraversalBuffCB = new JCheckBox( "フィヨルドでのテレポート許可" );
		Panel useFjordurTraversalBuffPanel = new Panel( useFjordurTraversalBuffCB, false );
		
		allowTekSuitPowersInGenesisCB = new JCheckBox( "ジェネシスでのTEKスーツ使用許可" );
		Panel allowTekSuitPowersInGenesisPanel = new Panel( allowTekSuitPowersInGenesisCB, false );

		preventSpawnAnimationsCB = new JCheckBox( "スポーン時のアニメーション(手首掻くやつ)の許可" );
		Panel preventSpawnAnimationsPanel = new Panel( preventSpawnAnimationsCB, false );
		
		spectatorPasswordTF = new JTextField( 10 );
		Panel spectatorPasswordPanel = new Panel( 
				spectatorPasswordTF, 
				"スペクテイターモードになるためのパスワード",
				new double[] { 0.7, 0.3 }
		);
		
		autoSavePeriodMinutesTF = new JTextField( 10 );
		Panel autoSavePeriodMinutesPanel = new Panel( autoSavePeriodMinutesTF, "オートセーブ間隔(分)" );
		
		allowHitMarkersCB = new JCheckBox( "ヒットマーカーの表示許可設定" );
		Panel allowHitMarkersPanel = new Panel( allowHitMarkersCB, false );
		
		// その他関係
		JPanel otherPanel = new Panel( 
				"その他関係", 
				17, 
				Panel.CREAM, 
				new int[] { 12, 2 },
				new int[] { windowSize[0] - 200, 300 }, 
				new int[] { 1, 1, 0, 1 }, 
				new Component[] { 
						spectatorPasswordPanel,
						new Panel( Panel.CREAM ),
						maxPlayersPanel,
						new Panel( Panel.CREAM ),
						showMapPlayerLocationPanel,
						autoSavePeriodMinutesPanel,
						implantSuicideCDPanel,
						itemStackSizeMultiplierPanel,
						randomSupplyCratePointsPanel,
						dontAlwaysNotifyPlayerJoinedPanel,
						kickIdlePlayersPeriodPanel,
						clampResourceHarvestDamagePanel,
						useOptimizedHarvestingHealthPanel,
						clampItemSpoilingTimesPanel,
						disableWeatherFogPanel,
						allowHitMarkersPanel,
						maxHexagonsPerCharacterPanel,
						allowTekSuitPowersInGenesisPanel,
						useFjordurTraversalBuffPanel,
						preventSpawnAnimationsPanel,
						bFilterTribeNamesPanel,
						bFilterCharacterNamesPanel,
						bFilterChatPanel
				} );
		otherPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		
		messageTF = new JTextField( 10 );
		Panel messagePanel = new Panel( messageTF, "メッセージ" );

		durationTF = new JTextField( 10 );
		Panel durationPanel = new Panel( 
				durationTF, 
				"メッセージを表示する時間(秒)",
				new double[] { 0.7, 0.3 }
		);

		messageSetterIDTF = new JTextField( 10 );
		Panel messageSetterIDPanel = new Panel( messageSetterIDTF, "メッセージを設定した人のID" );

		JPanel messagesPanel = new Panel( 
				"メッセージ関係", 
				17, 
				Panel.CREAM, 
				new int[] { 3, 1 },
				new int[] { windowSize[0] - 200, 300 }, 
				new int[] { 1, 1, 0, 1 }, 
				new Component[] { 
						messagePanel,
						durationPanel,
						messageSetterIDPanel
				} );
		messagesPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		
		// 共有接続-わからない、何の設定だろう...
		allowSharedConnectionsCB = new JCheckBox( "共有接続を許可" );
		Panel allowSharedConnectionsPanel = new Panel( allowSharedConnectionsCB, false );
		
		// よくわからない関係
		JPanel idkPanel = new Panel( 
				"よくわからない関係 (基本的に触らなくていい)", 
				17,
				Panel.CREAM,
				new int[] { 1, 1 },
				new int[] { windowSize[0] - 200, 80 }, 
				new int[] { 1, 1, 1, 1 }, 
				new Component[] { 
						allowSharedConnectionsPanel,
				} );
		idkPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		
		JavasSystem.addAll( serverSettingsPanel, new Component[] {
				RCONPanel,
				modsPanel,
				tribePanel,
				structurePanel,
				dinoPanel,
				tpPanel,
				otherPanel,
				messagesPanel,
				idkPanel
		});
		
		mainPanel.add( "server", serverSettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 プレイヤー のレイアウト
	 */
	void playerSettingsLayout() {
		
		playerSettingsPanel = new ScrollPanel();
		
		// ダメージ
		playerDamageMultiplierTF = new JTextField( 10 );
		Panel playerDamageMultiplierPanel = new Panel( playerDamageMultiplierTF, "ダメージ(攻撃力)" );
		
		// 耐性
		playerResistanceMultiplierTF = new JTextField( 10 );
		Panel playerResistanceMultiplierPanel = new Panel( playerResistanceMultiplierTF, "耐性(防御力)" );
		
		// 水分消費速度
		playerCharacterWaterDrainMultiplierTF = new JTextField( 10 );
		Panel playerCharacterWaterDrainMultiplierPanel = new Panel( playerCharacterWaterDrainMultiplierTF, "水分消費速度" );
		
		// 食料消費速度
		playerCharacterFoodDrainMultiplierTF = new JTextField( 10 );
		Panel playerCharacterFoodDrainMultiplierPanel = new Panel( playerCharacterFoodDrainMultiplierTF, "食料消費速度" );
		
		// スタミナ減少
		playerCharacterStaminaDrainMultiplierTF = new JTextField( 10 );
		Panel playerCharacterStaminaDrainMultiplierPanel = new Panel( playerCharacterStaminaDrainMultiplierTF, "スタミナ減少" );
		
		// 体力回復
		playerCharacterHealthRecoveryMultiplierTF = new JTextField( 10 );
		Panel playerCharacterHealthRecoveryMultiplierPanel = new Panel( playerCharacterHealthRecoveryMultiplierTF, "体力回復" );
		
		// 採取
		playerHarvestingDamageMultiplierTF = new JTextField( 10 );
		Panel playerHarvestingDamageMultiplierPanel = new Panel( playerHarvestingDamageMultiplierTF, "採取効率" );

		// 酸素値に応じた泳ぐ速度の倍率
		oxygenSwimSpeedStatMultiplierTF = new JTextField( 10 );
		Panel oxygenSwimSpeedStatMultiplierPanel = new Panel( oxygenSwimSpeedStatMultiplierTF, "酸素値に応じた泳ぐ速度" );
		
		JPanel playerPanel = new Panel( 
				"プレイヤー設定", 
				17, 
				Panel.CREAM, 
				new int[] { 8, 1 },
				new int[] { windowSize[0] - 900, 400 }, 
				new int[] { 1, 1, 1, 1 }, 
				new Component[] {
						playerDamageMultiplierPanel,
						playerResistanceMultiplierPanel,
						playerCharacterWaterDrainMultiplierPanel,
						playerCharacterFoodDrainMultiplierPanel,
						playerCharacterStaminaDrainMultiplierPanel,
						playerCharacterHealthRecoveryMultiplierPanel,
						playerHarvestingDamageMultiplierPanel,
						oxygenSwimSpeedStatMultiplierPanel,
				} );
		playerSettingsPanel.add( playerPanel );
		
		mainPanel.add( "player", playerSettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 恐竜 のレイアウト
	 */
	void dinoSettingsLayout() {
		
		dinoSettingsPanel = new ScrollPanel();
		
		// 最大カウント
		dinoCountMultiplierTF = new JTextField( 10 );
		Panel dinoCountMultiplierPanel = new Panel( dinoCountMultiplierTF, "ワールドの恐竜の数(0～1)" );
		
		// ダメージ
		dinoDamageMultiplierTF = new JTextField( 10 );
		Panel dinoDamageMultiplierPanel = new Panel( dinoDamageMultiplierTF, "ダメージ" );
		
		// 耐性
		dinoResistanceMultiplierTF = new JTextField( 10 );
		Panel dinoResistanceMultiplierPanel = new Panel( dinoResistanceMultiplierTF, "耐性" );
		
		// 食料消費速度
		dinoCharacterFoodDrainMultiplierTF = new JTextField( 10 );
		Panel dinoCharacterFoodDrainMultiplierPanel = new Panel( dinoCharacterFoodDrainMultiplierTF, "食料消費速度" );

		// スタミナ減少
		dinoCharacterStaminaDrainMultiplierTF = new JTextField( 10 );
		Panel dinoCharacterStaminaDrainMultiplierPanel = new Panel( dinoCharacterStaminaDrainMultiplierTF, "スタミナ減少" );
		
		// 体力回復
		dinoCharacterHealthRecoveryMultiplierTF = new JTextField( 10 );
		Panel dinoCharacterHealthRecoveryMultiplierPanel = new Panel( dinoCharacterHealthRecoveryMultiplierTF, "体力回復" );
		
		// 採取
		dinoHarvestingDamageMultiplierTF = new JTextField( 10 );
		Panel dinoHarvestingDamageMultiplierPanel = new Panel( dinoHarvestingDamageMultiplierTF, "採取効率" );
		
		// タレットによる被ダメージ
		dinoTurretDamageMultiplierTF = new JTextField( 10 );
		Panel dinoTurretDamageMultiplierPanel = new Panel( dinoTurretDamageMultiplierTF, "タレット被ダメージ倍率" );
		
		// テイムを無効化
		bDisableDinoTamingCB = new JCheckBox( "テイムを無効化" );
		Panel bDisableDinoTamingPanel = new Panel( bDisableDinoTamingCB, false );
		
		// 騎乗を無効化
		bDisableDinoRidingCB = new JCheckBox( "騎乗を無効化" );
		Panel bDisableDinoRidingPanel = new Panel( bDisableDinoRidingCB, false );

		JPanel dinoPanel = new Panel( 
				"恐竜設定", 
				17, 
				Panel.CREAM, 
				new int[] { 10, 1 },
				new int[] { windowSize[0] - 900, 550 }, 
				new int[] { 1, 1, 1, 1 }, 
				new Component[] {
						dinoCountMultiplierPanel,
						dinoDamageMultiplierPanel,
						dinoResistanceMultiplierPanel,
						dinoCharacterFoodDrainMultiplierPanel,
						dinoCharacterStaminaDrainMultiplierPanel,
						dinoCharacterHealthRecoveryMultiplierPanel,
						dinoHarvestingDamageMultiplierPanel,
						dinoTurretDamageMultiplierPanel,
						bDisableDinoTamingPanel,
						bDisableDinoRidingPanel
				} );
		dinoSettingsPanel.add( dinoPanel );
		
		mainPanel.add( "dino", dinoSettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 構造物 のレイアウト
	 */
	void structureSettingsLayout() {
		
		structureSettingsPanel = new ScrollPanel();
		
		// ダメージ
		structureDamageMultiplierTF = new JTextField( 10 );
		Panel structureDamageMultiplierPanel = new Panel( structureDamageMultiplierTF, "ダメージ" );

		// 耐性
		structureResistanceMultiplierTF = new JTextField( 10 );
		Panel structureResistanceMultiplierPanel = new Panel( structureResistanceMultiplierTF, "耐性" );

		// ダメージ修復不可時間
		structureDamageRepairCooldownTF = new JTextField( 10 );
		Panel structureDamageRepairCooldownPanel = new Panel( structureDamageRepairCooldownTF, "ダメージの修復時間" );
		
		// 建造物設置時のコリジョン制限を無効化
		bDisableStructurePlacementCollisionCB = new JCheckBox( "建造物設置時のコリジョン制限を無効化" );
		Panel bDisableStructurePlacementCollisionPanel = new Panel( bDisableStructurePlacementCollisionCB, false );
		
		// プラットフォーム上に置かれた土台だけが土台として機能する様にする
		bAllowPlatformSaddleMultiFloorsCB = new JCheckBox( "<html>プラットフォーム上に置かれた<br>土台だけが土台として機能する様にする</html>" );
		Panel bAllowPlatformSaddleMultiFloorsPanel = new Panel( bAllowPlatformSaddleMultiFloorsCB, false );
		
		// タレットの設置数が制限を超えた際に超えた分のタレットを停止する
		bHardLimitTurretsInRangeCB = new JCheckBox( "<html>タレットの設置数が制限を超えた際に<br>超えた分のタレットを停止する</html>" );
		Panel bHardLimitTurretsInRangePanel = new Panel( bHardLimitTurretsInRangeCB, false );
		
		JPanel structurePanel = new Panel( 
				"建造物", 
				17, 
				Panel.CREAM, 
				new int[] { 6, 1 },
				new int[] { windowSize[0] - 900, 350 }, 
				new int[] { 1, 1, 1, 1 }, 
				new Component[] {
						structureDamageMultiplierPanel,
						structureResistanceMultiplierPanel,
						structureDamageRepairCooldownPanel,
						bDisableStructurePlacementCollisionPanel,
						bAllowPlatformSaddleMultiFloorsPanel,
						bHardLimitTurretsInRangePanel
				} );
		structureSettingsPanel.add( structurePanel );
		
		mainPanel.add( "structure", structureSettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 ワールド のレイアウト
	 */
	void worldSettingsLayout() {
		
		worldSettingsPanel = new ScrollPanel();
		
		// 経験値倍率
		XPMultiplierTF = new JTextField( 10 );
		Panel XPMultiplierPanel = new Panel( XPMultiplierTF, "全体の経験値倍率" );
		
		// テイム速度倍率
		tamingSpeedMultiplierTF = new JTextField( 10 );
		Panel tamingSpeedMultiplierPanel = new Panel( tamingSpeedMultiplierTF, "テイム速度" );
		
		// 採取量倍率
		harvestAmountMultiplierTF = new JTextField( 10 );
		Panel harvestAmountMultiplierPanel = new Panel( harvestAmountMultiplierTF, "採取量倍率" );
		
		// スピードレベリングを許可
		bAllowSpeedLevelingCB = new JCheckBox( "スピードレベリングを許可" );
		Panel bAllowSpeedLevelingPanel = new Panel( bAllowSpeedLevelingCB, false );
		
		// 飛行速度のレベリング上昇を許可
		bAllowFlyerSpeedLevelingCB = new JCheckBox( "飛行速度のレベリング上昇を許可" );
		Panel bAllowFlyerSpeedLevelingPanel = new Panel( bAllowFlyerSpeedLevelingCB, false );
		
		// 最高難易度に設定
		// マップに関係なく野生生物の最大レベルを150にする
		maxDifficultyCB = new JCheckBox( "最高難易度に設定" );
		Panel maxDifficultyPanel = new Panel( maxDifficultyCB, false );
		
		// 難易度 (最大でない場合)
		difficultyOffsetTF = new JTextField( 10 );
		Panel difficultyOffsetPanel = new Panel( difficultyOffsetTF, "ゲーム難易度( 0 ～ 1 )" );
		
		// PvEモード
		serverPVECB = new JCheckBox( "PvEモード" );
		Panel serverPVEPanel = new Panel( serverPVECB, false );
		
		// ハードコアモード
		serverHardcoreCB = new JCheckBox( "ハードコアモード" );
		Panel serverHardcorePanel = new Panel( serverHardcoreCB, false );
		
		// リスペックを無制限化
		bAllowUnlimitedRespecsCB = new JCheckBox( "リスペックを無制限化" );
		Panel bAllowUnlimitedRespecsPanel = new Panel( bAllowUnlimitedRespecsCB, false );

		JPanel worldPanel = new Panel(
				"ワールド設定 (ゲームルール)", 
				17, 
				Panel.CREAM, 
				new int[] { 10, 1 },
				new int[] { windowSize[0] - 900, 1100 }, 
				new int[] { 1, 1, 1, 1 }, 
				new Component[] {
						XPMultiplierPanel,
						tamingSpeedMultiplierPanel,
						harvestAmountMultiplierPanel,
						bAllowSpeedLevelingPanel,
						bAllowFlyerSpeedLevelingPanel,
						maxDifficultyPanel,
						difficultyOffsetPanel,
						serverPVEPanel,
						serverHardcorePanel,
						bAllowUnlimitedRespecsPanel
				} );
		worldSettingsPanel.add( worldPanel );
		mainPanel.add( "world", worldSettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 ルール のレイアウト
	 * ルールは破るためにある！
	 */
	void ruleSettingsLayout() {
		
		ruleSettingsPanel = new ScrollPanel();
		
		// 三人称視点の有効化
		allowThirdPersonPlayerCB = new JCheckBox( "三人称視点の有効化" );
		JPanel allowThirdPersonPlayerPanel = new Panel( allowThirdPersonPlayerCB, false );

		// 全体ボイスチャットの有効化
		globalVoiceChatCB = new JCheckBox( "全体ボイスチャット許可" );
		JPanel globalVoiceChatPanel = new Panel( globalVoiceChatCB, false );
		
		// 近接テキストチャットの有効化
		proximityChatCB = new JCheckBox( "近接テキストチャット許可" );
		JPanel proximityChatPanel = new Panel( proximityChatCB, false );
		
		// 他プレイヤーのログイン通知
		alwaysNotifyPlayerJoinedCB = new JCheckBox( "他プレイヤーのログイン通知表示" );
		JPanel alwaysNotifyPlayerJoinedPanel = new Panel( alwaysNotifyPlayerJoinedCB, false );
		
		// 他プレイヤーのログアウト通知
		alwaysNotifyPlayerLeftCB = new JCheckBox( "他プレイヤーのログアウト通知表示" );
		JPanel alwaysNotifyPlayerLeftPanel = new Panel( alwaysNotifyPlayerLeftCB, false );
		
		// 管理者ログの有効化
		adminLoggingCB = new JCheckBox( "管理者ログの有効化" );
		JPanel adminLoggingPanel = new Panel( adminLoggingCB, false );
		
		// クロスヘアの有効化
		serverCrosshairCB = new JCheckBox( "クロスヘアの有効化" );
		JPanel serverCrosshairPanel = new Panel( serverCrosshairCB, false );
		
		// HUDの無効化
		serverForceNoHudCB = new JCheckBox( "HUDの無効化" );
		JPanel serverForceNoHudPanel = new Panel( serverForceNoHudCB, false );
		
		// サバイバーダウンロードの無効化
		preventDownloadSurvivorsCB = new JCheckBox( "サバイバーダウンロード無効化" );
		JPanel preventDownloadSurvivorsPanel = new Panel( preventDownloadSurvivorsCB, false );
		
		// 恐竜ダウンロードの無効化
		preventDownloadDinosCB = new JCheckBox( "恐竜のダウンロード無効化" );
		JPanel preventDownloadDinosPanel = new Panel( preventDownloadDinosCB, false );
		
		// アイテムダウンロードの無効化
		preventDownloadItemsCB = new JCheckBox( "アイテムのダウンロード無効化" );
		JPanel preventDownloadItemsPanel = new Panel( preventDownloadItemsCB, false );
		
		// 貢物ダウンロードの無効化
		noTributeDownloadsCB = new JCheckBox( "貢物ダウンロード許可" );
		JPanel noTributeDownloadsPanel = new Panel( noTributeDownloadsCB, false );
		
		// 恐竜アップロードの無効化
		preventUploadDinosCB = new JCheckBox( "恐竜のアップロード許可" );
		JPanel preventUploadDinosPanel = new Panel( preventUploadDinosCB, false );
		
		// アイテムアップロードの無効化
		preventUploadItemsCB = new JCheckBox( "アイテムのアップロード許可" );
		JPanel preventUploadItemsPanel = new Panel( preventUploadItemsCB, false );
		
		// サバイバーアップロードの無効化
		preventUploadSurvivorsCB = new JCheckBox( "サバイバーのアップロード許可" );
		JPanel preventUploadSurvivorsPanel = new Panel( preventUploadSurvivorsCB, false );

		// 非専用サーバーで離れられる距離 (これは不要だからいれない)
		
		// PvPガンマの有効化
		enablePVPGammaCB = new JCheckBox( "PvPガンマの有効化" );
		JPanel enablePVPGammaPanel = new Panel( enablePVPGammaCB, false );
		
		// クリエイティブモードを表示
		bShowCreativeModeCB = new JCheckBox( "クリエイティブモードを表示" );
		Panel bShowCreativeModePanel = new Panel( bShowCreativeModeCB, false );
		
		// クレート無効化
		bDisableLootCratesCB = new JCheckBox( "クレート無効化" );
		Panel bDisableLootCratesPanel = new Panel( bDisableLootCratesCB, false );
		
		// フレンドリーファイア無効化
		bDisableFriendlyFireCB = new JCheckBox( "フレンドリーファイア無効化" );
		Panel bDisableFriendlyFirePanel = new Panel( bDisableFriendlyFireCB, false );
		
		// シングルプレイヤー設定を有効化
		bUseSingleplayerSettingsCB = new JCheckBox( "シングルプレイヤー設定を有効化" );
		Panel bUseSingleplayerSettingsPanel = new Panel( bUseSingleplayerSettingsCB, false );
		
		JPanel rulesPanel = new Panel(
				"ゲームルール設定", 
				17, 
				Panel.CREAM, 
				new int[] { 20, 1 },
				new int[] { windowSize[0] - 1000, 1100 }, 
				new int[] { 1, 1, 1, 1 }, 
				new Component[] {
						allowThirdPersonPlayerPanel,
						globalVoiceChatPanel,
						proximityChatPanel,
						alwaysNotifyPlayerJoinedPanel,
						alwaysNotifyPlayerLeftPanel,
						adminLoggingPanel,
						serverCrosshairPanel,
						serverForceNoHudPanel,
						preventDownloadSurvivorsPanel,
						preventDownloadDinosPanel,
						preventDownloadItemsPanel,
						noTributeDownloadsPanel,
						preventUploadDinosPanel,
						preventUploadItemsPanel,
						preventUploadSurvivorsPanel,
						enablePVPGammaPanel,
						bShowCreativeModePanel,
						bDisableLootCratesPanel,
						bDisableFriendlyFirePanel,
						bUseSingleplayerSettingsPanel
				} );
		ruleSettingsPanel.add( rulesPanel );
		mainPanel.add( "rule", ruleSettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 詳細-PvE のレイアウト
	 */
	void PvESettingsLayout() {
		
		PvESettingsPanel = new ScrollPanel();
		
		// 1
		// タイマーを使用
		bAutoPvETimerCB = new JCheckBox( "タイマーを使用" );
		Panel bAutoPvETimerPanel = new Panel( bAutoPvETimerCB, false );
		
		// 自動開始時間(秒)
		autoPvEStartTimeSecondsTF = new JTextField( 10 );
		Panel autoPvEStartTimeSecondsPanel = new Panel( autoPvEStartTimeSecondsTF, "自動開始時間(秒)" );
		
		// 自動終了時間(秒)
		autoPvEStopTimeSecondsTF = new JTextField( 10 );
		Panel autoPvEStopTimeSecondsPanel = new Panel( autoPvEStopTimeSecondsTF, "自動終了時間(秒)" );
		
		// システム時間使用
		bAutoPvEUseSystemTimeCB = new JCheckBox( "システム時間使用" );
		Panel bAutoPvEUseSystemTimePanel = new Panel( bAutoPvEUseSystemTimeCB, false );
		
		// トライブ同盟を禁止
		preventTribeAlliancesCB = new JCheckBox( "トライブ同盟を禁止" );
		Panel preventTribeAlliancesPanel = new Panel( preventTribeAlliancesCB, false );
		
		// トライブ戦争の許可
		bPvEAllowTribeWarCB = new JCheckBox( "トライブ戦争の許可" );
		Panel bPvEAllowTribeWarPanel = new Panel( bPvEAllowTribeWarCB, false );
		
		// トライブ戦争の中止
		bPvEAllowTribeWarCancelCB = new JCheckBox( "トライブ戦争の中止" );
		Panel bPvEAllowTribeWarCancelPanel = new Panel( bPvEAllowTribeWarCancelCB, false );
		
		// ガンマを無効化
		disablePvEGammaCB = new JCheckBox( "ガンマの無効化" );
		Panel disablePvEGammaPanel = new Panel( disablePvEGammaCB, false );
		
		// 洞窟内建築許可
		allowCaveBuildingPvECB = new JCheckBox( "洞窟内での建築許可" );
		Panel allowCaveBuildingPvEPanel = new Panel( allowCaveBuildingPvECB, false );
		
		// クレート降下ポイントでの建築許可
		PvEAllowStructuresAtSupplyDropsCB = new JCheckBox( "クレート降下ポイントでの建築許可" );
		Panel PvEAllowStructuresAtSupplyDropsPanel = new Panel( PvEAllowStructuresAtSupplyDropsCB, false );
		
		// 2
		// 建造物崩壊を無効化
		// 建造物の所有権失効無効化
		disableStructureDecayPVECB = new JCheckBox( "建築物の自然消滅の無効化" );
		Panel disableStructureDecayPVEPanel = new Panel( disableStructureDecayPVECB, false );
		
		// 建造物の所有権失効時間
		PvEStructureDecayPeriodMultiplierTF = new JTextField( 10 );
		Panel PvEStructureDecayPeriodMultiplierPanel = new Panel( PvEStructureDecayPeriodMultiplierTF, "建造物の所有権失効時間" );
		
		// 恐竜の所有権失効時間の無効化
		disableDinoDecayPvECB = new JCheckBox( "恐竜の所有権失効時間の無効化" );
		Panel disableDinoDecayPvEPanel = new Panel( disableDinoDecayPvECB, false );
		
		// 恐竜の所有権失効時間
		PvEDinoDecayPeriodMultiplierTF = new JTextField( 10 );
		Panel PvEDinoDecayPeriodMultiplierPanel = new Panel( PvEDinoDecayPeriodMultiplierTF, "恐竜が自然消滅する時間" );
		
		// 飛行運搬
		allowFlyerCarryPVECB = new JCheckBox( "飛行生物の掴みの許可" );
		Panel allowFlyerCarryPVEPanel = new Panel( allowFlyerCarryPVECB, false );
		
		// 資源豊富エリアへの建造制限
		enableExtraStructurePreventionVolumesCB = new JCheckBox( "資源豊富エリアでの建築制限" );
		Panel enableExtraStructurePreventionVolumesPanel = new Panel( enableExtraStructurePreventionVolumesCB, false );
		
		// 病気なし
		preventDiseasesCB = new JCheckBox( "病気のデバフの許可" );
		Panel preventDiseasesPanel = new Panel( preventDiseasesCB, false );
		
		// リスポーン後の感染症を無効化
		nonPermanentDiseasesCB = new JCheckBox( "リスポーン後の感染症を無効化" );
		Panel nonPermanentDiseasesPanel = new Panel( nonPermanentDiseasesCB, false );
		
		// 生物の洞窟内飛行を許可
		forceAllowCaveFlyersCB = new JCheckBox( "生物の洞窟内飛行を許可" );
		Panel forceAllowCaveFlyersPanel = new Panel( forceAllowCaveFlyersCB, false );
		
		JPanel PvEPanel = new Panel( 
				"PvE", 
				17, 
				Panel.CREAM, 
				new int[] { 10, 2 },
				new int[] { windowSize[0] - 450, 380 }, 
				new int[] { 1, 1, 1, 1 }, 
				new Component[] {
						bAutoPvETimerPanel,
						disableStructureDecayPVEPanel,
						autoPvEStartTimeSecondsPanel,
						PvEStructureDecayPeriodMultiplierPanel,
						autoPvEStopTimeSecondsPanel,
						disableDinoDecayPvEPanel,
						bAutoPvEUseSystemTimePanel,
						PvEDinoDecayPeriodMultiplierPanel,
						preventTribeAlliancesPanel,
						allowFlyerCarryPVEPanel,
						bPvEAllowTribeWarPanel,
						enableExtraStructurePreventionVolumesPanel,
						bPvEAllowTribeWarCancelPanel,
						preventDiseasesPanel,
						disablePvEGammaPanel,
						nonPermanentDiseasesPanel,
						allowCaveBuildingPvEPanel,
						forceAllowCaveFlyersPanel,
						PvEAllowStructuresAtSupplyDropsPanel
				} );
		PvESettingsPanel.add( PvEPanel );
		mainPanel.add( "pve", PvESettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 詳細-PvP のレイアウト
	 */
	void PvPSettingsLayout() {

		PvPSettingsPanel = new ScrollPanel();
		
		// 恐竜の所有権失効許可
		PvPDinoDecayCB = new JCheckBox( "恐竜の所有権失効有効化" );
		Panel PvPDinoDecayPanel = new Panel( PvPDinoDecayCB, false );
		
		// プラットフォームへの建造禁止を無効化
		overrideStructurePlatformPreventionCB = new JCheckBox( "プラットフォームへの建造禁止を無効化" );
		Panel overrideStructurePlatformPreventionPanel = new Panel( overrideStructurePlatformPreventionCB, false );
		
		// リスポーン間隔を変更
		increasePvPRespawnIntervalMultiplierTF = new JTextField( 10 );
		Panel increasePvPRespawnIntervalMultiplierPanel = new Panel( increasePvPRespawnIntervalMultiplierTF, "リスポーン間隔を変更" );
		
		// リスポーン間隔の周期
		increasePvPRespawnIntervalCheckPeriodTF = new JTextField( 10 );
		Panel increasePvPRespawnIntervalCheckPeriodPanel = new Panel( increasePvPRespawnIntervalCheckPeriodTF, "リスポーン間隔の周期" );
		
		// 連続死亡時のリスポーン間隔
		bIncreasePvPRespawnIntervalTF = new JTextField( 10 );
		Panel bIncreasePvPRespawnIntervalPanel = new Panel( bIncreasePvPRespawnIntervalTF, "連続死亡時のリスポーン間隔" );

		// リスポーン間隔の基準
		increasePvPRespawnIntervalBaseAmountTF = new JTextField( 10 );
		Panel increasePvPRespawnIntervalBaseAmountPanel = new Panel( increasePvPRespawnIntervalBaseAmountTF, "リスポーン間隔の基準" );

		// エリア建造物へのダメージ
		PvPZoneStructureDamageMultiplierTF = new JTextField( 10 );
		Panel PvPZoneStructureDamageMultiplierPanel = new Panel( PvPZoneStructureDamageMultiplierTF, "エリア建造物へのダメージ" );
		
		// 建造物周辺の資源リスポーン間隔
		structurePreventResourceRadiusMultiplierTF = new JTextField( 10 );
		Panel structurePreventResourceRadiusMultiplierPanel = new Panel( structurePreventResourceRadiusMultiplierTF, "構造物周辺の資源リスポーン間隔" );
		
		// オフライン時でのPvP攻撃の許可
		preventOfflinePvPCB = new JCheckBox( "オフライン時でのPvP攻撃の許可" );
		Panel preventOfflinePvPPanel = new Panel( preventOfflinePvPCB, false );
		
		// 建造物の自然消滅の許可
		PvPStructureDecayCB = new JCheckBox( "構造物の自然消滅の許可" );
		Panel PvPStructureDecayPanel = new Panel( PvPStructureDecayCB, false );

		// 洞窟内での建築を許可
		allowCaveBuildingPvPCB = new JCheckBox( "洞窟内での建築を許可" );
		Panel allowCaveBuildingPvPPanel = new Panel( allowCaveBuildingPvPCB, false );

		JPanel PvPPanel = new Panel( 
				"PvP設定",
				17, 
				Panel.CREAM,
				new int[] { 11, 1 },
				new int[] { windowSize[0] - 800, 400 }, 
				new int[] { 1, 1, 1, 1 }, 
				new Component[] {
						PvPDinoDecayPanel,
						overrideStructurePlatformPreventionPanel,
						increasePvPRespawnIntervalMultiplierPanel,
						increasePvPRespawnIntervalCheckPeriodPanel,
						bIncreasePvPRespawnIntervalPanel,
						increasePvPRespawnIntervalBaseAmountPanel,
						PvPZoneStructureDamageMultiplierPanel,
						structurePreventResourceRadiusMultiplierPanel,
						preventOfflinePvPPanel,
						PvPStructureDecayPanel,
						allowCaveBuildingPvPPanel
				} );
		PvPSettingsPanel.add( PvPPanel );
		mainPanel.add( "pvp", PvPSettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 詳細-ワールド のレイアウト
	 */
	void worldDetailSettingsLayout() {
		
		worldDetailedSettingsPanel = new ScrollPanel();
		
		// 1
		// 一日の経過速度
		dayCycleSpeedScaleTF = new JTextField( 10 );
		Panel dayCycleSpeedScaleTFPanel = new Panel( dayCycleSpeedScaleTF, "一日の経過速度" );

		// 日中の経過速度
		dayTimeSpeedScaleTF = new JTextField( 10 );
		Panel dayTimeSpeedScalePanel = new Panel( dayTimeSpeedScaleTF, "日中の経過速度" );

		// 夜間の経過速度
		nightTimeSpeedScaleTF = new JTextField( 10 );
		Panel nightTimeSpeedScalePanel = new Panel( nightTimeSpeedScaleTF, "夜間の経過速度" );

		// 開始時刻をオーバーライド
		startTimeOverrideCB = new JCheckBox( "開始時刻をオーバーライド" );
		Panel startTimeOverridePanel = new Panel( startTimeOverrideCB, false );
		
		// 開始時刻
		startTimeHourTF = new JTextField( 10 );
		Panel startTimeHourPanel = new Panel( 
				startTimeHourTF, 
				"<html>開始時刻(ワールド開始の時間)</html>",
				new double[] { 0.7, 0.3 }
		);
		
		// 腐敗までの時間 (大きくすると長くなる)
		globalSpoilingTimeMultiplierTF = new JTextField( 10 );
		Panel globalSpoilingTimeMultiplierPanel = new Panel( 
				globalSpoilingTimeMultiplierTF, 
				"<html>腐敗までの時間(肉やベリーなど)<br>大きくすると長くなる</html>",
				new double[] { 0.7, 0.3 } 
		);

		// アイテム消滅までの時間
		globalItemDecompositionTimeMultiplierTF = new JTextField( 10 );
		Panel globalItemDecompositionTimeMultiplierPanel = new Panel( globalItemDecompositionTimeMultiplierTF, "<html>アイテム消滅までの時間<br>大きくすると長くなる</html>" );
	
		// 死体消失までの時間
		globalCorpseDecompositionTimeMultiplierTF = new JTextField( 10 );
		Panel globalCorpseDecompositionTimeMultiplierPanel = new Panel( globalCorpseDecompositionTimeMultiplierTF, "死体消失までの時間" );

		// 資源のリスポーンに必要なプレイヤーからの距離
		resourceNoReplenishRadiusPlayersTF = new JTextField( 10 );
		Panel resourceNoReplenishRadiusPlayersPanel = new Panel( 
				resourceNoReplenishRadiusPlayersTF, 
				"<html>資源リスポーンに必要な<br>プレイヤーの距離</html>",
				new double[] { 0.7, 0.3 }
		);
		
		// 資源のリスポーンに必要な建造物からの距離
		resourceNoReplenishRadiusStructuresTF = new JTextField( 10 );
		Panel resourceNoReplenishRadiusStructuresPanel = new Panel( 
				resourceNoReplenishRadiusStructuresTF, 
				"<html>資源リスポーンに必要な<br>建造物の距離</html>",
				new double[] { 0.7, 0.3 }
		);
		
		// 2
		// 採取可能物の耐久力
		harvestHealthMultiplierTF = new JTextField( 10 );
		Panel harvestHealthMultiplierPanel = new Panel( harvestHealthMultiplierTF, "採取可能物の耐久力" );
		
		// 資源のリスポーン間隔
		resourcesRespawnPeriodMultiplierTF = new JTextField( 10 );
		Panel resourcesRespawnPeriodMultiplierPanel = new Panel( resourcesRespawnPeriodMultiplierTF, "資源のリスポーン間隔" );

		// 作物の成長速度
		cropGrowthSpeedMultiplierTF = new JTextField( 10 );
		Panel cropGrowthSpeedMultiplierPanel = new Panel( cropGrowthSpeedMultiplierTF, "作物の成長速度" );

		// 作物の腐敗速度
		cropDecaySpeedMultiplierTF = new JTextField( 10 );
		Panel cropDecaySpeedMultiplierPanel = new Panel( cropDecaySpeedMultiplierTF, "作物の腐敗速度" );

		// 排泄の間隔
		poopIntervalMultiplierTF = new JTextField( 10 );
		Panel poopIntervalMultiplierPanel = new Panel( poopIntervalMultiplierTF, "排泄の間隔" );

		// 交配可能になるまでの期間
		matingIntervalMultiplierTF = new JTextField( 10 );
		Panel matingIntervalMultiplierPanel = new Panel( matingIntervalMultiplierTF, "交配可能になるまでの期間" );

		// 産卵可能になるまでの期間
		layEggIntervalMultiplierTF = new JTextField( 10 );
		Panel layEggIntervalMultiplierPanel = new Panel( layEggIntervalMultiplierTF, "産卵可能になるまでの期間" );

		// 卵の孵化速度
		eggHatchSpeedMultiplierTF = new JTextField( 10 );
		Panel eggHatchSpeedMultiplierPanel = new Panel( eggHatchSpeedMultiplierTF, "卵の孵化速度" );
		
		// 3
		// 恐竜への刷り込み時のバフ無効化
		disableImprintDinoBuffCB = new JCheckBox( "恐竜の刷り込みバフの無効化" );
		Panel disableImprintDinoBuffPanel = new Panel( disableImprintDinoBuffCB, false );
		
		// 全員に赤ちゃんの世話を許可
		allowAnyoneBabyImprintCuddleCB = new JCheckBox( "刷り込み主以外の刷り込みの許可" );
		Panel allowAnyoneBabyImprintCuddlePanel = new Panel( allowAnyoneBabyImprintCuddleCB, false );
		
		// 赤ちゃんの成長速度
		babyMatureSpeedMultiplierTF = new JTextField( 10 );
		Panel babyMatureSpeedMultiplierPanel = new Panel( babyMatureSpeedMultiplierTF, "赤ちゃんの成長速度" );
		
		// 赤ちゃんの食料消費速度
		babyFoodConsumptionSpeedMultiplierTF = new JTextField( 10 );
		Panel babyFoodConsumptionSpeedMultiplierPanel = new Panel( babyFoodConsumptionSpeedMultiplierTF, "赤ちゃんの食料消費速度" );
		
		// 赤ちゃんの世話の間隔
		babyCuddleIntervalMultiplierTF = new JTextField( 10 );
		Panel babyCuddleIntervalMultiplierPanel = new Panel( babyCuddleIntervalMultiplierTF, "赤ちゃんの世話の間隔" );

		// 赤ちゃんの世話の猶予期間
		babyCuddleGracePeriodMultiplierTF = new JTextField( 10 );
		Panel babyCuddleGracePeriodMultiplierPanel = new Panel( babyCuddleGracePeriodMultiplierTF, "赤ちゃんの世話の猶予間隔" );

		// 赤ちゃんの世話の刷り込み時のバフ効果減少速度
		babyCuddleLoseImprintQualitySpeedMultiplierTF = new JTextField( 10 );
		Panel babyCuddleLoseImprintQualitySpeedMultiplierPanel = new Panel( 
				babyCuddleLoseImprintQualitySpeedMultiplierTF, 
				"刷り込み時のバフ効果減少速度",
				new double[] { 0.7, 0.3 }
		);

		// 赤ちゃんへの刷り込み時のバフ効果
		babyImprintingStatScaleMultiplierTF = new JTextField( 10 );
		Panel babyImprintingStatScaleMultiplierPanel = new Panel( babyImprintingStatScaleMultiplierTF, "刷り込み時のバフ効果倍率" );
		
		JPanel worldPanel = new Panel( 
				"ワールド (詳細)", 
				17, 
				Panel.CREAM, 
				new int[] { 10, 3 },
				new int[] { windowSize[0] - 100, 1100 }, 
				new int[] { 1, 1, 1, 1 }, 
				new Component[] {
						dayCycleSpeedScaleTFPanel,
						harvestHealthMultiplierPanel,
						disableImprintDinoBuffPanel,
						dayTimeSpeedScalePanel,
						resourcesRespawnPeriodMultiplierPanel,
						allowAnyoneBabyImprintCuddlePanel,
						nightTimeSpeedScalePanel,
						cropGrowthSpeedMultiplierPanel,
						babyMatureSpeedMultiplierPanel,
						startTimeOverridePanel,
						cropDecaySpeedMultiplierPanel,
						babyFoodConsumptionSpeedMultiplierPanel,
						startTimeHourPanel,
						poopIntervalMultiplierPanel,
						babyCuddleIntervalMultiplierPanel,
						globalSpoilingTimeMultiplierPanel,
						matingIntervalMultiplierPanel,
						babyCuddleGracePeriodMultiplierPanel,
						globalItemDecompositionTimeMultiplierPanel,
						layEggIntervalMultiplierPanel,
						babyCuddleLoseImprintQualitySpeedMultiplierPanel,
						globalCorpseDecompositionTimeMultiplierPanel,
						eggHatchSpeedMultiplierPanel,
						babyImprintingStatScaleMultiplierPanel,
						resourceNoReplenishRadiusPlayersPanel,
						new Panel( Panel.CREAM ),
						new Panel( Panel.CREAM ),
						resourceNoReplenishRadiusStructuresPanel
				} );
		worldPanel.setAlignmentX( LEFT_ALIGNMENT );
		worldDetailedSettingsPanel.add( worldPanel );
		mainPanel.add( "worldDetailed", worldDetailedSettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 詳細-野生恐竜 のレイアウト
	 */
	void dinoWildSettingsLayout() {
		
		dinoWildSettingsPanel = new ScrollPanel();
		
		perLevelStatsMultiplier_DinoWildHPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoWildHPPanel = new Panel( perLevelStatsMultiplier_DinoWildHPTF, "ＨＰ　　　" );

		perLevelStatsMultiplier_DinoWildSTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoWildSTPanel = new Panel( perLevelStatsMultiplier_DinoWildSTTF, "スタミナ　" );

		perLevelStatsMultiplier_DinoWildOXTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoWildOXPanel = new Panel( perLevelStatsMultiplier_DinoWildOXTF, "酸素値　　" );

		perLevelStatsMultiplier_DinoWildFDTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoWildFDPanel = new Panel( perLevelStatsMultiplier_DinoWildFDTF, "食料値　　" );

		perLevelStatsMultiplier_DinoWildWTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoWildWTPanel = new Panel( perLevelStatsMultiplier_DinoWildWTTF, "水分値　　" );

		perLevelStatsMultiplier_DinoWildWETF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoWildWEPanel = new Panel( perLevelStatsMultiplier_DinoWildWETF, "重量　　　" );

		perLevelStatsMultiplier_DinoWildDMGTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoWildDMGPanel = new Panel( perLevelStatsMultiplier_DinoWildDMGTF, "近接攻撃力" );

		perLevelStatsMultiplier_DinoWildSPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoWildSPPanel = new Panel( perLevelStatsMultiplier_DinoWildSPTF, "移動速度　" );

		perLevelStatsMultiplier_DinoWildFTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoWildFTPanel = new Panel( perLevelStatsMultiplier_DinoWildFTTF, "忍耐力　　" );

		perLevelStatsMultiplier_DinoWildTRPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoWildTRPPanel = new Panel( perLevelStatsMultiplier_DinoWildTRPTF, "気絶値　　" );

		perLevelStatsMultiplier_DinoWildTMPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoWildTMPPanel = new Panel( perLevelStatsMultiplier_DinoWildTMPTF, "温度　　　" );
		
		JPanel dinoPanel = new Panel( 
				"野生恐竜のステータス", 
				17, 
				Panel.CREAM, 
				new int[] { 11, 1 },
				new int[] { windowSize[0] - 900, 1000 }, 
				new int[] { 1, 1, 1, 1 }, 
				new Component[] {
						perLevelStatsMultiplier_DinoWildHPPanel,
						perLevelStatsMultiplier_DinoWildSTPanel,
						perLevelStatsMultiplier_DinoWildOXPanel,
						perLevelStatsMultiplier_DinoWildFDPanel,
						perLevelStatsMultiplier_DinoWildWTPanel,
						perLevelStatsMultiplier_DinoWildWEPanel,
						perLevelStatsMultiplier_DinoWildDMGPanel,
						perLevelStatsMultiplier_DinoWildSPPanel,
						perLevelStatsMultiplier_DinoWildFTPanel,
						perLevelStatsMultiplier_DinoWildTRPPanel,
						perLevelStatsMultiplier_DinoWildTMPPanel
				} );
		dinoWildSettingsPanel.add( dinoPanel );
		
		mainPanel.add( "dinoWild", dinoWildSettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 詳細-テイム済みの恐竜 のレイアウト
	 */
	void tamedDinoSettingsLayout() {
		
		tamedDinoSettingsPanel = new ScrollPanel();
		
		perLevelStatsMultiplier_DinoTamedHPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamedHPPanel = new Panel( perLevelStatsMultiplier_DinoTamedHPTF, "ＨＰ　　　" );

		perLevelStatsMultiplier_DinoTamedSTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamedSTPanel = new Panel( perLevelStatsMultiplier_DinoTamedSTTF, "スタミナ　" );

		perLevelStatsMultiplier_DinoTamedOXTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamedOXPanel = new Panel( perLevelStatsMultiplier_DinoTamedOXTF, "酸素値　　" );

		perLevelStatsMultiplier_DinoTamedFDTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamedFDPanel = new Panel( perLevelStatsMultiplier_DinoTamedFDTF, "食料値　　" );

		perLevelStatsMultiplier_DinoTamedWTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamedWTPanel = new Panel( perLevelStatsMultiplier_DinoTamedWTTF, "水分値　　" );

		perLevelStatsMultiplier_DinoTamedWETF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamedWEPanel = new Panel( perLevelStatsMultiplier_DinoTamedWETF, "重量　　　" );

		perLevelStatsMultiplier_DinoTamedDMGTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamedDMGPanel = new Panel( perLevelStatsMultiplier_DinoTamedDMGTF, "近接攻撃力" );

		perLevelStatsMultiplier_DinoTamedSPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamedSPPanel = new Panel( perLevelStatsMultiplier_DinoTamedSPTF, "移動速度　" );

		perLevelStatsMultiplier_DinoTamedFTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamedFTPanel = new Panel( perLevelStatsMultiplier_DinoTamedFTTF, "忍耐力　　" );

		perLevelStatsMultiplier_DinoTamedTRPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamedTRPPanel = new Panel( perLevelStatsMultiplier_DinoTamedTRPTF, "気絶値　　" );

		perLevelStatsMultiplier_DinoTamedTMPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamedTMPPanel = new Panel( perLevelStatsMultiplier_DinoTamedTMPTF, "温度　　　" );

		perLevelStatsMultiplier_DinoTamed_AddHPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AddHPPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AddHPTF, "ＨＰ　　　" );

		perLevelStatsMultiplier_DinoTamed_AddSTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AddSTPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AddSTTF, "スタミナ　" );

		perLevelStatsMultiplier_DinoTamed_AddOXTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AddOXPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AddOXTF, "酸素値　　" );

		perLevelStatsMultiplier_DinoTamed_AddFDTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AddFDPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AddFDTF, "食料値　　" );

		perLevelStatsMultiplier_DinoTamed_AddWTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AddWTPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AddWTTF, "水分値　　" );

		perLevelStatsMultiplier_DinoTamed_AddWETF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AddWEPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AddWETF, "重量　　　" );

		perLevelStatsMultiplier_DinoTamed_AddDMGTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AddDMGPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AddDMGTF, "近接攻撃力" );

		perLevelStatsMultiplier_DinoTamed_AddSPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AddSPPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AddSPTF, "移動速度　" );

		perLevelStatsMultiplier_DinoTamed_AddFTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AddFTPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AddFTTF, "忍耐力　　" );

		perLevelStatsMultiplier_DinoTamed_AddTRPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AddTRPPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AddTRPTF, "気絶値　　" );

		perLevelStatsMultiplier_DinoTamed_AddTMPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AddTMPPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AddTMPTF, "温度　　　" );

		perLevelStatsMultiplier_DinoTamed_AffinityHPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AffinityHPPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AffinityHPTF, "ＨＰ　　　" );

		perLevelStatsMultiplier_DinoTamed_AffinitySTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AffinitySTPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AffinitySTTF, "スタミナ　" );

		perLevelStatsMultiplier_DinoTamed_AffinityOXTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AffinityOXPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AffinityOXTF, "酸素値　　" );

		perLevelStatsMultiplier_DinoTamed_AffinityFDTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AffinityFDPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AffinityFDTF, "食料値　　" );

		perLevelStatsMultiplier_DinoTamed_AffinityWTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AffinityWTPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AffinityWTTF, "水分値　　" );

		perLevelStatsMultiplier_DinoTamed_AffinityWETF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AffinityWEPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AffinityWETF, "重量　　　" );

		perLevelStatsMultiplier_DinoTamed_AffinityDMGTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AffinityDMGPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AffinityDMGTF, "近接攻撃力" );

		perLevelStatsMultiplier_DinoTamed_AffinitySPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AffinitySPPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AffinitySPTF, "移動速度　" );

		perLevelStatsMultiplier_DinoTamed_AffinityFTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AffinityFTPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AffinityFTTF, "忍耐力　　" );

		perLevelStatsMultiplier_DinoTamed_AffinityTRPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AffinityTRPPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AffinityTRPTF, "気絶値　　" );

		perLevelStatsMultiplier_DinoTamed_AffinityTMPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_DinoTamed_AffinityTMPPanel = new Panel( perLevelStatsMultiplier_DinoTamed_AffinityTMPTF, "温度　　　" );

		JPanel tamedDinoPanel = new Panel( 
				"テイム済みの恐竜の各ステータス倍率", 
				17, 
				Panel.CREAM, 
				new int[] { 12, 3 },
				new int[] { windowSize[0], 1000 }, 
				new int[] { 0, 0, 1, 0 }, 
				new Component[] {
						new JLabel("レベル毎のステータス"),
						new JLabel("レベル毎の追加ステータス"),
						new JLabel("テイム後のステータスの変化の倍率"),
						perLevelStatsMultiplier_DinoTamedHPPanel,
						perLevelStatsMultiplier_DinoTamed_AddHPPanel,
						perLevelStatsMultiplier_DinoTamed_AffinityHPPanel,
						perLevelStatsMultiplier_DinoTamedSTPanel,
						perLevelStatsMultiplier_DinoTamed_AddSTPanel,
						perLevelStatsMultiplier_DinoTamed_AffinitySTPanel,
						perLevelStatsMultiplier_DinoTamedOXPanel,
						perLevelStatsMultiplier_DinoTamed_AddOXPanel,
						perLevelStatsMultiplier_DinoTamed_AffinityOXPanel,
						perLevelStatsMultiplier_DinoTamedFDPanel,
						perLevelStatsMultiplier_DinoTamed_AddFDPanel,
						perLevelStatsMultiplier_DinoTamed_AffinityFDPanel,
						perLevelStatsMultiplier_DinoTamedWTPanel,
						perLevelStatsMultiplier_DinoTamed_AddWTPanel,
						perLevelStatsMultiplier_DinoTamed_AffinityWTPanel,
						perLevelStatsMultiplier_DinoTamedWEPanel,
						perLevelStatsMultiplier_DinoTamed_AddWEPanel,
						perLevelStatsMultiplier_DinoTamed_AffinityWEPanel,
						perLevelStatsMultiplier_DinoTamedDMGPanel,
						perLevelStatsMultiplier_DinoTamed_AddDMGPanel,
						perLevelStatsMultiplier_DinoTamed_AffinityDMGPanel,
						perLevelStatsMultiplier_DinoTamedSPPanel,
						perLevelStatsMultiplier_DinoTamed_AddSPPanel,
						perLevelStatsMultiplier_DinoTamed_AffinitySPPanel,
						perLevelStatsMultiplier_DinoTamedFTPanel,
						perLevelStatsMultiplier_DinoTamed_AddFTPanel,
						perLevelStatsMultiplier_DinoTamed_AffinityFTPanel,
						perLevelStatsMultiplier_DinoTamedTRPPanel,
						perLevelStatsMultiplier_DinoTamed_AddTRPPanel,
						perLevelStatsMultiplier_DinoTamed_AffinityTRPPanel,
						perLevelStatsMultiplier_DinoTamedTMPPanel,
						perLevelStatsMultiplier_DinoTamed_AddTMPPanel,
						perLevelStatsMultiplier_DinoTamed_AffinityTMPPanel
				} );
		tamedDinoSettingsPanel.add( tamedDinoPanel );
		
		mainPanel.add( "tamedDino", tamedDinoSettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 詳細-プレイヤー のレイアウト
	 */
	void playerDetailedSettingsLayout() {
		
		playerDetailedSettingsPanel = new ScrollPanel();
		
		perLevelStatsMultiplier_PlayerHPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_PlayerHPPanel = new Panel( perLevelStatsMultiplier_PlayerHPTF, "ＨＰ　　　" );

		perLevelStatsMultiplier_PlayerSTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_PlayerSTPanel = new Panel( perLevelStatsMultiplier_PlayerSTTF, "スタミナ　" );

		perLevelStatsMultiplier_PlayerOXTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_PlayerOXPanel = new Panel( perLevelStatsMultiplier_PlayerOXTF, "酸素値　　" );

		perLevelStatsMultiplier_PlayerFDTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_PlayerFDPanel = new Panel( perLevelStatsMultiplier_PlayerFDTF, "食料値　　" );

		perLevelStatsMultiplier_PlayerWTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_PlayerWTPanel = new Panel( perLevelStatsMultiplier_PlayerWTTF, "水分値　　" );

		perLevelStatsMultiplier_PlayerWETF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_PlayerWEPanel = new Panel( perLevelStatsMultiplier_PlayerWETF, "重量　　　" );

		perLevelStatsMultiplier_PlayerDMGTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_PlayerDMGPanel = new Panel( perLevelStatsMultiplier_PlayerDMGTF, "近接攻撃力" );

		perLevelStatsMultiplier_PlayerSPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_PlayerSPPanel = new Panel( perLevelStatsMultiplier_PlayerSPTF, "移動速度　" );

		perLevelStatsMultiplier_PlayerFTTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_PlayerFTPanel = new Panel( perLevelStatsMultiplier_PlayerFTTF, "忍耐力　　" );

		perLevelStatsMultiplier_PlayerTRPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_PlayerTRPPanel = new Panel( perLevelStatsMultiplier_PlayerTRPTF, "気絶値　　" );

		perLevelStatsMultiplier_PlayerTMPTF = new JTextField( 10 );
		Panel perLevelStatsMultiplier_PlayerTMPPanel = new Panel( perLevelStatsMultiplier_PlayerTMPTF, "温度　　　" );
		
		JPanel playerDPanel = new Panel( 
				"プレイヤーのステータス", 
				17, 
				Panel.CREAM, 
				new int[] { 11, 1 },
				new int[] { windowSize[0] - 900, 1000 }, 
				new int[] { 1, 1, 1, 1 }, 
				new Component[] {
						perLevelStatsMultiplier_PlayerHPPanel,
						perLevelStatsMultiplier_PlayerSTPanel,
						perLevelStatsMultiplier_PlayerOXPanel,
						perLevelStatsMultiplier_PlayerFDPanel,
						perLevelStatsMultiplier_PlayerWTPanel,
						perLevelStatsMultiplier_PlayerWEPanel,
						perLevelStatsMultiplier_PlayerDMGPanel,
						perLevelStatsMultiplier_PlayerSPPanel,
						perLevelStatsMultiplier_PlayerFTPanel,
						perLevelStatsMultiplier_PlayerTRPPanel,
						perLevelStatsMultiplier_PlayerTMPPanel
				} );
		playerDetailedSettingsPanel.add( playerDPanel );

		mainPanel.add( "playerDetailed", playerDetailedSettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 詳細-経験値倍率 のレイアウト
	 */
	void experienceSettingsLayout() {
		
		experienceSettingsPanel = new ScrollPanel();
		
		// 一般
		genericXPMultiplierTF = new JTextField( 10 );
		Panel genericXPMultiplierPanel = new Panel( genericXPMultiplierTF, "一般" );

		// 採取
		harvestXPMultiplierTF = new JTextField( 10 );
		Panel harvestXPMultiplierPanel = new Panel( harvestXPMultiplierTF, "採取" );

		// 製作
		craftXPMultiplierTF = new JTextField( 10 );
		Panel craftXPMultiplierPanel = new Panel( craftXPMultiplierTF, "製作" );

		// 探索家の記録
		explorerNoteXPMultiplierTF = new JTextField( 10 );
		Panel explorerNoteXPMultiplierPanel = new Panel( explorerNoteXPMultiplierTF, "探索家の記録" );

		// 特殊
		specialXPMultiplierTF = new JTextField( 10 );
		Panel specialXPMultiplierPanel = new Panel( specialXPMultiplierTF, "特殊" );

		// キル
		killXPMultiplierTF = new JTextField( 10 );
		Panel killXPMultiplierPanel = new Panel( killXPMultiplierTF, "キル" );

		// ボス生物のキル
		bossKillXPMultiplierTF = new JTextField( 10 );
		Panel bossKillXPMultiplierPanel = new Panel( bossKillXPMultiplierTF, "ボス生物のキル" );

		// アルファ生物のキル
		alphaKillXPMultiplierTF = new JTextField( 10 );
		Panel alphaKillXPMultiplierPanel = new Panel( alphaKillXPMultiplierTF, "アルファ生物のキル" );

		// 洞窟生物のキル
		caveKillXPMultiplierTF = new JTextField( 10 );
		Panel caveKillXPMultiplierPanel = new Panel( caveKillXPMultiplierTF, "洞窟生物のキル" );

		// 野生生物のキル
		wildKillXPMultiplierTF = new JTextField( 10 );
		Panel wildKillXPMultiplierPanel = new Panel( wildKillXPMultiplierTF, "野生生物のキル" );

		// テイム済み生物のキル
		tamedKillXPMultiplierTF = new JTextField( 10 );
		Panel tamedKillXPMultiplierPanel = new Panel( tamedKillXPMultiplierTF, "テイム済み生物のキル" );

		// 未所有の生物のキル
		unclaimedKillXPMultiplierTF = new JTextField( 10 );
		Panel unclaimedKillXPMultiplierPanel = new Panel( unclaimedKillXPMultiplierTF, "未所有の生物のキル" );

		JPanel exPanel = new Panel( 
				"経験値倍率の設定", 
				17, 
				Panel.CREAM, 
				new int[] { 12, 1 },
				new int[] { windowSize[0] - 900, 550 }, 
				new int[] { 1, 1, 1, 1 }, 
				new Component[] {
						genericXPMultiplierPanel,
						harvestXPMultiplierPanel,
						craftXPMultiplierPanel,
						explorerNoteXPMultiplierPanel,
						specialXPMultiplierPanel,
						killXPMultiplierPanel,
						bossKillXPMultiplierPanel,
						alphaKillXPMultiplierPanel,
						caveKillXPMultiplierPanel,
						wildKillXPMultiplierPanel,
						tamedKillXPMultiplierPanel,
						unclaimedKillXPMultiplierPanel
				} );
		experienceSettingsPanel.add( exPanel );
		
		mainPanel.add( "experience", experienceSettingsPanel.getScrollPane() );
	}
	
	/**
	 * 設定項目 詳細-その他 のレイアウト
	 */
	void otherSettingsLayout() {
		
		otherSettingsPanel = new ScrollPanel();
		
		// 1
		// トライブ内の最大人数( 0で無制限 )
		maxNumberOfPlayersInTribeTF = new JTextField( 10 );
		Panel maxNumberOfPlayersInTribePanel = new Panel( maxNumberOfPlayersInTribeTF, "トライブ内の最大人数 ( 0で無制限 )" );
		
		// プレイヤーの最大経験値
		overrideMaxExperiencePointsPlayerTF = new JTextField( 10 );
		Panel overrideMaxExperiencePointsPlayerPanel = new Panel( overrideMaxExperiencePointsPlayerTF, "プレイヤーの最大経験値" );
		
		// 生物の最大経験値
		overrideMaxExperiencePointsDinoTF = new JTextField( 10 );
		Panel overrideMaxExperiencePointsDinoPanel = new Panel( overrideMaxExperiencePointsDinoTF, "生物の最大経験値" );
		
		// 飛行生物のプラットフォーム上への恐竜の搭乗を許可
		bFlyerPlatformAllowUnalignedDinoBasingCB = new JCheckBox( "飛行生物のプラットフォーム上への生物の搭乗を許可" );
		Panel bFlyerPlatformAllowUnalignedDinoBasingPanel = new Panel( bFlyerPlatformAllowUnalignedDinoBasingCB, false );
		
		// 騎乗者のいない恐竜の防護柵ダメージ有効化
		bPassiveDefensesDamageRiderlessDinosCB = new JCheckBox( "騎乗者のいない恐竜の防護柵ダメージ有効化" );
		Panel bPassiveDefensesDamageRiderlessDinosPanel = new Panel( bPassiveDefensesDamageRiderlessDinosCB, false );
		
		// 特定のエングラムのみ許可 (解析が終わり次第実装予定)
		onlyAllowSpecifiedEngramsCB = new JCheckBox( "特定のエングラムのみ許可" );
		Panel onlyAllowSpecifiedEngramsPanel = new Panel( onlyAllowSpecifiedEngramsCB, false );
		
		// レイド恐竜の食料消費許可
		allowRaidDinoFeedingCB = new JCheckBox( "レイド恐竜の食料消費許可" );
		Panel allowRaidDinoFeedingPanel = new Panel( allowRaidDinoFeedingCB, false );

		// レイド恐竜の食料値消費速度
		raidDinoCharacterFoodDrainMultiplierTF = new JTextField( 10 );
		Panel raidDinoCharacterFoodDrainMultiplierPanel = new Panel( raidDinoCharacterFoodDrainMultiplierTF, "レイド恐竜の食料値消費速度" );
		
		// フォトモードを無効化
		bDisablePhotoModeCB = new JCheckBox( "フォトモードを無効化" );
		Panel bDisablePhotoModePanel = new Panel( bDisablePhotoModeCB, false );
		
		// フォトモードの範囲制限
		photoModeRangeLimitTF = new JTextField( 10 );
		Panel photoModeRangeLimitPanel = new Panel( photoModeRangeLimitTF, "フォトモードの範囲制限" );
		
		// 死亡地点の表示の有効化
		bUseCorpseLocatorCB = new JCheckBox( "死亡地点の表示の有効化" );
		Panel bUseCorpseLocatorPanel = new Panel( bUseCorpseLocatorCB, false );
		
		// 2
		// カスタムレシピを許可
		bAllowCustomRecipesCB = new JCheckBox( "カスタムレシピを許可" );
		Panel bAllowCustomRecipesPanel = new Panel( bAllowCustomRecipesCB, false );
		
		// カスタムレシピの効果
		customRecipeEffectivenessMultiplierTF = new JTextField( 10 );
		Panel customRecipeEffectivenessMultiplierPanel = new Panel( customRecipeEffectivenessMultiplierTF, "カスタムレシピの効果" );
		
		// 製作速度によるカスタムレシピの性能
		customRecipeSkillMultiplierTF = new JTextField( 10 );
		Panel customRecipeSkillMultiplierPanel = new Panel( customRecipeSkillMultiplierTF, "<html>製作速度による<br>カスタムレシピの性能</html>" );
		
		// 製作スキルボーナス
		craftingSkillBonusMultiplierTF = new JTextField( 10 );
		Panel craftingSkillBonusMultiplierPanel = new Panel( craftingSkillBonusMultiplierTF, "製作スキルボーナス" );
		
		// 物資クレートの戦利品の品質
		supplyCrateLootQualityMultiplierTF = new JTextField( 10 );
		Panel supplyCrateLootQualityMultiplierPanel = new Panel( supplyCrateLootQualityMultiplierTF, "物資クレートの戦利品の品質" );
		
		// 釣り上げた戦利品の品質
		fishingLootQualityMultiplierTF = new JTextField( 10 );
		Panel fishingLootQualityMultiplierPanel = new Panel( fishingLootQualityMultiplierTF, "釣り上げた戦利品の品質" );
		
		// 燃料消費の間隔
		fuelConsumptionIntervalMultiplierTF = new JTextField( 10 );
		Panel fuelConsumptionIntervalMultiplierPanel = new Panel( fuelConsumptionIntervalMultiplierTF, "燃料消費の間隔" );
		
		// 土台の建造物設置上限
		perPlatformMaxStructuresMultiplierTF = new JTextField( 10 );
		Panel perPlatformMaxStructuresMultiplierPanel = new Panel( perPlatformMaxStructuresMultiplierTF, "<html>プラットフォームサドル上に<br>配置できる建造物の数</html>" );
		
		// ダメージテキストの表示
		showFloatingDamageTextCB = new JCheckBox( "ダメージテキストの表示" );
		Panel showFloatingDamageTextPanel = new Panel( showFloatingDamageTextCB, false );
		
		JPanel otherPanel = new Panel( 
				"その他の設定",
				17,
				Panel.CREAM,
				new int[] { 10, 2 },
				new int[] { windowSize[0] - 450, 1000 }, 
				new int[] { 1, 1, 1, 1 },  
				new Component[] {
						maxNumberOfPlayersInTribePanel,
						bAllowCustomRecipesPanel,
						overrideMaxExperiencePointsPlayerPanel,
						customRecipeEffectivenessMultiplierPanel,
						overrideMaxExperiencePointsDinoPanel,
						customRecipeSkillMultiplierPanel,
						bFlyerPlatformAllowUnalignedDinoBasingPanel,
						craftingSkillBonusMultiplierPanel,
						bPassiveDefensesDamageRiderlessDinosPanel,
						supplyCrateLootQualityMultiplierPanel,
						onlyAllowSpecifiedEngramsPanel,
						fishingLootQualityMultiplierPanel,
						allowRaidDinoFeedingPanel,
						fuelConsumptionIntervalMultiplierPanel,
						raidDinoCharacterFoodDrainMultiplierPanel,
						perPlatformMaxStructuresMultiplierPanel,
						bDisablePhotoModePanel,
						photoModeRangeLimitPanel,
						showFloatingDamageTextPanel,
						bUseCorpseLocatorPanel,
				} );
		otherSettingsPanel.add( otherPanel );
		mainPanel.add( "other", otherSettingsPanel.getScrollPane() );
	}
	
	/**
	 * コンフィグ切り替えボタンを配置するパネルのレイアウト
	 * @deprecated
	 */
	public void configsButtonLayout() {

		configButtonsPanel = new JPanel();
		configButtonsPanel.setBackground( Panel.CREAM );
		configButtonsPanel.setLayout( new GridLayout( 1, 12 ) );
		menu.add( configButtonsPanel );
		
		/**
		 * サーバー用の設定(主にゲーム内設定にない設定)
		 */
		JButton serverSettings = new Button( "サーバー", new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				clayout.show( mainPanel, "server" );				
			}
		});
		
		/**
		 * ゲームルール
		 * プレイヤー、恐竜、構造物、ワールド、ルール
		 */
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
		JavasSystem.addAll( configButtonsPanel, new JButton[] { serverSettings, playerSettings, dinoSettings, structureSettings, worldSettings, ruleSettings } );
		
		/**
		 * 詳細
		 * PvE, PvP, テイム済みの恐竜, ワールド, 経験値倍率, その他
		 */
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
			public void actionPerformed( ActionEvent e ) {
				clayout.show( mainPanel, "other" );
			}
		});
		JavasSystem.addAll( configButtonsPanel, new JButton[] { 
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
	 * 設定ファイルから値を読み取り、各々のテキストフィールドに値を格納する
	 * ロードボタンを押すと実行されるようにする
	 */
	void loadSettings() {
		
		// プロファイル名が空白なら「プロファイルがありません」とログに表示する
		if ( profTF.getText().equals( "" ) ) {
			console.print( "プロファイルがありません" );
			return;
		}
		
		gusBuilder = new GameUserSettingBuilder( profTF.getText() );
		for ( int i = 0; i < ConfigText.COLLECTIONS_SERVER.length; i++ ) {
			if ( TEXTFIELDS_SERVER[i] instanceof JTextField ) {
				JTextField jtf = (JTextField)TEXTFIELDS_SERVER[i];
				jtf.setText( gusBuilder.getServerSettingsValue( ConfigText.COLLECTIONS_SERVER[i] ) );
			}else if( TEXTFIELDS_SERVER[i] instanceof JCheckBox ) {
				JCheckBox jcb = (JCheckBox)TEXTFIELDS_SERVER[i];
				String str = gusBuilder.getServerSettingsValue( ConfigText.COLLECTIONS_SERVER[i] );
				if ( str.equals( "True" ) ) {
					jcb.setSelected( true );
				}else if ( str.equals( "False" ) ) {
					jcb.setSelected( false );
				}
			}
		}
		
		for ( int i = 0; i < ConfigText.COLLECTIONS_SESSION.length; i++ ) {
			if ( TEXTFIELDS_SESSION[i] instanceof JTextField ) {
				JTextField jtf = (JTextField)TEXTFIELDS_SESSION[i];
				jtf.setText( gusBuilder.getSessionSettingsValue( ConfigText.COLLECTIONS_SESSION[i] ) );
			}else if( TEXTFIELDS_SESSION[i] instanceof JCheckBox ) {
				JCheckBox jcb = (JCheckBox)TEXTFIELDS_SESSION[i];
				String str = gusBuilder.getSessionSettingsValue( ConfigText.COLLECTIONS_SESSION[i] );
				if ( str.equals( "True" ) ) {
					jcb.setSelected( true );
				}else if ( str.equals( "False" ) ) {
					jcb.setSelected( false );
				}
			}
		}
		
		for ( int i = 0; i < ConfigText.COLLECTIONS_GAMESESSION.length; i++ ) {
			if ( TEXTFIELDS_GAMESESSION[i] instanceof JTextField ) {
				JTextField jtf = (JTextField)TEXTFIELDS_GAMESESSION[i];
				jtf.setText( gusBuilder.getEngineGameSessionValue( ConfigText.COLLECTIONS_GAMESESSION[i] ) );
			}else if( TEXTFIELDS_GAMESESSION[i] instanceof JCheckBox ) {
				JCheckBox jcb = (JCheckBox)TEXTFIELDS_GAMESESSION[i];
				String str = gusBuilder.getEngineGameSessionValue( ConfigText.COLLECTIONS_GAMESESSION[i] );
				if ( str.equals( "True" ) ) {
					jcb.setSelected( true );
				}else if ( str.equals( "False" ) ) {
					jcb.setSelected( false );
				}
			}
		}
		
		for ( int i = 0; i < ConfigText.COLLECTIONS_MESSAGE.length; i++ ) {
			if ( TEXTFIELDS_MESSAGE[i] instanceof JTextField ) {
				JTextField jtf = (JTextField)TEXTFIELDS_MESSAGE[i];
				jtf.setText( gusBuilder.getMessageOfTheDayValue( ConfigText.COLLECTIONS_MESSAGE[i] ) );
			}else if( TEXTFIELDS_MESSAGE[i] instanceof JCheckBox ) {
				JCheckBox jcb = (JCheckBox)TEXTFIELDS_MESSAGE[i];
				String str = gusBuilder.getMessageOfTheDayValue( ConfigText.COLLECTIONS_MESSAGE[i] );
				if ( str.equals( "True" ) ) {
					jcb.setSelected( true );
				}else if ( str.equals( "False" ) ) {
					jcb.setSelected( false );
				}
			}
		}
		
		gBuilder = new GameBuilder( profTF.getText() );
		for ( int i = 0; i < ConfigText.COLLECTIONS_SHOOTERGAMEMODE.length; i++ ) {
			if ( TEXTFIELDS_SHOOTERGAMEMODE[i] instanceof JTextField ) {
				JTextField jtf = (JTextField)TEXTFIELDS_SHOOTERGAMEMODE[i];
				jtf.setText( gBuilder.getShooterGameModeSettingsValue( ConfigText.COLLECTIONS_SHOOTERGAMEMODE[i] ) );
			}else if( TEXTFIELDS_SHOOTERGAMEMODE[i] instanceof JCheckBox ) {
				JCheckBox jcb = (JCheckBox)TEXTFIELDS_SHOOTERGAMEMODE[i];
				String str = gBuilder.getShooterGameModeSettingsValue( ConfigText.COLLECTIONS_SHOOTERGAMEMODE[i] );
				if ( str.equals( "True" ) ) {
					jcb.setSelected( true );
				}else if ( str.equals( "False" ) ) {
					jcb.setSelected( false );
				}
			}
		}
		
		console.print( "プロファイル「" + profTF.getText() + "」をロードしました!" );
	}
	
	/**
	 * 変更内容(現在テキストフィールドに書かれている内容)を書き込む
	 */
	void modifySettings() {
		if ( gusBuilder == null || gBuilder == null ) {
			console.print( "プロファイルがロードされていないか、ありません" );
			return;
		}
		
		for ( int i = 0; i < ConfigText.COLLECTIONS_SERVER.length; i++ ) {
			if ( TEXTFIELDS_SERVER[i] instanceof JTextField ) {
				JTextField jtf = (JTextField)TEXTFIELDS_SERVER[i];
				gusBuilder.setServerSettingsValue( ConfigText.COLLECTIONS_SERVER[i], jtf.getText() );
			}else if( TEXTFIELDS_SERVER[i] instanceof JCheckBox ) {
				JCheckBox jcb = (JCheckBox)TEXTFIELDS_SERVER[i];
				if ( jcb.isSelected() == true ) {
					gusBuilder.setServerSettingsValue( ConfigText.COLLECTIONS_SERVER[i], "True" );
				}else if ( jcb.isSelected() == false ) {
					gusBuilder.setServerSettingsValue( ConfigText.COLLECTIONS_SERVER[i], "False" );
				}
			}
		}
		
		for ( int i = 0; i < ConfigText.COLLECTIONS_SESSION.length; i++ ) {
			if ( TEXTFIELDS_SESSION[i] instanceof JTextField ) {
				JTextField jtf = (JTextField)TEXTFIELDS_SESSION[i];
				gusBuilder.setSessionSettingsValue( ConfigText.COLLECTIONS_SESSION[i], jtf.getText() );
			}else if( TEXTFIELDS_SESSION[i] instanceof JCheckBox ) {
				JCheckBox jcb = (JCheckBox)TEXTFIELDS_SESSION[i];
				if ( jcb.isSelected() == true ) {
					gusBuilder.setSessionSettingsValue( ConfigText.COLLECTIONS_SESSION[i], "True" );
				}else if ( jcb.isSelected() == false ) {
					gusBuilder.setSessionSettingsValue( ConfigText.COLLECTIONS_SESSION[i], "False" );
				}
			}
		}
		
		for ( int i = 0; i < ConfigText.COLLECTIONS_GAMESESSION.length; i++ ) {
			if ( TEXTFIELDS_GAMESESSION[i] instanceof JTextField ) {
				JTextField jtf = (JTextField)TEXTFIELDS_GAMESESSION[i];
				gusBuilder.setEngineGameSessionValue( ConfigText.COLLECTIONS_GAMESESSION[i], jtf.getText() );
			}else if( TEXTFIELDS_GAMESESSION[i] instanceof JCheckBox ) {
				JCheckBox jcb = (JCheckBox)TEXTFIELDS_GAMESESSION[i];
				if ( jcb.isSelected() == true ) {
					gusBuilder.setEngineGameSessionValue( ConfigText.COLLECTIONS_GAMESESSION[i], "True" );
				}else if ( jcb.isSelected() == false ) {
					gusBuilder.setEngineGameSessionValue( ConfigText.COLLECTIONS_GAMESESSION[i], "False" );
				}
			}
		}
		
		for ( int i = 0; i < ConfigText.COLLECTIONS_MESSAGE.length; i++ ) {
			if ( TEXTFIELDS_MESSAGE[i] instanceof JTextField ) {
				JTextField jtf = (JTextField)TEXTFIELDS_MESSAGE[i];
				gusBuilder.setMessageOfTheDayValue( ConfigText.COLLECTIONS_MESSAGE[i], jtf.getText() );
			}else if( TEXTFIELDS_MESSAGE[i] instanceof JCheckBox ) {
				JCheckBox jcb = (JCheckBox)TEXTFIELDS_MESSAGE[i];
				if ( jcb.isSelected() == true ) {
					gusBuilder.setMessageOfTheDayValue( ConfigText.COLLECTIONS_MESSAGE[i], "True" );
				}else if ( jcb.isSelected() == false ) {
					gusBuilder.setMessageOfTheDayValue( ConfigText.COLLECTIONS_MESSAGE[i], "False" );
				}
			}
		}
		
		for ( int i = 0; i < ConfigText.COLLECTIONS_SHOOTERGAMEMODE.length; i++ ) {
			if ( TEXTFIELDS_SHOOTERGAMEMODE[i] instanceof JTextField ) {
				JTextField jtf = (JTextField)TEXTFIELDS_SHOOTERGAMEMODE[i];
				gBuilder.setShooterGameModeSettingsValue( ConfigText.COLLECTIONS_SHOOTERGAMEMODE[i], jtf.getText() );
			}else if( TEXTFIELDS_SHOOTERGAMEMODE[i] instanceof JCheckBox ) {
				JCheckBox jcb = (JCheckBox)TEXTFIELDS_SHOOTERGAMEMODE[i];
				if ( jcb.isSelected() == true ) {
					gBuilder.setShooterGameModeSettingsValue( ConfigText.COLLECTIONS_SHOOTERGAMEMODE[i], "True" );
				}else if ( jcb.isSelected() == false ) {
					gBuilder.setShooterGameModeSettingsValue( ConfigText.COLLECTIONS_SHOOTERGAMEMODE[i], "False" );
				}
			}
		}
	}
	
	/**
	 * ASA Server Managerウィンドウを起動
	 */
	public void start() {
		if ( th == null ) {
			th = new Thread( this );
			th.start();
		}
	}
	
	/**
	 * ASA Server Managerウィンドウを停止
	 */
	public void stop() {
		if ( th != null ) {
			th = null;
		}
	}
	
	@Override
	public void run() {
		while ( th != null ) {
			try {
				Thread.sleep( 16 );
				repaint();
			} catch ( InterruptedException e ) { e.printStackTrace(); }
		}
	}
}