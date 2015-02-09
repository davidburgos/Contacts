package co.mobilemakers.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.List;


public class ContactListFragment extends ListFragment{

    protected final static int CREATE_REQUEST_CODE = 3141;
    protected final static int DELETE_REQUEST_CODE = 1980;
    protected final static int UPDATE_REQUEST_CODE = 2717;

    protected final static String LOG_TAG = ContactListFragment.class.getSimpleName();
    private DatabaseHelper mDBHelper;
    List<Contact> mEntries;
    ContactAdapter mAdapter;

    public ContactListFragment() {
    }

    private DatabaseHelper getDBHelper(){
        if(mDBHelper == null){
           mDBHelper = OpenHelperManager.getHelper(getActivity(),DatabaseHelper.class);
        }
        return mDBHelper;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareListView();
    }

    private void prepareListView() {
        mEntries = getDBHelper().retrieveAllContacts();
        mAdapter = new ContactAdapter(getActivity(), mEntries);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editContact = new Intent(getActivity(), EditContactActivity.class);
/*
                  TextView  FirstName = (TextView) view.findViewById(R.id.edit_text_firstname_edit);
                  TextView  LastName  = (EditText) view.findViewById(R.id.edit_text_lastname_edit);
                  TextView  NickName  = (EditText) view.findViewById(R.id.edit_text_nickname_edit);
                  ImageView Image     = (ImageView)view.findViewById(R.id.image_edit);
*/
                Contact contact = getContactById(view.getId());
                if(contact != null){
                    editContact.putExtra(Contact.ID,view.getId());
                    editContact.putExtra(Contact.FIRSTNAME,contact.getmFirstName());
                    editContact.putExtra(Contact.LASTNAME, contact.getmLastName());
                    editContact.putExtra(Contact.NICKNAME, contact.getmNickname());
                    editContact.putExtra(Contact.IMAGE ,   contact.getmImage());

                    /*if(Image.getmImage().getDrawable() != null){
                        Image.buildDrawingCache();
                        Bitmap bm=((BitmapDrawable)Image.getDrawable()).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    }    */
                }

                startActivityForResult(editContact, UPDATE_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);
//        return rootView;

        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    private void createNewContact(String pFirstName, String pLastName, String pNickName, byte[] pImage) {
        Contact contact = new Contact();
        contact.setmFirstName(pFirstName);
        contact.setmLastName(pLastName);
        contact.setmNickname(pNickName);
        contact.setmImage(pImage);
        mEntries.add(contact);
        saveContactInDatabase(contact);
        mAdapter.notifyDataSetChanged();
    }

    private void updateContact(int Id, String pFirstName, String pLastName, String pNickName, byte[] pImage) {

        try{
            Contact contact;
            Dao<Contact, Integer> dao = getDBHelper().getContactDao();
            contact = dao.queryForId(Id);
            if(contact != null){
                contact.setmFirstName(pFirstName);
                contact.setmLastName(pLastName);
                contact.setmNickname(pNickName);
                contact.setmImage(pImage);
                dao.update(contact);
                mAdapter.notifyDataSetChanged();
            }
        }catch (SQLException e){
            Log.e(LOG_TAG, "Can´t update Contact into database "+DatabaseHelper.DATABASE_NAME, e);
        }
    }

    private Contact getContactById(int Id){
        Contact contact = null;
        Dao<Contact, Integer> dao = null;
        try {
            dao = getDBHelper().getContactDao();
            contact = dao.queryForId(Id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contact;
    }

    @Override
    public void onDestroy() {
        if(mDBHelper != null){
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
        super.onDestroy();
    }

    private void saveContactInDatabase(Contact contact){
        try{
            Dao<Contact, Integer> dao = getDBHelper().getContactDao();
            dao.create(contact);
        }catch (SQLException e){
            Log.e(LOG_TAG, "Can´t insert Contact into database "+DatabaseHelper.DATABASE_NAME, e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case UPDATE_REQUEST_CODE:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        updateContact(data.getIntExtra(Contact.ID,-1),
                                      data.getStringExtra(Contact.FIRSTNAME),
                                      data.getStringExtra(Contact.LASTNAME),
                                      data.getStringExtra(Contact.NICKNAME),
                                      data.getByteArrayExtra(Contact.IMAGE));
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getActivity(), R.string.canceled_message, Toast.LENGTH_LONG).show();
                        break;
                    case DELETE_REQUEST_CODE:
                        switch (resultCode){
                            case Activity.RESULT_OK:
                                createNewContact(data.getStringExtra(Contact.FIRSTNAME),
                                        data.getStringExtra(Contact.LASTNAME),
                                        data.getStringExtra(Contact.NICKNAME),
                                        data.getByteArrayExtra(Contact.IMAGE));
                                break;
                            case Activity.RESULT_CANCELED:
                                Toast.makeText(getActivity(), R.string.canceled_message, Toast.LENGTH_LONG).show();
                                break;
                        }
                        break;
                }
                break;
            case CREATE_REQUEST_CODE:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        createNewContact(data.getStringExtra(Contact.FIRSTNAME),
                                data.getStringExtra(Contact.LASTNAME),
                                data.getStringExtra(Contact.NICKNAME),
                                data.getByteArrayExtra(Contact.IMAGE));
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getActivity(), R.string.canceled_message, Toast.LENGTH_LONG).show();
                        break;
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_contact_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Boolean handled = false;

        switch (item.getItemId()){
            case R.id.action_add_contact:
                Intent intent = new Intent(getActivity(), CreateContact.class);
                startActivityForResult(intent, CREATE_REQUEST_CODE);
                handled = true;
                break;
        }
        if(!handled){
            handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }
}
