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
 * See the License for the specific language governing permissions and limitations under the License.
 */

package io.cdap.wrangler.api.step;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.exception.SkipRowException;
import io.cdap.wrangler.api.exception.StepException;

import java.util.List;

public interface Step {
  List<Row> execute(List<Row> rows) throws StepException, SkipRowException;
}

