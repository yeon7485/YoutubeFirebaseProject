package com.kplo.youtubefirebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private EditText mNickname, mEmailText, mPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mNickname = findViewById(R.id.signup_nickname);
        mEmailText = findViewById(R.id.signup_email);
        mPasswordText = findViewById(R.id.signup_password);

        findViewById(R.id.signup_success).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        mAuth.createUserWithEmailAndPassword(mEmailText.getText().toString(), mPasswordText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put(UserData.documentId, user.getUid());
                                userMap.put(UserData.nickname, mNickname.getText().toString());
                                userMap.put(UserData.email, mEmailText.getText().toString());
                                userMap.put(UserData.password, mPasswordText.getText().toString());
                                mStore.collection(UserData.user).document(user.getUid()).set(userMap, SetOptions.merge());
                                Toast.makeText(SignupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } else {
                            Toast.makeText(SignupActivity.this, "Sign up error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
