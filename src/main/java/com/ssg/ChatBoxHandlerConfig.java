package com.ssg;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("ssgchatboxhandler")
public interface ChatBoxHandlerConfig extends Config {
	@ConfigSection(
			name = "Beta 1.2",
			description = "Version control",
			position = -1
	)
	String versionControl = "versionControl";
	@ConfigSection(
			name = "Settings",
			description = "Various settings for the plugin",
			position = 0
	)
	String Settings = "Settings";

	@ConfigItem(
			keyName = "enablePlugin",
			name = "Enable",
			description = "Enable the plugin",
			position = 0,
			section = Settings
	)
	default boolean enablePlugin() { return true; }

	@ConfigItem(
			keyName = "autoMake",
			name = "Auto inventory make all",
			description = "Auto press space when the option appears in chat box like cooking, smelting ores, fletching, crafting etc",
			position = 1,
			section = Settings
	)
	default boolean autoMake() { return false; }

	@ConfigItem(
			keyName = "autoJewelry",
			name = "Auto smelt jewelry",
			description = "Auto press space when smelting jewelry at a furnace",
			position = 2,
			section = Settings
	)
	default boolean autoJewelry() { return false; }

	@ConfigItem(
			keyName = "autoAnvil",
			name = "Auto anvil",
			description = "Auto press space on anvil widget",
			position = 3,
			section = Settings
	)
	default boolean autoAnvil() { return false; }

}
