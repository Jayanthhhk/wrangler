/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonPrimitive;

import java.io.Serializable;

/**
 * Represents a ByteSize token like 10MB, 2GB, 0.5g, etc.
 */
public class ByteSize implements Token, Serializable {
  private final long size;

  public ByteSize(String input) {
    this.size = parseSize(input);
  }

  private long parseSize(String input) {
    input = input.trim().toLowerCase();
    double value;
    long multiplier;

    if (input.endsWith("kb")) {
      value = Double.parseDouble(input.replace("kb", ""));
      multiplier = 1024L;
    } else if (input.endsWith("k")) {
      value = Double.parseDouble(input.replace("k", ""));
      multiplier = 1024L;
    } else if (input.endsWith("mb")) {
      value = Double.parseDouble(input.replace("mb", ""));
      multiplier = 1024L * 1024L;
    } else if (input.endsWith("m")) {
      value = Double.parseDouble(input.replace("m", ""));
      multiplier = 1024L * 1024L;
    } else if (input.endsWith("gb")) {
      value = Double.parseDouble(input.replace("gb", ""));
      multiplier = 1024L * 1024L * 1024L;
    } else if (input.endsWith("g")) {
      value = Double.parseDouble(input.replace("g", ""));
      multiplier = 1024L * 1024L * 1024L;
    } else if (input.endsWith("tb")) {
      value = Double.parseDouble(input.replace("tb", ""));
      multiplier = 1024L * 1024L * 1024L * 1024L;
    } else if (input.endsWith("t")) {
      value = Double.parseDouble(input.replace("t", ""));
      multiplier = 1024L * 1024L * 1024L * 1024L;
    } else {
      throw new IllegalArgumentException("Invalid ByteSize unit: " + input);
    }

    return (long) (value * multiplier);
  }

  public long getSize() {
    return size;
  }

  @Override
  public TokenType type() {
    return TokenType.BYTE_SIZE;
  }

  @Override
  public JsonPrimitive toJson() {
    return new JsonPrimitive(size);
  }

  @Override
  public String value() {
    return String.valueOf(size);
  }
}
