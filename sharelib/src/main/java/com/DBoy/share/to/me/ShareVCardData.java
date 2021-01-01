package com.DBoy.share.to.me;

import androidx.annotation.NonNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ezvcard.VCard;

import static com.DBoy.share.to.me.BaseShareData.ShareDataType.SHARE_VCARD_DATA;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ShareVCardData extends BaseShareData {
    /**
     * 分享内容
     */
    private String content;
    /**
     * VCard
     */
    private VCard mVCard;
    /**
     * 字节
     */
    private byte[] vCardBt;


    public ShareVCardData(String content, VCard VCard, byte[] vCardBt) {
        this.content = content;
        this.mVCard = VCard;
        this.vCardBt = vCardBt;
    }


    public byte[] getVCardBt() {
        return vCardBt;
    }

    public void setVCardBt(byte[] vCardBt) {
        this.vCardBt = vCardBt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public VCard getVCard() {
        return mVCard;
    }

    public void setVCard(VCard VCard) {
        mVCard = VCard;
    }

    /**
     * 获取一个文件构建器
     */
    public FileBuilder getFileBuilder(){
        return new FileBuilder().setFileBytes(getVCardBt());
    }

    @Override
    public ShareDataType getDataType() {
        return SHARE_VCARD_DATA;
    }


    @NonNull
    @Override
    public String toString() {
        return content;
    }

    /**
     * 将VCard制作成文件vcf格式的文件
     */
    public static class FileBuilder {
        /**
         * 写入的字节
         */
        private byte[] fileBytes;
        /**
         * 文件名字
         */
        private String fileName;
        /**
         * 保存路径
         */
        private String path;

        public FileBuilder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public FileBuilder setPath(String path) {
            this.path = path;
            return this;
        }

        public FileBuilder setFileBytes(byte[] fileBytes) {
            this.fileBytes = fileBytes;
            return this;
        }

        /**
         * 创建File对象，其中包含文件所在的目录以及文件的命名
         */
        public File createFile() {
            if (path == null) {
                throw new NullPointerException("path = null");
            }
            if (fileName == null) {
                throw new NullPointerException("fileName = null");
            }
            if (fileBytes == null) {
                throw new NullPointerException("fileBytes = null");
            }
            File file = new File(path, fileName);
            // 创建FileOutputStream对象
            FileOutputStream outputStream = null;
            // 创建BufferedOutputStream对象
            BufferedOutputStream bufferedOutputStream = null;
            try {
                // 如果文件存在则删除
                if (file.exists()) {
                    file.delete();
                }
                // 在文件系统中根据路径创建一个新的空文件
                file.createNewFile();
                // 获取FileOutputStream对象
                outputStream = new FileOutputStream(file);
                // 获取BufferedOutputStream对象
                bufferedOutputStream = new BufferedOutputStream(outputStream);
                // 往文件所在的缓冲输出流中写byte数据
                bufferedOutputStream.write(fileBytes);
                // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
                bufferedOutputStream.flush();
            } catch (Exception e) {
                // 打印异常信息
                e.printStackTrace();
            } finally {
                // 关闭创建的流对象
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            return file;
        }

    }

}
