package com.ryan.springbootquartzcluster;

import com.ryan.springbootquartzcluster.task.MyTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootQuartzClusterApplication {


    public static void main(String[] args) {
        SpringApplication.run(SpringbootQuartzClusterApplication.class, args);
    }

}
