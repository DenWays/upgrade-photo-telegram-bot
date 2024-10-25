package com.example.TelegramBotUpgradePhoto.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot"),
            new BotCommand("/help", "bot info")
    );

    String HELP_TEXT = "Данный бот с помощью нейросети улучшит ваши фотографии.\n" +
            "/start - запуск бота\n" +
            "/help - информация о боте";
}
