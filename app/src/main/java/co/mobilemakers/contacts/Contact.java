package co.mobilemakers.contacts;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Contact {

    public final static String ID        = "_id";
    public final static String FIRSTNAME = "FirstName";
    public final static String LASTNAME  = "LastName";
    public final static String NICKNAME  = "Nickname";
    public final static String IMAGE     = "Image";

    @DatabaseField(generatedId = true,
                    columnName = ID)      private int _id;
    @DatabaseField(columnName = FIRSTNAME)private String mFirstName;
    @DatabaseField(columnName = LASTNAME) private String mLastName;
    @DatabaseField(columnName = NICKNAME) private String mNickname;
    @DatabaseField(columnName = IMAGE,
                     dataType = DataType.BYTE_ARRAY)private byte[] mImage;


    public int get_id() {
        return _id;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public void setmNickname(String mNickname) {
        this.mNickname = mNickname;
    }

    public void setmImage(byte[] mImage) {
        this.mImage = mImage;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public String getmNickname() {
        return mNickname;
    }

    public byte[] getmImage() {
        return mImage;
    }
}
