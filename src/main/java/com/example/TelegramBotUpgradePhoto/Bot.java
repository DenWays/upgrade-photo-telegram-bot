package com.example.TelegramBotUpgradePhoto;

import com.example.TelegramBotUpgradePhoto.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {
    final BotConfig config;

    public Bot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            String username = update.getMessage().getFrom().getFirstName();
            long chatId = update.getMessage().getChatId();

            switch (message) {
                case "/start":
                    startBot(chatId, username);
                    break;
                default:
                    log.info("Unknown command");
                    break;
            }
        }
    }

    private void startBot(long chatId, String username) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Hello, " + username);

        try {
            execute(sendMessage);
            log.info("Bot started");
        }
        catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
