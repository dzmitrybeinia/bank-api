package com.dzmitrybeinia.operations;

public interface OperationProcessor {
    void processOperation();

    OperationType getOperationType();
}
