/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cafe.imagefilter.GPUImage.filter;

import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import android.content.Context;

public class GPUImageFilterTools {  

	public static class FilterList {
		public List<String> names = new LinkedList<String>();
		public List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
		public void addFilter(final String name, final GPUImageFilter filter) {
			names.add(name);
			filters.add(filter);
		}
	}
	public static FilterList initFilterList(Context paramContext){
		final FilterList filters = new FilterList();
		filters.addFilter("1977", new IF1977Filter(paramContext));
		filters.addFilter("Amaro", new IFAmaroFilter(paramContext));
		filters.addFilter("Brannan", new IFBrannanFilter(paramContext));
		filters.addFilter("Earlybird", new IFEarlybirdFilter(paramContext));
		filters.addFilter("Hefe", new IFHefeFilter(paramContext));
		filters.addFilter("Hudson", new IFHudsonFilter(paramContext));
		filters.addFilter("Inkwell", new IFInkwellFilter(paramContext));
		filters.addFilter("Lomo", new IFLomoFilter(paramContext));
		filters.addFilter("LordKelvin", new IFLordKelvinFilter(paramContext));
		filters.addFilter("Nashville", new IFNashvilleFilter(paramContext));
		filters.addFilter("Rise", new IFRiseFilter(paramContext));
		filters.addFilter("Sierra", new IFSierraFilter(paramContext));
		filters.addFilter("sutro",new IFSutroFilter(paramContext));
		filters.addFilter("Toaster", new IFToasterFilter(paramContext));
		filters.addFilter("Valencia", new IFValenciaFilter(paramContext));
		filters.addFilter("Walden", new IFWaldenFilter(paramContext));
		filters.addFilter("Xproll", new IFXprollFilter(paramContext));
		return filters;
	}
}
