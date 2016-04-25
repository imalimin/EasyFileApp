```
public void updateHead(String path, final OnUpdateHeadListener onUpdateHeadListener) {
        File file = new File(path);
        if (!file.exists() || file.length() > MAX_SIZE) {
            onUpdateHeadListener.onError("图片大小不能超过1M");
            return;
        }
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_POST, Api.UPDATE_HEAD_PHOTO
        , new StringHttpTask.HttpExecuteLinstener<String>() {

            @Override
            public void onSuccess(String result) {
                Log.v(UserModelImpl.class, "result=" + result);
                onUpdateHeadListener.onSuccess(result);
            }

            @Override
            public void onError(int code, String msg) {
                onUpdateHeadListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        task.addFile("file", file);
        HttpService.instance().execute(task);
    }
```
