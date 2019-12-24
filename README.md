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

    ShareToMe.handleShareToMe(this, getIntent(), new ShareToMe.HandleListener() {

            @Override
            public void handleType(String type) {
                Log.d("Dboy", "type =>" + type);
            }

            @Override
            public void handleContent(BaseShareData shareData) {
	    
                if (shareData.isMultipleImagesData()) {
		
                    ShareMultipleImagesData shareMultipleImagesData = shareData.getMultipleImagesData();
                    mBaseQuickAdapter.addData(shareMultipleImagesData.getImgPath());
		    
                } else if (shareData.isImageData()) {
		
                    ShareImageData shareImageData = shareData.getImageData();
                    viewById.setVisibility(View.VISIBLE);
                    String path = shareImageData.getPath();
                    Glide.with(getApplicationContext()).load(path).into(viewById);

                } else if (shareData.isTextData()){
		
                    ShareTextData textData = shareData.getTextData();

                } else if (shareData.isVCardData()) {
		
                    ShareVCardData vCardData = shareData.getVCardData();

                }
            }

            @Override
            public void handleError(String e) {
                Log.d("Dboy", "error =>" + e);
            }
        });

```

    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

    dependencies {
	        implementation 'com.github.Dboy233:ShareToMe:3.0'
	}
    

#  Are there any partners willing to work together to complete the project?
# 有没有愿意一起完成此项目的合作者。
## mailbox 894230813@qq.com
