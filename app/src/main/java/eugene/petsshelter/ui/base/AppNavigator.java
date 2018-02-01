package eugene.petsshelter.ui.base;


import android.os.Bundle;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.ui.main.PetDetailsFragment;
import eugene.petsshelter.ui.main.PetsListFragment;
import eugene.petsshelter.ui.main.ShelterDetailsFragment;
import eugene.petsshelter.ui.map.MapsActivity;

public class AppNavigator {

    private final NavigationController navigation;

    @Inject
    public AppNavigator(NavigationController navigationController) {
        this.navigation = navigationController;
    }

    public void popUpBackStack(){navigation.popupBackStack();}

    private Bundle putStringToBundle(String key, String value){
        Bundle args = new Bundle();
        args.putString(key,value);
        return args;
    }

    //Main Activity Navigation

    public void navigateToDogs() {navigation.replaceFragment(R.id.fragment_container, new PetsListFragment(),
            putStringToBundle(PetsListFragment.KEY_LIST_TYPE,PetsListFragment.FRAGMENT_LIST_TYPE_DOGS));}

    public void navigateToCats() {navigation.replaceFragment(R.id.fragment_container, new PetsListFragment(),
            putStringToBundle(PetsListFragment.KEY_LIST_TYPE,PetsListFragment.FRAGMENT_LIST_TYPE_CATS));}

    public void navigateToCatDetails(String id) {navigateToPetDetails(id,PetDetailsFragment.PET_TYPE_CAT);}

    public void navigateToDogDetails(String id) {navigateToPetDetails(id,PetDetailsFragment.PET_TYPE_DOG);}

    private void navigateToPetDetails(String id, String type){
        Bundle args = new Bundle();
        args.putString(PetDetailsFragment.KEY_PET_ID,id);
        args.putString(PetDetailsFragment.KEY_PET_TYPE, type);
        navigation.replaceFragmentBackStack(R.id.fragment_container,
                new PetDetailsFragment(),PetDetailsFragment.class.getSimpleName(),args,null,
                R.anim.slide_in_up_anim, R.anim.fade_out_anim,R.anim.fade_in_anim,R.anim.slide_out_down_anim);}

    public void navigateToShelter() {navigation.replaceFragment(R.id.fragment_container, new ShelterDetailsFragment(),null);}

    public void navigateToMap(){navigation.startActivity(MapsActivity.class);}
}
