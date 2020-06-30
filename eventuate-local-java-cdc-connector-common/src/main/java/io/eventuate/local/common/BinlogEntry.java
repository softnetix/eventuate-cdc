package io.eventuate.local.common;

import io.eventuate.local.BinlogFileOffset;

public interface BinlogEntry {
  Object getColumn(String name);
  BinlogFileOffset getBinlogFileOffset();

  default String getJsonColumn(String name) {
    return getStringColumn(name);
  }

  default String getStringColumn(String name) {
    Object columnValue = getColumn(name);

    if (columnValue == null) {
      return null;
    }

    if (columnValue instanceof String) return (String) columnValue;

    throw new IllegalArgumentException(String.format("Unexpected type %s of column %s, should be String", columnValue.getClass(), name));
  }
}
