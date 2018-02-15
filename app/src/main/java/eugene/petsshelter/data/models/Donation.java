package eugene.petsshelter.data.models;

/**
 * Created by Eugene on 14.02.2018.
 */

public class Donation {

    private int amount;

    private String donator;

    private String donatory;

    private String cardLastNumbers;

    private String stripeTokenId;

    private String description;

    private boolean isConfirmed;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDonator() {
        return donator;
    }

    public void setDonator(String donator) {
        this.donator = donator;
    }

    public String getDonatory() {
        return donatory;
    }

    public void setDonatory(String donatory) {
        this.donatory = donatory;
    }

    public String getCardLastNumbers() {
        return cardLastNumbers;
    }

    public void setCardLastNumbers(String cardLastNumbers) {
        this.cardLastNumbers = "**** **** **** "+cardLastNumbers;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getStripeTokenId() {
        return stripeTokenId;
    }

    public void setStripeTokenId(String stripeTokenId) {
        this.stripeTokenId = stripeTokenId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

