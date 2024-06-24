package com.env.test;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class Controller {
    private static final Logger log= LoggerFactory.getLogger(Controller.class);
    @Autowired
    public RuntimeService runtimeService;
    @Autowired
    public ProcessEngine processEngine;
    @Autowired
    public ExecutorService executorService;

    @PostMapping(value = "/{processID}/start",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> startProcess(@PathVariable("processID") String processID,
                                               @RequestBody Object payload
                                               ) throws JSONException {
        System.out.println("Hi");
        @SuppressWarnings("unchecked")
		Map<String,Object> variables=new HashMap<>(
                new ObjectMapper().convertValue(payload,Map.class)
        );
        System.out.println("Hi");
        variables.put("requestPayload",new HashMap<>(
                new ObjectMapper().convertValue(payload,Map.class)
        ));
        System.out.println("Hi");
        JSONObject response = executorService.executeCamundaProcess(processID,variables);
        return ResponseEntity.accepted().body(response.toString());
    }

}
