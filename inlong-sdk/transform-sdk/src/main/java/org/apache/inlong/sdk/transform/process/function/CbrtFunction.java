/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.inlong.sdk.transform.process.function;

import org.apache.inlong.sdk.transform.decode.SourceData;
import org.apache.inlong.sdk.transform.process.Context;
import org.apache.inlong.sdk.transform.process.operator.OperatorTools;
import org.apache.inlong.sdk.transform.process.parser.ValueParser;

import net.sf.jsqlparser.expression.Function;

/**
 * CbrtFunction   -> cbrt(numeric)
 * description:
 * - return NULL if numeric is NULL
 * - return cube root
 */
@TransformFunction(names = {"cbrt"})
public class CbrtFunction implements ValueParser {

    private ValueParser numParser;

    public CbrtFunction(Function expr) {
        numParser = OperatorTools.buildParser(expr.getParameters().getExpressions().get(0));
    }

    @Override
    public Object parse(SourceData sourceData, int rowIndex, Context context) {
        Object numObj = numParser.parse(sourceData, rowIndex, context);
        if (numObj == null) {
            return null;
        }
        double num = OperatorTools.parseBigDecimal(numObj).doubleValue();
        return Math.pow(num, 1.0 / 3.0);
    }
}
