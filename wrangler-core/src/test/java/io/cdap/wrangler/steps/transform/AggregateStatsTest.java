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
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AggregateStatsTest {

  @Test
  public void testAggregateByteSizes() throws SkipRowException, StepException {
    List<Token> tokens = Arrays.asList(
      new ColumnName("size"), new Text("totalBytes")
    );
    AggregateStats step = new AggregateStats(tokens);

    List<Row> input = new ArrayList<>();
    input.add(new Row("size", "1KB"));
    input.add(new Row("size", "2KB"));
    input.add(new Row("size", "0.5KB"));

    List<Row> output = step.execute(input);
    Row result = output.get(0);
    assertEquals(3584L, result.getValue("totalBytes"));
  }

  @Test
  public void testAggregateDurations() throws SkipRowException, StepException {
    List<Token> tokens = Arrays.asList(
      new ColumnName("duration"), new Text("totalDuration")
    );
    AggregateStats step = new AggregateStats(tokens);

    List<Row> input = new ArrayList<>();
    input.add(new Row("duration", "1s"));
    input.add(new Row("duration", "500ms"));
    input.add(new Row("duration", "1.5s"));

    List<Row> output = step.execute(input);
    Row result = output.get(0);
    assertEquals(3000L, result.getValue("totalDuration"));
  }
}
