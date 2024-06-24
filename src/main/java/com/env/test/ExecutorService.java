package com.env.test;

import org.camunda.bpm.engine.RuntimeService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ExecutorService {

    private static final Logger log= LoggerFactory.getLogger(ExecutorService.class);

    @Autowired
    @Qualifier("runtimeService")
    public RuntimeService runtimeService;

    @Autowired
    public ThreadPoolTaskExecutor taskExecutor;

    public JSONObject executeCamundaProcess(String processName, Map<String,Object> payload) throws JSONException {
        log.info("Process name <{}> , varibales <{}>",processName,payload);
        JSONObject respone=new JSONObject();
        try{
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    runtimeService.startProcessInstanceByKey(processName,payload);
                }
            };

            taskExecutor.submit(runnable);
            respone.put("message","accepted");
            respone.put("errorCode",0);
            respone.put("errorMessage","");
        }catch (Exception e){
            log.info(e.getLocalizedMessage());
            respone.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR);
            respone.put("errorMessage",e.getLocalizedMessage());
        }
        return respone;

    }
}
