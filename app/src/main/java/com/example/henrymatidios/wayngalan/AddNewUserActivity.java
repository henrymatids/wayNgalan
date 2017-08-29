package com.example.henrymatidios.wayngalan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.Map;

public class AddNewUserActivity extends BaseActivity implements View.OnClickListener {

    //UI variables
    private EditText mUsername;
    private EditText mPassword;
    private Spinner mType;
    private Button mAddButton;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    List<String> accountTypes = new ArrayList<>();
    String accType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        //UI instances
        mAddButton = (Button) findViewById(R.id.add_new_user_button);
        mUsername = (EditText) findViewById(R.id.add_new_user_editText_username);
        mPassword = (EditText) findViewById(R.id.add_new_user_editText_password);


        //Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();


        accountTypes.add("Admin");
        accountTypes.add("User");

        //Spinner
        mType =  (Spinner) findViewById(R.id.account_type);
        CustomAdapter spinnerAdapter = new CustomAdapter(AddNewUserActivity.this, accountTypes);
        mType.setAdapter(spinnerAdapter);
        mType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                accType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

            if(!(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))) {
                createAccount(email, password, accType);
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
                            Map<String, String> mapType = new HashMap<String, String>();
                            mapType.put("type", type);

                            dbRef.child("Accounts").child(task.getResult().getUser().getUid()).setValue(mapType);

                            Toast.makeText(AddNewUserActivity.this,"Successfully Added", Toast.LENGTH_LONG).show();

                            mUsername.setText("");
                            mPassword.setText("");
                            mType.setSelection(0);

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
//                                Toast.makeText(AddNewUserActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(AddNewUserActivity.this, "Failed to add.",Toast.LENGTH_LONG).show();
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
