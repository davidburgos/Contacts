package co.mobilemakers.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


public class EditContactActivity extends ActionBarActivity {

    EditText mFirstName, mLastName, mNickName;
    TextView mTextImagen;
    ImageView mImage;
    Button mButtonDelete, mButtonUpdate;
    int mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        WireUpViews();
        populateData();
        setListeners();
    }

    private void setListeners() {
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent();

                mainActivity.putExtra(Contact.ID, mID);
                mainActivity.putExtra(Contact.FIRSTNAME,mFirstName.getText().toString());
                mainActivity.putExtra(Contact.LASTNAME ,mLastName.getText().toString());
                mainActivity.putExtra(Contact.NICKNAME ,mNickName.getText().toString());

                if(mImage.getDrawable() != null){
                    mImage.buildDrawingCache();
                    Bitmap bm=((BitmapDrawable)mImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    mainActivity.putExtra(Contact.IMAGE ,stream.toByteArray());
                }

                setResult(Activity.RESULT_OK, mainActivity);
                finish();
            }
        });


        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent();

                mainActivity.putExtra(Contact.ID, mID);

                setResult(ContactListFragment.DELETE_REQUEST_CODE, mainActivity);
                finish();
            }
        });

        mImage.setDrawingCacheEnabled(true);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(CameraIntent, CreateContact.CAMERA_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CreateContact.CAMERA_REQUEST){

            switch (resultCode){
                case RESULT_OK:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    mImage.setImageBitmap(photo);
                    mTextImagen.setVisibility(View.GONE);
                    break;
                case RESULT_CANCELED:
                    if(mImage.getDrawable() != null){
                        mTextImagen.setVisibility(View.GONE);
                    }else{
                        mTextImagen.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    private void populateData() {
        mID = getIntent().getIntExtra(Contact.ID, -1);
        mFirstName.setText(getIntent().getStringExtra(Contact.FIRSTNAME));
         mLastName.setText(getIntent().getStringExtra(Contact.LASTNAME));
         mNickName.setText(getIntent().getStringExtra(Contact.NICKNAME));

        byte[] byteArray = getIntent().getByteArrayExtra(Contact.IMAGE);

        if(byteArray != null){
            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            mImage.setImageBitmap(bm);
            mTextImagen.setVisibility(View.GONE);
        }else
        {
            mTextImagen.setVisibility(View.VISIBLE);
        }
    }

    private void WireUpViews() {
        mFirstName = (EditText) findViewById(R.id.edit_text_firstname_edit);
        mLastName = (EditText) findViewById(R.id.edit_text_lastname_edit);
        mNickName = (EditText) findViewById(R.id.edit_text_nickname_edit);
        mTextImagen = (TextView)findViewById(R.id.textViewImageMessage_edit);
        mImage    = (ImageView) findViewById(R.id.image_edit);
        mButtonDelete = (Button)findViewById(R.id.button_delete);
        mButtonUpdate = (Button)findViewById(R.id.button_update);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_contact, menu);
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
