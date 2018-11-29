package ltd.yazz.demo.controller;

import lombok.extern.slf4j.Slf4j;
import ltd.yazz.demo.service.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloWorld {

    @Autowired
    private EchoService echoService;

    @PostMapping("/post")
    public ResponseEntity<String> post(@RequestParam String id) {
        log.info("id:{}", id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("hi")
    public ResponseEntity<String> hi(@RequestParam("name") String name) {
        String echo = echoService.sayHi(name);
        log.info("echo {}", echo);
        return ResponseEntity.ok(echo);
    }
}
