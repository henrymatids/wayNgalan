package com.example.henrymatidios.wayngalan;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.henrymatidios.wayngalan.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddNewUserActivity extends BaseActivity implements View.OnClickListener {

    //UI variables
    private EditText mUsername;
    private EditText mPassword;
    private Spinner mType;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        //UI instances
        Button mAddButton = (Button) findViewById(R.id.add_new_user_button);
        mUsername = (EditText) findViewById(R.id.add_new_user_editText_username);
        mPassword = (EditText) findViewById(R.id.add_new_user_editText_password);
        mType = (Spinner) findViewById(R.id.account_type);

        //Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("Users");

        //Spinner Values
        List<String> accountTypes = new ArrayList<String>();
        accountTypes.add("Admin");
        accountTypes.add("User");

        //Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String> (this,R.layout.support_simple_spinner_dropdown_item);
//        spinnerAdapter.add("Account Type");
        spinnerAdapter.addAll(accountTypes);
//        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mType.setAdapter(spinnerAdapter);
//        mType.setSelection(spinnerAdapter.getCount());


        mUsername.setError(null);
        mPassword.setError(null);

        mAddButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();

        if(i == R.id.add_new_user_button) {
            String email = mUsername.getText().toString();
            String password = mPassword.getText().toString();
            String type = mType.getSelectedItem().toString();

            if(!(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))) {
                createAccount(email, password, type);
            } else {
                if (TextUtils.isEmpty(email)) {
                    mUsername.setError(getString(R.string.error_field_required));
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError(getString(R.string.error_field_required));
                }
            }
        }
    }

    /**
     * Creates new account and stores to the database.
     * @param email user inputted email
     * @param password user inputted password
     */
    private void createAccount(final String email, final String password, final String type) {

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI
//                            Log.d("createAccount Method", "createUserWithEmail:success");
//                            Toast.makeText(AddNewUserActivity.this,"task.isSuccessful",Toast.LENGTH_LONG).show();
                            User user = new User(email, type);
                            dbRef.push().setValue(user);
                        } else {
                            // If sign in fails.
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                //weak password
                                mPassword.setError(getString(R.string.error_weak_password));
                                mPassword.requestFocus();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                //invalid email
                                mUsername.setError(getString(R.string.error_invalid_email));
                                mUsername.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                //user already exist
                                mUsername.setError(getString(R.string.error_user_exists));
                                mUsername.requestFocus();
                            } catch(Exception e) {
                                Log.e("createAccount Error:", e.getMessage());
                                Toast.makeText(AddNewUserActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(AddNewUserActivity.this,"task failed",Toast.LENGTH_LONG).show();
                        }
                        hideProgressDialog();
                    }
                });

    }

    /**
     * Updates the UI
     * @param user Object of the current user
     */
    public void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if(user != null) {

        } else {

        }
    }
}
