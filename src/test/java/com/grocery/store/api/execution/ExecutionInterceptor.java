package com.grocery.store.api.execution;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.ArrayList;
import java.util.List;

public class ExecutionInterceptor implements IMethodInterceptor {

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {

        List<IMethodInstance> filtered = new ArrayList<>();

        for (IMethodInstance method : methods) {

            if (shouldExecuteTest(method)) {
                filtered.add(method);
                System.out.println("RUNNING: " + getTestName(method));
            } else {
                System.out.println("SKIPPED: " + getTestName(method));
            }
        }

        System.out.println("Execution Summary:");
        System.out.println("Total: " + methods.size());
        System.out.println("Executed: " + filtered.size());

        return filtered;
    }

    private String getTestName(IMethodInstance method) {
        return method.getMethod().getTestClass().getRealClass().getSimpleName()
                + "." + method.getMethod().getMethodName();
    }

    private boolean shouldExecuteTest(IMethodInstance method) {

        String[] groups = method.getMethod().getGroups();

        if (groups == null || groups.length == 0) {
            return true;
        }

        for (String group : groups) {
            if (group.equals("smoke") && ExecutionController.isSmoke()) return true;
            if (group.equals("regression") && ExecutionController.isRegression()) return true;
            if (group.equals("integration") && ExecutionController.isIntegration()) return true;
        }

        return false;
    }
}