package com.vincent.mutualan.mutualankuy.repository.custom;

import javax.persistence.NoResultException;
import java.util.Optional;

public class CustomRetrieveRepositoryUtil {

  public static <T> Optional<T> findOrEmpty(final CustomRetrieverRepository<T> retrieverRepository) {
    try {
      return Optional.of(retrieverRepository.get());
    } catch (NoResultException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }
}
