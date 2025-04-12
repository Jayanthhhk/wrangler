/*
 * Copyright © 2024-2025 YOUR NAME
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cdap.wrangler.steps.transform;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.exception.SkipRowException;
import io.cdap.wrangler.api.exception.StepException;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.Text;
import io.cdap.wrangler.api.parser.Token;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.step.Step;
import io.cdap.wrangler.api.annotations.Name;
import io.cdap.wrangler.api.annotations.Description;
import io.cdap.wrangler.api.annotations.Categories;

import java.util.List;

@Name("aggregate-stats")
@Description("Aggregates a column of byte sizes or time durations and outputs the total")
@Categories(categories = {"aggregate", "statistics", "bytes", "time"})
public class AggregateStats implements Step {
  private final String columnName;
  private final String outputColumn;

  public AggregateStats(List<Token> args) throws StepException {
    if (args.size() != 2 ||
        args.get(0).type() != TokenType.COLUMN_NAME ||
        args.get(1).type() != TokenType.TEXT) {
      throw new StepException("Usage: aggregate-stats :columnName 'outputColumn'");
    }

    this.columnName = ((ColumnName) args.get(0)).value();
    this.outputColumn = ((Text) args.get(1)).value();
  }

  @Override
  public List<Row> execute(List<Row> rows) throws SkipRowException {
    long total = 0L;

    for (Row row : rows) {
      Object value = row.getValue(columnName);

      if (value instanceof Number) {
        total += ((Number) value).longValue();
      } else if (value instanceof String) {
        String valStr = ((String) value).trim().toLowerCase();
        try {
          if (valStr.endsWith("b") || valStr.endsWith("kb") || valStr.endsWith("mb") ||
              valStr.endsWith("gb") || valStr.endsWith("tb")) {
            total += parseByteSize(valStr);
          } else if (valStr.endsWith("ms") || valStr.endsWith("s") ||
                     valStr.endsWith("m") || valStr.endsWith("h")) {
            total += parseTimeDuration(valStr);
          }
        } catch (Exception e) {
          throw new SkipRowException("Failed to parse: " + valStr, e);
        }
      }
    }

    Row result = new Row();
    result.add(outputColumn, total);
    return List.of(result);
  }

  private long parseByteSize(String input) {
    input = input.toLowerCase().replaceAll("\\s+", "");
    double value;
    long multiplier;

    if (input.endsWith("kb")) {
      value = Double.parseDouble(input.replace("kb", ""));
      multiplier = 1024L;
    } else if (input.endsWith("mb")) {
      value = Double.parseDouble(input.replace("mb", ""));
      multiplier = 1024L * 1024L;
    } else if (input.endsWith("gb")) {
      value = Double.parseDouble(input.replace("gb", ""));
      multiplier = 1024L * 1024L * 1024L;
    } else if (input.endsWith("tb")) {
      value = Double.parseDouble(input.replace("tb", ""));
      multiplier = 1024L * 1024L * 1024L * 1024L;
    } else if (input.endsWith("b")) {
      value = Double.parseDouble(input.replace("b", ""));
      multiplier = 1L;
    } else {
      value = Double.parseDouble(input);
      multiplier = 1L;
    }

    return (long) (value * multiplier);
  }

  private long parseTimeDuration(String input) {
    input = input.toLowerCase().replaceAll("\\s+", "");
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
      multiplier = 60L * 1000L;
    } else if (input.endsWith("h")) {
      value = Double.parseDouble(input.replace("h", ""));
      multiplier = 60L * 60L * 1000L;
    } else {
      value = Double.parseDouble(input);
      multiplier = 1L;
    }

    return (long) (value * multiplier);
  }
}
