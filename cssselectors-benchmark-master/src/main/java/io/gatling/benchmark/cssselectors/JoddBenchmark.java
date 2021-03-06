/*
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package io.gatling.benchmark.cssselectors;

import java.util.List;
import java.util.concurrent.TimeUnit;

import jodd.lagarto.dom.LagartoDOMBuilder;
import jodd.lagarto.dom.Node;
import jodd.lagarto.dom.NodeSelector;

import jodd.util.UnsafeUtil;
import org.openjdk.jmh.annotations.*;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class JoddBenchmark {

	private static final LagartoDOMBuilder LAGARTO_DOM_BUILDER = new LagartoDOMBuilder();

	@Param({"0", "1", "2", "3"})
	public int sample;

	@Benchmark
	public List<Node> parseJava8CharStealing() {
		Data data = Data.DATA[sample];
		char[] chars = UnsafeUtil.getChars(data.toString());
		return new NodeSelector(LAGARTO_DOM_BUILDER.parse(chars)).select(data.joddSelectors);
	}

	@Benchmark
	public List<Node> parseJava8CharDirect() {
		Data data = Data.DATA[sample];
		char[] chars = data.toChars();
		return new NodeSelector(LAGARTO_DOM_BUILDER.parse(chars)).select(data.joddSelectors);
	}

	@Benchmark
	public List<Node> parseCharCopy() {
		Data data = Data.DATA[sample];
		char[] chars = data.toString().toCharArray();
		return new NodeSelector(LAGARTO_DOM_BUILDER.parse(chars)).select(data.joddSelectors);
	}
}
