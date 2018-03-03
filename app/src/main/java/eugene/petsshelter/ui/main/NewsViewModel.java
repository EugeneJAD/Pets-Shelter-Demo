package eugene.petsshelter.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import eugene.petsshelter.data.models.NewsItem;
import eugene.petsshelter.data.repository.Repository;

/**
 * Created by Admin on 2/24/2018.
 */

public class NewsViewModel extends ViewModel {

    @Inject Repository repository;

    @Inject
    public NewsViewModel() {}

    public void doStarTransaction(String key) {
        repository.doStarTransaction(key);
    }

    public LiveData<List<NewsItem>> getNews() {return repository.getNews();}

    public void startListeningNews() {repository.startListeningNews();}
    public void stopListeningNews() {repository.stopListeningNews();}

    public void startListeningNewsItem(String key) {repository.startListeningNewsItem(key);}
    public void stopListeningNewsItem(String key) {repository.stopListeningNewsItem(key);}

    public LiveData<NewsItem> getSelectedNews() {return repository.getSelectedNews();}



}
