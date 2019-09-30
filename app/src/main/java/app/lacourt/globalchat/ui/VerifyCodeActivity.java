package app.lacourt.globalchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import app.lacourt.globalchat.R;

public class VerifyCodeActivity extends AppCompatActivity {
    private String verificationId;
    private TextView tvWarn;
    private EditText code;
    private ImageButton exit;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        tvWarn = (TextView) findViewById(R.id.tv_warn);
        tvWarn.setText("Insert the code we sent to you.");
        code = (EditText) findViewById(R.id.verification_code);
        exit = (ImageButton) findViewById(R.id.exit_verification_button);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        verify = (Button) findViewById(R.id.verify_button);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyPhoneNumberWithCode();
            }
        });

        verificationId = getIntent().getStringExtra("verification_id");
        Log.d("myverif", "verificationId = " + verificationId);

        code.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    verifyPhoneNumberWithCode();
                    return true;
                }
                return false;
            }
        });
    }

    private void verifyPhoneNumberWithCode() {
        String insertedCode = code.getText().toString();

        if (code != null && !insertedCode.isEmpty()) {

            if(insertedCode.length() == 6) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, insertedCode);
                signInWithPhoneAuthCredentail(credential);

            } else {
                Toast.makeText(VerifyCodeActivity.this, getString(R.string.warn_insert_code_correctly), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(VerifyCodeActivity.this, getString(R.string.warn_insert_verif_code), Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredentail(PhoneAuthCredential phoneAuthCredential) {
        tvWarn.setText("Verifying code...");

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null) {
                        final DatabaseReference userDb = FirebaseDatabase.getInstance().getReference()
                                .child("user")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        //Insert user infomation into the database
                        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    Log.d("myverif", "dataSnaphot exists!");
                                    //Insert new user into Real Time Database.
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("phone", user.getPhoneNumber());
                                    userMap.put("name", user.getPhoneNumber());
                                    userDb.updateChildren(userMap);

                                }
                                userIsLoggedIn();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("myverif", "database call cencelled. Error: " + databaseError.getMessage());
                            }
                        });
                    }
                }
                else {

                    Log.d("myverif", "signInWithPhoneAuthCredentail task NOT successfull: " + task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        tvWarn.setText(":/ Invalid code.");
                    }

                }
            }
        });
    }



    private void userIsLoggedIn() {
        Log.d("myverif", "userIsLoggedIn called.");
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.d("myverif", "user IS logged in.");
            startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
            setResult(getResources().getInteger(R.integer.VERIFICATION_RESULT));
            finish();
            return;
        } else {
            Log.d("myverif", "user NOT logged in.");
        }
    }
}
