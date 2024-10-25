package com.example.TelegramBotUpgradePhoto;

import com.example.TelegramBotUpgradePhoto.components.BotCommands;
import com.example.TelegramBotUpgradePhoto.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot implements BotCommands {
    final BotConfig config;

    public Bot(BotConfig config) {
        this.config = config;

        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
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
        long chatId = 0;
        long userID = 0;
        String username = null;
        String receivedMessage;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userID = update.getMessage().getFrom().getId();
            username = update.getMessage().getChat().getFirstName();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, username);
            }
        }
        else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userID = update.getCallbackQuery().getFrom().getId();
            username = update.getCallbackQuery().getFrom().getFirstName();
            receivedMessage = update.getCallbackQuery().getData();

            botAnswerUtils(receivedMessage, chatId, username);
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String username) {
        switch (receivedMessage) {
            case "/start":
                startBot(chatId, username);
                break;
            case "/help":
                sendHepText(chatId, HELP_TEXT);
                break;
            default:
                break;
        }
    }

    private void startBot(long chatId, String username) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Hello, " + username);

        try {
            execute(sendMessage);
            log.info("Bot started");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendHepText(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);

        try {
            execute(sendMessage);
            log.info("Help send");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
