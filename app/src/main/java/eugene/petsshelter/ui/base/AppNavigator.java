package eugene.petsshelter.ui.base;


import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

import javax.inject.Inject;

import eugene.petsshelter.BuildConfig;
import eugene.petsshelter.R;
import eugene.petsshelter.ui.donation.CardDonationFragment;
import eugene.petsshelter.ui.donation.DonationActivity;
import eugene.petsshelter.ui.donation.PaymentMethodsFragment;
import eugene.petsshelter.ui.donation.SummaryDonationFragment;
import eugene.petsshelter.ui.main.MainActivity;
import eugene.petsshelter.ui.main.NewsListFragment;
import eugene.petsshelter.ui.main.PetDetailsFragment;
import eugene.petsshelter.ui.main.PetsListFragment;
import eugene.petsshelter.ui.main.ShelterDetailsFragment;
import eugene.petsshelter.ui.map.MapsActivity;

public class AppNavigator {

    public static final int RC_SIGN_IN = 123;
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

    public void navigateToMain() {
        navigation.startActivity(MainActivity.class);
        navigation.finishActivity();
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

    public void navigateToLogin(){

        navigation.startActivityForResult(
            AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                    .setTheme(R.style.AppTheme)
                    .setLogo(R.drawable.ic_pet_logo_2)
                    .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                    .build(),
            RC_SIGN_IN);
    }

    public void navigateToNews() {navigation.replaceFragment(R.id.fragment_container, new NewsListFragment(), null);}

    public void navigateToDonation(){navigation.startActivity(DonationActivity.class);}


    //Donation Activity Navigation

    public void navigateToPaymentMethods() {
        navigation.replaceFragment(R.id.donation_fragment_container, new PaymentMethodsFragment(),null);
    }

    public void navigateToCardDonation() {
        navigation.replaceFragmentBackStack(R.id.donation_fragment_container,new CardDonationFragment(),
                CardDonationFragment.class.getSimpleName(),null,null,R.anim.slide_from_right,
                R.anim.slide_to_left,R.anim.slide_from_left,R.anim.slide_to_right);
    }

    public void navigateToSummury() {
        navigation.replaceFragmentBackStack(R.id.donation_fragment_container,new SummaryDonationFragment(),
                SummaryDonationFragment.class.getSimpleName(),null,null,R.anim.slide_from_right,
                R.anim.slide_to_left,R.anim.slide_from_left,R.anim.slide_to_right);
    }

}
