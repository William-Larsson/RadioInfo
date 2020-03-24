package controller;

/**
 * Functional interface with sole purpose to signal that
 * the worker is done
 */
@FunctionalInterface
public interface WorkerFunctionalInterface {
    void done();
}