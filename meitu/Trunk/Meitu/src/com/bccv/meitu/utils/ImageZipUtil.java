package com.bccv.meitu.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
  
/** 
 * 图片工具类. 
 */  
public class ImageZipUtil {  
  
	public static String rootPath = SystemUtil.getMeiTuCacheFolder(SystemUtil.CACHE_IMAGE_TYPE)+File.separator;
	public static String TAG = "ImageZipUtil";
    /** 
     * 获取合适的Bitmap平时获取Bitmap就用这个方法吧. data/uri/path只需要一种,uri时context不能为null.
     * @param path 路径. 
     * @param data byte[]数组. 
     * @param context 上下文 
     * @param uri uri 
     * @param target 模板宽或者高的大小. 
     * @param width 是否是宽度 
     * @return 
     */  
    public Bitmap getResizedBitmap(String path, byte[] data,  
            Context context,Uri uri, int target, boolean width) {  
        Options options = null;  
  
        if (target > 0) {  
  
            Options info = new Options();  
            //这里设置true的时候，decode时候Bitmap返回的为空，  
            //将图片宽高读取放在Options里.  
            info.inJustDecodeBounds = false;  
              
            decode(path, data, context,uri, info);  
              
            int dim = info.outWidth;  
            if (!width)  
                dim = Math.max(dim, info.outHeight);  
            int ssize = sampleSize(dim, target);  
  
            options = new Options();  
            options.inSampleSize = ssize;  
  
        }  
  
        Bitmap bm = null;  
        try {  
            bm = decode(path, data, context,uri, options);  
        } catch(Exception e){  
            e.printStackTrace();  
        }  
        return bm;  
  
    }  
      
    /** 
     * 解析Bitmap的公用方法. 
     * @param path 
     * @param data 
     * @param context 
     * @param uri 
     * @param options 
     * @return 
     */  
    public Bitmap decode(String path, byte[] data, Context context,  
            Uri uri, BitmapFactory.Options options) {  
  
        Bitmap result = null;  
  
        if (path != null) {  
  
            result = BitmapFactory.decodeFile(path, options);  
  
        } else if (data != null) {  
  
            result = BitmapFactory.decodeByteArray(data, 0, data.length,  
                    options);  
  
        } else if (uri != null) {  
            //uri不为空的时候context也不要为空.  
            ContentResolver cr = context.getContentResolver();  
            InputStream inputStream = null;  
  
            try {  
                inputStream = cr.openInputStream(uri);  
                result = BitmapFactory.decodeStream(inputStream, null, options);  
                inputStream.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
  
        }  
  
        return result;  
    }
    
    public static File zipPic(String path,int width,int height,Uri uri,Context context){
    	//为解析图片 设置一个选项 
        Options opts = new Options();
	    //设置选项的inJustDecodeBounds  告诉解析器是否真正去解析器   
	    //值为true时，不去真正解析这个位图，而是解析这个图片的out信息，（高度宽度）不会为图片的的每个点 申请内        存空间
	    if (path != null) {
	    	long fileSize = FileUtils.getFileSize(path);
	    	if (fileSize / 1024 < 300) {
//	    		opts.inJustDecodeBounds = false;
//				Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
//	    		return saveFile(bitmap);
	    		return copyFile(path,rootPath+System.currentTimeMillis()+".jpg");
			}else{
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(path, opts);
			}
		}else if (uri != null) {
			ContentResolver cr = context.getContentResolver();  
            InputStream inputStream = null;  
            try {  
                inputStream = cr.openInputStream(uri);  
                BitmapFactory.decodeStream(inputStream, null, opts);  
                inputStream.close();  
            } catch (Exception e) {  
                Logger.e("ZipImage", "parse uri exception", e.getMessage());
                return null;
            }  
		}else{
			Logger.e("ZipImage", "Both path and uri are null", "NullPointException");
			return null;
		}
	    //解析图片的out信息
	    //在解析到的ou信息中获取图片的高和宽
	    int picHeight = opts.outHeight;
	    int picWidth = opts.outWidth;
	
	    //计算出宽高缩放比
	    int scaleX = picWidth/width;
	
	    int scaleY = picHeight/height;
	
	    //如果宽的缩放比大于高的缩放比  缩放比按宽缩放比
	    int scale = 1;
	    if(scaleX>scaleY && scaleX>1){
	        scale = scaleX;
	    }
	
	    //如果高的缩放比大于宽的缩放比  缩放比按高缩放比
	    if(scaleY>scaleX && scaleY>1){
	        scale = scaleY;
	    }
	
	    //确定缩放比后  真正的开始解析该图片   设置选项的 inJustDecodeBounds 为false  
	    opts.inJustDecodeBounds = false;
	
	    //指定采样图片跟原图的缩放比   如果设置的值>1 则解析图片按缩放比 进行缩放
	    opts.inSampleSize = scale;
	
	    //根据指定的选项 解析出采样图片  分两种情况进行解析,,与前面对应
	    Bitmap bitmap = null;
	    if (path != null) {
	    	bitmap = BitmapFactory.decodeFile(path, opts);
		}else if (uri != null) {
			ContentResolver cr = context.getContentResolver();  
            InputStream inputStream = null;  
            try {  
                inputStream = cr.openInputStream(uri);  
                bitmap = BitmapFactory.decodeStream(inputStream, null, opts);  
                inputStream.close();  
            } catch (Exception e) {  
                return null;
            }  
		}else{
			return null;
		}
	    
	    return compressImage(bitmap);
//	    return bitmap;
    }
      
      
    /** 
     * 获取合适的sampleSize. 
     * 这里就简单实现都是2的倍数啦. 
     * @param width 
     * @param target 
     * @return 
     */  
    private int sampleSize(int width, int target){             
            int result = 1;           
            for(int i = 0; i < 10; i++){               
                if(width < target * 2){  
                    break;  
                }                 
                width = width / 2;  
                result = result * 2;                  
            }             
            return result;  
        } 
    
    
    public static File compressImage(Bitmap image) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 60, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>300) {//循环判断如果压缩后图片是否大于30kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 20;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        
//        File f = new File(SystemUtil.getMeiTuCacheFolder(SystemUtil.CACHE_IMAGE_TYPE)+File.separator+System.currentTimeMillis()
//        		+".jpg");
//        if (f.exists()) {
//			f.delete();
//		}
//        FileOutputStream fOut = null;
//        try {
//        	f.createNewFile();
//        	fOut = new FileOutputStream(f);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//            fOut.flush();
//        } catch (Exception e) {
//        	Logger.e("ImageZipUtil", "catch", e.getMessage());
//        }finally{
//        	if (fOut != null) {
//        		try {
//					fOut.close();
//					bitmap.recycle();
//				} catch (IOException e) {
//					Logger.e("ImageZipUtil", "finally", e.getMessage());
//				}
//			}
//        }
        
        return saveFile(bitmap);  
    } 
    
    public static File saveFile(Bitmap bitmap){
    	File f = new File(rootPath+System.currentTimeMillis()+".jpg");
        if (f.exists()) {
			f.delete();
		}
    	FileOutputStream fOut = null;
        try {
        	f.createNewFile();
        	fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
        } catch (Exception e) {
        	Logger.e(TAG, "catch", e.getMessage());
        }finally{
        	if (fOut != null) {
        		try {
					fOut.close();
					bitmap.recycle();
				} catch (IOException e) {
					Logger.e(TAG, "finally", e.getMessage());
				}
			}
        }
        return f;  
    }
    
    public static File copyFile(String oldPath, String newPath) {   
    	File oldfile = new File(oldPath);   
    	File newFile = new File(newPath);
    	if (newFile.exists()) {
    		newFile.delete();
    	}
    	InputStream inStream = null;
    	FileOutputStream fs = null;
        try {   
            int byteread = 0;   
            if (oldfile.exists()) { //文件存在时   
            	inStream = new FileInputStream(oldPath); //读入原文件   
                fs = new FileOutputStream(newFile);   
                byte[] buffer = new byte[1024];   
                while ( (byteread = inStream.read(buffer)) != -1) {   
                    fs.write(buffer, 0, byteread);   
                }   
            }   
        }catch (Exception e) {   
        	Logger.e(TAG, "copy file error", e.getMessage());
        }finally{
    		try {
    			if (inStream != null) {
    				inStream.close();
    			}
    			if (fs != null) {
					fs.close();
				}
			} catch (IOException e) {
				Logger.e(TAG, "close stream error", e.getMessage());
			}   
        }
        return newFile;
    }   
   
    /**  
      * 复制整个文件夹内容  
      * @param oldPath String 原文件路径 如：c:/fqf  
      * @param newPath String 复制后路径 如：f:/fqf/ff  
      * @return boolean  
      */   
    public void copyFolder(String oldPath, String newPath) {   
   
        try {   
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹   
            File a=new File(oldPath);   
            String[] file=a.list();   
            File temp=null;   
            for (int i = 0; i < file.length; i++) {   
                if(oldPath.endsWith(File.separator)){   
                    temp=new File(oldPath+file[i]);   
                }   
                else{   
                    temp=new File(oldPath+File.separator+file[i]);   
                }   
   
                if(temp.isFile()){   
                    FileInputStream input = new FileInputStream(temp);   
                    FileOutputStream output = new FileOutputStream(newPath + "/" +   
                            (temp.getName()).toString());   
                    byte[] b = new byte[1024 * 5];   
                    int len;   
                    while ( (len = input.read(b)) != -1) {   
                        output.write(b, 0, len);   
                    }   
                    output.flush();   
                    output.close();   
                    input.close();   
                }   
                if(temp.isDirectory()){//如果是子文件夹   
                    copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);   
                }   
            }   
        }   
        catch (Exception e) {   
            System.out.println("复制整个文件夹内容操作出错");   
            e.printStackTrace();   
   
        }   
   
    }
    
//    private static FormatTools tools = new FormatTools();  
//    
//    public static FormatTools getInstance() {  
//        if (tools == null) {  
//            tools = new FormatTools();  
//            return tools;  
//        }  
//        return tools;  
//    }  
  
    // 将byte[]转换成InputStream  
    public  InputStream Byte2InputStream(byte[] b) {  
        ByteArrayInputStream bais = new ByteArrayInputStream(b);  
        return bais;  
    }  
  
    // 将InputStream转换成byte[]  
    public byte[] InputStream2Bytes(InputStream is) {  
        String str = "";  
        byte[] readByte = new byte[1024];  
        int readCount = -1;  
        try {  
            while ((readCount = is.read(readByte, 0, 1024)) != -1) {  
                str += new String(readByte).trim();  
            }  
            return str.getBytes();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    // 将Bitmap转换成InputStream  
    public static InputStream Bitmap2InputStream(Bitmap bm,CompressFormat type) {
    	//Bitmap.CompressFormat.JPEG
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bm.compress(type, 100, baos);  
        InputStream is = new ByteArrayInputStream(baos.toByteArray());  
        return is;  
    }  
  
    // 将Bitmap转换成InputStream  
//    public InputStream Bitmap2InputStream(Bitmap bm) {  
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);  
//        InputStream is = new ByteArrayInputStream(baos.toByteArray());  
//        return is;  
//    }  
  
    // 将InputStream转换成Bitmap  
    public Bitmap InputStream2Bitmap(InputStream is) {  
        return BitmapFactory.decodeStream(is);  
    }  
  
    // Drawable转换成InputStream  
//    public InputStream Drawable2InputStream(Drawable d) {  
//        Bitmap bitmap = this.drawable2Bitmap(d);  
//        return this.Bitmap2InputStream(bitmap);  
//    }  
  
    // InputStream转换成Drawable  
    public Drawable InputStream2Drawable(InputStream is) {  
        Bitmap bitmap = this.InputStream2Bitmap(is);  
        return this.bitmap2Drawable(bitmap);  
    }  
  
    // Drawable转换成byte[]  
//    public byte[] Drawable2Bytes(Drawable d) {  
//        Bitmap bitmap = this.drawable2Bitmap(d);  
//        return this.Bitmap2Bytes(bitmap,Bitmap.CompressFormat.PNG);  
//    }  
  
    // byte[]转换成Drawable  
    public Drawable Bytes2Drawable(byte[] b) {  
        Bitmap bitmap = this.Bytes2Bitmap(b);  
        return this.bitmap2Drawable(bitmap);  
    }  
  
    // Bitmap转换成byte[]  
    public static byte[] Bitmap2Bytes(Bitmap bm,CompressFormat type) { 
    	//Bitmap.CompressFormat.PNG
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bm.compress(type, 100, baos);  
        return baos.toByteArray();  
    }  
  
    // byte[]转换成Bitmap  
    public Bitmap Bytes2Bitmap(byte[] b) {  
        if (b.length != 0) {  
            return BitmapFactory.decodeByteArray(b, 0, b.length);  
        }  
        return null;  
    }  
  
    // Drawable转换成Bitmap  
    public Bitmap drawable2Bitmap(Drawable drawable) {  
        Bitmap bitmap = Bitmap  
                .createBitmap(  
                        drawable.getIntrinsicWidth(),  
                        drawable.getIntrinsicHeight(),  
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                                : Bitmap.Config.RGB_565);  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
                drawable.getIntrinsicHeight());  
        drawable.draw(canvas);  
        return bitmap;  
    }  
  
    // Bitmap转换成Drawable  
    public Drawable bitmap2Drawable(Bitmap bitmap) {  
        BitmapDrawable bd = new BitmapDrawable(bitmap);  
        Drawable d = (Drawable) bd;  
        return d;  
    } 
}
