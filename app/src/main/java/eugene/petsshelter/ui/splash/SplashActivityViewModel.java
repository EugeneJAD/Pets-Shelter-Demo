package eugene.petsshelter.ui.splash;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import eugene.petsshelter.data.models.NewsItem;
import eugene.petsshelter.data.models.Resource;
import eugene.petsshelter.data.repository.Repository;

/**
 * Created by Eugene on 3/3/2018.
 */

public class SplashActivityViewModel extends ViewModel {

    @Inject Repository repository;

    @Inject
    public SplashActivityViewModel() {}

    public LiveData<Resource<List<NewsItem>>> getNews() {return repository.getNews();}

}
