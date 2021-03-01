package is.hi.hbv601g.matbjorg_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import is.hi.hbv601g.matbjorg_app.R;

public class LoginActivity extends AppCompatActivity {

    private Button mLogin;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLogin = (Button) findViewById(R.id.login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Login virkni
            }
        });
    }
}