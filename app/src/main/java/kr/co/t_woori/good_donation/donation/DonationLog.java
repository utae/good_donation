package kr.co.t_woori.good_donation.donation;

import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-08-17.
 */

public class DonationLog {

    private String time;
    private String charityName;
    private String amount;

    public DonationLog(String time, String charityName, String amount) {
        this.time = convertTimestampToDataFormat(time);
        this.charityName = charityName;
        this.amount = Utilities.convertStringToNumberFormat(amount) + "원";
    }

    public String getTime() {
        return time;
    }

    public String getCharityName() {
        return charityName;
    }

    public String getAmount() {
        return amount;
    }

    private String convertTimestampToDataFormat(String timestamp){
        return Utilities.convertStringToDateFormat(timestamp, "yyyyMMddHHmmss", "yyyy년 MM월 dd일 HH시 mm분");
    }
}
