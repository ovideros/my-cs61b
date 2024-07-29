package gitlet;

import java.sql.Timestamp;

public class EasyTest {
    public void unixTimeStampTest() {
        Commit cm1 = new Commit("try", null);
        System.out.println(cm1.getTimeStamp());
        Commit cm2 = new Commit("try", cm1);
        System.out.println(cm2.getTimeStamp());
    }
}
