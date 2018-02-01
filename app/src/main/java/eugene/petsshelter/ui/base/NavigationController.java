package eugene.petsshelter.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import javax.inject.Inject;

public class NavigationController implements Navigator{

    public static final String FRAGMENT_TAG = "content";

    private final FragmentActivity activity;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(FragmentActivity activity) {
        this.activity = activity;
        this.fragmentManager = activity.getSupportFragmentManager();
    }


    @Override
    public void finishActivity() {activity.finish();}

    @Override
    public void startActivity(@NonNull Class<? extends Activity> activityClass) {
        startActivityNavigation(activityClass, null, null);
    }

    @Override
    public void startActivity(@NonNull Class<? extends Activity> activityClass, Bundle args) {
        startActivityNavigation(activityClass, args, null);
    }

    @Override
    public void startActivityForResult(@NonNull Class<? extends Activity> activityClass, int requestCode) {
        startActivityNavigation(activityClass, null, requestCode);
    }

    @Override
    public void replaceFragment(int containerId, @NonNull Fragment fragment, Bundle args) {
        replaceFragmentNavigation(containerId,fragment,null,args,false,null,
                null,null,null,null);
    }

    @Override
    public void replaceFragment(int containerId, @NonNull Fragment fragment, Bundle args, String fragmentTag) {
        replaceFragmentNavigation(containerId,fragment,fragmentTag,args,false,null,
                null,null,null,null);
    }

    @Override
    public void replaceFragmentBackStack(int containerId, @NonNull Fragment fragment,@NonNull String fragmentTag, Bundle args, String backstackTag) {
        replaceFragmentNavigation(containerId,fragment,fragmentTag,args,true,fragment.getClass().getSimpleName(),
                null,null,null,null);
    }

    @Override
    public void replaceFragmentBackStack(int containerId, @NonNull Fragment fragment, @NonNull String fragmentTag, Bundle args, String backstackTag,
                                         int enterAnimation, int exitAnimation, int popEnterAnimation, int popExitAnimation) {
        replaceFragmentNavigation(containerId,fragment,fragmentTag,args,true,fragment.getClass().getSimpleName(),
                enterAnimation,exitAnimation,popEnterAnimation,popExitAnimation);
    }

    @Override
    public void showDialog(DialogFragment dialogFragment){
        dialogFragment.show(activity.getSupportFragmentManager(), dialogFragment.getTag());
    }

    protected void clearBackStack() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.popBackStack(NavigationController.FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    protected final void popupBackStack(){
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

    private void startActivityNavigation(@NonNull Class<? extends Activity> activityClass, Bundle args, Integer requestCode){
        Intent intent = new Intent(activity, activityClass);

        if(args!=null)
            intent.putExtras(args);

        if(requestCode!=null)
            activity.startActivityForResult(intent,requestCode);
        else
            activity.startActivity(intent);
    }


    private void replaceFragmentNavigation(@IdRes int containerId, Fragment fragment, String fragmentTag, Bundle args, boolean addToBackstack, String backstackTag,
                                                 @AnimRes Integer enterAnimation, @AnimRes Integer exitAnimation, @AnimRes Integer popEnterAnimation, @AnimRes Integer popExitAnimation) {

        if (fragmentTag != null) {
            if (isFragmentTheSame(getCurrentFragment(containerId), fragment))
                return;
        }

        if(args != null) { fragment.setArguments(args);}

        FragmentTransaction ft = fragmentManager.beginTransaction();
        if(enterAnimation!=null && exitAnimation!=null)
            ft.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation,popExitAnimation);
        ft.replace(containerId, fragment, fragmentTag);
        if(addToBackstack) {
            ft.addToBackStack(backstackTag).commit();
            fragmentManager.executePendingTransactions();
        } else {
            ft.commitAllowingStateLoss();
        }
    }

    private boolean isFragmentTheSame(Fragment current, Fragment newFragment) {
        return current != null && newFragment.getClass().equals(current.getClass());
    }

    private Fragment getCurrentFragment(@IdRes int containerId) {
        return activity.getSupportFragmentManager().findFragmentById(containerId);
    }

}
