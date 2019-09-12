# ShareToMe

# Parsing the information data shared with my application

## 解析分享到我的应用的信息数据


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
