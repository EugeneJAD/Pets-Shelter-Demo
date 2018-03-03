package eugene.petsshelter.data.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Wrapper for android {@link SharedPreferences}
 */

@Singleton
public class PrefsManager {

    protected SharedPreferences sharedPreferences;
    public static final String SP_NAME="user_data";

    @Inject
    public PrefsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void deleteSP() {
        this.sharedPreferences.edit().clear().apply();
    }

    protected void saveString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    protected void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    protected void saveInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    protected void saveLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    protected String retrieveString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    protected int retrieveInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    protected long retrieveLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    protected boolean retrieveBoolean(String key, Boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    protected float retrieveFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }


}