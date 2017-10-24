package com.easy.kotlin.kotlinwithspringboot

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class HelloWorldController {

    @RequestMapping("/")
    @ResponseBody
    fun home(): String {
        return "Hello World!"
    }

}
