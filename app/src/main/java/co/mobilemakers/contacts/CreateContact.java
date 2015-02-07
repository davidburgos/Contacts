package co.mobilemakers.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;


public class CreateContact extends ActionBarActivity {

    public final static String FIRSTNAME = "FIRSTNAME";
    public final static String LASTNAME  = "LASTNAME";
    public final static String NICKNAME  = "NICKNAME";
    public final static String IMAGE  = "IMAGE";
    public final static int CAMERA_REQUEST  = 31415;

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mNickName;
    private TextView mTextView;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        prepareButtons();
    }

    private void prepareButtons() {
        mFirstName = (EditText) findViewById(R.id.edit_text_firstname);
        mLastName  = (EditText) findViewById(R.id.edit_text_lastname);
        mNickName  = (EditText) findViewById(R.id.edit_text_nickname);
        mTextView  = (TextView) findViewById(R.id.textViewImageMessage);
        mImage     = (ImageView)findViewById(R.id.imageButton);
        mImage.setDrawingCacheEnabled(true);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(CameraIntent, CAMERA_REQUEST);
            }
        });

        Button mButton = (Button)findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent();

                mainActivity.putExtra(FIRSTNAME,mFirstName.getText().toString());
                mainActivity.putExtra(LASTNAME ,mLastName.getText().toString());
                mainActivity.putExtra(NICKNAME ,mNickName.getText().toString());

                 if(mImage.getDrawable() != null){
                    mImage.buildDrawingCache();
                    Bitmap bm=((BitmapDrawable)mImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    mainActivity.putExtra(IMAGE ,stream.toByteArray());
                }

                setResult(Activity.RESULT_OK, mainActivity);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST){

            switch (resultCode){
                case RESULT_OK:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    mImage.setImageBitmap(photo);
                    mTextView.setVisibility(View.GONE);
                    break;
                case RESULT_CANCELED:
                    mTextView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
