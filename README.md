# ShareToMe

# Parsing the information data shared with my application

## 解析分享到我的应用的信息数据 支持 图片 多组图片 明信片 文字
### android.intent.action.SEND 

```xml
    
        <intent-filter>
                <action android:name="android.intent.action.SEND" />

                <category android:name="android.intent.category.DEFAULT" />

                <data android:mimeType="image/*" />
         </intent-filter>
         ....
         ....
    
```

### 解析的全部类型参考AndroidManifest.xml
### Refer to it for all types of parsing AndroidManifest.xml


### 使用很简单

### 在nCreate 或者 onResume 中执行此方法 分享内容的解析结果就会通接口返回
```java
ShareToMe.handleShareToMe(this, getIntent(), new ShareToMe.ShareCallBack() {
            /**
             *
             * @param type  Original Sharing Type image/* ,text/plain ,text/x-vcard<p/>
             */
            @Override
            public void callType(String type) {
                Log.d("Dboy", "type =>" + type);
            }

            /**
             * @param shareData 处理分享数据的实体基类
             */
            @Override
            public void onSuccess(BaseShareData shareData) {
                switch (shareData.getDataType()) {
                    case SHARE_IMG_DATA:
                        //多组图片分享
                        ShareMultipleImagesData shareMultipleImagesData = shareData.getMultipleImagesData();
                        break;
                    case SHARE_MULTIPLE_IMAGES_DATA:
                        //单个图片分享
                        ShareImageData shareImageData = shareData.getImageData();
                        break;
                    case SHARE_TEXT_DATA:
                        //文本分享
                        ShareTextData textData = shareData.getTextData();
                        break;
                    case SHARE_VCARD_DATA:
                        //明信片分享
                        ShareVCardData vCardData = shareData.getVCardData();
                        break;
                }
            }


            @Override
            public void onError(String e) {
                Log.d("Dboy", "error =>" + e);
            }
        })

```

    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

    dependencies {
	        implementation 'com.github.Dboy233:ShareToMe:3.1.0'
	}
    
