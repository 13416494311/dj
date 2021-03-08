import com.alibaba.fastjson.JSONObject;
import com.apicloud.sdk.api.Push;
import lombok.SneakyThrows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PushTest {
    @SneakyThrows
    public static void main(String[] args) {
        //JSONObject json = Push.pushMessage("活动督办", "请尽快处理!", 1, 0, "", "18");
        //System.out.print(json);



        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowdayTime = dateFormat.format(new Date());
        Date nowDate = dateFormat.parse(nowdayTime);

        long time = nowDate.getTime();
        long year = 94608000000L;
        long day =  864000000L;
        time = time - year + day ;
        Date d = new Date(time);
    }

}
