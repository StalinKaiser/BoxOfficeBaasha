package com.omglabs.boxofficebaasha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import org.w3c.dom.Text;

/**
 * Created by anusha on 30/05/16.
 */
public class LoginActivity extends AppCompatActivity {
    private Button login;
    private Button register;
    private TextView skipLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.loadingPanelLogin).setVisibility(View.GONE);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loadingPanelLogin).setVisibility(View.VISIBLE);
                final String username = ((EditText) findViewById(R.id.UserNameEdit)).getText().toString().trim();
                final String password =((EditText) findViewById(R.id.PasswordEdit)).getText().toString().trim();
                if(username.isEmpty())
                {
                    findViewById(R.id.loadingPanelLogin).setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,"Enter EmailID",Toast.LENGTH_LONG).show();
                }
                else if(password.isEmpty())
                {
                    findViewById(R.id.loadingPanelLogin).setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,"Enter password",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Backendless.UserService.login(username, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser backendlessUser) {
                            findViewById(R.id.loadingPanelLogin).setVisibility(View.GONE);
                            Intent i = new Intent(LoginActivity.this,MainActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putString("isLoggedIn", "true");
                            i.putExtras(mBundle);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            findViewById(R.id.loadingPanelLogin).setVisibility(View.GONE);
                            if (backendlessFault.getCode().compareTo("3033")==0) {
                                Toast.makeText(LoginActivity.this,"Invalid Credentials",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(LoginActivity.this,"Unable to login. Please check the connection or try again later",Toast.LENGTH_LONG).show();
                            }

                        }
                    },true);
                }

            }
        });

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        skipLogin =(TextView) findViewById(R.id.skiplogin);
        skipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("isLoggedIn", "false");
                i.putExtras(mBundle);
                startActivity(i);
                finish();
            }
        });
    }
}
