# ShareToMe

# Parsing the information data shared with my application

## 解析分享到我的应用的信息数据
### android.intent.action.SEND 

``xml``
    
        <intent-filter>
                <action android:name="android.intent.action.SEND" />

                <category android:name="android.intent.category.DEFAULT" />

                <data android:mimeType="image/*" />
         </intent-filter>
         ....
         ....
    
``xml``

### 解析的全部类型参考AndroidManifest.xml
### Refer to it for all types of parsing AndroidManifest.xml

``java``

    ShareToMe.handleShareToMe(this, getIntent(), new ShareToMe.HandleListener() {

            @Override
            public void handleType(String type) {
                Log.d("Dboy", "type =>" + type);
            }

            @Override
            public void handleContent(BaseShareData shareData) {
                Log.d("Dboy", "data =>" + shareData.toString());
            }

            @Override
            public void handleError(String e) {
                Log.d("Dboy", "error =>" + e);
            }
        });

``java``


    dependencies {
	        implementation 'com.github.Dboy233:ShareToMe:Tag'
	}
    

#  Are there any partners willing to work together to complete the project?
# 有没有愿意一起完成此项目的合作者。
## mailbox 894230813@qq.com
