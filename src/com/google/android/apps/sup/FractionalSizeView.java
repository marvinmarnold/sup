package com.google.android.apps.sup;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;

public class FractionalSizeView extends Activity {
	  public FractionalSizeView(Context context, AttributeSet attrs) {
	    super();
	  }

	  public FractionalSizeView(Context context, AttributeSet attrs, int defStyle) {
	    super();
	  }

	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    int width = MeasureSpec.getSize(widthMeasureSpec);
	    setMeasuredDimension(width * 70 / 100, 0);
	  }

	private void setMeasuredDimension(int i, int j) {
		// TODO Auto-generated method stub
		
	}
	}