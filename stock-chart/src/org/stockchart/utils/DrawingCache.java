/**
 * Copyright 17.04.2013 Alex Vikulov (vikuloff@gmail.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.stockchart.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;


public class DrawingCache 
{
	public class Params
	{
		public Canvas canvas;
		public Bitmap bitmap;
		
		public boolean isValid()
		{
			return canvas != null && bitmap != null;
		}
		
		public void recycle()
		{
			if(null != canvas)
			{
				canvas = null;
			}
			
			if(null != bitmap)
			{
				bitmap.recycle();
				bitmap = null;
			}
		}
		
	}
	
	private Params fParams = null;
	
	public Params getParams(Canvas c)
	{
		if(!verifyParams(fParams,c))
		{
			recycle();
			
			fParams = new Params();
			fParams.bitmap = Bitmap.createBitmap(c.getWidth(), c.getHeight(), Config.ARGB_8888);
			fParams.canvas = new Canvas(fParams.bitmap);
		}
		
		return fParams;
	}
	
	public void recycle()
	{
		if(null != fParams)
		{
			fParams.recycle();
		}
	}
	
	public static boolean verifyParams(Params p,Canvas c)
	{
		if(null == p || !p.isValid()) return false;
		
		return (p.canvas.getWidth() == c.getWidth() && p.canvas.getHeight() == c.getHeight());
	}
}
