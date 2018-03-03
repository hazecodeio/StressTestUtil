package org.hsmak.shuffle;

import org.hsmak.shuffle.model.CallableReplica;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ReplicaShuffler<T> {
    private Set<CallableReplica<T>> callableReplicas;
    private int parallelismFactor;//calculate from replicaFactor, number of callables, and number of replicas of each callable

    private List<Callable<T>> replicatedCallables = new ArrayList<>();


    public ReplicaShuffler() {
    }

    public ReplicaShuffler(Set<CallableReplica<T>> callableReplicas) {
        this.callableReplicas = callableReplicas;
    }


    public ReplicaShuffler<T> shuffle() throws InterruptedException {

        replicatedCallables.addAll(callableReplicas.stream()
                .flatMap(
                        c -> {
                            Callable<T> callable = c.getCallable();
                            int replicas = c.getReplicas();

                            Stream<Callable<T>> callableStream = IntStream.rangeClosed(1, replicas).mapToObj(i -> callable);
                            return callableStream;

                        })
                .collect(Collectors.toList()));

        return this;


    }

    public ReplicaShuffler<T> withReplicaFactor(int factor) {

        if (factor <= 0)
            return this;

        replicatedCallables = IntStream.rangeClosed(1, factor)
                .mapToObj(
                        i -> replicatedCallables).flatMap(i -> i.stream()).collect(Collectors.toList());

        return this;
    }

    public Set<CallableReplica<T>> getCallableReplicas() {
        return callableReplicas;
    }

    public void setCallableReplicas(Set<CallableReplica<T>> callableReplicas) {
        this.callableReplicas = callableReplicas;
    }

    public int getParallelismFactor() {
        return parallelismFactor;
    }

    public void setParallelismFactor(int parallelismFactor) {
        this.parallelismFactor = parallelismFactor;
    }

    public void setReplicatedCallables(List<Callable<T>> replicatedCallables) {
        this.replicatedCallables = replicatedCallables;
    }

    public List<Callable<T>> getReplicatedCallables() {
        return replicatedCallables;
    }
}
