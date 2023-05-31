package com.ssg;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;

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
				pressSpace();
			}

			if (craftAllWidget != null && config.autoMake()) {
				pressSpace();
			}

			if (smeltAllWidget != null && config.autoJewelry()) {
				pressSpace();
			}

			if (anvilAllWidget != null && config.autoAnvil()) {
				pressSpace();
			}

			if (npcContinue != null && npcContinue.getText().contains(CONTINUE_OPTION)) {
				pressSpace();
			}

			if (playerContinue != null && playerContinue.getText().contains(CONTINUE_OPTION)) {
				pressSpace();
			}

			if (gotrPortalContinue != null && gotrPortalContinue.getText().contains(CONTINUE_OPTION)) {
				pressSpace();
			}

			if (dialogueChoice != null) {
				for (Widget child : dialogueChoice.getChildren()) {
					String text = child.getText();
					int option = extractDialogueOption(text);
					if (option != -1) {
						chooseDialogueOption(option);
					}
				}
			}
		}
	}

	private void chooseDialogueOption(int option) {
		pressNumberKey(option);
	}

	// Presses the specified number key on the keyboard
	private void pressNumberKey(int number) {
		int keyCode = KeyEvent.VK_0 + number; // Calculate the corresponding key code for the number
		pressKey(keyCode);
	}

	// Presses a key with the given key code
	private void pressKey(int keyCode) {
		long eventTime = System.currentTimeMillis();

		KeyEvent keyPress = new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_PRESSED, eventTime, 0, keyCode);
		this.client.getCanvas().dispatchEvent(keyPress);

		KeyEvent keyRelease = new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_RELEASED, eventTime, 0, keyCode);
		this.client.getCanvas().dispatchEvent(keyRelease);
	}


	private void pressSpace() {
		KeyEvent keyPress = new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), BUTTON1_DOWN_MASK, KeyEvent.VK_SPACE);
		this.client.getCanvas().dispatchEvent(keyPress);
		KeyEvent keyRelease = new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE);
		this.client.getCanvas().dispatchEvent(keyRelease);
	}

	private int extractDialogueOption(String text) {
		Pattern pattern = Pattern.compile("\\[(\\d+)\\]");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		}
		return -1;
	}
}
