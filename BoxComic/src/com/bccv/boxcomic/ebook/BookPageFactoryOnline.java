package com.bccv.boxcomic.ebook;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Vector;

import org.apache.http.util.ByteArrayBuffer;

import u.aly.by;

import net.sf.chineseutils.ChineseUtils;
import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;

public class BookPageFactoryOnline {
	private static final String TAG = "BookPageFactory";

	private File book_file = null;
	private ByteArrayBuffer m_mbBuf = null;
	private byte[] bytes = null;
	private RandomAccessFile randomAccessFile;
	private int m_mbBufLen = 0;
	private int m_mbBufBegin = 0;
	private int m_mbBufEnd = 0;
	private String m_strCharsetName = "UTF-8";
	private Bitmap m_book_bg = null;
	private int mWidth;
	private int mHeight;

	private Vector<String> m_lines = new Vector<String>();

	private static int m_fontSize = 34; // 字体大小
	public static int m_textColor = 0xff555555; // 字体颜色
	public static int m_backColor = 0xffcde5d5; // 背景颜色
	private static int m_lineSpacing = 6; // 行间距
	private static int marginWidth = 40; // 左右与边缘的距离
	private static int marginHeight = 50; // 上下与边缘的距离

	private int mLineCount; // 每页可以显示的行数
	private float mVisibleHeight; // 绘制内容的宽
	private float mVisibleWidth; // 绘制内容的宽
	// private boolean m_isfirstPage,m_islastPage;

	private boolean isSimpToTrad = false;

	private Paint mPaint;

	private BookPageFactoryOnline() {
	};

	public BookPageFactoryOnline(int w, int h) {
		mWidth = w;
		mHeight = h;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2;
		mLineCount = (int) (mVisibleHeight / (m_fontSize + m_lineSpacing)); // 可显示的行数
	}

	/**
	 * 设置是否简体转繁体
	 * 
	 * @param isSimpToTrad
	 */
	public void setSimpToTrad(boolean isSimpToTrad) {
		this.isSimpToTrad = isSimpToTrad;
	}

	/**
	 * 是否简体转繁体
	 * 
	 * @return
	 */
	public boolean isSimpToTrad() {
		return isSimpToTrad;
	}

	/**
	 * 设置字体大小 默认为30
	 * 
	 * @param fontsize
	 */
	public static void setFontSize(int fontsize) {
		m_fontSize = fontsize;
	}

	/**
	 * 设置字体颜色 默认为黑色
	 * 
	 * @param textColor
	 */
	public static void setTextColor(int textColor) {
		m_textColor = textColor;
	}

	/**
	 * 设置阅读背景色颜色 默认为 0xffff9e85
	 * 
	 * @param backColor
	 */
	public static void setBackGroundColor(int backColor) {
		m_backColor = backColor;
	}

	/**
	 * 设置行间距 默认为 5
	 * 
	 * @param lineSpacing
	 */
	public static void setLineSpacing(int lineSpacing) {
		m_lineSpacing = lineSpacing;
	}

	/**
	 * 设置阅读背景图片 若设置了背景图片则背景图片优先
	 * 
	 * @param bitmap
	 */
	public void setBackGround(Bitmap bitmap) {

		if (bitmap != null) {
			// 计算出宽高缩放比
			float scaleX = ((float) mWidth) / bitmap.getWidth();
			float scaleY = ((float) mHeight) / bitmap.getHeight();

			Matrix matrix = new Matrix();
			matrix.postScale(scaleX, scaleY);
			m_book_bg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
		} else {
			m_book_bg = null;
		}

	}

	/**
	 * 创建factory
	 * 
	 * @param w
	 *            画布的宽
	 * @param h
	 *            画布的高
	 * @return
	 */
	public static BookPageFactoryOnline build(int w, int h) {
		return new BookPageFactoryOnline(w, h);
	}

	/**
	 * 重置画笔
	 */
	public void reset() {
		m_lines.clear();
		m_mbBufBegin = 0;
		m_mbBufEnd = 0;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2;
		mLineCount = (int) (mVisibleHeight / (m_fontSize + m_lineSpacing)); // 可显示的行数
	}

	public void openbook(String strFilePath) throws IOException {// 打开书籍

		// final String path = Environment.getExternalStorageDirectory()
		// + "/test.txt";

		book_file = new File(strFilePath);
		long lLen = book_file.length();
		m_mbBufLen = (int) lLen;
		if (randomAccessFile != null) {
			randomAccessFile.close();
		}
		randomAccessFile = new RandomAccessFile(book_file, "r");
		m_mbBuf = new ByteArrayBuffer(m_mbBufLen);
		bytes = m_mbBuf.buffer();
	}

	public void openBookWithContent(String content)
			throws UnsupportedEncodingException {
		bytes = content.getBytes(m_strCharsetName);
		m_mbBufLen = bytes.length;
	}

	public int getFileLength() {
		return m_mbBufLen;
	}

	// 根据所给的结束位置读取上一个段落
	protected byte[] readParagraphBack(int nFromPos) {

		int nEnd = nFromPos;
		int i;
		byte b0, b1;
		if (m_strCharsetName.equals("UTF-16LE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = bytes[i];
				b1 = bytes[i + 1];
				if (b0 == 0x0a && b1 == 0x00 && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}

		} else if (m_strCharsetName.equals("UTF-16BE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = bytes[i];
				b1 = bytes[i + 1];
				if (b0 == 0x00 && b1 == 0x0a && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}
		} else {
			i = nEnd - 1;
			while (i > 0) {
				b0 = bytes[i];
				if (b0 == 0x0a && i != nEnd - 1) {
					i++;
					break;
				}
				i--;
			}
		}
		if (i < 0)
			i = 0;
		int nParaSize = nEnd - i;
		int j;
		byte[] buf = new byte[nParaSize];

		for (j = 0; j < nParaSize; j++) {
			buf[j] = bytes[i + j];
		}
		return buf;
	}

	// 读取所给的起始位置所对应的段落
	protected byte[] readParagraphForward(int nFromPos) {
		int nStart = nFromPos;
		int i = nStart;
		byte b0, b1;
		// 根据编码格式判断换行
		if (m_strCharsetName.equals("UTF-16LE")) {
			while (i < m_mbBufLen - 1) {
				b0 = bytes[i++];
				b1 = bytes[i++];
				if (b0 == 0x0a && b1 == 0x00) { // 换行符
					break;
				}
			}
		} else if (m_strCharsetName.equals("UTF-16BE")) {
			while (i < m_mbBufLen - 1) {
				b0 = bytes[i++];
				b1 = bytes[i++];
				if (b0 == 0x00 && b1 == 0x0a) { // 换行符
					break;
				}
			}
		} else {
			while (i < m_mbBufLen) {
				b0 = bytes[i++];
				if (b0 == 0x0a) { // 换行符
					break;
				}
			}
		}
		int nParaSize = i - nStart; // 段落长度
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++) {// 获取段落文字
			buf[i] = bytes[nFromPos + i];
		}
		return buf;
	}

	// 获取当前页的行
	protected Vector<String> pageDown() {

		long startTime = System.currentTimeMillis();

		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		while (lines.size() < mLineCount && m_mbBufEnd < m_mbBufLen) {
			byte[] paraBuf = readParagraphForward(m_mbBufEnd); // 读取一个段落
			m_mbBufEnd += paraBuf.length; // 标记所获取的结束位置
			try {
				strParagraph = new String(paraBuf, m_strCharsetName); // GBK
																		// 转换为Str
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String strReturn = "";
			if (strParagraph.indexOf("\r\n") != -1) { // 将换行符替换
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0) {
				lines.add(strParagraph);
			}

			int nSize = 0;// 测量 一行放下的字符长度

			if (strParagraph.length() > 0) {
				nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);

			}

			while (strParagraph.length() > 0 && nSize > 0) {
				if (nSize > strParagraph.length()) {
					nSize = strParagraph.length();

				}

				String substring = strParagraph.substring(0, nSize);
				int l = (int) mPaint.measureText(substring);
				if (l > mVisibleWidth) {
					nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
							null);
				}

				if (l + m_fontSize < mVisibleWidth
						&& strParagraph.length() > nSize + 1) {
					lines.add(strParagraph.substring(0, nSize + 1));
					strParagraph = strParagraph.substring(nSize + 1);
				} else {
					lines.add(strParagraph.substring(0, nSize));// 获取一行的文字
					strParagraph = strParagraph.substring(nSize);// 段落减去这行文字
				}

				if (lines.size() >= mLineCount) {
					break;
				}
			}
			// 如果到达每一页的最大行数 段落没有画完 则将该页的结束位置重新计算
			if (strParagraph.length() != 0) {
				try {
					m_mbBufEnd -= (strParagraph + strReturn)
							.getBytes(m_strCharsetName).length;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		L.v(TAG, "pageDown",
				String.valueOf(System.currentTimeMillis() - startTime));

		return lines;
	}

	protected Vector<String> pageUp() {

		long startTime = System.currentTimeMillis();

		if (m_mbBufBegin < 0) {
			m_mbBufBegin = 0;
		}
		Vector<String> lines = new Vector<String>();
		String strParagraph = "";
		while (lines.size() < mLineCount && m_mbBufBegin > 0) {
			Vector<String> paraLines = new Vector<String>();

			byte[] paraBuf = readParagraphBack(m_mbBufBegin);
			m_mbBufBegin -= paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (strParagraph.indexOf("\r\n") != -1) { // 将换行符替换
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0) {
				paraLines.add(strParagraph);
			}

			int nSize = 0;

			if (strParagraph.length() > 0) {
				nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
			}

			while (strParagraph.length() > 0) {
				if (nSize > strParagraph.length()) {
					nSize = strParagraph.length();
				}

				String substring = strParagraph.substring(0, nSize);
				int l = (int) mPaint.measureText(substring);
				if (l > mVisibleWidth) {
					nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
							null);
				}
				if (l + m_fontSize < mVisibleWidth
						&& strParagraph.length() > nSize + 1) {
					paraLines.add(strParagraph.substring(0, nSize + 1));
					strParagraph = strParagraph.substring(nSize + 1);
				} else {
					paraLines.add(strParagraph.substring(0, nSize));
					strParagraph = strParagraph.substring(nSize);
				}
			}
			lines.addAll(0, paraLines);
		}

		while (lines.size() > mLineCount) {
			try {
				m_mbBufBegin += lines.get(0).getBytes(m_strCharsetName).length;
				lines.remove(0);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (m_mbBufBegin < 0) {
			m_mbBufBegin = 0;
		}
		// m_mbBufEnd = m_mbBufBegin;

		L.v(TAG, "pageUp",
				String.valueOf(System.currentTimeMillis() - startTime));

		return lines;
	}

	public boolean isBlank = false;

	// 在所给画布上画出当前页的文字
	public void draw(Canvas c) { // 将背景 和 文字 画到画布上
		isBlank = false;
		long startTime = System.currentTimeMillis();

		if (m_lines.size() == 0) {
			m_lines = pageDown(); // 获取当前页的文字集合 (行)
		}

		// 清屏
		c.drawColor(Color.TRANSPARENT);
		Paint p = new Paint();
		p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		c.drawPaint(p);
		//
		// p.setXfermode(new PorterDuffXfermode(Mode.SRC));

		if (m_lines.size() > 0) {
			// 画背景
			if (m_book_bg != null) {
				c.drawBitmap(m_book_bg, 0, 0, null);
			} else {
				c.drawColor(m_backColor);
			}

			int y = marginHeight; // 起始y的位置
			for (String strLine : m_lines) {// 循环画文字
				y += m_fontSize + m_lineSpacing;// 计算起始位置

				if (isSimpToTrad) {
					strLine = ChineseUtils.simpToTrad(strLine);
				}

				c.drawText(strLine, marginWidth, y, mPaint);// 画文字
			}
		}

		float totalLine = getLinesFrom(0);
		float currline = totalLine - getLinesFrom(m_mbBufBegin);
		int pagesize = totalLine % mLineCount > 0 ? (int) (totalLine / mLineCount) + 1
				: (int) (totalLine / mLineCount);

		int totalPage = (int) (pagesize * currline / totalLine) + 1;

		String strPercent = (int) (totalPage) + "/" + pagesize;
		// float fPercent = (float) (m_mbBufBegin * 1.0 / m_mbBufLen);// 获取当前进度
		// DecimalFormat df = new DecimalFormat("#0.0");// 格式化数值
		// String strPercent = df.format(fPercent * 100) + "%";
		int nPercentWidth = (int) mPaint.measureText("999999") + 1;
		c.drawText(strPercent, mWidth - nPercentWidth, mHeight - 5, mPaint); // 画进度

		L.v(TAG, "draw", String.valueOf(System.currentTimeMillis() - startTime));
	}

	public void drawBlank(Canvas c) {
		isBlank = true;
		// 清屏
		c.drawColor(Color.TRANSPARENT);
		Paint p = new Paint();
		p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		c.drawPaint(p);

		// 画背景
		if (m_book_bg != null) {
			c.drawBitmap(m_book_bg, 0, 0, null);
		} else {
			c.drawColor(m_backColor);
		}

	}

	/**
	 * @param m_mbBufBegin
	 * @throws IOException
	 */
	public void prePage(int m_mbBufBegin) throws IOException { // 前一页
		this.m_mbBufBegin = m_mbBufBegin;
		if (this.m_mbBufBegin <= 0) { // 如果起始位置为0
			this.m_mbBufBegin = 0;
			// m_isfirstPage=true;
			return;
		} else {
			// m_isfirstPage=false;
		}
		m_lines.clear();
		m_lines = pageUp();
		// 修复 方法 或缩小字体后 页面首字与方法前不一致问题
		if (this.m_mbBufBegin == 0) {
			m_mbBufEnd = this.m_mbBufBegin;
			m_lines = pageDown();
		} else {
			m_mbBufEnd = m_mbBufBegin;
		}
	}

	/**
	 * @param m_mbBufEnd
	 * @throws IOException
	 */
	public void nextPage(int m_mbBufEnd) throws IOException {// 下一页
		this.m_mbBufEnd = m_mbBufEnd;
		if (this.m_mbBufEnd >= m_mbBufLen) {
			// m_islastPage=true;
			return;
		} else {
			// m_islastPage=false;
		}

		m_lines.clear();
		m_mbBufBegin = this.m_mbBufEnd;
		m_lines = pageDown();
	}

	/**
	 * 获取前一页的起始字符下标
	 * 
	 * @param endIndex
	 *            前一页的结束字符
	 * @return
	 */
	public int measureBeginIndex(int endIndex) {
		if (endIndex <= 0) {
			endIndex = m_mbBufLen;
		}

		Vector<String> lines = new Vector<String>();
		String strParagraph = "";
		while (lines.size() < mLineCount && endIndex > 0) {

			Vector<String> paraLines = new Vector<String>();

			byte[] paraBuf = readParagraphBack(endIndex);
			endIndex -= paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (strParagraph.indexOf("\r\n") != -1) { // 将换行符替换
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0) {
				paraLines.add(strParagraph);
			}

			int nSize = 0;

			if (strParagraph.length() > 0) {
				nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
			}

			while (strParagraph.length() > 0) {
				if (nSize > strParagraph.length()) {
					nSize = strParagraph.length();
				}

				String substring = strParagraph.substring(0, nSize);
				int l = (int) mPaint.measureText(substring);
				if (l > mVisibleWidth) {
					nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
							null);
				}
				if (l + m_fontSize < mVisibleWidth
						&& strParagraph.length() > nSize + 1) {
					paraLines.add(strParagraph.substring(0, nSize + 1));
					strParagraph = strParagraph.substring(nSize + 1);
				} else {
					paraLines.add(strParagraph.substring(0, nSize));
					strParagraph = strParagraph.substring(nSize);
				}
			}

			// while (strParagraph.length() > 0) {
			// int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
			// null);
			// paraLines.add(strParagraph.substring(0, nSize));
			// strParagraph = strParagraph.substring(nSize);
			// }
			lines.addAll(0, paraLines);
		}
		while (lines.size() > mLineCount) {
			try {
				endIndex += lines.get(0).getBytes(m_strCharsetName).length;
				lines.remove(0);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (endIndex < 0) {
			endIndex = 0;
		}

		return endIndex;
	}

	public boolean isFirstPage() {
		return m_mbBufBegin <= 0 ? true : false;
	}

	public boolean isLastPage() {
		return m_mbBufEnd >= m_mbBufLen ? true : false;
	}

	public int getBiginIndex() {
		return m_mbBufBegin;
	}

	public int getEndIndex() {
		return m_mbBufEnd;
	}

	private float getLinesFrom(int positi) {

		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		while (positi < m_mbBufLen) {
			byte[] paraBuf = readParagraphForward(positi); // 读取一个段落
			positi += paraBuf.length; // 标记所获取的结束位置
			try {
				strParagraph = new String(paraBuf, m_strCharsetName); // GBK
																		// 转换为Str
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String strReturn = "";
			if (strParagraph.indexOf("\r\n") != -1) { // 将换行符替换
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0) {
				lines.add(strParagraph);
			}

			int nSize = 0;// 测量 一行放下的字符长度

			if (strParagraph.length() > 0) {
				nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);

			}

			while (strParagraph.length() > 0 && nSize > 0) {
				if (nSize > strParagraph.length()) {
					nSize = strParagraph.length();

				}

				String substring = strParagraph.substring(0, nSize);
				int l = (int) mPaint.measureText(substring);
				if (l > mVisibleWidth) {
					nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
							null);
				}

				if (l + m_fontSize < mVisibleWidth
						&& strParagraph.length() > nSize + 1) {
					lines.add(strParagraph.substring(0, nSize + 1));
					strParagraph = strParagraph.substring(nSize + 1);
				} else {
					lines.add(strParagraph.substring(0, nSize));// 获取一行的文字
					strParagraph = strParagraph.substring(nSize);// 段落减去这行文字
				}

			}
			// 如果到达每一页的最大行数 段落没有画完 则将该页的结束位置重新计算
			if (strParagraph.length() != 0) {
				try {
					positi -= (strParagraph + strReturn)
							.getBytes(m_strCharsetName).length;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return lines.size();

	}

	/**
	 * 关闭文件
	 */
	public void close() {
		if (m_mbBuf != null) {
			m_mbBuf = null;
		}
		if (bytes != null) {
			bytes = null;
		}
		if (m_book_bg != null) {
			m_book_bg.recycle();
		}
		if (randomAccessFile != null) {
			try {
				randomAccessFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			randomAccessFile = null;
			book_file = null;
		}
		System.gc();
	}
}
