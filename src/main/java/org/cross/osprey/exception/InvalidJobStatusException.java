package org.cross.osprey.exception;

public class InvalidJobStatusException extends RuntimeException {
  public InvalidJobStatusException(String status) {
    super("invalid job status: " + status);
  }
}
