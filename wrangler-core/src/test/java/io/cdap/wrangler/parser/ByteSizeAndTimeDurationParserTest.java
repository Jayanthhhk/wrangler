/*
 * Copyright © 2024 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.wrangler.parser;

import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import org.junit.Assert;
import org.junit.Test;

public class ByteSizeAndTimeDurationParserTest {

  @Test
  public void testByteSizeParsing() {
    ByteSize oneKB = new ByteSize("1k");
    Assert.assertEquals(1024L, oneKB.getSize());

    ByteSize fiveMB = new ByteSize("5MB");
    Assert.assertEquals(5 * 1024 * 1024L, fiveMB.getSize());

    ByteSize halfGB = new ByteSize("0.5g");
    Assert.assertEquals((long) (0.5 * 1024 * 1024 * 1024), halfGB.getSize());
  }

  @Test
  public void testTimeDurationParsing() {
    TimeDuration tenSeconds = new TimeDuration("10s");
    Assert.assertEquals(10_000L, tenSeconds.getMilliseconds());

    TimeDuration fiveMinutes = new TimeDuration("5m");
    Assert.assertEquals(5 * 60 * 1000L, fiveMinutes.getMilliseconds());

    TimeDuration twoHours = new TimeDuration("2h");
    Assert.assertEquals(2 * 60 * 60 * 1000L, twoHours.getMilliseconds());
  }
}
