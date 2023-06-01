package com.ssg;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.KeyCode;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.awt.*;
import java.awt.event.KeyEvent;

@Slf4j
@Singleton
@PluginDescriptor(
		name = "SSG Chatbox Handler",
		enabledByDefault = false,
		description = "Handles chatbox events",
		tags = {"Chat", "Continue", "Clicker", "SSG"}
)
public class ChatBoxHandlerPlugin extends Plugin {
	@Inject
	private Client client;
	@Inject
	private ChatBoxHandlerConfig config;

	private static final int DIALOGUE_NPC_CONTINUE = 15138821;
	private static final int DIALOGUE_PLAYER_CONTINUE = 14221317;
	private static final int GOTR_PORTAL_CONTINUE = 15007746;
	private static final int LEVEL_UP_POPUP = 15269888;
	private static final int DIALOGUE_CHOICE_BOX = 14352385;
	private static final int DIALOGUE_CHOICE_PARAM1 = 14352385;
	private static final int INVENTORY_MAKE_ALL = 17694732;
	private static final int SMELTING_MAKE_ALL = 29229120;
	private static final int ANVIL_MAKE_ALL = 20447239;
	private static final String CONTINUE_OPTION = "Click here to continue";


	@Provides
	ChatBoxHandlerConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ChatBoxHandlerConfig.class);
	}

	@Override
	protected void startUp() throws Exception {

	}

	@Override
	protected void shutDown() throws Exception {

	}

	@Subscribe
	private void onGameTick(GameTick tick) throws AWTException {
		Widget npcContinue = client.getWidget(DIALOGUE_NPC_CONTINUE);
		Widget playerContinue = client.getWidget(DIALOGUE_PLAYER_CONTINUE);
		Widget dialogueChoice = client.getWidget(DIALOGUE_CHOICE_BOX);
		Widget gotrPortalContinue = client.getWidget(GOTR_PORTAL_CONTINUE);
		Widget levelUpWidget = client.getWidget(WidgetInfo.LEVEL_UP);
		Widget levelUpSkillWidget = client.getWidget(WidgetInfo.LEVEL_UP_SKILL);
		Widget levelUpLevelWidget = client.getWidget(WidgetInfo.LEVEL_UP_LEVEL);
		Widget levelUpPopup = client.getWidget(LEVEL_UP_POPUP);
		Widget craftAllWidget = client.getWidget(INVENTORY_MAKE_ALL);
		Widget smeltAllWidget = client.getWidget(SMELTING_MAKE_ALL);
		Widget anvilAllWidget = client.getWidget(ANVIL_MAKE_ALL);

		if (config.enablePlugin()) {
			if (levelUpWidget != null || levelUpLevelWidget != null || levelUpSkillWidget != null || levelUpPopup != null) {
				pressKey(KeyEvent.VK_SPACE);
			}

			if (craftAllWidget != null && config.autoMake()) {
				pressKey(KeyEvent.VK_SPACE);
			}

			if (smeltAllWidget != null && config.autoJewelry()) {
				pressKey(KeyEvent.VK_SPACE);
			}

			if (anvilAllWidget != null && config.autoAnvil()) {
				pressKey(KeyEvent.VK_SPACE);
			}

			if (npcContinue != null && npcContinue.getText().contains(CONTINUE_OPTION)) {
				pressKey(KeyEvent.VK_SPACE);
			}

			if (playerContinue != null && playerContinue.getText().contains(CONTINUE_OPTION)) {
				pressKey(KeyEvent.VK_SPACE);
			}

			if (gotrPortalContinue != null && gotrPortalContinue.getText().contains(CONTINUE_OPTION)) {
				pressKey(KeyEvent.VK_SPACE);
			}

			if (dialogueChoice != null) {
				for (Widget child : dialogueChoice.getChildren()) {
					String text = child.getText();
					if (text.contains("[1]") || text.contains("[2]") || text.contains("[3]") || text.contains("[4]") || text.contains("[5]")) {
						log.info("We see quest dialogue choice, it is: " +text);
						chooseDialogueOption(extractOptionFromText(text));
					}
				}
			}
		}
	}

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event) {
		log.info("" + event.getMenuAction());
	}

	private void chooseDialogueOption(int option) {
		int keyCode = KeyEvent.VK_0 + option;
		pressKey(keyCode);
	}
	private int extractOptionFromText(String text) {
		String optionText = text.replaceAll("[^\\d]", "");
		if (!optionText.isEmpty()) {
			return Integer.parseInt(optionText);
		}

		return 0;
	}

	private void pressKey(int keyCode) {
		log.info("Pressing key with keycode: " + keyCode);
		long eventTime = System.currentTimeMillis();
		int modifiers = 0;

		KeyEvent keyPress = new KeyEvent(
				client.getCanvas(),
				KeyEvent.KEY_PRESSED,
				eventTime,
				modifiers,
				keyCode
		);
		client.getCanvas().dispatchEvent(keyPress);

		KeyEvent keyRelease = new KeyEvent(
				client.getCanvas(),
				KeyEvent.KEY_RELEASED,
				eventTime,
				modifiers,
				keyCode
		);
		client.getCanvas().dispatchEvent(keyRelease);
	}
}
