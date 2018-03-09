package eugene.petsshelter.ui.base;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import eugene.petsshelter.data.models.LoadingState;

/**
 * Created by Eugene on 10.02.2018.
 */

public abstract class BaseLoadingViewModel extends ViewModel{

    protected MutableLiveData<LoadingState> loadingState = new MutableLiveData<>();
    private View button;

    public BaseLoadingViewModel() {loadingState = new MutableLiveData<>();}

    public LiveData<LoadingState> getLoadingState(){return loadingState;}

    public void startLoading(){loadingState.setValue(new LoadingState(true,null, null));}

    public void startLoading(String message){loadingState.setValue(new LoadingState(true,message,null));}

    public void startLoading(View buttonToDisable){
        loadingState.setValue(new LoadingState(true,null,null));
        this.button=buttonToDisable;
        button.setClickable(false);
    }

    public void startLoading(String message, View buttonToDisable){
        loadingState.setValue(new LoadingState(true,message,null));
        this.button=buttonToDisable;
        button.setClickable(false);
    }

    public void onFailLoading(String errorMessage) {
        loadingState.setValue(new LoadingState(false,null,errorMessage));
        setClickable();
    }

    public void onCompleteLoading() {
        loadingState.setValue(new LoadingState(false,null,null));
        setClickable();
    }

    public boolean isLoading(){

        if(loadingState.getValue()==null){
            return false;
        } else {
            return loadingState.getValue().isRunning();
        }
    }

    private void setClickable(){
        if(this.button != null){
            this.button.setClickable(true);
            this.button = null;
        }
    }

}
