package com.kplo.youtubefirebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private EditText mTitle, mContents;

    private String mNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mTitle = findViewById(R.id.post_title_edit);
        mContents = findViewById(R.id.post_contents_edit);

        findViewById(R.id.post_save_button).setOnClickListener(this);

        if (mAuth.getCurrentUser() != null){
            mStore.collection(UserData.user).document(mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                mNickname = (String) task.getResult().getData().get(UserData.nickname);
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        if (mAuth.getCurrentUser() != null){
            String postID = mStore.collection(UserData.post).document().getId();
            Map<String, Object> data = new HashMap<>();
            data.put(UserData.documentId, mAuth.getCurrentUser().getUid());
            data.put(UserData.nickname, mNickname);
            data.put(UserData.title , mTitle.getText().toString());
            data.put(UserData.contents , mContents.getText().toString());
            data.put(UserData.timestamp , FieldValue.serverTimestamp());
            mStore.collection(UserData.post).document(postID).set(data, SetOptions.merge());
            finish();
        }
    }
}
