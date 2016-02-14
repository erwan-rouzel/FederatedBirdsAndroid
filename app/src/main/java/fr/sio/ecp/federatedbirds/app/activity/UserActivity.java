package fr.sio.ecp.federatedbirds.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.FileNotFoundException;
import java.io.IOException;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.fragment.UserFragment;
import fr.sio.ecp.federatedbirds.app.task.UploadTask;

public class UserActivity extends AppCompatActivity {
    UserFragment fragment;
    private static final String ARG_USER_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        if (savedInstanceState == null) {
            fragment = new UserFragment();

            Bundle bundle = getIntent().getExtras();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==1 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                UploadTask uploadTask = new UploadTask(this, getApplicationContext(), UserActivity.class, selectedImage, fragment);
                uploadTask.execute();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
