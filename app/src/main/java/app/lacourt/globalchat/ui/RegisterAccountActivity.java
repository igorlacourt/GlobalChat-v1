package app.lacourt.globalchat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import app.lacourt.globalchat.R;
import app.lacourt.globalchat.utils.DialogHandler;

public class RegisterAccountActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private Button buttonCreateAccount;
//    private RegisterAccountViewModel viewModel;
    private final Context mContext = this;
    private AppCompatActivity activity;
    private DialogHandler dialogHandler = null;
    private Toast toast = null;
    private ImageButton exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        exitButton = (ImageButton) findViewById(R.id.exit_button);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        buttonCreateAccount = (Button) findViewById(R.id.btn_create_account);
        dialogHandler = DialogHandler.getInstance();

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void startMainScreen() {
//        displayToast(getString(R.string.toast_log_in_with_your_new_account));
        dialogHandler.dismissLoadingBarDialog(activity);

        Intent intent = new Intent(mContext, MainScreenActivity.class);
        startActivity(intent);
        finish();

    }

    private void displayToast(String message) {
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,100);
        toast.show();
    }

    private void sendBroadCast() {
        Intent userDataChange = new Intent(getString(R.string.broad_cast_user_data_change));
        LocalBroadcastManager.getInstance(activity).sendBroadcast(userDataChange);
    }

    @Override
    protected void onDestroy() {
        dialogHandler.dismissLoadingBarDialog(activity);
        super.onDestroy();
    }

    public void onExit(View view) {
        finish();
    }
}





