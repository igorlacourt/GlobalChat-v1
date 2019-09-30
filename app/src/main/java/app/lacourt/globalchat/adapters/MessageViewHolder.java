package app.lacourt.globalchat.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import app.lacourt.globalchat.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView sender;
    public TextView messageBody;


    public MessageViewHolder(View itemView) {
        super(itemView);
        sender = (TextView) itemView.findViewById(R.id.message_user_name);
        messageBody = (TextView) itemView.findViewById(R.id.message_body);
    }

}