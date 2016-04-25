```
public void updateHead(String path, final OnUpdateHeadListener onUpdateHeadListener) {
        File file = new File(path);
        if (!file.exists() || file.length() > MAX_SIZE) {
            onUpdateHeadListener.onError("图片大小不能超过1M");
            return;
        }
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_POST, Api.UPDATE_HEAD_PHOTO, new StringHttpTask.HttpExecuteLinstener<String>() {

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

```
public void updateInfo(String introduce, String name, String type, final OnUpdateInfoListener onUpdateInfoListener) {
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_POST, Api.UPDATE_USER, new StringHttpTask.HttpExecuteLinstener<String>() {

            @Override
            public void onSuccess(String result) {
                Log.v(UserModelImpl.class, "result=" + result);
                onUpdateInfoListener.onSuccess();
            }

            @Override
            public void onError(int code, String msg) {
                onUpdateInfoListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        task.addParam("introduce", introduce).addParam("name", name);
        HttpService.instance().execute(task);
}
```

```
public void delUser(String studentId, final OnCommonListener onCommonListener) {
        HttpTask task = StringHttpTask.create(HttpTask.Method.EXECUTE_TYPE_POST, Api.DEL_USER, new StringHttpTask.HttpExecuteLinstener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.v(UserModelImpl.class, "result=" + result);
                if (result.contains(Api.ERROR_JSON)) {
                    onCommonListener.onError(MyApplication.instance().getApplicationContext().getResources().getString(R.string.Service_Error));
                } else {
                    onCommonListener.onSuccess("已删除");
                }
            }

            @Override
            public void onError(int code, String msg) {
                onCommonListener.onError(msg + ":" + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
        task.addParam("studentId", studentId);
        HttpService.instance().execute(task);
}
```
