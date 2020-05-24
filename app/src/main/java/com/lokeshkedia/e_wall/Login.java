package com.lokeshkedia.e_wall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        final AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.activity_login)
                .setGoogleButtonId(R.id.google)
                .build();
        AuthUI.getInstance().createSignInIntentBuilder()
                .setAuthMethodPickerLayout(customLayout).build();
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(Login.this, user.getDisplayName(), Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(getApplicationContext(),ComplaintFeed.class));
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAuthMethodPickerLayout(customLayout).setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Photographers").child(UID).child("Profile");
                databaseReference.child("Name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                databaseReference.child("Email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                databaseReference.child("Img").setValue(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()));


                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, PhotographersBlog.class));
                finish();
            } else {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        // String s = String.valueOf(plotNo.size());

    }

    @Override
    protected void onResume() {
        super.onResume();
        // attachDatabaseReadListener();
        //FirebaseApp.initializeApp(Login.this);
        // firebaseAuth.addAuthStateListener(authStateListener);

    }

    public void login(View view) {
        FirebaseApp.initializeApp(Login.this);
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
