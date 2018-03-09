package eugene.petsshelter.ui.donation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.res.Resources;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;

import java.text.DecimalFormat;
import java.util.Arrays;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Resource;
import eugene.petsshelter.utils.AppConstants;
import timber.log.Timber;

/**
 * Created by Eugene on 3/7/2018.
 */

public class GooglePayViewModel extends ViewModel{

    @Inject Resources resources;

    private MutableLiveData<Resource<Boolean>> isGooglePayReady = new MutableLiveData<>();

    private int amountInCents = 0;
    private String inputAmount="";

    @Inject
    public GooglePayViewModel() {}

    public int getAmountInCents() {return amountInCents;}
    public void setAmountInCents(int amountInCents) {this.amountInCents = amountInCents;}

    public String getInputAmount() {return inputAmount;}
    public void setInputAmount(String inputAmount) {this.inputAmount = inputAmount;}

    public LiveData<Resource<Boolean>> getIsGooglePayReady() {return isGooglePayReady;}

    public void isReadyToGooglePay(PaymentsClient paymentsClient) {

        IsReadyToPayRequest request = IsReadyToPayRequest.newBuilder()
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                .build();
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(task1 -> {
            try {
                boolean result = task1.getResult(ApiException.class);
                if(result) {
                    //show Google as payment option
                    isGooglePayReady.setValue(Resource.success(true));
                } else {
                    //hide Google as payment option
                    isGooglePayReady.setValue(Resource.success(false));
                }
            } catch (ApiException exception) {
                isGooglePayReady.setValue(Resource.error(resources.getString(R.string.google_pay_not_available),false));
            }
        });
    }

    public PaymentDataRequest createPaymentDataRequest() {

        if(amountInCents ==0) return null;

        PaymentDataRequest.Builder request =
            PaymentDataRequest.newBuilder()
                    .setTransactionInfo(
                            TransactionInfo.newBuilder()
                                    .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                                    .setTotalPrice(getFormattedAmount(amountInCents)) // "10.00"
                                    .setCurrencyCode("cad")
                                    .build())
                    .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                    .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                    .setCardRequirements(
                            CardRequirements.newBuilder()
                                    .addAllowedCardNetworks(Arrays.asList(
                                            WalletConstants.CARD_NETWORK_AMEX,
                                            WalletConstants.CARD_NETWORK_DISCOVER,
                                            WalletConstants.CARD_NETWORK_VISA,
                                            WalletConstants.CARD_NETWORK_MASTERCARD))
                                    .build());
        request.setPaymentMethodTokenizationParameters(createTokenizationParameters());
        return request.build();
    }

    private PaymentMethodTokenizationParameters createTokenizationParameters() {
        return PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                .addParameter("gateway", "stripe")
                .addParameter("stripe:publishableKey", AppConstants.STRIPE_PUBLISHABLE_TEST_KEY)
                .addParameter("stripe:version", "6.1.1")
                .build();
    }

    private String getFormattedAmount(int amountInCents) {
        double doubleAmount = amountInCents/100;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(doubleAmount);
    }
}
