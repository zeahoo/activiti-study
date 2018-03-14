package com.sample;

import org.activiti.engine.EngineServices;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class AutoListener implements ExecutionListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        EngineServices engineServices = execution.getEngineServices();
        RuntimeService runtimeService = engineServices.getRuntimeService();
        runtimeService.signal(execution.getId());
    }

}
