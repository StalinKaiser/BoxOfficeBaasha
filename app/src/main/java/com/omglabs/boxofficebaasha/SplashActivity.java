package com.omglabs.boxofficebaasha;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserTokenStorageFactory;

import java.util.Currency;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Backendless.initApp(this,"2B6A10F5-1587-80EC-FF72-155910C00400","785BD6D9-E716-9F7C-FFD3-165CD4996F00","v1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String userToken = UserTokenStorageFactory.instance().getStorage().get();
        /**Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean aBoolean) {
                if (aBoolean == true) {
                    BackendlessUser currentUser = Backendless.UserService.CurrentUser();
                    if (currentUser == null) {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("isLoggedIn", "true");
                        i.putExtras(mBundle);
                        startActivity(i);
                        finish();
                    }

                }
                else
                {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });**/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                BackendlessUser currentUser = Backendless.UserService.CurrentUser();
                if (currentUser == null) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("isLoggedIn", "true");
                    i.putExtras(mBundle);
                    startActivity(i);
                    finish();
                }
            }
        }, 1500);

    }
}
