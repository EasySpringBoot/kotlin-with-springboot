package com.easy.kotlin.kotlinwithspringboot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class KotlinWithSpringbootApplication

fun main(args: Array<String>) {
    SpringApplication.run(KotlinWithSpringbootApplication::class.java, *args)
}
