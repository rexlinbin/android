package com.bccv.boxcomic.ebook;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class BookPageFactory {

	private File book_file = null;
	private MappedByteBuffer m_mbBuf = null;
	private int m_mbBufLen = 0;
	private int m_mbBufLastBegin = 0;
	private int m_mbBufLastEnd = 0;
	private int m_mbBufBegin = 0;
	private int m_mbBufEnd = 0;
	private String m_strCharsetName = "UTF-8";
	private Bitmap m_book_bg = null;
	private int mWidth;
	private int mHeight;

	private Vector<String> m_lines = new Vector<String>();

	private int m_fontSize = 34;
	private int m_textColor = Color.BLACK;
	private int m_backColor = 0xffff9e85; // 背景颜色
	private int marginWidth = 40; // 左右与边缘的距离
	private int marginHeight = 50; // 上下与边缘的距离

	private int mLineCount; // 每页可以显示的行数
	private float mVisibleHeight; // 绘制内容的宽
	private float mVisibleWidth; // 绘制内容的宽
	private boolean m_isfirstPage, m_islastPage;

	// private int m_nLineSpaceing = 5;

	public boolean isBlank = false;
	private int m_lineSpacing = 5;

	private Paint mPaint;
	private byte[] bytes;

	public BookPageFactory(int w, int h) {
		// TODO Auto-generated constructor stub
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
	 * 重置画笔
	 */
	public void reset() {
		m_lines.clear();
		m_mbBufBegin = m_mbBufLastBegin;
		m_mbBufEnd = m_mbBufLastEnd;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2;
		mLineCount = (int) (mVisibleHeight / (m_fontSize + m_lineSpacing)); // 可显示的行数
	}

	public void openbook(String strFilePath) throws IOException {
		book_file = new File(strFilePath);
		long lLen = book_file.length();
		m_mbBufLen = (int) lLen;
		m_mbBuf = new RandomAccessFile(book_file, "r").getChannel().map(
				FileChannel.MapMode.READ_ONLY, 0, lLen);
	}

	public void openBookWithContent(String content)
			throws UnsupportedEncodingException {
		bytes = content.getBytes(m_strCharsetName);
		m_mbBufLen = bytes.length;
	}

	public void setBeginIndex(int begin) {
		m_mbBufLastBegin = m_mbBufBegin;
		m_mbBufBegin = begin;
		if (begin == 0) {
			m_mbBufLastEnd = m_mbBufEnd;
			m_mbBufEnd = 0;
		} else {
			m_mbBufLastEnd = m_mbBufEnd;
			m_mbBufEnd = m_mbBufLen;
		}
		try {
			if (begin != 0) {
				m_mbBufEnd = 0;
				while (!islastPage()) {
					nextPage();
				}
			}else {
				prePage();
				nextPage();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// int lineNum = (int) getLinesFrom(0);
		// int pageNum = lineNum % mLineCount == 0 ? lineNum / mLineCount :
		// lineNum / mLineCount + 1;
		//
		// for (int i = 0; i < pageNum; i++) {
		// try {
		// nextPage();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
	}

	public int getBeginIndex() {
		return m_mbBufBegin;
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
		String strReturn = "";
		Map<String, String> map = new HashMap<String, String>();
		Vector<String> lines = new Vector<String>();
		String strParagraph = "";
		while (endIndex > 0) {

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
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0) {
				map.put(lines.size() + "", strReturn);
				paraLines.add(strParagraph);
			}

			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
				paraLines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
			}
			lines.addAll(0, paraLines);
		}
		endIndex = 0;
		int last = lines.size() % mLineCount;
		int lastSize = 0;
		if (last == 0) {
			lastSize = mLineCount;
		} else {
			lastSize = last;
		}
		List<String> list = lines.subList(0, lines.size() - lastSize);
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);
			if (string.equals("")) {
				string = strReturn;
			}
			try {
				endIndex += string.getBytes(m_strCharsetName).length;
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

	public int getFileLength() {
		return m_mbBufLen;
	}

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

	// 读取上一段落
	protected byte[] readParagraphForward(int nFromPos) {
		int nStart = nFromPos;
		int i = nStart;
		byte b0, b1;
		// 根据编码格式判断换行
		if (m_strCharsetName.equals("UTF-16LE")) {
			while (i < m_mbBufLen - 1) {
				b0 = bytes[i++];
				b1 = bytes[i++];
				if (b0 == 0x0a && b1 == 0x00) {
					break;
				}
			}
		} else if (m_strCharsetName.equals("UTF-16BE")) {
			while (i < m_mbBufLen - 1) {
				b0 = bytes[i++];
				b1 = bytes[i++];
				if (b0 == 0x00 && b1 == 0x0a) {
					break;
				}
			}
		} else {
			while (i < m_mbBufLen) {
				b0 = bytes[i++];
				if (b0 == 0x0a) {
					break;
				}
			}
		}
		int nParaSize = i - nStart;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++) {
			buf[i] = bytes[nFromPos + i];
		}
		return buf;
	}

	protected Vector<String> lastPage() {
		int positi = 0;
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

		int startIndex = lines.size() / mLineCount;

		return (Vector<String>) lines.subList(startIndex * mLineCount,
				lines.size());
	}

	protected Vector<String> pageDown() {
		m_mbBufLastEnd = m_mbBufEnd;
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		while (lines.size() < mLineCount && m_mbBufEnd < m_mbBufLen) {
			byte[] paraBuf = readParagraphForward(m_mbBufEnd); // 读取一个段落
			m_mbBufEnd += paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String strReturn = "";
			if (strParagraph.indexOf("\r\n") != -1) {
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0) {
				lines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
				lines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
				if (lines.size() >= mLineCount) {
					break;
				}
			}
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
		return lines;
	}

	protected void pageUp() {
		if (m_mbBufBegin < 0)
			m_mbBufBegin = 0;
		if (m_mbBufLastBegin < 0)
			m_mbBufLastBegin = 0;
		m_mbBufLastBegin = m_mbBufBegin;
		m_mbBufLastEnd = m_mbBufEnd;
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
			strParagraph = strParagraph.replaceAll("\r\n", "");
			strParagraph = strParagraph.replaceAll("\n", "");

			if (strParagraph.length() == 0) {
				paraLines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
				paraLines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
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
		m_mbBufEnd = m_mbBufBegin;
		return;
	}

	protected void prePage() throws IOException {
		if (m_mbBufBegin <= 0) {
			m_mbBufBegin = 0;
			m_isfirstPage = true;
			m_islastPage = false;
			return;
		} else
			m_isfirstPage = false;
		m_lines.clear();
		pageUp();
		m_lines = pageDown();
	}

	public void currPage() {
		if (m_mbBufBegin <= 0) {
			m_mbBufBegin = 0;
			m_isfirstPage = true;
			m_islastPage = false;
			return;
		} else
			m_isfirstPage = false;

		if (m_mbBufEnd >= m_mbBufLen) {
			m_islastPage = true;
			m_isfirstPage = false;
			return;
		} else
			m_islastPage = false;
		m_mbBufLastBegin = m_mbBufBegin;
		m_mbBufLastEnd = m_mbBufEnd;
		m_mbBufEnd = m_mbBufBegin;
		m_lines.clear();
		m_lines = pageDown();
	}

	public void nextPage() throws IOException {
		if (m_mbBufEnd >= m_mbBufLen) {
			m_islastPage = true;
			m_isfirstPage = false;
			return;
		} else
			m_islastPage = false;
		m_mbBufLastBegin = m_mbBufBegin;
		m_mbBufLastEnd = m_mbBufEnd;
		m_lines.clear();
		m_mbBufBegin = m_mbBufEnd;
		m_lines = pageDown();
	}

	public void draw(Canvas c) {
		isBlank = false;
		if (m_lines.size() == 0)
			m_lines = pageDown();
		if (m_lines.size() > 0) {
			if (m_book_bg == null)
				c.drawColor(m_backColor);
			else
				c.drawBitmap(m_book_bg, 0, 0, null);
			int y = marginHeight;
			for (String strLine : m_lines) {
				y += m_fontSize + m_lineSpacing;
				c.drawText(strLine, marginWidth, y, mPaint);
			}
		}

		float totalLine = getLinesFrom(0);
		float currline = totalLine - getLinesFrom(m_mbBufBegin);
		int pagesize = totalLine % mLineCount > 0 ? (int) (totalLine / mLineCount) + 1
				: (int) (totalLine / mLineCount);

		int totalPage = (int) (pagesize * currline / totalLine) + 1;

		String strPercent = (int) (totalPage) + "/" + pagesize;
		int nPercentWidth = (int) mPaint.measureText("999999") + 1;
		c.drawText(strPercent, mWidth - nPercentWidth, mHeight - 5, mPaint); // 画进度

		// float fPercent = (float) (m_mbBufBegin * 1.0 / m_mbBufLen);
		// DecimalFormat df = new DecimalFormat("#0.0");
		// String strPercent = df.format(fPercent * 100) + "%";
		// int nPercentWidth = (int) mPaint.measureText("999.9%") + 1;
		// c.drawText(strPercent, mWidth - nPercentWidth, mHeight - 5, mPaint);
	}

	public void drawBlank(Canvas c) {
		isBlank = true;
		if (m_book_bg == null)
			c.drawColor(m_backColor);
		else
			c.drawBitmap(m_book_bg, 0, 0, null);
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
		}
		return lines.size();

	}

	public void setBgBitmap(Bitmap BG) {
		m_book_bg = BG;
	}

	/**
	 * 设置字体大小 默认为30
	 * 
	 * @param fontsize
	 */
	public void setFontSize(int fontsize) {
		m_fontSize = fontsize;
	}

	/**
	 * 设置字体颜色 默认为黑色
	 * 
	 * @param textColor
	 */
	public void setTextColor(int textColor) {
		m_textColor = textColor;
	}

	/**
	 * 设置阅读背景色颜色 默认为 0xffff9e85
	 * 
	 * @param backColor
	 */
	public void setBackGroundColor(int backColor) {
		m_backColor = backColor;
	}

	/**
	 * 设置行间距 默认为 5
	 * 
	 * @param lineSpacing
	 */
	public void setLineSpacing(int lineSpacing) {
		m_lineSpacing = lineSpacing;
	}

	public void setIsFirstPage(boolean isfirstPage) {
		this.m_isfirstPage = isfirstPage;
	}

	public void setIsLastPage(boolean islastPage) {
		this.m_islastPage = islastPage;
	}

	public boolean isfirstPage() {
		return m_isfirstPage;
	}

	public boolean islastPage() {
		return m_islastPage;
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

		System.gc();
	}
}
