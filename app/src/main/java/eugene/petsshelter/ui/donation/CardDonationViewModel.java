package eugene.petsshelter.ui.donation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import javax.inject.Inject;

import eugene.petsshelter.utils.AppConstants;
import timber.log.Timber;

/**
 * Created by Eugene on 13.02.2018.
 */

public class CardDonationViewModel extends ViewModel{

    private int amount;
    private String inputAmount="";
    private Card selectedCard;

    MutableLiveData<String> stripeToken = new MutableLiveData<>();

    @Inject
    public CardDonationViewModel() {}

    public int getAmount() {return amount;}
    public void setAmount(int amount) {this.amount = amount;}

    public String getInputAmount() {return inputAmount;}
    public void setInputAmount(String inputAmount) {this.inputAmount = inputAmount;}

    public void createStripeToken(Stripe stripe){

        if(selectedCard==null) return;

        stripe.createToken(selectedCard, AppConstants.STRIPE_PUBLISHABLE_TEST_KEY, new TokenCallback() {
            @Override
            public void onError(Exception error) {stripeToken.setValue(null);}
            @Override
            public void onSuccess(Token token) {stripeToken.setValue(token.getId());}
        });
    }

    public LiveData<String> getStripeToken() {return stripeToken;}
    public void resetStripeToken() {stripeToken.setValue(null);}

    public Card getSelectedCard() {return selectedCard;}
    public void setSelectedCard(Card selectedCard) {this.selectedCard = selectedCard;}
}
