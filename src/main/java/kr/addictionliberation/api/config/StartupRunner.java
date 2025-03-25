package kr.addictionliberation.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component // 필수!
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("서버 실행 중입니다!");
    }
}