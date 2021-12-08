package com.zt.zeus.transfer.controller;

import com.zt.zeus.transfer.properties.QueryProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/8/18 17:11
 * description:
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "redirect:/swagger-ui/index.html";
    }
}
