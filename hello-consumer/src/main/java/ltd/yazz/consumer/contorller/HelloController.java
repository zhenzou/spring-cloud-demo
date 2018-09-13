package ltd.yazz.consumer.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class HelloController {

    @GetMapping("/hi")
    public ResponseEntity<Map<String, String>> sayHi(@RequestParam String name) {
        Map<String, String> map = new HashMap<>();
        map.put("Hi", name);
        log.info("name:{}", name);
        return ResponseEntity.ok(map);
    }

    /**
     * 测试post超时不重试
     * @param name
     * @return
     */
    @PostMapping("/slow/hi")
    public ResponseEntity<Map<String, String>> slowSayHi(@RequestParam String name) {
        Map<String, String> map = new HashMap<>();
        map.put("Hi", name);
        log.info("name:{}", name);
        try {
            Thread.sleep(4_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(map);
    }

    /**
     *
     * 测试只有在connection refused 的时候才重试
     * @param name
     * @return
     */
    @PostMapping("/retry/hi")
    public ResponseEntity<Map<String, String>> retrySayHi(@RequestParam String name) {
        Map<String, String> map = new HashMap<>();
        map.put("Hi", name);
        log.info("name:{}", name);
        return ResponseEntity.ok(map);
    }

}
