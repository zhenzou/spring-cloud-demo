package ltd.yazz.provider.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class HelloController {

    @GetMapping("/say")
    public ResponseEntity<Map<String, String>> sayHi(@RequestParam String name) {
        Map<String, String> map = new HashMap<>();
        map.put("Hi", name);
        log.info("name:{}", name);
        return ResponseEntity.ok(map);
    }
}
