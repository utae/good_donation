package kr.co.t_woori.good_donation.rank;

/**
 * Created by rladn on 2017-09-01.
 */

public class User {

    private String rank;
    private String nickName;
    private String accumulation;

    public User(String rank, String nickName, String accumulation) {
        this.rank = rank;
        this.nickName = nickName;
        this.accumulation = accumulation;
    }

    public String getRank() {
        return rank;
    }

    public String getNickName() {
        return nickName;
    }

    public String getAccumulation() {
        return accumulation;
    }
}
