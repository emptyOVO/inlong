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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
/**
 * ArrayExceptFunction
 * description: ARRAY_EXCEPT(array1, array2)--Returns an ARRAY that contains the elements from array1 that are not in
 *              array2, without duplicates. If no elements remain after excluding the elements in array2 from array1,
 *              the function returns an empty ARRAY. If one or both arguments are NULL, the function returns NULL.
 *              The order of the elements from array1 is kept.
 * for example: array_except(array('he',7,'xxd'),array('he'))--return [7, xxd]
 *              array_except(array('he',7,'xxd'),array('cloud'))--return [he, 7, xxd]
 */
@TransformFunction(names = {"array_except"})
public class ArrayExceptFunction implements ValueParser {

    private final ValueParser leftArrayParser;

    private final ValueParser rightArrayParser;

    public ArrayExceptFunction(Function expr) {
        this.leftArrayParser = OperatorTools.buildParser(expr.getParameters().getExpressions().get(0));
        this.rightArrayParser = OperatorTools.buildParser(expr.getParameters().getExpressions().get(1));
    }

    @Override
    public Object parse(SourceData sourceData, int rowIndex, Context context) {
        Object leftArrayObj = leftArrayParser.parse(sourceData, rowIndex, context);
        Object rightArrayObj = rightArrayParser.parse(sourceData, rowIndex, context);
        if (leftArrayObj == null || rightArrayObj == null) {
            return null;
        }
        if (leftArrayObj instanceof ArrayList && rightArrayObj instanceof ArrayList) {
            ArrayList<?> leftArray = (ArrayList<?>) leftArrayObj;
            ArrayList<?> rightArray = (ArrayList<?>) rightArrayObj;
            if (leftArray.isEmpty() || rightArray.isEmpty()) {
                return null;
            }
            Set<Object> res = new LinkedHashSet<>();

            for (Object value : leftArray) {
                if (!rightArray.contains(value)) {
                    res.add(value);
                }
            }
            return new ArrayList<>(res);
        }
        return null;
    }
}
