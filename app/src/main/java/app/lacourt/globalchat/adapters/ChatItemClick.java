package app.lacourt.globalchat.adapters;


import app.lacourt.globalchat.model.Chat;

public interface ChatItemClick {
    void onChatClick(String name, String chatId);
    void onChatLongClick(Chat chat);
}
