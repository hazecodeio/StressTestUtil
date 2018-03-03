package org.hsmak;

import org.hsmak.shuffle.ReplicaShuffler;
import org.hsmak.shuffle.model.CallableReplica;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static java.lang.System.out;

public class StressTestMain {
    public static void main(String[] args) throws InterruptedException {

        CallableReplica<String> callableReplica1 = new CallableReplica<String>(
                () -> {
                    System.out.println("entering replica 1");
                    return "replica 1";

                }, 2);

        CallableReplica<String> callableReplica2 = new CallableReplica<String>(
                () -> {
                    System.out.println("entering replica 2");
                    return "replica 2";
                }, 3);

        CallableReplica<String> callableReplica3 = new CallableReplica<String>(
                () -> {
                    System.out.println("entering replica 3");
                    return "replica 3";
                }, 6);

        Set<CallableReplica<String>> callableReplicas = new HashSet<>();
        callableReplicas.add(callableReplica1);
        callableReplicas.add(callableReplica2);
        callableReplicas.add(callableReplica3);
        ReplicaShuffler<String> replicaShuffler = new ReplicaShuffler(callableReplicas);

        int replicaFactor = 40;
        List<Callable<String>> replicatedCallables = replicaShuffler.shuffle().withReplicaFactor(replicaFactor).getReplicatedCallables();
        out.println(replicatedCallables.size());

        int sum = callableReplicas.stream().mapToInt(c -> c.getReplicas()).sum();
        double parallelismFactor = 0.1;//1 = maximum parallelism
        int poolSize = (int)((sum*replicaFactor)* parallelismFactor);
        System.out.println(poolSize);

        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        List<Future<String>> futures = executorService.invokeAll(replicatedCallables);
        futures.stream().map(f -> {
            try {
                return f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return "";
        }).forEach(out::println);
        executorService.shutdown();

        Set<Callable<Integer>> callables = new HashSet(replicatedCallables);
        out.println(callables);
    }

    public void withDuration(){

    }

    public void withRotation(){

    }
}