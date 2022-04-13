package com.vincent.mutualan.mutualankuy.repository.custom;

import javax.persistence.NoResultException;

@FunctionalInterface
public interface CustomRetrieverRepository<T> {

  T get() throws NoResultException;
}