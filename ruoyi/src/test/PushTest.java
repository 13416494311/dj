import com.alibaba.fastjson.JSONObject;
import com.apicloud.sdk.api.Push;

public class PushTest {
    public static void main(String[] args) {
        JSONObject json = Push.pushMessage("活动督办", "请尽快处理!", 1, 0, "", "18");
        System.out.print(json);

    }

}
