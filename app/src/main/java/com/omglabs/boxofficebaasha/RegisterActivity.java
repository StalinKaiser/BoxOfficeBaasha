package com.omglabs.boxofficebaasha;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anusha on 22/11/16.
 */

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.loadingPanelRegister).setVisibility(View.GONE);
        register = (Button) findViewById(R.id.reg_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loadingPanelRegister).setVisibility(View.VISIBLE);
                final String username = ((EditText) findViewById(R.id.reg_UserNameEdit)).getText().toString().trim();
                final String emailid =((EditText) findViewById(R.id.reg_EmailEdit)).getText().toString().trim();
                final String password =((EditText) findViewById(R.id.reg_PasswordEdit)).getText().toString().trim();
                final String cnfPasssword =((EditText) findViewById(R.id.reg_ConfirmPasswordEdit)).getText().toString().trim();
                final String mobileno =((EditText) findViewById(R.id.reg_MobileEdit)).getText().toString().trim();

                if(username.isEmpty())
                {
                    findViewById(R.id.loadingPanelRegister).setVisibility(View.GONE);

                    Toast.makeText(RegisterActivity.this,"Enter Username",Toast.LENGTH_LONG).show();
                }
                else if(emailid.isEmpty())
                {
                    findViewById(R.id.loadingPanelRegister).setVisibility(View.GONE);

                    Toast.makeText(RegisterActivity.this,"Enter EmailID",Toast.LENGTH_LONG).show();
                }
                else if(password.isEmpty())
                {
                    findViewById(R.id.loadingPanelRegister).setVisibility(View.GONE);

                    Toast.makeText(RegisterActivity.this,"Enter Password",Toast.LENGTH_LONG).show();
                }
                else if(mobileno.isEmpty())
                {
                    findViewById(R.id.loadingPanelRegister).setVisibility(View.GONE);

                    Toast.makeText(RegisterActivity.this,"Enter MobileNo",Toast.LENGTH_LONG).show();
                }
                else if(password.compareTo(cnfPasssword)!=0)
                {
                    findViewById(R.id.loadingPanelRegister).setVisibility(View.GONE);

                    Toast.makeText(RegisterActivity.this,"Password does not match",Toast.LENGTH_LONG).show();
                }
                else
                {
                    HashMap sellerDetail = new HashMap();
                    sellerDetail.put( "userid", emailid );
                    sellerDetail.put( "mobileno", mobileno );
                    sellerDetail.put( "sellername", username );


                    // save object asynchronously
                    Backendless.Persistence.of( "sellerdetails" ).save( sellerDetail, new AsyncCallback<Map>() {
                        public void handleResponse( Map response )
                        {
                            BackendlessUser user = new BackendlessUser();
                            user.setProperty( "email", emailid );
                            user.setProperty( "location", "Chennai");
                            user.setProperty( "mobile", mobileno );
                            user.setProperty( "username", username );
                            user.setPassword( password );

                            Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
                            {
                                public void handleResponse( BackendlessUser registeredUser )
                                {
                                    //findViewById(R.id.loadingPanelRegister).setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this,"Account Created Successfully",Toast.LENGTH_LONG).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }, 2000);

                                }

                                public void handleFault( BackendlessFault fault ) {
                                    findViewById(R.id.loadingPanelRegister).setVisibility(View.GONE);
                                    if (fault.getCode().compareTo("3033")==0) {
                                        Toast.makeText(RegisterActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Unable to create account. Please check the connection or try again later", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } );
                        }

                        public void handleFault( BackendlessFault fault )
                        {
                            findViewById(R.id.loadingPanelRegister).setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Unable to create account. Please try again later", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });




                }
            }
        });
    }
}
