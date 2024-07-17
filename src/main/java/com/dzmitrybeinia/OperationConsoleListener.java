package com.dzmitrybeinia;

import com.dzmitrybeinia.operations.OperationProcessor;
import com.dzmitrybeinia.operations.OperationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class OperationConsoleListener {

    private final Scanner scanner;
    private final Map<OperationType, OperationProcessor> processors;

    public OperationConsoleListener(Scanner scanner, List<OperationProcessor> processorList) {
        this.scanner = scanner;
        this.processors = processorList.stream()
                .collect(Collectors.toMap(OperationProcessor::getOperationType, processor -> processor));
    }

    public void start() {
        System.out.println("Start listener");
    }

    public void end() {
        System.out.println("End listener");
    }

    public void listenUpdates() {
        System.out.println("Please type operation:\n");
        while (!Thread.currentThread().isInterrupted()) {
            var operationType = listenNextOperation();
            if(operationType == null) {
                return;
            }
            processNextOperation(operationType);
        }
    }

    private OperationType listenNextOperation() {
        System.out.println("Please type next operation:");
        while (!Thread.currentThread().isInterrupted()) {
            printAllAvailableOperations();
            var nextOperation = scanner.nextLine();
            try {
                return OperationType.valueOf(nextOperation);
            } catch (IllegalArgumentException e) {
                System.out.println("No such command found");
            }
        }
        return null;
    }

    private void printAllAvailableOperations() {
        processors.keySet().forEach(System.out::println);
    }

    public void processNextOperation(OperationType operation) {
        try {
            processors.get(operation).processOperation();
        } catch (Exception ex) {
            System.out.printf("Error executing command %s: error: %s%n", operation, ex.getMessage());
        }
    }
}
