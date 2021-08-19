package com.zt.zeus.transfer.controller;

import com.zt.zeus.transfer.base.controller.BaseResultMessage;
import com.zt.zeus.transfer.base.controller.ResultMessage;
import com.zt.zeus.transfer.service.PushService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/20 9:48
 * description:
 */

@AllArgsConstructor
@RestController
@RequestMapping("api")
public class PushController extends BaseResultMessage {

    private final PushService pushService;

    @GetMapping("pushArticle")
    public ResultMessage pushArticle() throws ExecutionException, InterruptedException {
        pushService.start();
        return success();
    }
}
