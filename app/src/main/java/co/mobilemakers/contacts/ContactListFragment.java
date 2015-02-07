package co.mobilemakers.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ContactListFragment extends ListFragment {

    protected final static int REQUEST_CODE = 314;
    List<Contact> mEntries;
    ContactAdapter mAdapter;

    public ContactListFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareListView();
    }

    private void prepareListView() {
        mEntries = new ArrayList<>();
        mAdapter = new ContactAdapter(getActivity(), mEntries);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"Edit task in next version!",Toast.LENGTH_LONG).show();
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
        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);

        return rootView;
    }

    private void createNewTask(String pFirstName, String pLastName, String pNickName, byte[] pImage) {
        Contact contact = new Contact();
        contact.setmFirstName(pFirstName);
        contact.setmLastName(pLastName);
        contact.setmNickname(pNickName);
        contact.setmImage(pImage);
        mEntries.add(contact);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case Activity.RESULT_OK:
                createNewTask(data.getStringExtra(CreateContact.FIRSTNAME),
                              data.getStringExtra(CreateContact.LASTNAME) ,
                              data.getStringExtra(CreateContact.NICKNAME) ,
                              data.getByteArrayExtra(CreateContact.IMAGE));
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(getActivity(), R.string.canceled_message, Toast.LENGTH_LONG).show();
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
                startActivityForResult(intent, REQUEST_CODE);
                handled = true;
                break;
        }
        if(!handled){
            handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }
}
