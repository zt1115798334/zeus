package com.zt.zeus.transfer.service.callable;

import com.zt.zeus.transfer.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@AllArgsConstructor
public class WriteInLocal implements Callable<Long> {
    private final LocalDate localDate;

    private final String articleJson;

    private final String filePath;

    private final String fileName;

    private final AtomicInteger atomicInteger;

    private final  Random random = new Random();

    @Override
    public Long call() {
        long start = System.currentTimeMillis();
        String formatDate = DateUtils.formatDate(localDate);
        String fileNum = String.valueOf(atomicInteger.getAndIncrement() % 10);
        String realPath = filePath + File.separator + formatDate + File.separator + fileNum + File.separator;
        File file = new File(realPath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (mkdirs) {
                log.info("路径：{},不存在，现在创建完成", realPath);
            }
        }
        if (true) {
            Path path = Paths.get(realPath + File.separator + fileName + ".txt");
            try (BufferedWriter writer =
                         Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                writer.write(articleJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        return end - start;
    }

    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextBoolean());
        }
    }
}
