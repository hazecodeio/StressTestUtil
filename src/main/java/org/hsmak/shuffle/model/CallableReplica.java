package org.hsmak.shuffle.model;

import java.util.concurrent.Callable;

public class CallableReplica<T> {
    private Callable<T> callable;
    private Integer replicas;

    public CallableReplica() {
    }

    public CallableReplica(Callable<T> callable, int replicas) {
        this.callable = callable;
        this.replicas = replicas;
    }

    public Callable<T> getCallable() {
        return callable;
    }

    public void setCallable(Callable<T> callable) {
        this.callable = callable;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallableReplica<?> that = (CallableReplica<?>) o;

        if (replicas != that.replicas) return false;
        return callable != null ? callable.equals(that.callable) : that.callable == null;
    }

    @Override
    public int hashCode() {
        int result = callable != null ? callable.hashCode() : 0;
        result = 31 * result + replicas;
        return result;
    }
}
