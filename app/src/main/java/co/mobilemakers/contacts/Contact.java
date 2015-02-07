package co.mobilemakers.contacts;

public class Contact {

    private String mFirstName;
    private String mLastName;
    private String mNickname;
    byte[] mImage;

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
