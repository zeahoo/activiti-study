package me.kafeitu.activiti.executionlistenerintask;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class ExecutionListenerInTaskListenerForTask implements TaskListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("output in ExecutionListenerInTaskListenerForTask");
    }

}
