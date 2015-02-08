package co.mobilemakers.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 08/02/2015 for project Contacts
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private final static String LOG_TAG = DatabaseHelper.class.getSimpleName();
    public  final static String DATABASE_NAME ="contacts.db";
    private final static int DATABASE_VERSION = 1;
    private Dao<Contact,Integer> mContactDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Dao<Contact,Integer> getContactDao() throws SQLException{
        if(mContactDao == null){
           mContactDao = getDao(Contact.class);
        }
        return mContactDao;
    }

    public List<Contact> retrieveAllContacts() {
        List<Contact> contacts = null;

        try {
            Dao<Contact,Integer> contactDao = getContactDao();
            contacts = contactDao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Contact.class);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Failed to create database "+DATABASE_NAME,e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    @Override
    public void close() {
        super.close();
        mContactDao = null;
    }
}
