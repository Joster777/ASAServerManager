package com.stevejavas.config;

/**
 * GameUserSettings.iniとかGame.iniとかのコンフィグの名前を書いとくやつ
 * プロファイルのロード、保存するときに使う
 */
public class ConfigText {
	
	/**
	 * GameUserSettings.iniの[ServerSettings]
	 */
	public static final String[] COLLECTIONS_SERVER = {
		"DayCycleSpeedScale",
		"DayTimeSpeedScale",
		"NightTimeSpeedScale",
		"ShowMapPlayerLocation", 
		"AllowThirdPersonPlayer", 
		"ServerCrosshair", 
		"RCONPort",
		"TheMaxStructuresInRange",
		"StructurePreventResourceRadiusMultiplier",
		"TribeNameChangeCooldown",
		"PlatformSaddleBuildAreaBoundsMultiplier",
		"StructurePickupTimeAfterPlacement",
		"StructurePickupHoldDuration",
		"AllowIntegratedSPlusStructures", 
		"AllowHideDamageSourceFromLogs", 
		"AutoSavePeriodMinutes",
		"MaxTamedDinos",
		"RCONServerGameLogBuffer",
		"AllowHitMarkers", 
		"AllowCrateSpawnsOnTopOfStructures", 
		"ServerPassword",
		"ServerAdminPassword",
		"SpectatorPassword", 
		"RCONEnabled", 
		"AdminLogging", 
		"ActiveMods", 
		"TribeLogDestroyedEnemyStructures", 
		"AllowSharedConnections", 
		"ServerHardcore", 
		"ServerPVE", 
		"AllowCaveBuildingPvE", 
		"EnableExtraStructurePreventionVolumes", 
		"OverrideOfficialDifficulty",
		"DifficultyOffset",
		"NoTributeDownloads", 
		"PreventUploadSurvivors", 
		"PreventUploadItems", 
		"PreventUploadDinos", 
		"CrossARKAllowForeignDinoDownloads", 
		"PreventOfflinePvP", 
		"PreventTribeAlliances", 
		"PreventDiseases", 
		"NonPermanentDiseases", 
		"PreventSpawnAnimations", 
		"MaxGateFrameOnSaddles",
		"AllowTekSuitPowersInGenesis", 
		"EnableCryoSicknessPVE", 
		"MaxHexagonsPerCharacter",
		"UseFjordurTraversalBuff", 
		"globalVoiceChat", 
		"proximityChat", 
		"alwaysNotifyPlayerLeft", 
		"alwaysNotifyPlayerJoined", 
		"ServerForceNoHud", 
		"EnablePVPGamma", 
		"DisablePvEGamma", 
		"ShowFloatingDamageText", 
		"AllowFlyerCarryPVE", 
		"XPMultiplier",
		"AllowRaidDinoFeeding", 
		"DisableDinoDecayPvE", 
		"PvPDinoDecay", 
		"AutoDestroyDecayedDinos", 
		"AllowMultipleAttachedC4", 
		"MaxPersonalTamedDinos",
		"PersonalTamedDinosSaddleStructureCost",
		"DisableImprintDinoBuff", 
		"AllowAnyoneBabyImprintCuddle", 
		"TamingSpeedMultiplier",
		"HarvestAmountMultiplier",
		"UseOptimizedHarvestingHealth", 
		"ClampResourceHarvestDamage", 
		"ClampItemSpoilingTimes", 
		"DisableWeatherFog", 
		"PvPStructureDecay", 
		"PvEAllowStructuresAtSupplyDrops", 
		"DisableStructureDecayPVE", 
		"ForceAllStructureLocking", 
		"OnlyAutoDestroyCoreStructures", 
		"OnlyDecayUnsnappedCoreStructures", 
		"FastDecayUnsnappedCoreStructures", 
		"DestroyUnconnectedWaterPipes", 
		"AlwaysAllowStructurePickup", 
		"KickIdlePlayersPeriod",
		"PreventDownloadSurvivors", 
		"PreventDownloadItems", 
		"PreventDownloadDinos", 
		"MaxTributeDinos",
		"MaxTributeItems",
		"ServerAutoForceRespawnWildDinosInterval",
		"bFilterTribeNames", 
		"bFilterCharacterNames", 
		"bFilterChat", 
		"AllowCaveBuildingPvP", 
		"RandomSupplyCratePoints", 
		"DontAlwaysNotifyPlayerJoined", 
		"IgnoreLimitMaxStructuresInRangeTypeFlag", 
		"StartTimeHour",
		"ImplantSuicideCD",
		"OxygenSwimSpeedStatMultiplier",
		"RaidDinoCharacterFoodDrainMultiplier",
		"PvEDinoDecayPeriodMultiplier",
		"PerPlatformMaxStructuresMultiplier",
		"ItemStackSizeMultiplier",
		"MaxTamedDinos_SoftTameLimit",
		"MaxTamedDinos_SoftTameLimit_CountdownForDeletionDuration",
		"StartTimeOverride",//
		"PlayerDamageMultiplier",
		"PlayerResistanceMultiplier",
		"PlayerCharacterWaterDrainMultiplier",
		"PlayerCharacterFoodDrainMultiplier",
		"PlayerCharacterStaminaDrainMultiplier",
		"PlayerCharacterHealthRecoveryMultiplier",
		"DinoCountMultiplier",
		"DinoDamageMultiplier",
		"DinoResistanceMultiplier",
		"DinoCharacterFoodDrainMultiplier",
		"DinoCharacterStaminaDrainMultiplier",
		"DinoCharacterHealthRecoveryMultiplier",
		"StructureDamageMultiplier",
		"StructureResistanceMultiplier",
		"PvEStructureDecayPeriodMultiplier",
		"ForceAllowCaveFlyers",
		"OverrideStructurePlatformPrevention",
		"HarvestHealthMultiplier",
		"ResourcesRespawnPeriodMultiplier",
	};
	
	/**
	 * GameUserSettings.iniの[SessionSettings]
	 */
	public static final String[] COLLECTIONS_SESSION = {
			"SessionName",
			"Port",
			"QueryPort",
			"MultiHome"
	};
	
	/**
	 * GameUserSettings.iniの[/Script/Engine.GameSession]
	 */
	public static final String[] COLLECTIONS_GAMESESSION = {
			"MaxPlayers"
	};
	
	/**
	 * GameUserSettings.iniの[MessageOfTheDay]
	 */
	public static final String[] COLLECTIONS_MESSAGE = {
			"Message",
			"Duration",
			"MessageSetterID"
	};
	
	/**
	 * Game.iniの[ShooterGameMode]
	 */
	public static final String[] COLLECTIONS_SHOOTERGAMEMODE = {
			
			"BabyImprintingStatScaleMultiplier",
			"BabyCuddleIntervalMultiplier",
			"BabyCuddleGracePeriodMultiplier",
			"BabyCuddleLoseImprintQualitySpeedMultiplier",
			"PerLevelStatsMultiplier_DinoTamed[0]",
			"PerLevelStatsMultiplier_DinoTamed[1]",
			"PerLevelStatsMultiplier_DinoTamed[2]",
			"PerLevelStatsMultiplier_DinoTamed[3]",
			"PerLevelStatsMultiplier_DinoTamed[4]",
			"PerLevelStatsMultiplier_DinoTamed[5]",
			"PerLevelStatsMultiplier_DinoTamed[6]",
			"PerLevelStatsMultiplier_DinoTamed[7]",
			"PerLevelStatsMultiplier_DinoTamed[8]",
			"PerLevelStatsMultiplier_DinoTamed[9]",
			"PerLevelStatsMultiplier_DinoTamed[10]",
			"PerLevelStatsMultiplier_DinoTamed_Add[0]",
			"PerLevelStatsMultiplier_DinoTamed_Add[1]",
			"PerLevelStatsMultiplier_DinoTamed_Add[2]",
			"PerLevelStatsMultiplier_DinoTamed_Add[3]",
			"PerLevelStatsMultiplier_DinoTamed_Add[4]",
			"PerLevelStatsMultiplier_DinoTamed_Add[5]",
			"PerLevelStatsMultiplier_DinoTamed_Add[6]",
			"PerLevelStatsMultiplier_DinoTamed_Add[7]",
			"PerLevelStatsMultiplier_DinoTamed_Add[8]",
			"PerLevelStatsMultiplier_DinoTamed_Add[9]",
			"PerLevelStatsMultiplier_DinoTamed_Add[10]",
			"PerLevelStatsMultiplier_DinoTamed_Affinity[0]",
			"PerLevelStatsMultiplier_DinoTamed_Affinity[1]",
			"PerLevelStatsMultiplier_DinoTamed_Affinity[2]",
			"PerLevelStatsMultiplier_DinoTamed_Affinity[3]",
			"PerLevelStatsMultiplier_DinoTamed_Affinity[4]",
			"PerLevelStatsMultiplier_DinoTamed_Affinity[5]",
			"PerLevelStatsMultiplier_DinoTamed_Affinity[6]",
			"PerLevelStatsMultiplier_DinoTamed_Affinity[7]",
			"PerLevelStatsMultiplier_DinoTamed_Affinity[8]",
			"PerLevelStatsMultiplier_DinoTamed_Affinity[9]",
			"PerLevelStatsMultiplier_DinoTamed_Affinity[10]",
			"PerLevelStatsMultiplier_DinoWild[0]",
			"PerLevelStatsMultiplier_DinoWild[1]",
			"PerLevelStatsMultiplier_DinoWild[2]",
			"PerLevelStatsMultiplier_DinoWild[3]",
			"PerLevelStatsMultiplier_DinoWild[4]",
			"PerLevelStatsMultiplier_DinoWild[5]",
			"PerLevelStatsMultiplier_DinoWild[6]",
			"PerLevelStatsMultiplier_DinoWild[7]",
			"PerLevelStatsMultiplier_DinoWild[8]",
			"PerLevelStatsMultiplier_DinoWild[9]",
			"PerLevelStatsMultiplier_DinoWild[10]",
			"PerLevelStatsMultiplier_Player[0]",
			"PerLevelStatsMultiplier_Player[1]",
			"PerLevelStatsMultiplier_Player[2]",
			"PerLevelStatsMultiplier_Player[3]",
			"PerLevelStatsMultiplier_Player[4]",
			"PerLevelStatsMultiplier_Player[5]",
			"PerLevelStatsMultiplier_Player[6]",
			"PerLevelStatsMultiplier_Player[7]",
			"PerLevelStatsMultiplier_Player[8]",
			"PerLevelStatsMultiplier_Player[9]",
			"PerLevelStatsMultiplier_Player[10]",
			"GlobalSpoilingTimeMultiplier",
			"GlobalItemDecompositionTimeMultiplier",
			"GlobalCorpseDecompositionTimeMultiplier",
			"PvPZoneStructureDamageMultiplier",
			"StructureDamageRepairCooldown",
			"IncreasePvPRespawnIntervalCheckPeriod",
			"IncreasePvPRespawnIntervalMultiplier",
			"IncreasePvPRespawnIntervalBaseAmount",
			"ResourceNoReplenishRadiusPlayers",
			"ResourceNoReplenishRadiusStructures",
			"CropGrowthSpeedMultiplier",
			"LayEggIntervalMultiplier",
			"PoopIntervalMultiplier",
			"CropDecaySpeedMultiplier",
			"MatingIntervalMultiplier",
			"EggHatchSpeedMultiplier",
			"BabyMatureSpeedMultiplier",
			"BabyFoodConsumptionSpeedMultiplier",
			"DinoTurretDamageMultiplier",
			"DinoHarvestingDamageMultiplier",
			"PlayerHarvestingDamageMultiplier",
			"CustomRecipeEffectivenessMultiplier",
			"CustomRecipeSkillMultiplier",
			"AutoPvEStartTimeSeconds",
			"AutoPvEStopTimeSeconds",
			"KillXPMultiplier",
			"HarvestXPMultiplier",
			"CraftXPMultiplier",
			"GenericXPMultiplier",
			"SpecialXPMultiplier",
			"FuelConsumptionIntervalMultiplier",
			"PhotoModeRangeLimit",
			"bDisablePhotoMode",
			"bIncreasePvPRespawnInterval",
			"bAutoPvETimer",
			"bAutoPvEUseSystemTime",
			"bDisableFriendlyFire",
			"bFlyerPlatformAllowUnalignedDinoBasing",
			"bDisableLootCrates",
			"bAllowCustomRecipes",
			"bPassiveDefensesDamageRiderlessDinos",
			"bPvEAllowTribeWar",
			"bPvEAllowTribeWarCancel",
			"MaxDifficulty",
			"bUseSingleplayerSettings",
			"bUseCorpseLocator",
			"bShowCreativeMode",
			"bHardLimitTurretsInRange",
			"bDisableStructurePlacementCollision",
			"bAllowPlatformSaddleMultiFloors",
			"bAllowUnlimitedRespecs",
			"bDisableDinoRiding",
			"bDisableDinoTaming",
			"OverrideMaxExperiencePointsPlayer",
			"OverrideMaxExperiencePointsDino",
			"MaxNumberOfPlayersInTribe",
			"ExplorerNoteXPMultiplier",
			"BossKillXPMultiplier",
			"AlphaKillXPMultiplier",
			"WildKillXPMultiplier",
			"CaveKillXPMultiplier",
			"TamedKillXPMultiplier",
			"UnclaimedKillXPMultiplier",
			"SupplyCrateLootQualityMultiplier",
			"FishingLootQualityMultiplier",
			"CraftingSkillBonusMultiplier",
			"bAllowSpeedLeveling",
			"bAllowFlyerSpeedLeveling",
	};
	
	/**
	 * いらない
	 */
	public static final String[] COLLECTIONS_TEMPOVERRIDES = {
			"bUseCorpseLocator"
	};
}