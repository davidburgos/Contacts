package co.mobilemakers.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;

/**
 * Created by David on 06/02/2015 for project Contacts
 */
public class ContactAdapter extends BaseAdapter{

    private final Context mContext;
    List<Contact> mContacts = Collections.emptyList();

    public ContactAdapter(Context context, List<Contact> Contacts) {
        this.mContext = context;
        this.mContacts = Contacts;
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = reuseOrGenerateRowView(convertView, parent);
        displayContentInView(position, rowView);
        return rowView;
    }

    private View reuseOrGenerateRowView(View convertView, ViewGroup parent) {
        View rowView;
        if (convertView != null) {
            rowView = convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.contact_item_entry, parent, false);
        }
        return rowView;
    }

    private void displayContentInView(final int position, View rowView) {
        if (rowView != null) {

            rowView.setId(mContacts.get(position).get_id());

            TextView textViewFirstName = (TextView)rowView.findViewById(R.id.text_view_firstname);
            textViewFirstName.setText(mContacts.get(position).getmFirstName());

            TextView textViewLastName = (TextView)rowView.findViewById(R.id.text_view_lastname);
            textViewLastName.setText(mContacts.get(position).getmLastName());

            TextView textViewNickName = (TextView)rowView.findViewById(R.id.text_view_nickname);
            textViewNickName.setText(mContacts.get(position).getmNickname());

            byte[] byteArray = mContacts.get(position).getmImage();

            if(byteArray != null){
                ImageView imageViewPhoto = (ImageView)rowView.findViewById(R.id.imageView_photo);
                Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imageViewPhoto.setImageBitmap(bm);
            }

        }
    }
}
