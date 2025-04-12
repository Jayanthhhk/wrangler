/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonPrimitive;

import java.io.Serializable;

/**
 * Represents a TimeDuration token like 5s, 2m, 100ms, etc.
 */
public class TimeDuration implements Token, Serializable {
  private final long milliseconds;

  public TimeDuration(String input) {
    this.milliseconds = parseDuration(input);
  }

  private long parseDuration(String input) {
    input = input.trim().toLowerCase();
    double value;
    long multiplier;

    if (input.endsWith("ms")) {
      value = Double.parseDouble(input.replace("ms", ""));
      multiplier = 1L;
    } else if (input.endsWith("s")) {
      value = Double.parseDouble(input.replace("s", ""));
      multiplier = 1000L;
    } else if (input.endsWith("m")) {
      value = Double.parseDouble(input.replace("m", ""));
      multiplier = 60 * 1000L;
    } else if (input.endsWith("h")) {
      value = Double.parseDouble(input.replace("h", ""));
      multiplier = 60 * 60 * 1000L;
    } else {
      throw new IllegalArgumentException("Invalid TimeDuration unit: " + input);
    }

    return (long) (value * multiplier);
  }

  public long getMilliseconds() {
    return milliseconds;
  }

  @Override
  public TokenType type() {
    return TokenType.TIME_DURATION;
  }

  @Override
  public JsonPrimitive toJson() {
    return new JsonPrimitive(milliseconds);
  }

  @Override
  public Object value() {
    return milliseconds;
  }
}
