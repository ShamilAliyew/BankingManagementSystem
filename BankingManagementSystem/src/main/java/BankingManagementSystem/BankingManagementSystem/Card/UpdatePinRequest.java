package BankingManagementSystem.BankingManagementSystem.Card;

public class UpdatePinRequest {
    private String oldPin;
    private String newPin;

    public String getOldPin() {
        return oldPin;
    }

    public void setOldPin(String oldPin) {
        this.oldPin = oldPin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }
}
