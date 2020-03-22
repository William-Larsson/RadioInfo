package controller;

import javax.swing.*;

public class Worker extends SwingWorker<Object[][], Object> {
    /**
     * Functional interface with sole purpose to signal that
     * the worker is done
     */
    @FunctionalInterface
    public interface WorkerFunctionalInterface {
        void done();
    }

    private WorkerFunctionalInterface workerFunc;

    /**
     * Init the Swingworker.
     * @param workerFunc
     */
    public Worker(WorkerFunctionalInterface workerFunc) {
        this.workerFunc = workerFunc;
    }

    /**
     * Parse XML and return the parsed values.
     * @return Object[][]
     */
    @Override
    protected Object[][] doInBackground() {
        try {
            //return ;
        } catch (Exception e){
            return null;
        }
        return null;
    }


    @Override
    protected void done() {
        workerFunc.done();
    }
}
