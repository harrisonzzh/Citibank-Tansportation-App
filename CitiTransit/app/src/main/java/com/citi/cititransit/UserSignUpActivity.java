package com.citi.cititransit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserSignUpActivity extends AppCompatActivity {

    private Button buttonSignUp;
    private EditText emailAddress;
    private EditText password;
    private EditText confirmPassword;
    private TextView signInButton;
    private AlertDialog regiAlert;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        //Initial values
        this.buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        this.emailAddress = (EditText) findViewById(R.id.editTextEmail);
        this.password = (EditText) findViewById(R.id.editTextPassword);
        this.confirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        this.signInButton = (TextView) findViewById(R.id.oldUserSignIn);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == buttonSignUp) {
                    registerUser(emailAddress, password, confirmPassword);
                }
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == signInButton) {
                    Intent intent = new Intent(UserSignUpActivity.this, UserSignInActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Register user and add the info to the firebase auth
     *
     * @param email
     * @param password
     * @param confirmPassword
     */
    private void registerUser(final EditText email, EditText password, EditText confirmPassword) {
        final String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String conFirmPasswordStr = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(emailStr)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(passwordStr)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(conFirmPasswordStr)) {
            Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.equals(passwordStr, conFirmPasswordStr)) {

            Toast.makeText(this, "Passwords not match", Toast.LENGTH_SHORT).show();

            return;
        }

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            //Create a own view for the alert message
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(UserSignUpActivity.this);
                            alertBuilder.setMessage("Register Successful")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startActivity(new Intent(UserSignUpActivity.this, MapsActivity.class));
                                        }
                                    });
                            regiAlert = alertBuilder.create();
                            regiAlert.show();
                        } else {
                            //TODO: add more case and handle more creafuuly
                            Toast.makeText(UserSignUpActivity.this, "Something went wrong, try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }
}

