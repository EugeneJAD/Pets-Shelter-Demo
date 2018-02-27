package eugene.petsshelter.ui.main;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import eugene.petsshelter.data.repository.Repository;

/**
 * Created by Admin on 2/24/2018.
 */

public class NewsListViewModel extends ViewModel {

    @Inject Repository repository;

    @Inject
    public NewsListViewModel() {}

    public void doStarTransaction(String key) {
        repository.doStarTransaction(key);
    }
}
