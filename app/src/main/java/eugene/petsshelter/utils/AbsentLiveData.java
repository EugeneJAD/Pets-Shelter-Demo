package eugene.petsshelter.utils;

/**
 * Created by Eugene on 20.01.2018.
 */

import android.arch.lifecycle.LiveData;

/**
 * A LiveData class that has {@code null} value.
 */
public class AbsentLiveData extends LiveData {
    private AbsentLiveData() {
        postValue(null);
    }
    public static <T> LiveData<T> create() {
        //noinspection unchecked
        return new AbsentLiveData();
    }
}
