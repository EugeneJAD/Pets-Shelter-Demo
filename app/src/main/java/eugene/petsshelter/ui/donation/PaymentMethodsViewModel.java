package eugene.petsshelter.ui.donation;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.res.Resources;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.WalletConstants;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Resource;

/**
 * Created by Eugene on 13.02.2018.
 */

public class PaymentMethodsViewModel extends ViewModel {

    @Inject
    public PaymentMethodsViewModel() {}

}
