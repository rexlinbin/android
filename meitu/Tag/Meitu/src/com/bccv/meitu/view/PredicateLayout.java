package com.bccv.meitu.view;


import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.ScreenUtil;


public class PredicateLayout extends LinearLayout {
    int mLeft, mRight, mTop, mBottom;
    Hashtable<View, Position> map = new Hashtable<View, Position>();
    
    /**  
     * 每个view上下的间距  
     */ 
    private final int dividerLine = ScreenUtil.dp2px(getContext(), 10); 
    /**  
     * 每个view左右的间距  
     */ 
    private final int dividerCol = ScreenUtil.dp2px(getContext(), 10);  


    public PredicateLayout(Context context) { super(context);}

    public PredicateLayout(Context context, int horizontalSpacing, int verticalSpacing) { super(context);}

    public PredicateLayout(Context context, AttributeSet attrs) {super(context, attrs);}

	@SuppressLint("DrawAllocation")
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mCount = getChildCount();
        mLeft = 0;
        mRight = 0;
        mTop = getPaddingTop();
        mBottom = 0;

        map.clear();
        int j = 0;
        for (int i = 0; i < mCount; i++) {
            final View child = getChildAt(i);
           
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

            //计算当前子控件的左边距
            mLeft = getChildviewLeft(i - j, i);
            //将每次子控件宽度进行统计叠加
            mRight = mLeft + child.getMeasuredWidth();
            //此处增加onlayout中的换行判断，如果叠加的宽度大于设定的宽度则需要换行，高度即Top坐标也需重新设置
            if (mRight >= mWidth) {
                j = i;
                mLeft = getPaddingLeft();
                mRight = mLeft + child.getMeasuredWidth();
                // 重新计算top位置
				mTop += child.getMeasuredHeight() + dividerLine;
				//PS：如果发现高度还是有问题就得自己再细调了
            }
            mBottom = mTop + child.getMeasuredHeight();
            //每次的高度必须记录 否则控件会叠加到一起 
            Position position = new Position();
            position.left = mLeft;
            position.top = mTop;
            position.right = mRight;
            position.bottom = mBottom;
            map.put(child, position);
        }
        setMeasuredDimension(mWidth, mBottom+getPaddingBottom());
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(1, 1); // default of 1px spacing
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	
    	//循环设置孩子控件的位置
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            Position pos = map.get(child);
            if (pos != null) {
                child.layout(pos.left, pos.top, pos.right, pos.bottom);
            } else {
                Logger.e("PredicateLayout","onLayout","error");
            }
        }
    }

    /**
     * 空间的位置数据结构
     */
    private class Position {
        int left, top, right, bottom;
    }

    /**
     * 获取指定行指定坐标的孩子控件的左边距
     * @param IndexInRow	行号
     * @param childIndex	控件下标
     * @return
     */
    public int getChildviewLeft(int IndexInRow, int childIndex) {
        if (IndexInRow > 0) {
            return getChildviewLeft(IndexInRow - 1, childIndex - 1)
                    + getChildAt(childIndex - 1).getMeasuredWidth() + dividerCol;
        }
        return getPaddingLeft();
    }
}